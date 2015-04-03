////////////////////////////////////////////////////////////////////////////
// Class:       Record
// Package:     database
// Author:      Peter Golaszewski
// Date:        12/5/99
// Description: Used to represent table rows

package database;

import java.util.Vector;
import java.io.Serializable;

public class Record extends FreeRecord implements Serializable {

   ////////////////////////////////////////////////////////////////////////////	
   /**
    * see FreeRecord(Vector fieldVector, String primaryField) constructor
    */
    public Record (Vector fieldVector, String primaryField) throws DatabaseException {        
       super(fieldVector, primaryField); 
    }

	////////////////////////////////////////////////////////////////////////////    
    /**
     * Contains Field - returns true if the field exists in the record
     *    match is made only based on the field name.
     */
    public boolean containsField (Field f) {                
        if (getField(f.getName())==null) return false; else return true;
	}		
	
	////////////////////////////////////////////////////////////////////////////    
    /**
     * Matches - returns true if and only if every field in v has corresponding
     *   field in the current record and the field values match.
     */
    public boolean matches (Vector v) {      
        if (v==null) return false;
        Field field1, field2;        
        for (int a=0; a<v.size(); a++) {
            field1 = (Field) v.elementAt(a);            
            field2 = (Field) this.getField(field1.getName());
            if ((field2==null) || (field2.getValue() != field1.getValue())) return false;            
        }
		return true;        
	}
	
	////////////////////////////////////////////////////////////////////////////    
    /**
     * Matches Definition - returns false if there is at least one field in v that
     *    does not exists in the current record. Match is made based on the field name only.     
     *    Else it returns true.
     */
    public boolean matchesDefinition (Vector v) {              
        if (v==null) return false;
        Field field1, field2;        
        field1 = new Field();
        for (int a=0; a<v.size(); a++) {            
            if (field1.getClass()!=v.elementAt(a).getClass()) return false;
            field1 = (Field) v.elementAt(a);            
            field2 = (Field) this.getField(field1.getName());
            if (field2==null) return false;            
        }
		return true;        
	}
	

	////////////////////////////////////////////////////////////////////////////    
    /**
     * Update - for each field in v, corresponding value is updated in the current
     *   record. If a field in v does not have corresponding field in the current
     *   record, then that field is ignored.
     */	
    public void update (Vector v) {                
        if (v!=null) {
            Field field1, field2;        
            for (int a=0; a<v.size(); a++) {
                field1 = (Field) v.elementAt(a);            
                field2 = (Field) this.getField(field1.getName());
                if (field2!=null) field2.setValue(field1.getValue());            
            }	
        }
	}
	
}

// END OF FILE
////////////////////////////////////////////////////////////////////////////