////////////////////////////////////////////////////////////////////////////
// Class:       DatabaseException
// Package:     database
// Author:      Peter Golaszewski
// Date:        12/12/99
// Description: The expection class thrown by the Database and FreeRecord 
//              classes
package database;
import java.lang.String;

public class DatabaseException extends Exception {
    DatabaseException(String event) { super(event); }   
}

// END OF FILE
////////////////////////////////////////////////////////////////////////////