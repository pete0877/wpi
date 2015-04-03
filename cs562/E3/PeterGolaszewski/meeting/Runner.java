// Class        : Runner
// Author       : Peter Golaszewski (peterg@wpi.edu)
// Version      : 1.0
// Description  : Runs applications through Runnable interface

package meeting;

import java.util.Vector;
import java.util.Enumeration;
import java.lang.Runnable;
import java.lang.Exception;
import java.io.Serializable;

public class Runner extends Thread implements Serializable{
    Vector  m_apps;
    int     m_sleep_time;
    
    public Runner()
    {
        super();
        m_apps = new Vector();        
        m_sleep_time = 1000 * 60 * 5; // 5 minutes
    }    
    
    public void run()
    {        
        do {
            Enumeration e_apps = m_apps.elements();
            while (e_apps.hasMoreElements()) {
                ((Runnable) e_apps.nextElement()).run();
            }
            try {
                wait (m_sleep_time);
            } catch (Exception e) {
                // no action necessary
            }                
        } while (true);            
    }
    
    public void addApp(Runnable app)
    {        
        m_apps.addElement(app);
    }    
    
    public void removeApp(Runnable app)
    {        
        m_apps.removeElement(app);
    }        
    
    public void setSleep(int time)
    {        
        m_sleep_time = time;
    }        

    public int getSleep()
    {        
        return m_sleep_time;
    }   
    
    // returns object serialization
    public Serializable Serialize() {
        Vector result = new Vector();
        result.addElement(new Integer(m_sleep_time));
        return result;
    }        
    
}