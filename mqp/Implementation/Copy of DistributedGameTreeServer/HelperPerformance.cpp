// HelperPerformance.cpp
//

#include "stdafx.h"
#include "HelperPerformance.h"
#include <math.h>

HelperPerformance::HelperPerformance()
{
	helperPerformanceData.alphaBetaGain = 0;
	helperPerformanceData.branchingFactor = 0;
	helperPerformanceData.evaluationTime = 0;
	helperPerformanceData.pctCompleted = 0;
	helperPerformanceData.splitTime = 0;
	helperPerformanceData.totalWorkTime = 0;	
	overHeadTime = 0;
}

HelperPerformance::HelperPerformance(const HelperPerformance& new_helperperformance)
{
	this->helperPerformanceData = new_helperperformance.helperPerformanceData;
	overHeadTime = 0;
	overHeadTime = new_helperperformance.overHeadTime;
}

HelperPerformance::HelperPerformance(const HelperPerformanceData& new_helperperformancedata)
{
	this->helperPerformanceData = new_helperperformancedata;
	overHeadTime = 0;
}

HelperPerformance::HelperPerformance(vector<HelperPerformance> new_helperperformance_list)
{
	float Avg_alphaBetaGain = 0;	
	float Avg_branchingFactor = 0;
	float Avg_evaluationTime = 0;
	float Avg_pctCompleted = 0;	
	float Avg_splitTime = 0;
	float Avg_totalWorkTime = 0;
	float Avg_overHeadTime = 0;

	// sum up all the data
	vector<HelperPerformance>::iterator i;
	int count = 0;
	for (i = new_helperperformance_list.begin(); i!= new_helperperformance_list.end(); ++i) {
		count++;
		Avg_alphaBetaGain		+= i->getAlphaBetaGain();
		Avg_branchingFactor		+= i->getBranchingFactor();
		Avg_evaluationTime		+= i->getEvaluationTime();
		Avg_pctCompleted		+= i->getPctCompleted();
		Avg_splitTime			+= i->getSplitTime();
		Avg_totalWorkTime		+= i->getTotalWorkTime();
		Avg_overHeadTime		+= i->getOverheadTime();
	}
	// compute averages:
	Avg_alphaBetaGain		= Avg_alphaBetaGain		/ count;
	Avg_branchingFactor		= Avg_branchingFactor	/ count;
	Avg_evaluationTime		= Avg_evaluationTime	/ count;
	Avg_pctCompleted		= Avg_pctCompleted		/ count;
	Avg_splitTime			= Avg_splitTime			/ count;
	Avg_totalWorkTime		= Avg_totalWorkTime		/ count;
	Avg_overHeadTime		= Avg_overHeadTime		/ count;

	// store averages:
	helperPerformanceData.alphaBetaGain		= Avg_alphaBetaGain;
	helperPerformanceData.branchingFactor	= Avg_branchingFactor;
	helperPerformanceData.evaluationTime	= Avg_evaluationTime;
	helperPerformanceData.pctCompleted		= Avg_pctCompleted;
	helperPerformanceData.splitTime			= Avg_splitTime;
	helperPerformanceData.totalWorkTime		= Avg_totalWorkTime;	
	overHeadTime							= (long) Avg_overHeadTime;
}

void HelperPerformance::update(const HelperPerformanceData& new_helperperformancedata, long new_turnaroundtime)
{
	lock();

	// average out the current performance data and new performance data:
	helperPerformanceData.evaluationTime	= (helperPerformanceData.evaluationTime		+ new_helperperformancedata.evaluationTime)		/ 2;
	helperPerformanceData.splitTime			= (helperPerformanceData.splitTime			+ new_helperperformancedata.splitTime)			/ 2;	
	helperPerformanceData.alphaBetaGain		= (helperPerformanceData.alphaBetaGain		+ new_helperperformancedata.alphaBetaGain)		/ 2;
	helperPerformanceData.branchingFactor	= (helperPerformanceData.branchingFactor	+ new_helperperformancedata.branchingFactor)	/ 2;	
	
	// record the completion factor and work time of the last evaluation:
	helperPerformanceData.pctCompleted		= new_helperperformancedata.pctCompleted;
	helperPerformanceData.totalWorkTime		= new_helperperformancedata.totalWorkTime;
	
	// calculate the overhead time:
	overHeadTime							= new_turnaroundtime - helperPerformanceData.totalWorkTime;

	unlock();
}

float HelperPerformance::getBranchingFactor() { return helperPerformanceData.branchingFactor; }

void HelperPerformance::setBranchingFactor(float new_branchingfactor) { 
	lock();	
	helperPerformanceData.branchingFactor = new_branchingfactor; 
	unlock();
}

float HelperPerformance::getOverheadTime() { return overHeadTime; }

void HelperPerformance::setOverheadTime(float new_overheadtime) { 
	lock();	
	overHeadTime = new_overheadtime; 
	unlock();	
}

float HelperPerformance::getEvaluationTime() { return helperPerformanceData.evaluationTime; }

void HelperPerformance::setEvaluationTime(float new_evaluationtime) { 
	lock();	
	helperPerformanceData.evaluationTime = new_evaluationtime; 
	unlock();
}

float HelperPerformance::getSplitTime() { return helperPerformanceData.splitTime; }

void HelperPerformance::setSplitTime(float new_splittime) { 
	lock();	
	helperPerformanceData.splitTime = new_splittime; 
	unlock();
}

float HelperPerformance::getPctCompleted() { return helperPerformanceData.pctCompleted; }

void HelperPerformance::setPctCompleted(float new_pctcompleted) { 
	lock();	
	helperPerformanceData.pctCompleted = new_pctcompleted; 
	unlock();
}

float HelperPerformance::getAlphaBetaGain() { return helperPerformanceData.alphaBetaGain; }

void HelperPerformance::setAlphaBetaGain(float new_alphabetagain) { 
	lock();	
	helperPerformanceData.alphaBetaGain = new_alphabetagain; 
	unlock();
}

float HelperPerformance::getTotalWorkTime() { return helperPerformanceData.totalWorkTime; }

void HelperPerformance::setTotalWorkTime(float new_worktime) { 
	lock();	
	helperPerformanceData.totalWorkTime = new_worktime; 
	unlock();
}

long HelperPerformance::estimateEvaluationTime(int ply)
{
	float result;
	float N		= ply;
	float B		= helperPerformanceData.branchingFactor;
	float V		= helperPerformanceData.evaluationTime;
	float S		= helperPerformanceData.splitTime;	
	float O		= overHeadTime;
	float AB	= helperPerformanceData.alphaBetaGain;
	result  = O;	
	result += V * (((pow(B, N	 ) - 1) / (B - 1)));
	result += S * (((pow(B, N - 1) - 1) / (B - 1)));
	result  = (AB * result);
    return (long) result;
}

void HelperPerformance::lock() { dataLock.Lock(); }

void HelperPerformance::unlock() { dataLock.Unlock(); }

const HelperPerformance& HelperPerformance::operator=(const HelperPerformance& new_helperperformance)
{	
	lock();	
	helperPerformanceData = new_helperperformance.helperPerformanceData;
	overHeadTime = new_helperperformance.overHeadTime;
	unlock();
	return *this;
}

boolean HelperPerformance::operator==(const HelperPerformance& new_helperperformance)
{
	if (new_helperperformance.helperPerformanceData.alphaBetaGain	!= helperPerformanceData.alphaBetaGain)		return false;
	if (new_helperperformance.helperPerformanceData.branchingFactor != helperPerformanceData.branchingFactor)	return false;
	if (new_helperperformance.helperPerformanceData.evaluationTime	!= helperPerformanceData.evaluationTime)	return false;
	if (new_helperperformance.helperPerformanceData.splitTime		!= helperPerformanceData.splitTime)			return false;
	if (new_helperperformance.helperPerformanceData.pctCompleted	!= helperPerformanceData.pctCompleted)		return false;
	if (new_helperperformance.helperPerformanceData.totalWorkTime	!= helperPerformanceData.totalWorkTime)		return false;
	if (new_helperperformance.overHeadTime != overHeadTime) return false;
	return true;
}



