// Helper.cpp
//

#include "stdafx.h"
#include "Helper.h"
#include "GameTreeNode.h"
#include "MoveStatus.h"

#include "Helper_i.h"
#include "Helper_i.c"
#include <comdef.h>


Helper::Helper()
{
	logContext = NULL;
}

Helper::Helper(const HelperPerformance& new_performance, const InternetAddress& new_address, const GameType& new_gametype)
{
}

HelperPerformance Helper::getPerformance()
{	
	return this->performance;
}

void Helper::setPerformance(const HelperPerformance& new_performance)
{
	this->performance = new_performance;
}

InternetAddress Helper::getAddress()
{	
	return address;
}

void Helper::setAddress(const InternetAddress& new_address)
{
	this->address = new_address;
}

GameType Helper::getGameType()
{
	return this->gametype;
}

void Helper::setGameType(const GameType& new_gametype)
{
	this->gametype = new_gametype;
}

boolean Helper::getAvailable()
{
	return this->available;
}

void Helper::setAvailable(boolean new_available)
{
	this->available = new_available;
}

long Helper::getHelperID()
{
    return this->helperID;
}

void Helper::setHelperID(long new_helperid)
{
	this->helperID = new_helperid;
}

long Helper::estimateEvaluationTurnaround(GameTreeNode* node)
{
	return estimateEvaluationTurnaround(node->getPly());
}

long Helper::estimateEvaluationTurnaround(int ply)
{
	return (performance.estimateEvaluationTime(ply) + jobManager.getTotalQueueTime());    
}

void Helper::evaluateBoard(GameTreeNode* node, long timelimit)
{
	// TODO: Make this a real DCom call:	
	int quality;
	float pctCompleted;
	SimpleDateTime timeBefore, timeAfter;
	HelperPerformanceData perfData;

	CSemaphore* jobLock = new CSemaphore();
	int jobNumber;
	jobLock->Lock(); // reset to 0
	jobNumber = jobManager.addJob(timelimit, jobLock);

	// now that the job has been added to the manager, we can 
	// unlock the manager. It has been locked before this function
	// has been called.
	unlockJobQueue();

	jobLock->Lock(); // wait till job manager unlocks the job semaphore:
	delete jobLock;

	timeBefore.setToNow();
	// here goes the DComcall		


	// Initiate DCOM:
	int gameStateSize = this->gametype.getGameStateSize();
	int moveSize	  = this->gametype.getMoveSize();
	int moveListSize  = this->gametype.getMoveListSize();

	IHelper *pHelper;
	HRESULT hr;

    COSERVERINFO cs;
    memset(&cs, 0, sizeof(cs));
    cs.pwszName = _bstr_t(_T(this->address.ipAddress)); 

    MULTI_QI qi[1];
    memset(qi, 0, sizeof(qi)); 

    qi[0].pIID = &IID_IHelper;
	#if _WIN32_WINNT >= 0x0400
		HRESULT hRes = CoInitializeEx(NULL, COINIT_MULTITHREADED);
	#else
		HRESULT hRes = CoInitialize(NULL);
	#endif
	hr = CoCreateInstanceEx(CLSID_Helper, NULL, CLSCTX_SERVER, &cs, 1, qi);

	if( FAILED(hr) ) {
		_com_error e(hr);
		afxDump << e.ErrorMessage();
	}

	pHelper = (IHelper *) qi[0].pItf;


	// Make the DCom call:
	GameState gs, gs_node;
	gs_node			= node->getGameState();
	gs				= gs_node;
	gs.gamestate	= new unsigned char[gameStateSize];
	strcpy((char *) gs.gamestate, (char *) gs_node.gamestate);
	gs.maxSize		= gameStateSize;
	gs.actualSize	= strlen((char *) gs.gamestate)+1;	;

	log("Making DCom call to the evaluation service");

	hr = pHelper->evaluate(gs, node->getLevel(), node->getPly(), timelimit, &quality, &perfData);
	if( FAILED(hr) ) {
		_com_error e(hr);
		afxDump << e.ErrorMessage();
	}

	log("Game state evaluated");

	pctCompleted = perfData.pctCompleted;

	// Release the helper and return the result
	pHelper->Release();
	CoUninitialize();
	
	// delete the job from the Job manager:
	jobManager.deleteJob(jobNumber);

	// calculate and update the performance:
	timeAfter.setToNow();
	long turnAround = timeAfter - timeBefore;
	// ...

	// mark the results:
	node->setQuality(quality);
	node->setPctCompleted(pctCompleted);

	delete gs.gamestate;
}

void Helper::splitBoard(GameTreeNode* node)
{
	// Initiate DCOM:
	int gameStateSize = this->gametype.getGameStateSize();
	int moveSize	  = this->gametype.getMoveSize();
	int moveListSize  = this->gametype.getMoveListSize();

	IHelper *pHelper;
	HRESULT hr;

    COSERVERINFO cs;
    memset(&cs, 0, sizeof(cs));
    cs.pwszName = _bstr_t(_T(this->address.ipAddress)); 

    MULTI_QI qi[1];
    memset(qi, 0, sizeof(qi)); 

    qi[0].pIID = &IID_IHelper;
	
	#if _WIN32_WINNT >= 0x0400
		HRESULT hRes = CoInitializeEx(NULL, COINIT_MULTITHREADED);
	#else
		HRESULT hRes = CoInitialize(NULL);
	#endif

	hr = CoCreateInstanceEx(CLSID_Helper, NULL, CLSCTX_SERVER, &cs, 1, qi);

	if( FAILED(hr) ) {
		_com_error e(hr);
		afxDump << e.ErrorMessage();
	}

	pHelper = (IHelper *) qi[0].pItf;

	// Make the DCom call:
	Move *moves = new Move[moveListSize];
	for(int i = 0; i < moveListSize; i++) {
		moves[i].maxSize = moveSize;
		moves[i].actualSize = 0;
		moves[i].move = new unsigned char[moveSize];
		moves[i].gamestate.maxSize = gameStateSize;
		moves[i].gamestate.actualSize = 0;
		moves[i].gamestate.gamestate = new unsigned char[gameStateSize];	
	}

	GameState gs, gs_node;
	gs_node			= node->getGameState();
	gs				= gs_node;
	gs.gamestate	= new unsigned char[gameStateSize];
	strcpy((char *) gs.gamestate, (char *) gs_node.gamestate);
	gs.maxSize		= gameStateSize;
	gs.actualSize	= strlen((char *) gs.gamestate)+1;	;

	log("Making DCom call to the split service");

	int actualSize = moveListSize;
	hr = pHelper->split(gs, node->getLevel(), moveListSize, &actualSize, moves);

	// make sure that all character arrays end with \0:
	for (int a=0; a<actualSize; a++) {
		moves[a].move[moveSize-1] = '\0';
		moves[a].gamestate.gamestate[gameStateSize-1] = '\0';
	}


	if( FAILED(hr) ) {
		_com_error e(hr);
		afxDump << e.ErrorMessage();
	}

	log("Game state splitted");

	// for each move in moves[], create new child node:
	GameTreeNode* tmpNode;
	for (a=0; a<actualSize; a++) {
		// create new node:
		tmpNode = new GameTreeNode();
		tmpNode->setLastMove(moves[a]); 

		// set the game tree level indicator:
		if (node->getLevel()==MAXIMAXING) tmpNode->setLevel(MINIMAZING); else tmpNode->setLevel(MAXIMAXING); 

		// set the parent node to the same node passed as argument:
		tmpNode->setParentNode(node);

		// play is one smaller then the ply of the parent node:
		tmpNode->setPly(node->getPly()-1);

		node->addChildNode(tmpNode);
	}

	// delallocate the array of moves:	
	for(i = 0; i < moveListSize; i++) {
		delete moves[i].move;
		delete moves[i].gamestate.gamestate;	
	}
	delete moves;
	delete gs.gamestate;

	// release the helper:
	pHelper->Release();
	CoUninitialize();
}

Move* Helper::makeRapidDesicion(const GameState& gamestate)
{
	// Initiate DCOM:
	int gameStateSize = this->gametype.getGameStateSize();
	int moveSize	  = this->gametype.getMoveSize();
	int moveListSize  = this->gametype.getMoveListSize();

	IHelper *pHelper;
	HRESULT hr;

    COSERVERINFO cs;
    memset(&cs, 0, sizeof(cs));
    cs.pwszName = _bstr_t(_T(this->address.ipAddress)); 

    MULTI_QI qi[1];
    memset(qi, 0, sizeof(qi)); 

    qi[0].pIID = &IID_IHelper;
	
	#if _WIN32_WINNT >= 0x0400
		HRESULT hRes = CoInitializeEx(NULL, COINIT_MULTITHREADED);
	#else
		HRESULT hRes = CoInitialize(NULL);
	#endif

	hr = CoCreateInstanceEx(CLSID_Helper, NULL, CLSCTX_SERVER, &cs, 1, qi);

	if( FAILED(hr) ) {
		_com_error e(hr);
		afxDump << e.ErrorMessage();
	}

	pHelper = (IHelper *) qi[0].pItf;

	// Make the DCOM call:
	GameState gs;
	gs.gamestate		= new unsigned char[gameStateSize];	
	gs.maxSize			= gameStateSize;	
	strcpy((char *) gs.gamestate, (char *) gamestate.gamestate);
	gs.actualSize		= strlen((char *) gs.gamestate)+1;	

	Move move;
	move.move					= new unsigned char[moveSize];
	move.gamestate.gamestate	= new unsigned char[gameStateSize];
	move.maxSize				= moveSize;
	move.actualSize				= 0;
	move.gamestate.maxSize		= gameStateSize;
	move.gamestate.actualSize	= 0;

	log("Making DCom call to the rapid move service");

	int actualSize = 1;	
	hr = pHelper->getQuickMove(gs, MAXIMAXING, 1, &actualSize, &move);
	actualSize = 0;

	// make sure that any received character strings end with \0:
	move.gamestate.gamestate[gameStateSize-1] = '\0';
	move.move[moveSize-1] = '\0';


	if( FAILED(hr) ) {
		_com_error e(hr);
		afxDump << e.ErrorMessage();
	}
	
	// Release the helper and return the result
	pHelper->Release();
	CoUninitialize();

	if (actualSize==0) {
		// no rapid move:
		delete [] gs.gamestate;
		delete [] move.move;
		delete [] move.gamestate.gamestate;
		log("No rapid move");
		return NULL;		
	} else {
		log("Rapid move found");
	}

	// there was a rapid move:	
	Move* tmpResult = new Move;
	tmpResult->gamestate.gamestate = new unsigned char[move.gamestate.actualSize + 1];
	strcpy ((char*) tmpResult->gamestate.gamestate, (char*)move.gamestate.gamestate);

	tmpResult->move = new unsigned char[move.actualSize];
	strcpy ((char*) tmpResult->move, (char*) move.move );
	tmpResult->quality = move.quality;
	tmpResult->status = move.status;

	delete [] gs.gamestate;
	delete [] move.move;
	delete [] move.gamestate.gamestate;
	return tmpResult;
}

void Helper::lockJobQueue()
{
	jobManagerLock.Lock();
}

void Helper::unlockJobQueue()
{
	jobManagerLock.Unlock();
}

Helper::~Helper()
{
}

void Helper::setLogContext(LogContext* new_logContext) {
	logContext = new_logContext;
}


void Helper::log(CString event) {	
	char hid[30];
	sprintf(hid, "[%d],", (long) this);
	CString tmp = hid;
	sprintf(hid, "%d,", helperID);
	tmp = tmp +  hid;
	tmp = tmp + address.ipAddress;
	if (logContext!=NULL) logContext->addEntry("HELPER", tmp , event);
}
