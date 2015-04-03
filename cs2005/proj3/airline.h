#include <iostream.h>
#include <fstream.h>
#include <ctype.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#define FLIGHTFILE "/cs/cs2005/pub/example/fairair"
#define RESERVEFILE "reservations"// Reservations DB
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

extern int HashCity(char *name);
extern void PrintTime(int t);
extern int TimeDiff(int t2, int t1);
