___________________________________________________
file	: Doc/readme.txt
project	: CS509 HOMEWORK 3 - Database component
author	: Peter Golaszewski

TO INSTALL:
	edit setpath.bat to point to your Java bin directory.
		(e.g. "c:\jdk\bin")
	run setpath.bat

TO BUILD:
	run build.bat

TO RUN SIMPLE-DRIVER VERSION:
	run run_drv.bat

TO RUN FRAMEWORK VERSION:
	run run_frm.bat
	view Doc/out_drv.txt

TO READ COMPONENT USE CASES:
	open Doc/dbc_usecases.doc (MS Word file)

___________________________________________________
NOTES:

Both run-versions operate on the same version of the 
database component. 

The simple-driver version will just run and quit when 
it's done with bunch of use cases. You can view
the driver output in Doc/out_drv.txt

The framework version will execute the Office 509
application. Go to Menu -> File -> New -> Database.
Use following field syntaxt specifications to operate 
the database:

Office509 Application Syntax:

	new schema ::= fieldname1|fieldvalue1, fieldname2|fieldvalue2, fieldname3|fieldvalue3

		ASSUMPTIONS:
		(The primary key field will be assumed to be the first field in the list.
		You have to specify exactly one PK field. Field values of other fields
		will be assumed to be default-values).

	insert	::= fieldname1|fieldvalue1, fieldname3|fieldvalue3, fieldname4|fieldvalue4

		ASSUMPTIONS:
		(you have to provide unique value for the PK field).


	filter  ::= fieldname2|fieldvalue2, fieldname3|fieldvalue3, fieldname4|fieldvalue4

		ASSUMPTIONS:
		(none, all or some fields can be specified. In none are specified, all records
		will be matched)

	delete  ::= fieldname2|fieldvalue2, fieldname3|fieldvalue3, fieldname4|fieldvalue4

		ASSUMPTIONS:
		(none, all or some fields can be specified. In none are specified, all records
		will be matched)

	Note: both, field names and field values, are always trimmed.

	Sample execution:

		type: ID|, Name|, Address|, State|MA, Age|21
		press: new schema

		type: ID|1, Name|Peter
		press: insert

		type: ID|2, Name|Peter, State|CT
		press: insert

		type: ID|3, Name|Mark, Age|18
		press: insert

		type: Name|Peter
		press: filter

		type: 
		press: filter

		type: Name|Peter, State|MA
		press: filter

		type: 
		press: filter

		type: Name|Peter, Age|21
		press: delete

		type: 
		press: filter

		type: 
		press: clear all


___________________________________________________
End of file