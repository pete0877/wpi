/* FILE:        TIdentifiable.java
 * CLASS:       TIdentifiable
 * PACKAGE:     
 * VERSION:     1.0 
 * UPDATED:     4/21/00
 * AUTHOR:      Peter Golaszewski <peterg@wpi.edu>
 */
 
package TransactionManager;

public interface TIdentifiable {

    /***************************************************************************
    *   Compares two identifiable objects to each other
    */
    boolean equals(Object anObject);        
    
    /***************************************************************************
    *   Converts the identifiable object to a string representation
    */    
    String toString();        
    
}

