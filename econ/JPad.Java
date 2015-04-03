// File    : EServerProfile.java
// Updated : 4/16/98
// Content : EServerProfile Class for ECON project
// Author  : Carl Nielsen
// Detail  : Stores information about the server.
// Code    :

// Imported classes:
import wfc.app.*;
import wfc.core.*;
import wfc.ui.*;
import java.util.Date;					 
import java.net.*;
import java.io.*;	
import java.lang.Integer;
import java.util.Vector;
import java.util.Enumeration;

// Class definition
public class JPad extends Form
{	
	// Data members:
	private void JPad_click(Object sender, Event e)
	{

	}

	private ECalendar Calendar = new ECalendar();
	private EServerList Servers = new EServerList();
	private EConstants Constants = new EConstants();
	private DateTools DTools = new DateTools();
	private int activeBox;		
	
///////////////////////////////////////////////////////////////////////////

	public JPad()
	{
			int year;
			
			// Initialize active day box variable
			activeBox=0;
			
			// Open the form:
			initForm();		
			
			// Set the current date in the calendar and the screen
			Date d0 = new Date();					
			this.Calendar.setCDate (d0);
			
			month_fm.setSelectedIndex(d0.getMonth());
			year = d0.getYear();
			if (year >=100) { year = year - 100; }
			if (year >9) 
			{ 
				year_fm.setText(""+ year); 
				this.cmonth.setText ("" + DTools.getMonthName (d0.getMonth()) + " '" + year);
			} 
			else
			{ 
				year_fm.setText("0"+ year); 
				this.cmonth.setText ("" + DTools.getMonthName(d0.getMonth()) + " '0" + year);
			}
			
			// Load Servers
			Servers.load(Constants.getSERVERS());
			
			if (Servers.getSize ()>0)
			{				
				Servers.setChanged(false);				
				serv.setText (Servers.getServer (Servers.getCurrentServerIndex ()).getDescription ());				
			}
			
			// get the events for current calendar
			updateCalendar ();					
	}
	
///////////////////////////////////////////////////////////////////////////
		
	private void clearBGBoxes()
	{
		  this.activeBox  = 0;
		  
		  // set background colors of all the day boxes:
		  e1.setBackColor(new Color(255, 255, 255)); 
		  e2.setBackColor(new Color(255, 255, 255));
		  e3.setBackColor(new Color(255, 255, 255));
		  e4.setBackColor(new Color(255, 255, 255));
		  e5.setBackColor(new Color(255, 255, 255));
		  e6.setBackColor(new Color(255, 255, 192));
		  e7.setBackColor(new Color(255, 255, 192));

		  e8.setBackColor(new Color(255, 255, 255)); 
		  e9.setBackColor(new Color(255, 255, 255));
		  e10.setBackColor(new Color(255, 255, 255));
		  e11.setBackColor(new Color(255, 255, 255));
		  e12.setBackColor(new Color(255, 255, 255));
		  e13.setBackColor(new Color(255, 255, 192));
		  e14.setBackColor(new Color(255, 255, 192));

 		  e15.setBackColor(new Color(255, 255, 255)); 
		  e16.setBackColor(new Color(255, 255, 255));
		  e17.setBackColor(new Color(255, 255, 255));
		  e18.setBackColor(new Color(255, 255, 255));
		  e19.setBackColor(new Color(255, 255, 255));
		  e20.setBackColor(new Color(255, 255, 192));
		  e21.setBackColor(new Color(255, 255, 192));

		  e22.setBackColor(new Color(255, 255, 255)); 
		  e23.setBackColor(new Color(255, 255, 255));
		  e24.setBackColor(new Color(255, 255, 255));
		  e25.setBackColor(new Color(255, 255, 255));
		  e26.setBackColor(new Color(255, 255, 255));
		  e27.setBackColor(new Color(255, 255, 192));
		  e28.setBackColor(new Color(255, 255, 192));


		  e29.setBackColor(new Color(255, 255, 255)); 
		  e30.setBackColor(new Color(255, 255, 255));
		  e31.setBackColor(new Color(255, 255, 255));
		  e32.setBackColor(new Color(255, 255, 255));
		  e33.setBackColor(new Color(255, 255, 255));
		  e34.setBackColor(new Color(255, 255, 192));
		  e35.setBackColor(new Color(255, 255, 192));

		  e36.setBackColor(new Color(255, 255, 255)); 
		  e37.setBackColor(new Color(255, 255, 255));
	}
	
///////////////////////////////////////////////////////////////////////////
	
	private void setAsActiveBox(Object sender, Event e)
	{		  
		  // get the sender box:
		  Edit box = (Edit) sender;		  
		  
		  // Clear previous active box
		  clearBGBoxes();
		  
		  // get the new active day of the month
		  // 0 if the box is empty
		  if (box.getText ().trim().equals(""))
		  {		  
			this.activeBox = 0;
		  } 
		  else
		  {
			try {
				this.activeBox = Integer.parseInt(box.getText ().trim());
				Date d01 = Calendar.getCDate();
				d01.setDate (activeBox);				
				
				Calendar.setCDate(d01);						
			} catch (NumberFormatException expt)
			{
				this.activeBox = 0;
			}						  
		  }
		  
		  // Sets background active background color on the new active box.
		  box.setBackColor(new Color(150, 150, 255)); 		  
		  
	}

///////////////////////////////////////////////////////////////////////////
	
	private void filterEvents()
	{		
		// clear reds:
		this.unmarkDates();
		
		// See what kind of events does the user want to see.
		boolean st = this.students_fm.getChecked ();
		boolean fa = this.faculty_fm.getChecked ();
		
		// What month is currently shown ? 
		Date d0 = Calendar.getCDate();		
		
		EEvent ev;
		
		// Filter the events for this one month
		Enumeration en;		
		en = Calendar.getEventsMonth (d0);			
		
		// Mark all matching dates
		while (en.hasMoreElements ())
		{
			ev = (EEvent) en.nextElement ();
			if ((st && ev.getStudents () ) || (fa && ev.getFaculty ()))
			{
				 markDate(ev.getTheDate());			
			}
		}		
	}
	
///////////////////////////////////////////////////////////////////////////
	
		private Date getSelectedMonth()
		{			
			// return the month selected for update
			Date d0 = new Date();
			d0.setMonth(this.month_fm.getSelectedIndex ());
			int year;
			
			try {
				year = Integer.parseInt(this.year_fm.getText ().trim());				
				if (year<90) { year = year + 100; }
			} catch (NumberFormatException expt)
			{
				EError newError1 = new EError (7, "Invalid Date");
				ErrorForm newForm1 = new ErrorForm(newError1);
				newForm1.showDialog ();				
				Date d00 = new Date ();
				year = (d00.getYear());
			}	
			d0.setYear (year);
			return d0;
		}

///////////////////////////////////////////////////////////////////////////
		
		private Date getSelectedDate()
		{					
			// Check if selection was made
			if (activeBox==0) 
			{ 
				return null;
			}
			
			// Ok, things are fine
			Date d1 = Calendar.getCDate();
			d1.setDate (activeBox);			
			return d1;		
	}
		
///////////////////////////////////////////////////////////////////////////
	
	private void markDate(Date d0)
	{		
		int day = d0.getDate();
		int start=0;		
		
		Date tmp = new Date(d0.getYear (), d0.getMonth (),1);
		
		// Dind out what week day does the current month start at
		if (tmp.getDay()	== 0) { start=6; }
		if (tmp.getDay()	== 1) { start=0; }
		if (tmp.getDay()	== 2) { start=1; }
		if (tmp.getDay()	== 3) { start=2; }
		if (tmp.getDay()	== 4) { start=3; }
		if (tmp.getDay()	== 5) { start=4; }
		if (tmp.getDay()	== 6) { start=5; }	
		
		// Calculate start of the month with respect to the day boxes
		int box = day + start;
		
		// Mark the box - there is an event on that day
		if (box==1) { e1.setForeColor(Color.RED); } else
		if (box==2) { e2.setForeColor(Color.RED); } else
		if (box==3) { e3.setForeColor(Color.RED); } else
		if (box==4) { e4.setForeColor(Color.RED); } else
		if (box==5) { e5.setForeColor(Color.RED); } else
		if (box==6) { e6.setForeColor(Color.RED); } else
		if (box==7) { e7.setForeColor(Color.RED); } else
		
		if (box==8) { e8.setForeColor(Color.RED); } else
		if (box==9) { e9.setForeColor(Color.RED); } else
		if (box==10) { e10.setForeColor(Color.RED); } else
		if (box==11) { e11.setForeColor(Color.RED); } else
		if (box==12) { e12.setForeColor(Color.RED); } else
		if (box==13) { e13.setForeColor(Color.RED); } else
		if (box==14) { e14.setForeColor(Color.RED); } else		
		
		if (box==15) { e15.setForeColor(Color.RED); } else
		if (box==16) { e16.setForeColor(Color.RED); } else
		if (box==17) { e17.setForeColor(Color.RED); } else
		if (box==18) { e18.setForeColor(Color.RED); } else
		if (box==19) { e19.setForeColor(Color.RED); } else
		if (box==20) { e20.setForeColor(Color.RED); } else
		if (box==21) { e21.setForeColor(Color.RED); } else
			
		if (box==22) { e22.setForeColor(Color.RED); } else
		if (box==23) { e23.setForeColor(Color.RED); } else
		if (box==24) { e24.setForeColor(Color.RED); } else
		if (box==25) { e25.setForeColor(Color.RED); } else
		if (box==26) { e26.setForeColor(Color.RED); } else
		if (box==27) { e27.setForeColor(Color.RED); } else
		if (box==28) { e28.setForeColor(Color.RED); } else
			
		if (box==29) { e29.setForeColor(Color.RED); } else
		if (box==30) { e30.setForeColor(Color.RED); } else
		if (box==31) { e31.setForeColor(Color.RED); } else
		if (box==32) { e32.setForeColor(Color.RED); } else
		if (box==33) { e33.setForeColor(Color.RED); } else
		if (box==34) { e34.setForeColor(Color.RED); } else
		if (box==35) { e35.setForeColor(Color.RED); } else
		if (box==36) { e36.setForeColor(Color.RED); } else
		if (box==37) { e37.setForeColor(Color.RED); }				
	}

///////////////////////////////////////////////////////////////////////////
	
	private void unmarkDates()
	{
		// Set font color to black on all boxes:
		e1.setForeColor(Color.BLACK);	
		e2.setForeColor(Color.BLACK);
		e3.setForeColor(Color.BLACK);
		e4.setForeColor(Color.BLACK);
		e5.setForeColor(Color.BLACK);
		e6.setForeColor(Color.BLACK);
		e7.setForeColor(Color.BLACK);
		e8.setForeColor(Color.BLACK);
		e9.setForeColor(Color.BLACK);
		e10.setForeColor(Color.BLACK);
		e11.setForeColor(Color.BLACK);
		e12.setForeColor(Color.BLACK);
		e13.setForeColor(Color.BLACK);
		e14.setForeColor(Color.BLACK);
		e15.setForeColor(Color.BLACK);
		e16.setForeColor(Color.BLACK);
		e17.setForeColor(Color.BLACK);
		e18.setForeColor(Color.BLACK);	
		e19.setForeColor(Color.BLACK);
		e20.setForeColor(Color.BLACK);
		e21.setForeColor(Color.BLACK);
		e22.setForeColor(Color.BLACK);
		e23.setForeColor(Color.BLACK);
		e24.setForeColor(Color.BLACK);
		e25.setForeColor(Color.BLACK);
		e26.setForeColor(Color.BLACK);
		e27.setForeColor(Color.BLACK);
		e28.setForeColor(Color.BLACK);
		e29.setForeColor(Color.BLACK);
		e30.setForeColor(Color.BLACK);
		e31.setForeColor(Color.BLACK);
		e32.setForeColor(Color.BLACK);
		e33.setForeColor(Color.BLACK);
		e34.setForeColor(Color.BLACK);
		e35.setForeColor(Color.BLACK);
		e36.setForeColor(Color.BLACK);
		e37.setForeColor(Color.BLACK);						
	}

///////////////////////////////////////////////////////////////////////////
	
	private void clickRefresh(Object sender, Event e)
	{
		this.month_fm.setSelectedIndex (Calendar.getCDate ().getMonth ());
		int year = Calendar.getCDate ().getYear ();
		if ( year > 99 ) { year = year - 100; }
		if (year>9)
		{ this.year_fm .setText ("" + year); }
		else
		{ this.year_fm .setText ("0" + year); }
		updateCalendar();
	}

///////////////////////////////////////////////////////////////////////////

	private void updateCalendar()
	{	
		Date d00 = Calendar.getCDate();		
		
		// Clear the calendar:
		Calendar = new ECalendar();
		Calendar.setCDate (d00);			
		
		// Clear fully all the boxes
		this.clearBoxes ();
		
		// Fill boxes with numbers
		this.fillBoxes(d00);					
		
		// Set no current box
		activeBox = 0;		  		  		  			
		
		// Request the new Calendar from the current server:		
		EServerProfile s0 = new EServerProfile ();
		
		// See if there are any server registered:
		if (Servers.getSize() != 0)
		{
			// Inform the user about the conneciton 
			this.sta.setText (" Status: Requesting calendar from the ECON server ..."); 
			
			// get the current server from the list
			s0 = Servers.getServer (Servers.getCurrentServerIndex());			
			
			// Initialize socket for the server connection
			Socket socket = null;
			EError error1 = new EError(0,"");
			
			// Define IO streams
			ObjectInputStream is = null;
			ObjectOutputStream os= null;
			boolean success = false;
	
			try {
				
				// Get internet name of the server
				String DNS = Servers.getServer (Servers.getCurrentServerIndex ()).getDNSname ();
				
				// Open connection
				socket = new Socket(DNS, this.Constants.getPORT ());
			
				// Get IO Ids
				os = new ObjectOutputStream(socket.getOutputStream());
				is = new ObjectInputStream(socket.getInputStream());				
				
				// Send Calendar request
				Integer cmd1 = new Integer(0);
				os.writeObject(cmd1);									
				os.flush();
				
				// Get the error object
				error1 = (EError) is.readObject();					
						
				// Check if the command was succesful
				if (error1.getEErrorNumber ()!= 0) 
				{	
					// Error found .. display the error
					ErrorForm form7 = new ErrorForm(error1);
					form7.showDialog ();						
				} 
					else
				{
					// download the calendar:					
					EList enum = (EList) is.readObject();					
					this.Calendar.setEvents (enum);
					this.Calendar.setCDate (d00);
				}	
				
				// Close connection with the server
				socket.close();
				
				// Ok. Now just filter the events for students / faculty
				this.filterEvents();
			
				// Set the staus back to normal
				this.sta.setText (" Status: Ready");			
				
			}
			catch (Exception netexep)
			{
					// Network problems exception
					EError newError1 = new EError(1,"Unable to Establish a Network Connection");
					ErrorForm newForm8 = new ErrorForm (newError1);
					newForm8.showDialog();	
					this.sta.setText (" Status: Network connection failed."); 
			}						
   	     }	
		 else
		 {
			 this.sta.setText (" Status: Use ECON Servers button to add servers");
		 }
	
	}		
	
///////////////////////////////////////////////////////////////////////////	

	private void fillBoxes(Date d1)
	{		
		int count=0;
		int days=0;
		
		Date d2 = new Date();
		
		d2.setYear (d1.getYear());
		d2.setMonth (d1.getMonth ());
		d2.setDate (1);
		int start = d2.getDay (); 
		// 0 - Sun
		// 1 - Mon, ..		
		
		if (start==0) { count=7; }
		if (start==1) { count=1; }
		if (start==2) { count=2; }
		if (start==3) { count=3; }
		if (start==4) { count=4; }
		if (start==5) { count=5; }
		if (start==6) { count=6; }		
		
		// Find out the week day offset
		int month = d2.getMonth  ();
		
		// Find out number of day in the month:
		if (month==0) { days = 31; }
		if (month==1) 
		{ 
				days = 28; 
				if (d1.getYear() % 4 == 0) { days = 29; }
		}
		if (month==2) { days = 31; }
		if (month==3) { days = 30; }
		if (month==4) { days = 31; }
		if (month==5) { days = 30; }
		if (month==6) { days = 31; }
		if (month==7) { days = 31; }
		if (month==8) { days = 30; }
		if (month==9) { days = 31; }
		if (month==10) { days = 30; }
		if (month==11) { days = 31; }
		
		// Fill the numbers
		String dn = new String ();
		int a=0;
		while (a<days) 
		{			
			a++;
			dn = "" + a;
			setBoxDay(count,dn);			
			count ++;
		}
		
	}	
	
///////////////////////////////////////////////////////////////////////////
	
	private void setBoxDay(int n, String d)
	{
		// Put in text into one of the day boxes:
		if (n==1) { e1.setText("" + d);  } else
		if (n==2) { e2.setText("" + d);  } else
		if (n==3) { e3.setText("" + d);  } else
		if (n==4) { e4.setText("" + d);  } else
		if (n==5) { e5.setText("" + d);  } else
		if (n==6) { e6.setText("" + d);  } else
		if (n==7) { e7.setText("" + d);  } else
		
		if (n==8) { e8.setText("" + d);  } else
		if (n==9) { e9.setText("" + d);  } else
		if (n==10) { e10.setText("" + d);  } else
		if (n==11) { e11.setText("" + d);  } else
		if (n==12) { e12.setText("" + d);  } else
		if (n==13) { e13.setText("" + d);  } else
		if (n==14) { e14.setText("" + d);  } else		
		
		if (n==15) { e15.setText("" + d);  } else
		if (n==16) { e16.setText("" + d);  } else
		if (n==17) { e17.setText("" + d);  } else
		if (n==18) { e18.setText("" + d);  } else
		if (n==19) { e19.setText("" + d);  } else
		if (n==20) { e20.setText("" + d);  } else
		if (n==21) { e21.setText("" + d);  } else
			
		if (n==22) { e22.setText("" + d);  } else
		if (n==23) { e23.setText("" + d);  } else
		if (n==24) { e24.setText("" + d);  } else
		if (n==25) { e25.setText("" + d);  } else
		if (n==26) { e26.setText("" + d);  } else
		if (n==27) { e27.setText("" + d);  } else
		if (n==28) { e28.setText("" + d);  } else
			
		if (n==29) { e29.setText("" + d);  } else
		if (n==30) { e30.setText("" + d);  } else
		if (n==31) { e31.setText("" + d);  } else
		if (n==32) { e32.setText("" + d);  } else
		if (n==33) { e33.setText("" + d);  } else
		if (n==34) { e34.setText("" + d);  } else
		if (n==35) { e35.setText("" + d);  } else
		if (n==36) { e36.setText("" + d);  } else
		if (n==37) { e37.setText("" + d);  } 
	}

///////////////////////////////////////////////////////////////////////////
	
	private void clearBoxes()  // text, bgcolor, font color
	{		
	
		  clearBGBoxes();		  
		
		// Set text in all day boxes to nothing
		e1.setText("");
		e2.setText("");
		e3.setText("");
		e4.setText("");
		e5.setText("");
		e6.setText("");
		e7.setText("");
		e8.setText("");
		e9.setText("");
		e10.setText("");
		e11.setText("");
		e12.setText("");
		e13.setText("");
		e14.setText("");
		e15.setText("");
		e16.setText("");
		e17.setText("");
		e18.setText("");
		e19.setText("");
		e20.setText("");
		e21.setText("");
		e22.setText("");
		e23.setText("");
		e24.setText("");
		e25.setText("");
		e26.setText("");
		e27.setText("");
		e28.setText("");
		e29.setText("");
		e30.setText("");
		e31.setText("");
		e32.setText("");
		e33.setText("");
		e34.setText("");
		e35.setText("");
		e36.setText("");
		e37.setText("");
		
		// erase font colors	
		this.unmarkDates();
	}	

///////////////////////////////////////////////////////////////////////////
	
	private void students_fm_click(Object sender, Event e)
	{
		faculty_fm.setChecked (true);	// make sure at least one is checked
		filterEvents();
	}
	
///////////////////////////////////////////////////////////////////////////

	private void faculty_fm_click(Object sender, Event e)
	{
		students_fm.setChecked (true); // make sure at least one is checked
		filterEvents();
	}
	
///////////////////////////////////////////////////////////////////////////

	private void menuItem6_click(Object sender, Event e)
	{
		// Show credits
		AboutForm form1 = new AboutForm();
		form1.showDialog ();
	}

///////////////////////////////////////////////////////////////////////////

	private void menuItem5_click(Object sender, Event e)
	{
		// Show Help		
		Runtime rt = Runtime.getRuntime();
		try {
			rt.exec("help\\Econ.hlp");
		}
		catch (IOException ex) { }
	}

///////////////////////////////////////////////////////////////////////////
	
	private void button6_click(Object sender, Event e)
	{
		// Quit application
		System.exit(0);
	}
	
///////////////////////////////////////////////////////////////////////////

	private void menuItem1_click(Object sender, Event e)
	{
		// Quit application
		System.exit(0);
	}
	
///////////////////////////////////////////////////////////////////////////
	
	private void button5_click(Object sender, Event e)
	{
		if (Servers.getSize()>0) 
		{
			String servname = "" + Servers.getServer (Servers.getCurrentServerIndex ()).getDNSname ();
			
			EEvent newEvent1 = new EEvent ();
			NewEventForm newForm1 = new NewEventForm (newEvent1, this.getSelectedDate ());
			newForm1.showDialog ();		
		
			// check if Form was canceled
			if (newEvent1.getID () != -1)
			{	
				this.sta.setText (" Status: Sending new event to the ECON Server"); 
				Socket socket = null;
				EError error1 = new EError(0,"");
				ObjectInputStream is = null;
				ObjectOutputStream os= null;
		
				try {
					socket = new Socket(servname, Constants.getPORT());
					os = new ObjectOutputStream(socket.getOutputStream());
					is = new ObjectInputStream(socket.getInputStream());
					Integer cmd1 = new Integer(1);
					os.writeObject(cmd1);
					os.writeObject(newEvent1);
					os.flush();
					error1 = (EError) is.readObject();
					socket.close();
					this.sta.setText (" Status: Ready"); 
					if (error1.getEErrorNumber ()!= 0) 
					{
						ErrorForm form7 = new ErrorForm(error1);
						form7.showDialog ();						
					} else
					{	
						this.Calendar.addEvent(newEvent1);
						this.filterEvents();
					}		
				}
				catch (Exception netexep)
				{
					EError newError1 = new EError(1,"Unable to Establish a Network Connection");
					ErrorForm newForm8 = new ErrorForm (newError1);
					newForm8.showDialog();					
					this.sta.setText (" Status: Network connection failed."); 
				}				
			}
		} else {
			EError newError1 = new EError (8, "No Servers Registered");
			ErrorForm newForm1 = new ErrorForm(newError1);
			newForm1.showDialog ();
		}
	}

///////////////////////////////////////////////////////////////////////////
	
	private void button3_click(Object sender, Event e)
	{
		Date day;		
		if (this.activeBox == 0) 
		{ 
			day = null;
		} else
		{
			day = Calendar.getCDate();
		}
		if (day == null)
		{
			Message msg1 = new Message("First select day by clicking on one of the dates.");			
			msg1.showDialog ();			
		} 
		 else
		{			 
			EventsForm newForm1 = new EventsForm(Calendar, day, this.students_fm.getChecked (), this.faculty_fm.getChecked (), Servers, Constants.getPORT ());
			newForm1.showDialog();		
			this.filterEvents();
		}
	}

///////////////////////////////////////////////////////////////////////////

	private void button4_click(Object sender, Event e)
	{
		ServersForm newForm1 = new ServersForm(Servers, this.Constants.getPORT ());
		newForm1.showDialog();			
		if (Servers.wasChanged () && (Servers.getSize ()>0))
		{
			Servers.save(Constants.getSERVERS());
			Servers.setChanged(false);
			
			serv.setText (Servers.getServer (Servers.getCurrentServerIndex ()).getDescription ());
			updateCalendar();
		}

	}
	
///////////////////////////////////////////////////////////////////////////

	private void button2_click(Object sender, Event e)
	{
			// Set the current date in the calendar and the screen
			Date d0 = getSelectedMonth();
			this.Calendar.setCDate (d0);
			
			month_fm.setSelectedIndex(d0.getMonth());
			int year = d0.getYear();
			if (year >=100) { year = year - 100; }
			if (year >9) 
			{ 
				year_fm.setText(""+ year); 
				this.cmonth.setText ("" + DTools.getMonthName (d0.getMonth()) + " '" + year);
			} 
			else
			{ 
				year_fm.setText("0"+ year); 
				this.cmonth.setText ("" + DTools.getMonthName(d0.getMonth()) + " '0" + year);
			}
			
			// Clear fully all the boxes
			this.clearBoxes ();
		
			// Fill boxes with numbers
			this.fillBoxes(d0);					
		
			// Set no current box
			activeBox = 0;				
			
			this.filterEvents();	
	}
	
///////////////////////////////////////////////////////////////////////////

	private void button1_click(Object sender, Event e)
	{
		EServerProfile s1;
		
		if (Servers.getSize() != 0)
		{
			s1 = Servers.getServer (Servers.getCurrentServerIndex());
			SSIForm newForm1 = new SSIForm (s1);
			newForm1.showDialog();			
		} else {
			EError newError1 = new EError (8, "No Servers Registered");
			ErrorForm newForm1 = new ErrorForm(newError1);
			newForm1.showDialog ();
		}
	}
	
///////////////////////////////////////////////////////////////////////////
	
	private void button55_click(Object sender, Event e)
	{
		// call Find event form
		FindForm newForm1 = new FindForm (Calendar);
		newForm1.showDialog();	
	}

///////////////////////////////////////////////////////////////////////////
		
	private void callQuickEventView(Object sender, Event e)
	{
		// Dbl-click on a day box
		this.setAsActiveBox (sender,e);
		Date day;
		if (this.activeBox == 0) 
		{ 
			day = null;
		} else
		{
			day = Calendar.getCDate();
		}
		if (day == null)
		{
			Message msg1 = new Message("First select day by clicking on one of the dates.");			
			msg1.showDialog ();			
		} 
		 else
		{
			EventsForm newForm1 = new EventsForm(Calendar, day, this.students_fm.getChecked (), this.faculty_fm.getChecked (), Servers, Constants.getPORT ());
			newForm1.showDialog();		
		}
	}

///////////////////////////////////////////////////////////////////////////

	public static void main(String args[])
	{
		Application.run(new JPad());
	}

///////////////////////////////////////////////////////////////////////////
	
	Container components = new Container();
	MainMenu mainMenu1 = new MainMenu();
	MenuItem fileMenu = new MenuItem();
	MenuItem editMenu = new MenuItem();
	MenuItem helpMenu = new MenuItem();
	Edit serv = new Edit();
	Button button4 = new Button();
	Button button2 = new Button();
	Button button5 = new Button();
	Button button3 = new Button();
	Button button1 = new Button();
	Edit year_fm = new Edit();
	Label label8 = new Label();
	Label label7 = new Label();
	Label label6 = new Label();
	Label label5 = new Label();
	Label label4 = new Label();
	Label label3 = new Label();
	Label label2 = new Label();
	Edit e35 = new Edit();
	Edit e28 = new Edit();
	Edit e34 = new Edit();
	Edit e21 = new Edit();
	Edit e32 = new Edit();
	Edit e33 = new Edit();
	Edit e27 = new Edit();
	Edit e14 = new Edit();
	Edit e30 = new Edit();
	Edit e31 = new Edit();
	Edit e25 = new Edit();
	Edit e26 = new Edit();
	Edit e29 = new Edit();
	Edit e20 = new Edit();
	Edit e7 = new Edit();
	Edit e23 = new Edit();
	Edit e24 = new Edit();
	Edit e18 = new Edit();
	Edit e19 = new Edit();
	Edit e22 = new Edit();
	Edit e13 = new Edit();
	Edit e6 = new Edit();
	Edit e16 = new Edit();
	Edit e17 = new Edit();
	Edit e11 = new Edit();
	Edit e12 = new Edit();
	Edit e15 = new Edit();
	Edit e4 = new Edit();
	Edit e5 = new Edit();
	Edit e9 = new Edit();
	Edit e10 = new Edit();
	Edit e2 = new Edit();
	Edit e3 = new Edit();
	Edit e8 = new Edit();
	Edit e1 = new Edit();
	Label label9 = new Label();
	Checkbox faculty_fm = new Checkbox();
	Checkbox students_fm = new Checkbox();
	ComboBox month_fm = new ComboBox();
	Button button6 = new Button();
	Edit e37 = new Edit();
	Edit e36 = new Edit();
	MenuItem menuItem1 = new MenuItem();
	MenuItem menuItem2 = new MenuItem();
	MenuItem menuItem3 = new MenuItem();
	MenuItem menuItem4 = new MenuItem();
	MenuItem menuItem5 = new MenuItem();
	MenuItem menuItem6 = new MenuItem();
	Label sta = new Label();
	Button button7 = new Button();
	Label cmonth = new Label();
	Button button8 = new Button();

	private void initForm()
	{
		ResourceManager resources = new ResourceManager(this, "JPad");
		serv.setBackColor(Color.WHITE);
		serv.setCursor(Cursor.IBEAM);
		serv.setLocation(new Point(5, 0));
		serv.setSize(new Point(280, 23));
		serv.setTabIndex(0);
		serv.setTabStop(false);
		serv.setText("");
		serv.setReadOnly(true);
		serv.setTextAlign(HorizontalAlignment.CENTER);
		serv.setWordWrap(false);
		button4.setLocation(new Point(290, 170));
		button4.setSize(new Point(120, 25));
		button4.setTabIndex(5);
		button4.setTabStop(true);
		button4.setText("ECON Servers");
		button4.addOnClick(new EventHandler(this.button4_click));
		button2.setLocation(new Point(5, 30));
		button2.setSize(new Point(145, 25));
		button2.setTabIndex(18);
		button2.setTabStop(true);
		button2.setText("Show Calendar for:");
		button2.addOnClick(new EventHandler(this.button2_click));
		button5.setLocation(new Point(290, 110));
		button5.setSize(new Point(120, 25));
		button5.setTabIndex(8);
		button5.setTabStop(true);
		button5.setText("Add Event");
		button5.addOnClick(new EventHandler(this.button5_click));
		button3.setLocation(new Point(290, 80));
		button3.setSize(new Point(120, 25));
		button3.setTabIndex(3);
		button3.setTabStop(true);
		button3.setText("View Events");
		button3.addOnClick(new EventHandler(this.button3_click));
		button1.setLocation(new Point(290, 0));
		button1.setSize(new Point(120, 25));
		button1.setTabIndex(1);
		button1.setTabStop(true);
		button1.setText("Server Info");
		button1.addOnClick(new EventHandler(this.button1_click));
		year_fm.setBackColor(Color.WHITE);
		year_fm.setCursor(Cursor.IBEAM);
		year_fm.setLocation(new Point(255, 30));
		year_fm.setSize(new Point(30, 23));
		year_fm.setTabIndex(17);
		year_fm.setTabStop(true);
		year_fm.setText("");
		year_fm.setMaxLength(2);
		label8.setLocation(new Point(245, 60));
		label8.setSize(new Point(40, 20));
		label8.setText("Sun.");
		label8.setTabIndex(50);
		label8.setAlignment(HorizontalAlignment.CENTER);
		label7.setLocation(new Point(205, 60));
		label7.setSize(new Point(40, 20));
		label7.setText("Sat.");
		label7.setTabIndex(51);
		label7.setAlignment(HorizontalAlignment.CENTER);
		label6.setLocation(new Point(165, 60));
		label6.setSize(new Point(40, 20));
		label6.setText("Fri.");
		label6.setTabIndex(52);
		label6.setAlignment(HorizontalAlignment.CENTER);
		label5.setLocation(new Point(125, 60));
		label5.setSize(new Point(40, 20));
		label5.setText("Thu.");
		label5.setTabIndex(53);
		label5.setAlignment(HorizontalAlignment.CENTER);
		label4.setLocation(new Point(85, 60));
		label4.setSize(new Point(40, 20));
		label4.setText("Wed.");
		label4.setTabIndex(54);
		label4.setAlignment(HorizontalAlignment.CENTER);
		label3.setLocation(new Point(45, 60));
		label3.setSize(new Point(40, 20));
		label3.setText("Tue.");
		label3.setTabIndex(55);
		label3.setAlignment(HorizontalAlignment.CENTER);
		label2.setLocation(new Point(5, 60));
		label2.setSize(new Point(40, 20));
		label2.setText("Mon.");
		label2.setTabIndex(56);
		label2.setAlignment(HorizontalAlignment.CENTER);
		e35.setBackColor(new Color(255, 255, 192));
		e35.setCursor(Cursor.IBEAM);
		e35.setFont(new Font("MS Sans Serif", -17));
		e35.setForeColor(Color.BLACK);
		e35.setLocation(new Point(245, 180));
		e35.setSize(new Point(40, 25));
		e35.setTabIndex(47);
		e35.setTabStop(false);
		e35.setText("");
		e35.setAutoSize(false);
		e35.setReadOnly(true);
		e35.setTextAlign(HorizontalAlignment.CENTER);
		e35.addOnDoubleClick(new EventHandler(this.callQuickEventView));
		e35.addOnGotFocus(new EventHandler(this.setAsActiveBox));
		e28.setBackColor(new Color(255, 255, 192));
		e28.setCursor(Cursor.IBEAM);
		e28.setFont(new Font("MS Sans Serif", -17));
		e28.setForeColor(Color.BLACK);
		e28.setLocation(new Point(245, 155));
		e28.setSize(new Point(40, 25));
		e28.setTabIndex(40);
		e28.setTabStop(false);
		e28.setText("");
		e28.setAutoSize(false);
		e28.setReadOnly(true);
		e28.setTextAlign(HorizontalAlignment.CENTER);
		e28.addOnDoubleClick(new EventHandler(this.callQuickEventView));
		e28.addOnGotFocus(new EventHandler(this.setAsActiveBox));
		e34.setBackColor(new Color(255, 255, 192));
		e34.setCursor(Cursor.IBEAM);
		e34.setFont(new Font("MS Sans Serif", -17));
		e34.setForeColor(Color.BLACK);
		e34.setLocation(new Point(205, 180));
		e34.setSize(new Point(40, 25));
		e34.setTabIndex(46);
		e34.setTabStop(false);
		e34.setText("");
		e34.setAutoSize(false);
		e34.setReadOnly(true);
		e34.setTextAlign(HorizontalAlignment.CENTER);
		e34.addOnDoubleClick(new EventHandler(this.callQuickEventView));
		e34.addOnGotFocus(new EventHandler(this.setAsActiveBox));
		e21.setBackColor(new Color(255, 255, 192));
		e21.setCursor(Cursor.IBEAM);
		e21.setFont(new Font("MS Sans Serif", -17));
		e21.setForeColor(Color.BLACK);
		e21.setLocation(new Point(245, 130));
		e21.setSize(new Point(40, 25));
		e21.setTabIndex(33);
		e21.setTabStop(false);
		e21.setText("");
		e21.setAutoSize(false);
		e21.setReadOnly(true);
		e21.setTextAlign(HorizontalAlignment.CENTER);
		e21.addOnDoubleClick(new EventHandler(this.callQuickEventView));
		e21.addOnGotFocus(new EventHandler(this.setAsActiveBox));
		e32.setBackColor(Color.WHITE);
		e32.setCursor(Cursor.IBEAM);
		e32.setFont(new Font("MS Sans Serif", -17));
		e32.setForeColor(Color.BLACK);
		e32.setLocation(new Point(125, 180));
		e32.setSize(new Point(40, 25));
		e32.setTabIndex(44);
		e32.setTabStop(false);
		e32.setText("");
		e32.setAutoSize(false);
		e32.setReadOnly(true);
		e32.setTextAlign(HorizontalAlignment.CENTER);
		e32.addOnDoubleClick(new EventHandler(this.callQuickEventView));
		e32.addOnGotFocus(new EventHandler(this.setAsActiveBox));
		e33.setBackColor(Color.WHITE);
		e33.setCursor(Cursor.IBEAM);
		e33.setFont(new Font("MS Sans Serif", -17));
		e33.setForeColor(Color.BLACK);
		e33.setLocation(new Point(165, 180));
		e33.setSize(new Point(40, 25));
		e33.setTabIndex(45);
		e33.setTabStop(false);
		e33.setText("");
		e33.setAutoSize(false);
		e33.setReadOnly(true);
		e33.setTextAlign(HorizontalAlignment.CENTER);
		e33.addOnDoubleClick(new EventHandler(this.callQuickEventView));
		e33.addOnGotFocus(new EventHandler(this.setAsActiveBox));
		e27.setBackColor(new Color(255, 255, 192));
		e27.setCursor(Cursor.IBEAM);
		e27.setFont(new Font("MS Sans Serif", -17));
		e27.setForeColor(Color.BLACK);
		e27.setLocation(new Point(205, 155));
		e27.setSize(new Point(40, 25));
		e27.setTabIndex(39);
		e27.setTabStop(false);
		e27.setText("");
		e27.setAutoSize(false);
		e27.setReadOnly(true);
		e27.setTextAlign(HorizontalAlignment.CENTER);
		e27.addOnDoubleClick(new EventHandler(this.callQuickEventView));
		e27.addOnGotFocus(new EventHandler(this.setAsActiveBox));
		e14.setBackColor(new Color(255, 255, 192));
		e14.setCursor(Cursor.IBEAM);
		e14.setFont(new Font("MS Sans Serif", -17));
		e14.setForeColor(Color.BLACK);
		e14.setLocation(new Point(245, 105));
		e14.setSize(new Point(40, 25));
		e14.setTabIndex(26);
		e14.setTabStop(false);
		e14.setText("");
		e14.setAutoSize(false);
		e14.setReadOnly(true);
		e14.setTextAlign(HorizontalAlignment.CENTER);
		e14.addOnDoubleClick(new EventHandler(this.callQuickEventView));
		e14.addOnGotFocus(new EventHandler(this.setAsActiveBox));
		e30.setBackColor(Color.WHITE);
		e30.setCursor(Cursor.IBEAM);
		e30.setFont(new Font("MS Sans Serif", -17));
		e30.setForeColor(Color.BLACK);
		e30.setLocation(new Point(45, 180));
		e30.setSize(new Point(40, 25));
		e30.setTabIndex(42);
		e30.setTabStop(false);
		e30.setText("");
		e30.setAutoSize(false);
		e30.setReadOnly(true);
		e30.setTextAlign(HorizontalAlignment.CENTER);
		e30.addOnDoubleClick(new EventHandler(this.callQuickEventView));
		e30.addOnGotFocus(new EventHandler(this.setAsActiveBox));
		e31.setBackColor(Color.WHITE);
		e31.setCursor(Cursor.IBEAM);
		e31.setFont(new Font("MS Sans Serif", -17));
		e31.setForeColor(Color.BLACK);
		e31.setLocation(new Point(85, 180));
		e31.setSize(new Point(40, 25));
		e31.setTabIndex(43);
		e31.setTabStop(false);
		e31.setText("");
		e31.setAutoSize(false);
		e31.setReadOnly(true);
		e31.setTextAlign(HorizontalAlignment.CENTER);
		e31.addOnDoubleClick(new EventHandler(this.callQuickEventView));
		e31.addOnGotFocus(new EventHandler(this.setAsActiveBox));
		e25.setBackColor(Color.WHITE);
		e25.setCursor(Cursor.IBEAM);
		e25.setFont(new Font("MS Sans Serif", -17));
		e25.setForeColor(Color.BLACK);
		e25.setLocation(new Point(125, 155));
		e25.setSize(new Point(40, 25));
		e25.setTabIndex(37);
		e25.setTabStop(false);
		e25.setText("");
		e25.setAutoSize(false);
		e25.setReadOnly(true);
		e25.setTextAlign(HorizontalAlignment.CENTER);
		e25.addOnDoubleClick(new EventHandler(this.callQuickEventView));
		e25.addOnGotFocus(new EventHandler(this.setAsActiveBox));
		e26.setBackColor(Color.WHITE);
		e26.setCursor(Cursor.IBEAM);
		e26.setFont(new Font("MS Sans Serif", -17));
		e26.setForeColor(Color.BLACK);
		e26.setLocation(new Point(165, 155));
		e26.setSize(new Point(40, 25));
		e26.setTabIndex(38);
		e26.setTabStop(false);
		e26.setText("");
		e26.setAutoSize(false);
		e26.setReadOnly(true);
		e26.setTextAlign(HorizontalAlignment.CENTER);
		e26.addOnDoubleClick(new EventHandler(this.callQuickEventView));
		e26.addOnGotFocus(new EventHandler(this.setAsActiveBox));
		e29.setBackColor(Color.WHITE);
		e29.setCursor(Cursor.IBEAM);
		e29.setFont(new Font("MS Sans Serif", -17));
		e29.setForeColor(Color.BLACK);
		e29.setLocation(new Point(5, 180));
		e29.setSize(new Point(40, 25));
		e29.setTabIndex(41);
		e29.setTabStop(false);
		e29.setText("");
		e29.setAutoSize(false);
		e29.setReadOnly(true);
		e29.setTextAlign(HorizontalAlignment.CENTER);
		e29.addOnDoubleClick(new EventHandler(this.callQuickEventView));
		e29.addOnGotFocus(new EventHandler(this.setAsActiveBox));
		e20.setBackColor(new Color(255, 255, 192));
		e20.setCursor(Cursor.IBEAM);
		e20.setFont(new Font("MS Sans Serif", -17));
		e20.setForeColor(Color.BLACK);
		e20.setLocation(new Point(205, 130));
		e20.setSize(new Point(40, 25));
		e20.setTabIndex(32);
		e20.setTabStop(false);
		e20.setText("");
		e20.setAutoSize(false);
		e20.setReadOnly(true);
		e20.setTextAlign(HorizontalAlignment.CENTER);
		e20.addOnDoubleClick(new EventHandler(this.callQuickEventView));
		e20.addOnGotFocus(new EventHandler(this.setAsActiveBox));
		e7.setBackColor(new Color(255, 255, 192));
		e7.setCursor(Cursor.IBEAM);
		e7.setFont(new Font("MS Sans Serif", -17));
		e7.setForeColor(Color.BLACK);
		e7.setLocation(new Point(245, 80));
		e7.setSize(new Point(40, 25));
		e7.setTabIndex(21);
		e7.setTabStop(false);
		e7.setText("");
		e7.setAutoSize(false);
		e7.setReadOnly(true);
		e7.setTextAlign(HorizontalAlignment.CENTER);
		e7.addOnDoubleClick(new EventHandler(this.callQuickEventView));
		e7.addOnGotFocus(new EventHandler(this.setAsActiveBox));
		e23.setBackColor(Color.WHITE);
		e23.setCursor(Cursor.IBEAM);
		e23.setFont(new Font("MS Sans Serif", -17));
		e23.setForeColor(Color.BLACK);
		e23.setLocation(new Point(45, 155));
		e23.setSize(new Point(40, 25));
		e23.setTabIndex(35);
		e23.setTabStop(false);
		e23.setText("");
		e23.setAutoSize(false);
		e23.setReadOnly(true);
		e23.setTextAlign(HorizontalAlignment.CENTER);
		e23.addOnDoubleClick(new EventHandler(this.callQuickEventView));
		e23.addOnGotFocus(new EventHandler(this.setAsActiveBox));
		e24.setBackColor(Color.WHITE);
		e24.setCursor(Cursor.IBEAM);
		e24.setFont(new Font("MS Sans Serif", -17));
		e24.setForeColor(Color.BLACK);
		e24.setLocation(new Point(85, 155));
		e24.setSize(new Point(40, 25));
		e24.setTabIndex(36);
		e24.setTabStop(false);
		e24.setText("");
		e24.setAutoSize(false);
		e24.setReadOnly(true);
		e24.setTextAlign(HorizontalAlignment.CENTER);
		e24.addOnDoubleClick(new EventHandler(this.callQuickEventView));
		e24.addOnGotFocus(new EventHandler(this.setAsActiveBox));
		e18.setBackColor(Color.WHITE);
		e18.setCursor(Cursor.IBEAM);
		e18.setFont(new Font("MS Sans Serif", -17));
		e18.setForeColor(Color.BLACK);
		e18.setLocation(new Point(125, 130));
		e18.setSize(new Point(40, 25));
		e18.setTabIndex(30);
		e18.setTabStop(false);
		e18.setText("");
		e18.setAutoSize(false);
		e18.setReadOnly(true);
		e18.setTextAlign(HorizontalAlignment.CENTER);
		e18.addOnDoubleClick(new EventHandler(this.callQuickEventView));
		e18.addOnGotFocus(new EventHandler(this.setAsActiveBox));
		e19.setBackColor(Color.WHITE);
		e19.setCursor(Cursor.IBEAM);
		e19.setFont(new Font("MS Sans Serif", -17));
		e19.setForeColor(Color.BLACK);
		e19.setLocation(new Point(165, 130));
		e19.setSize(new Point(40, 25));
		e19.setTabIndex(31);
		e19.setTabStop(false);
		e19.setText("");
		e19.setAutoSize(false);
		e19.setReadOnly(true);
		e19.setTextAlign(HorizontalAlignment.CENTER);
		e19.addOnDoubleClick(new EventHandler(this.callQuickEventView));
		e19.addOnGotFocus(new EventHandler(this.setAsActiveBox));
		e22.setBackColor(Color.WHITE);
		e22.setCursor(Cursor.IBEAM);
		e22.setFont(new Font("MS Sans Serif", -17));
		e22.setForeColor(Color.BLACK);
		e22.setLocation(new Point(5, 155));
		e22.setSize(new Point(40, 25));
		e22.setTabIndex(34);
		e22.setTabStop(false);
		e22.setText("");
		e22.setAutoSize(false);
		e22.setReadOnly(true);
		e22.setTextAlign(HorizontalAlignment.CENTER);
		e22.addOnDoubleClick(new EventHandler(this.callQuickEventView));
		e22.addOnGotFocus(new EventHandler(this.setAsActiveBox));
		e13.setBackColor(new Color(255, 255, 192));
		e13.setCursor(Cursor.IBEAM);
		e13.setFont(new Font("MS Sans Serif", -17));
		e13.setForeColor(Color.BLACK);
		e13.setLocation(new Point(205, 105));
		e13.setSize(new Point(40, 25));
		e13.setTabIndex(25);
		e13.setTabStop(false);
		e13.setText("");
		e13.setAutoSize(false);
		e13.setReadOnly(true);
		e13.setTextAlign(HorizontalAlignment.CENTER);
		e13.addOnDoubleClick(new EventHandler(this.callQuickEventView));
		e13.addOnGotFocus(new EventHandler(this.setAsActiveBox));
		e6.setBackColor(new Color(255, 255, 192));
		e6.setCursor(Cursor.IBEAM);
		e6.setFont(new Font("MS Sans Serif", -17));
		e6.setForeColor(Color.BLACK);
		e6.setLocation(new Point(205, 80));
		e6.setSize(new Point(40, 25));
		e6.setTabIndex(15);
		e6.setTabStop(false);
		e6.setText("");
		e6.setAutoSize(false);
		e6.setReadOnly(true);
		e6.setTextAlign(HorizontalAlignment.CENTER);
		e6.addOnDoubleClick(new EventHandler(this.callQuickEventView));
		e6.addOnGotFocus(new EventHandler(this.setAsActiveBox));
		e16.setBackColor(Color.WHITE);
		e16.setCursor(Cursor.IBEAM);
		e16.setFont(new Font("MS Sans Serif", -17));
		e16.setForeColor(Color.BLACK);
		e16.setLocation(new Point(45, 130));
		e16.setSize(new Point(40, 25));
		e16.setTabIndex(28);
		e16.setTabStop(false);
		e16.setText("");
		e16.setAutoSize(false);
		e16.setReadOnly(true);
		e16.setTextAlign(HorizontalAlignment.CENTER);
		e16.addOnDoubleClick(new EventHandler(this.callQuickEventView));
		e16.addOnGotFocus(new EventHandler(this.setAsActiveBox));
		e17.setBackColor(Color.WHITE);
		e17.setCursor(Cursor.IBEAM);
		e17.setFont(new Font("MS Sans Serif", -17));
		e17.setForeColor(Color.BLACK);
		e17.setLocation(new Point(85, 130));
		e17.setSize(new Point(40, 25));
		e17.setTabIndex(29);
		e17.setTabStop(false);
		e17.setText("");
		e17.setAutoSize(false);
		e17.setReadOnly(true);
		e17.setTextAlign(HorizontalAlignment.CENTER);
		e17.addOnDoubleClick(new EventHandler(this.callQuickEventView));
		e17.addOnGotFocus(new EventHandler(this.setAsActiveBox));
		e11.setBackColor(Color.WHITE);
		e11.setCursor(Cursor.IBEAM);
		e11.setFont(new Font("MS Sans Serif", -17));
		e11.setForeColor(Color.BLACK);
		e11.setLocation(new Point(125, 105));
		e11.setSize(new Point(40, 25));
		e11.setTabIndex(23);
		e11.setTabStop(false);
		e11.setText("");
		e11.setAutoSize(false);
		e11.setReadOnly(true);
		e11.setTextAlign(HorizontalAlignment.CENTER);
		e11.addOnDoubleClick(new EventHandler(this.callQuickEventView));
		e11.addOnGotFocus(new EventHandler(this.setAsActiveBox));
		e12.setBackColor(Color.WHITE);
		e12.setCursor(Cursor.IBEAM);
		e12.setFont(new Font("MS Sans Serif", -17));
		e12.setForeColor(Color.BLACK);
		e12.setLocation(new Point(165, 105));
		e12.setSize(new Point(40, 25));
		e12.setTabIndex(24);
		e12.setTabStop(false);
		e12.setText("");
		e12.setAutoSize(false);
		e12.setReadOnly(true);
		e12.setTextAlign(HorizontalAlignment.CENTER);
		e12.addOnDoubleClick(new EventHandler(this.callQuickEventView));
		e12.addOnGotFocus(new EventHandler(this.setAsActiveBox));
		e15.setBackColor(Color.WHITE);
		e15.setCursor(Cursor.IBEAM);
		e15.setFont(new Font("MS Sans Serif", -17));
		e15.setForeColor(Color.BLACK);
		e15.setLocation(new Point(5, 130));
		e15.setSize(new Point(40, 25));
		e15.setTabIndex(27);
		e15.setTabStop(false);
		e15.setText("");
		e15.setAutoSize(false);
		e15.setReadOnly(true);
		e15.setTextAlign(HorizontalAlignment.CENTER);
		e15.addOnDoubleClick(new EventHandler(this.callQuickEventView));
		e15.addOnGotFocus(new EventHandler(this.setAsActiveBox));
		e4.setBackColor(Color.WHITE);
		e4.setCursor(Cursor.IBEAM);
		e4.setFont(new Font("MS Sans Serif", -17));
		e4.setForeColor(Color.BLACK);
		e4.setLocation(new Point(125, 80));
		e4.setSize(new Point(40, 25));
		e4.setTabIndex(13);
		e4.setTabStop(false);
		e4.setText("");
		e4.setAutoSize(false);
		e4.setReadOnly(true);
		e4.setTextAlign(HorizontalAlignment.CENTER);
		e4.addOnDoubleClick(new EventHandler(this.callQuickEventView));
		e4.addOnGotFocus(new EventHandler(this.setAsActiveBox));
		e5.setBackColor(Color.WHITE);
		e5.setCursor(Cursor.IBEAM);
		e5.setFont(new Font("MS Sans Serif", -17));
		e5.setForeColor(Color.BLACK);
		e5.setLocation(new Point(165, 80));
		e5.setSize(new Point(40, 25));
		e5.setTabIndex(14);
		e5.setTabStop(false);
		e5.setText("");
		e5.setAutoSize(false);
		e5.setReadOnly(true);
		e5.setTextAlign(HorizontalAlignment.CENTER);
		e5.addOnDoubleClick(new EventHandler(this.callQuickEventView));
		e5.addOnGotFocus(new EventHandler(this.setAsActiveBox));
		e9.setBackColor(Color.WHITE);
		e9.setCursor(Cursor.IBEAM);
		e9.setFont(new Font("MS Sans Serif", -17));
		e9.setForeColor(Color.BLACK);
		e9.setLocation(new Point(45, 105));
		e9.setSize(new Point(40, 25));
		e9.setTabIndex(20);
		e9.setTabStop(false);
		e9.setText("");
		e9.setAutoSize(false);
		e9.setReadOnly(true);
		e9.setTextAlign(HorizontalAlignment.CENTER);
		e9.addOnDoubleClick(new EventHandler(this.callQuickEventView));
		e9.addOnGotFocus(new EventHandler(this.setAsActiveBox));
		e10.setBackColor(Color.WHITE);
		e10.setCursor(Cursor.IBEAM);
		e10.setFont(new Font("MS Sans Serif", -17));
		e10.setForeColor(Color.BLACK);
		e10.setLocation(new Point(85, 105));
		e10.setSize(new Point(40, 25));
		e10.setTabIndex(22);
		e10.setTabStop(false);
		e10.setText("");
		e10.setAutoSize(false);
		e10.setReadOnly(true);
		e10.setTextAlign(HorizontalAlignment.CENTER);
		e10.addOnDoubleClick(new EventHandler(this.callQuickEventView));
		e10.addOnGotFocus(new EventHandler(this.setAsActiveBox));
		e2.setBackColor(Color.WHITE);
		e2.setCursor(Cursor.IBEAM);
		e2.setFont(new Font("MS Sans Serif", -17));
		e2.setForeColor(Color.BLACK);
		e2.setLocation(new Point(45, 80));
		e2.setSize(new Point(40, 25));
		e2.setTabIndex(11);
		e2.setTabStop(false);
		e2.setText("");
		e2.setAutoSize(false);
		e2.setReadOnly(true);
		e2.setTextAlign(HorizontalAlignment.CENTER);
		e2.addOnDoubleClick(new EventHandler(this.callQuickEventView));
		e2.addOnGotFocus(new EventHandler(this.setAsActiveBox));
		e3.setBackColor(Color.WHITE);
		e3.setCursor(Cursor.IBEAM);
		e3.setFont(new Font("MS Sans Serif", -17));
		e3.setForeColor(Color.BLACK);
		e3.setLocation(new Point(85, 80));
		e3.setSize(new Point(40, 25));
		e3.setTabIndex(12);
		e3.setTabStop(false);
		e3.setText("");
		e3.setAutoSize(false);
		e3.setReadOnly(true);
		e3.setTextAlign(HorizontalAlignment.CENTER);
		e3.addOnDoubleClick(new EventHandler(this.callQuickEventView));
		e3.addOnGotFocus(new EventHandler(this.setAsActiveBox));
		e8.setBackColor(Color.WHITE);
		e8.setCursor(Cursor.IBEAM);
		e8.setFont(new Font("MS Sans Serif", -17));
		e8.setForeColor(Color.BLACK);
		e8.setLocation(new Point(5, 105));
		e8.setSize(new Point(40, 25));
		e8.setTabIndex(19);
		e8.setTabStop(false);
		e8.setText("");
		e8.setAutoSize(false);
		e8.setReadOnly(true);
		e8.setTextAlign(HorizontalAlignment.CENTER);
		e8.addOnDoubleClick(new EventHandler(this.callQuickEventView));
		e8.addOnGotFocus(new EventHandler(this.setAsActiveBox));
		e1.setBackColor(Color.WHITE);
		e1.setCursor(Cursor.IBEAM);
		e1.setFont(new Font("MS Sans Serif", -17));
		e1.setForeColor(Color.BLACK);
		e1.setLocation(new Point(5, 80));
		e1.setSize(new Point(40, 25));
		e1.setTabIndex(10);
		e1.setTabStop(false);
		e1.setText("");
		e1.setAutoSize(false);
		e1.setReadOnly(true);
		e1.setTextAlign(HorizontalAlignment.CENTER);
		e1.addOnDoubleClick(new EventHandler(this.callQuickEventView));
		e1.addOnGotFocus(new EventHandler(this.setAsActiveBox));
		label9.setLocation(new Point(5, 235));
		label9.setSize(new Point(90, 15));
		label9.setText("Show events:");
		label9.setTabIndex(57);
		faculty_fm.setLocation(new Point(225, 235));
		faculty_fm.setSize(new Point(130, 20));
		faculty_fm.setTabIndex(7);
		faculty_fm.setTabStop(true);
		faculty_fm.setText("Faculty Related");
		faculty_fm.setChecked(true);
		faculty_fm.setCheckState(CheckState.CHECKED);
		faculty_fm.addOnClick(new EventHandler(this.faculty_fm_click));
		students_fm.setLocation(new Point(95, 235));
		students_fm.setSize(new Point(130, 20));
		students_fm.setTabIndex(6);
		students_fm.setTabStop(true);
		students_fm.setText("Students Related");
		students_fm.setChecked(true);
		students_fm.setCheckState(CheckState.CHECKED);
		students_fm.addOnClick(new EventHandler(this.students_fm_click));
		month_fm.setBackColor(Color.WHITE);
		month_fm.setLocation(new Point(155, 30));
		month_fm.setSize(new Point(95, 24));
		month_fm.setTabIndex(16);
		month_fm.setTabStop(true);
		month_fm.setText("");
		month_fm.setItems(new Object[] {
			"January", 
			"February", 
			"March", 
			"April", 
			"May", 
			"June", 
			"July", 
			"August", 
			"September", 
			"October", 
			"November", 
			"December"});
		button6.setLocation(new Point(290, 205));
		button6.setSize(new Point(120, 25));
		button6.setTabIndex(9);
		button6.setTabStop(true);
		button6.setText("Exit");
		button6.addOnClick(new EventHandler(this.button6_click));
		e37.setBackColor(Color.WHITE);
		e37.setCursor(Cursor.IBEAM);
		e37.setFont(new Font("MS Sans Serif", -17));
		e37.setForeColor(Color.BLACK);
		e37.setLocation(new Point(45, 205));
		e37.setSize(new Point(40, 25));
		e37.setTabIndex(49);
		e37.setTabStop(false);
		e37.setText("");
		e37.setAutoSize(false);
		e37.setReadOnly(true);
		e37.setTextAlign(HorizontalAlignment.CENTER);
		e37.addOnDoubleClick(new EventHandler(this.callQuickEventView));
		e37.addOnGotFocus(new EventHandler(this.setAsActiveBox));
		e36.setBackColor(Color.WHITE);
		e36.setCursor(Cursor.IBEAM);
		e36.setFont(new Font("MS Sans Serif", -17));
		e36.setForeColor(Color.BLACK);
		e36.setLocation(new Point(5, 205));
		e36.setSize(new Point(40, 25));
		e36.setTabIndex(48);
		e36.setTabStop(false);
		e36.setText("");
		e36.setAutoSize(false);
		e36.setReadOnly(true);
		e36.setTextAlign(HorizontalAlignment.CENTER);
		e36.addOnDoubleClick(new EventHandler(this.callQuickEventView));
		e36.addOnGotFocus(new EventHandler(this.setAsActiveBox));
		menuItem1.setText("Exit");
		menuItem1.addOnClick(new EventHandler(this.menuItem1_click));
		fileMenu.setMenuItems(new MenuItem[] {
			menuItem1});
		fileMenu.setText("&File");
		menuItem2.setText("Server Info");
		menuItem2.addOnClick(new EventHandler(this.button1_click));
		menuItem3.setText("ECON Servers");
		menuItem3.addOnClick(new EventHandler(this.button4_click));
		menuItem4.setText("View Events");
		menuItem4.addOnClick(new EventHandler(this.button3_click));
		editMenu.setMenuItems(new MenuItem[] {
			menuItem2, 
			menuItem3, 
			menuItem4});
		editMenu.setText("&View");
		menuItem5.setText("Help");
		menuItem5.addOnClick(new EventHandler(this.menuItem5_click));
		menuItem6.setText("About ECON");
		menuItem6.addOnClick(new EventHandler(this.menuItem6_click));
		helpMenu.setMenuItems(new MenuItem[] {
			menuItem5, 
			menuItem6});
		helpMenu.setText("&Help");
		mainMenu1.setMenuItems(new MenuItem[] {
			fileMenu, 
			editMenu, 
			helpMenu});
		/* @designTimeOnly mainMenu1.setLocation(new Point(315, 250)); */
		this.setBackColor(Color.CONTROL);
		this.setLocation(new Point(298, 88));
		this.setSize(new Point(421, 329));
		this.setTabIndex(-1);
		this.setTabStop(true);
		this.setText("ECON Client");
		this.setAutoScaleBaseSize(16);
		this.setBorderStyle(FormBorderStyle.FIXED_3D);
		this.setClientSize(new Point(415, 279));
		this.setIcon((Icon)resources.getObject("this_icon"));
		this.setMenu(mainMenu1);
		this.addOnClick(new EventHandler(this.JPad_click));
		this.addOnClosed(new EventHandler(this.releaseResources));
		sta.setLocation(new Point(5, 255));
		sta.setSize(new Point(405, 20));
		sta.setText(" Status: Ready");
		sta.setTabIndex(58);
		sta.setBorder(true);
		button7.setLocation(new Point(290, 140));
		button7.setSize(new Point(120, 25));
		button7.setTabIndex(4);
		button7.setTabStop(true);
		button7.setText("Find Event");
		button7.addOnClick(new EventHandler(this.button55_click));
		cmonth.setFont(new Font("MS Sans Serif", -17, 700, false, false, false));
		cmonth.setLocation(new Point(85, 205));
		cmonth.setSize(new Point(200, 25));
		cmonth.setText("");
		cmonth.setTabIndex(59);
		cmonth.setAlignment(HorizontalAlignment.CENTER);
		cmonth.setBorder(true);
		button8.setLocation(new Point(290, 30));
		button8.setSize(new Point(120, 25));
		button8.setTabIndex(2);
		button8.setTabStop(true);
		button8.setText("Refresh");
		button8.addOnClick(new EventHandler(this.clickRefresh));
		this.setNewControls(new Control[] {
			button8, 
			cmonth, 
			button7, 
			sta, 
			e36, 
			e37, 
			button6, 
			month_fm, 
			students_fm, 
			faculty_fm, 
			e1, 
			e8, 
			e3, 
			e2, 
			e10, 
			e9, 
			e5, 
			e4, 
			e15, 
			e12, 
			e11, 
			e17, 
			e16, 
			e6, 
			e13, 
			e22, 
			e19, 
			e18, 
			e24, 
			e23, 
			e7, 
			e20, 
			e29, 
			e26, 
			e25, 
			e31, 
			e30, 
			e14, 
			e27, 
			e33, 
			e32, 
			e21, 
			e34, 
			e28, 
			e35, 
			label2, 
			label3, 
			label4, 
			label5, 
			label6, 
			label7, 
			label8, 
			year_fm, 
			button1, 
			button3, 
			button5, 
			button2, 
			button4, 
			serv, 
			label9});
	}
	
///////////////////////////////////////////////////////////////////////////
	
	public static class ClassInfo extends Form.ClassInfo
	{ }
	
///////////////////////////////////////////////////////////////////////////
	
	private void releaseResources(Object sender, Event e)
	{
		System.exit (0);
	}	
}


///////////////////////////////////////////////////////////////////////////
// End of Code   