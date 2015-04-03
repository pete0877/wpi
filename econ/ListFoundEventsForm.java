// ListFoundEventsForm.java

import wfc.app.*;
import wfc.core.*;
import wfc.ui.*;
import java.lang.Integer;
import java.util.Vector;
import java.util.Enumeration;

/**
* This class can take a variable number of parameters on the command
* line. Program execution begins with the main() method. The class
* constructor is not invoked unless an object of type 'ListFoundEventsForm'
* created in the main() method.
*/
public class ListFoundEventsForm extends Form
{	
	Enumeration list;	
	Vector eids;
	
	private void button1_click(Object sender, Event e)
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

	
	private void bdlClick(Object sender, Event e)
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
		this.hide();
		this.dispose();
	}

	public ListFoundEventsForm()
	{
		initForm();		
	}

	public ListFoundEventsForm(Enumeration en1)
	{		
		initForm();		
		list = en1;
		
		EEvent e00;			
		eids = new Vector();
		le.removeAll ();	
		
		while (list.hasMoreElements ())
		{			
				e00 = (EEvent) list.nextElement();			
				le.addItem (e00.getEventTitle ());
				le.setSelectedIndex  (0);	
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
		Application.run(new ListFoundEventsForm());
	}


	/**
	* NOTE: The following code is required by the Visual J++ form
	* designer.  It can be modified using the form editor.  Do not
	* modify it using the code editor.
	*/

	Container components = new Container();
	Button button1 = new Button();
	Button button2 = new Button();
	ListBox le = new ListBox();

	private void initForm()
	{

		this.setBackColor(Color.CONTROL);
		this.setLocation(new Point(0, 0));
		this.setSize(new Point(393, 248));
		this.setTabIndex(-1);
		this.setTabStop(true);
		this.setText("Found Events");
		this.setAutoScaleBaseSize(16);
		this.setBorderStyle(FormBorderStyle.FIXED_3D);
		this.setClientSize(new Point(387, 223));
		this.setControlBox(false);
		this.setShowInTaskbar(false);
		button1.setLocation(new Point(265, 10));
		button1.setSize(new Point(115, 25));
		button1.setTabIndex(0);
		button1.setTabStop(true);
		button1.setText("View Event");
		button1.addOnClick(new EventHandler(this.button1_click));
		button2.setLocation(new Point(265, 195));
		button2.setSize(new Point(115, 25));
		button2.setTabIndex(1);
		button2.setTabStop(true);
		button2.setText("Close");
		button2.addOnClick(new EventHandler(this.button2_click));
		le.setLocation(new Point(5, 5));
		le.setSize(new Point(255, 214));
		le.setTabIndex(3);
		le.setTabStop(true);
		le.setIntegralHeight(true);
		le.setItemHeight(16);
		le.setItems(new Object[] {});
		le.addOnDoubleClick(new EventHandler(this.bdlClick));
		this.setNewControls(new Control[] {
			le, 
			button2, 
			button1});
	}
	// NOTE: End of form designer support code

	public static class ClassInfo extends Form.ClassInfo
	{
		// TODO: Add your property and event infos here
	}
}
