// HelperRegistry.cpp
//

#include "stdafx.h"
#include "HelperRegistry.h"
#include "GameType.h"
#include "InternetAddress.h"
#include "Helper.h"

// TODOS:
//	- make returnHelpers() and unregisterHelper() remove the helpers if they are dead and have no references.

HelperRegistry::HelperRegistry()
{	
	helpersListLock.Lock();
	nextHelperID = 0;
	helpersListLock.Unlock();
}

vector<Helper*> HelperRegistry::getHelpers(GameType gametype)
{
	helpersListLock.Lock();
	vector<Helper*>				result;	
	vector<Helper*>::iterator	ir;
	vector<int>::iterator		ic;
	Helper*						helper;
	ir = helpers.begin(); 
	ic = refereceCounts.begin();
	while (ir!=helpers.end()   &&   ic!=refereceCounts.end()) {		
		helper = *ir;		
		if (helper->getGameType()==gametype) {
			*ic = *ic + 1;			
			result.push_back(helper);
		}
		ir++;
		ic++;
	}; 
	helpersListLock.Unlock();
    return result;
}

vector<GameType> HelperRegistry::getAllGameTypes()
{
	helpersListLock.Lock();
	vector<GameType> result;	
	GameType gt;
	vector<GameType>::iterator	ig;
	vector<Helper*>::iterator	ir;
	vector<int>::iterator		ic;
	Helper*						helper;
	ir = helpers.begin(); 
	ic = refereceCounts.begin();
	while (ir!=helpers.end()   &&   ic!=refereceCounts.end()) {		
		helper = *ir;		
		if (helper->getAvailable()) {
			gt = helper->getGameType();
			boolean found = false;
			for (ig = result.begin(); ig != result.end(); ++ig) {				
				if ((*ig)==gt) found = true;
			}
			if (!found) result.push_back(gt);
		}
		ir++;
		ic++;
	}; 
	helpersListLock.Unlock();
	return result;
}

boolean HelperRegistry::isGameImplemented(const GameType& gametype)
{
	helpersListLock.Lock();
	vector<Helper*>::iterator	ir;
	vector<int>::iterator		ic;
	Helper*						helper;
	ir = helpers.begin(); 
	ic = refereceCounts.begin();
	while (ir!=helpers.end()   &&   ic!=refereceCounts.end()) {		
		helper = *ir;		
		if (helper->getAvailable()) {
			if (helper->getGameType()==gametype) 
			{
				helpersListLock.Unlock();
				return true;
			}
		}
		ir++;
		ic++;
	}; 
	helpersListLock.Unlock();
	return false;
}

void HelperRegistry::returnHelpers(vector<Helper*> returnedhelpers)
{
	helpersListLock.Lock();
	vector<Helper*>::iterator	ir;
	vector<Helper*>::iterator	ir_ret;
	vector<int>::iterator		ic;
	Helper*						helper;
	Helper*						helper_ret;
	ir_ret = returnedhelpers.begin(); 
	while (ir_ret!=returnedhelpers.end()) {		
		helper_ret = *ir_ret;		
		ir = helpers.begin(); 
		ic = refereceCounts.begin();
		boolean found = false;
		while (ir!=helpers.end()   &&   !found) {		
			helper = *ir;		
			if (helper->getHelperID() == helper_ret->getHelperID()) {
				*ic = *ic - 1;
				found = true;
			}
			ir++;
			ic++;
		}; 
		
		ir_ret++;
	}; 
	helpersListLock.Unlock();
}

HelperRegistry::~HelperRegistry()
{		
	// loop until all references are returned:
	boolean allReturned;
	int a;
	do {
		allReturned = true;
		helpersListLock.Lock();		
		for (a = 0; a<refereceCounts.size(); a++) 
			if (refereceCounts[a]>0) allReturned = false;
		if (allReturned) {
			for (a = 0; a < helpers.size(); a++) {
				Helper* tmpHelper = helpers[a];			
				delete tmpHelper;
			}
			return;
		}
		helpersListLock.Unlock();		
		::Sleep(100);
	} while (!allReturned); 	
}


void HelperRegistry::registerHelper(unsigned char *address, GameTypeData data)
{	
	unregisterHelper(address);
	helpersListLock.Lock();
	GameType gt(data);	
	InternetAddress ha;
	if (strlen((char*) address)>255) return;
	strcpy(ha.ipAddress, (char*) address);
	Helper* h1 = new Helper();
	h1->setGameType(gt);
	h1->setHelperID(nextHelperID++);
	h1->setAddress(ha);
	h1->setAvailable(true);
	h1->setLogContext(this->logContext);
	helpers.push_back(h1);
	refereceCounts.push_back(0);
	helpersListLock.Unlock();
	CString tmpEvent;
	tmpEvent = "Registered helper ";
	tmpEvent = tmpEvent + ha.ipAddress;
	tmpEvent = tmpEvent + " (";
	tmpEvent = tmpEvent + gt.getGameName() + ", " + gt.getGameEncoding() + ")";
	log (tmpEvent);
}

void HelperRegistry::unregisterHelper(unsigned char *address)
{
	helpersListLock.Lock();
	vector<Helper*>::iterator	ir;
	vector<int>::iterator		ic;
	Helper*						helper;
	CString tmpHelperInfo;
	ir = helpers.begin(); 
	ic = refereceCounts.begin();
	while (ir!=helpers.end()   &&   ic!=refereceCounts.end()) {		
		helper = *ir;		
		if (!strcmp((char*)helper->getAddress().ipAddress,(char*) address)) {
			tmpHelperInfo = helper->getAddress().ipAddress;
			tmpHelperInfo = tmpHelperInfo + " (";
			tmpHelperInfo = tmpHelperInfo + helper->getGameType().getGameName();
			tmpHelperInfo = tmpHelperInfo + ", ";
			tmpHelperInfo = tmpHelperInfo + helper->getGameType().getGameEncoding() + ")";
			helper->setAvailable(false);				
			helpersListLock.Unlock();
			return;
		}
		ir++;
		ic++;
	}; 
	helpersListLock.Unlock();
	CString tmpEvent = "Unregistered helper " + tmpHelperInfo;
	log (tmpEvent);
}

void HelperRegistry::setLogContext(LogContext* new_logContext) {
	logContext = new_logContext;
	helpersListLock.Lock();		
		for (int a = 0; a < helpers.size(); a++) {
		Helper* tmpHelper = helpers[a];			
		tmpHelper->setLogContext(new_logContext);
	}
	helpersListLock.Unlock();
}


void HelperRegistry::log(CString event) {
	char hid[30];
	sprintf(hid, "[%d],", (long) this);
	CString tmp = hid;
	if (logContext!=NULL) logContext->addEntry("HELPER REGISTRY", tmp , event);
}