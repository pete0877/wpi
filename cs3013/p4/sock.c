#include "sock.h"

/* sockcreate -- Create socket from which to read.
   return socket descriptor if ok, -1 if not ok.  */
int sockcreate(int port) {
  int sockfd;
  struct sockaddr_in serv_addr;

#ifdef DEBUG
   printf("Trying to create socket at port %d...\n", port);   
#endif
   
  sockfd = socket(AF_INET, SOCK_STREAM, 0);

  if (sockfd == -1)
    return -1;
   
   /* bind our local address so client can connect to us */
   bzero((char *) &serv_addr, sizeof(serv_addr));
   serv_addr.sin_family = AF_INET;
   serv_addr.sin_addr.s_addr = htonl(INADDR_ANY);
   serv_addr.sin_port = htons(port);
   
   if (bind(sockfd, (struct sockaddr *) &serv_addr, sizeof(serv_addr)) < 0)
     return -1;
   
#ifdef DEBUG
   printf("Socket created! Accepting connections.\n");
#endif

   /* listen on the socket for connection */
   if (listen(sockfd, 5) == -1 )
     return -1;

   return sockfd;
   
}

/* sockaccept -- accept connection on a socket.
   Return new socket descriptor if ok, -1 if not ok. */
int sockaccept(int sock) {
  int clilen;
  int newsockfd; 
  struct sockaddr_in cli_addr;
  
  clilen = sizeof(cli_addr);
  newsockfd = accept(sock, (struct sockaddr *) &cli_addr, &clilen);
  if (newsockfd < 0)
    return -1;
  return newsockfd;
}

/* sockconnect -- create socket to host at port.
   Return socket if ok, -1 if no ok. */
int sockconnect(char *host, int port) {
  unsigned long int inaddr;
  struct sockaddr_in serv_addr;
  int sockfd;
  struct hostent *hp;

  /*
   * First try to convert the host name as a dotted-decimal number.
   * Only if that fails do we call gethostbyname().
   */
  bzero((void *) &serv_addr, sizeof(serv_addr));
#ifdef DEBUG
  printf("Looking up %s...\n", host);
#endif
  if ((inaddr = inet_addr(host)) != INADDR_NONE) { 
    bcopy((char *) &inaddr, (char *) &serv_addr.sin_addr, sizeof(inaddr));
  } else {
    if ((hp = gethostbyname(host)) == NULL) 
      return -1;
    bcopy(hp->h_addr, (char *) &serv_addr.sin_addr, hp->h_length);
  }

#ifdef DEBUG
  printf("Found it.  Setting port connection to %d...\n", port);
#endif
  serv_addr.sin_family = AF_INET;
  serv_addr.sin_port = htons(port);

  /* open a TCP socket (an Internet stream socket). */
#ifdef DEBUG
  puts("Done. Opening socket...");
#endif
  if ((sockfd = socket(AF_INET, SOCK_STREAM, 0)) < 0) 
    return -1;
  
  /* socket open, so connect to the server */
#ifdef DEBUG
  puts("Open. Trying connection to server...");
#endif
  if (connect(sockfd, (struct sockaddr *) &serv_addr, 
	      sizeof(serv_addr)) < 0) 
    return -1;

#ifdef DEBUG  
  printf("Connection established!\n");
#endif

  return sockfd;
}

