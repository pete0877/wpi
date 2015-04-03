// Interface:  DatabaseInterface
// Author: Database Group
//            
// Version:    1.3
// Date:       Fri Dec 3 17:00:55 1999


   /**
    * The main class of the database component.  It controls the functionality
	* of the component.
	*/  
public class Database ...
	implements DatabaseInterface ... {

   /**
    * Initializes the Database class 
    */
    public Database ();
	
	...
	
}
	
   /**
    *  A read only representation of a Record.  It is a Record that is not attached to 
	*  a database, and therefore it is "free."
	*/
public class FreeRecord ...
	implements FieldInterface ... {
  
   /**
    * Creates a FreeRecord object containing the specified fields, with the 
	* specidfied primary key field.  The reason for passing the parameters in
	* the constructor is to make sure that the FreeRecord is read only.  If we
	* add public methods to set these things, then it may be possible for the user
	* to modify the data in some way.
	*
	* @param fieldVector - a list of fields that make up the internal record
	* @param primaryField - a string containing the name of the primary key
	* field
	*
	* @throws DatabaseException - a DatabaseException is thrown if the fieldVector
	* is empty or null, if the primaryField doesn't relate to any of the Fields in
	* fieldVector, if the fieldVector contains an object other than a Field, or if the
	* fieldVector contains more than one Field with the same name
    */
    public FreeRecord (Vector fieldVector, String primaryField) throws DatabaseException;

	...
	
}

   /**
    * The representation of a database field.    
	*/
public class Field ...
	implements FieldInterface ... {

   /**
    *  Initializes the Field object 
    */
    public Field ();
	
	...
	
}
