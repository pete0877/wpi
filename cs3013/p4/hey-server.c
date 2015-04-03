#include "hey.h"

void receive(void *arg);
void cleanup(int i);
void add_login(char *name, char* host, int ppid);
void del_login(char *name, char* host, int ppid);
void get_logins(int sock);
void parse (char *command, char *argv[], int *argc);

struct heybook hey;
pthread_mutex_t mutex;

int main(int argc, char *argv[])
{
	pthread_t tid;
	int srv_sock = sockcreate(PORT);
	if (srv_sock == -1) {
		printf("Couldn't create socket.\n");
		exit(1);
	}
	pthread_mutex_init(&mutex, NULL);
	printf("Hey-Server activating on port %ld.\n", PORT);

	signal(SIGINT, cleanup);

	while (1) {
		int cl_sock = sockaccept(srv_sock);
		printf("Connection request received.\n");
		pthread_create(&tid, NULL,
				   (void*) &receive, (void*) cl_sock);
	}
	cleanup(0);
	return 0;
}

void cleanup(int i)
{
	pthread_mutex_destroy(&mutex);
	exit(0);
}

void receive(void *arg)
{
	int done = 0;
	int cl_sock = (int) arg;
	char buffer[MAX_BUFFER];
	int argc;
	char *argv[5];
	int i;

	while (!done) {
		recv (cl_sock, buffer, sizeof(buffer), 0);
		
		i = 0;
		while (buffer[i++]!=';') {}
		buffer[i]='\0';

		printf("Received message: '%s'\n", buffer);
		parse (buffer, argv, &argc);
		if      ( !strcmp(argv[0], "LOGIN") )
		{
			if (argc < 4)
				printf("Wrong number of arguments (%d).\n",argc);
			else
				add_login (argv[1], argv[2], (int)(atoi(argv[3])));
		}
		else if ( !strcmp(argv[0], "LOGOUT") )
		{
			if (argc < 4) {
				printf("Wrong number of arguments (%d).\n",argc);
				close(cl_sock);
				printf("Thread exiting with error.\n");
				pthread_exit(NULL);
			} else {
				del_login (argv[1], argv[2], (int)(atoi(argv[3])));
				done = 1;
			}
		}
		else if ( !strcmp(argv[0], "HEY") )
		{
			if (argc < 1)
				printf("Wrong number of arguments (%d).\n",argc);
			else
				get_logins(cl_sock);
		}
  	     else
		{
			 printf("Unrecognized command.\n");
		}
	}
	close(cl_sock);
	printf("Thread exiting.\n");
	pthread_exit(NULL);
}

void add_login(char *name, char* host, int ppid)
{
	int index = -1;
	printf("Adding: name = %s, host = %s, pid = %d.\n", name, host, ppid);
	pthread_mutex_lock(&mutex);
	index = hey.count++;
	strcpy(hey.login[index].name, name);
	strcpy(hey.login[index].host, host);
	hey.login[index].ppid = (int) (ppid);
	pthread_mutex_unlock(&mutex);
}

void del_login (char *name, char* host, int ppid)
{
	int i;
	int index = -1;
	printf("Deleting: name = %s, host = %s, pid = %d.\n", name, host, ppid);
	pthread_mutex_lock(&mutex);

	for (i = 0; i < hey.count; i++)
		if (hey.login[i].ppid == ppid &&
			!strcmp(hey.login[i].name, name) 
			) {
			 index = i;
			 break;
		        }
	if (index != -1) {
		int last = --hey.count;
		if (index != last) {
			strcpy(hey.login[index].name, hey.login[last].name);
			strcpy(hey.login[index].host, hey.login[last].host);
			hey.login[index].ppid = hey.login[last].ppid;
		}
	} else { printf("\n\n ERROR IN HEYBOOK !!! \n\n"); }

	pthread_mutex_unlock(&mutex);
}

void get_logins(int sock)
{
	int i;
	printf("Sending HeyBook...\n");
	pthread_mutex_lock(&mutex);
	for (i = 0; i < hey.count; i++)
	{
		char buffer[MAX_BUFFER];
		sprintf (buffer, "%s,%s,%d;",
				 hey.login[i].name, hey.login[i].host, hey.login[i].ppid);
		/* printf("%s\n", buffer); */
		/* send(sock, buffer, strlen(buffer), 0); */
	}
	send(sock, (char *) &hey, sizeof(struct heybook), 0);
	pthread_mutex_unlock(&mutex);

}

void parse (char *command, char *argv[], int *argc)
{
	int i=0;
	char *ptr, *next;

	argv[i++] = strtok_r (command, ",;", &next);
	while ( (ptr = strtok_r (NULL, ",;", &next)) )
	  { argv [i++] = ptr; }
	*argc = i;
	argv[i] = NULL;
}

