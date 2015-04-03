/* FILE:        TTransactionID.java
 * CLASS:       TTransactionID
 * PACKAGE:     
 * VERSION:     1.0 
 * UPDATED:     4/21/00
 * AUTHOR:      Peter Golaszewski <peterg@wpi.edu>
 */
 
package TransactionManager;

public class TTransactionID implements TIdentifiable {
    // DATA MEMBERS
    protected int m_ID;
    
    // CONSTRUCTORS
    /***************************************************************************
    *   Creates default ID
    */      
    public TTransactionID() {
        m_ID = 0;
    }
    
    /***************************************************************************
    *   Copy constructor
    */      
    public TTransactionID(int ID) {
        m_ID = ID;
    }
    
    /***************************************************************************
    *   Copy constructor
    */      
    public TTransactionID(Integer ID) {
        m_ID = ID.intValue();
    }    
    
    // METHODS
    /***************************************************************************
    *   Compares this ID to another one
    */        
    public boolean equals(Object anObject) {
        TTransactionID otherID = (TTransactionID) anObject;
        return (otherID.get() == m_ID);
    }
    
    /***************************************************************************
    *   Returns this ID as an integer
    */       
    public int get() {
        return m_ID;
    }    
    
    /***************************************************************************
    *   Set this ID to the given integer
    */           
    public void set(int new_ID) {
        m_ID = new_ID;
    }  
    
    /***************************************************************************
    *   Set this ID to the given integer
    */               
    public void set(Integer new_ID) {
        m_ID = new_ID.intValue();
    }  
    
    /***************************************************************************
    *   Converts this ID to a string
    */         
    public String toString() {
        return (new Integer(m_ID)).toString();
    }      
    
}

