/* FILE:        BTFileScan.java
 * CLASS:       BTFileScan
 * PACKAGE:     btree
 * VERSION:     1.0 
 * UPDATED:     4/20/00
 * AUTHOR:      Peter Golaszewski <peterg@wpi.edu>
 * NOTE:        Please see the documentation within BTreeFile class to find out about 
 *              exceptions thrown by this class.
 */

package btree;

import global.*;

public class BTFileScan extends IndexFileScan {
    
    // DATA MEMEBERS
    // ****************************************************************************
    protected BTreeFile m_bTree; // the B tree that will feed us the keys and RIDs
    
    
    // CONSTRUCTORS
    // ****************************************************************************
    public BTFileScan (BTreeFile bTree) {
        m_bTree = bTree;
    }
    
    
    // METHODS
    /****************************************************************************
    *	Gets the next record from the scan. Null will be returned if there 
    *   are no more keys for this scan.
    */
    public KeyDataEntry get_next() throws Exception {
        return m_bTree.getNextScan();
    }
    
    
    /****************************************************************************
    *	Deletes the current record from the scan
    */    
    public void delete_current() throws Exception {
        m_bTree.deleteCurrentScan();
    }
    

    /****************************************************************************
    *	Destroys the currently present record scan
    */    
    public void destroyBTreeFileScan() throws Exception {
        m_bTree.closeScan();
    }
    
    
    /****************************************************************************
    *	Returns the key size
    */    
    public int keysize() throws Exception {
        return m_bTree.getKeySize();   
    }
    

    /****************************************************************************
    *	Returns the key type
    */    
    public short keytype() throws Exception {
        return m_bTree.getKeyType();   
    }
    
}
