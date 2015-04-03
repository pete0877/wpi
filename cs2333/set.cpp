////////////////////////////////////////////////////////////////////////////////
// Program    : Sets Example
// Author     : Peter Golaszewski (peterg@wpi.edu)
// Course     : CS2333 B97, Homework #2, Problem 2
// File Name  : set.cpp
// Secription : This program defines simple abstract data type called set_type,
//		which can simulate few basic operations on sets such as:
//		MakeNull, Insert, Member, and Show.
////////////////////////////////////////////////////////////////////////////////

#include <iostream.h>
#include <limits.h>

#define LIMIT 100	     // This defines (maximum size of the set - 1)

// Define the abstract data type
typedef struct
{
	int elements[LIMIT]; // Array of integers
	int empty_idx;	     // Index to the first empty spot in the array
	                     // (1 if the set is empty)
} set_type;

////////////////////////////////////////////////////////////////////////////////

// MakeNull(A) - Empties out set [A]
void MakeNull(set_type *set)
{
	set->elements[0] = INT_MIN;
	set->empty_idx = 1;
}

// Member(e,A) - Return TRUE iff [e] is in [A]
int Member(int element, set_type *set)
{
	int i;
	for (i=set->empty_idx - 1; set->elements[i] > element; i--);
	return (set->elements[i] == element);
}

// Insert(e,A) - Inserts new element [e] into set [A]
void Insert(int element, set_type *set)
{
	int i,j;
	for (i=set->empty_idx - 1; set->elements[i] > element; i--);
	if (!(set->elements[i] == element))
	{
   	i++;
		for (j=set->empty_idx; j>=i; j--)
		set->elements[j] = set->elements[j-1];
		set->elements[i] = element;
		set->empty_idx++;
	}
}

// Show(A) - displays the content of the set [A]
void Show(set_type *set)
{
	int i;
	cout << "\n Elements of the set: ";

	for (i=1; i < set->empty_idx; i++) cout << set->elements[i] << " ";
}

////////////////////////////////////////////////////////////////////////////////

// Main Driver program:
void main ()
{
	set_type *My_set;
   My_set = new set_type;

	MakeNull(My_set);
	Show(My_set);

	Insert(1,My_set);
	Show(My_set);

	Insert(5,My_set);
	Show(My_set);

	Insert(1,My_set);
	Show(My_set);

	Insert(4,My_set);
	Show(My_set);

	// check if the member function works:

	if (Member(5,My_set))
	{ cout << "\n 5 is member of the set"; } else
	{ cout << "\n 5 is NOT member of the set"; }

	if (Member(6,My_set))
	{ cout << "\n 6 is member of the set"; } else
	{ cout << "\n 6 is NOT member of the set"; }

	// Dellocate the memory assigned for the Set
   delete My_set;

   // Wait for user to terminate the program.
   int temp;
   cin >> temp;
}

////////////////////////////////////////////////////////////////////////////////