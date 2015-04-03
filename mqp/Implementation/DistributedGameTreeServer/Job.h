// Job.h
//

#if !defined(Job_h)
#define Job_h

/*
 * Job Structure is a simple data type to store and track work
 * assigments.
 */
#include "stdafx.h"
#include "resource.h"
#include "SimpleDateTime.h"

typedef struct tagJob {
    SimpleDateTime	*timeStarted;
    int				jobnumber;
    long			length;
    CSemaphore		*semaphore;
} Job;

#endif /* Job_h */
