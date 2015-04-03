// Interface:  DatabaseInterface
// Author: Database Group
//            
// Version:    1.3
// Date:       Fri Dec 3 17:00:55 1999

package database;   

   /**
    * Provides an interface to manipulate database fields 
    */
public interface FieldInterface {

   /**
    * Initialize the Field object with the specified name and value.
	*
	* @param name - the column name of the field
	* @param val - the string value of the field
    */
    public void init (String name, String val);

   /**
    * Sets the field's value to the string specified
	*
	* @param val - the new string value for the field
    */
    public void setValue (String val);

   /**
    * Sets the field's name to the string specified
	*
	* @param n - the new string name of the field
    */
    public void setName  (String n ); 
	
   /**
    * Returns the current value of the field
	*
	* @return Returns the string value of the current field
    */
    public String getValue ();
	
   /**
    * Returns the current name value of the field
	*
	* @return Returns the string name of the current Field
    */
    public String getName  (); 
	
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
    public boolean equals (Field f); 

}