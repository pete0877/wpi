// Imported classes:
import java.util.Date;					 
import java.lang.Integer;
import java.util.Vector;
import java.util.Enumeration;
import wfc.app.*;
import wfc.core.*;
import wfc.ui.*;

public class FindForm extends Form
{
	DateTools DTools = new DateTools();	
	ECalendar calendar;
	
	private void button1_click(Object sender, Event e)
	{
		this.hide();
		this.dispose();
	}

	private void setDomain(Object sender, Event e)
	{
		RadioButton rad;
		r1.setChecked (false);
		r2.setChecked (false);
		r3.setChecked (false);
		r4.setChecked (false);
		r5.setChecked (false);		
		rad = (RadioButton) sender;
		rad.setChecked (true);		
	}

	private void button2_click(Object sender, Event e)
	{
		edit1.setText(edit1.getText().trim());
		String key = edit1.getText();
		if (key.equals(""))
		{
			Message msg1 = new Message("First enter the keyword.");			
			msg1.showDialog ();			
		}
		else
		{
			int mode=0;
			Date d0 = new Date();
			if (r1.getChecked()) { mode = 0; }
			if (r2.getChecked()) { mode = 1; }
			if (r3.getChecked()) { mode = 2; }
			if (r4.getChecked()) { mode = 1; d0 = calendar.getCDate (); }
			if (r5.getChecked()) { mode = 2; d0 = calendar.getCDate (); }
			ListFoundEventsForm FoudnE = new ListFoundEventsForm(
				calendar.getFoundEvents(
					key,
					mode,
					d0,
					this.c1.getChecked(), 
					this.c2.getChecked()
				)
			);			
			FoudnE.showDialog ();	
			
		}
	}

	private void c2_click(Object sender, Event e)
	{
		c1.setChecked (true);
	}

	private void changec1(Object sender, Event e)
	{
		c2.setChecked (true);
	}

	public FindForm()
	{		
		initForm();
	}
	
	public FindForm(ECalendar cal)
	{		
		calendar = cal;
		
		initForm();
		
		// Display the current calendar date on the screen
		String years = new String();		
		int year = calendar.getCDate ().getYear();		
		if (year >=100) { year = year - 100; }
		if (year >9) 
		{ 	years = " '" + year;	} 		else
		{ 	years = " '0" + year;	}
		this.shown.setText("" + DTools.getMonthName(calendar.getCDate ().getMonth ())+ years);
	}
	
	public static void main(String args[])
	{
		Application.run(new FindForm());
	}


	/**
	* NOTE: The following code is required by the Visual J++ form
	* designer.  It can be modified using the form editor.  Do not
	* modify it using the code editor.
	*/

	Container components = new Container();
	Button button1 = new Button();
	Label shown = new Label();
	Label label4 = new Label();
	Label label1 = new Label();
	Edit edit1 = new Edit();
	Label label2 = new Label();
	RadioButton r1 = new RadioButton();
	RadioButton r2 = new RadioButton();
	RadioButton r3 = new RadioButton();
	RadioButton r4 = new RadioButton();
	RadioButton r5 = new RadioButton();
	Checkbox c1 = new Checkbox();
	Checkbox c2 = new Checkbox();
	Label label3 = new Label();
	Button button2 = new Button();

	private void initForm()
	{

		this.setBackColor(Color.CONTROL);
		this.setLocation(new Point(0, 0));
		this.setSize(new Point(341, 217));
		this.setTabIndex(-1);
		this.setTabStop(true);
		this.setText("Find Events");
		this.setAutoScaleBaseSize(16);
		this.setBorderStyle(FormBorderStyle.FIXED_3D);
		this.setClientSize(new Point(335, 192));
		this.setControlBox(false);
		this.setShowInTaskbar(false);
		button1.setLocation(new Point(220, 160));
		button1.setSize(new Point(110, 25));
		button1.setTabIndex(2);
		button1.setTabStop(true);
		button1.setText("Cancel");
		button1.addOnClick(new EventHandler(this.button1_click));
		shown.setLocation(new Point(215, 0));
		shown.setSize(new Point(110, 25));
		shown.setText("");
		shown.setTabIndex(13);
		label4.setLocation(new Point(100, 0));
		label4.setSize(new Point(100, 25));
		label4.setText("Shown month:");
		label4.setTabIndex(14);
		label4.setAlignment(HorizontalAlignment.RIGHT);
		label1.setLocation(new Point(5, 10));
		label1.setSize(new Point(85, 20));
		label1.setText("Keyword:");
		label1.setTabIndex(10);
		edit1.setBackColor(Color.WINDOW);
		edit1.setCursor(Cursor.IBEAM);
		edit1.setLocation(new Point(5, 30));
		edit1.setSize(new Point(325, 23));
		edit1.setTabIndex(0);
		edit1.setTabStop(true);
		edit1.setText("");
		label2.setLocation(new Point(5, 60));
		label2.setSize(new Point(185, 20));
		label2.setText("Look through:");
		label2.setTabIndex(12);
		r1.setLocation(new Point(10, 80));
		r1.setSize(new Point(180, 20));
		r1.setTabIndex(3);
		r1.setTabStop(true);
		r1.setText("Entire calendar");
		r1.setChecked(true);
		r1.addOnClick(new EventHandler(this.setDomain));
		r2.setLocation(new Point(10, 100));
		r2.setSize(new Point(180, 20));
		r2.setTabIndex(4);
		r2.setTabStop(false);
		r2.setText("This year");
		r3.setLocation(new Point(10, 120));
		r3.setSize(new Point(180, 20));
		r3.setTabIndex(5);
		r3.setTabStop(false);
		r3.setText("This month");
		r4.setLocation(new Point(10, 145));
		r4.setSize(new Point(180, 20));
		r4.setTabIndex(6);
		r4.setTabStop(false);
		r4.setText("Currenlty shown year");
		r5.setLocation(new Point(10, 165));
		r5.setSize(new Point(180, 20));
		r5.setTabIndex(7);
		r5.setTabStop(false);
		r5.setText("Currenlty shown month");
		c1.setLocation(new Point(200, 80));
		c1.setSize(new Point(135, 20));
		c1.setTabIndex(8);
		c1.setTabStop(false);
		c1.setText("Students Related");
		c1.setChecked(true);
		c1.setCheckState(CheckState.CHECKED);
		c1.addOnClick(new EventHandler(this.changec1));
		c2.setLocation(new Point(200, 100));
		c2.setSize(new Point(135, 20));
		c2.setTabIndex(9);
		c2.setTabStop(false);
		c2.setText("Faculty Ralated");
		c2.setChecked(true);
		c2.setCheckState(CheckState.CHECKED);
		c2.addOnClick(new EventHandler(this.c2_click));
		label3.setLocation(new Point(195, 60));
		label3.setSize(new Point(140, 20));
		label3.setText("Look for events:");
		label3.setTabIndex(11);
		button2.setLocation(new Point(220, 130));
		button2.setSize(new Point(110, 25));
		button2.setTabIndex(1);
		button2.setTabStop(true);
		button2.setText("Ok");
		button2.addOnClick(new EventHandler(this.button2_click));
		this.setNewControls(new Control[] {
			button2, 
			label3, 
			c2, 
			c1, 
			r5, 
			r4, 
			r3, 
			r2, 
			r1, 
			label2, 
			edit1, 
			label1, 
			button1, 
			shown, 
			label4});
	}

	public static class ClassInfo extends Form.ClassInfo
	{}
}
