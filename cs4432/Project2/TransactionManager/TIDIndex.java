/* FILE:        TIDIndex.java
 * CLASS:       TIDIndex
 * PACKAGE:     
 * VERSION:     1.0 
 * UPDATED:     4/21/00
 * AUTHOR:      Peter Golaszewski <peterg@wpi.edu>
 */
 
package TransactionManager;

import java.util.Vector;
import java.util.Enumeration;

public class TIDIndex {
    
    // DATA MEMEBERS
    protected Vector m_elements;

    // CONSTRUCTORS
    /***************************************************************************
    *   Creates empty ID index
    */
	public TIDIndex () {
	    m_elements = new Vector();
    }

    // METHODS
    /***************************************************************************
    *   Returns the position of the ID within the index, or returns -1 if the ID
    *   does not exist. First position in the index is 0
    */
    public int getIndex (TIdentifiable ID) {
        int result = -1;
        Enumeration enIDs = m_elements.elements();        
        int index = 0;
        while (enIDs.hasMoreElements()) {
            TIdentifiable existingID = (TIdentifiable) enIDs.nextElement();
            if (existingID.equals(ID)) result = index;
            index++;
        }
        return result;
    }

    /***************************************************************************
    *   Returns the ID at given position. If there is no ID at the given position,
    *   null is returned
    */
    public TIdentifiable getID (int position) {
        TIdentifiable result = null;
        if (position < m_elements.size()) {
            result = (TIdentifiable) m_elements.elementAt(position);
        }
        return result;
    }    

    /***************************************************************************
    *   Adds a new ID to the index and returns its position
    */
    public int addID (TIdentifiable ID) {
        m_elements.addElement(ID);
        return (m_elements.size() - 1);
    }     

    /***************************************************************************
    *   Removes given ID from the index. Returns true if the ID was found.
    */    
    public boolean removeID (TIdentifiable ID) {
        int position = getIndex(ID);
        boolean result = false;
        if (position >= 0) {
            m_elements.removeElementAt(position);
            result = true;
        }   
        return result;
    }         
    
    /***************************************************************************
    *   Removes ID at the given position. Returns true if the ID was found.
    */    
    public boolean removeIndex (int position) {
        if (position < m_elements.size()) {
            m_elements.removeElementAt(position);
            return true;
        }        
        return false;
    }      
    
    /***************************************************************************
    *   Returns the number of ID in the index
    */    
    public int getSize () {
        return m_elements.size();
    }         
    
    /***************************************************************************
    *   Prints all present elements
    */    
    public String toString() {
        String result = "";
        Enumeration enElements = m_elements.elements();
        while (enElements.hasMoreElements()) {
            TIdentifiable tmpElement = (TIdentifiable) enElements.nextElement();
            result = result + tmpElement.toString() + ", ";
        }
        return result;
    }         
    
    
    
}

