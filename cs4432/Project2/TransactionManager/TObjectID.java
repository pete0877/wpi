/* FILE:        TObjectID.java
 * CLASS:       TObjectID
 * PACKAGE:     
 * VERSION:     1.0 
 * UPDATED:     4/21/00
 * AUTHOR:      Peter Golaszewski <peterg@wpi.edu>
 */
 
package TransactionManager;

public class TObjectID implements TIdentifiable {
    // DATA MEMBERS
    protected String m_ID;
    
    // CONSTRUCTORS
    /***************************************************************************
    *   Creates default ID
    */    
    public TObjectID() {
        m_ID = new String();
    }
    
    /***************************************************************************
    *   Copy constructor
    */        
    public TObjectID(String ID) {
        m_ID = new String(ID);
    }
    
    // METHODS
    /***************************************************************************
    *   Compares this ID to another one
    */        
    public boolean equals(Object anObject) {
        TObjectID otherID = (TObjectID) anObject;
        return otherID.get().equals(m_ID);
    }
    
    /***************************************************************************
    *   Returns this ID as a string
    */            
    public String get() {
        return new String(m_ID);
    }    

    /***************************************************************************
    *   Sets this ID to the given string
    */                
    public void set(String new_ID) {
        m_ID = new String(new_ID);
    }  

    /***************************************************************************
    *   Converts this ID to a string
    */                
    public String toString() {
        return get();
    }        
    
}

