////////////////////////////////////////////////////////////////////////////
// Class:       FreeRecord
// Package:     database
// Author:      Peter Golaszewski
// Date:        12/12/99
// Description: Used for communication with the database component

package database;

import java.util.Vector;

public class FreeRecord implements RecordInterface {
    
    ////////////////////////////////////////////////////////////////////////////	
    /**
     * data members
     */
	protected Vector fields;
	protected String primary; 
	
   ////////////////////////////////////////////////////////////////////////////	
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
    public FreeRecord (Vector fieldVector, String primaryField) throws DatabaseException {        
        // check for null values:
        if ((fieldVector==null) || (primaryField==null)) throw new DatabaseException("No primary key specified.");
        
        // check if the vector is empty
        if (fieldVector.size()==0) throw new DatabaseException("No records fields specified.");
        
        // check if the vector consists only of Field instances:
        int a;
        Field tmpField = new Field();        
        for (a=0; a<fieldVector.size(); a++) 
            if (fieldVector.elementAt(a).getClass()!=tmpField.getClass()) 
                throw new DatabaseException("Invalid field specification.");
        
        // create copy of all the fields:
        fields = new Vector();
        Field newfield, oldfield;
        for (a=0; a<fieldVector.size(); a++) {
            oldfield = (Field) fieldVector.elementAt(a);
            newfield = new Field();
            newfield.init(oldfield.getName(), oldfield.getValue());            
            fields.addElement(newfield);
        }
        
        // check to make sure that the primary key relates to one
        // of the fields:             
        if (getField(primaryField)==null) throw new DatabaseException("Invalid primary key specification.");        
        
        // check if all the fields have unique names:
        int b;
        if (fields.size()>1) {
            for (a=0; a<fields.size()-1; a++) {
                Field field1 = (Field) fields.elementAt(a);
                for (b=a+1; b<fields.size(); b++) {
                    Field field2 = (Field) fields.elementAt(b);
                    if (field1.getName()==field2.getName()) throw new DatabaseException("Dupe(s) in field names.");
                }
            }                    
        }
        
        // everything is fine:
        primary = primaryField;
    }

	////////////////////////////////////////////////////////////////////////////	
    /**
     * Returns the fields contained within the record as an Enumeration
     */
    public java.util.Enumeration getFields () { return fields.elements(); }

    ////////////////////////////////////////////////////////////////////////////	
    /**
     * Returns the fields contained within the record as a Vector
     */
    public Vector getFieldsVect () { return fields; }

	////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the field with the name passed in.  If a field is not found,
	 * null is returned
	 *
	 * @param s - name of field being search for within the record
	 *
	 * @return Returns the Field containing the passed in field name, null otherwise
     */
    public Field getField (String s) {        
        if (s==null) return null;
        int wow = fields.size();
        for (int a=0; a<fields.size(); a++)        
            if (((Field)fields.elementAt(a)).getName()==s) return (Field)fields.elementAt(a);
		return null;
	}
	
	////////////////////////////////////////////////////////////////////////////    
    /**
     * Return the primary key Field of the Record
	 *
	 * @return Returns the primary key field of the Record
     */
    public Field getPrimaryField () {        
        return getField(primary);
	}
	

    ////////////////////////////////////////////////////////////////////////////
    /**  
    * As String - returns the state of the instance as one string.
    */  
    public String asString() {
        String result = "";
        for (int a=0; a<fields.size(); a++) {
            Field f = (Field) fields.elementAt(a);
            if (f.getName()==primary) result = result + "*";
            result = result + f.asString() + " ";
        }
        return result;
    }
	
	
}


// END OF FILE
////////////////////////////////////////////////////////////////////////////