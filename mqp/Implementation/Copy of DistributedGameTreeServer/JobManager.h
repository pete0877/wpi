// JobManager.h
//

#if !defined(JobManager_h)
#define JobManager_h

/*
 * JobManager Class is used by each helper to put in jobs of
 * particular lenghts into its schedule as well as to figure out
 * how much time will elapse before the helper is idle.
 */

#include "resource.h"
#include <afxmt.h>
#include "Job.h"
#include "LogContext.h"


class JobManager
{

public:

	/*
     * JobManager() - Constructor. Sets the job id counter at 0.
     */
    JobManager();

	/*
     * addJob() - adds new job of given lenght to the queue and sets
	 *	it's wake-up semaphore. The semaphore will be woken up when
	 *	it's time to do the job.
     */
    int addJob(long joblength, CSemaphore* new_semaphore);

	/*
     * deleteJob() - deletes the matching job from the queue. If
	 *	the job was on the front of the queue, next job in-line will
	 *	be woken up.
     */
    void deleteJob(int jobnumber);

	/*
     * getTotalQueueTime() - estimates time in milisec that will elapse before
	 *  the queue is emptied.
     */
    long getTotalQueueTime();

	/*
     * isEmpty() - returns true only if there are no jobs present in the queue.
     */
    boolean isEmpty();

	void setLogContext(LogContext* new_logContext);

	/*
     * ~JobManager() - destructor.
     */
	~JobManager();

protected:
	LogContext*					logContext;
	void						log(CString event);
    void lock();					// locks managerLock
    void unlock();					// unlocks managerLock
    vector<Job>	jobs;				// list of currently scheduled jobs
    int	newJobNumber;				// next job id number to be assigned 
    CCriticalSection managerLock;	// thread synchronization variable

};

#endif /* JobManager_h */
