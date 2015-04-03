/*
   This program processes a data file containing a collection of daily high
   temperatures. It counts and prints the number of hot days(high temperature
   85 or higher), the number of pleasant days(high temperature 60-84), and
   the number of cold days(high temperatures below 60). The average tempera-
   ture is also printed.
*/

#include <stdio.h>

#define HOT_TEMP  84		/* Anything above HOT_TEMP  is high */
#define COLD_TEMP 60		/* Anything below COLD_TEMP is cold */

int main(void)
{

  double   avg_temp;		/* Average temperature */
  int   days = 0,		/* Number of days */
  	hot_days      = 0,	/* Number of hot days */
	pleasant_days = 0,	/* Number of pleasant days */
        cold_days     = 0,	/* Number of cold days */
        temp	      = 0,	/* Temperature currently processed */
        total_temp    = 0;	/* (Running) Sum of all temperatures */

  total_temp=0;
  printf("Input day temperatures: "); 	/* 55 62 68 74 59 45 ... */
  do {
    scanf("%d",&temp);  /* Read in a temperature, process it,*/
    if (temp>0) {
  					/* exit at end of the input */
      days = days + 1;			/* Count number of days */
      total_temp += temp;		/* Accumulatet emperatures*/
      if (COLD_TEMP <= temp && temp <= HOT_TEMP) /* A pleasant day */
       pleasant_days++;	
      else if (temp > HOT_TEMP)			/* A hot day */
       hot_days++;
      else cold_days++;	 /* A cold day */
    }
  } while (temp>0);
  if (days>0) {
    avg_temp = (float)total_temp / days;   /* Average high temperature*/
    printf("\nOut of %d days\n", days);	/* output */
    printf("There are %d hot days\n",      hot_days);
    printf("There are %d pleasant days\n",  pleasant_days);
    printf("There are %d cold days\n",	 cold_days);
    printf("The average temp is %f\n", avg_temp);
  }			 
  printf("Have a good one :)\n");
  return(0);

}
