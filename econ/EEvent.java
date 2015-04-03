// File    : EEvent.java
// Updated : 4/16/98
// Content : EEvent Class for ECON project
// Author  : Carl Nielsen
// Detail  : Stores single event information.
// Code    :

import java.io.Serializable;
import java.util.Date;	

//////////////////////////////////////////////////////////////////////////////
// Class EEvent

public class EEvent implements Serializable
{
	private long ID;				// Event ID number
	private String EventTitle;		// One line title of the event
	private boolean Students;		// True if related to students, else
									// false
	private boolean Faculty;		// True if related to faculty, else false
	private Date TheDate;			// Date of the event
	private Date ExpirationDate;	// Date when the event should be erased
									// by the server
	private String Description;		// Multi-line, detailed event description
	private String Author;			// Full name of the Event Creator
	private String Password;		// Event Creator's User Password

	public EEvent(EEvent e)
	{
		ID = e.ID;
		EventTitle = new String(e.EventTitle);
		Students = e.Students;
		Faculty = e.Faculty;
		TheDate = e.TheDate;				// TODO: date need to be cloned
		ExpirationDate = e.ExpirationDate;	// TODO: date need to be cloned
		Description = new String(e.Description);
		Author = new String(e.Author);
		Password = new String(e.Password);
	}
	
	public EEvent()
	{
		ID = 0;
		EventTitle = "";
		Students = true;
		Faculty = true;
		TheDate = new Date();				// TODO: date need to be cloned
		ExpirationDate = new Date();	// TODO: date need to be cloned
		Description = "";
		Author = "";
		Password = "";
	}	
	
	public EEvent(long l1, String s1, boolean b1, boolean b2, Date d1, Date d2, String s2, String s3, String s4)
	{
		ID = l1;
		EventTitle = new String(s1);
		Students = b1;
		Faculty = b2;
		TheDate = d1;				// TODO: date need to be cloned
		ExpirationDate = d2;		// TODO: date need to be cloned
		Description = new String(s2);
		Author = new String(s3);
		Password = new String(s4);
	}

	public void setID(long id)
	{
		ID = id;
	}

	public void setEventTitle(String s)
	{
		EventTitle = s;
	}

	public void setStudents(boolean b)
	{
		Students = b;
	}

	public void setFaculty(boolean b)
	{
		Faculty = b;
	}

	public void setTheDate(Date d)
	{
		TheDate = d;
	}

	public void setExpirationDate(Date d)
	{
		ExpirationDate = d;
	}

	public void setDescription(String s)
	{
		Description = s;
	}

	public void setAuthor(String s)
	{
		Author = s;
	}

	public void setPassword(String s)
	{
		Password = s;
	}

	public long getID()
	{
		return ID;
	}

	public String getEventTitle()
	{
		return EventTitle;
	}

	public boolean getStudents()
	{
		return Students;
	}

	public boolean getFaculty()
	{
		return Faculty;
	}

	public Date getTheDate()
	{
		return TheDate;
	}

	public Date getExpirationDate()
	{
		return ExpirationDate;
	}

	public String getDescription()
	{
		return Description;
	}

	public String getAuthor()
	{
		return Author;
	}

	public String getPassword()
	{
		return Password;
	}
}

// End of code ///////////////////////////////////////////////////////////////
