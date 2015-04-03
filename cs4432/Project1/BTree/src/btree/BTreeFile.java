/* FILE:        BTreeFile.java
 * CLASS:       BTreeFile
 * PACKAGE:     btree
 * VERSION:     1.0 
 * UPDATED:     4/20/00
 * AUTHOR:      Peter Golaszewski <peterg@wpi.edu>
 * NOTE:        Implements B+ Tree ADT
 */

package btree;

import java.io.*;
import global.*;
import diskmgr.*;
import heap.HFPage;
import java.util.Vector;
import java.util.Enumeration;

/*
 * B+ Tree Index
 */
public class BTreeFile extends IndexFile 
{
    // DATA MEMBERS:
	// ****************************************************************************
    protected BTreeHeaderPage	m_header;   
	protected PageId			m_headerPageID;   
	protected FileOutputStream	m_traceFileOutput;
	protected DataOutputStream	m_traceFileStream;
	protected Vector 			m_pinnedNodes; 		
	protected String        	m_filename; 		
	protected KeyClass          m_scanStartKey;
	protected KeyClass          m_scanEndKey;	
	protected int               m_scanLeafPosition;
	protected boolean           m_scanWasInitialized;
	protected PageId            m_scanCurrentLeaf;
	protected final int         m_nullPageId = -1;
	
	
    // CONSTRUCTORS
    /****************************************************************************
    *	BTreeFile class an index file with given filename should already exist; 
	*	this opens it. 
	*	Parameters: 
    *		filename - the B+ tree file name. Input parameter. 
    */
    public BTreeFile(String filename) throws Exception {
		// initialize trace variables
		m_traceFileOutput 	= null;
		m_traceFileStream	= null;	 
		m_pinnedNodes		= new Vector();		
		m_scanWasInitialized = false;

		// Check the parameter:
		if (filename==null) throw new Exception ("Incorrect parameter");
	
		// try opening the header page:
		Page headerPage = new Page();
		PageId headerPageId = new PageId(); 
		try {
        	headerPageId = SystemDefs.JavabaseDB.get_file_entry(filename); 
			if (headerPageId==null) throw new Exception(""); 
		} catch (Exception e) { 
			m_header = null;
			throw new Exception("Could not find the header page"); 
		}	
		// we know what is the header page id. now we need to just 
		// pin it down:
		try {
			SystemDefs.JavabaseBM.pinPage(headerPageId, headerPage, false); 
		} catch (Exception e) { 
			m_header = null;
			throw new Exception("Could not read in the header page"); 
		}		
		
		m_header        = new BTreeHeaderPage (headerPage);
		m_headerPageID  = headerPageId;		
		m_filename      = new String(filename);		
    }
    
    
    /****************************************************************************
    *	If index file exists, open it; else create it. 
    *	Parameters: 
    *		filename - file name. Input parameter. 
    *		keytype - the type of key. Input parameter. 
    *		keysize - the maximum size of a key. Input parameter. 
    *		delete_fashion - full delete or naive delete. Input parameter. 
	*		  It is either DeleteFashion.NAIVE_DELETE or DeleteFashion.FULL_DELETE. 
    */
    public BTreeFile(String filename, int keytype, int keysize, int delete_fashion) throws Exception {
		// initialize trace variables
		m_traceFileOutput 	= null;
		m_traceFileStream 	= null;				 
		m_pinnedNodes		= new Vector();		
		m_scanWasInitialized = false;
	
		// Check parameters:
		if (filename==null) throw new Exception ("Incorrect parameters");

        // Create new index                
        Page headerPage = new Page();
		PageId headerPageId = null;
		// Allocate new page for the header and pin it:
		try {			
			headerPageId = SystemDefs.JavabaseBM.newPage(headerPage, 1); 
			if (headerPageId==null) throw new Exception();
			SystemDefs.JavabaseDB.add_file_entry(filename, headerPageId); 
		} catch (Exception e) { 
			m_header = null;
			if (headerPageId!=null) {
				// Could not add the page to the file. Since we have the page already 
				// allocated, we need to deallocate it now:
				try {
					SystemDefs.JavabaseBM.freePage(headerPageId); 
				} catch (Exception e2) {}
			}
			throw new Exception("Could not allocate the header page"); 
		}			

		// Page is now in the memory and pined. 
		// We now need to make a header page out of it and configure it
        m_header = new BTreeHeaderPage (headerPage);
		m_header.set_keyType((short) keytype);
		m_header.set_maxKeySize(keysize);
		// We have to remember the ID of the header page:
		m_headerPageID = headerPageId;

		// We have the header page. We now need to create the root node		
		BTLeafPage leafNode;
		try {
			leafNode = createLeafPage();
		} catch (Exception e) { 
			// Because we failed creating the root node, we have to release the
			// header page stuff and throw an exception
			try {
				SystemDefs.JavabaseBM.freePage(headerPageId);
				SystemDefs.JavabaseDB.delete_file_entry(filename); 
			} catch (Exception e2) {}			
			m_header = null;
			throw new Exception("Could not allocate the root page"); 		
		}

		// Set the root page ID in the header page:
		m_header.set_rootId(leafNode.getCurPage());
		leafNode.setNextPage(new PageId(m_nullPageId));
		leafNode.setPrevPage(new PageId(m_nullPageId));		

		// Now we can unping the page right away because we don't
		// need it at this point.
		unpinPages(true);
		
		m_filename      = new String(filename);
    } 
	
        
    // METHODS:
    /****************************************************************************
	*	It causes a structured trace to be written to a
	*	file.  This output is used to drive a visualization tool that shows 
	*	the inner workings of the b-tree during its operations. 
    *	Parameters: 	
	* 		@param filename input parameter. The trace file name
	* 		@exception IOException error from the lower layer
	*/ 
	public void traceFilename(String filename) throws Exception {		
        // check if we even have the header page loaded:
		if (m_header==null) throw new Exception ("Index was not initialized");
		m_traceFileOutput = new FileOutputStream(filename);
		m_traceFileStream = new DataOutputStream(m_traceFileOutput);
    }
    
    
    /****************************************************************************
	*	Returns the current key size
	*/ 
    public int getKeySize() throws Exception {
        // check if we even have the header page loaded:
		if (m_header==null) throw new Exception ("Index was not initialized");
		try {
		    return m_header.get_maxKeySize();
        } catch (Exception e) {            
            throw new Exception ("Could not access the header information");
        }
    }
    
    
    /****************************************************************************
	*	Returns the current key type
	*/ 
    public short getKeyType() throws Exception {
        // check if we even have the header page loaded:
		if (m_header==null) throw new Exception ("Index was not initialized");
		try {
		    return m_header.get_keyType();
        } catch (Exception e) {            
            throw new Exception ("Could not access the header information");
        }
    }    
    
	
    /****************************************************************************
	*	Stops tracing. And close trace file. 
   	*	@exception IOException error from the lower layer
	*/
	public void destroyTrace() throws Exception {
        // check if we even have the header page loaded:
		if (m_header==null) throw new Exception ("Index was not initialized");
		if (m_traceFileStream != null) m_traceFileStream.close();
		if (m_traceFileOutput != null) m_traceFileOutput.close();				
		m_traceFileStream=null;
		m_traceFileOutput=null;
	}
    
	
    /****************************************************************************
    *	Returns a BTreeHeaderPage object that is the header page of this btree file.     
    */    
    public BTreeHeaderPage getHeaderPage() throws Exception {
		// check if we even have the header page loaded:
		if (m_header==null) throw new Exception ("Index was not initialized");
        return m_header;
    }
    
    
    /****************************************************************************
    *	Closes the B+ tree file. Unpin header page. 
    */
    public void close() throws Exception {
        if (m_header == null) return; // we don't have a header page, so we don't have to clean up anything
        
        // Kill the scan if it exists
		if (m_scanWasInitialized) closeScan();		
        
		m_header = null;		
		// The only page pinned at the moment must be the header page.
		try {
			SystemDefs.JavabaseBM.unpinPage(m_headerPageID, true); 				
		} catch (Exception e) { 
			throw new Exception("Could not write the header page"); 
		}				
    }
	

    /****************************************************************************
    *	Destroys entire B+ tree file. 
    */
    public void destroyFile() throws Exception {
        // check if we even have the header page loaded:
		if (m_header==null) throw new Exception ("Index was not initialized");        
        
        // Kill the scan if it exists
		if (m_scanWasInitialized) closeScan();		
        
        // filePages will store all the pages that belong to this index file
        Vector filePages = new Vector();
        
        // put all page ID that belong to the file into filePages
        filePages.addElement(m_headerPageID);
        filePages.addElement(m_header.get_rootId());
        
        findAllNodes(m_header.get_rootId(), filePages);
        
        m_header = null;
        
        // We have all the page IDs (used by this file) stored
        // in the filePages vector. We simply go through all
        // of them and delete them one by one:
        
        SystemDefs.JavabaseBM.unpinPage(m_headerPageID, false); 	        
        try {            
            Enumeration enFilePages = filePages.elements();
		    while (enFilePages.hasMoreElements()) {					        		        
    			PageId pageId = (PageId) enFilePages.nextElement();
                SystemDefs.JavabaseDB.deallocate_page(pageId);  
			}
		} catch (Exception e2) {
		    throw new Exception ("Could not free all the file pages!");    
        }			    
        
        // now we just need to delete the file entry point
        try {
            SystemDefs.JavabaseDB.delete_file_entry(m_filename); 
		} catch (Exception e) {
		    throw new Exception ("Could not delete the file entry point");    
        }	
    }
 
			
    /****************************************************************************
    *	Insert record with the given key and rid.
    */
    public void insert(KeyClass key, RID rid) throws Exception { 
        // check if we even have the header page loaded:
		if (m_header==null) throw new Exception ("Index was not initialized");
		
		// check parameters:
		if ((key==null) || (rid==null)) throw new Exception ("Incorrect parameters");

        // We can't rely on the scan results after a regular delete has been called
        // That's why we have to close the scan session if it's currently running:
		if (m_scanWasInitialized) closeScan();

		try {			
			BTLeafPage tmpLeafNode = findWantedLeaf(key);
			insertIntoNode(m_pinnedNodes.size() - 1, new KeyDataEntry(key, rid), null);
			unpinPages(true);
		} catch (Exception e){
			// there was an error in inserting. we need to unpin all the 
			// pinned pages without writing:
			unpinPages(false);
			// Rethrow the error message:
			throw e;
		}
    }			

    
    /****************************************************************************
    *	Deletes leaf entry given its pair. `rid' is IN the data entry; 
	*	it is not the id of the data entry) 
    */    
    public boolean Delete(KeyClass key, RID rid) throws Exception {
		// check if we even have the header page loaded:
		if (m_header==null) throw new Exception ("Index was not initiated");
		
		// check parameters:
		if ((key==null) || (rid==null)) throw new Exception ("Incorrect parameters");

        // We can't rely on the scan results after a regular delete has been called
        // That's why we have to close the scan session if it's currently running:
		if (m_scanWasInitialized) closeScan();
		try {			
			BTLeafPage tmpLeafNode = findWantedLeaf(key);
			// We have the leaf node that should store our key
			// Try inserting the new key. If we get an exception, then
			// we know we need to split the node because its full
			try {
				boolean result = tmpLeafNode.delEntry(new KeyDataEntry (key, rid));								
				// We succeeded! unpin all pages and get da hell out
				unpinPages(true);				
				return result;
			} catch (Exception e) {
				// Couldn't delete from the leaf node
				unpinPages(true);
				throw new Exception ("Could not delete the key");
			}
		} catch (Exception e){
			// there was an error in inserting. we need to unpin all the 
			// pinned pages without writing:
			unpinPages(false);
			// Rethrow the error message:
			throw e;
		}
    }
   
    
    /****************************************************************************
    *	Creates a scan with given keys cases: 
	*		(1)	lo_key = null, 
	*			hi_key = null scan the whole index 
	*		(2) lo_key = null, 
	*			hi_key!= null range scan from min to the hi_key 
	*		(3)	lo_key!= null, hi_key = null range scan from the lo_key to max 
	*		(4) lo_key!= null, hi_key!= null, lo_key = hi_key exact match ( might not unique) 
	*		(5) lo_key!= null, hi_key!= null, lo_key < hi_key range scan from lo_key to hi_key 
    *	Parameters: 
    *		lo_key - the key where we begin scanning. Input parameter. 
    *		hi_key - the key where we stop scanning. Input parameter. 
    */    
    public BTFileScan new_scan(KeyClass lo_key, KeyClass hi_key) throws Exception {
        // check if we even have the header page loaded:
		if (m_header==null) throw new Exception ("Index was not initialized");      
				
	    m_scanStartKey = lo_key;
	    m_scanEndKey = hi_key;	    
	    Vector  scanLeafPageIDs = new Vector();
	    findAllLeafNodes(m_header.get_rootId(), scanLeafPageIDs);
	    // the first element in the scanLeafPageIDs vector is the page ID of the 
	    // left-most leaf node
	    m_scanCurrentLeaf = (PageId) scanLeafPageIDs.elementAt(0); 
	    m_scanLeafPosition = 0;		
		
		// find the first key that will be set to current:
		boolean foundFirstPage = false;
		while (m_scanCurrentLeaf!=null && !foundFirstPage) {
            
            // pin the page down and start looking for the first key:
            Page leafPage = new Page();
            BTLeafPage leafNode;
            try {
    			SystemDefs.JavabaseBM.pinPage(m_scanCurrentLeaf, leafPage, false); 
    			leafNode = new BTLeafPage (leafPage, m_header.get_keyType());
		    } catch (Exception e) {
			    throw new Exception("Could not load a leaf node");
		    }   
		    // look for the first matching key:
		    RID tmpRID = new RID(new PageId(0), 0);
		    KeyDataEntry scanData = null;
		    try {
                scanData = leafNode.getFirst(tmpRID);
            } catch (Exception e) {}
            // check to make sure that the current node is not empty
            if (scanData!=null) {
                // this leaf node is not empty 
                while (scanData!=null && !foundFirstPage) {
                    KeyClass scanKey = scanData.key; 				 
                    if (matchesCurrentScan(scanKey)) {
                        // we found the first matching key                        
                        foundFirstPage = true;                                                
                    } else {
                        try {
                            scanData = leafNode.getNext(tmpRID);
                            m_scanLeafPosition++;
                        } catch (Exception e2) {}
                    }
                }
            } 
            // find the ID of the next leaf:
            PageId nextLeafID = null;
            try {
                nextLeafID = leafNode.getNextPage();
                if (nextLeafID.pid == m_nullPageId) nextLeafID = null;
            } catch (Exception e3) {
                nextLeafID = null;
            }
            
            // we are done with this page so we can just unpin it:
            try {
    			SystemDefs.JavabaseBM.unpinPage(m_scanCurrentLeaf, false);     			
		    } catch (Exception e) {
			    throw new Exception("Could not unload a leaf node");
		    }   
		    
		    // we move on to the next leaf if we haven't found the right one yet
		    if (!foundFirstPage) {
		        m_scanCurrentLeaf = nextLeafID;
		        m_scanLeafPosition = 0;
            }		        
		}
		if (!foundFirstPage) {
		    // We did not find any keys that match our scan
		    throw new Exception ("Could not find any matching keys");
		}
		
		BTFileScan scan = new BTFileScan(this);		        
		m_scanWasInitialized = true;
        return scan;
    }
    
    
    /****************************************************************************
    *	Returns true if the given key matches the current scan range (inclusive comparison)
    */    
    protected boolean matchesCurrentScan(KeyClass key) {
        try {
	        if ((m_scanStartKey == null) && (m_scanEndKey == null)) return true;
	        if ((m_scanStartKey != null) && (m_scanEndKey == null)) return (BT.keyCompare(m_scanStartKey, key) <= 0);
            if ((m_scanStartKey == null) && (m_scanEndKey != null)) return (BT.keyCompare(key, m_scanEndKey) <= 0);	    
            return ((BT.keyCompare(key, m_scanEndKey) <= 0) &&
                    (BT.keyCompare(m_scanStartKey, key) <= 0));
        } catch (Exception e) {}
        return false;                
    }   
    
    
    /****************************************************************************
    *	Gets the next scan key
    */    
    public KeyDataEntry getNextScan() throws Exception {
        // check if we even have the header page loaded:
		if (m_header==null) throw new Exception ("Index was not initialized");        
		
        // check if we even have the header page loaded:
		if (!m_scanWasInitialized) throw new Exception ("Scan was not initialized");        		
		
		// check to see if we reached the end of the scan:
		if (m_scanCurrentLeaf.pid == m_nullPageId) return null;
		
		// get the key at the current position
		Page leafPage = new Page();
		BTLeafPage leafNode;
		KeyDataEntry result;
		try {
		    // Pin down the current page
		    // Note that if the page is already pinned, no extra IO will be done
            SystemDefs.JavabaseBM.pinPage(m_scanCurrentLeaf, leafPage, false); 
            leafNode = new BTLeafPage (leafPage, m_header.get_keyType());            
        } catch (Exception e) {          
            // error found so we cancel the scan
            m_scanWasInitialized = false;
		    throw new Exception("Could not load a leaf node");
        }         
        
        // we now simply return the key data that appears on the current position
        try {            
		    result = getKeyDataAt(leafNode, m_scanLeafPosition);
		    // check if we reached the last key within this scan:
		    if (!matchesCurrentScan(result.key)) {
		        m_scanCurrentLeaf = new PageId(m_nullPageId);
		        return null;
            }
        } catch (Exception e) {          
            closeScan();
            throw new Exception("An outside thread has accessed the current leaf");
        }

		// move up the pointer:		
		m_scanLeafPosition++;
		if (m_scanLeafPosition == leafNode.getSlotCnt()) {
		    // the key that we are about to return is the last one on this leaf
		    // so we need to jump to the next leaf and un-pin this leaf
		    // What we actually need to do is to scroll through the leafs
		    // until we reached non-empty leaf node or until we reach the end of the 
		    // leaf list:
		    boolean stopScroll = false;
		    while (!stopScroll) {
                try {
                    PageId nextPageID = new PageId(leafNode.getNextPage().pid);
                    SystemDefs.JavabaseBM.unpinPage(m_scanCurrentLeaf, false); 
                    m_scanCurrentLeaf = nextPageID;
                    m_scanLeafPosition = 0;
                    if (m_scanCurrentLeaf.pid == m_nullPageId) {
                        // there are no more leafs out to the right
                        stopScroll = true;
                    } else {
                        // pin the leaf down and see if it's empty or not
		                try {
		                    leafPage = new Page();
                            SystemDefs.JavabaseBM.pinPage(m_scanCurrentLeaf, leafPage, false); 
                        } catch (Exception e) {          
                            // error found so we cancel the scan
                            m_scanWasInitialized = false;
		                    throw new Exception("Could not load a leaf node");
                        }                           
		                try {
		                    leafNode = new BTLeafPage (leafPage, m_header.get_keyType());            
                            if (leafNode.getSlotCnt()!=0) {
                                // we found a non-empty page.
                                stopScroll = true;
                            }
                        } catch (Exception e) {          
                            // error found so we cancel unpin the loaded page and cancel the scan
                            try {
                                SystemDefs.JavabaseBM.unpinPage(m_scanCurrentLeaf, false); 
                            } catch (Exception e2) {}
                            m_scanWasInitialized = false;
		                    throw new Exception("Could not load a leaf node");
                        }                         
                    }
                } catch (Exception e2) {
                    // error found so we cancel the scan
                    closeScan();
    		        throw new Exception("Could not unload a leaf node");                
                }
            }
        }
	
        return result;
    }    
    
    
    /****************************************************************************
    *	Returns the leaf key entry at the given position. Throws exception 
    *   if there is no such key to be found or if the an exception is caugth
    *   from a lower level. Position of 0 corresponds to the first key in the
    *   leaf.
    */    
    protected KeyDataEntry getKeyDataAt(BTLeafPage leaf, int position) throws Exception {        
        if (position<0) throw new Exception ("Position parameter cannot be less then zero");   
        KeyDataEntry result;
        RID tmpRID = new RID();
        try {
            int currentPosition = 0;
            result = leaf.getFirst(tmpRID);
            if (result == null) throw new Exception();
            while (currentPosition != position && result!=null) {
                // move on to the next position:
                currentPosition++;
                result = leaf.getNext(tmpRID);
            }            
            if (currentPosition == position) {
                // we found it so we simply return it     
                return new KeyDataEntry (result.key, result.data);
            }            
        } catch (Exception e) {}        
        // if we got this far, we know that an error was found
        throw new Exception ("No key entry exists at that position");    
    }    
    
    
    /****************************************************************************
    *	Deletes the current scan key
    */    
    public void deleteCurrentScan() throws Exception {
        // check if we even have the header page loaded:
		if (m_header==null) throw new Exception ("Index was not initialized");        

        // check if we even have the header page loaded:
		if (!m_scanWasInitialized) throw new Exception ("Scan was not initialized");        		
		
        // check to see if we reached the end of the scan:
	    if (m_scanCurrentLeaf.pid == m_nullPageId) throw new Exception ("No current scan key");        		
		
		// pin the current page:
		Page leafPage = new Page();
		BTLeafPage leafNode;
		try {
		    // Note that if the page is already pinned, no extra IO will be done
            SystemDefs.JavabaseBM.pinPage(m_scanCurrentLeaf, leafPage, false); 
            leafNode = new BTLeafPage (leafPage, m_header.get_keyType());            
        } catch (Exception e) {          
            // error found so we cancel the scan
            m_scanWasInitialized = false;
		    throw new Exception("Could not load a leaf node");
        }         
		
        // find the key at the current position:
        KeyDataEntry keyToDelete;
        try {            
		    keyToDelete = getKeyDataAt(leafNode, m_scanLeafPosition);
        } catch (Exception e) {          
            closeScan();
            throw new Exception("An outside thread has accessed the current leaf");
        }
        
        // delete the key
        try {
		    leafNode.delEntry(keyToDelete);			
        } catch (Exception e) {
            closeScan();
            throw new Exception("An outside thread has accessed the current leaf");
        }       

        // check to pointer position and make sure we are standing on a new
        // current key or that the current page pointer is set to null
        // if the just deleted key was not the last one in this leaf, we don't
        // have to do anything. Otherwise we have to scroll throught he leaf
        // nodes to find the new current key
		if (m_scanLeafPosition == leafNode.getSlotCnt()) {
		    // we are beyond the end of the leaf so we have to find a new one:
		    boolean stopScroll = false;
		    while (!stopScroll) {
                try {
                    PageId nextPageID = new PageId(leafNode.getNextPage().pid);
                    SystemDefs.JavabaseBM.unpinPage(m_scanCurrentLeaf, true); 
                    m_scanCurrentLeaf = nextPageID;
                    m_scanLeafPosition = 0;
                    if (m_scanCurrentLeaf.pid == m_nullPageId) {
                        // there are no more leafs out to the right
                        stopScroll = true;
                    } else {
                        // pin the leaf down and see if it's empty or not
		                try {
		                    leafPage = new Page();
                            SystemDefs.JavabaseBM.pinPage(m_scanCurrentLeaf, leafPage, false); 
                        } catch (Exception e) {          
                            // error found so we cancel the scan
                            m_scanWasInitialized = false;
		                    throw new Exception("Could not load a leaf node");
                        }                           
		                try {
		                    leafNode = new BTLeafPage (leafPage, m_header.get_keyType());            
                            if (leafNode.getSlotCnt()!=0) {
                                // we found a non-empty page.
                                stopScroll = true;
                            }
                        } catch (Exception e) {          
                            // error found so we cancel unpin the loaded page and cancel the scan
                            try {
                                SystemDefs.JavabaseBM.unpinPage(m_scanCurrentLeaf, false); 
                            } catch (Exception e2) {}
                            m_scanWasInitialized = false;
		                    throw new Exception("Could not load a leaf node");
                        }                         
                    }
                } catch (Exception e2) {
                    // error found so we cancel the scan
                    closeScan();
    		        throw new Exception("Could not unload a leaf node");                
                }
            }
        }		
		
		
    }       
    

    /****************************************************************************
    *	Closes the scan session
    */    
    public void closeScan() {
        if (m_scanWasInitialized) {
            m_scanWasInitialized = false;            
            // unpin the current scan page if it exists:            
            if (m_scanCurrentLeaf.pid != m_nullPageId) {
                try {                
                    SystemDefs.JavabaseBM.unpinPage(m_scanCurrentLeaf, false);                 
                } catch (Exception e2) {}            
            }
        }        
    }   


	/****************************************************************************
	*	Displays the tree
	*/    	
	public void print() throws Exception {
        // check if we even have the header page loaded:
		if (m_header==null) throw new Exception ("Index was not initialized");
		
		// we simply need to grab the root node and call
		// a recursive function:
		System.out.print ("\nTREE VIEW (DEPTH-FIRST, LEFT-TO-RIGHT):");
		Page rootNode = new Page();
		PageId rootPageId;
		try {
			rootPageId = m_header.get_rootId();		
			SystemDefs.JavabaseBM.pinPage(rootPageId, rootNode, false); 
		} catch (Exception e) {
			throw new Exception("Could not load the root node");
		}
  
		// we need to mark the fact that we have pinned the current page
		
		HFPage tmpHFPage = new HFPage (rootNode);
		m_pinnedNodes.addElement(tmpHFPage);		
		
		try {
			printSubTree(tmpHFPage, "| ");
		} catch (Exception e) {
			throw new Exception("Could not print the entire tree");
		}
		unpinPages(false);		
		System.out.print ("\n");
	}

	/****************************************************************************
	*	Displays sub-tree with an indent
	*/    	
	protected void printSubTree(HFPage hfnode, String indent) throws Exception {
		RID dummy_rid = new RID();			
		System.out.print ("\n" + indent + "NODE " + (new Integer(hfnode.getCurPage().pid)).toString() + ":");
		if (hfnode.getCurPage().pid == m_header.get_rootId().pid) System.out.print (" (root)");
		if (hfnode.getType()==NodeType.LEAF) {
			System.out.print (" (leaf)");
			RID fetch_rid = new RID();			
			BTLeafPage node = new BTLeafPage(hfnode, m_header.get_keyType());
			KeyDataEntry fetch_data = node.getFirst(dummy_rid);
			fetch_rid = ((LeafData)fetch_data.data).getData();
			while (fetch_data!=null) {
			    String key_string = fetch_data.key.toString();
				String page_id_string = (new Integer (fetch_rid.pageNo.pid)).toString();
				String slot_string = (new Integer (fetch_rid.slotNo)).toString();				
				System.out.print ("\n" + indent + "   key: " + key_string + " ->  (" + page_id_string + "," + slot_string + ")");
				// grab the next key:
				fetch_data = node.getNext(dummy_rid); 
				if (fetch_data!=null) fetch_rid = ((LeafData)fetch_data.data).getData();
			}
		} else {
			// we are at an index node
			BTIndexPage node = new BTIndexPage(hfnode, m_header.get_keyType());
		
			// First grab the left-most page:
			PageId leftMostPageId = node.getPrevPage();
			Page childNode = new Page();
			try {
				SystemDefs.JavabaseBM.pinPage(leftMostPageId, childNode, false); 
				// we need to mark the fact that we have pinned the current page
				// and mark the fact
				m_pinnedNodes.addElement(new HFPage(childNode));								
			} catch (Exception e) {
				throw new Exception("Could not load a child node");
			}
			// display the left-most child node:
			HFPage tmpHFPage = new HFPage (childNode);
			try {
				printSubTree(tmpHFPage, indent + "             ");
			} catch (Exception e) {
				throw new Exception("Could not print the entire tree");
			}			
			
			// Now that we have printed the node on the left-most, we can
			// go into a loop and print the rest of <key,node> pairs.						
			
			KeyDataEntry fetch_data = node.getFirst(dummy_rid);
			PageId fetch_pid = null;
			while (fetch_data!=null) {
				fetch_pid = node.getPageNoByKey(fetch_data.key);
				String key_string = fetch_data.key.toString();
				childNode = new Page();
				try {
					SystemDefs.JavabaseBM.pinPage(fetch_pid, childNode, false); 
					m_pinnedNodes.addElement(new HFPage(childNode));					
				} catch (Exception e) {
					throw new Exception("Could not load a child node");
				}
				// diplay the child node
				tmpHFPage = new HFPage (childNode);
				try {
					System.out.print ("\n" + indent + "   key: " + key_string);
					printSubTree(tmpHFPage, indent + "             ");
				} catch (Exception e) {
					throw new Exception("Could not print the entire tree");
				}							
				fetch_data = node.getNext(dummy_rid); 				
			}		
		}		
	}


    /****************************************************************************
    *	Returns PIDs of all leaf nodes below the given page
    */
    protected void findAllLeafNodes(PageId pageId, Vector pageIDList) throws Exception {
        try {
            // Pin the current node:
            Page page = new Page();        
			SystemDefs.JavabaseBM.pinPage(pageId, page, false); 			
    		HFPage hfPage = new HFPage(page);
	    	if (hfPage.getType()!=NodeType.LEAF) {
		        // it's not a leaf node so we have some work to do:
    		    BTIndexPage indexPage = new BTIndexPage(page, m_header.get_keyType());
	    	    RID tmpRID = new RID();
	    	    // put all the child IDs into a vector	    	        	    
	    	    PageId childPageID;
	    	    Vector childPageIDs = new Vector();	    	    
	    	    childPageID = indexPage.getPrevPage();
	    	    childPageIDs.addElement(childPageID);		        
		        KeyDataEntry data = indexPage.getFirst(tmpRID);		        
    		    while (data!=null) {
    		        try {
    		            childPageID = new PageId (indexPage.getPageNoByKey(data.key).pid);
    	    	        childPageIDs.addElement(childPageID);
	        	        data = indexPage.getNext(tmpRID);		            		            
                    } catch (Exception e2) {
                        data = null;
                    }		        
		        }
		        // we gathered all the child page IDs and we can now unpin the page:
		        SystemDefs.JavabaseBM.unpinPage(pageId, false); 					        
		        
		        // childPageIDs now contains all the PIDs of child nodes
		        Enumeration enPIDs = childPageIDs.elements(); 
		        while (enPIDs.hasMoreElements()) {
		            childPageID = (PageId) enPIDs.nextElement();
                    findAllLeafNodes(childPageID, pageIDList);
                }
		    } else {
                pageIDList.addElement(pageId);		        
                SystemDefs.JavabaseBM.unpinPage(pageId, false); 					        
		    }
		} catch (Exception e) { 
			throw new Exception("Could not pin a node page"); 
		}	        
    }        
        

    /****************************************************************************
    *	Appends all PIDs below the current node into the given vector
    */
    protected void findAllNodes(PageId pageId, Vector pageIDList) throws Exception {
        // pageId is already in the pageIDList
        // If node ID of pageId is a leaf node, we are done.
        // If node ID of pageId is not a leaf node, we need to add all its
        // page IDs into pageIDList and call the function recursivelly 
        // on the child nodes        
        try {
            // Pin the current node:
            Page page = new Page();
        
			SystemDefs.JavabaseBM.pinPage(pageId, page, false); 			
    		HFPage hfPage = new HFPage(page);
	    	if (hfPage.getType()!=NodeType.LEAF) {
		        // it's not a leaf node so we have some work to do:
    		    BTIndexPage indexPage = new BTIndexPage(page, m_header.get_keyType());
	    	    RID tmpRID = new RID();
	    	    // put all the child IDs into a vector
	    	    PageId childPageID;
	    	    Vector childPageIDs = new Vector();	    	    
	    	    childPageID = indexPage.getPrevPage();
	    	    childPageIDs.addElement(childPageID);		        
		        KeyDataEntry data = indexPage.getFirst(tmpRID);		        
    		    while (data!=null) {
    		        try {
    		            childPageID = new PageId (indexPage.getPageNoByKey(data.key).pid);
    	    	        childPageIDs.addElement(childPageID);
	        	        data = indexPage.getNext(tmpRID);		            		            
                    } catch (Exception e2) {
                        data = null;
                    }		        
		        }
		        // we gathered all the child page IDs and we can now unpin the page:
		        SystemDefs.JavabaseBM.unpinPage(pageId, false); 					        
		        
		        // childPageIDs now contains all the PIDs of child nodes
		        Enumeration enPIDs = childPageIDs.elements(); 
		        while (enPIDs.hasMoreElements()) {
		            childPageID = (PageId) enPIDs.nextElement();
		            pageIDList.addElement(childPageID);
                    findAllNodes(childPageID, pageIDList);
                }
		    } else {
                SystemDefs.JavabaseBM.unpinPage(pageId, false); 					        
		    }
		} catch (Exception e) { 
			throw new Exception("Could not pin a node page"); 
		}	
    }
    

	/****************************************************************************
    *	Recursive insert into a node. The recursion happens if the node is full.
	*	The function travels up the tree based on the nodes in m_pinnedNodes.
	*	nodeCounter is the index to the m_pinnedNodes vector where the node
	*	is located.
    */
    protected void insertIntoNode(int nodeCounter, KeyDataEntry keyData, PageId pageId) throws Exception { 	
	
		// First we need to find out what type of node we are dealing with:
		Page pinnedPage = (Page) m_pinnedNodes.elementAt(nodeCounter);
		HFPage tmpPage = new HFPage(pinnedPage);
	
		if (tmpPage.getType()==NodeType.LEAF) {
			// It's a leaf node.
			// Try inserting the new key. If we get an exception, then
			// we know we need to split the node because its full
			BTLeafPage tmpLeafNode = null;
			RID newRID = null;
			try {						
				tmpLeafNode = new BTLeafPage(pinnedPage, m_header.get_keyType());
				newRID = ((LeafData) keyData.data).getData();				
				RID tmpRID = tmpLeafNode.insertRecord(keyData.key, newRID);
				if (tmpRID==null) throw new Exception ();
				// Here we know that the simple insert was successful so we simply return
				return;
			} catch (Exception e) {
				// Insert failed so our node must be full. We have to split the node.
				BTLeafPage emptyPage = createLeafPage();
 				KeyClass 	keyForParent = redistributeLeafAndInsert(tmpLeafNode, emptyPage, keyData.key, newRID);
								
				PageId 		pageIdForParent = emptyPage.getCurPage();
				
				// We now have to check if the original leaf node was the root:
				if (m_header.get_rootId().pid == tmpLeafNode.getCurPage().pid) {
   			

					// we need new root:
					BTIndexPage newRoot = createIndexPage();
					newRoot.insertKey(keyForParent, pageIdForParent);
					newRoot.setPrevPage(tmpLeafNode.getCurPage());
					m_header.set_rootId(newRoot.getCurPage());					
				} else {
					// the current page is not a root and there is a parent index node above it:
					insertIntoNode(nodeCounter - 1, new KeyDataEntry(keyForParent, pageIdForParent), pageIdForParent);
				}
			}
		} else {

			// It's an index node.
			// Try inserting the new key. If we get an exception, then
			// we know we need to split the node because its full
			BTIndexPage tmpIndexNode = null;
			PageId newPageId = null;
			try {						
				tmpIndexNode = new BTIndexPage(pinnedPage, m_header.get_keyType());
				newPageId = pageId; 
								
				RID tmpRID = tmpIndexNode.insertKey(keyData.key, newPageId);
				if (tmpRID==null) throw new Exception ();
				// Here we know that the simple insert was successful so we simply return
				return;
			} catch (Exception e) {
				// Insert failed so our node must be full. We have to split the node.
				BTIndexPage emptyPage = createIndexPage();
 				KeyClass 	keyForParent = redistributeIndexAndInsert(tmpIndexNode, emptyPage, keyData.key, newPageId);
								
				PageId 		pageIdForParent = emptyPage.getCurPage();
				
				// We now have to check if the original index node was the root:
				if (m_header.get_rootId().pid == tmpIndexNode.getCurPage().pid) {

					// we need new root:
					BTIndexPage newRoot = createIndexPage();
					newRoot.insertKey(keyForParent, pageIdForParent);
					newRoot.setPrevPage(tmpIndexNode.getCurPage());
					m_header.set_rootId(newRoot.getCurPage());					
				} else {
					// the current page is not a root and there is a parent index node above it:
					insertIntoNode(nodeCounter - 1, new KeyDataEntry(keyForParent, pageIdForParent), pageIdForParent);
				}
			}			
		}
	}			


    /****************************************************************************
    *	Unpins all pages other then the root node
    */    
    protected void unpinPages(boolean dirty) {
		Enumeration enPinnedNodes = m_pinnedNodes.elements();
		while (enPinnedNodes.hasMoreElements()) {			
			HFPage tmpPage = (HFPage) enPinnedNodes.nextElement();
			try {				
				SystemDefs.JavabaseBM.unpinPage(tmpPage.getCurPage(), dirty); 
			} catch (Exception e2) {}
		}
		m_pinnedNodes.removeAllElements();
    }		


	/****************************************************************************
  	*	Redistributes keys from one index page into another. Target node has to be empty.
  	*/       
  	protected KeyClass redistributeIndexAndInsert(BTIndexPage sourceIndex, BTIndexPage targetIndex, KeyClass newKey, PageId newPageId) throws Exception {
		BTIndexPage newKeyDestination = null;

		Vector oldKeys = new Vector();
		KeyClass result = null;
		try {
			// check the target node:
			if (targetIndex.getSlotCnt()!=0) throw new Exception();
	
			// find out how many keys we want to move			
			int firstToMove = sourceIndex.getSlotCnt() / 2;  

			int keyPosition = 1;
			PageId moving_pid = new PageId();
			RID dummy_rid = new RID();
			
			KeyDataEntry moving_data = sourceIndex.getFirst(dummy_rid);
			if (moving_data==null) throw new Exception();
			moving_pid = sourceIndex.getPageNoByKey(moving_data.key);	
			boolean movedAnyYet = false;
			while (moving_data!=null) {					
				if (keyPosition >= firstToMove) {
					if (!movedAnyYet) {
						// this key would end up at the front of the target
						// node, so we need to check if it's bigger or smaller then
						// our new key:
						if (BT.keyCompare(newKey, moving_data.key) > 0) {
							// we want to insert into the target node
							newKeyDestination = targetIndex;
						} else {
							newKeyDestination = sourceIndex;
						}
					} 
					// copy the pair over ot the target node
					targetIndex.insertKey(moving_data.key, moving_pid);

					// mark that we need to delete this RID
					oldKeys.addElement(moving_data.key);
					movedAnyYet = true;
				}
				moving_data = sourceIndex.getNext(dummy_rid);
				if (moving_data!=null) {
					moving_pid = sourceIndex.getPageNoByKey(moving_data.key);	
					keyPosition++; 
				}
			}
			// delete all the old RIDs:
			Enumeration enOldKeys = oldKeys.elements();			
 			while (enOldKeys.hasMoreElements()) {				
 				sourceIndex.deleteKey((KeyClass) enOldKeys.nextElement());
			}
			
			// Now that we have rhough room in both nodes, we can move the new key
			// into the right spot:
			newKeyDestination.insertKey(newKey, newPageId);
			
			// Delete the first key from the target node and return the deleted key
			KeyDataEntry deleted_data = targetIndex.getFirst(dummy_rid);			
			targetIndex.setPrevPage(targetIndex.getPageNoByKey(deleted_data.key));									
			targetIndex.deleteKey(deleted_data.key);						
			return deleted_data.key;
			
		} catch (Exception e) { 
			throw new Exception("Could not redistribute the index");			
		}	

	}

	
	/****************************************************************************
  	*	Redistributes keys from one leaf page into another. Target node has to be empty.
  	*/       
  	protected KeyClass redistributeLeafAndInsert(BTLeafPage sourceLeaf, BTLeafPage targetLeaf, KeyClass newKey, RID newRID) throws Exception {
		BTLeafPage newKeyDestination = null;
	
		Vector oldData = new Vector();
		try {
			// check the target node:
			if (targetLeaf.getSlotCnt()!=0) throw new Exception();
		
			// find out how many keys we want to move			
			int firstToMove = sourceLeaf.getSlotCnt() / 2;  

			int keyPosition = 1;
			RID moving_rid = new RID();
			RID dummy_rid = new RID();
			KeyClass dummy_key;
			
			KeyDataEntry moving_data = sourceLeaf.getFirst(dummy_rid);

			if (moving_data==null) throw new Exception();
			moving_rid = ((LeafData)moving_data.data).getData();	
			boolean movedAnyYet = false;
			while (moving_data!=null) {					
				if (keyPosition >= firstToMove) {
					if (!movedAnyYet) {					
										
						// this key would end up at the front of the target
						// node, so we need to check if it's bigger or smaller then
						// our new key:
					    if (BT.keyCompare(newKey, moving_data.key) > 0) {
							// we want to insert into the target node
							newKeyDestination = targetLeaf;
						} else {
							newKeyDestination = sourceLeaf;
						}
					}
					// copy the pair over ot the target node
					targetLeaf.insertRecord(moving_data.key, moving_rid);
					// mark that we need to delete this RID
					oldData.addElement(new KeyDataEntry(moving_data.key, moving_rid));
					movedAnyYet = true;
												
				}
				moving_data = sourceLeaf.getNext(dummy_rid);
				
				if (moving_data!=null) {
					moving_rid = ((LeafData)moving_data.data).getData();	
					keyPosition++; 
				}
			}
			
			// delete all the old RIDs:
			Enumeration enOldData = oldData.elements();
			while (enOldData.hasMoreElements()) {
				sourceLeaf.delEntry((KeyDataEntry)enOldData.nextElement());
			}
		
			// Now that we have enough room in both nodes, we can move the new key
			// into the right spot:
			newKeyDestination.insertRecord(newKey, newRID);

			// Double link both nodes:
			targetLeaf.setPrevPage(sourceLeaf.getCurPage());									
			targetLeaf.setNextPage(sourceLeaf.getNextPage());									
			sourceLeaf.setNextPage(targetLeaf.getCurPage());	

 			// Find the first key from the target node and return it			
			KeyDataEntry first_data = targetLeaf.getFirst(dummy_rid);			
			
			return first_data.key;			
			
		} catch (Exception e) { 
			throw new Exception("Could not redistribute the leaf");			
		}	

	}	
	
	
	/****************************************************************************
  	*	Creates a new leaf node. The new node will be be pinned in the memory
  	*/       
  	protected BTLeafPage createLeafPage() throws Exception {
		try {
			return new BTLeafPage(m_header.get_keyType());
		} catch (Exception e) { 
			throw new Exception("Could not create the page");			
		}					
	}
	
	
  	/****************************************************************************
  	*	Creates a new index node. The new node will be be pinned in the memory
  	*/    
  	protected BTIndexPage createIndexPage() throws Exception {
		try {
            return new BTIndexPage(m_header.get_keyType());			
		} catch (Exception e) { 
			throw new Exception("Could not create the page");			
		}					
	}	

	
	/****************************************************************************
	*	Finds the leaf node that should store (or stores now) the value 
	*	of the key. As the method goes down, it pins pages down. It does not
	*	unpin these pages. It also puts the visited pages in a vector
	*	list for future use.
	*/    
	protected BTLeafPage findWantedLeaf(KeyClass key) throws Exception {
		// First thing we need to do is to bring up the root node:
		Page treeNode = new Page();
		PageId treePageId = m_header.get_rootId();
		try {
			SystemDefs.JavabaseBM.pinPage(treePageId, treeNode, false); 
			// we need to mark the fact that we have pinned the current page
			m_pinnedNodes.addElement(new HFPage(treeNode));
		} catch (Exception e) {
			throw new Exception("Could not load the root node");
		}
   
		// Now that we have the root node in the memory, we
		// can go into the 'recursive' loop. First thing we need
		// to do is to find the leaf node where the key should
		// be inserted. 

		boolean foundLeafNode = false;
		while (!foundLeafNode) {
			
			HFPage tmpHFPage = new HFPage (treeNode);
			if (tmpHFPage.getType()==NodeType.LEAF) {
				// the current node is a leaf node
				foundLeafNode = true;
			} else {								
			
				// We are not at the leaf node yet. We need
				// to go down the tree				
				BTIndexPage non_leaf_node = new BTIndexPage(treeNode, m_header.get_keyType());
				PageId jumpPageID = non_leaf_node.getPrevPage();
				// find the right page to jump to:				
				RID dummy_rid = new RID();
				KeyDataEntry fetch_data = non_leaf_node.getFirst(dummy_rid);
				boolean stopSearch = false;
				while (!stopSearch) {					
					if (fetch_data==null) {
						// we have reached the end of the keys list and we can quit
						stopSearch = true;
					} else {
						if (BT.keyCompare(fetch_data.key, key) > 0) {						
							// We have reached a key that is greater than the one 
							// we have. The previous page number should be used						
							stopSearch = true;
						} else {
							// The key that we are on right now is less or equal to 
							// the key that we have. So we go on with searching
							jumpPageID = non_leaf_node.getPageNoByKey(fetch_data.key); // record the page number
							fetch_data = non_leaf_node.getNext(dummy_rid); // go to the next key								
						}			
					}
				}		
				try {
					SystemDefs.JavabaseBM.pinPage(jumpPageID, treeNode, false); 
					// we need to mark the fact that we have pinned the current page
					m_pinnedNodes.addElement(new HFPage(treeNode));
				} catch (Exception e) {}
			}			
		}
		return new BTLeafPage(treeNode, m_header.get_keyType());		
    }	

	/****************************************************************************
	*	Copies contents of one page into another
	*/    	
	protected void copyPageData(Page source, Page destination) {
		byte[] array_source = source.getpage();
		byte[] array_destination = destination.getpage();
		for (int a = 0; a < GlobalConst.MINIBASE_PAGESIZE; a++) array_destination[a] = array_source[a];
	}	
}
