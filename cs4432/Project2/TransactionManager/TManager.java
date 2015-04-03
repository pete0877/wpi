/* FILE:        TManager.java
 * CLASS:       TManager
 * PACKAGE:     
 * VERSION:     1.0 
 * UPDATED:     4/21/00
 * AUTHOR:      Peter Golaszewski <peterg@wpi.edu>
 */
 
package TransactionManager;

import java.util.Vector;
import java.util.Enumeration;
import java.util.StringTokenizer;

public class TManager {
    // CONSTANTS
    protected final String    START_TRANSACTION         = "ST";
    protected final String    END_TRANSACTION           = "EN";
    protected final String    REQUEST_SHARED_LOCK       = "SL";
    protected final String    REQUEST_EXCLUSIVE_LOCK    = "XL";
    protected final String    RELEASE_LOCK              = "UO";
    
    // DATA MEMEBERS
    protected Vector          m_results;
    protected TStateTable     m_table;

    // CONSTRUCTORS
    /***************************************************************************
    *   Default constructor. Creates an empty state table
    */             
	public TManager () {
	    m_results   = new Vector();
	    m_table     = new TStateTable();
	}

    // METHODS
    /***************************************************************************
    *   Parses string commands and calls appropriate public methods
    */         
    public void parseStringCommand(String command) {
       
        // get the command tokens:
        StringTokenizer tokens = new StringTokenizer(command, " ");
        String tmpCommand = null;
        String tmpTransaction = null;
        String tmpObject = null;
        try {
            tmpCommand = tokens.nextToken();
            tmpTransaction = tokens.nextToken();
            tmpObject = tokens.nextToken();
        } catch (Exception e) {}        
        
        // check the required input:
        if (tmpCommand==null) {         
            m_results.addElement("Missing or invalid command.");
            return;
        }
        if (tmpTransaction==null) {
            m_results.addElement("Missing or invalid transaction number.");
            return;
        }
        
        // make sure transaction is a number:
        Integer transactionID;
        try {
            transactionID = new Integer(tmpTransaction);
        } catch (Exception e) {
            m_results.addElement("Missing or invalid transaction number.");
            return;
        }
        TTransactionID transaction = new TTransactionID(transactionID);       
        
        if (tmpCommand.equals(START_TRANSACTION)) {
            // start new transaction:
            startTransaction(transaction);
        } else if (tmpCommand.equals(END_TRANSACTION)) {
            // end this transaction:
            endTransaction(transaction);
        } else if (tmpCommand.equals(REQUEST_SHARED_LOCK)) {
            if (tmpObject==null) {
                m_results.addElement("Missing or invalid object name.");
                return;
            }
            TObjectID object = new TObjectID(tmpObject);
            requestShareLock(transaction, object);
        } else if (tmpCommand.equals(REQUEST_EXCLUSIVE_LOCK)) {
            if (tmpObject==null) {
                m_results.addElement("Missing or invalid object name.");
                return;
            }
            TObjectID object = new TObjectID(tmpObject);
            requestExclusiveLock(transaction, object);            
        } else if (tmpCommand.equals(RELEASE_LOCK)) {
            if (tmpObject==null) {
                m_results.addElement("Missing or invalid object name.");
                return;
            }
            TObjectID object = new TObjectID(tmpObject);
            this.releaseLock(transaction, object);  
        } else m_results.addElement("Missing or invalid command.");
    }

    /***************************************************************************
    *   Returns and deletes one line of the output message buffer, or returns null 
    *   if the buffer is empty
    */                    
    public String getMessageLine() {
        if (m_results.size()==0) return null;
        String tmpResult = (String) m_results.elementAt(0);
        m_results.remove(0);
        return tmpResult;
    }
    
    /***************************************************************************
    *   Starts transaction
    */                    
    public void startTransaction(TIdentifiable transaction) {
        // check to see if the transaction is running or not
        if (m_table.userExists(transaction)) {
            m_results.addElement("Transaction " + transaction.toString() + " already running.");
        } else { 
            m_table.addUser(transaction);        
            m_results.addElement("Transaction " + transaction.toString() + " started.");            
        }            
    }    
    
    public void endTransaction(TIdentifiable transaction) {
        // check to see if the transaction is even running
        if (!m_table.userExists(transaction)) {
            m_results.addElement("Transaction " + transaction.toString() + " is not running.");            
            return;
        }            
        
        // display the message first
        m_results.addElement("Transaction " + transaction.toString() + " stopped.");
        
        // we need to realease any locks owed by this transaction:
        Vector useStatuses = m_table.getResources(transaction);
        if (useStatuses!=null) {
            Enumeration enStatuses = useStatuses.elements();
            while (enStatuses.hasMoreElements()) {
                TUseStatus status = (TUseStatus) enStatuses.nextElement();
                if (    (status.getStatus()==status.EXCLUSIVELY_LOCKED) ||
                        (status.getStatus()==status.SHARED)) {
                    releaseLock(transaction, status.getResource());
                }
            }
        }
        // now that all the locks for this transaction are release, we can 
        // remove it from the state table:
        m_table.removeUser(transaction);        
    }    
    
    /***************************************************************************
    *   Appempts to extablish exclusive shared-lock on object by transaction
    */                
    public void requestShareLock(TIdentifiable transaction, TIdentifiable object) {
        // check to see if the transaction is even running
        if (!m_table.userExists(transaction)) {
            m_results.addElement("Transaction " + transaction.toString() + " is not running.");            
            return;
        }                   
        // check if this is a new resource, and if yes, add it:
        if (!m_table.resourceExists(object)) m_table.addResource(object);
             
        // check the status to make sure nothing illegal is going on:
        TUseStatus existingStatus = m_table.getUseStatus(transaction, object);        
        if (existingStatus!=null) {
            if (existingStatus.getStatus()==existingStatus.SHARED) {
                m_results.addElement("Object already shared.");                                  
                return;
            }
            if (existingStatus.getStatus()==existingStatus.EXCLUSIVELY_LOCKED) {
                m_results.addElement("Object already locked. You must unlock first.");                                  
                return;
            }                        
        }
        
        // collect the list of transactions that are preventing us from 
        // granting the desired access so that we can detect dead-locks:
        Vector lockingTransactions = new Vector();        
        
        // we can grant the shared lock only if there are no exclusive locks on this resource:
        Vector useStatuses = m_table.getUsers(object);
        boolean grantAccess = true;
        String lockingObjects = "";
        if (useStatuses!=null) {
            Enumeration enUses = useStatuses.elements();
            while (enUses.hasMoreElements()) {
                TUseStatus status = (TUseStatus) enUses.nextElement();
                if (status.getStatus()==status.EXCLUSIVELY_LOCKED) {
                    grantAccess = false;
                    TIdentifiable usingObject = status.getUser();
                    lockingObjects = lockingObjects + usingObject.toString() + " ";
                    lockingTransactions.addElement(usingObject);
                }
           }
        }
        // check if we found any locking conditions:
        if (grantAccess) {                        
            TUseStatus newStatus = new TUseStatus((new TUseStatus()).SHARED, transaction, object);
            m_table.setUseStatus(transaction, object, newStatus);
            m_results.addElement("Granted share lock on " + object.toString() + " to transaction " + transaction.toString() + ".");            
        } else {
            TUseStatus newStatus = new TUseStatus((new TUseStatus()).WAITING_FOR_SHARED, transaction, object);            
            m_table.setUseStatus(transaction, object, newStatus);
            m_results.addElement("Waiting for lock (exclusive lock held by: " + lockingObjects + ").");              
            // Look for dead-locks
            Enumeration enLockingTransactions = lockingTransactions.elements();
            while (enLockingTransactions.hasMoreElements()) {
                TIdentifiable lockingTransaction = (TIdentifiable) enLockingTransactions.nextElement();
                // find out if 'lockingTransaction' waits for any objects locked/shared by 'transaction'
                if (transactionWaitsFor(lockingTransaction, transaction)) 
                    m_results.addElement("Detected dead-lock between transaction " + transaction.toString() + " and " + lockingTransaction.toString() + ".");              
            }
        }
    }    
    
    /***************************************************************************
    *   Appempts to extablish exclusive lock on object by transaction
    */            
    public void requestExclusiveLock(TIdentifiable transaction, TIdentifiable object) {
        // check to see if the transaction is even running
        if (!m_table.userExists(transaction)) {
            m_results.addElement("Transaction " + transaction.toString() + " is not running.");            
            return;
        }                    
        // check if this is a new resource, and if yes, add it:
        if (!m_table.resourceExists(object)) m_table.addResource(object);
        
        // check the status to make sure nothing illegal is going on:
        TUseStatus existingStatus = m_table.getUseStatus(transaction, object);        
        if (existingStatus!=null) {
            if (existingStatus.getStatus()==existingStatus.EXCLUSIVELY_LOCKED) {
                m_results.addElement("Object already locked.");                                  
                return;
            }                        
        }        
        
        // collect the list of transactions that are preventing us from 
        // granting the desired access so that we can detect dead-locks:
        Vector lockingTransactions = new Vector();         
        
        // we can grant the shared lock only if there are no exclusive locks on this resource:
        Vector useStatuses = m_table.getUsers(object);
        boolean grantAccess = true;
        boolean tryingUpdgrade = false;
        String sharedLockingObjects = "";
        String exclusiveLockingObjects = "";
        TIdentifiable usingObject;
        if (useStatuses!=null) {
            Enumeration enUses = useStatuses.elements();
            while (enUses.hasMoreElements()) {
                TUseStatus status = (TUseStatus) enUses.nextElement();
                if (status.getStatus()==status.EXCLUSIVELY_LOCKED) {
                    grantAccess = false;
                    usingObject = status.getUser();
                    exclusiveLockingObjects = exclusiveLockingObjects + usingObject.toString() + " ";
                    lockingTransactions.addElement(usingObject);
                }
                if (status.getStatus()==status.SHARED) {
                    // check if this is an upgrade deal:
                    if (transaction.equals(status.getUser())) {
                        tryingUpdgrade=true; 
                    } else {
                        grantAccess = false;
                        usingObject = status.getUser();
                        sharedLockingObjects = sharedLockingObjects + usingObject.toString() + " ";
                        lockingTransactions.addElement(usingObject);
                    }
                }                
           }
        }
        // check if we found any locking conditions:
        if (grantAccess) {                        
            TUseStatus newStatus = new TUseStatus((new TUseStatus()).EXCLUSIVELY_LOCKED, transaction, object);
            m_table.setUseStatus(transaction, object, newStatus);            
            if (tryingUpdgrade) 
                m_results.addElement("Granted exclusive lock on " + object.toString() + " to transaction " + transaction.toString() + " (upgrade).");            
            else 
                m_results.addElement("Granted exclusive lock on " + object.toString() + " to transaction " + transaction.toString() + ".");            
        } else {
            TUseStatus newStatus = new TUseStatus((new TUseStatus()).WAITING_FOR_EXCLUSIVELY_LOCKED, transaction, object);            
            m_table.setUseStatus(transaction, object, newStatus);
            if (!exclusiveLockingObjects.equals("")) 
                m_results.addElement("Waiting for lock (exclusive lock held by: " + exclusiveLockingObjects + ")");              
            if (!sharedLockingObjects.equals("")) 
                m_results.addElement("Waiting for lock (shared lock held by: " + sharedLockingObjects + ")");                  
            // Look for dead-locks
            Enumeration enLockingTransactions = lockingTransactions.elements();
            while (enLockingTransactions.hasMoreElements()) {
                TIdentifiable lockingTransaction = (TIdentifiable) enLockingTransactions.nextElement();
                // find out if 'lockingTransaction' waits for any objects locked/shared by 'transaction'
                if (transactionWaitsFor(lockingTransaction, transaction)) 
                    m_results.addElement("Detected dead-lock between transaction " + transaction.toString() + " and " + lockingTransaction.toString() + ".");              
            }
        }
    }    
    
    /***************************************************************************
    *   Releases a lock held by transaction on the object
    */        
    public void releaseLock(TIdentifiable transaction, TIdentifiable object) {
        // check to see if the transaction is even running
        if (!m_table.userExists(transaction)) {
            m_results.addElement("Transaction " + transaction.toString() + " is not running.");            
            return;
        }   
        // check to see if the object even exists
        if (!m_table.resourceExists(object)) {
            m_results.addElement("Object " + object.toString() + " does not exist.");            
            return;
        }         
        // check to see if there even is any lock between the object and the transaction
        // check the status to make sure nothing illegal is going on:
        TUseStatus existingStatus = m_table.getUseStatus(transaction, object);           
        if (existingStatus==null) {
            m_results.addElement("Transaction " + transaction.toString() + " has no locks on object " + object.toString() + ".");            
            return;
        }                 
        if (existingStatus.getStatus()==existingStatus.NO_ASSOCIATION) {
            m_results.addElement("Transaction " + transaction.toString() + " has no locks on object " + object.toString() + ".");            
            return;
        }                         
        
        if (existingStatus.getStatus()==existingStatus.SHARED)
            m_results.addElement("Transaction " + transaction.toString() + " released shared lock on " + object.toString() + ".");            
        
        if (existingStatus.getStatus()==existingStatus.EXCLUSIVELY_LOCKED)
            m_results.addElement("Transaction " + transaction.toString() + " released exclusive lock on " + object.toString() + ".");            
                    
            
        // Erase the existing status:
        m_table.setUseStatus(transaction, object, null);           
        
        
        // Now we can check to see if any other transactions are waiting for this object
        boolean foundShares = false;
        boolean foundExclusiveLocks = false;
        Vector waitingShares = new Vector();
        Vector waitingExclusiveLocks = new Vector();
        Vector allStates = m_table.getUsers(object);
        Enumeration enStates = allStates.elements();
        TUseStatus tmpState;
        while (enStates.hasMoreElements()) {
            tmpState = (TUseStatus) enStates.nextElement();
            if (tmpState.getStatus()==tmpState.EXCLUSIVELY_LOCKED) foundExclusiveLocks = true;
            if (tmpState.getStatus()==tmpState.SHARED) foundShares = true;
            if (tmpState.getStatus()==tmpState.WAITING_FOR_EXCLUSIVELY_LOCKED) waitingExclusiveLocks.addElement(tmpState);
            if (tmpState.getStatus()==tmpState.WAITING_FOR_SHARED) waitingShares.addElement(tmpState);
        }
        // now we need to make our decision as to who gets the locks / shares
        // Our policy is to give higher priority to shares:
        if ((waitingShares.size() > 0) && (!foundExclusiveLocks)) {
            // there are no exclusive locks present, we so can 
            // give the share status to all these waiting transactions:
            enStates = waitingShares.elements();
            while (enStates.hasMoreElements()) {                
                tmpState = (TUseStatus) enStates.nextElement();
                TUseStatus newStatus = new TUseStatus(tmpState.SHARED, tmpState.getUser(), object);
                m_table.setUseStatus(tmpState.getUser(), object, newStatus);            
                m_results.addElement("Granted share lock on " + object.toString() + " to transaction " + transaction.toString() + ".");            
            }
            return;
        }
        if ((waitingExclusiveLocks.size() > 0) && (!foundExclusiveLocks) && (!foundShares)){
            // there are no other shares or locks on this object so
            // we can give the lock status to the longest waiting object
            
            TUseStatus oldestRequest = (TUseStatus) waitingExclusiveLocks.elementAt(0);
            enStates = waitingExclusiveLocks.elements();
            while (enStates.hasMoreElements()) {                
                tmpState = (TUseStatus) enStates.nextElement();
                if (tmpState.getTimeStamp().before(oldestRequest.getTimeStamp())) oldestRequest = tmpState;
            }            
            TUseStatus newStatus = new TUseStatus(oldestRequest.EXCLUSIVELY_LOCKED, oldestRequest.getUser(), object);
            m_table.setUseStatus(newStatus.getUser(), object, newStatus);            
            m_results.addElement("Granted exclusive lock on " + object.toString() + " to transaction " + oldestRequest.getUser().toString() + ".");            
            return;
        }
        
    }    
    
    /***************************************************************************
    *   Prints the current state of all the transactions objects and associations
    */    
    public void printTTable() {
        System.out.print("\nTRANSACTIONS: ");
        m_table.printUsers();
        System.out.print("\nOBJECTS: ");
        m_table.printResources();
        // now print the associations:
        System.out.print("\nASSOCIATIONS: \n");
        m_table.printByUser();
        System.out.print("\n");
    }
    
    /***************************************************************************
    *   Returns true iff 'waiting' waits for access to at least one object locked/shared by
    *   'locking'. Otherwise it returns false.
    */    
    protected boolean transactionWaitsFor(TIdentifiable waiting, TIdentifiable locking) {
        // first we need to gather all the objects that 'waiting' is waiting for:
        Vector waitingObjects = m_table.getResources(waiting);        
        // now we collect all the objects that 'locking' is locking:
        Vector lockedObjects = m_table.getResources(locking);        
        Enumeration enLockedObjects;
        
        // for each object use in waitingObjects, we need to check if it is locked by 
        // by one of the uses in lockedObjects
        Enumeration enWaitingObjects = waitingObjects.elements();
        while (enWaitingObjects.hasMoreElements()) {
            TUseStatus waitingUse = (TUseStatus) enWaitingObjects.nextElement();
            TUseStatus lockingUse;
            if (waitingUse.getStatus()==waitingUse.WAITING_FOR_EXCLUSIVELY_LOCKED) {
                // 'waiting' is waiting for an exclusive lock on this object
                // we need to check if 'locking' has any locks/shares on it:
                enLockedObjects = lockedObjects.elements();
                while (enLockedObjects.hasMoreElements()) {
                    lockingUse = (TUseStatus) enLockedObjects.nextElement();
                    if (lockingUse.getResource().equals(waitingUse.getResource())) {
                        // it's the same object now we just need to check if it's a lock:
                        if (    (lockingUse.getStatus()==lockingUse.EXCLUSIVELY_LOCKED) ||
                                (lockingUse.getStatus()==lockingUse.SHARED)) return true;
                    }
                }            
            } else if (waitingUse.getStatus()==waitingUse.WAITING_FOR_SHARED) {
                // 'waiting' is waiting for a share-lock on this object
                // we need to check if 'locking' has any exclusive locks on it:
                enLockedObjects = lockedObjects.elements();
                while (enLockedObjects.hasMoreElements()) {
                    lockingUse = (TUseStatus) enLockedObjects.nextElement();
                    if (lockingUse.getResource().equals(waitingUse.getResource())) {
                        // it's the same object now we just need to check if it's a lock:
                        if (lockingUse.getStatus()==lockingUse.EXCLUSIVELY_LOCKED) return true;
                    }
                }                   
            }
        }
        return false;
    }
}

