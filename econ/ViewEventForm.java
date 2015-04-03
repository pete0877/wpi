// ViewEventForm.java

import wfc.app.*;
import wfc.core.*;
import wfc.ui.*;

/**
* This class can take a variable number of parameters on the command
* line. Program execution begins with the main() method. The class
* constructor is not invoked unless an object of type 'ViewEventForm'
* created in the main() method.
*/
public class ViewEventForm extends Form
{
	DateTools dtools = new DateTools();
	
	private void c2_click(Object sender, Event e)
	{
		c2.setChecked(!c2.getChecked());
	}

	private void clickST(Object sender, Event e)
	{
		c1.setChecked(!c1.getChecked());
	}

	private void button1_click(Object sender, Event e)
	{
		this.hide();
		this.dispose ();
	}

	public ViewEventForm()
	{		
		initForm();		
	}
	
	public ViewEventForm(EEvent event)
	{		
		initForm();		
		e1.setText (event.getEventTitle () );
		e2.setText (event.getDescription () );
		e3.setText (event.getAuthor () );
		
		d11.setText(dtools.getMonthName (event.getTheDate().getMonth()));
		d21.setText(dtools.getMonthName (event.getExpirationDate().getMonth()));		
		
		d12.setText ("" + event.getTheDate ().getDate());
		d22.setText ("" + event.getExpirationDate ().getDate());			
		
		String years;		
		int year;
		year = event.getTheDate ().getYear ();		
		if (year >99) {year = year -100;}
		if (year>9)
		{ years = "'" + year; }
		else
		{ years = "'0" + year; }
		d13.setText (years);
		
		year = event.getExpirationDate ().getYear();
		if (year >99) {year = year -100;}
		if (year>9)
		{ years = "'" + year; }
		else
		{ years = "'0" + year; }
		d23.setText (years);
		
		c1.setChecked (event.getStudents ());
		c2.setChecked (event.getFaculty ());		
	}

	/**
	 * The main entry point for the application. 
	 *
	 * @param args Array of parameters passed to the application
	 * via the command line.
	 */
	public static void main(String args[])
	{
		Application.run(new ViewEventForm());
	}


	/**
	* NOTE: The following code is required by the Visual J++ form
	* designer.  It can be modified using the form editor.  Do not
	* modify it using the code editor.
	*/

	Container components = new Container();
	Button button1 = new Button();
	Checkbox c1 = new Checkbox();
	Checkbox c2 = new Checkbox();
	Edit e1 = new Edit();
	Label label2 = new Label();
	Label label3 = new Label();
	Edit d12 = new Edit();
	Edit d13 = new Edit();
	Label label4 = new Label();
	Label label5 = new Label();
	Label label6 = new Label();
	Edit e3 = new Edit();
	Label label7 = new Label();
	Label label10 = new Label();
	Edit d21 = new Edit();
	Edit d22 = new Edit();
	Edit d23 = new Edit();
	Label label11 = new Label();
	Label label12 = new Label();
	Label label13 = new Label();
	Edit e2 = new Edit();
	Edit d11 = new Edit();

	private void initForm()
	{

		this.setBackColor(Color.CONTROL);
		this.setLocation(new Point(0, 0));
		this.setSize(new Point(509, 317));
		this.setTabIndex(-1);
		this.setTabStop(true);
		this.setText("View Event Detail");
		this.setAutoScaleBaseSize(16);
		this.setBorderStyle(FormBorderStyle.FIXED_3D);
		this.setClientSize(new Point(503, 292));
		this.setControlBox(false);
		this.setShowInTaskbar(false);
		button1.setLocation(new Point(380, 260));
		button1.setSize(new Point(110, 25));
		button1.setTabIndex(0);
		button1.setTabStop(true);
		button1.setText("Close");
		button1.addOnClick(new EventHandler(this.button1_click));
		c1.setLocation(new Point(300, 150));
		c1.setSize(new Point(190, 20));
		c1.setTabIndex(9);
		c1.setTabStop(false);
		c1.setText("Students Related");
		c1.addOnClick(new EventHandler(this.clickST));
		c2.setLocation(new Point(300, 170));
		c2.setSize(new Point(190, 20));
		c2.setTabIndex(10);
		c2.setTabStop(false);
		c2.setText("Faculty Ralated");
		c2.addOnClick(new EventHandler(this.c2_click));
		e1.setBackColor(Color.WHITE);
		e1.setCursor(Cursor.IBEAM);
		e1.setLocation(new Point(10, 30));
		e1.setSize(new Point(280, 23));
		e1.setTabIndex(1);
		e1.setTabStop(false);
		e1.setText("");
		e1.setBorderStyle(BorderStyle.FIXED_SINGLE);
		e1.setReadOnly(true);
		label2.setLocation(new Point(10, 10));
		label2.setSize(new Point(280, 20));
		label2.setText("Event Title:");
		label2.setTabIndex(11);
		label3.setLocation(new Point(10, 60));
		label3.setSize(new Point(280, 20));
		label3.setText("Event Description:");
		label3.setTabIndex(12);
		d12.setBackColor(Color.WHITE);
		d12.setCursor(Cursor.IBEAM);
		d12.setLocation(new Point(400, 30));
		d12.setSize(new Point(40, 23));
		d12.setTabIndex(4);
		d12.setTabStop(false);
		d12.setText("");
		d12.setBorderStyle(BorderStyle.FIXED_SINGLE);
		d12.setMaxLength(2);
		d12.setReadOnly(true);
		d13.setBackColor(Color.WHITE);
		d13.setCursor(Cursor.IBEAM);
		d13.setLocation(new Point(440, 30));
		d13.setSize(new Point(50, 23));
		d13.setTabIndex(5);
		d13.setTabStop(false);
		d13.setText("");
		d13.setBorderStyle(BorderStyle.FIXED_SINGLE);
		d13.setMaxLength(2);
		d13.setReadOnly(true);
		label4.setLocation(new Point(300, 10));
		label4.setSize(new Point(100, 20));
		label4.setText("Month");
		label4.setTabIndex(13);
		label5.setLocation(new Point(400, 10));
		label5.setSize(new Point(40, 20));
		label5.setText("Day");
		label5.setTabIndex(14);
		label6.setLocation(new Point(440, 10));
		label6.setSize(new Point(40, 20));
		label6.setText("Year");
		label6.setTabIndex(15);
		e3.setBackColor(Color.WHITE);
		e3.setCursor(Cursor.IBEAM);
		e3.setLocation(new Point(10, 260));
		e3.setSize(new Point(280, 23));
		e3.setTabIndex(16);
		e3.setTabStop(false);
		e3.setText("");
		e3.setBorderStyle(BorderStyle.FIXED_SINGLE);
		e3.setReadOnly(true);
		label7.setLocation(new Point(10, 240));
		label7.setSize(new Point(280, 20));
		label7.setText("Author\'s Name:");
		label7.setTabIndex(17);
		label10.setLocation(new Point(300, 80));
		label10.setSize(new Point(190, 20));
		label10.setText("Event Expiration Date:");
		label10.setTabIndex(18);
		d21.setBackColor(Color.WHITE);
		d21.setCursor(Cursor.IBEAM);
		d21.setLocation(new Point(300, 120));
		d21.setSize(new Point(100, 23));
		d21.setTabIndex(2);
		d21.setTabStop(false);
		d21.setText("");
		d21.setBorderStyle(BorderStyle.FIXED_SINGLE);
		d21.setMaxLength(2);
		d21.setReadOnly(true);
		d22.setBackColor(Color.WHITE);
		d22.setCursor(Cursor.IBEAM);
		d22.setLocation(new Point(400, 120));
		d22.setSize(new Point(40, 23));
		d22.setTabIndex(7);
		d22.setTabStop(false);
		d22.setText("");
		d22.setBorderStyle(BorderStyle.FIXED_SINGLE);
		d22.setMaxLength(2);
		d22.setReadOnly(true);
		d23.setBackColor(Color.WHITE);
		d23.setCursor(Cursor.IBEAM);
		d23.setLocation(new Point(440, 120));
		d23.setSize(new Point(50, 23));
		d23.setTabIndex(8);
		d23.setTabStop(false);
		d23.setText("");
		d23.setBorderStyle(BorderStyle.FIXED_SINGLE);
		d23.setMaxLength(2);
		d23.setReadOnly(true);
		label11.setLocation(new Point(300, 100));
		label11.setSize(new Point(80, 20));
		label11.setText("Month");
		label11.setTabIndex(19);
		label12.setLocation(new Point(400, 100));
		label12.setSize(new Point(40, 20));
		label12.setText("Day");
		label12.setTabIndex(20);
		label13.setLocation(new Point(440, 100));
		label13.setSize(new Point(40, 20));
		label13.setText("Year");
		label13.setTabIndex(21);
		e2.setBackColor(Color.WHITE);
		e2.setCursor(Cursor.IBEAM);
		e2.setLocation(new Point(10, 80));
		e2.setSize(new Point(280, 120));
		e2.setTabIndex(6);
		e2.setTabStop(false);
		e2.setText("");
		e2.setBorderStyle(BorderStyle.FIXED_SINGLE);
		e2.setMultiline(true);
		e2.setReadOnly(true);
		e2.setScrollBars(ScrollBars.VERTICAL);
		d11.setBackColor(Color.WHITE);
		d11.setCursor(Cursor.IBEAM);
		d11.setLocation(new Point(300, 30));
		d11.setSize(new Point(100, 23));
		d11.setTabIndex(3);
		d11.setTabStop(false);
		d11.setText("");
		d11.setBorderStyle(BorderStyle.FIXED_SINGLE);
		d11.setMaxLength(2);
		d11.setReadOnly(true);
		this.setNewControls(new Control[] {
			d21, 
			d11, 
			e2, 
			label13, 
			label12, 
			label11, 
			d23, 
			d22, 
			label10, 
			label7, 
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
			button1});
	}
	// NOTE: End of form designer support code

	public static class ClassInfo extends Form.ClassInfo
	{
		// TODO: Add your property and event infos here
	}
}

