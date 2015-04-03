// SimpleDateTime.cpp
//

// TODOS:
// - Account for leap years

#include "stdafx.h"
#include "SimpleDateTime.h"
#include <stdio.h>

SimpleDateTime::SimpleDateTime()
{
	// get the current time:
	thetime = new SYSTEMTIME;
	GetSystemTime(thetime);		

	// adjust the structure, so that all data members start at 0:
	thetime->wDay--;
	thetime->wMonth--;

	// find the number of milisecs since:
	this->milisecs = this->getMiliTime();

	// calculate number of days in the month:
	days_in_month = 31;
	int month = thetime->wMonth + 1;
	if (month>=8) month++;
	if (month % 2 == 0) days_in_month = 30; 
	if (month == 2) days_in_month = 28;
}

CString SimpleDateTime::toString()
{
	CString result;
	char tmp[50];	
	// format the stored time to include date, time and milisec:
	sprintf(tmp, "%d/%d/%d, %d:%d:%d, %d msec", thetime->wMonth + 1, thetime->wDay + 1, thetime->wYear, thetime->wHour, thetime->wMinute, thetime->wSecond, thetime->wMilliseconds);
	result = tmp; 
	return result;	
}


void SimpleDateTime::add(long milisec) 
{
	if (milisec<=0) return;

	long tmp;

	// add the number of milisecs to the milisec count
	this->milisecs =+ milisec;

	// add the milisec to the stored time:
	tmp = this->thetime->wMilliseconds + milisec;
	this->thetime->wMilliseconds = tmp % 1000;

	tmp = this->thetime->wSecond + (tmp / 1000);
	this->thetime->wSecond = tmp % 60;

	tmp = this->thetime->wMinute + (tmp / 60);
	this->thetime->wMinute = tmp % 60;

	tmp = this->thetime->wHour + (tmp / 60);
	this->thetime->wHour = tmp % 24;

	tmp = this->thetime->wDay + (tmp / 24);
	this->thetime->wDay = tmp % days_in_month;

	tmp = this->thetime->wMonth + (tmp / days_in_month);
	this->thetime->wMonth = (tmp % 12) ;

	tmp = this->thetime->wYear + (tmp / 12);	

	// re-calculate number of days in the month:
	days_in_month = 31;
	int month = thetime->wMonth + 1;
	if (month>=8) month++;
	if (month % 2 == 0) days_in_month = 30; 
	if (month == 2) days_in_month = 28;

	return;
}

const SimpleDateTime& SimpleDateTime::operator+(long milisec) 
{
	if (milisec<=0) return *this;

	long tmp;

	// add the number of milisecs to the milisec count
	this->milisecs =+ milisec;

	// add the milisec to the stored time:
	tmp = this->thetime->wMilliseconds + milisec;
	this->thetime->wMilliseconds = tmp % 1000;

	tmp = this->thetime->wSecond + (tmp / 1000);
	this->thetime->wSecond = tmp % 60;

	tmp = this->thetime->wMinute + (tmp / 60);
	this->thetime->wMinute = tmp % 60;

	tmp = this->thetime->wHour + (tmp / 60);
	this->thetime->wHour = tmp % 24;

	tmp = this->thetime->wDay + (tmp / 24);
	this->thetime->wDay = tmp % days_in_month;

	tmp = this->thetime->wMonth + (tmp / days_in_month);
	this->thetime->wMonth = (tmp % 12) ;

	tmp = this->thetime->wYear + (tmp / 12);	

	// re-calculate number of days in the month:
	days_in_month = 31;
	int month = thetime->wMonth + 1;
	if (month>=8) month++;
	if (month % 2 == 0) days_in_month = 30; 
	if (month == 2) days_in_month = 28;

	return *this;
}

long SimpleDateTime::operator-(const SimpleDateTime& simpledatetime)
{
	return (long) (this->milisecs - simpledatetime.milisecs);
}

const SimpleDateTime& SimpleDateTime::operator=(const SimpleDateTime& simpledatetime)
{
	if (this == &simpledatetime) return *this;	
	this->milisecs					= simpledatetime.milisecs;	
	this->thetime->wMilliseconds	= simpledatetime.thetime->wMilliseconds;
	this->thetime->wSecond			= simpledatetime.thetime->wSecond;
	this->thetime->wMinute			= simpledatetime.thetime->wMinute;
	this->thetime->wHour			= simpledatetime.thetime->wHour;
	this->thetime->wDay				= simpledatetime.thetime->wDay;
	this->thetime->wMonth			= simpledatetime.thetime->wMonth;
	this->thetime->wYear			= simpledatetime.thetime->wYear;
	return *this;
}

boolean SimpleDateTime::operator==(const SimpleDateTime& simpledatetime)
{			
	return (this->milisecs==simpledatetime.milisecs); 		
}

boolean SimpleDateTime::operator!=(const SimpleDateTime& simpledatetime)
{			
	return (this->milisecs!=simpledatetime.milisecs); 		
}

boolean SimpleDateTime::operator>(const SimpleDateTime& simpledatetime)
{		
	return (this->milisecs>simpledatetime.milisecs);		
}

boolean SimpleDateTime::operator<(const SimpleDateTime& simpledatetime)
{		
	return (this->milisecs<simpledatetime.milisecs);
}

boolean SimpleDateTime::operator>=(const SimpleDateTime& simpledatetime)
{		
	return (this->milisecs>=simpledatetime.milisecs);		
}

boolean SimpleDateTime::operator<=(const SimpleDateTime& simpledatetime)
{		
	return (this->milisecs<=simpledatetime.milisecs);
}


// here go the pointer-based versions of the operators:

long SimpleDateTime::operator-(SimpleDateTime* simpledatetime)
{
	return (long) (this->milisecs - simpledatetime->milisecs);
}

const SimpleDateTime& SimpleDateTime::operator=(SimpleDateTime* simpledatetime)
{
	if (this == simpledatetime) return *this;	
	this->milisecs					= simpledatetime->milisecs;	
	this->thetime->wMilliseconds	= simpledatetime->thetime->wMilliseconds;
	this->thetime->wSecond			= simpledatetime->thetime->wSecond;
	this->thetime->wMinute			= simpledatetime->thetime->wMinute;
	this->thetime->wHour			= simpledatetime->thetime->wHour;
	this->thetime->wDay				= simpledatetime->thetime->wDay;
	this->thetime->wMonth			= simpledatetime->thetime->wMonth;
	this->thetime->wYear			= simpledatetime->thetime->wYear;
	return *this;
}

boolean SimpleDateTime::operator==(SimpleDateTime* simpledatetime)
{			
	return (this->milisecs==simpledatetime->milisecs); 		
}

boolean SimpleDateTime::operator!=(SimpleDateTime* simpledatetime)
{			
	return (this->milisecs!=simpledatetime->milisecs); 		
}

boolean SimpleDateTime::operator>(SimpleDateTime* simpledatetime)
{		
	return (this->milisecs>simpledatetime->milisecs);		
}

boolean SimpleDateTime::operator<(SimpleDateTime* simpledatetime)
{		
	return (this->milisecs<simpledatetime->milisecs);
}

boolean SimpleDateTime::operator>=(SimpleDateTime* simpledatetime)
{		
	return (this->milisecs>=simpledatetime->milisecs);		
}

boolean SimpleDateTime::operator<=(SimpleDateTime* simpledatetime)
{		
	return (this->milisecs<=simpledatetime->milisecs);
}


double SimpleDateTime::getMiliTime() 
{
	time_t ltime;
	_timeb tstruct;
    _tzset();
	time(&ltime);		
    struct tm* today = localtime( &ltime );
    double sec = today->tm_sec;
	_ftime( &tstruct );
	double milli = tstruct.millitm;
    double theResult = (sec*1000) + milli;    
	return(theResult);
}

SimpleDateTime::~SimpleDateTime()
{
	delete thetime;	
}

void SimpleDateTime::setToNow()
{
	delete thetime;

	// get the current time:
	thetime = new SYSTEMTIME;
	GetSystemTime(thetime);		

	// adjust the structure, so that all data members start at 0:
	thetime->wDay--;
	thetime->wMonth--;

	// find the number of milisecs since:
	this->milisecs = this->getMiliTime();

	// calculate number of days in the month:
	days_in_month = 31;
	int month = thetime->wMonth + 1;
	if (month>=8) month++;
	if (month % 2 == 0) days_in_month = 30; 
	if (month == 2) days_in_month = 28;
}
