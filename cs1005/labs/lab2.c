/* Peterg M. Golaszewski
 * peterg
 * section A08
 * Sept 11,1996
 */

#include <stdio.h>

int main(void){

  int n,                        /* number limit */
      sum,                      /* result sum   */
      counter;                  /* loop counter */
  
   /* Provide user with a little info and ask for number N : */
  printf("\n\nThis program calculates sum of all positive integers from 1 to number N,\n");
  printf(" that divide exacly by 11.\n\n");
  printf("Give me number N : ");
  scanf("%d",&n);

   /* Calculate the sum :*/
  sum=0;
  for (counter=1; counter<=n; ++counter){ if (counter%11==0) sum=sum+counter;}

   /* Give user final answer : */
  printf("Sum of positive integers from 1 to %d that divide exacly by 11 is %d.\n\n",n,sum);

  return(0);   

} 
