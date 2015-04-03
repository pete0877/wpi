#include "hey_there.h"

       int  sock;
       char msgout[MAX_LINE];
       int  my_ppid;

void Clean_Up() {
  char my_host[MAX_HOST_NAME];

  printf("\nHey Client goes bye bye.");  
  gethostname(my_host,MAX_HOST_NAME);
  sprintf(msgout,"LOGOUT,%s,%s,%d",getlogin(),my_host,my_ppid);
  send(sock, msgout, strlen(msgout) + 1, 0);

  exit(0);
}

int main(int argc, char *argv[]) {

  struct heybook old, *current;
  char *tmp;
  char name[MAX_NAME];
  char host[MAX_HOST_NAME];
  char my_host[MAX_HOST_NAME];
  char buffer[MAX_BUFFER], 
      *server, 
       input[MAX_LINE],
       cid[20],
      *ptr,
      *logins[3 * MAX_LOGINS];
  int  a, b, c, d,
       found, 
       map[MAX_LOGINS], 
       id, 
       bytes,
       ppid;

  my_ppid = getppid();

  tmp=malloc(sizeof(struct heybook));
  current = malloc(sizeof(struct heybook));

  server = argv[1];

  printf("\nTrying to connect to server %s at port %d...", server, PORT);

  sock = sockconnect(server, PORT);
  if (sock == -1) {
    printf("\nCouldn't connect to the server!");
    exit(1); }

  /* set up clean up function */
  signal(SIGINT, Clean_Up);

  /* send LOGIN */
  gethostname(my_host,MAX_HOST_NAME);
  sprintf(msgout,"LOGIN,%s,%s,%d",getlogin(),my_host,my_ppid);
  bytes = send(sock, msgout, strlen(msgout) + 1, 0);
  if (bytes == -1) {
	printf("\nCouldn't send command!");
	Clean_Up(); }

  old.count=0;

  while (1) 
  {
    sleep (2);

    if (my_ppid != getppid()) Clean_Up();

    bytes = send(sock, "HEY", 4, 0);
    if (bytes == -1) {
	printf("\nCouldn't send command!");
	Clean_Up();
    }

      /* clear the logins map */
      for (a=0; a < MAX_LOGINS ; a++) map[a] = 0;

      /* Get Hey book */

      printf("(rec)");
      recv(sock, tmp, sizeof(struct heybook), MSG_WAITALL);
      printf("(recievd)");
      memcpy(current,tmp,sizeof(struct heybook));
      printf(":%s:",tmp);

      /* Check Heybook */

      /* log INs: */
      for (a=0; a < current->count; a++)
      {
         found = 0;
         for (b=0; b<old.count; b++)
         {
              if ( (!strcmp(old.login[b].name,current->login[a].name)) &&
 		   (!strcmp(old.login[b].host,current->login[a].host)) &&
		   (old.login[b].ppid == current->login[a].ppid) )	
                 {
			map[b] = 1;
                        found = 1;
                 }
          }
          if (found==0)
          {
               		printf("\n  %s has logged IN on %s.",
                  	current->login[a].name,
                        current->login[a].host);
          }
       }

       /* log OUTs: */
       for (a=0; a < old.count; a++)
       {
           if (map[a]==FALSE)
           {
               		printf("\n  %s has logged OUT on %s.",
                  	old.login[a].name,
                        old.login[a].host);
	   }
       }

       /* copy the current Heybook in to old Heybook */

       old.count = current->count;
       for (a=0; a < current->count; a++)
       {
 		old.login[a].ppid = current->login[a].ppid;
		strcpy(old.login[a].name, current->login[a].name);
		strcpy(old.login[a].host, current->login[a].host);
       }
  }
}




