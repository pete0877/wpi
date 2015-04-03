/*
 * /cs/cs2005/pub/labs/lab7.C
 *
 * usage: lab7 sorttype count
 */

#include <iostream.h>
#include <stdlib.h>
#include <math.h>

typedef short BOOLEAN;
#define TRUE 1
#define FALSE 0

#define MAXINT 10000

void Swap(int [], int, int);
void BubbleSort(int [], int);
void SelectionSort(int [], int);
void MergeSort(int [], int, int);
void Merge(int [], int, int, int);
void QuickSort(int [], int, int);
int Partition(int [], int, int);
int GetRanInt(int);

int cComp;			/* count the comparisons */

main(int argc, char *argv[])
{
    int rgInt[MAXINT], cnt, i;
    char *sbType;		/* type of sort */
    BOOLEAN bTrace;
    
    cnt = 0;

    if (argc < 3) {
	cout << "syntax: lab7 sorttype count\n";
	exit(1);		/* no type given */
    }
    else {
	sbType = argv[1];
	if ((cnt = atoi(argv[2])) <= 0) {
	    cout << "Invalid count specified (must be in range 0-" <<
		MAXINT << ":" << argv[2] << "\n";
	    exit(1);
	}
    }

    /* use a random generator to put elements in the array */
    for (i = 0; i < cnt; i++)
	rgInt[i] = GetRanInt(cnt);

    if (strcmp(sbType, "bubble") == 0)
	BubbleSort(rgInt, cnt);
    else if (strcmp(sbType, "selection") == 0)
	SelectionSort(rgInt, cnt);
    else if (strcmp(sbType, "merge") == 0)
	MergeSort(rgInt, 0, cnt-1);
    else if (strcmp(sbType, "quick") == 0)
	QuickSort(rgInt, 0, cnt-1);
    else {
	cout << "Unknown sort type: "<< sbType << "\n";
	exit(1);
    }
    if (cnt <= 10) {
	cout << "Ordered list:";
	for (i = 0; i < cnt; i++)
	    cout << " " <<  rgInt[i];
	cout << "\n";
    }
    cout << cnt << " " << cComp << "\n";
}

/*
 * Swap -- swap two elements of the array
 */
void Swap(int rgInt[], int i, int j)
{
    int tmp;

    tmp = rgInt[i];
    rgInt[i] = rgInt[j];
    rgInt[j] = tmp;
}

/*
 * BubbleSort -- use bubblesort to sort an array
 */
void BubbleSort(int rgInt[], int cnt)
{
    int i, j;

    for (i = cnt-1; i > 0; i--) {
	for (j = 0; j < i; j++) {
	    cComp++;
	    if (rgInt[j] > rgInt[j+1])
		Swap(rgInt, j, j+1);
	}
    }
}

/*
 * MaxKey -- return the index of the maximum value in the range
 */
int MaxKey(int rgInt[], int low, int high)
{
    int i, max;

    max = low;
    for (i = low + 1; i <= high; i++) {
	cComp++;
	if (rgInt[max] < rgInt[i])
	    max = i;
    }
    return(max);
}

/*
 * SelectionSort -- use selectionsort to sort an array
 */
void SelectionSort(int rgInt[], int cnt)
{
    int i, j;

    for (i = cnt-1; i > 0; i--) {
	j = MaxKey(rgInt, 0, i);
	Swap(rgInt, i, j);
    }
}

/* MergeSort --
 * Programmer: David Finkel
 * Parameters: array to be sorted, low and high limits of indices to
 *             be sorted
 */
void MergeSort(int rgInt[], int low, int high)
{
    int middle;

    if (low >= high)		/* if list is empty or has only one element */
	return;
    else {
	middle = (low + high) / 2;
	MergeSort(rgInt, low, middle);
	MergeSort(rgInt, middle + 1, high);
	Merge(rgInt, low, middle, high);
    }
}

/* Merge --
 * Programmer: David Finkel
 * merges two sorted portions of the rgInt array, the first from low
 * to middle, and the second from middle + 1 to high
 */
void Merge(int rgInt[], int low, int middle, int high)
{
    int first, second;    /* indices into the two portions of the array */
    int rgTemp[MAXINT];    /* array to hold sorted elements  */
    int third;            /* index into rgTemp array */
    int i;                /* loop index */

    first = low;
    second = middle + 1;
    third = low;
    
    while ((first <= middle) && (second <= high)) { /* while sub-lists are
						       not exhausted */
	cComp++;
	if ( rgInt[first] <= rgInt[second] ) { /* copy smaller elt into rgTemp */
	    rgTemp[third] = rgInt[first];
	    first++;
	}
	else {
	    rgTemp[third] = rgInt[second];
	    second++;
	}
	third++;
    }
    /* copy remainder of non-exhausted sub-list into rgTemp */
    if (first > middle)          /* first sub-list is exhausted */
	while ( second <= high ) {
	    rgTemp[third] = rgInt[second];
	    second++;
	    third++;
	}
    else                         /* second sub-list is exhausted */
	while ( first <= middle ) {
	    rgTemp[third] = rgInt[first];
	    first++;
	    third++;
	}
    
    /* copy rgTemp back over original rgInt array */
    for ( i = low; i <= high; i++)
	rgInt[i] = rgTemp[i];
    
    return;
}  /* end of merge */

/* QuickSort --
 * Programmer: David Finkel
 * parameters: array to be sorted, low and high limits of indices to
 *             be sorted
 */
void QuickSort(int rgInt[], int low, int high)
{
    int pivot_loc;
    
    if ( low >= high )       /* if only one element to be sorted  */
	return;
    else {
	pivot_loc = Partition(rgInt, low, high);
	QuickSort(rgInt, low, pivot_loc - 1);
	QuickSort(rgInt, pivot_loc + 1, high);
    }
} /* end of merge_sort */

/* partition function
 * parameters: array to be sorted, low and high indices to be sorted
 * returns: pivot location
 */
int Partition(int rgInt[], int low, int high)
{
    int i, pivot_loc, pivot_val;

    Swap(rgInt, low, (low + high)/2);
    pivot_val = rgInt[low];               /* select a pivot */
    pivot_loc = low;                     /* record pivot's location */
    for (i = low + 1; i <= high; i++) {
	cComp++;
	if (rgInt[i] < pivot_val)
	    Swap(rgInt, ++pivot_loc, i);   /* found an entry smaller than pivot */
    }
    Swap(rgInt, low, pivot_loc);
    return(pivot_loc);
} /* end partition */

extern "C" {
int random();	// standard C random number generator
void srandom(int);	// seed for random number generator
}

int GetRanInt(int limit)	/* return a random integer i, 0 <= i < limit */
{
    return(random()%limit);
}
