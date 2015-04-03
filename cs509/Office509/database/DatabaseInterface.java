// Interface:  DatabaseInterface
// Author:     George T. Heineman
// ModifiedBy: Database Group
//            
// Version:    1.3
// Date:       Fri Dec 3 17:00:55 1999
//
// NOTES:
//
//   The Database provides a convenient access to information stored in tabular form.
//
//   The Database relies on an external entity to provide persistant storage.
//
//   The RecordInterface objects returned by the Database are READ ONLY.  There is no
//   way to change the values directly; this preserves the integrity of the database
//   at the expense of a slightly more complicated interface.  To update a record, 
//   therefore, the user must call updateRecord (RecordInterface, Field) or
//   updateRecord (RecordInterface, Vector fieldVector).
//   
//   The template record structure used by the database can be retrieved by the
//   getTemplate() method.  Note that this returns a 'FreeRecord', namely, a record 
//   not associated with any particular database.
//
//   To provide persistence, each object stored by the Database must implement the
//   java.io.Serializable interface.
//
// Each Database Component must 'implement' this interface as well as provide for
// the supporting classes: FreeRecord, Field, RecordInterface, and DatabaseException;

package database;

import java.util.Vector;
import java.util.Enumeration;
import java.io.Serializable;

	/**
	 * The main user interface for the database component.  Provides methods 
	 * for the user to manipulate the database.
	 */
public interface DatabaseInterface {
    
    /**
     * Searches through the database and returns an Enumeration of the
	 * RecordInterface representations of Records that contain a Field 
	 * that matches the name and value of the Field passed in.
	 *
	 * @param f - Field with name and value to be searched for
	 *
	 * @throws DatabaseException - A DatabaseException is thrown if the Field
	 * passed in is null, or if the Field name is not currently in the current
	 * Record structure.
	 *
	 * @returns Returns an enumeration of all Records containing a matching Field
	 * to the Field passed in.
     */
	 
	 public Enumeration query (Field f) throws DatabaseException;
	 
	/**
	 * Returns an enumeration of the RecordInterface representations of Records 
	 * in the database that has fields that are equal to all of the fields specified.
	 *
	 * @param fieldVector - a list of fields to search for in the database
	 *
	 * @throws DatabaseException - a DatabaseException is thrown if the Vector is
	 * null, if the vector does not contain any Fields, if any of the Fields are not
	 * in the current Record structure, if the vector contains something other 
	 * than a Field object.
	 * @returns Returns an enumeration of all Records containing a matching Field
	 * to the Field passed in.
	 */
	 public Enumeration query (Vector fieldVector) throws DatabaseException;
	
    /**
     * Inserts the record represented by the FreeRecord object into the 
	 * database
	 *
	 * @param fr - a FreeRecord containing the record data to be entered into
	 * the database
     *
     * @throws DatabaseException if input record is null, if the FreeRecord
	 * structure contains a Field that is not in the current database Record
	 * structure, if the primary key value is null, or if the primary key value
	 * is already contained within another Record already in the database.
     */
    public void addRecord (FreeRecord fr) throws DatabaseException;
    
    /**
     * Delete any record (if one exists) whose field value matches the field
     * value stored in the given field.  If the field is a primary key, then only
     * the single record (if it exists) is deleted.
     *
	 * @param f - Field specifying deletion criteria
	 *
	 * @throws DatabaseException - a DatabaseException is thrown if the Field
	 * passed in is null, or if the Field is not in the current Record structure.
	 *
	 * @return Returns true if a Record is deleted, false otherwise
     */
    public boolean deleteRecord (Field f) throws DatabaseException;
    
    /**
     * Updates a Field of the Record represented by the RecordInterface object with 
	 * the Field passed in.  It is assumed that the RecordInterface object was previously
	 * returned from a databae query.
     *
     * @param ri - RecordInterface representation of Record to be updated
	 * @param f - Field to be updated in the Record, contains the value to be updated
	 *
	 * @throws DatabaseException -  a DatabaseException is thrown if either the 
	 * RecordInterface or Field is null, if the Field is not in the current
	 * Record structure, if the RecordInterface contains a valid Field structure 
	 * (ie the record structure is correct), or if the Field being updated
	 * is a primary key field.
	 *
	 * @return Returns true is a record is updated, false otherwise
     */
    public boolean updateRecord (RecordInterface ri, Field f) throws DatabaseException;
    
    /**
     * A record can have multiple fields updated using this method.  All Field objects
     * in the Vector must match (by name) to a field in the given RecordInterface object
     *
     * @param ri - RecordInterface representation of Record to be updated
	 * @param fieldVector - a list of Fields to be updated in the Record, contain 
	 * the values to be updated
	 *
	 * @throws DatabaseException -  a DatabaseException is thrown if either the 
	 * RecordInterface or fieldVector is null, if the fieldVector doesn't contain
	 * any Fields, if the fieldVector contains an object other than a Field, 
	 * if a Field in the fieldVector is not in the current Record structure, if the 
	 * RecordInterface contains a valid Field structure(ie the record structure is correct), 
	 * or if one of the Fields being updated is a primary key field.
	 *
	 * @return Returns true if a record is updated, false otherwise
     */
    public boolean updateRecord (RecordInterface ri, Vector fieldVector) throws DatabaseException;
       
    /**
     * Delete the actual record r from the database.  It is assumed that the record was
	 * returned from a previous query.
     *
     * @param r - the RecordInterface of the Record to be deleted from the database
	 *
	 * @throws DatabaseException - a DatabaseException is thrown if the RecordInterface is null,
	 * or if it's internal record structure is not the same as the current Record structure.
     */
    public void deleteRecord (RecordInterface r) throws DatabaseException;

    /**
     * Sets the defalut template for the database 
     *
	 * @param f - a FreeRecord object containing the default record structure for the database
	 *
	 * @throws DatabaseException - a DatabaseException is thrown if the FreeRecord is null,
	 * if there records already stored in another format, or if a primary key is not specified.
     */
    public void setTemplate (FreeRecord f) throws DatabaseException;

    /**
     * Gets template as a FreeRecord for clients to know the format of the Database.
     * 
	 * @return Returns the current database template, if one does not exist null is returned
	 * instead
     */
    public FreeRecord getTemplate ();

    /**
     * Returns a persistent storable object representation of the database
	 *
	 * @return Returns a Serializable object that can be stored persistently
     */
    public Serializable getState ();
    
    /** 
     * Loads up the state of a database from an object which was previously used
     * to store the state
	 *
	 * @param o - a stored object that can be used to recreate a stored database
	 *
  	 * @throws DatabaseException - a Database Exception will be thrown if the Serializable
	 * object is not a valid stored representation of a database
     */
    public void restoreState (Serializable o) throws DatabaseException;
}
