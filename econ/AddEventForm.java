// AddEventForm.java

import wfc.app.*;
import wfc.core.*;
import wfc.ui.*;

/**
* This class can take a variable number of parameters on the command
* line. Program execution begins with the main() method. The class
* constructor is not invoked unless an object of type 'AddEventForm'
* created in the main() method.
*/
public class AddEventForm extends Form
{
	EEvent newEvent1;
	
	private void button2_click(Object sender, Event e)
	{
		String s1 = new String(e1.toString().trim ());
		String s2 = new String(e2.toString().trim ());
		String s3 = new String(e3.toString().trim ());
		String s4 = new String(e4.toString().trim ());
		
		String sd11 = new String(d11.toString().trim ());
		String sd12 = new String(d12.toString().trim ());
		String sd13 = new String(d13.toString().trim ());
		
		String sd21 = new String(d21.toString().trim ());
		String sd22 = new String(d22.toString().trim ());
		String sd23 = new String(d23.toString().trim ());		
		
		boolean students  = c1.getChecked ();
		boolean faculty  = c2.getChecked ();
		
		boolean filled = true;
		
		if (s1.equals ("")) { filled = false; }
		if (s2.equals ("")) { filled = false; }
		if (s3.equals ("")) { filled = false; }
		if (s4.equals ("")) { filled = false; }
		
		if (!filled) 
		{
			EError newError1 = new EError(6,"Required Data Field Missing");
			ErrorForm newForm1 = new ErrorForm (newError1);
		}
		else
		{
			newEvent1.setEventTitle (s1);
			this.hide();
			this.dispose();			
		}
		
		
	}

	private void button1_click(Object sender, Event e)
	{
		this.hide();
		this.dispose();
	}

	private void AddEventForm_click(Object sender, Event e)
	{

	}

	private void edit2_change(Object sender, Event e)
	{

	}

	public AddEventForm(EEvent e1)
	{
		initForm();				
		newEvent1 = e1;
	}	

	public AddEventForm()
	{
		initForm();				
		newEvent1 = new EEvent ();
	}	
	
	/**
	 * The main entry point for the application. 
	 *
	 * @param args Array of parameters passed to the application
	 * via the command line.
	 */
	public static void main(String args[])
	{
		Application.run(new AddEventForm());
	}


	/**
	* NOTE: The following code is required by the Visual J++ form
	* designer.  It can be modified using the form editor.  Do not
	* modify it using the code editor.
	*/

	Container components = new Container();
	Button button1 = new Button();
	Button button2 = new Button();
	Checkbox c1 = new Checkbox();
	Checkbox c2 = new Checkbox();
	Edit e1 = new Edit();
	Label label2 = new Label();
	ComboBox d11 = new ComboBox();
	Label label3 = new Label();
	Edit e2 = new Edit();
	Edit d12 = new Edit();
	Edit d13 = new Edit();
	Label label4 = new Label();
	Label label5 = new Label();
	Label label6 = new Label();
	Edit e3 = new Edit();
	Edit e4 = new Edit();
	Label label7 = new Label();
	Label label8 = new Label();
	Label label10 = new Label();
	ComboBox d21 = new ComboBox();
	Edit d22 = new Edit();
	Edit d23 = new Edit();
	Label label11 = new Label();
	Label label12 = new Label();
	Label label13 = new Label();

	private void initForm()
	{

		this.setBackColor(Color.CONTROL);
		this.setLocation(new Point(0, 0));
		this.setSize(new Point(505, 312));
		this.setTabIndex(-1);
		this.setTabStop(true);
		this.setText("Add Event");
		this.setAutoScaleBaseSize(16);
		this.setAutoScroll(true);
		this.setBorderStyle(FormBorderStyle.FIXED_3D);
		this.setClientSize(new Point(499, 287));
		this.setControlBox(false);
		this.setShowInTaskbar(false);
		this.addOnClick(new EventHandler(this.AddEventForm_click));
		button1.setLocation(new Point(380, 260));
		button1.setSize(new Point(110, 25));
		button1.setTabIndex(13);
		button1.setTabStop(true);
		button1.setText("Cancel");
		button1.addOnClick(new EventHandler(this.button1_click));
		button2.setLocation(new Point(260, 260));
		button2.setSize(new Point(110, 25));
		button2.setTabIndex(12);
		button2.setTabStop(true);
		button2.setText("Add");
		button2.addOnClick(new EventHandler(this.button2_click));
		c1.setLocation(new Point(300, 150));
		c1.setSize(new Point(190, 20));
		c1.setTabIndex(8);
		c1.setTabStop(true);
		c1.setText("Students Related");
		c1.setChecked(true);
		c1.setCheckState(CheckState.CHECKED);
		c2.setLocation(new Point(300, 170));
		c2.setSize(new Point(190, 20));
		c2.setTabIndex(9);
		c2.setTabStop(true);
		c2.setText("Faculty Ralated");
		e1.setBackColor(Color.WINDOW);
		e1.setCursor(Cursor.IBEAM);
		e1.setLocation(new Point(10, 30));
		e1.setSize(new Point(280, 23));
		e1.setTabIndex(0);
		e1.setTabStop(true);
		e1.setText("");
		label2.setLocation(new Point(10, 10));
		label2.setSize(new Point(280, 20));
		label2.setText("Event Title:");
		label2.setTabIndex(14);
		d11.setLocation(new Point(300, 30));
		d11.setSize(new Point(100, 24));
		d11.setTabIndex(4);
		d11.setTabStop(true);
		d11.setText("");
		d11.setItems(new Object[] {
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
		label3.setLocation(new Point(10, 60));
		label3.setSize(new Point(280, 20));
		label3.setText("Event Description:");
		label3.setTabIndex(15);
		e2.setBackColor(Color.WINDOW);
		e2.setCursor(Cursor.IBEAM);
		e2.setLocation(new Point(10, 80));
		e2.setSize(new Point(280, 120));
		e2.setTabIndex(3);
		e2.setTabStop(true);
		e2.setText("");
		e2.setMultiline(true);
		d12.setBackColor(Color.WINDOW);
		d12.setCursor(Cursor.IBEAM);
		d12.setLocation(new Point(400, 30));
		d12.setSize(new Point(40, 23));
		d12.setTabIndex(1);
		d12.setTabStop(true);
		d12.setText("");
		d13.setBackColor(Color.WINDOW);
		d13.setCursor(Cursor.IBEAM);
		d13.setLocation(new Point(440, 30));
		d13.setSize(new Point(50, 23));
		d13.setTabIndex(2);
		d13.setTabStop(true);
		d13.setText("");
		label4.setLocation(new Point(300, 10));
		label4.setSize(new Point(100, 20));
		label4.setText("Month");
		label4.setTabIndex(16);
		label5.setLocation(new Point(400, 10));
		label5.setSize(new Point(40, 20));
		label5.setText("Day");
		label5.setTabIndex(17);
		label6.setLocation(new Point(440, 10));
		label6.setSize(new Point(40, 20));
		label6.setText("Year");
		label6.setTabIndex(18);
		e3.setBackColor(Color.WINDOW);
		e3.setCursor(Cursor.IBEAM);
		e3.setLocation(new Point(10, 230));
		e3.setSize(new Point(280, 23));
		e3.setTabIndex(10);
		e3.setTabStop(true);
		e3.setText("");
		e4.setBackColor(Color.WINDOW);
		e4.setCursor(Cursor.IBEAM);
		e4.setLocation(new Point(300, 230));
		e4.setSize(new Point(190, 23));
		e4.setTabIndex(11);
		e4.setTabStop(true);
		e4.setText("");
		label7.setLocation(new Point(10, 210));
		label7.setSize(new Point(280, 20));
		label7.setText("Author\'s Name:");
		label7.setTabIndex(19);
		label8.setLocation(new Point(300, 210));
		label8.setSize(new Point(190, 20));
		label8.setText("Choose User Password");
		label8.setTabIndex(20);
		label10.setLocation(new Point(300, 80));
		label10.setSize(new Point(190, 20));
		label10.setText("Event Expiration Date:");
		label10.setTabIndex(21);
		d21.setLocation(new Point(300, 120));
		d21.setSize(new Point(100, 24));
		d21.setTabIndex(5);
		d21.setTabStop(true);
		d21.setText("");
		d21.setItems(new Object[] {
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
		d22.setBackColor(Color.WINDOW);
		d22.setCursor(Cursor.IBEAM);
		d22.setLocation(new Point(400, 120));
		d22.setSize(new Point(40, 23));
		d22.setTabIndex(6);
		d22.setTabStop(true);
		d22.setText("");
		d23.setBackColor(Color.WINDOW);
		d23.setCursor(Cursor.IBEAM);
		d23.setLocation(new Point(440, 120));
		d23.setSize(new Point(50, 23));
		d23.setTabIndex(7);
		d23.setTabStop(true);
		d23.setText("");
		label11.setLocation(new Point(300, 100));
		label11.setSize(new Point(80, 20));
		label11.setText("Month");
		label11.setTabIndex(22);
		label12.setLocation(new Point(400, 100));
		label12.setSize(new Point(40, 20));
		label12.setText("Day");
		label12.setTabIndex(23);
		label13.setLocation(new Point(440, 100));
		label13.setSize(new Point(40, 20));
		label13.setText("Year");
		label13.setTabIndex(25);
		this.setNewControls(new Control[] {
			d11, 
			e2, 
			label13, 
			label12, 
			label11, 
			d23, 
			d22, 
			d21, 
			label10, 
			label8, 
			label7, 
			e4, 
			e3, 
			label6, 
			label5, 
			label4, 
			d13, 
			d12, 
			label3, 
			label2, 
			e1, 
			c2, 
			c1, 
			button2, 
			button1});
	}
	// NOTE: End of form designer support code

	public static class ClassInfo extends Form.ClassInfo
	{
		// TODO: Add your property and event infos here
	}
}
