AUTHOR(S):	Peter Golaszewski <peterg@wpi.edu>

COURSE:		WPI, CS4432, D00

PROJECT:	1

VERSION:	1.0

UPDATED:	4/20/00

HOW TO REBUILD:
	under /BTree/src/btree, type 'make'

HOW TO EXECUTE:
	There are two test drivers. 
	1.	BTTest class (original test driver)	
		under /BTree/src/tests, type 'make bttest' 
		Following tests have been tested successfully:
			
	2.	OtherTests class (my own test driver)	
		under /BTree/src/tests, type 'make other' 		

REQUIREMENTS IMPLEMENTED:
	All
	
ADDITIONAL FEATURES:
	The B+ tree can fully print itself. View the 
	output.txt file for an example.
	
KNOWN BUGS / PROBLEMS:
	BTFileScan::delete_current() does not delete
	the most recently scanned record, but rather
	the next record to be scanned. I misunderstood
	the requirement for this function and by the
	time I realized it (2am, Friday), it was to 
	late to fix this problem.
	
ADDITIONAL COMMENTS:
	All my source files total to around 1300 
	lines of code.
	