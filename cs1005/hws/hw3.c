/*
 * Peter Golaszewski
 * Section 8
 * Homework Assigment #3
 * 10/28/96
 * Program : hw3.c
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

int dim;	   /* hunt field dimention */       
char key;           /* for keyboard input */


void 
read_coordinates(int * xp_hunter, int * yp_hunter,
                      int * xp_prey, int * yp_prey);

void 
read_command(void);

void
move_hunter(int * xp_hunter, int * yp_hunter);


void
move_prey(int * xp_prey, int * yp_prey, int prey, int x_hunter, int
y_hunter);

int
captured(int x_prey,int y_prey, int x_hunter, int y_hunter);

void
print_table(int x_prey, int y_prey, int x_hunter, int y_hunter);


int main(void) {
	int x_hunter,y_hunter,      /* x and y of the hunter */
	    x_prey,y_prey,          /* x and y of the prey */
	    got_it=0,      /* =0 prey alive, =1 prey killed */
	    a,b,           /* handy variables */
	    quit,          /* if wants to quit, it =1 */
	    preyt,         /* prey type */
	    wrong;	   /* wrong/illegal command typed */	

	srandom(time(NULL));   /* randomizer */

	for (a=1;a<=40; a++) printf("\n");  /* scroll screen clean up */

		/* Enter required data */

	do {
	 printf("Please enter side length of the hunt field (min. 5): ");
 	 scanf("%d",&dim);
         printf("\n");
	} while (dim<5);
	
	--dim;   /* make sure it is from 0 to 4 not 1 to 5 */

	/* Choose prey type : */

        preyt=0;
	do {
		printf("Do you want to play with Smart(1), Stupid(2)");
		printf(",\n Dead(3) or Magic(4) prey ? (1-4): ");
		scanf ("%c",&key);	
		scanf ("%c",&key);
		switch (key) {
			case '1':
			     preyt=2;
			     printf("\n\n\nGOOD LUCK BODY (he he !)\n");
			     printf("The prey will try to run away.\n"); 
			     /* Unless you set H next to P */
			     /*,you will NEVER make it !!! */	 	
			     break;
                        case '2': 
                             preyt=1;
		             printf("\n\n\nPrey will run around.\n");
                             break;  	
			case '3':
                             preyt=3;
                             printf("\n\n\nThe prey won't move.\n");
                             break;
		 	case '4':
                             preyt=4;
                             printf("\n\n\nThe prey will use magic !.\n");
                             break;
		}
		printf("\n  press any key...");
		scanf("%c",&key);
		scanf("%c",&key);
		for (a=1;a<=40; a++) printf("\n");
	} while (preyt==0);

	read_coordinates(&x_hunter,&y_hunter,&x_prey,&y_prey);

	/* Check if game can go on ...*/

	if (captured(x_prey, y_prey, x_hunter, y_hunter)) {
    		print_table(x_prey, y_prey, x_hunter, y_hunter);
    		printf("Game is over\n");
    		return(0);
  	}

	/* display instructions (only once) */

	printf("You can move the hunter by using the following commands:\n");
	printf("  U - moves the hunter one cell upwards\n");
	printf("  D - moves the hunter one cell downwards\n");
	printf("  R - moves the hunter one cell to the right\n");
	printf("  L - moves the hunter one cell to the left\n");
	printf("  Q - quit game\n");
	printf("Game started!\n\n");

	scanf("%c",&key);

	quit=0;   /* initialize */

	do {

		print_table(x_prey,y_prey,x_hunter,y_hunter); 

		move_hunter(&x_hunter,&y_hunter);

		if (key=='q' || key=='Q') 
		{
			/* do we want to quit the game ? */
                        printf(" You ended the Game ... \n");
			printf("  press any key...");
        		scanf("%c",&key);
        		for (a=1;a<=40; a++) printf("\n"); 
                        return(0);
		}

		if (captured(x_prey,y_prey,x_hunter,y_hunter)==1) 
		{
			/* hunter got it */
                        printf(" You've got the prey! Consider ");
                        printf("registering for a safari!\n\n");  
			print_table(x_prey,y_prey,x_hunter,y_hunter); 
			got_it=1;
		}  
		 else move_prey(&x_prey,&y_prey,preyt,x_hunter,y_hunter);

		if (captured(x_prey,y_prey,x_hunter,y_hunter)==1 &&
		    got_it==0) 
                {
			/* prey steped on the hunter */
                        printf("Prey steped on you. You won! Consider");
                        printf("registering for a safari!\n\n");
			print_table(x_prey,y_prey,x_hunter,y_hunter); 
			got_it=1;
                }  

	} while (got_it==0);

	/* end the game : */

	printf("\n\n  Game ended,\n  press any key...");  
	scanf("%c",&key);
	for (a=1;a<=40; a++) printf("\n");

	return(0);
}


/* printf nice field table: */
void
print_table(int x_prey, int y_prey, int x_hunter, int y_hunter) 
{
	      /* Draw map : */

	int a,b;
	for (a=0;a<=dim;a++) {
         for (b=0;b<=dim;b++) {
if  (x_hunter==b && y_hunter==a && x_prey==b && y_prey==a) printf("C");
else
                  if (x_hunter==b && y_hunter==a) printf("H"); else
                   if  (x_prey==b && y_prey==a) printf("P"); else
                    printf("*");
         }
         printf("\n");
        }
        printf("\n");
}


/* initialize the coordinates : */
void
read_coordinates(int * xp_hunter, int * yp_hunter,
                      int * xp_prey, int * yp_prey)
{
 int yp,xp,yh,xh;
 do {
         printf("Enter position of the hunter (integers in the range");
         printf(" 0-%d): ",dim);
         scanf("%d%c%d",&yh,&key,&xh);
         printf("\n");
        } while (xh<0 || yh<0 || xh>dim || yh>dim);

        do {
         printf("Enter position of the prey (integers in the range");
         printf(" 0-%d): ",dim);
         scanf("%d%c%d",&yp,&key,&xp);
         printf("\n");
        } while (xp<0 || yp<0 || xp>dim || yp>dim);  

	*xp_hunter=xh;
	*yp_hunter=yh;
	*xp_prey=xp;
	*yp_prey=yp;

}

/* read in valid command into key */
void
read_command(void)
{
	int wrong=1;
	char x_key;
	do {
	 	printf("\nCommand: ");
                scanf("%c",&key);
                switch (key) {
                  case 'U':
                  case 'u':
                  case 'D':
                  case 'd':
                  case 'R':
                  case 'r':  
                  case 'L':
                  case 'l':
                  case 'Q':
                  case 'q': wrong=0;	
		            break;
                  default:
                    printf("\nUnknown command !\n press any key ...");
        	    scanf("%c",&x_key);
		}
                scanf("%c",&x_key);

	} while (wrong==1);
}


/* move the hunter depending on key */
void
move_hunter(int * xp_hunter, int * yp_hunter)
{
     int wrong2;
     char x_key;   
	
     do {	
	   wrong2=0;
	   read_command();
	   switch (key) {
                  case 'U':
                  case 'u':
                    if (*yp_hunter>0) --*yp_hunter; else {
                      wrong2=1;
                      printf("Illegal move! Look at map !\n");	
		      printf(" press any key ..");
		      scanf("%c",&x_key); 	}
                    break;
                  case 'D':
                  case 'd':
                    if (*yp_hunter<dim) ++*yp_hunter; else {
                      wrong2=1;
                      printf("Illegal move! Look at map !\n");
                      printf(" press any key ..");
                      scanf("%c",&x_key);       } 
                    break;
                  case 'R':
                  case 'r':
                    if (*xp_hunter<dim) ++*xp_hunter; else {
                      wrong2=1;
                      printf("Illegal move! Look at map !\n");
                      printf(" press any key ..");
                      scanf("%c",&x_key);       } 
                    break;
                  case 'L':
                  case 'l':
                    if (*xp_hunter>0) --*xp_hunter; else {
                      wrong2=1;
                      printf("Illegal move! Look at map !\n");}
                      break;
		  case 'Q':
		  case 'q':
		      wrong2=0;
		      break;	
                }            

      }while (wrong2==1);
}


/* time for prey to move (or not ) */
void
move_prey(int * xp_prey, int * yp_prey, int prey, int xp_hunter, int
yp_hunter)
{
	    int a,	
            step_x,step_y, /* prey decision steps (smart prey) */
            same_x,same_y; /* =1 if same vert/horiz position of H and P */ 

	    if (prey==1) 
	    { /* stupid */
                        a=random()%4;
                        switch (a) {
                           case 0: { /* up */

                                     if (*yp_prey>0) *yp_prey=*yp_prey-1;
				 	else
                                        *yp_prey=*yp_prey+1;
                                        break;
                                   }
                           case 1: { /* down */  
                                     if (*yp_prey<dim)
					*yp_prey=*yp_prey+1; else
                                        *yp_prey=*yp_prey-1;
                                        break;
                                   }
                           case 2: { /* right */
                                     if (*xp_prey<dim) 
					*xp_prey=*xp_prey+1; else
                                        *xp_prey=*xp_prey-1;
                                        break;
                                   }
                           case 3: { /* left */
                                     if (*xp_prey>0) 
					*xp_prey=*xp_prey-1; else
                                        *xp_prey=*xp_prey+1;
                                     break;
                                   }

                        }   
 		} else if (prey==2)
		{ /* smart */

                 same_x=0;  /* check where is the hunter : */
                 same_y=0;
                 if (*xp_prey==xp_hunter)
                  { step_x=1;
                    same_x=1;
                  } else

step_x=(*xp_prey-xp_hunter)/(abs(*xp_prey-xp_hunter));
                 if (*yp_prey==yp_hunter)
                  { step_y=1;
                    same_y=1;
                  } else

step_y=(*yp_prey-yp_hunter)/(abs(*yp_prey-yp_hunter));
                  /* Decide where to go : */
                  if (*xp_prey+step_x<=dim && *xp_prey+step_x>=0)
                                *xp_prey=*xp_prey+step_x; 
                  else
                        if (*yp_prey+step_y<=dim && *yp_prey+step_y>=0)
                                *yp_prey=*yp_prey+step_y;
                  else
                        if (same_x==1) *xp_prey=*xp_prey-1; else
                        if (same_y==1) *yp_prey=*yp_prey-1;

               } else if (prey==4) 
	       {
			/* Magic prey */
			/*   apear in random position */

		 	*yp_prey=random()%(dim+1);			
		 	*xp_prey=random()%(dim+1);			
	       }	

}


/* returns 1 if hunter got the prey. Otherwise =0 */
int
captured(int x_prey, int y_prey, int x_hunter, int y_hunter)
{
 if (x_prey==x_hunter && y_prey==y_hunter) return(1); else return(0);
}
