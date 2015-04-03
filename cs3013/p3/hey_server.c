#include "hey_there.h"

void add_login(char *name, char* host, int ppid);
void del_login(char *name, char* host, int ppid);
void get_logins(int sock);
void parse (char *command, char *argv[]);
void cleanup(int i);

int sem;
struct heybook *hey;

int main(int argc, char *argv[])
{
	int done = 0;
	int srv_sock;
	int cl_sock;
	int pid;

char buffer[MAX_BUFFER];
char *argvs[5];

	hey = (struct heybook *) shmcreate (KEY_MEMORY, sizeof(struct heybook));

	if (hey == 0) {
		printf("\nCouldn't create shared memory.");
		exit(1);
	}

	sem = semcreate(KEY_SEMAPHORE, 1);

	if (sem == -1) {
		printf("\nCouldn't create semaphore.");
		exit(1);
	}

	semwait(sem);
	hey->count=0;
	semsignal(sem);

	srv_sock = sockcreate(PORT);

	if (srv_sock == -1) {
		printf("\nCouldn't create socket.");
		exit(1);
	}

	printf("\nHey-Server activating on port %d:\n", PORT);

	signal(SIGINT, cleanup);

	while (1) {

		cl_sock = sockaccept(srv_sock);

		printf("\nConnection request received.");

		pid = fork();

		if (pid == 0) {
		
			while (!done) {
			
				recv (cl_sock, buffer, sizeof(buffer), 0);

				parse (buffer, argvs);

				if      ( !strcmp(argvs[0], "LOGIN") )
				{
					add_login (argvs[1], argvs[2], atoi(argvs[3]));
				}
				else if ( !strcmp(argvs[0], "LOGOUT") )
				{
					del_login (argvs[1], argvs[2], atoi(argvs[3]));
					done = 1;
				}
				else if ( !strcmp(argvs[0], "HEY") )
				{
					get_logins(cl_sock);
				}
			}
			exit(0);
		}
	}

	cleanup(0);
	return 0;
}

void add_login(char *name, char* host, int ppid)
{
	int index;

	semwait(sem);

	printf("\nAdding: %s @ %s (%d)",name,host,ppid);

	index = hey->count;
	strcpy(hey->login[index].name, name);
	strcpy(hey->login[index].host, host);
	hey->login[index].ppid = ppid;

	hey->count++;

	semsignal(sem);
}

void del_login (char *name, char* host, int ppid)
{
	int index;
	int i;

	printf("\nRemoving: %s @ %s (%d)",name,host,ppid);

	semwait(sem);

	for (i = 0; i < hey->count; i++)
		if (hey->login[i].ppid == ppid &&
			!strcmp(hey->login[i].name, name) &&
			!strcmp(hey->login[i].host, host)
			) {
			index = i;
			break;
		}

	if (index != -1) {
		int last = --hey->count;

		if (index != last) {
			strcpy(hey->login[index].name,
hey->login[last].name);
			strcpy(hey->login[index].host,
hey->login[last].host);
			hey->login[index].ppid = hey->login[last].ppid;
		}
	}

	semsignal(sem);
}

void get_logins(int sock)
{
	semwait(sem);
        send(sock, hey, sizeof(struct heybook), 0);
	semsignal(sem);
}

void parse (char *command, char *argv[])
{
	int i = 0;
	char *ptr;

	argv[i++] = strtok (command, ",;");

	while ( (ptr = strtok (NULL, ",;")) )
		argv [i++] = ptr;

	argv[i] = NULL;
}

void cleanup(int i)
{
	semdelete(sem);
	shmdelete((char *)hey);	
	
	exit(0);
}
