#include "airline.h"

int mode=OPTIMAL;

int HashCity(char *name)
// Sum of all letters  % size hashing:
{
	int sum=0,a;
   for (a=0;a<strlen(name); a++) sum=sum+name[a];
   return(sum%MAXCITY);
}

// Prints out time int t in hh:mm xm format
void PrintTime(int t)
{
	int h,m;
	h=t/100;
	m=t-h*100;
	
	if (h>=12) { // pm
	   if (h>12) h=h-12;
	   printf("%2d:",h);
	   if(m<10) printf("0%1dpm",m);
	   else printf("%2dpm",m);
	} 

	else { // am
	   printf("%2d:",h);
	   if(m<10) printf("0%1dam",m);
	   else printf("%2dam",m);
	}
}

// Calculates time difference between t2 and t1 in minutes
int TimeDiff(int t2, int t1)
{
 int h1,h2,m1,m2;
 h1=t1/100;
 h2=t2/100;
 if (h2<h1) return(0);
 m1=t1-100*h1;
 m2=t2-100*h2;
 return(60*(h2-h1)-m1+m2);
}

// Prints out menu
void Print_Menu(char mode)
{
   cout << "\n\n             FAIRAIR AIRLINE Program menu: ";
   cout << "\n\n  0. Print this menu.";
   cout << "  \n  1. Exit.";
   cout << "  \n  2. Show list of cities serviced by FairAir.";
cout<<"\n  3. Show flight departures for a city listed in order of time.";
cout<<"  \n  4. Show flight arrivals for a city listed in order of time.";
   cout << "  \n  5. Make an airline reservation for a passanger.";
   cout << "  \n  6. Print list of passangers, by last name.";
   cout << "  \n  7. Print a passenger's reservation schedule.";
   cout << "  \n  8. Delete a passenger's reservation.";
   cout << "  \n  9. Change Find Route mode (current is ";
   if (mode) cout << "optimal)."; else cout << "selection).";
}

void main ()
{
   int menu;
   char c;
   Flights f;
   Reservations r;

   // Icrease CPassanger counters in FDB
   if (r.SetCounters(f)==-1)
   {
   	cout << "\n!!! Error found in reservation datebase !!!\n";
      exit(1);
   }

   cout << "\nReady:\n";
  	Print_Menu(mode);

   do
   {
   cout << "\n\nFAIRAIR: Enter Command (2..9, 0-help, 1-exit) : ";
   menu=-1;
   do
   { cin >> c;
     if (isdigit(c)) menu=c-'0';
   } while (menu==-1);
      switch (menu)
      {
      	case 0:
         	// Print this menu
	         Print_Menu(mode);
            break;
         case 1:
         	// Exit
            break;
         case 2:
         	// Show list of cities serviced by FairAir
	         f.PrintCityList();
            break;
         case 3:
       	   // Show flight departures for a city listed in order of time
            f.CityDepartureList();
	    break;
         case 4:
         	// Show flight arrivals for a city listed in order of time
            f.CityArrivalList();
	     break;
         case 5:
            r.MakeReserve(f);
         	// Make an airline reservation for a passanger
            break;
         case 6:
         	// Print list of passangers, by last name
            r.PrintPassengers();
		 break;
         case 7:
         	// Print a passenger's reservation schedule
            r.PrintSchedule(f);
		 break;
         case 8:
         	// Delete a passenger's reservation
       		r.DeleteReserve(f);
		     break;
         case 9:
         	// Change Find Route mode
	    if (mode)
		  cout<< "\nFind Route mode changed to SELECT."; else
		  cout<< "\nFind Route mode changed to OPTIMAL.";
        mode=!mode;
            break;
         default:
         	// Unknown command
            cout << "\n Error : Unknown Command";
            break;
      }
   } while (menu!=1);
   r.WriteReservations();
   cout << "\n\nProgram finished.\n\n";
}
