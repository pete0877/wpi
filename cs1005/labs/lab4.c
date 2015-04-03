#include <stdio.h>

/* put the function prototype here */
float bill(int);

void main(void)
{
    int numberOfCalls;   /* number of telephone calls */
    float telephoneBill; /* total telephone bill */

    printf("Enter the number of calls : ");
    scanf("%d", &numberOfCalls);
    
    telephoneBill = bill(numberOfCalls);

     printf("\nThe telephone bill is $%.2f\n",telephoneBill);
}
/* put the function definition here */

float bill(int time)
{
	float charge;
	if (time<=100) charge=.5*time; 
	   else        charge=50.+(time-100)*.75;    
	return(charge);
}
    
