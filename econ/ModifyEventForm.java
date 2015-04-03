// ModifyEventForm.java

import wfc.app.*;
import wfc.core.*;
import wfc.ui.*;
import java.util.Date;

/**
* This class can take a variable number of parameters on the command
* line. Program execution begins with the main() method. The class
* constructor is not invoked unless an object of type 'ModifyEventForm'
* created in the main() method.
*/
public class ModifyEventForm extends Form
{
	private void ModifyEventForm_click(Object sender, Event e)
	{

	}

	private void e4_change(Object sender, Event e)
	{

	}

	private void button2_click(Object sender, Event e)
	{
		if (this.e4.getText().trim().equals(""))
		{
			EError newError1 = new EError(10,"Missing User Password");
			ErrorForm newForm1 = new ErrorForm (newError1);
			newForm1.showDialog ();	
		} else
		{
			TheEvent.setPassword(this.e4.getText().trim());
			
			TheEvent.setID (-1);
		
			String s1 = "" + e1.getText();
			s1 = "" + s1.trim();
		
			String s2 = "" + e2.getText();
			s1 = "" + s1.trim();
		
			String s3 = "" + e3.getText();
			s1 = "" + s1.trim();
		
			String s4 = "" + e4.getText();
			s1 = "" + s1.trim();
		
	
			String sd12 = "" + d12.getText();
			sd12 = "" + sd12.trim();
		
			String sd13 = "" + d13.getText();
			sd13 = "" + sd13.trim();

	
			String sd22 = "" + d22.getText();
			sd22 = "" + sd22.trim();
		
			String sd23 = "" + d23.getText();
			sd23 = "" + sd23.trim();
		
			boolean students  = c1.getChecked ();
			boolean faculty  = c2.getChecked ();		
		
			boolean filled = true;
		
			if (s1.equals ("")) { filled = false; }
			if (s2.equals ("")) { filled = false; }
			if (s3.equals ("")) { filled = false; }
			if (s4.equals ("")) { filled = false; }
		
			if (sd12.equals ("")) { filled = false; }
			if (sd13.equals ("")) { filled = false; }
		
			if (sd22.equals ("")) { filled = false; }
			if (sd23.equals ("")) { filled = false; }
		
		
			if (!filled) 
			{
				EError newError1 = new EError(6,"Required Data Field Missing");
				ErrorForm newForm1 = new ErrorForm (newError1);
				newForm1.showDialog ();				
			} else
			{
				TheEvent.setEventTitle (s1);
				TheEvent.setDescription (s2);
				TheEvent.setAuthor (s3);
				TheEvent.setPassword (s4);
				TheEvent.setStudents (students);
				TheEvent.setFaculty (faculty);			
				
				Date d1 = new Date();
				Date d2 = new Date();			
				filled = true;
				
				d1.setMonth (d11.getSelectedIndex());
				d2.setMonth (d21.getSelectedIndex());			
				
	
				String g;
				int val;
				
				g = "" + d13.getText ();						
				try {
					val = Integer.parseInt(g.trim());	
					if (val < 90) {val = val + 100;}					
					d1.setYear(val);								
				} catch (NumberFormatException expt)
				{ filled = false; }

				g = "" + d12.getText ();						
				try {
					val = Integer.parseInt(g.trim());					
					d1.setDate(val);								
					if (val>31) { filled = false; }
				} catch (NumberFormatException expt)
				{ filled = false; }
				
				
				
				g = "" + d23.getText ();						
				try {
					val = Integer.parseInt(g.trim());	
					if (val < 90) {val = val + 100;}					
					d2.setYear(val);								
				} catch (NumberFormatException expt)
				{ filled = false; }

				g = "" + d22.getText ();						
				try {
					val = Integer.parseInt(g.trim());					
					d2.setDate(val);								
					if (val>31) { filled = false; }
				} catch (NumberFormatException expt)
				{ filled = false; }
				
				if (d2.before(d1)) { filled = false; }
				
  		        if (d1.getMonth() != d11.getSelectedIndex()) { filled = false; }
			    if (d2.getMonth() != d21.getSelectedIndex()) { filled = false; }

				
				if (filled)
				{
					TheEvent.setTheDate (d1);
					TheEvent.setExpirationDate (d2);				
					TheEvent.setID (0);
					this.hide();
					this.dispose();			
				}
				 else
				{
					if (d2.before(d1))
					{
						EError newError1 = new EError(11,"Event Date can not be after Expiration Date.");
						ErrorForm newForm1 = new ErrorForm (newError1);
						newForm1.showDialog ();						
					} else
					{
						EError newError1 = new EError(7,"Invalid Date");
						ErrorForm newForm1 = new ErrorForm (newError1);
						newForm1.showDialog ();						
					}
				}					
					
			}			
		}

	}

	private void c2_click(Object sender, Event e)
	{
		c2.setChecked(!c2.getChecked());
	}

	private void c1_click(Object sender, Event e)
	{
		c1.setChecked(!c1.getChecked());
	}

	EEvent TheEvent;	
	DateTools DTools = new DateTools();
	
	private void button1_click(Object sender, Event e)
	{
		TheEvent.setID (-1);
		this.hide();
		this.dispose ();
	}

	public ModifyEventForm()
	{
		initForm();		
	}
	
	public ModifyEventForm(EEvent event)
	{	
		TheEvent = event;
		
		initForm();		
		e1.setText (event.getEventTitle () );
		e2.setText (event.getDescription () );
		e3.setText (event.getAuthor () );		
				
		d11.setSelectedIndex (event.getTheDate().getMonth());
		d21.setSelectedIndex (event.getExpirationDate().getMonth());
		
		d12.setText ("" + event.getTheDate ().getDate());
		d22.setText ("" + event.getExpirationDate ().getDate());			
		
		String years;		
		int year;
		year = event.getTheDate ().getYear ();		
		if (year >99) {year = year -100;}
		if (year>9)
		{ years = "" + year; }
		else
		{ years = "0" + year; }
		d13.setText (years);
		
		year = event.getExpirationDate ().getYear();
		if (year >99) {year = year -100;}
		if (year>9)
		{ years = "" + year; }
		else
		{ years = "0" + year; }
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
		Application.run(new ModifyEventForm());
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
	Edit d22 = new Edit();
	Edit d23 = new Edit();
	Label label11 = new Label();
	Label label12 = new Label();
	Label label13 = new Label();
	Edit e2 = new Edit();
	ComboBox d21 = new ComboBox();
	ComboBox d11 = new ComboBox();
	Button button2 = new Button();
	Edit e4 = new Edit();
	Label label8 = new Label();

	private void initForm()
	{

		this.setBackColor(Color.CONTROL);
		this.setLocation(new Point(0, 0));
		this.setSize(new Point(508, 337));
		this.setTabIndex(-1);
		this.setTabStop(true);
		this.setText("Update Event");
		this.setAutoScaleBaseSize(16);
		this.setBorderStyle(FormBorderStyle.FIXED_3D);
		this.setClientSize(new Point(502, 312));
		this.setControlBox(false);
		this.setShowInTaskbar(false);
		this.addOnClick(new EventHandler(this.ModifyEventForm_click));
		button1.setLocation(new Point(380, 280));
		button1.setSize(new Point(110, 25));
		button1.setTabIndex(13);
		button1.setTabStop(true);
		button1.setText("Cancel");
		button1.addOnClick(new EventHandler(this.button1_click));
		c1.setLocation(new Point(295, 145));
		c1.setSize(new Point(190, 20));
		c1.setTabIndex(8);
		c1.setTabStop(true);
		c1.setText("Students Related");
		c1.setChecked(true);
		c1.setCheckState(CheckState.CHECKED);
		c2.setLocation(new Point(295, 165));
		c2.setSize(new Point(190, 20));
		c2.setTabIndex(9);
		c2.setTabStop(true);
		c2.setText("Faculty Ralated");
		e1.setBackColor(Color.WHITE);
		e1.setCursor(Cursor.IBEAM);
		e1.setLocation(new Point(5, 25));
		e1.setSize(new Point(280, 23));
		e1.setTabIndex(0);
		e1.setTabStop(true);
		e1.setText("");
		label2.setLocation(new Point(5, 5));
		label2.setSize(new Point(280, 20));
		label2.setText("Event Title:");
		label2.setTabIndex(14);
		label3.setLocation(new Point(5, 55));
		label3.setSize(new Point(280, 20));
		label3.setText("Event Description:");
		label3.setTabIndex(15);
		d12.setBackColor(Color.WHITE);
		d12.setCursor(Cursor.IBEAM);
		d12.setLocation(new Point(395, 25));
		d12.setSize(new Point(40, 23));
		d12.setTabIndex(2);
		d12.setTabStop(true);
		d12.setText("");
		d12.setMaxLength(2);
		d13.setBackColor(Color.WHITE);
		d13.setCursor(Cursor.IBEAM);
		d13.setLocation(new Point(435, 25));
		d13.setSize(new Point(50, 23));
		d13.setTabIndex(3);
		d13.setTabStop(true);
		d13.setText("");
		d13.setMaxLength(2);
		label4.setLocation(new Point(295, 5));
		label4.setSize(new Point(100, 20));
		label4.setText("Month");
		label4.setTabIndex(16);
		label5.setLocation(new Point(395, 5));
		label5.setSize(new Point(40, 20));
		label5.setText("Day");
		label5.setTabIndex(17);
		label6.setLocation(new Point(435, 5));
		label6.setSize(new Point(40, 20));
		label6.setText("Year");
		label6.setTabIndex(18);
		e3.setBackColor(Color.WHITE);
		e3.setCursor(Cursor.IBEAM);
		e3.setLocation(new Point(5, 250));
		e3.setSize(new Point(280, 23));
		e3.setTabIndex(10);
		e3.setTabStop(true);
		e3.setText("");
		label7.setLocation(new Point(5, 230));
		label7.setSize(new Point(280, 20));
		label7.setText("Author\'s Name:");
		label7.setTabIndex(20);
		label10.setLocation(new Point(295, 75));
		label10.setSize(new Point(190, 20));
		label10.setText("Event Expiration Date:");
		label10.setTabIndex(21);
		d22.setBackColor(Color.WHITE);
		d22.setCursor(Cursor.IBEAM);
		d22.setLocation(new Point(395, 115));
		d22.setSize(new Point(40, 23));
		d22.setTabIndex(6);
		d22.setTabStop(true);
		d22.setText("");
		d22.setMaxLength(2);
		d23.setBackColor(Color.WHITE);
		d23.setCursor(Cursor.IBEAM);
		d23.setLocation(new Point(435, 115));
		d23.setSize(new Point(50, 23));
		d23.setTabIndex(7);
		d23.setTabStop(true);
		d23.setText("");
		d23.setMaxLength(2);
		label11.setLocation(new Point(295, 95));
		label11.setSize(new Point(80, 20));
		label11.setText("Month");
		label11.setTabIndex(22);
		label12.setLocation(new Point(395, 95));
		label12.setSize(new Point(40, 20));
		label12.setText("Day");
		label12.setTabIndex(23);
		label13.setLocation(new Point(435, 95));
		label13.setSize(new Point(40, 20));
		label13.setText("Year");
		label13.setTabIndex(24);
		e2.setBackColor(Color.WHITE);
		e2.setCursor(Cursor.IBEAM);
		e2.setLocation(new Point(5, 75));
		e2.setSize(new Point(280, 120));
		e2.setTabIndex(4);
		e2.setTabStop(true);
		e2.setText("");
		e2.setMultiline(true);
		e2.setScrollBars(ScrollBars.VERTICAL);
		d21.setLocation(new Point(295, 115));
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
		d11.setLocation(new Point(295, 25));
		d11.setSize(new Point(100, 24));
		d11.setTabIndex(1);
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
		button2.setLocation(new Point(260, 280));
		button2.setSize(new Point(110, 25));
		button2.setTabIndex(12);
		button2.setTabStop(true);
		button2.setText("Update");
		button2.addOnClick(new EventHandler(this.button2_click));
		e4.setBackColor(Color.WINDOW);
		e4.setCursor(Cursor.IBEAM);
		e4.setLocation(new Point(300, 250));
		e4.setSize(new Point(190, 23));
		e4.setTabIndex(11);
		e4.setTabStop(true);
		e4.setText("");
		e4.addOnChange(new EventHandler(this.e4_change));
		label8.setLocation(new Point(300, 230));
		label8.setSize(new Point(190, 20));
		label8.setText("Enter your User Password");
		label8.setTabIndex(19);
		this.setNewControls(new Control[] {
			d21, 
			d11, 
			label8, 
			e4, 
			button2, 
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
