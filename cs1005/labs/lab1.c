/* Peter Golaszewski
 * peterg
 * Section A07
 * Sept.4,96
 * This program calculates weekly payroll for an hour employee */

#include <stdio.h>
#define max_reg_time  40
#define ot_factor  1.5
void main (void){
  int hours;
  float rate,gross_pay;

  printf("Enter the hourly rate of pay: ");
  scanf("%f",&rate);
  printf("\nEnter the number of hours worked ");
  printf("\nround up to whole number of hours: ");
  scanf("%d",&hours);
  if (hours>max_reg_time)
     gross_pay = rate * max_reg_time + ot_factor * (hours - max_reg_time);
  else
     gross_pay = rate * hours;
  printf ("\n\nhours = %d \n",hours);
  printf ("hourly rate pay = %.2f\n", rate);
  printf ("gross pay = $%.2f\n",gross_pay);
}
