// FoundEventsForm.java

import wfc.app.*;
import wfc.core.*;
import wfc.ui.*;
import java.lang.Integer;
import java.util.Vector;
import java.util.Enumeration;

/**
* This class can take a variable number of parameters on the command
* line. Program execution begins with the main() method. The class
* constructor is not invoked unless an object of type 'FoundEventsForm'
* created in the main() method.
*/
public class FoundEventsForm extends Form
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
	
	
	private void button2_click(Object sender, Event e)
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

	private void button1_click(Object sender, Event e)
	{
		this.hide();
		this.dispose();
	}

	Enumeration list;	
	Vector eids;
	ECalendar calendar;	
	
	public FoundEventsForm()
	{
		// Required for Visual J++ Form Designer support
		initForm();		

		// TODO: Add any constructor code after initForm call
	}

	public FoundEventsForm(Enumeration evs)
	{	
		list = evs;
		EEvent e00;			
		eids = new Vector();
		le.removeAll ();			
		while (list.hasMoreElements ())
		{			
			e00 = (EEvent) list.nextElement();			
			le.addItem (e00.getEventTitle ());
			le.setSelectedIndex (0);	
			eids.addElement (e00);			
		}
	}	
	
	/**
	 * The main entry point for the application. 
	 *
	 * @param args Array of parameters passed to the application
	 * via the command line.
	 */
	public static void main(String args[])
	{
		Application.run(new FoundEventsForm());
	}


	/**
	* NOTE: The following code is required by the Visual J++ form
	* designer.  It can be modified using the form editor.  Do not
	* modify it using the code editor.
	*/

	Container components = new Container();
	ListBox le = new ListBox();
	Button button1 = new Button();
	Button button2 = new Button();

	private void initForm()
	{

		this.setBackColor(Color.CONTROL);
		this.setLocation(new Point(0, 0));
		this.setSize(new Point(403, 232));
		this.setTabIndex(-1);
		this.setTabStop(true);
		this.setText("Found Events");
		this.setAutoScaleBaseSize(16);
		this.setBorderStyle(FormBorderStyle.FIXED_3D);
		this.setClientSize(new Point(397, 207));
		this.setControlBox(false);
		this.setShowInTaskbar(false);
		le.setLocation(new Point(5, 5));
		le.setSize(new Point(270, 198));
		le.setTabIndex(0);
		le.setTabStop(true);
		le.setIntegralHeight(true);
		le.setItemHeight(16);
		le.setItems(new Object[] {});
		le.addOnDoubleClick(new EventHandler(this.callDblCkick));
		button1.setLocation(new Point(280, 175));
		button1.setSize(new Point(110, 25));
		button1.setTabIndex(1);
		button1.setTabStop(true);
		button1.setText("Close");
		button1.addOnClick(new EventHandler(this.button1_click));
		button2.setLocation(new Point(280, 10));
		button2.setSize(new Point(110, 25));
		button2.setTabIndex(3);
		button2.setTabStop(true);
		button2.setText("View Event");
		button2.addOnClick(new EventHandler(this.button2_click));
		this.setNewControls(new Control[] {
			button1, 
			le, 
			button2});
	}
	// NOTE: End of form designer support code

	public static class ClassInfo extends Form.ClassInfo
	{
		// TODO: Add your property and event infos here
	}
}
