/////////////////////////////////////////////////////////
//
//   TShell
//   Peter Golaszewski & Sebastian Jastrzebski
//
/////////////////////////////////////////////////////////

#include <iostream.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <sys/wait.h>
#include <sys/time.h>
#include <sys/resource.h>

/////////////////////////////////////////////////////////

#define MAX_ARGS 5
#define LOWEST_PRIORITY 19
#define MAX_INPUT_LEN 70

/////////////////////////////////////////////////////////

void change_dir(char *name)
{
     if (chdir(name))
        { cout << "Directory not found.\n"; } 
     else
        { cout << "Moving to directory: " << name << "\n"; }
}

/////////////////////////////////////////////////////////

void execute(char *args[])
{
     int stat, pid = fork();
     if (!pid) 
     {  // Child:
	if (!strcmp(args[0],"kind"))
	{
	   nice(LOWEST_PRIORITY);
	   exit(execvp (args[1], &args[1]) );
	} else exit(execvp (args[0], args) );
     } else { // Parent:
	wait (&stat);
	if (stat) { cout << "Not found!\n"; }
     }
}

/////////////////////////////////////////////////////////

int main () 
{
  bool verboseFlag=true;
  int exit=0, counter;
  char input[MAX_INPUT_LEN], delimiters[2] = " ",
       *pstr, *args[MAX_ARGS+2];

  cout << "\nTShell by Peter Golaszewski & Sebastian Jastrzebski\n";
  while (!exit)
  {
	  // Get command line:
	  cout << "\nEnter Command: ";  
	  gets(input);

	  // Get arguments
          if ((pstr = strtok(input, delimiters )) != NULL)
	  {
	        counter = 0;
		args[counter] = new char(strlen(pstr)+1);
		strcpy (args[counter], pstr);
		counter = 1;
                while ((pstr = strtok((char *)NULL, delimiters ))!= NULL) 
		{
		      args[counter] = new char(strlen(pstr)+1);
		      strcpy (args[counter], pstr);
                      counter++;
                }
          }
          args[counter]=NULL;

	  // Parse input string
	  if (!strcmp(args[0],"exit"))    { exit=1; } 
          else if (!strcmp(args[0],"cd")) { change_dir(args[1]); }
          else if (!strcmp(args[0],"verbose"))
	       {
		  verboseFlag = !verboseFlag;
	          cout << "verbose mode is now ";
		  cout << (verboseFlag ? "on" : "off") << "\n";
	       } 
          else execute(args);
  }

  rusage *usage_info = new rusage;
  getrusage(RUSAGE_SELF,usage_info);
  cout << "\nUsage Info:\n";
  cout << "   CPU time (user + system): \n";
  cout << "      ";
  cout << usage_info->ru_utime.tv_sec + usage_info->ru_utime.tv_sec;
  cout << " seconds\n";
  cout << "      ";
  cout << usage_info->ru_utime.tv_usec + usage_info->ru_utime.tv_usec;
  cout << " microseconds\n";
  cout << "   Voluntary context switches  : ";
  cout << usage_info->ru_nvcsw << "\n";
  cout << "   Involuntary context switches: ";
  cout << usage_info->ru_nivcsw << "\n";
  delete usage_info;
}
