////////////////////////////////////////////////////////////////////////////
// Class:       Database
// Package:     database
// Author:      Peter Golaszewski
// Date:        12/5/99
// Description: Holds the main class of the database component

package database;

import java.util.Vector;
import java.util.Enumeration;
import java.io.Serializable;

public class Database implements DatabaseInterface {

   ////////////////////////////////////////////////////////////////////////////
   /**  
    *  data members:
    */  
   private Vector records;      // list of records
   private Record definition;   // table definition

   ////////////////////////////////////////////////////////////////////////////	 
   /**
    * Initializes the Database class without setting a template
	* or storing any data. 
    */
    public Database () {                
        records = null;
        definition = null;
    }

   ////////////////////////////////////////////////////////////////////////////	  
   /**
    * Initializes the Database class from a Serializable object that was stored
	* previously.
	*
	* @param o - Object that contains previously stored information that is needed to 
	* restore the database
	* 
	* @throws DatabaseException - a Database Exception will be thrown if the Serializable
	* object being passed in is not a valid stored representation of a database
    */
    public Database (Serializable o) throws DatabaseException { restoreState(o); }

   ////////////////////////////////////////////////////////////////////////////	  
   /**
    * Initializes the Database, setting the default Template to contain the same
	* fields as the FreeRecord that is being passed in.
	*
	* @param fr - Contains a list of Fields that will become the default Record fields
	* and values for the database
	*
	* @throws DatabaseException - a Database Exception is thrown if an invalid field 
	* structure is found, the FreeRecord doesn't contain any fields, or a Primary Field
	* is not defined
    */
    public Database (FreeRecord fr) throws DatabaseException {
        // check for null the parameter
        if (fr == null) throw new DatabaseException("Invalid schema template.");
        // we do not have to check for exception conditions because the 
        // FreeRecord cannot be created unless above mentioned conditions 
        // are met.
        definition = new Record(fr.getFieldsVect(),fr.getPrimaryField().getName());
        records = new Vector();        
    }

   ////////////////////////////////////////////////////////////////////////////    
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
    public Enumeration query (Field f) throws DatabaseException {	
        // check for null parameters
        if (definition == null) throw new DatabaseException("Database schema not defined.");                        
        if (f == null) throw new DatabaseException("Empty query criteria.");                                
		Vector tmp = new Vector();
		tmp.addElement(f);		
		// leave the rest of the error checking to the other version of the funtion:
		return query(tmp);
	}

   ////////////////////////////////////////////////////////////////////////////    	    
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
	public Enumeration query (Vector fieldVector) throws DatabaseException {
	    // check for null parameters
	    if (definition == null) throw new DatabaseException("Database schema not defined.");                        
        if (fieldVector == null) throw new DatabaseException("Empty query criteria.");                                
    
        // make sure that all the fields match the definition of our table:
	    if (!definition.matchesDefinition(fieldVector)) throw new DatabaseException("Invalid query criteria.");
	    
	    // create the results list:
		Vector result = new Vector();
		Record tmp;		
		for (int a=0; a<records.size(); a++) {
		    tmp = (Record) records.elementAt(a);
	        if (tmp.matches(fieldVector)) result.addElement(new FreeRecord(tmp.getFieldsVect(), tmp.getPrimaryField().getName()));
		}
		return result.elements();
	}

    ////////////////////////////////////////////////////////////////////////////    	
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
    public void addRecord (FreeRecord fr) throws DatabaseException {
        // check the null-ness of the parameters:
        if (definition == null) throw new DatabaseException("Database schema not defined.");                        
        if (fr == null) throw new DatabaseException("New record is empty.");                                
        
        // check if the free record matches the definition of the template record:
        if (!definition.matchesDefinition(fr.getFieldsVect())) throw new DatabaseException("Invalid new record.");
        if (definition.getPrimaryField().getName()!=fr.getPrimaryField().getName()) throw new DatabaseException("Invalid new record.");
      
        // check the consistency new PK value passed:        
        String newPK = fr.getPrimaryField().getValue();
        if (newPK == null) throw new DatabaseException("No primary key specified.");
        Record r;
        for (int a=0; a<records.size(); a++) {
            r = (Record) records.elementAt(a);
            if (r.getPrimaryField().getValue()==newPK) throw new DatabaseException("Primary key violation");
        }
        
        // the new PK value is unique, so we add the new record to the table:        
        Record tmpRecord = new Record(definition.getFieldsVect(), definition.getPrimaryField().getName());
        tmpRecord.update(fr.getFieldsVect());
        records.addElement(tmpRecord);
    }

    ////////////////////////////////////////////////////////////////////////////    	
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
    public boolean deleteRecord (Field f) throws DatabaseException {
        // check for null parameters
        if (definition == null) throw new DatabaseException("Database schema not defined.");                        
        if (f == null) throw new DatabaseException("Empty delete criteria."); 
        Vector tmp = new Vector();
		tmp.addElement(f);		
		// leave all error
		return deleteRecord((Vector) tmp);
	}

    ////////////////////////////////////////////////////////////////////////////    	
    /**
     * Delete any record (if one exists) whose field values matches the field
     * values stored in the given field vector.  
     *
	 * @param v - Field vector specifying deletion criteria
	 *
	 * @throws DatabaseException - a DatabaseException is thrown if the Field vector 
	 * passed in is null, or if the Field vector is not in the current Record structure.
	 *
	 * @return Returns true if a Record is deleted, false otherwise
     */    
    public boolean deleteRecord (Vector v) throws DatabaseException {
        boolean result = false;	
        
	    // check if any parameters are null:
        if (definition == null) throw new DatabaseException("Database schema not defined.");                        
        if (v == null) throw new DatabaseException("Empty delete criteria."); 
	    
	    // check if all records are meant to be deleted:
	    if (v.size()==0) {
	        if (records.size()>0) {	            
                records = new Vector();
                return true;
            } else {
                return false;   
            }
        }
	    
        // make sure that all the fields match the definition of our table:
	    if (!definition.matchesDefinition(v)) throw new DatabaseException("Invalid delete criteria.");
	    
	    // delete mathicng records					
	    Vector tmpScan = (Vector) records.clone();
		Enumeration i = tmpScan.elements();
		Record tmpRecord;
		while (i.hasMoreElements()) {
		    tmpRecord = (Record) i.nextElement();
		    if (tmpRecord.matches(v)) {
		        result = true;
		        records.removeElement(tmpRecord);
		    }
		}		
		return result;
	}	
    
    ////////////////////////////////////////////////////////////////////////////    	
    /**
     * Delete the actual record r from the database.  It is assumed that the record was
	 * returned from a previous query.
     *
     * @param r - the RecordInterface of the Record to be deleted from the database
	 *
	 * @throws DatabaseException - a DatabaseException is thrown if the RecordInterface is null,
	 * or if it's internal record structure is not the same as the current Record structure.
     */
    public void deleteRecord (RecordInterface r) throws DatabaseException {
	    // check if any parameters are null:
        if (definition == null) throw new DatabaseException("Database schema not defined.");                        
        if (r == null) throw new DatabaseException("Empty delete record criteria."); 
	    // convert the RecordInterface to Vector:
	    Vector v = new Vector();
	    Enumeration i = r.getFields();
	    while (i.hasMoreElements()) { v.addElement((Field) i.nextElement()); }	
	    if (deleteRecord(v)==false) throw new DatabaseException("Record not found.");      
    }    
    
    ////////////////////////////////////////////////////////////////////////////    	
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
    public boolean updateRecord (RecordInterface ri, Field f) throws DatabaseException {
        // check for null parameters
        if (definition == null) throw new DatabaseException("Database schema not defined.");                        
        if (f == null) throw new DatabaseException("Empty update specifications."); 
        if (ri == null) throw new DatabaseException("Empty update record."); 
        Vector tmp = new Vector();
		tmp.addElement(f);		
		return updateRecord(ri, (Vector) tmp);
	}
    
    ////////////////////////////////////////////////////////////////////////////    	
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
    public boolean updateRecord (RecordInterface ri, Vector fieldVector) throws DatabaseException {        
        // check for null parameters
        if (definition == null) throw new DatabaseException("Database schema not defined.");                        
        if (fieldVector == null) throw new DatabaseException("Empty update specifications."); 
        if (ri == null) throw new DatabaseException("Empty update record."); 
        
        // convert ri into a Vector v:
        Vector v = new Vector();
	    Enumeration i = ri.getFields();
	    while (i.hasMoreElements()) { v.addElement((Field) i.nextElement()); }	
	    
	    // create temporary Record instance from v:
	    Record tmpRecord = new Record (v, ri.getPrimaryField().getName());	    
	            
        // check if the v matches the definition of the current template exactly:
        if (!definition.matchesDefinition(v) || !tmpRecord.matchesDefinition(definition.getFieldsVect())) throw new DatabaseException("Invalid update specifications.");
        if (definition.getPrimaryField().getName()!=ri.getPrimaryField().getName()) throw new DatabaseException("Invalid update specifications.");
        
        // check if the fieldVector matches the definition of the current template:
        if ( !definition.matchesDefinition(fieldVector)) throw new DatabaseException("Invalid update specifications.");
        
        // check if the fieldVector contains reference to the primary key:
        Record dummyRecord = new Record (fieldVector, ((Field) fieldVector.elementAt(0)).getName());
        if (dummyRecord.containsField(definition.getPrimaryField())) throw new DatabaseException("Primary key violation.");
        
        boolean result  = false;
        Record r;
        for (int a=0; a<records.size(); a++) {
            r = (Record) records.elementAt(a);
            if (r.matches(v)) {
                r.update(fieldVector);
                result = true;
            }
        }
        return result;
	}
	
       
    ////////////////////////////////////////////////////////////////////////////    	
    /**
     * Sets the defalut template for the database 
     *
	 * @param f - a FreeRecord object containing the default record structure for the database
	 *
	 * @throws DatabaseException - a DatabaseException is thrown if the FreeRecord is null,
	 * if there records already stored in another format, or if a primary key is not specified.
     */
    public void setTemplate (FreeRecord f) throws DatabaseException {
        // check for null parameters
        if (definition != null) throw new DatabaseException("Database schema already defined.");                        
        if (f == null) throw new DatabaseException("Invalid schema template."); 
        
        definition = new Record(f.getFieldsVect(),f.getPrimaryField().getName());
        records = new Vector();  
    }

    ////////////////////////////////////////////////////////////////////////////    	
    /**
     * Gets template as a FreeRecord for clients to know the format of the Database.
     * 
	 * @return Returns the current database template, if one does not exist null is returned
	 * instead
     */
    public FreeRecord getTemplate () {        
        return (FreeRecord) definition;
	}

    ////////////////////////////////////////////////////////////////////////////    	
    /**
     * Returns a persistent storable object representation of the database
	 *
	 * @return Returns a Serializable object that can be stored persistently
     */
    public Serializable getState () {         
        Vector result = new Vector();
        result.addElement(records);        
        result.addElement(definition);
        return result;
    }
    
    ////////////////////////////////////////////////////////////////////////////    	
    /** 
     * Loads up the state of a database from an object which was previously used
     * to store the state
	 *
	 * @param o - a stored object that can be used to recreate a stored database
	 *
  	 * @throws DatabaseException - a Database Exception will be thrown if the Serializable
	 * object is not a valid stored representation of a database
     */
    public void restoreState (Serializable o) throws DatabaseException {
        Vector result = (Vector) o;
        records = (Vector) result.elementAt(0);
        definition = (Record) result.elementAt(1);
        if ((records==null) || (definition==null)) throw new DatabaseException("Invalid data stream.");
    }

    ////////////////////////////////////////////////////////////////////////////
    /**  
    * As String - returns the state of the instance as one string.
    */  
    public String asString() {
        String result = "DBC STATE: ";
        if (definition == null) return result + "NULL";
        result = result + "DEFINITION: " + definition.asString() + "\n";
        result = result + "DATA: ";       
        for (int a=0; a<records.size(); a++) {
            Record r = (Record) records.elementAt(a);
            result = result + "\n   RECORD: " + r.asString();
        }        
        return result;
    }    
}


// END OF FILE
////////////////////////////////////////////////////////////////////////////