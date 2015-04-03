// GameTreeNode.cpp
//

#include "stdafx.h"
#include "GameTreeNode.h"
#include "MoveStatus.h"
#include "GameServerExceptions.h"

GameTreeNode::GameTreeNode()
{
	// initiate the node to the most typical settings so that we have to do the 
	// least when creating a game tree node:
	ply = 0;
	level = MAXIMAXING;
    processingDone = false;
    consideredChildren = false;
    evaluationFailed = false;
    pctCompleted = 0;
    parentNode = NULL;
    childrenNodes.clear();
    doneChildProcessings = 0;
	lastMove.gamestate.gamestate = NULL;
	lastMove.move = NULL;
	lastMove.quality = 0;
	lastMove.status = MOVEDECISION_CONTINUE;
	processingSemaphore = NULL;
}

void GameTreeNode::addChildNode(GameTreeNode* childnode)
{
	childrenNodes.push_back(childnode);
}

vector<GameTreeNode*> GameTreeNode::getChildNodes()
{
    return childrenNodes;
}

int GameTreeNode::getPly()
{
    return ply;
}

void GameTreeNode::setPly(int new_ply)
{
	ply = new_ply;
}

GameState GameTreeNode::getGameState()
{
	return lastMove.gamestate;
}

void GameTreeNode::setGameState(const GameState& new_gamestate)
{
	if (lastMove.gamestate.gamestate!=NULL) delete (char*)lastMove.gamestate.gamestate;
	lastMove.gamestate.gamestate = (unsigned char*) new char[strlen((char*)new_gamestate.gamestate)+1];
	strcpy((char*)lastMove.gamestate.gamestate, (char*)new_gamestate.gamestate);
}

LevelType GameTreeNode::getLevel()
{	
	return level;
}

void GameTreeNode::setLevel(LevelType new_level)
{
	level = new_level;
}

Move GameTreeNode::getLastMove()
{
	return lastMove;
}

void GameTreeNode::setLastMove(const Move& new_lastmove)
{
	if (lastMove.gamestate.gamestate!=NULL) delete (char*)lastMove.gamestate.gamestate;
	lastMove.gamestate.gamestate = (unsigned char*) new char[strlen((char*)new_lastmove.gamestate.gamestate)+1];
	strcpy((char*)lastMove.gamestate.gamestate, (char*)new_lastmove.gamestate.gamestate);
	
	if (lastMove.move!=NULL) delete (char*)lastMove.move;
	lastMove.move = (unsigned char*) new char[strlen((char*)new_lastmove.move)+1];
	strcpy((char*)lastMove.move, (char*)new_lastmove.move);

	lastMove.quality = new_lastmove.quality;
	lastMove.status = new_lastmove.status;
}

int GameTreeNode::getQuality()
{
    return lastMove.quality;
}

void GameTreeNode::setQuality(int new_quality)
{
	lastMove.quality = new_quality;
}

GameTreeNode* GameTreeNode::getParentNode()
{
    return parentNode;
}

void GameTreeNode::setParentNode(GameTreeNode* new_parentnode)
{
	parentNode = new_parentnode;
}

boolean GameTreeNode::isProcessingDone()
{
	return (doneChildProcessings == childrenNodes.size());
}

void GameTreeNode::markProcessingDone()
{
	processingDone = true;
	if (processingSemaphore!=NULL) {
		processingSemaphore->Unlock();
		processingSemaphore = NULL;
	}
	if (parentNode!=NULL) parentNode->markChildProcessingDone(this->lastMove.quality);
}

void GameTreeNode::markChildProcessingDone(int quality)
{
	boolean completed_all_children = false;
	doneChildProcessingsLock.Lock();
	doneChildProcessings++;
	pctCompleted = pctCompleted + (1 / childrenNodes.size());
	if (level == MAXIMAXING) {
		if ((quality > lastMove.quality) || (consideredChildren == false)){
			lastMove.quality = quality;
			consideredChildren = true;
		}
	} else {
		if ((quality < lastMove.quality) || (consideredChildren == false)){
			lastMove.quality = quality;
			consideredChildren = true;
		}
	}
	if (doneChildProcessings == childrenNodes.size()) completed_all_children = true;
	doneChildProcessingsLock.Unlock();
	if (completed_all_children) markProcessingDone();
}

boolean GameTreeNode::getEvaluationFailed()
{
	return this->evaluationFailed;
}

void GameTreeNode::setEvaluationFailed(boolean new_evaluationfailed)
{
	evaluationFailed = new_evaluationfailed;
}

float GameTreeNode::getPctCompleted()
{
    return pctCompleted;
}

void GameTreeNode::setPctCompleted(float new_pctcompleted)
{
	pctCompleted = new_pctcompleted;
}

MoveDecision GameTreeNode::generateDecision()
{
	MoveDecision result;
	result.pctCompleted = 0;
	if ((childrenNodes.size()==0) || (getEvaluationFailed())) throw new NodeEvaluationFailedException();
	int max_quality = childrenNodes[0]->getQuality();
	result.gamestatus = childrenNodes[0]->getLastMove().status;
	result.move = (char*) childrenNodes[0]->getLastMove().move;	

	// depending on what level we are on, we might do different thing:

	if (level==MAXIMAXING) {
		for (int a=0; a<childrenNodes.size(); a++) {
			result.pctCompleted += childrenNodes[a]->getPctCompleted();
			if (max_quality < childrenNodes[a]->getQuality()) {
				// this node has a higher quality:
				max_quality = childrenNodes[a]->getQuality();
				result.gamestatus = childrenNodes[a]->getLastMove().status;
				result.move = (char*) childrenNodes[a]->getLastMove().move;			
			}
		}
	} else {
		for (int a=0; a<childrenNodes.size(); a++) {
			result.pctCompleted += childrenNodes[a]->getPctCompleted();
			if (max_quality > childrenNodes[a]->getQuality()) {
				// this node has a lower quality:
				max_quality = childrenNodes[a]->getQuality();
				result.gamestatus = childrenNodes[a]->getLastMove().status;
				result.move = (char*) childrenNodes[a]->getLastMove().move;			
			}
		}
	}
	result.pctCompleted /= childrenNodes.size();
 	return result;
}

void GameTreeNode::setProcessingSemaphore(CSemaphore* new_processingSemaphore)
{ processingSemaphore = new_processingSemaphore; }


GameTreeNode::~GameTreeNode()
{
	if (lastMove.gamestate.gamestate != NULL) delete lastMove.gamestate.gamestate;
	if (lastMove.move != NULL) delete lastMove.move;
	for (int a=0; a<childrenNodes.size(); a++) delete childrenNodes[a];
}


	

