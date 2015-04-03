// Interface:  RecordInterface
// Author:     George T. Heineman
// ModifiedBy: Database Group
//             
// Version:    1.3
// Date:       Fri Dec 3 17:00:55 1999
//

package database;


  	/**
	 *  Provides for a read only interface to objects stored within a database
	 */	 
public interface RecordInterface {

    /**
     * Returns the fields contained within the record as an Enumeration
     */
    public java.util.Enumeration getFields ();

    /**
     * Returns the field with the name passed in.  If a field is not found,
	 * null is returned
	 *
	 * @param s - name of field being search for within the record
	 *
	 * @return Returns the Field containing the passed in field name, null otherwise
     */
    public Field getField (String s);
    
    /**
     * Return the primary key Field of the Record
	 *
	 * @return Returns the primary key field of the Record
     */
    public Field getPrimaryField ();
	
}
