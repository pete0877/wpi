#include <stdio.h>
#include <signal.h>

void clean_up () {
   printf ("Cool!!!");
   exit(0);
}

int main () {
   printf("Press CTRL-C !!!\n");
   signal(SIGINT, clean_up);
   while (1);
}
