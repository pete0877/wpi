#include "airline.h"

extern int HashCity(char *name);
extern void PrintTime(int t);
extern int TimeDiff(int t2, int t1);

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
