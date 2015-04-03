#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <signal.h>
#include <pthread.h>
#include "shm.h"
#include "sock.h"

#define MAX_NAME 20
#define MAX_LOGINS 20
#define MAX_HOST_NAME 20
#define MAX_LINE 100
#define MAX_BUFFER 500
#define KEY_MEMORY 420
#define KEY_SEMAPHORE 555
#define PORT 6900
#define SLEEP_TIME 2

struct login {  
 char name[MAX_NAME];
 char host[MAX_NAME];				
 int ppid;
};

struct heybook {  
 int count;
 struct login login[MAX_LOGINS];	
};
