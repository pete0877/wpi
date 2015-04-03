////////////////////////////////////////////////////////////////////////////
// Class:       Field
// Package:     database
// Author:      Peter Golaszewski
// Date:        12/12/99
// Description: Representation of a table cell (table field)

package database;
import java.io.Serializable;

public class Field implements FieldInterface, Serializable{  

   ////////////////////////////////////////////////////////////////////////////
   /**  
    *  data members:
    */  	
	private String  mName;    // field name (e.g. "Address")	
	private String  mValue;   // field value (e.g. "100 Institute")
	
    ////////////////////////////////////////////////////////////////////////////  
    /**
    *  Initializes the Field object and sets the Field's name and value to null.
    */
    public Field () {
		mName = "";
		mValue = "";		
	}
	
   //////////////////////////////////////////////////////////////////////////// 
   /**
    * Initialize the Field object with the specified name and value.
	*
	* @param name - the column name of the field
	* @param val - the string value of the field
    */
    public void init (String name, String val) {        
        mName = new String(name);
		mValue = new String(val);	
    }
     
   ////////////////////////////////////////////////////////////////////////////
   /**
    * Sets the field's value to the string specified
	*
	* @param val - the new string value for the field
    */
    public void setValue (String val) { mValue = new String(val); }
  
   ////////////////////////////////////////////////////////////////////////////
   /**
    * Sets the field's name to the string specified
	*
	* @param n - the new string name of the field
    */
    public void setName  (String n ) { mName = new String(n); }  
	  
   ////////////////////////////////////////////////////////////////////////////
   /**
    * Returns the current value of the field
	*
	* @return Returns the string value of the current field
    */
    public String getValue () { return new String(mValue); }
	  
   ////////////////////////////////////////////////////////////////////////////
   /**
    * Returns the current name value of the field
	*
	* @return Returns the string name of the current Field
    */
    public String getName  () { return new String(mName); }
	  
   ////////////////////////////////////////////////////////////////////////////
   /**
    * Checks to see if the current Field being passed in has the same
	* name and value as the current Field.  
	*	ie  f.getName() == this.getName() && f.getValue() == this.getValue()
	*
	* @param f - Field object being check to see if it is equal to the current Field
	*
	* @return Returns a boolean specifying whether or not the Field is equal to the current
	* Field
    */ 
    public boolean equals (Field f) {
		return ((mName.equals(f.getName())) && (mValue.equals(f.getValue())));
	} 
	
	////////////////////////////////////////////////////////////////////////////
    /**  
    * As String - returns the state of the instance as one string.
    */  
	public String asString () {
        String result = "";
        result = result + "[";        
        result = result + mName;
        result = result + "='";
        if (mValue==null) 
            result = result + "NULL"; 
        else
            result = result + mValue; 
        result = result + "']";
        return result;
    }
}

// END OF FILE
////////////////////////////////////////////////////////////////////////////
