/* FILE:        TStateTable.java
 * CLASS:       TStateTable
 * PACKAGE:     
 * VERSION:     1.0 
 * UPDATED:     4/21/00
 * AUTHOR:      Peter Golaszewski <peterg@wpi.edu>
 */
 
package TransactionManager;

import java.util.Vector;

public class TStateTable {
    
    // DATA MEMEBERS
    protected TIDIndex        m_users;
    protected TIDIndex        m_resources;
    protected TUseStatus[][]  m_associations;
    protected final int       INITIAL_MAX_USERS_COUNT = 10;
    protected final int       INITIAL_MAX_RESOURCES_COUNT = 10;
    protected int             m_current_max_users;
    protected int             m_current_max_resources;
    protected int             m_current_users_count;
    protected int             m_current_resources_count;

    // CONSTRUCTORS
    /***************************************************************************
    *   Creates the default state table
    */     
	public TStateTable () {
	    m_users     = new TIDIndex();
	    m_resources = new TIDIndex();
	    m_associations = new TUseStatus[INITIAL_MAX_RESOURCES_COUNT][INITIAL_MAX_USERS_COUNT];
	    for (int a=0; a<INITIAL_MAX_RESOURCES_COUNT; a++)
	        for (int b=0; b<INITIAL_MAX_USERS_COUNT; b++)
	            m_associations[a][b] = null;
	    m_current_max_users = INITIAL_MAX_USERS_COUNT;
	    m_current_max_resources = INITIAL_MAX_RESOURCES_COUNT;
	    m_current_users_count = 0;
	    m_current_resources_count = 0;
    }
    
    // METHODS
    /***************************************************************************
    *   Adds another user to the table
    */     
	public void addUser(TIdentifiable user) {
	    // make sure that the user doesn't exists already
	    if (userExists(user)) return;
	    
	    // check to see if we need to expand the array:
	    if (m_current_users_count == m_current_max_users) expandTable(2, 1);	    
	    m_users.addID(user);
	    m_current_users_count++;
    }

    /***************************************************************************
    *   Adds another resource to the table
    */     
	public void addResource(TIdentifiable resource) {
        // make sure that the resource doesn't exists already
	    if (resourceExists(resource)) return;	    
	    
        // check to see if we need to expand the array:
	    if (m_current_resources_count == m_current_max_resources) expandTable(1, 2);	    	    
	    m_resources.addID(resource);
	    m_current_resources_count++;
    }
    
    
    /***************************************************************************
    *   Returns true if the user exists
    */     
	public boolean userExists(TIdentifiable user) {
        return (m_users.getIndex(user) >= 0);
    }    
    
    /***************************************************************************
    *   Returns true if the resource exists
    */     
	public boolean resourceExists(TIdentifiable resource) {
        return (m_resources.getIndex(resource) >= 0);
    }        
    
    
    /***************************************************************************
    *   Removes a user from the table
    */     
	public void removeUser(TIdentifiable user) {
	    int row = m_users.getIndex(user);
	    if (row >= 0) {
	        m_users.removeID(user);
	        m_current_users_count--;
	        // shift the lower rows up:
	        for (int b=row; b < m_current_users_count; b++) 
	            for (int a=0; a < m_current_resources_count; a++)
	                m_associations[a][b] = m_associations[a][b + 1];
            // clean up that last row:	                
            for (int a=0; a < m_current_resources_count; a++)
                m_associations[a][m_current_users_count] = null;
	    }
    }

    /***************************************************************************
    *   Removes a resource from the table
    */     
	public void removeResource(TIdentifiable resource) {
	    int column = m_resources.getIndex(resource);
	    if (column >= 0) {
	        m_resources.removeID(resource);
	        m_current_resources_count--;
	        // shift the right columns left:
	        for (int a=column; a < m_current_resources_count; a++)
	            for (int b=0; b < m_current_users_count; b++) 
	                m_associations[a][b] = m_associations[a + 1][b];
            // clean up that last column:	                
            for (int b=0; b < m_current_users_count; b++)
                m_associations[m_current_resources_count][b] = null;
	    }	    
    }        
    
    /***************************************************************************
    *   Returns all users that have a accociation with the given resource
    *   Returns null if no such resource is to be found.
    *   Otherwise, it returns vector of TUseStatus
    */     
	public Vector getUsers(TIdentifiable resource) {
        int column = m_resources.getIndex(resource);
        Vector result = new Vector();
        TUseStatus useStatus;
	    if (column >= 0) {
            for (int b=0; b < m_current_users_count; b++)
                if (m_associations[column][b]!=null) {
                    useStatus = m_associations[column][b];
                    if (useStatus.getStatus()!=useStatus.NO_ASSOCIATION) result.addElement(useStatus);
                }
            return result;
        } else return null;
    }
    
    
    /***************************************************************************
    *   Returns all resources that have a accociation with the given user
    *   Returns null if no such user is to be found.
    *   Otherwise, it returns vector of TUseStatus
    */     
	public Vector getResources(TIdentifiable user) {
        int row = m_users.getIndex(user);
        Vector result = new Vector();
        TUseStatus useStatus;
	    if (row >= 0) {
            for (int a=0; a < m_current_resources_count; a++)
                if (m_associations[a][row]!=null) {
                    useStatus = m_associations[a][row];
                    if (useStatus.getStatus()!=useStatus.NO_ASSOCIATION) result.addElement(useStatus);
                }
            return result;
        } else return null;
    }  
    

    /***************************************************************************
    *   Sets use status between a user and a resource
    */     
	public void setUseStatus(TIdentifiable user, TIdentifiable resource, TUseStatus status) {
        int row = m_users.getIndex(user);
        int column = m_resources.getIndex(resource);
        if (row >= 0 && column >= 0) m_associations[column][row] = status;
    } 
    
    /***************************************************************************
    *   Gets the use status between a user and a resource
    */     
	public TUseStatus getUseStatus(TIdentifiable user, TIdentifiable resource) {
        int row = m_users.getIndex(user);
        int column = m_resources.getIndex(resource);
        if (row >= 0 && column >= 0) 
            return m_associations[column][row];
        else 
            return null;
    }     
    
    
    /***************************************************************************
    *   Prints the current state table and sorts entries by users
    */     
    public void printByUser() {
        TUseStatus useStatus;
	    for (int a=0; a < m_current_resources_count; a++)
	        for (int b=0; b < m_current_users_count; b++) {
	            useStatus = m_associations[a][b];
	            if (useStatus!=null) System.out.println(useStatus.toString()); 
	        }                                                                              
    }
    
    
    /***************************************************************************
    *   Prints the current state table and sorts entries by resources
    */         
    public void printByResource() {
        TUseStatus useStatus;
	    for (int b=0; b < m_current_users_count; b++)
	        for (int a=0; a < m_current_resources_count; a++) {
	            useStatus = m_associations[a][b];
	            if (useStatus!=null) System.out.println(useStatus.toString()); 
	        }                                            
    }    
    
    /***************************************************************************
    *   Prints all users
    */     
    public void printUsers() {
        System.out.println(m_users.toString());         
    }    
    

    /***************************************************************************
    *   Prints all resources
    */     
    public void printResources() {
        System.out.println(m_resources.toString());         
    }    
    
    
    /***************************************************************************
    *   Expands the table so that it can hold userFactor-as-many users compared 
    *   to its current users size and resourcesFactor-as-many resources comapred
    *   to its current resources
    */     
	protected void expandTable(int userFactor, int resourcesFactor) {
	    // just in case, check to make sure that the factor parameters are
	    // greater then zero:
	    if ((userFactor <= 0) || (resourcesFactor<=0)) return;
	    
	    // create the new array:
	    int new_max_users = m_current_max_users * userFactor;
	    int new_max_resources = m_current_max_resources * resourcesFactor;
	    TUseStatus[][] newArray = new TUseStatus[new_max_resources][new_max_users];
	    
	    // empty the new array:
	    int a, b;
	    for (a=0; a < new_max_resources; a++)
	        for (b=0; b < new_max_users; b++)
	            newArray[a][b] = null;
	    
	    // copy existing data over from the old array:
	    for (a=0; a<m_current_max_resources; a++)
	        for (b=0; b<m_current_max_users; b++)
	            newArray[a][b] = m_associations[a][b];

        // set the current array to the new array:
        m_associations = newArray;
        m_current_max_resources = new_max_resources;
        m_current_max_users = new_max_users;
    }       
}

