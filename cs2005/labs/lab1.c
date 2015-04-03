/*
 * /cs/cs2005/pub/example/poly.C -- read and print a polynomial in C++
 *
 * programmer -- Craig Wills
 *
 * date -- October 28, 1996
 *
 * modification history
 */

#define MAXCOEFF 10       // maximum number of coefficients

#include <iostream.h>

#define FALSE 0
#define TRUE 1
typedef short Boolean;

typedef float CoeffList[MAXCOEFF];   // for storing the list of coefficients

// function prototypes
void ReadPoly(CoeffList, int &);
void PrintPoly(CoeffList, int);


void main()
{
    CoeffList rgCoeff;
    int wDegree;
    
    /* read in and print a polynomial */
    ReadPoly(rgCoeff, wDegree);
    PrintPoly(rgCoeff, wDegree);
}

/*
 * ReadPoly -- read and store the contents of a polynomial along with its
 *     degree.
 */
void ReadPoly(CoeffList rgCoeff, int &wDegree)
{
    Boolean bOk;		// is the degree ok?
    int i;

    // obtain the degree of the polynomial
    bOk = FALSE;
    while (!bOk) {
	cout << "What is the degree of your polynomial? \n";
	cin >> wDegree;
	if ((wDegree < 0) || (wDegree >= MAXCOEFF))
	    cout << wDegree << "is not in the allowed degree range of 0-"
		<< MAXCOEFF-1 << "\n";
	else
	    bOk = TRUE;
    }

    // obtain the coefficients of the polynomial
    for (i = wDegree; i >= 0; i--) {
	cout << "Enter the coefficient for the degree " << i << " term: ";
	cin >> rgCoeff[i];
    }
}    

//
// PrintPoly -- print the contents of a polynomial given the coefficient
//    list and the polynomial degree (in a rather ugly manner).
//
void PrintPoly(CoeffList rgCoeff, int wDegree)
{
    int i;
    cout << "f(x) =";
for (i=wDegree; i>=0; i--)
{ cout << "+" << rgCoeff[i] << "x^" << i;}
cout << "\n";
}
