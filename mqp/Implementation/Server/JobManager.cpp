// JobManager.cpp
//

#include "stdafx.h"
#include "JobManager.h"
#include "Job.h"
#include "SimpleDateTime.h"

JobManager::JobManager() 
{	
	newJobNumber = 0;	
}

int JobManager::addJob(long joblength, CSemaphore* new_semaphore)
{
    int result;
	Job job;
	lock();
	result = newJobNumber++;
	job.jobnumber = result;
	job.length = joblength;
	job.semaphore = new_semaphore;
	job.timeStarted = NULL;
	if (jobs.empty()) {
		// the new job will be started right away:
		job.timeStarted = new SimpleDateTime();
		if (job.semaphore != NULL) job.semaphore->Unlock();
	}
	jobs.push_back(job);
	unlock();
	return result;
}

void JobManager::deleteJob(int jobnumber)
{
	vector<Job>::iterator i;
	lock();
	for (i=jobs.begin(); i!=jobs.end(); ++i) {
		if (i->jobnumber == jobnumber) {
			// delete the job and its data members
			if (i->timeStarted!=NULL) delete i->timeStarted;
			jobs.erase(i);
			// check if a job in the front needs to be woken-up:
			if (!jobs.empty()) {
				i=jobs.begin();
				if (i->timeStarted==NULL) {
					// yes it does:
					i->timeStarted = new SimpleDateTime();
					if (i->semaphore!=NULL) i->semaphore->Unlock();
				}
			}
			unlock();		
			return;
		}
	}
	unlock();
}

long JobManager::getTotalQueueTime()
{
	long result = 0;	
	SimpleDateTime *time;
	vector<Job>::iterator i;		
	lock();
	for (i=jobs.begin(); i!=jobs.end(); ++i) {	
		if (i->timeStarted == NULL) 
			// this is a job that hasn't been started yet:
			result += i->length;
		else {
			// job has been started
			time = new SimpleDateTime();
			long timediff = *time - i->timeStarted;
			if (timediff >= i->length) 
				// it's running longer then expected:
				result += timediff;
			else
				// it's still within it's expected running time:
				result += i->length;	
			delete time;
		}
	}
	unlock();
    return result;
}

boolean JobManager::isEmpty() { return (jobs.empty()); }

void JobManager::lock() { managerLock.Lock(); }

void JobManager::unlock() { managerLock.Unlock(); }

void JobManager::log(CString event) {	
	char hid[30];
	sprintf(hid, "[%d],", (long) this);	
	if (logContext!=NULL) logContext->addEntry("JOB MANAGER", hid , event);
}

JobManager::~JobManager() 
{	
	lock();
	for (int i=0; i<jobs.size(); i++) 
		if (jobs[i].timeStarted!=NULL) delete jobs[i].timeStarted;
	unlock();
}

