#include <iostream.h>
#include <fstream.h>
#include <ctype.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#define FLIGHTFILE "fairair" // Flight FB
// #define FLIGHTFILE "/cs/cs2005/pub/example/fairair"
#define RESERVEFILE "reservat"// Reservations DB
#define MAXCITY 97 // maximum number of cities to maintain
#define MAXFLIGHT 199 // maximum number of flights to maintain
#define MAXNAME 32 // maximum number of any name
#define MINLAYOVER 30 //  minimum layover time (in minutes)
#define ROUNDTRIP 0 // round trip reservation
#define ONEWAY 1 // oneway reservation
#define SELECT 0  // select mode for find route
#define OPTIMAL 1 // optimal mode for find route
#define MAXSELECT 100 // max number of chices in FindRoute
//#define NULL 0  // Empty pointer

int mode=1;

/* this structure defines the type for a flight node */
struct FlightType {
    int wFlight;		/* flight number */
    char *sbCity1;		/* departure city */
    int tDeparture;		/* departure time hhmm, e.g. 623 or 1258
*/
    char *sbCity2;		/* arrival city */
    int tArrival;		/* arrival time hhmm, e.g. 623 or 1258 */
    int cPassenger;		/* number of passengers on the flight */
    FlightType *pFlightDep;     /* next departure node from this city */
    FlightType *pFlightArr;     /* next arrival node to this city */
};

/* this structure defines the info for each city in your list */
struct CityListType {
    char *sbCity;		/* name of the city */
    FlightType *pFlightDep;	/* first departure from this city */
    FlightType *pFlightArr;	/* first arrival to this city */
};

/* this structure defines the info for storing flights by their number */
struct FlightNumberListType {
    int wFlight;		/* flight number */
    FlightType *pFlight;	/* flight node for this type */
};

/* this class encapsulates all the data and routines for the flight
database */
class Flights {
  private:
    CityListType rgCity[MAXCITY];  /* array of cities */
    FlightNumberListType rgFlight[MAXFLIGHT];  /* array of flights */
    int cFlight;     /* count of flights in the database */
    FlightType *MakeFlightNode(int, char *, int, char *, int);
  public:
    Flights();
    ~Flights();
    void PrintCityList();
    void *CityDepartureList();
    void *CityArrivalList();
    FlightType *FlightByNumber(int);
    int FindCity(char *);
    int FirstEmpty(int);
    FlightType *FirstDep(int);
    FlightType *FirstArr(int);
};

/* this structure defines the type for a route between two cities */
struct RouteType {
    int wDay;			/* day of travel: mmdd */
    int cHop;			/* Number of hops (1 or 2) */
    int wFlight1;		/* Flight number of first hop */
    int wFlight2;		/* Flight number of second hop (if needed)
*/
};

struct ReservationType {
    char *sbFirst;		/* first name of passenger */
    char *sbLast;		/* last name of passenger */
    int wType;			/* ROUNDTRIP or ONEWAY */
    RouteType route1;		/* first route */
    RouteType route2;		/* second route (only if ROUNDTRIP) */
    ReservationType *pReserve;  /* next reservation in linked list */
};

/* this class encapsulates all the data and routines for the flight
database */
class Reservations {
  private:
    ReservationType *pReserveHead; /* head of the reservation list */
    ReservationType *MakeReserveNode(char *, char *, int , RouteType,
RouteType);
    int FindRoute(char *, char *, RouteType &, Flights &);
  public:
    Reservations();
    ~Reservations();
    ReservationType *FindPerson(char *, char *);
    int MakeReserve(Flights &);
    void PrintPassengers();
    void PrintSchedule(Flights &);
    void DeleteReserve(Flights &);
    int WriteReservations();
    int Reservations::SetCounters(Flights &f);
};

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
	if (h>=12)
	// pm
	{
	   if (h>12) h=h-12;
	   printf("%2d:%2dpm",h,m);
	} else
	// am
	{
	   printf("%2d:%2dam",h,m);
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


/*
 * Reservations constructor, if there is a file read from it, otherwise start
 * null.
 * Assumes file is sorted alphabetically by last name.
 */
Reservations::Reservations(void){
	char sbFirst[MAXNAME], sbLast[MAXNAME];
   int wType;
   RouteType route1, route2;
   ReservationType *pTemp=NULL, *pTail=NULL;

   pReserveHead=NULL;
   ifstream resfile(RESERVEFILE);
   if(!resfile) //cannot open the file (doesn't exist)
   	pReserveHead=NULL;
   else {       //read from the file
   cout << "\nLoading Reservations Database ";
   	while (1) {
         wType = -1;
         route1.cHop = NULL;
         route2.cHop = NULL;

         //all done if this is a oneway, one hop
      	resfile >> sbFirst >> sbLast >> wType >> route1.wDay >> route1.cHop >>
         	route1.wFlight1;

         //if this is a oneway flight but two hops
         if(route1.cHop == 2) resfile >> route1.wFlight2;

         //if this is a roundtrip flight
         if(wType == ROUNDTRIP) {
         	resfile >> route2.wDay >> route2.cHop >> route2.wFlight1;
            //if the return flight has two hops
            if(route2.cHop == 2) resfile >> route2.wFlight2;
         }

         if(resfile.eof()) break; //if at end of file, leave loop.

         pTemp=MakeReserveNode(sbFirst, sbLast, wType, route1, route2);

        	cout << ".";
         if(pReserveHead == NULL) { //first datum to be entered
         	pReserveHead=pTemp;
            pTail=pReserveHead;
         }
         else{  // The other data.
            pTail->pReserve=pTemp;
         	pTail=pTemp;
         }
      }
   }
   resfile.close();
}

// Finds route from dep to arr city (depends on mode variale)
int Reservations::FindRoute(char *dep, char *arr, RouteType &r, Flights &f)
{
    int a,b,found,min=24*60,pos,count,choice;
    FlightType *pd, *pa;
    RouteType rgSelect[MAXSELECT];

    a=f.FindCity(dep);
    b=f.FindCity(arr);
    if (a<0 || b<0) return(-1);
    if (mode==OPTIMAL)
    {
    	cout << "\nOptimal Selection Mode:";
      if (f.FirstDep(a)==NULL ||
	  f.FirstArr(b)==NULL) return(-1);

      // Let's try direct connection :
      pd=f.FirstDep(a);
      pa=NULL;
      do
      {
          if (strcmp(pd->sbCity2,arr)==0)
          	if (min>TimeDiff(pd->tArrival,pd->tDeparture))
            {
            	pa=pd;
               min=TimeDiff(pd->tArrival,pd->tDeparture);
            }
	  		 pd=pd->pFlightDep;
      } while (pd!=NULL);
      min=24*60;
      if (pa!=NULL)
      // found direct !!!
      {
	  // set up info for reservation :
	  r.wFlight1=pa->wFlight;
	  r.cHop=1;
	  return(0);
      } else
      // Keep looking for double
      {
       // Go through all of the arrivals from dep[]
       //  and check, if any match any departs from arr[]
       //   boy ... its gonna be fun :
       pd=f.FirstDep(a);
       found=0;
       do {
	     pa=f.FirstArr(b);
	     do {
		   if (strcmp(pd->sbCity2,pa->sbCity1)==0)
		   // Yey ! .. Found it !
		   {
		     if (TimeDiff(pa->tDeparture,pd->tArrival)>=MINLAYOVER)
           {
			    if (min>TimeDiff(pa->tArrival,pd->tDeparture))
             {
               found=1;
			    	r.wFlight1=pd->wFlight;
			    	r.wFlight2=pa->wFlight;
			    	r.cHop=2;
               min=TimeDiff(pa->tArrival,pd->tDeparture);
             }
		     }
		   }
		   pa=pa->pFlightArr;
	     } while (pa!=NULL);
	  pd=pd->pFlightDep;
       } while(pd!=NULL);
       if (found==0) return(-1);
      }
    }
     else
    {
    	cout << "\nUser Select Mode:";
     if (f.FirstDep(a)==NULL ||
	  f.FirstArr(b)==NULL) return(-1);

      // Let's try direct connection :
      pd=f.FirstDep(a);
      pa=NULL;
      pos=0;
      do
      {
          if (strcmp(pd->sbCity2,arr)==0 && pos<MAXSELECT)
          {
            	pa=pd;
               rgSelect[pos].cHop=1;
               rgSelect[pos].wFlight1=pd->wFlight;
               pos++;
          }
	  		 pd=pd->pFlightDep;
      } while (pd!=NULL);

      if (pos>0)
      {
            // found some directs !!!
	  		   // Let User Choose:
            do {
             cout << "\nChoose from following flight routes:";
             cout << "\n( Choice    Flight#   Dep.Time   Arr.Time )\n";
             for (count=0;count<pos; count++)
             {
            	cout << count << ".  "<< rgSelect[count].wFlight1;
					pd=f.FlightByNumber(rgSelect[count].wFlight1);
               cout << "  " << pd->tDeparture;
               cout << "  " << pd->tArrival<<"\n";
             }
	          cin >> choice;
            } while (choice>=pos || choice<0);
            r=rgSelect[choice];
	         return(0);
      } else
      // Keep looking for double
      {
       // Go through all of the arrivals from dep[]
       //  and check, if any match any departs from arr[]
       //   boy ... its gonna be fun :
       pd=f.FirstDep(a);
       found=0;
       pos=0;
       do {
	     pa=f.FirstArr(b);
	     do {
		   if (strcmp(pd->sbCity2,pa->sbCity1)==0 && pos<MAXSELECT)
		   // Yey ! .. Found it !
		   {
		     if (TimeDiff(pa->tDeparture,pd->tArrival)>=MINLAYOVER)
           {
               found=1;
			    	rgSelect[pos].wFlight1=pd->wFlight;
			    	rgSelect[pos].wFlight2=pa->wFlight;
			    	rgSelect[pos].cHop=2;
               pos++;
           }
		   }
		   pa=pa->pFlightArr;
	     } while (pa!=NULL);
        pd=pd->pFlightDep;
       } while(pd!=NULL);
       if (found==0) return(-1);
		 // Let User Choose:
       do {
       		 cout << "\n DT-Departure time AT-Arrival time ";
             cout << "\n MC-Midpoint City DC-"<<dep<<" AC-"<<arr;
             cout << "\n\n  Choose from following flight routes:";
             cout << "\n| Choice# | Fl#1 | DT from DC | AT to MC | MC | Fl#2 | DT from MC | AT to AC |\n";
             for (count=0;count<pos; count++)
             {
            	cout << "| "<< count << ".  ";
					pd=f.FlightByNumber(rgSelect[count].wFlight1);
					pa=f.FlightByNumber(rgSelect[count].wFlight2);
               cout << " | " << rgSelect[count].wFlight1;
               cout << " | " << pd->tDeparture;
               cout << " | " << pd->tArrival;
				   cout << " | " << pd->sbCity2;
               cout << " | " << rgSelect[count].wFlight2;
               cout << " | " << pa->tDeparture;
               cout << " | " << pa->tArrival;
               cout << "\n";
             }
             cout << "Make Your Choice (0.."<<pos-1<<"): ";
	          cin >> choice;
       } while (choice>=pos || choice<0);
       r=rgSelect[choice];
	    return(0);
      }
    }
}

// Returns a poiter to new reservation node
ReservationType *Reservations::MakeReserveNode(char *fname, char *lname,
int oneway , RouteType r1, RouteType r2)
{
        ReservationType *temp;
        temp=new(ReservationType);
		  temp->sbFirst= new char[strlen(fname)+1];
		  temp->sbLast= new char[strlen(lname)+1];
        strcpy(temp->sbFirst,fname);
        strcpy(temp->sbLast,lname);
        temp->wType=oneway;
        temp->route1=r1;
        temp->route2=r2;
        temp->pReserve=NULL;
        return(temp);
}

// Delocates all reservations and names
Reservations::~Reservations()
{
	ReservationType *temp,*temp2;
	temp=pReserveHead;
	while (temp!=NULL)
	{
		temp2=temp->pReserve;
 		delete[]temp->sbLast;
		delete[]temp->sbFirst;
		delete(temp);
		temp=temp2;
	}
}


// Initializes all clients on their flights
//   run on start up
int Reservations::SetCounters(Flights &f)
{
	ReservationType *temp;
   RouteType r1,r2;
   FlightType *tf;

	temp=pReserveHead;
	while (temp!=NULL)
   {
   	r1=temp->route1;
     	r2=temp->route2;
		tf=f.FlightByNumber(r1.wFlight1);
      if (tf==NULL) return(-1);
		tf->cPassenger++;
      if (r1.cHop==2)
      {
			tf=f.FlightByNumber(r1.wFlight2);
         if (tf==NULL) return(-1);
			tf->cPassenger++;
      }
      if (temp->wType==ROUNDTRIP)
      {
       		tf=f.FlightByNumber(r2.wFlight1);
		      if (tf==NULL) return(-1);
				tf->cPassenger++;
      		if (r1.cHop==2)
      		{
					tf=f.FlightByNumber(r2.wFlight2);
	            if (tf==NULL) return(-1);
					tf->cPassenger++;
      		}
      }
      temp=temp->pReserve;
   }
   return(0);
}

// Creates a reservation for a new client
// (Uses Find Route)
int Reservations::MakeReserve(Flights &f)
{
    char fname[MAXNAME],lname[MAXNAME],dep[MAXNAME],arr[MAXNAME];
    ReservationType *temp, *tempp,*tempn;
    int way,mm,dd,mm2,dd2,quit;
    RouteType r1,r2;
    FlightType *tf;

    cout<<"\n Enter the first name: ";
    cin>>fname;
    cout<<"\n Enter the last name: ";
    cin>>lname;
    if (FindPerson(fname,lname)!=NULL)
    { 
	// Sorry  !
       cout << "\nSorry, FairAir can provide only one reservation per";
       cout << " customer.";
       return(0);
    }
    cout<<"\n Enter the Departure City: ";
    cin>>dep;    
    cout<<"\n Enter the Arrival City: ";
    cin>>arr;    
    cout<<"\n Type of trip (0-round or 1-oneway): ";
    cin>>way;
    cout<<"\n Month of Travel to "<<arr<<" (1-12): ";
    cin>>mm;
    cout<<"\n Day of Travel to "<<arr<<" (1-31): ";
    cin>>dd;
    do {
     if (way==ROUNDTRIP) 
     {
      cout<<"\n Month of Travel back to "<<dep<<" (1-12): ";
      cin>>mm2;
      cout<<"\n Day of Travel back to "<<dep<<" (1-31): ";
      cin>>dd2;
     }
    } while (mm==mm2 && dd==dd2); 
    //1. Check if we can make it one way:
    if (FindRoute(dep,arr,r1,f)==-1)
    {
     cout << "\nSorry, FairAir can't provide this flight";
     return(0);
    }    
    if (way==ROUNDTRIP)
    {
     //2. Can we make it backwards ?
     if (FindRoute(arr,dep,r2,f)==-1)
     {
      cout << "\nSorry, FairAir can't provide this flight";
      return(0);
     }
    }

    // Create reservation (r1 and r2 are ready):        
    cout << "\n Reservation for "<<fname<<" "<<lname<<" Confirmed.";
    r1.wDay=100*mm+dd;
    r2.wDay=100*mm2+dd2;
    temp=MakeReserveNode(fname, lname, way , r1, r2);

// Inrease Cpassenger's in flights
// put passenger on the counters :

                tf=f.FlightByNumber(r1.wFlight1);
                tf->cPassenger++;
                if (r1.cHop==2)
                {
                   tf = f.FlightByNumber(r1.wFlight2);
                   tf->cPassenger++;
                }
                if (temp->wType==0)
                // Round trip:
                {
                   tf = f.FlightByNumber(r2.wFlight1);
                   tf->cPassenger++;
                   if (r2.cHop==2)
                   {
                    tf = f.FlightByNumber(r2.wFlight2);
                    tf->cPassenger++;
                   }
                }     

    // Plug it in a right spot in list of reservations :
    //   Hell fun !

      // create or (add / sort) to link list ...
      if (pReserveHead!=NULL)
      // Add to link list :
      {
      	// First find the right spot by time sort :
         tempp=pReserveHead;  // previous
         tempn=tempp->pReserve;         // next
         
         if (tempn==NULL || strcmp(tempp->sbLast,lname)>0)
         // Just one person in list
         {
         	if (strcmp(tempp->sbLast,lname)>0)
            // plug in on the beginning
            {
            	pReserveHead=temp;
                temp->pReserve=tempp;
            } else
            // plug in on the back
            {
            	tempp->pReserve=temp;
            }
         } else
         // go on in the loop
         {
           quit=0;
           do
           {
            if (strcmp(tempp->sbLast,lname)<=0 &&
		strcmp(tempn->sbLast,lname)>=0)
               // Plug it in here :
               {
               	  tempp->pReserve=temp;
                  temp->pReserve=tempn;
               	quit=1;
               }
               if (quit==0)
               // move one one spot
               {
		  tempp=tempn;
                  tempn=tempn->pReserve;
               }
               if (tempn==NULL && quit==0)
               // Plug in @the end
               {
               	  tempp->pReserve=temp;
                  quit=1;
               }
           } while (quit==0);
         }
      }
      else
      {
      	// Create new list :
         pReserveHead=temp;
      }
      return(0);
}


/*
 * Writes reservation to file, in alphabetical order.
 */
Reservations::WriteReservations(){
   ReservationType *pTemp;

   ofstream resfile(RESERVEFILE);
   if(!resfile) return(-1); //kill if cannot open file
   else {
   	if(pReserveHead != NULL){ //if there are entries to write
   		for(pTemp = pReserveHead; pTemp != NULL; pTemp = pTemp->pReserve){
            //Write out the part that is the same for all
         	resfile << pTemp->sbFirst << " " << pTemp->sbLast << " " <<
         		pTemp->wType << " " << pTemp->route1.wDay << " " <<
            	pTemp->route1.cHop << " " << pTemp->route1.wFlight1;
            if(pTemp->route1.cHop == 2)
            	resfile << " " << pTemp->route1.wFlight2;
            if(pTemp->wType == ROUNDTRIP) {
               //Write out part that is the same for all roundtrips
            	resfile << " " << pTemp->route2.wDay << " " <<
               	pTemp->route2.cHop << " " << pTemp->route2.wFlight1;
               if(pTemp->route2.cHop == 2)
               	resfile << " " << pTemp->route2.wFlight2;
            }
            resfile << "\n";

			}
      }
      else resfile.close(); //no reservations to write.
      return(0);
	}
}
/*
 *print the list of passengers in alpha order, last name first
 */
void Reservations::PrintPassengers(){
	ReservationType *pTemp;

   if(pReserveHead == NULL)
   	cout << "\nSorry, there are no passengers in the database.\n";
   else
   	for(pTemp = pReserveHead; pTemp != NULL; pTemp = pTemp->pReserve)
      	cout << pTemp->sbLast << ", " << pTemp->sbFirst << "\n";
}

/*
 * PrintSchedule -- prompt the user for a passenger name and print
 * that passenger's schedule.
 */
void Reservations::PrintSchedule(Flights &flightdb){
   FlightType *pFTemp;
	ReservationType *pTemp;
   char sbFirst[MAXNAME], sbLast[MAXNAME];

   //get passenger
	cout << "\nPassenger's first name? ";
   cin >> sbFirst;
   cout << "\nPassenger's last name? ";
   cin >> sbLast;

   pTemp = FindPerson(sbFirst, sbLast);
   if(pTemp == NULL) cout << "\nThat person is not in the database.";
   else{
   	pFTemp = flightdb.FlightByNumber(pTemp->route1.wFlight1);

   	//print out the line (formatted) showing flight info
   	//that is the same for all
   	cout << "\n\nFlight " << pTemp->route1.wFlight1 << " " <<
   		(pTemp->route1.wDay/100) << "/" <<
      	(pTemp->route1.wDay%100) << " depart from " << pFTemp->sbCity1 << " ";
   	PrintTime(pFTemp->tDeparture);
   	cout << " arrive to " << pFTemp->sbCity2 << " ";
   	PrintTime(pFTemp->tArrival);

		//print out additional lines if necessary
   	if(pTemp->route1.cHop == 2){
   		pFTemp = flightdb.FlightByNumber(pTemp->route1.wFlight2);
      	cout << "\n-Flight " << pTemp->route1.wFlight2 << " " <<
      		(pTemp->route1.wDay/100) << "/" <<
         	(pTemp->route1.wDay%100) << " depart from " << pFTemp->sbCity1 << " ";
      	PrintTime(pFTemp->tDeparture);
      	cout << " arrive to " << pFTemp->sbCity2 << " ";
      	PrintTime(pFTemp->tArrival);
   	}
   	if(pTemp->wType == ROUNDTRIP){
   		pFTemp = flightdb.FlightByNumber(pTemp->route2.wFlight1);
			cout << "\nFlight " << pTemp->route2.wFlight1 << " " <<
   			(pTemp->route2.wDay/100) << "/" <<
      		(pTemp->route2.wDay%100) << " depart from " << pFTemp->sbCity1 << " ";
   		PrintTime(pFTemp->tDeparture);
   		cout << " arrive to " << pFTemp->sbCity2 << " ";
   		PrintTime(pFTemp->tArrival);

      	if(pTemp->route2.cHop == 2){
      		pFTemp = flightdb.FlightByNumber(pTemp->route2.wFlight2);
				cout << "\n-Flight " << pTemp->route2.wFlight2 << " " <<
   				(pTemp->route2.wDay/100) << "/" <<
      			(pTemp->route2.wDay%100) << " depart from " << pFTemp->sbCity1 << " ";
   			PrintTime(pFTemp->tDeparture);
   			cout << " arrive to " << pFTemp->sbCity2 << " ";
   			PrintTime(pFTemp->tArrival);
      	}
      }
   }
}

/*
 * DeleteReserve -- delete a reservation for a pallenger by prompting for name
 */
void Reservations::DeleteReserve(Flights &flightdb){
   char sbFirst[MAXNAME], sbLast[MAXNAME];
   ReservationType *pTemp, *pTempPre;
   FlightType *pflight;

	//get passenger
	cout << "\nPassenger\'s first name? ";
   cin >> sbFirst;
   cout << "\nPassenger\'s last name? ";
   cin >> sbLast;

	pTemp = FindPerson(sbFirst, sbLast);
   if(pTemp == NULL) cout << "\nNo such passenger.";
   else{
   	//always clean up the first flight
   	pflight = flightdb.FlightByNumber(pTemp->route1.wFlight1);
		pflight->cPassenger--;

   	//if there are two hops on the way out
   	if(pTemp->route1.cHop == 2){
   		pflight = flightdb.FlightByNumber(pTemp->route1.wFlight2);
			pflight->cPassenger--;
   	}

		if(pTemp->wType == ROUNDTRIP){
   		//once its a round trip, ya gotta clean up the first one
      	pflight = flightdb.FlightByNumber(pTemp->route2.wFlight1);
			pflight->cPassenger--;

      	//if there are two hops comming home
      	if(pTemp->route2.cHop == 2) {
      		pflight = flightdb.FlightByNumber(pTemp->route2.wFlight2);
				pflight->cPassenger--;
      	}
   	}
   	//remove the node
	   cout << "\nReservation for "<<sbFirst<<" "<<sbLast<<" has been deleted.";
   	//special case: first item in list
   	if(pTemp == pReserveHead) {
   		pReserveHead = pTemp->pReserve;
         delete[]pTemp->sbFirst;
         delete[]pTemp->sbLast;
         delete(pTemp);
   	}

   	else{
   		//find the node that precedes the to-be-deleted-node
   		for(pTempPre= pReserveHead; pTempPre != NULL;
         	pTempPre= pTempPre->pReserve){
      		if(pTempPre->pReserve == pTemp) break;
   		}
   		//special case: last item in list
   		if(pTemp->pReserve == NULL){
   			pTempPre->pReserve = NULL;
            delete[]pTemp->sbFirst;
         	delete[]pTemp->sbLast;
      		delete(pTemp);
   		}
   		//normal case
   		else {
				pTempPre = pTemp->pReserve;
            delete[]pTemp->sbFirst;
         	delete[]pTemp->sbLast;
      		delete(pTemp);
   		}
   	}
	}
}
/*
 *FindPerson -- locates the address of the node containing the person
 */
ReservationType *Reservations::FindPerson(char sbFirst[MAXNAME], char sbLast[MAXNAME]){
	ReservationType *pTemp;
   int i;

	//put it in the right format
   toupper(sbFirst[0]);
   toupper(sbLast[0]);
   for(i = 1; i != '\n'; i++)	sbFirst[i] = tolower(sbFirst[i]);
   for(i = 1; i != '\n'; i++) sbLast[i] = tolower(sbLast[i]);

   for(pTemp = pReserveHead; pTemp != NULL; pTemp = pTemp->pReserve){
   	if(!strcmp(pTemp->sbLast, sbLast) && !strcmp(pTemp->sbFirst, sbFirst))
      	return(pTemp);
   }
   return(NULL);
}

// Returns pointer to a new flight node
FlightType *Flights::MakeFlightNode(int wFlight, char *sbCity1, int
tDeparture, char *sbCity2, int tArrival)
{
      FlightType *tempnew;
      tempnew= new(FlightType);
      tempnew->wFlight=wFlight;
      tempnew->tDeparture=tDeparture;
      tempnew->tArrival=tArrival;
      tempnew->sbCity1=new char[strlen(sbCity1)+1];
      tempnew->sbCity2=new char[strlen(sbCity2)+1];
      strcpy(tempnew->sbCity1,sbCity1);
      strcpy(tempnew->sbCity2,sbCity2);
      tempnew->cPassenger=0;
      tempnew->pFlightDep=NULL;
      tempnew->pFlightArr=NULL;
      return(tempnew);
}

// delete the Flight DB by rgFlight[]
Flights::~Flights()
{
	int a;
	for (a=0; a<cFlight; a++)
	{
		delete[]rgFlight[a].pFlight->sbCity1;
		delete[]rgFlight[a].pFlight->sbCity2;
		delete(rgFlight[a].pFlight);
        }
   for (a=0; a<MAXCITY; a++)
   {
    if (rgCity[a].sbCity!=NULL) delete[]rgCity[a].sbCity;
   }
}

// returns index # in rgCity to *city
int Flights::FindCity(char *city)
{
        int i,j;
        j=i=HashCity(city);
        while (rgCity[i].sbCity!=NULL)
        {
         if (rgCity[i].sbCity==NULL) return (-1); else
          if (strcmp(rgCity[i].sbCity,city)==0)
                 return(i);
         i=(i+1)%MAXCITY;
         if (i==j) return(-1);
        }
        return(-1);
}

// Retunrs index first empty spot after (int n)
int Flights::FirstEmpty(int n)
{
	int i;
	i=n;
	do{
           if (rgCity[i].sbCity==NULL) return(i);
	   i=(i+1)%MAXCITY;
	}while (i!=n);
        return(-1);
}

// Initialize the Flight DB + create all Flight structures
Flights::Flights()
{
	ifstream flightfile(FLIGHTFILE);
   int wFlight, tDeparture, tArrival, start, found, timex, quit;
   char sbCity1[MAXNAME], sbCity2[MAXNAME];
   FlightType *temp, *tempp, *tempn;
   int a, b, min, pos;

   // Initialize all members:
   cFlight=0;
   for (start=0; start<MAXCITY; start++)
   {
    rgCity[start].sbCity=NULL;
      rgCity[start].pFlightDep=NULL;
		rgCity[start].pFlightArr=NULL;
   }
   for (start=0; start<MAXFLIGHT; start++)
   {
    rgFlight[start].wFlight=0;
    rgFlight[start].pFlight=NULL;
   }

   // Open DB:
   if (!flightfile)
   {
   	cerr << "\n Error : Could not open the file "<<FLIGHTFILE<<" for reading.";
      exit(1);
   }
   cout << "\nLoading Flight Database ";
   while (1)
   {
   	flightfile >> wFlight >> sbCity1 >> tDeparture >> sbCity2 >> tArrival;
      if (flightfile.eof()) break;
      // Put it in the DB :
      cout << ".";
      // create new flight :
      temp=MakeFlightNode(wFlight,sbCity1,tDeparture,sbCity2,tArrival);

   	// put the dep city in the rgCity[]
   	// See if the city is already in rgCity[] :

        found=FindCity(sbCity1);
	if (found==-1) start=FirstEmpty(HashCity(sbCity1)); else
          start=found;
        if (start==-1)
	{ cout << "\n ERROR !!! Database overfill.";
	  exit (1);
        }
      if (found==-1)
{      	// Create New City in rgCity[]
         rgCity[start].sbCity=new char[strlen(sbCity1)+1];
         strcpy(rgCity[start].sbCity,sbCity1);}
      // create or (add / sort) to link list ...

      if (rgCity[start].pFlightDep!=NULL)
      // Add to link list :
      {

      	// First find the right spot by time sort :
         tempp=rgCity[start].pFlightDep;  // previous
         tempn=tempp->pFlightDep;         // next
         timex=tDeparture;
         if (tempn==NULL || tempp->tDeparture>=timex)
         // Just one Flight
         {
         	if (tempp->tDeparture>timex)
            // plug in on the beginning
            {
            	rgCity[start].pFlightDep=temp;
               temp->pFlightDep=tempp;
            } else
            // plug in on the back
            {
            	tempp->pFlightDep=temp;
            }
         } else
         // go on in the loop
         {
           quit=0;
           do
           {
           		if (tempp->tDeparture<=timex &&
			tempn->tDeparture>=timex)
               // Plug it in here :
               {
               	tempp->pFlightDep=temp;
                  temp->pFlightDep=tempn;
               	quit=1;
               }
               if (quit==0)
               // move one one spot
               {
						tempp=tempn;
                  tempn=tempn->pFlightDep;
               }
               if (tempn==NULL && quit==0)
               // Plug in @the end
               {
               	tempp->pFlightDep=temp;
                  quit=1;
               }
           } while (quit==0);
         }
      }
      else
      {
      	// Create new list :
         rgCity[start].pFlightDep=temp;
      }

      // Do the same for the arrival City :
      // put the dep city in the rgCity[]
   	// See if the city is already in rgCity[] :
        found=FindCity(sbCity2);
	if (found==-1) start=FirstEmpty(HashCity(sbCity2)); else
          start=found;
        if (start==-1)
	{ cout << "\n ERROR !!! Database overfill.";
	  exit (1);
        }
      if (found==-1)
{
  
    	// Create New City in rgCity[]
         rgCity[start].sbCity=new char[strlen(sbCity2)+1];
     strcpy(rgCity[start].sbCity,sbCity2);}
      // create or (add / sort) to link list ...
      if (rgCity[start].pFlightArr!=NULL)
      // Add to link list :
      {

      	// First find the right spot by time sort :
         tempp=rgCity[start].pFlightArr;  // previous
         tempn=tempp->pFlightArr;         // next
         timex=tArrival;
         if (tempn==NULL || tempp->tArrival>=timex)
         // Just one Flight
         {
         	if (tempp->tArrival>timex)
            // plug in on the beginning
            {
            	rgCity[start].pFlightArr=temp;
               temp->pFlightArr=tempp;
            } else
            // plug in on the back
            {
            	tempp->pFlightArr=temp;
            }
         } else
         // go on in the loop
         {
           quit=0;
           do
           {
           		if (tempp->tArrival<=timex &&
tempn->tArrival>=timex)
               // Plug it in here :
               {
               	tempp->pFlightArr=temp;
                  temp->pFlightArr=tempn;
               	quit=1;
               }
               if (quit==0)
               // move one one spot
               {
						tempp=tempn;
                  tempn=tempn->pFlightArr;
               }
               if (tempn==NULL && quit==0)
               // Plug in @the end
               {
               	tempp->pFlightArr=temp;
                  quit=1;
               }
           } while (quit==0);
         }
      }
      else
      {
      	// Create new list :
         rgCity[start].pFlightArr=temp;
      }

      // Add the Flight to the rgFlight[]:
      rgFlight[cFlight].wFlight=wFlight;
      rgFlight[cFlight].pFlight=temp;
      cFlight++;
   }
   flightfile.close();
   if (cFlight>1)
   // Sort rgFlight[] :
   {
	for (a=0; a<cFlight; a++)
	{
		min=rgFlight[a].wFlight;
		pos=a;
		for (b=a; b<cFlight; b++)
		{
			if (rgFlight[b].wFlight<min)
			{
			   pos=b;
   			   min=rgFlight[b].wFlight;
			}
		}
		min=rgFlight[a].wFlight;
		temp=rgFlight[a].pFlight;
		rgFlight[a].wFlight=rgFlight[pos].wFlight;
		rgFlight[a].pFlight=rgFlight[pos].pFlight;
		rgFlight[pos].wFlight=min;
		rgFlight[pos].pFlight=temp;		
	}
   }
}

// Returns pointer to forst Departire flight
//  from the rgcity[n]
FlightType *Flights::FirstDep(int n)
{
 return(rgCity[n].pFlightDep);
}

// Returns pointer to forst arrival flight
//  from the rgcity[n]
FlightType *Flights::FirstArr(int n)
{ 
 return(rgCity[n].pFlightArr);
}

// Prints out list of cities in srCity[]
void Flights::PrintCityList()
{
   int a;
   cout << "\n\n List of Cities :";
   for (a=0; a<MAXCITY; a++)
     if (rgCity[a].sbCity!=NULL) cout << "\n" << rgCity[a].sbCity;
}

// Prints out City Departures
void *Flights::CityDepartureList()
{
   char name[MAXNAME];
   int found,start;
   FlightType *temp;

   cout << "\n\nEnter a city for its departures: ";
   cin >> name;

   // See if the city is in rgCity[] :
        found=FindCity(name);
        start=found;
   if (found==-1)
   // Not found
   {
	cout << "\n" << name << " city does not exist in Database";
   } else
   {
 	if (rgCity[start].pFlightDep==NULL)
	// No dep's
	{
	  cout << "\n" << name << " city does not have any departures";
        } else
        // Go through the linklist
        {
cout << "\n"<< name<< " city departures:\n(Dep Time     Flight#     ";
cout << "Arrival City     # of Passengers)";
	   temp=rgCity[start].pFlightDep;
	   do{
cout << "\n";
PrintTime(temp->tDeparture);
cout<<" "<<temp->wFlight<<" "; 
cout<<temp->sbCity2<<" ("<<temp->cPassenger<<")";
               temp = temp->pFlightDep;
	   } while (temp!=NULL);
        }
   }
}

// Prints out City Arrivals
void *Flights::CityArrivalList()
{
   char name[MAXNAME];
   int found,start;
   FlightType *temp;

   cout << "\n\nEnter a city for its arrivals: ";
   cin >> name;

   // See if the city is in rgCity[] :
        found=FindCity(name);
        start=found;
   if (found==-1)
   // Not found
   {
	cout << "\n" << name << " city does not exist in Database";
   } else
   {
 	if (rgCity[start].pFlightArr==NULL)
	// No arr's
	{
	  cout << "\n" << name << " city does not have any arrivals";
        } else
        // Go through the linklist
        {
cout << "\n"<<name<<" city arrivals:\n(Arr Time   Flight#  ";
cout <<"Departure City    # of Passengers)";
	   temp=rgCity[start].pFlightArr;
	   do{
cout << "\n";
PrintTime(temp->tArrival);
cout<<" "<<temp->wFlight<<" ";
cout<<temp->sbCity1<<" ("<<temp->cPassenger<<")";
               temp = temp->pFlightArr;
	   } while (temp!=NULL);
        }
   }
}

// Returns pointer to flight number n
// or NULL if not in DB
FlightType *Flights::FlightByNumber(int n)
{
	int first,last,mid,found;
	first=0;
	last=cFlight-1;
	found=0;
	while (found==0 && first<=last)
	{
		mid = (first + last) /2;
		if (n<rgFlight[mid].wFlight) last = mid-1; else
		 if (n>rgFlight[mid].wFlight) first = mid+1; else
		  found=1;
	}
	if (found==0) return(NULL); else
	 return(rgFlight[mid].pFlight);
}
