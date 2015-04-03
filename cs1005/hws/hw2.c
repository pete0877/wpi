/* Peter Golaszewski
 * Section A07
 * Homework Assigment #2
 * 9/28/96
 * Program : hw2.c
 *
 *    This is a simple hunter-prey game in which user uperates few basic
 * commands to catch the prey on the hunt field (min.size 5 by 5). User
 * can use following commands: 'u' to move hunter upune field, 'd' to move
 * down, 'r' - right, 'l' - left, and 'q' to quit the game.
 * The player ca choose to hunt for three types of preys. One of them can
 * be dead prey (prey does not change it's location), stupid (the prey 
 * runs in random directions), and smart (prey would run away from the
 * hunter). 
 * Game ends when hunter catches te prey of when prey runns into the
 * hunter.
 *
 */        

#include <stdio.h>
#include <stdlib.h>

int main(void) {
	int x_hunter,y_hunter,      /* x and y of the hunter */
	    x_prey,y_prey,          /* x and y of the prey */
	    dim,	   /* hunt field dimention */
	    got_it=0,      /* =0 prey alive, =1 prey killed */
	    a,b,           /* handy variables */
	    quit,          /* if wants to quit, it =1 */
	    prey,          /* =1 smart prey, =2 stupid */
	    step_x,step_y, /* prey decision steps (smart prey) */
	    same_x,same_y, /* =1 if same vert/horiz position of H and P */
	    wrong;	   /* wrong/illegal command typed */	
       char key;           /* for keyboard input */

	srandom(time(NULL));   /* randomizer */

	for (a=1;a<=40; a++) printf("\n");  /* scroll screen clean up */

		/* Enter required data */

	do {
	 printf("Please enter side length of the hunt field (min. 5): ");
 	 scanf("%d",&dim);
         printf("\n");
	} while (dim<5);
	
	--dim;   /* make sure it is from 0 to 4 not 1 to 5 */

	do {
	 printf("Enter position of the hunter (integers in the range");
         printf(" 0-%d): ",dim);
 	 scanf("%d%c%d",&y_hunter,&key,&x_hunter);
	 printf("\n");
        } while (x_hunter<0 || y_hunter<0 
              || x_hunter>dim || y_hunter>dim);

        do {
         printf("Enter position of the prey (integers in the range");
         printf(" 0-%d): ",dim);
         scanf("%d%c%d",&y_prey,&key,&x_prey);
         printf("\n");
        } while (x_hunter<0 || y_hunter<0
              || x_hunter>dim || y_hunter>dim);     
        prey=0;
	do {
		printf("do you want to play with Smart(1), Stupid(2)");
		printf(" or Dead(3) prey ? (1-3): ");
		scanf ("%c",&key);	
		scanf ("%c",&key);
		switch (key) {
			case '1':
			     prey=2;
			     printf("\n\n\nGOOD LUCK BODY (he he !)\n");
			     printf("The prey will try to run away.\n"); 
			     /* Unless you set H next to P */
			     /*,you will NEVER make it !!! */	 	
			     break;
                        case '2': 
                             prey=1;
		             printf("\n\n\nPrey will run around.\n");
                             break;  	
			case '3':
                             prey=3;
                             printf("\n\n\nThe prey won't move.\n");
                             break;
		}
		printf("\n  press any key...");
		scanf("%c",&key);
		scanf("%c",&key);
		for (a=1;a<=40; a++) printf("\n");
	} while (prey==0);

		/* display instructions (only once) */

	printf("You can move the hunter by using the following commands:\n");
	printf("  U - moves the hunter one cell upwards\n");
	printf("  D - moves the hunter one cell downwards\n");
	printf("  R - moves the hunter one cell to the right\n");
	printf("  L - moves the hunter one cell to the left\n");
	printf("  Q - quit game\n");
	printf("Game started!\n\n");

	quit=0;   /* initialize */

	do {

			/* Draw map : */
 		for (a=0;a<=dim;a++) {
                 for (b=0;b<=2*dim+2;b++) printf("-");
		 printf("\n");
                 for (b=0;b<=dim;b++) {
                        printf("|");
                        if (x_hunter==b && y_hunter==a) printf("H"); else
                         if  (x_prey==b && y_prey==a) printf("P"); else
                          printf(" ");
                 }
                 printf("|\n");
                }
                for (b=0;b<=2*dim+2;b++) printf("-");       
			/* legend */
		printf("\n  ( H - Hunter    P - Prey )\n");

		printf("\nCommand: ");
		scanf("%c",&key);
	        wrong=0;
			/* key control : */
		switch (key) {
		  case 'U':
	  	  case 'u': 
		    if (y_hunter>0) --y_hunter; else {
		      wrong=1;	
		      printf("Illegal move! Look at map !\n");}
		    break;
		  case 'D':
                  case 'd':
                    if (y_hunter<dim) ++y_hunter; else {
		      wrong=1;	
		      printf("Illegal move! Look at map !\n");}
                    break; 
		  case 'R':
                  case 'r':
                    if (x_hunter<dim) ++x_hunter; else {
		      wrong=1;
		      printf("Illegal move! Look at map !\n");}
                    break; 
  		  case 'L':
                  case 'l':
                    if (x_hunter>0) --x_hunter; else {
		      wrong=1;
		      printf("Illegal move! Look at map !\n");}
                    break; 
		  case 'Q':
		  case 'q': quit =1;
			    wrong=1;
			    break;	
		  default:
		    printf(" Unknown command !\n press any key ...");
		    wrong=1;
		}
		scanf("%c",&key);
		printf("\n");
	
		if (x_hunter==x_prey && y_hunter==y_prey) {
			 /* hunter steped on the prey */
			wrong=1;
			got_it=1;
			printf(" You've got the prey! Consider ");
                        printf("registering for a safari!\n\n"); 
		}

		if (wrong==0) {

		 /* time for prey to move : */
		 if (prey==1) { /* stupid */
			a=random()%4;
			switch (a) {
			   case 0: { /* up */
				     if (y_prey>0) y_prey--; else
					y_prey++;
					break;
				   }
			   case 1: { /* down */
                                     if (y_prey<dim) y_prey++; else
                                        y_prey--;
					break; 
                       		   }
			   case 2: { /* right */
                                     if (x_prey<dim) x_prey++; else
                                        x_prey--;
				     break; 
                                   }
			   case 3: { /* left */
                                     if (x_prey>0) x_prey--; else
                                        x_prey++;
				     break; 
                                   }
	
			}

		 } else if (prey==2){ /* smart */

		 same_x=0;  /* check where is the hunter : */
		 same_y=0;
		 if (x_prey==x_hunter)
		  { step_x=1;
                    same_x=1;
		  } else
			step_x=(x_prey-x_hunter)/(abs(x_prey-x_hunter));
		 if (y_prey==y_hunter) 
	 	  { step_y=1;
		    same_y=1; 
		  } else 
  		        step_y=(y_prey-y_hunter)/(abs(y_prey-y_hunter));
                  /* Decide where to go : */
                  if (x_prey+step_x<=dim && x_prey+step_x>=0)
				x_prey=x_prey+step_x; 
		  else
  		        if (y_prey+step_y<=dim && y_prey+step_y>=0)
                                y_prey=y_prey+step_y;
		  else
			if (same_x==1) x_prey--; else
			if (same_y==1) y_prey--; 

	       }	 

			/* new coordinates : */
		
		printf("  Hunter at: (%d,%d)\n",y_hunter,x_hunter);
                printf("  Prey at:   (%d,%d)\n\n",y_prey,x_prey);  

			/* Check if hunter got the prey : */
		if (x_hunter==x_prey && y_hunter==y_prey) {
			 /* prey steped on the hunter */    
			got_it++;
			printf(" You've got the prey! Consider "); 
			printf("registering for a safari!\n\n");
		}
	       } else wrong=0;	

	} while (quit==0 && got_it==0);

		/* end the game : */
	
	printf("\n\n  Game ended,\n  press any key...");  
	scanf("%c",&key);
	for (a=1;a<=40; a++) printf("\n");

return(0);
}
