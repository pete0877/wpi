// Engine.cpp
//

#include "stdafx.h"
#include "Engine.h"
#include "GameType.h"
#include "SimpleDateTime.h"
#include "Helper.h"
#include "GameServerExceptions.h"
#include "Types.h"

Engine::Engine()
{
	helperRegistry = new HelperRegistry();
	algorithm = NULL;	
	logContext = NULL;
}

Engine::Engine(const Engine& new_engine)
{
	this->setConfiguration(new_engine.configuration);
	helperRegistry	= new HelperRegistry();
	algorithm		= new_engine.algorithm;
	logContext		= new_engine.logContext;
}

Engine::Engine(const EngineConfiguration& new_configuration)
{
	this->setConfiguration(new_configuration);
	helperRegistry	= new HelperRegistry();
	algorithm		= NULL;
	logContext		= NULL;
}

EngineConfiguration Engine::getConfiguration()
{	
	return configuration;
}

void Engine::setConfiguration(const EngineConfiguration& new_configuration)
{
	configuration = new_configuration;
}

boolean Engine::isGameImplemented(const GameType& gametype)
{	
	return helperRegistry->isGameImplemented(gametype);
}

vector<GameType> Engine::getAllGameTypes()
{
	return helperRegistry->getAllGameTypes();
}

long Engine::estimateMoveTime(const GameType& gametype, int ply)
{
	if (algorithm==NULL) throw new GameNotSupportedException();
	// TODO: Make this function real:
	// ...
    return (long)60000;
}

MoveDecision Engine::makeMove(const GameState& gamestate, const GameType& gametype, int ply, SimpleDateTime* deadline)
{	
	if (algorithm==NULL) throw new GameNotSupportedException();
	MoveDecision result;		

	vector<Helper*> helperSet = helperRegistry->getHelpers(gametype);
	if (helperSet.size()==0) throw new GameNotSupportedException();
	int a = 0;
	boolean got_decision = false;
	Move* rapidMove;
	Helper* decisionHelper;
	do {
		decisionHelper = helperSet[a];		
		try {
			rapidMove = decisionHelper->makeRapidDesicion(gamestate);
			// now we know that the decision was made. if rapidMove is NULL, 
			// then we have to evaluate. Otherwise, we simply return the move.
			got_decision = true;
		} catch (HelperDeadException* the_exception) {
			the_exception->Delete();
			a++;
			if (a>=helperSet.size()) {
				// no more supprting helpers
				// so we return the helpers and let the calling module know about
				// the boo-boo:
				helperRegistry->returnHelpers(helperSet);
				throw new GameNotSupportedException();
			}
		} catch (GameOverException* the_exception) {
			// so we return the helpers and let the calling module know about
			// the boo-boo:
			helperRegistry->returnHelpers(helperSet);
			throw the_exception;
		}
	} while (!got_decision);

	// see what has been decided:
	if (rapidMove==NULL) {
		// tree evaluation:
		// create new job semaphore:
		CSemaphore* evaluationJobSem = new CSemaphore();
		evaluationJobSem->Lock();  // reset the counter to 0
		// create the root node:
		GameTreeNode* root = new GameTreeNode();
		root->setPly(ply);
		root->setGameState(gamestate);
		root->setLevel(MAXIMAXING);					
		root->setProcessingSemaphore(evaluationJobSem);

		// span new thread on the distribution algorithm:		
		algorithm->start((GameTreeNode*) root, helperSet, deadline);

		// wait till the algorithm is done:		
		evaluationJobSem->Lock(); 
		// once we get out of the lock .. we know that no other threads
		// are accessing the tree.

		delete evaluationJobSem;
		// once we get unlocked, we can now try to get the final decision:
		try {
			// attempt to genereate the resulting move:
			result = root->generateDecision();
			helperRegistry->returnHelpers(helperSet);
			delete root;
		} catch (GameOverException* the_exception) {			
			delete root;
			helperRegistry->returnHelpers(helperSet);
			throw the_exception;
		} catch (NodeEvaluationFailedException* the_exception) {
			the_exception->Delete();			
			delete root;
			helperRegistry->returnHelpers(helperSet);
			throw new GameNotSupportedException();
		}
		return result;
	} else {
		// rapid move:
		result.pctCompleted = 100;
		result.move = rapidMove->move;
		result.gamestatus = rapidMove->status;	
		if (rapidMove->gamestate.gamestate!=NULL) delete [] rapidMove->gamestate.gamestate;
		if (rapidMove->move!=NULL) delete [] rapidMove->move;
		delete rapidMove;
		helperRegistry->returnHelpers(helperSet);
		return result; 
	}
}


HelperRegistry* Engine::getHelperRegistry() 
{
	return helperRegistry;
}

Engine::~Engine()
{
	delete helperRegistry;
}

void Engine::setDistributionAlgorithm(DistributionAlgorithm *new_algorithm)
{
	algorithm = new_algorithm;
}

DistributionAlgorithm* Engine::getDistributionAlgorithm()
{
	return (DistributionAlgorithm*) algorithm;
}

void Engine::setLogContext(LogContext* new_logContext) {
	logContext = new_logContext;
	helperRegistry->setLogContext(new_logContext);
}


void Engine::log(CString event) {
	char instanceID[30];
	sprintf(instanceID, "[%d]", (long) this);
	if (logContext!=NULL) logContext->addEntry("ENGINE", instanceID, event);
}
