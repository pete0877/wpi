// File    : ECalendar.java
// Updated : 4/10/98
// Content : ECalendar Class for ECON project
// Author  : Sebastian Jastrzebski
// Detail  : Handles storage and retrieval off all Events.
// Code    :

import java.util.Enumeration;
import java.util.Date;

public class ECalendar
{
	private EList Events;		// list of Events in the calendar
	private Date CDate;			// Used to store month and year of the calendar.

	public ECalendar() 
	{	
		Events=new EList ();
		CDate = new Date();		
	}
	

	public void setEvents(EList elist) 
	{
		Events = elist;
	}
	
	public void setCDate(Date d)
	{
		CDate = d;
	}
	
	public Date getCDate() 
	{
		return CDate;
	}
	
	public void addEvent(EEvent e)
	{
		Events.addEvent(e);
	}
	
	public void deleteEvent(EEvent e)
	{
		Events.deleteEvent(e, "");
	}
	
	public void modifyEvent(EEvent e)
	{
		Events.modifyEvent(e, "");		
	}
	
	public EEvent getEvent(long id)
	{
		return Events.getEvent(id);
	}
	
	public Enumeration getEvents(Date date)
	{
		return Events.getEvents(date);
	}
	
	public Enumeration getFoundEvents(String key, int mode, Date d0, boolean st, boolean fa)
	{
		return Events.getFoundEvents(key, mode, d0,st,fa);
	}
	
	public Enumeration getEventsMonth(Date date)
	{
		return Events.getEventsMonth(date);
	}
	
	public void checkEvents() 
	{
		// Used only by the server
	}
	
}

// End of ECalendar.java /////////////////////////////////////////////////////////
