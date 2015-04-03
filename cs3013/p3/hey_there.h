#include <signal.h>
#include <unistd.h>

#include "sock.h"
#include "shm.h"
#include "sock.h"

#define MAX_NAME 20
#define MAX_HOST_NAME 20
#define MAX_LINE 30
#define MAX_LOGINS 20
#define MAX_BUFFER 500
#define KEY_MEMORY 420
#define KEY_SEMAPHORE 555
#define PORT 6900

struct login {
  char name[MAX_NAME];				/* login name */
  char host[MAX_NAME];				/* host name  */
  int ppid;		/* parent pid */
};

struct heybook {
  int count;						
  struct login login[MAX_LOGINS];	/* array of hey logins */
};
