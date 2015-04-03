#include "shm.h"

#define MAX_USER_NAME 25
#define MEM_KEY 130877
#define SEM_KEY 130878
#define SEM_INIT 1
#define MAX_LOGINS 10
#define FALSE 0
#define TRUE 1

typedef struct {
	 char name[MAX_USER_NAME];
    int ppid;
} login_type;

typedef struct {
    int count;
    login_type login[MAX_LOGINS];
} heybook_type;

typedef struct  {
    int check[MAX_LOGINS];
} login_map;

heybook_type *heybook;
heybook_type local_heybook;

int ppid;
int my_semaphore;
int index;

int done=FALSE;

void clean_up () {

   semwait(my_semaphore);

   /* CRITICAL REGION #2: */

   /* remove my login information */

   heybook->count--;
   if (heybook->count!=index) {

   heybook->login[index].ppid = heybook->login[heybook->count].ppid;
   strcpy(heybook->login[index].name, heybook->login[heybook->count].name);

   }


   /* END OF CRITICAL REGION #2 */

   semsignal(my_semaphore);

   /* release recourses */
   /* if I'm the last one, then remove shared mem and semaphore:  */

	if (heybook->count == 0) {		
		if (shmdelete((char *)heybook)==-1)
		{
			printf("Can't delete shared memory. I QUIT\n");
		} else 
		{ printf("Removing shared memory ...\n"); }

		if (semdelete(my_semaphore)==-1)		
		{
			printf("Can't delete semaphore. I QUIT\n");
		} else { printf("Removing semaphore ...\n"); }
	}


   exit(0);
}

int main () {

	char user[MAX_USER_NAME];
   login_map map;

   int a,b,c;

   int found;

	/* Get user login name */
   	strcpy(user,getlogin());

	/* Get parent PID */
   ppid = getppid();

   printf("LOGIN NAME: %s\n",user);

   /* try to attach to the semaphore */
   if ((my_semaphore=semattach(SEM_KEY))==-1) {
		printf("Semaphore attach failed.  Trying to create semaphore...\n");
      if (my_semaphore=(semcreate(SEM_KEY,SEM_INIT))==-1)
      {
      	printf("Failed ... I can't take this no more ! I QUIT !!!\n");
         clean_up();
         return 0;
      }
   } else { printf("Attaching to semaphore...\n"); }

   if (semwait(my_semaphore)==-1)
   {
   	printf("Error: Can't WAIT ! I QUIT !!! \n");
      clean_up();
      return 0;
   }

   /* CRITICAL REGION #0: */

   /* try to attach to shared mem */
   if ( (heybook=(heybook_type *)shmattach(MEM_KEY)) == NULL) {
    	printf("Memory attach failed.  Trying to create heybox...\n");
		if ((heybook=(heybook_type *)shmcreate(MEM_KEY,sizeof(*heybook)))==NULL)
      {
      	printf("Failed ... I can't take this no more ! I QUIT !!!\n");
         clean_up();
         return 0;
      } else { heybook->count=0; }
   } else { printf("Attaching to heybox...\n"); }

   /* add my login information */

   index = heybook->count;

   heybook->login[heybook->count].ppid = ppid;
   strcpy( heybook->login[heybook->count].name,user );
   heybook->count++;

   /* copy the shared Heybook in to local Heybook */

   local_heybook.count = heybook->count;
	for (a=0; a < heybook->count; a++)
   {
   	local_heybook.login[a].ppid = heybook->login[a].ppid;
      strcpy(local_heybook.login[a].name, heybook->login[a].name);
   }

   /* END OF CRITICAL REGION #0 */

   if (semsignal(my_semaphore)==-1)
   {
   	printf("Error: Can't SIGNAL ! I QUIT !!! \n");
      clean_up();
      return 0;
   }

 	/* set up clean up function */
   signal(SIGINT, clean_up);

   while (done==FALSE)
   {
            if (semwait(my_semaphore)==-1)
			   {
			   	printf("Error: Can't WAIT ! I QUIT !!! \n");
			      clean_up();
			      return 0;
			   }

			   /* CRITICAL REGION #1: */

				/* check for changes */

            for (a=0; a < MAX_LOGINS ; a++)
            { map.check[a] = FALSE; }

            /* log INs: */
				for (a=0; a < heybook->count; a++)
			   {
               found = FALSE;
               for (b=0; b<local_heybook.count; b++)
               {
               	if (!strcmp(local_heybook.login[b].name,heybook->login[a].name))
                  {
							map.check[b] = TRUE;
                     found = TRUE;
                  }
               }
               if (found==FALSE)
               {
               	printf("%s has logged in, ppid: %d\n",
                  			heybook->login[a].name,
                           heybook->login[a].ppid);
               }
			   }

            /* log OUTs: */
				for (a=0; a < local_heybook.count; a++)
			   {
            	if (map.check[a]==FALSE)
               {
               	printf("%s has logged out, ppid: %d\n",
                  			local_heybook.login[a].name,
                           local_heybook.login[a].ppid);
               }
            }

			   /* copy the shared Heybook in to local Heybook */

			   local_heybook.count = heybook->count;
				for (a=0; a < heybook->count; a++)
			   {
			   	local_heybook.login[a].ppid = heybook->login[a].ppid;
			      strcpy(local_heybook.login[a].name, heybook->login[a].name);
			   }

			   /* END OF CRITICAL REGION #1 */

			   if (semsignal(my_semaphore)==-1)
			   {
			   	printf("Error: Can't SIGNAL ! I QUIT !!! \n");
			      clean_up();
			      return 0;
			   }

				if (ppid != getppid())
	         {
					/* my parent left me ;( */
	            printf("My Parent does not exist anymore. I QUIT !");
               done = TRUE;
         	}

         /* sleep for a second or two */
      	sleep(1);
   }


   clean_up();

}
