#include "shm.h"

/*
 * shmattach() - Attach to a shared memory segment with the given key. 
 *               Return NULL on failure.  
 */
char *shmattach(key_t key) {
  int i;
  char *mem;

  /* There is already a segment there.  Attach to it. */
  if ((shmId = shmget(key, MAXMEM, 0)) < 0)
    return(NULL);
#ifdef DEBUG
  printf("In shmattach().  Did a successful shmget.\n");
#endif
  if (((int)(mem = (char *)shmat(shmId, NULL, 0))) == -1) {
#ifdef DEBUG
    perror("In shmattach().  We have a problem.\n");
#endif
    /* a problem */
    (void)shmctl(shmId, IPC_RMID, (struct shmid_ds *)NULL);
    return(NULL);
  }

  return mem;
}

/*
 * shmcreate() - create the given amount of shared memory and return the 
 *	pointer to this memory.  Return NULL on failure.  We have imposed
 *	a limit of 1024 bytes for the total amount of shared memory created.
 */
char *shmcreate(key_t key, int cnt /* number of bytes */) {
  int i;
  char *mem;

  if (cnt > MAXMEM)
    return(NULL);		/* too big */

  if (shmId != -1) {
    fprintf(stderr,"shmcreate: Shared memory already created.\n");
    return(NULL);
  }

  /* get mem */
  if ((shmId = shmget(key, MAXMEM, 0666|IPC_CREAT|IPC_EXCL)) < 0)
      return(NULL);		/* a problem */

  /* attach to it */
  if (((int)(mem = (char *)shmat(shmId, NULL, 0))) == -1) {
      /* a problem, cleanup and bail */
      (void)shmctl(shmId, IPC_RMID, (struct shmid_ds *)NULL);
      return(NULL);
  }
  
  /* ok if we get to this point then we have a shared memory chunk */
  return mem;

}

/*
 * shmdelete() - delete the shared memory associated with the given address.
 *	Return -1 on failure, zero on success.
 */
int shmdelete(char *mem) {
  int ret;
  
  /* remove entire chunk */
  if (shmdt(mem) == -1)
    return -1;
  ret = shmctl(shmId, IPC_RMID, (struct shmid_ds *)NULL);
  if (ret == 0)
    shmId = -1;
  return(ret);
}

/*
 * semwait() - wait on the given semaphore.  Return -1 on error, zero on
 *	success.
 */
int semwait(int sem) {
    struct sembuf sembuf;

#ifdef DEBUG
    printf("In swait(): rgSem[%d]=%d\n", sem, rgSem[sem]);
    printf("In swait(): semval= %d\n", semctl(rgSem[sem], 0, GETVAL));
#endif

    sembuf.sem_num = 0;
    sembuf.sem_op = -1;
    sembuf.sem_flg = 0;
    while (semop(rgSem[sem], &sembuf, 1) == -1) {
	if (errno != EINTR)
	    return(-1);		/* do not return on an interrupted wait */
    }
    return(0);
}

/*
 * semsignal() - signal the given semaphore.  Returns -1 on failure.
 */
int semsignal(int sem) {
    struct sembuf sembuf;

#ifdef DEBUG
    printf("In ssignal(). semval: %d\n", semctl(rgSem[sem], 0, GETVAL));
#endif

    sembuf.sem_num = 0;
    sembuf.sem_op = 1;
    sembuf.sem_flg = 0;
    return(semop(rgSem[sem], &sembuf, 1));
}

/*
 * semattach() - attach to the semaphore indicated by key.  Return
 *             -1 if error.
 */
int semattach(key_t key) {
    union semun arg;
    int temp_sem;

#ifdef DEBUG
    printf("sattach(): Attaching to semaphore set at %d...\n", key);
#endif
    temp_sem = semget(key, 0, 0);
    if (temp_sem < 0) {
#ifdef DEBUG
      perror("semget");
#endif
      return -1;
    }
    
#ifdef DEBUG
    printf("sattach(): Semaphore exists.  Attaching to it. \n");
#endif

    rgSem[iSem] = temp_sem;	/* put index value into array */
    iSem++;
    return(iSem-1);
}

/*
 * semcreate() - create a semaphore with the given initial count and the key.  
 *      Returns -1 on failure, a semaphore id on success.  
 *      We have imposed a limit 12 semaphores that can be created.
 *      Attaches to the semaphore if it exists.
 */
int semcreate(key_t key, int cnt) {
    union semun arg;
    int temp_sem;

    if (iSem >= MAXSEM)
	return(-1);		/* have reached semaphore limit */

#ifdef DEBUG
    printf("screate(): key is %d\n", key);
#endif
    
#ifdef DEBUG
    printf("screate(): trying to attach to semaphore set.\n");
#endif
    temp_sem = semget(key, 0, 0);
    if (temp_sem > 0) {
#ifdef DEBUG
      printf("screate(): semaphore set exists.  Attaching to it. \n");
#endif
      rgSem[iSem] = temp_sem;		/* put index value into array */
      iSem++;
      return(iSem-1);
    }
#ifdef DEBUG
    printf("screate(): creating new semaphore set. \n");
#endif
    
    temp_sem = semget(key, 1, 0666|IPC_CREAT|IPC_EXCL);
    if (temp_sem < 0) {
      return(-1);
    }

    /* ok, at this point we have a set of semaphores */
    arg.val = cnt;
    if (semctl(temp_sem, 0, SETVAL, arg) < 0) {
      return(-1);
    }
    rgSem[iSem] = temp_sem;		/* put index value into array */
    iSem++;
    return(iSem-1);
}

/*
 * semdelete() - delete the semaphore corresponding to the given sem id.  
 *      Returns -1 on failure, zero on success. 
 */
int semdelete(int sem) {
    int ret;

    ret = semctl(rgSem[sem], 0, IPC_RMID, (union semun *)NULL);
    rgSem[sem] = -1;
    return ret;
}

/*
 * semcount() - return the count of the given sem id.  -1 on failure.
 */
int semcount(int sem) {
#ifdef LINUX
    return semctl(rgSem[sem], 0, GETVAL, NULL);
#else
    return semctl(rgSem[sem], 0, GETVAL);
#endif
}


