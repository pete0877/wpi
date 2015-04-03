 // EventsForm.java

import wfc.app.*;
import wfc.core.*;
import wfc.ui.*;
import java.util.Date;					 
import java.net.*;
import java.io.*;	
import java.lang.Integer;
import java.util.Vector;
import java.util.Enumeration;

public class EventsForm extends Form
{
	
	private void callDblCkick(Object sender, Event e)
	{
		EEvent env0;
		if (le.getItemCount ()==0)
		{
			Message msg = new Message ("No events for this date");
			msg.showDialog ();
		} else {	
			env0 = (EEvent) this.eids.elementAt (le.getSelectedIndex ());
			
			ViewEventForm newForm1 = new ViewEventForm(env0);
			newForm1.showDialog ();
		}
	}

	Enumeration list;	
	Vector eids;
	ECalendar calendar;
	Date TheDay;
	EServerList Servers;
	int PortNumber;
	
	private void b4_click(Object sender, Event e)
	{
		if (Servers.getSize()>0) 
		{
			EEvent newEvent1;
			if (le.getItemCount ()==0)
			{
				Message msg = new Message ("No events for this date");
				msg.showDialog ();
			} else {	
				newEvent1 = (EEvent) this.eids.elementAt (le.getSelectedIndex ());
				
				EEvent old = new EEvent (newEvent1);
				
				long oldID = newEvent1.getID();				
				ModifyEventForm newForm1 = new ModifyEventForm (newEvent1);
				newForm1.showDialog ();
				
				// check if Form was canceled
				if (newEvent1.getID()!=-1)
				{	
					newEvent1.setID(oldID);
					String servname = "" + Servers.getServer (Servers.getCurrentServerIndex ()).getDNSname ();
					
					this.sta.setText (" Status: Sending update command to the ECON Server"); 	
					Socket socket = null;
					EError error1 = new EError(0,"");
					ObjectInputStream is = null;
					ObjectOutputStream os= null;
		
					try {
						socket = new Socket(servname, this.PortNumber);
						os = new ObjectOutputStream(socket.getOutputStream());
						is = new ObjectInputStream(socket.getInputStream());
						Integer cmd1 = new Integer(3);
						os.writeObject(cmd1);
						os.writeObject(newEvent1);
						os.flush();
						error1 = (EError) is.readObject();
						socket.close();
						this.sta.setText (" Status: Ready"); 
						if (error1.getEErrorNumber ()!= 0) 
						{
							newEvent1.setAuthor(old.getAuthor());
							newEvent1.setDescription (old.getDescription ());
							newEvent1.setEventTitle (old.getEventTitle());
							newEvent1.setExpirationDate(old.getExpirationDate());
							newEvent1.setTheDate(old.getTheDate());
							newEvent1.setFaculty(old.getFaculty());
							newEvent1.setStudents(old.getStudents ());
							newEvent1.setPassword(old.getPassword());
							newEvent1.setID (old.getID());
							
							ErrorForm form7 = new ErrorForm(error1);
							form7.showDialog ();						
						} else
						{													
							this.calendar.modifyEvent(newEvent1);							
							this.showTheList();
						}		
					}
					catch (Exception netexep)
					{
						newEvent1.setAuthor(old.getAuthor());
						newEvent1.setDescription (old.getDescription ());
						newEvent1.setEventTitle (old.getEventTitle());
						newEvent1.setExpirationDate(old.getExpirationDate());
						newEvent1.setTheDate(old.getTheDate());
						newEvent1.setFaculty(old.getFaculty());
						newEvent1.setStudents(old.getStudents ());
						newEvent1.setPassword(old.getPassword());
						newEvent1.setID (old.getID());
						
						EError newError1 = new EError(1,"Unable to Establish a Network Connection");
						ErrorForm newForm8 = new ErrorForm (newError1);
						newForm8.showDialog();					
						this.sta.setText (" Status: Network connection failed."); 
					}									
				} 
				 else
				{
					newEvent1.setAuthor(old.getAuthor());
					newEvent1.setDescription (old.getDescription ());
					newEvent1.setEventTitle (old.getEventTitle());
					newEvent1.setExpirationDate(old.getExpirationDate());
					newEvent1.setTheDate(old.getTheDate());
					newEvent1.setFaculty(old.getFaculty());
					newEvent1.setStudents(old.getStudents ());
					newEvent1.setPassword(old.getPassword());
					newEvent1.setID (old.getID());
				}
			}		
		} else {
			EError newError1 = new EError (8, "No Servers Registered");
			ErrorForm newForm1 = new ErrorForm(newError1);
			newForm1.showDialog ();
		}
	}

	private void b3_click(Object sender, Event e)
	{
		if (Servers.getSize()>0) 
		{
			EEvent newEvent1;
			if (le.getItemCount ()==0)
			{
				Message msg = new Message ("No events for this date");
				msg.showDialog ();
			} else {	
				newEvent1 = (EEvent) this.eids.elementAt (le.getSelectedIndex ());
				
				long oldID = newEvent1.getID();				
				DeleteEventForm newForm1 = new DeleteEventForm (newEvent1);
				newForm1.showDialog ();
				
				// check if Form was canceled
				if (newEvent1.getID()!=-1)
				{				
					String servname = "" + Servers.getServer (Servers.getCurrentServerIndex ()).getDNSname ();
					
					this.sta.setText (" Status: Sending delete command to the ECON Server"); 	
					Socket socket = null;
					EError error1 = new EError(0,"");
					ObjectInputStream is = null;
					ObjectOutputStream os= null;
		
					try {
						socket = new Socket(servname, this.PortNumber);
						os = new ObjectOutputStream(socket.getOutputStream());
						is = new ObjectInputStream(socket.getInputStream());
						Integer cmd1 = new Integer(2);
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
							this.calendar.deleteEvent(newEvent1);							
							this.showTheList();
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
				 else
				{
					newEvent1.setID(oldID);	 
				}
			}		
		} else {
			EError newError1 = new EError (8, "No Servers Registered");
			ErrorForm newForm1 = new ErrorForm(newError1);
			newForm1.showDialog ();
		}
	}


	private void b1_click(Object sender, Event e)
	{
		EEvent env0;
		if (le.getItemCount ()==0)
		{
			Message msg = new Message ("No events for this date");
			msg.showDialog ();
		} else {	
			env0 = (EEvent) this.eids.elementAt (le.getSelectedIndex ());
			
			ViewEventForm newForm1 = new ViewEventForm(env0);
			newForm1.showDialog ();
		}
	}

	private void students_fm_click(Object sender, Event e)
	{
		faculty_fm.setChecked (true);
		showTheList();
	}

	private void faculty_fm_click(Object sender, Event e)
	{
		students_fm.setChecked (true);
		showTheList();
	}
	
	private void showTheList()
	{
		boolean st = this.students_fm.getChecked ();
		boolean fa = this.faculty_fm.getChecked ();
		
		EEvent e00;			
		eids = new Vector();
		le.removeAll ();	
		list = calendar.getEvents (TheDay);
		while (list.hasMoreElements ())
		{			
			e00 = (EEvent) list.nextElement();			
			if ((e00.getStudents () && st) || (e00.getFaculty () && fa))
			{
				le.addItem (e00.getEventTitle ());
				le.setSelectedIndex  (0);	
				eids.addElement (e00);
			}
		}
		
	}

	private void button1_click(Object sender, Event e)
	{
		this.hide();
		this.dispose();
	}

	public EventsForm()
	{
		initForm();		
	}

	public EventsForm(ECalendar cal, Date day, boolean st, boolean fa, EServerList svs, int prtnumb)
	{	
		PortNumber = prtnumb;
		Servers = svs;
		
		TheDay = day;
		calendar = cal;					
		
		initForm();		
		
		this.setText("Events for " + day.toGMTString ());		
		this.students_fm.setChecked (st);
		this.faculty_fm.setChecked (fa);	
		
		showTheList();		
	}

	/**
	 * The main entry point for the application. 
	 *
	 * @param args Array of parameters passed to the application
	 * via the command line.
	 */
	public static void main(String args[])
	{
		Application.run(new EventsForm());
	}


	/**
	* NOTE: The following code is required by the Visual J++ form
	* designer.  It can be modified using the form editor.  Do not
	* modify it using the code editor.
	*/

	Container components = new Container();
	Button button1 = new Button();
	ListBox le = new ListBox();
	Label label2 = new Label();
	Label label9 = new Label();
	Checkbox students_fm = new Checkbox();
	Checkbox faculty_fm = new Checkbox();
	Button b1 = new Button();
	Button b3 = new Button();
	Button b4 = new Button();
	Label sta = new Label();

	private void initForm()
	{

		this.setBackColor(Color.CONTROL);
		this.setLocation(new Point(0, 0));
		this.setSize(new Point(403, 296));
		this.setTabIndex(-1);
		this.setTabStop(true);
		this.setText("Events for ");
		this.setAutoScaleBaseSize(16);
		this.setClientSize(new Point(395, 269));
		this.setControlBox(false);
		this.setShowInTaskbar(false);
		button1.setLocation(new Point(280, 215));
		button1.setSize(new Point(110, 25));
		button1.setTabIndex(4);
		button1.setTabStop(true);
		button1.setText("Close");
		button1.addOnClick(new EventHandler(this.button1_click));
		le.setLocation(new Point(5, 45));
		le.setSize(new Point(270, 198));
		le.setTabIndex(0);
		le.setTabStop(true);
		le.setIntegralHeight(true);
		le.setItemHeight(16);
		le.setItems(new Object[] {});
		le.addOnDoubleClick(new EventHandler(this.callDblCkick));
		label2.setLocation(new Point(80, 10));
		label2.setSize(new Point(290, 20));
		label2.setText("");
		label2.setTabIndex(7);
		label9.setLocation(new Point(5, 5));
		label9.setSize(new Point(90, 20));
		label9.setText("Show events:");
		label9.setTabIndex(8);
		students_fm.setLocation(new Point(10, 25));
		students_fm.setSize(new Point(130, 20));
		students_fm.setTabIndex(5);
		students_fm.setTabStop(true);
		students_fm.setText("Students Related");
		students_fm.setChecked(true);
		students_fm.setCheckState(CheckState.CHECKED);
		students_fm.addOnClick(new EventHandler(this.students_fm_click));
		faculty_fm.setLocation(new Point(150, 25));
		faculty_fm.setSize(new Point(130, 20));
		faculty_fm.setTabIndex(6);
		faculty_fm.setTabStop(true);
		faculty_fm.setText("Faculty Related");
		faculty_fm.setChecked(true);
		faculty_fm.setCheckState(CheckState.CHECKED);
		faculty_fm.addOnClick(new EventHandler(this.faculty_fm_click));
		b1.setLocation(new Point(280, 45));
		b1.setSize(new Point(110, 25));
		b1.setTabIndex(1);
		b1.setTabStop(true);
		b1.setText("View Event");
		b1.addOnClick(new EventHandler(this.b1_click));
		b3.setLocation(new Point(280, 100));
		b3.setSize(new Point(110, 25));
		b3.setTabIndex(2);
		b3.setTabStop(true);
		b3.setText("Delete Event");
		b3.addOnClick(new EventHandler(this.b3_click));
		b4.setLocation(new Point(280, 160));
		b4.setSize(new Point(110, 25));
		b4.setTabIndex(3);
		b4.setTabStop(true);
		b4.setText("Modify Event");
		b4.addOnClick(new EventHandler(this.b4_click));
		sta.setLocation(new Point(5, 245));
		sta.setSize(new Point(385, 20));
		sta.setText(" Status: Ready");
		sta.setTabIndex(10);
		sta.setBorder(true);
		this.setNewControls(new Control[] {
			sta, 
			b4, 
			b3, 
			b1, 
			faculty_fm, 
			students_fm, 
			label9, 
			label2, 
			le, 
			button1});
	}
	// NOTE: End of form designer support code

	public static class ClassInfo extends Form.ClassInfo
	{
		// TODO: Add your property and event infos here
	}
}
