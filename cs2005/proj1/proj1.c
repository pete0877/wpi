#include <iostream.h>  
#include <math.h>
/*
 * /cs/cs2005/pub/example/poly.C -- read and print a polynomial in C++
 *
 * programmer -- Uzma Ali
 *
 * date -- November 8, 1996
 *
 * modification history
 */


#define MAXCOEFF 10       // maximum number of coefficients

#define FALSE 0
#define TRUE 1

typedef short Boolean;

typedef float CoeffList[MAXCOEFF];   // for storing the list of
coefficients

void PrintPoly(CoeffList rgCoeff, int wDegree);
void ReadPoly(CoeffList rgCoeff, int &wDegree);
void PrintMenu();
void DiffPoly(CoeffList rgCoeff, int wDegree, CoeffList rgCoeffDiff, int
&wDegreeDiff);
void IntPoly(CoeffList rgCoeff, int wDegree, CoeffList rgCoeffInt, int
&wDegreeInt);
float EvalPoly(CoeffList rgCoeff, int wDegree, float x);

void main()
{
  CoeffList poly,integ,dirr;
  int wDegree,dd,id;
  char key;
  float xx,x,f;

  PrintMenu();
  do{              
    cout << "\nEnter Command (2-8, 0-help, 1-exit): ";
    cin >> key;
    switch(key){
    case '0': PrintMenu();
    	break;
    case '2': ReadPoly(poly, wDegree);
    	break;
    case '3': cout << "f(x)=";
			  PrintPoly(poly, wDegree);
    	break;
    case '4': cout<< "\nEnter the point for evaluation: ";
			  cin>>x;
              f= EvalPoly(poly, wDegree, x);
	     	  cout<<"\nf("<<x<<")="<<f;
    	break;
    case '5': DiffPoly(poly, wDegree, dirr, dd);
        	  cout<<"f'(x)=";
			  PrintPoly(dirr, dd);
    	break;
    case '6': cout<< "\nEnter the point for evaluation: ";
			  cin>>x;
              f= EvalPoly(dirr, dd, x);
	          cout<<"\nf'("<<x<<")="<<f;
    	break;
    case '7': IntPoly(poly, wDegree, integ,id);
        	  cout<<"F(x)=";
			  PrintPoly( integ, id);
        break;
    case '8': cout<< "\nEnter the first point of the interval: ";
			  cin>>x;
	 		  cout<< "\nEnter the second point of the
interval: ";
			  cin>>xx;
              f= EvalPoly(integ, id, xx) -EvalPoly(integ, id, x);
	          cout<<"\nF("<<xx<<")-F("<<x<<")="<<f;
     	break;
    }
  } while(key != '1');
}
   
 // ReadPoly -- read and store the contents of a polynomial along with its
 //     degree.

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

    // insert code for PrintPoly() from your lab handout here
    for (i = wDegree; i >= 0; i--) {
      cout << " + " << rgCoeff[i] << "x^" << i;
    }
    cout << "\n";
}

void PrintMenu()
{
   cout << "Menu :\n\n";
   cout << " 0. Print this menu.\n";
   cout << " 1. Exit.\n";
   cout << " 2. Input new polynomial.\n";
   cout << " 3. Print the current polynomial.\n";
   cout << " 4. Evaluate the current polynomial at point and print the
result.\n";
   cout << " 5. Differentiate the current polynomial and print the
resulting polynomial.\n";
   cout << " 6. Evaluate the differential at a point and print the
result.\n";
   cout << " 7. Integrate the current polynomial and print the resulting
polynomial.\n";
   cout << " 8. Evaluate the integral over an interval and print the
result.\n";    
}

void DiffPoly(CoeffList rgCoeff, int wDegree, CoeffList rgCoeffDiff, int
&wDegreeDiff)
{   
   int a;
   wDegreeDiff=wDegree-1;
   for (a=0; a<wDegree; a++)
   rgCoeffDiff[a]=rgCoeff[a+1]*float(a+1);

}

void IntPoly(CoeffList rgCoeff, int wDegree, CoeffList rgCoeffInt, int
&wDegreeInt)
{
	int a;                                                                      
	wDegreeInt=wDegree+1;
    for (a=1; a<=wDegreeInt; a++) rgCoeffInt[a]=rgCoeff[a-1]/float(a);
}

float EvalPoly(CoeffList rgCoeff, int wDegree, float x)
{
  int a;
  float sum=0;
  for (a=0; a<=wDegree; a++)
sum=sum+float(rgCoeff[a]*pow(double(x),double(a)));                            
  return(sum);
}
