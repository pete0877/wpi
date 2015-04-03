/* Peter Golaszewski
 * Section A07
 * Homework Assigment #1
 * 9/14/96
 * Program : hw1.c
 *
 *   This program calculates optimum number of cans (of different types)
 *  needed for a order. Given are number of galons of paint ordered
 *  and capacities of four types of containers. Program also askes for
 *  Shipping and Handing prices for each type of a container, and cal-
 *  culates total order cost. All variables are outputed in 2 decimal
 *  places or integer values. If program ignores any more than 2 decimal 
 *  places when enterring amount of paint orderred.
 *
 */ 

#include <stdio.h> 

int main(void){

	 /* number of galons ordered: */  
	double order;             
         /* output numbers of cans to order: */
	int needed_08,needed_05, needed_007, needed_001; 
	 /* order * 100 : */
        long int amount; 
	 /* S&H prices for aech type of containers, total cost: */
	double sh_08, sh_05, sh_007, sh_001, total_cost;

	 /* enter input data (number of galons) */
	printf ("\n\nSpecify amount of paint (in galons) \n");
	printf ("you want to order (optional 2 decimal places) : ");
	scanf ("%lf",&order);

	 /* calculate amount needed * 100 (easier and faster) */
	 /* +.005 part eliminates real2int conversion error */
	amount = 100 * (order+.005);

 	printf("\nYou ordered ");   /* display order amount :*/
        printf("%.2f galons of paint.\n",(float)(amount/100.)); 

	needed_08 = amount / 80;
	amount = amount % 80;
	needed_05 = amount / 50;
	amount = amount % 50;   
        needed_007 = amount / 7;
        needed_001 = amount % 7;    /* reminder will be needed_001 */

	printf("\nYou need:\n");    /* display order */
    	printf("   %d containers of 0.8 gal capacity,\n",needed_08); 
	printf("   %d containers of 0.5 gal capacity,\n",needed_05);	
        printf("   %d containers of 0.07 gal capacity,\n",needed_007);
        printf("   %d containers of 0.01 gal capacity,\n",needed_001);

	 /* extra cridit part : read S&H pices for each type of cont. */
	printf("\nEnter S&H price for 0.8 gal type container ($):");
	scanf("%lf",&sh_08);
	printf("Enter S&H price for 0.5 gal type container ($):");
	scanf("%lf",&sh_05);
	printf("Enter S&H price for 0.07 gal type container ($):");
	scanf("%lf",&sh_007);
	printf("Enter S&H price for 0.01 gal type container ($):");
	scanf("%lf",&sh_001);

	 /* Calculate / Display total cost */
	total_cost = sh_08 * needed_08 + 
		     sh_05 * needed_05 + 
		     sh_007 * needed_007 + 
		     sh_001 * needed_001;
	printf("\nTotal Shipping and Handling cost is");
	printf(" $%.2lf.\n\n",total_cost);

return(0);
}

