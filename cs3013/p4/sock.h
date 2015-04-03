/* sock.h */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <netdb.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>

#ifndef INADDR_NONE
#define INADDR_NONE 0xffffffff /* should be in <netinet/in.h> */
#endif

/* function prototypes */
int sockcreate(int port);

/* sockaccept -- accept connection on a socket.
   Return new socket descriptor if ok, -1 if not ok. */
int sockaccept(int sock);

/* sockconnect -- create socket to host at port.
   Return socket if ok, -1 if no ok. */
int sockconnect(char *host, int port);


