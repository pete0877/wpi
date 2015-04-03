#include "hey.h"

  int       sock, my_ppid, my_id;
  pthread_t tid_send_hey;
  pthread_t tid_get_hey;
  pthread_mutex_t mutex;  

void Clean_Up() {
  char msgout[MAX_LINE];
  char my_host[MAX_HOST_NAME];
  printf("\nHey Client goes bye bye.");  

  pthread_kill(tid_get_hey, SIGINT);
  pthread_kill(tid_send_hey, SIGINT);

  gethostname(my_host,MAX_HOST_NAME);
  sprintf(msgout,"LOGOUT,%s,%s,%d;12345",getlogin(),my_host,my_id);

pthread_mutex_lock(&mutex);
  send(sock, msgout, strlen(msgout) + 1, 0);
pthread_mutex_unlock(&mutex); 
  pthread_mutex_destroy(&mutex);
  exit(0);
}

void Send_Hey(void *arg)
{
	int send_sock = (int) arg;	
	while (1) {
              sleep (SLEEP_TIME);
pthread_mutex_lock(&mutex);
	      send(send_sock, "HEY;", 5, 0);
pthread_mutex_unlock(&mutex); 
        }        
}

void Get_Hey(void *arg)
{
        int  a, b, c, d, found, map[MAX_LOGINS];
	int get_sock = (int) arg;	
	char *tmp;
	struct heybook old, *current;
	tmp=malloc(sizeof(struct heybook));
	current=malloc(sizeof(struct heybook));
        old.count=0;

	while (1) {

		/* clear the logins map */
		for (a=0; a < MAX_LOGINS ; a++) map[a] = 0;

		recv(get_sock, tmp, sizeof(struct heybook), MSG_WAITALL);	      

		/* Get Hey book */
		memcpy(current,tmp,sizeof(struct heybook));

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
               			printf("\n  %s has logged IN on %s (%d).",
                  		current->login[a].name,
                        	current->login[a].host,
				current->login[a].ppid);
          		}
       		}

       		/* log OUTs: */
       		for (a=0; a < old.count; a++)
       		{
           		if (map[a]==FALSE)
           		{
               			printf("\n  %s has logged OUT from %s (%d).",
                  		old.login[a].name,
                        	old.login[a].host,
                        	old.login[a].ppid);
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

int main(int argc, char *argv[]) {

  char name[MAX_NAME],
       host[MAX_HOST_NAME],
       my_host[MAX_HOST_NAME],
       msgout[MAX_LINE],
      *server;
  int  bytes;

  my_ppid = getppid();

  server = argv[1];

  pthread_mutex_init(&mutex, NULL);

  printf("\nTrying to connect to server %s at port %d...", server, PORT);

  sock = sockconnect(server, PORT);
  if (sock == -1) {
    printf("\nCouldn't connect to the server!");
    exit(1); }

  /* set up clean up function */
  signal(SIGINT, Clean_Up);

  /* send LOGIN */
  gethostname(my_host,MAX_HOST_NAME);
  my_id = (int) (getpid());
  sprintf(msgout,"LOGIN,%s,%s,%d;",getlogin(),my_host,my_id);
pthread_mutex_lock(&mutex);
  bytes = send(sock, msgout, strlen(msgout)+1, 0);
pthread_mutex_unlock(&mutex); 
  if (bytes == -1) {
	printf("\nCouldn't send command!");
	Clean_Up(); 
  } else {
	printf("\nSent Login Info.");
  }

  pthread_create(&tid_send_hey, NULL, (void*) &Send_Hey, (void*) sock);
  pthread_create(&tid_get_hey,  NULL, (void*) &Get_Hey,  (void*) sock);

  while (1) { 
	sleep (2);
	if (my_ppid != getppid()) Clean_Up(); 
  } 

}




