/* FILE:        TUseStatus.java
 * CLASS:       TUseStatus
 * PACKAGE:     
 * VERSION:     1.0 
 * UPDATED:     4/21/00
 * AUTHOR:      Peter Golaszewski <peterg@wpi.edu>
 */
 
package TransactionManager;

import java.util.Calendar;

public class TUseStatus {
    
    // CONSTANTS
    public final int NO_ASSOCIATION = 0;
    public final int SHARED = 1;
    public final int EXCLUSIVELY_LOCKED = 2;
    public final int WAITING_FOR_SHARED = 3;
    public final int WAITING_FOR_EXCLUSIVELY_LOCKED = 4;
    
    // DATA MEMBERS
    protected int             m_status;
    protected Calendar        m_timeStamp;
    protected TIdentifiable   m_user;
    protected TIdentifiable   m_resource;
    
    // CONSTRUCTORS
    /***************************************************************************
    *   Default constructor
    */              
    public TUseStatus() {
        m_status = NO_ASSOCIATION;
        m_timeStamp = Calendar.getInstance();
        m_user = null;
        m_resource = null;
    }
    
    /***************************************************************************
    *   Constructor that sets the initial status
    */                  
    public TUseStatus(int new_status, TIdentifiable new_user, TIdentifiable new_resource) {
        m_status = new_status;
        m_timeStamp = Calendar.getInstance();
        m_user = new_user;
        m_resource = new_resource;
    }    

    // METHODS
    /***************************************************************************
    *   Returns the use status
    */                      
    public int getStatus() {
        return m_status;
    }       
    
    /***************************************************************************
    *   Sets the use status
    */                          
    public void setStatus(int new_status) {
        m_status = new_status;
    }       
    
    /***************************************************************************
    *   Returns the user
    */                      
    public TIdentifiable getUser() {
        return m_user;
    }      
    
    /***************************************************************************
    *   Sets the user
    */                      
    public void setUser(TIdentifiable new_user) {
        m_user = new_user;
    }       
    
    /***************************************************************************
    *   Returns the resource
    */                      
    public TIdentifiable getResource() {
        return m_resource;
    }      
    
    /***************************************************************************
    *   Sets the resource
    */                      
    public void setResource(TIdentifiable new_resource) {
        m_resource = new_resource;
    }               
    
    /***************************************************************************
    *   Returns the use time stamp
    */       
    public Calendar getTimeStamp() {
        return m_timeStamp;
    }       
    
    /***************************************************************************
    *   Sets the time stamp
    */       
    public void setTimeStamp(Calendar new_timeStamp) {
        m_timeStamp = new_timeStamp;
    }       
    
    /***************************************************************************
    *   Returns the status in form of string
    */       
    public String toString() {
        String result = "";
        // user information
        if (m_user != null) 
            result = result + m_user.toString();
        else 
            result = result + "?";            
        // stuatus information            
        if (m_status == EXCLUSIVELY_LOCKED) result = result + " has exclusive lock on ";
        else if (m_status == SHARED) result = result + " has shared lock on ";
        else if (m_status == WAITING_FOR_EXCLUSIVELY_LOCKED) result = result + " wants exclusive lock on ";
        else if (m_status == WAITING_FOR_SHARED) result = result + " wants shared lock on ";
        else result = result + " ? "; 

        // resource information
        if (m_resource != null) 
            result = result + m_resource.toString();
        else 
            result = result + "?";   
            
        // time stamp information:
        if (m_timeStamp!=null) {
            String strDateTime = "";
            Integer field;
            field = new Integer(m_timeStamp.get(m_timeStamp.MONTH));
            strDateTime = strDateTime + field.toString() + "/";
            field = new Integer(m_timeStamp.get(m_timeStamp.DAY_OF_MONTH));            
            strDateTime = strDateTime + field.toString() + "/";
            field = new Integer(m_timeStamp.get(m_timeStamp.YEAR));            
            strDateTime = strDateTime + field.toString() + " ";
            field = new Integer(m_timeStamp.get(m_timeStamp.HOUR_OF_DAY));            
            strDateTime = strDateTime + field.toString() + ":";
            field = new Integer(m_timeStamp.get(m_timeStamp.MINUTE));                        
            strDateTime = strDateTime + field.toString() + ":";
            field = new Integer(m_timeStamp.get(m_timeStamp.SECOND));            
            strDateTime = strDateTime + field.toString();                        
            result = result + " since " + strDateTime; 
        } else result = result + " since ?";              
        return result;
    }      
    
}

