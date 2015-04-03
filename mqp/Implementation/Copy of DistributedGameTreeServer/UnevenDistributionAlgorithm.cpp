// UnevenDistributionAlgorithm.cpp: implementation of the UnevenDistributionAlgorithm class.
//
//////////////////////////////////////////////////////////////////////

#include "stdafx.h"
#include "DistributedGameTreeServer.h"
#include "UnevenDistributionAlgorithm.h"

#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__;
#define new DEBUG_NEW
#endif

UINT UnevenDistributionAlgorithm::run(LPVOID pParam)
{	
	DistributionAlgorithmParameters* params = (DistributionAlgorithmParameters*) pParam;
	GameTreeNode*		node		= params->node;
	vector<Helper*>		helpers		= params->helpers;
	SimpleDateTime*		deadline	= params->deadline;
	delete params;

	srand(123);
	Helper* chosenHelper = helpers[0];
	chosenHelper->splitBoard(node); 
	SimpleDateTime nowTime;
	vector<GameTreeNode*> kids = node->getChildNodes();
	GameTreeNode* childNode;
	for (int a=0; a<kids.size(); a++) {
		childNode = kids[a];
		nowTime.setToNow();
		chosenHelper->evaluateBoard(childNode, *deadline - nowTime);
		childNode->markProcessingDone();
	}

	return (UINT)0;
}


void UnevenDistributionAlgorithm::start(GameTreeNode* node, vector<Helper*> helpers, SimpleDateTime* deadline) 
{
	DistributionAlgorithmParameters* threadParams = new DistributionAlgorithmParameters;
	threadParams->node = node;
	threadParams->helpers = helpers;
	threadParams->deadline = deadline;
	// leave the de-allocation to the run() thread:
	// AfxBeginThread(run, (LPVOID) threadParams);
	run((LPVOID) threadParams);
}

void UnevenDistributionAlgorithm::setLogContext(LogContext* new_log) 
{
	this->logContext = new_log;
}

