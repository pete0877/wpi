#include "bench.h"

void thread();
double gettime();
pthread_t th;
int * thread_array;

int main(int argc, char *argv[])
{
	int *array;
	int i, j;
	int tid, status;
	int no_procs, no_bytes;
	double start, stop, elapsed;

	if (argc != 3) {
		printf("USAGE: bench [number] [kbytes]\n");
		exit(1);
	} else {
		no_procs = atoi(argv[1]);
		no_bytes = atoi(argv[2]);
		if (no_bytes != 0) {
			array = (int *) malloc(no_bytes);
			if (array == NULL) {
				printf("Couldn't allocate memory!\n");
				exit(1);
			}
		}
	}

	printf("Creating child processes...\n");

	start = gettime();

	for (i = 0; i < no_procs; i++)
	{
		int pid = fork();

		if (pid == 0) {
			if (no_bytes != 0) {
				for(j = 0; j < no_bytes; j++)
					array[j] = 0;
			}
			exit(0);		/* destroy process */
		}
		else
			wait(&status);	/* wait for process to complete */
	}

	stop = gettime();
	elapsed = stop - start;

	printf("Done.\n");
	printf("Elapsed time for %ld processes: %lf\n", no_procs, elapsed);

///////////////////////
// Testing Threads:  //
///////////////////////

	printf("Creating threads...\n");

	thread_array = (int *) malloc(no_bytes);
	if (thread_array == NULL) {
		printf("Couldn't allocate memory!\n");
		exit(1);
	}

	start = gettime();
	for (i = 0; i < no_procs; i++)
	{
	   pthread_create(&th, NULL, (void*) &thread, (void*) no_bytes);
 	   pthread_join(th,NULL);
	}

	stop = gettime();
	elapsed = stop - start;

	printf("Done.\n");
	printf("Elapsed time for %ld processes: %lf\n", no_procs, elapsed);

	return 0;
}

void thread(void *arg)
{
	int j;
	int no_bytes = (int) arg;	
	for(j = 0; j < no_bytes; j++)
	{ thread_array[j] = 0; }
	pthread_exit(NULL);	
}

double gettime(void)
{
	double curtime;
	struct timeval tm;
	struct timezone tz;

	gettimeofday(&tm,&tz);
	curtime = (tm.tv_sec * 1000.0) + (tm.tv_usec / 1000.0);

	return(curtime);
}
