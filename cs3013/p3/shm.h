#include <stdio.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/shm.h>
#include <sys/sem.h>
#include <errno.h>
#include <signal.h>

#define mips

/** shared memory utilities **/
#define MAXMEM 2048		/* maximum amount of memory to be used 
				 * note: there is a system limit, too*/
static int shmId = -1;		/* shm id value for shared memory chunk */

/** semaphore utilities **/
#if defined(ns32000) || defined(mips)
union semun {
    int val;
    struct semid_ds *buf;
    u_short *array;
};
#endif

extern int errno;

#define MAXSEM 15		/* maximum number of semaphores */

static int semBase = -1;	/* base semaphore value */
static int cSem = 0;		/* count of current semaphores */
static int iSem = 0;		/* current index of semaphore to use */
static int rgSem[MAXSEM];	/* keep track of semaphores used */

/** function prototypes **/

/*
 * shmattach() - Attach to a shared memory segment with the given key. 
 *               Return NULL on failure.  
 */
char *shmattach(key_t key);

/*
 * shmcreate() - create the given amount of shared memory and return the 
 *	pointer to this memory.  Return NULL on failure.  We have imposed
 *	a limit of 2048 bytes for the total amount of shared memory created.
 */
char *shmcreate(key_t key, int cnt /* number of bytes */);

/*
 * shmdelete() - delete the shared memory associated with the given address.
 *	Return -1 on failure, zero on success.
 */
int shmdelete(char *sbShm);

/*
 * semattach() - attach to the semaphore indicated by key.  Return
 *             -1 if error.
 */
int semattach(key_t key);

/*
 * semcreate() - create a semaphore with the given initial count and the key.  
 *      Returns -1 on failure, a semaphore id on success.  
 *      We have imposed a limit 12 semaphores that can be created.
 */
int semcreate(key_t key, int cnt);

/*
 * semwait() - wait on the given semaphore.  Return -1 on error, zero on
 *	success.
 */
int semwait(int sem);

/*
 * semsignal() - signal the given semaphore.  Returns -1 on failure.
 */
int semsignal(int sem);


/*
 * semdelete() - delete the semaphore corresponding to the given sem id.  
 *      Returns -1 on failure, zero on success. 
 */
int semdelete(int sem);

/*
 * semcount() - return the count of the given sem id.  -1 on failure.
 */
int scount(int sem);




