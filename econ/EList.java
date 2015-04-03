// File    : EList.java
// Updated : 4/16/98
// Content : EList Class for ECON project
// Author  : Sebastian Jastrzebski
// Detail  : Handles storage and retrieval off all events.
// Code    :

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Date;
import java.util.Vector;
import java.util.NoSuchElementException;
import java.util.MissingResourceException;

//////////////////////////////////////////////////////////////////////////////
// Class EList

public class EList implements Serializable
{
	private Vector events;	
	
	public EList() {
		events = new Vector();
	}
	
	public void addEvent(EEvent e)
	{
		int i = 0;
		EEvent event = null;
		
		for(i = 0; i < events.size(); i++)
		{
			event = (EEvent) events.elementAt(i);
			if (e.getTheDate().before(event.getTheDate()))
				break;
		}
		
		e.setID(this.generateID());
		events.insertElementAt(new EEvent(e), i);
	}


	public void cloneEvent(EEvent e)
	{
		int i = 0;
		EEvent event = null;
		
		for(i = 0; i < events.size(); i++)
		{
			event = (EEvent) events.elementAt(i);
			if (e.getTheDate().before(event.getTheDate()))
				break;
		}
		
		events.insertElementAt(new EEvent(e), i);
	}
	
	public void deleteEvent(EEvent e, String admin)
	{
		int i = findEvent(e.getID());
		EEvent event = (EEvent) events.elementAt(i);
	
		String pwd1 = e.getPassword();
		String pwd2 = event.getPassword();
		
		if ( !(pwd1.equals(pwd2) == false) && 
			 !(pwd1.equals(admin) == false)
			) 
			throw new MissingResourceException("missing pwd",
				"event","password");
		else
			events.removeElementAt(i);
	}
	
	public void modifyEvent(EEvent e, String admin)
	{
		int i = findEvent(e.getID());
		EEvent event = (EEvent) events.elementAt(i);
			
		String pwd1 = e.getPassword();
		String pwd2 = event.getPassword();
		
		if ( !(pwd1.equals(pwd2) == false) && 
			 !(pwd1.equals(admin) == false)
			) 
			throw new MissingResourceException("missing pwd",
				"event","password");
		else
			events.setElementAt(new EEvent(e), i);
	}
	
	public EEvent getEvent(long id)
	{
		int i = findEvent(id);
		EEvent event = (EEvent) events.elementAt(i);
		
		return event;
	}
	
	public Enumeration getEvents(Date date)
	{
		EEvent e = null;
		Vector v = new Vector();
			
		for(int i = 0; i < events.size(); i++)
		{
			e = (EEvent) events.elementAt(i);
			if (
				(e.getTheDate().getDate () == date.getDate()) &&
				(e.getTheDate().getMonth() == date.getMonth()) &&
				(e.getTheDate().getYear() == date.getYear()))
			    { v.addElement(e); }
		}
		
		return v.elements();
	}
		
	public Enumeration getEventsMonth(Date date)
	{
		EEvent e = null;		
		Vector v = new Vector();
			   
		for(int i = 0; i < events.size(); i++)		
		{			
			e = (EEvent) events.elementAt(i);
			if ((e.getTheDate ().getMonth () == date.getMonth ())
			&& (e.getTheDate ().getYear() == date.getYear ()))
				v.addElement(e);
		}
		
		return v.elements();
	}
	
	public Enumeration getFoundEvents(String key, int mode, Date d0, boolean st, boolean fa)
	{
		EEvent e = null;
		Vector v = new Vector();
		key = key.trim();
	
		boolean tmp;
		
		for(int i = 0; i < events.size(); i++)
		{
			e = (EEvent) events.elementAt(i);
	
			tmp = false;
			// Mode:
			// 0 - entire calendar
			// 1 - given year (d0)
			// 2 - given month  (d0)
			switch (mode) {
				case 0:
						tmp = true;
						break;
				case 1:
						if (e.getTheDate().getYear() == d0.getYear()) 
						{ tmp = true; }
						break;
				case 2:
						if (
							(e.getTheDate().getYear() == d0.getYear()) &&
							(e.getTheDate().getMonth() == d0.getMonth())
							) 
						{ tmp = true; }
						break;
			}
			
			if (
				(tmp) &&
				(
				  (st && e.getStudents()) || 
				  (fa && e.getFaculty())
				)&&
				(
				   (e.getAuthor().indexOf(key)!=-1) ||
				   (e.getEventTitle().indexOf(key)!=-1) ||
				   (e.getDescription().indexOf(key)!=-1)
				 ) 
			    )
			    { v.addElement(e); }
		}		
		return v.elements();
	}
	
	private int findEvent(long id)
	{
		EEvent event = null;
		
		for(int i = 0; i < events.size(); i++) {	
			event = (EEvent) events.elementAt(i);
			if(event.getID() == id)
				return i;
		}
		
		throw new NoSuchElementException();
	}
	
	private long m_id = 0;
	private long generateID() {
		return ++m_id;
	}	
	
	public void deleteExpiredEvents(Date d) 
	{
		Date date = null;
		EEvent event = null;
		
		for(int i = 0; i < events.size(); i++) {	
			event = (EEvent) events.elementAt(i);
			date = event.getExpirationDate();
			if(date.after(d))
				events.removeElementAt(i);
		}
	}
}

// End of code ///////////////////////////////////////////////////////////////
