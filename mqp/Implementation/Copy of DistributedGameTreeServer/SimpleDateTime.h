// SimpleDateTime.h
//

#if !defined(SimpleDateTime_h)
#define SimpleDateTime_h

#include <afx.h>
#include <time.h>
#include <sys/types.h>
#include <sys/timeb.h>

// SimpleDateTime Class keeps track date and time up to milisec.
// This class works only for dates before 1990.

class SimpleDateTime
{
public:
	void setToNow();

	/*
     * SimpleDateTime() - default constructor. Sets the stored time to current system time.
     */
    SimpleDateTime();

	/*
     * toString() - Converts the stored time into a string.
     */
    CString toString();

	/*
     * add() - adds given number of milisec to the stored date
     */
    void add(long milisec);

	/*
     * operator-() - substracts two times from eachother and returns the result as long (in milisec).
	 *		both dates have to be after 1990.
     */
    long operator-(const SimpleDateTime& simpledatetime);

	/*
     * operator+() - adds given number of milisec to the stored date
     */
    const SimpleDateTime& operator+(long milisec);

	/*
     * operator=() - sets one stored time to another
     */
    const SimpleDateTime& operator=(const SimpleDateTime& simpledatetime);

	/*
     * operator==() - standard comparison operator
     */
    boolean operator==(const SimpleDateTime& simpledatetime);

	/*
     * operator!=() - standard comparison operator
     */
    boolean operator!=(const SimpleDateTime& simpledatetime);

	/*
     * operator>() - standard comparison operator
     */
    boolean operator>(const SimpleDateTime& simpledatetime);

	/*
     * operator<() - standard comparison operator
     */
    boolean operator<(const SimpleDateTime& simpledatetime);

	/*
     * operator>=() - standard comparison operator
     */
    boolean operator>=(const SimpleDateTime& simpledatetime);

	/*
     * operator<=() - standard comparison operator
     */
    boolean operator<=(const SimpleDateTime& simpledatetime);

	// create same operators for pointers to instances since we
	// will need it anyway:

	/*
     * operator-() - substracts two times from eachother and returns the result as long (in milisec).
	 *		both dates have to be after 1990.
     */
    long operator-(SimpleDateTime* simpledatetime);	

	/*
     * operator=() - sets one stored time to another
     */
    const SimpleDateTime& operator=(SimpleDateTime* simpledatetime);	

	/*
     * operator==() - standard comparison operator
     */
    boolean operator==(SimpleDateTime* simpledatetime);	

	/*
     * operator!=() - standard comparison operator
     */
    boolean operator!=(SimpleDateTime* simpledatetime);	

	/*
     * operator>() - standard comparison operator
     */
    boolean operator>(SimpleDateTime* simpledatetime);	

	/*
     * operator<() - standard comparison operator
     */
    boolean operator<(SimpleDateTime* simpledatetime);	

	/*
     * operator>=() - standard comparison operator
     */
    boolean operator>=(SimpleDateTime* simpledatetime);	

	/*
     * operator<=() - standard comparison operator
     */
    boolean operator<=(SimpleDateTime* simpledatetime);	

	/*
     * ~SimpleDateTime() - default destructor.
     */
    ~SimpleDateTime();

	/*
     * getMiliTime() - returns current date / time in milisec.
     */
	double			getMiliTime();

protected:
	SYSTEMTIME*		thetime;
	int				days_in_month;
	double			milisecs;



private:


};

#endif /* SimpleDateTime_h */
