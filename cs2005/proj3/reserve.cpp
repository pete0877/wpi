#include "airline.h"

extern int HashCity(char *name);
extern void PrintTime(int t);
extern int TimeDiff(int t2, int t1);
extern int mode;

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
         route1.cHop = 1;
         route2.cHop = 1;

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
               cout << "  ";
	       PrintTime(pd->tDeparture);
               cout << "  ";
	       PrintTime(pd->tArrival);
	       cout << "\n";
             }
cout << "Make Your Choice (0.."<<pos-1<<"): ";
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
             cout << "\n
#  | Fl#1| DT frm DC | AT to MC |  MC       | Fl#2| DT from MC|AT to AC|\n";
             for (count=0;count<pos; count++)
             {
                printf( "%2d |", count);
                pd=f.FlightByNumber(rgSelect[count].wFlight1);
                pa=f.FlightByNumber(rgSelect[count].wFlight2);
                printf(" %3d |  ",rgSelect[count].wFlight1);
                PrintTime(pd->tDeparture);
                cout << "  | ";
                PrintTime(pd->tArrival);
                printf("  |%11s",pd->sbCity1);
                printf("| %3d |  ",rgSelect[count].wFlight2);
                PrintTime(pa->tDeparture); 
                cout << "  |";
                PrintTime(pa->tArrival);
               cout << " |\n";
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
ReservationType *Reservations::FindPerson(char *sbFirst, char *sbLast)
{
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
