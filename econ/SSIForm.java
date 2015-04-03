// TestForm.java

import wfc.app.*;
import wfc.core.*;
import wfc.ui.*;

/**
* This class can take a variable number of parameters on the command
* line. Program execution begins with the main() method. The class
* constructor is not invoked unless an object of type 'TestForm'
* created in the main() method.
*/
public class SSIForm extends Form
{
	private void button1_click(Object sender, Event e)
	{
		this.hide();
		this.dispose();		
	}

	public SSIForm(EServerProfile s1)
	{
		initForm();				
		b1.setText(s1.getDescription());		
		b2.setText(s1.getComments());		
		b3.setText(s1.getAdminName ());		
		b4.setText(s1.getAdminTelNumber ());		
		b5.setText(s1.getAdminEMail ());		
	}
	
	public SSIForm()
	{
		initForm();			
	}

	public static void main(String args[])
	{
		Application.run(new SSIForm());
	}


	/**
	* NOTE: The following code is required by the Visual J++ form
	* designer.  It can be modified using the form editor.  Do not
	* modify it using the code editor.
	*/

	Container components = new Container();
	Label label3 = new Label();
	Edit b3 = new Edit();
	Label label1 = new Label();
	Edit b1 = new Edit();
	Edit b2 = new Edit();
	Label label2 = new Label();
	Label label4 = new Label();
	Edit b4 = new Edit();
	Label label5 = new Label();
	Edit b5 = new Edit();
	Button button1 = new Button();

	private void initForm()
	{

		this.setBackColor(Color.CONTROL);
		this.setLocation(new Point(0, 0));
		this.setSize(new Point(422, 307));
		this.setTabIndex(-1);
		this.setTabStop(true);
		this.setText("Server Profile");
		this.setAutoScaleBaseSize(16);
		this.setClientSize(new Point(414, 280));
		this.setControlBox(false);
		this.setShowInTaskbar(false);
		label3.setLocation(new Point(10, 180));
		label3.setSize(new Point(190, 20));
		label3.setText("Administrator\'s Name");
		label3.setTabIndex(7);
		b3.setBackColor(Color.WINDOW);
		b3.setCursor(Cursor.IBEAM);
		b3.setLocation(new Point(10, 200));
		b3.setSize(new Point(190, 23));
		b3.setTabIndex(4);
		b3.setTabStop(false);
		b3.setText("");
		b3.setAutoSize(false);
		b3.setBorderStyle(BorderStyle.FIXED_SINGLE);
		b3.setReadOnly(true);
		label1.setLocation(new Point(10, 10));
		label1.setSize(new Point(380, 20));
		label1.setText("Server Description");
		label1.setTabIndex(8);
		b1.setBackColor(Color.WINDOW);
		b1.setCursor(Cursor.IBEAM);
		b1.setLocation(new Point(10, 30));
		b1.setSize(new Point(390, 23));
		b1.setTabIndex(9);
		b1.setTabStop(false);
		b1.setText("");
		b1.setAutoSize(false);
		b1.setBorderStyle(BorderStyle.FIXED_SINGLE);
		b1.setReadOnly(true);
		b2.setBackColor(Color.WINDOW);
		b2.setCursor(Cursor.IBEAM);
		b2.setLocation(new Point(10, 80));
		b2.setSize(new Point(390, 90));
		b2.setTabIndex(3);
		b2.setTabStop(false);
		b2.setText("");
		b2.setAutoSize(false);
		b2.setBorderStyle(BorderStyle.FIXED_SINGLE);
		b2.setMultiline(true);
		b2.setReadOnly(true);
		b2.setScrollBars(ScrollBars.VERTICAL);
		label2.setLocation(new Point(10, 60));
		label2.setSize(new Point(380, 20));
		label2.setText("Comments and Rules");
		label2.setTabIndex(0);
		label4.setLocation(new Point(10, 230));
		label4.setSize(new Point(190, 20));
		label4.setText("Administrator\'s Phone");
		label4.setTabIndex(5);
		b4.setBackColor(Color.WINDOW);
		b4.setCursor(Cursor.IBEAM);
		b4.setLocation(new Point(10, 250));
		b4.setSize(new Point(190, 23));
		b4.setTabIndex(6);
		b4.setTabStop(false);
		b4.setText("");
		b4.setAutoSize(false);
		b4.setBorderStyle(BorderStyle.FIXED_SINGLE);
		b4.setReadOnly(true);
		label5.setLocation(new Point(210, 180));
		label5.setSize(new Point(190, 20));
		label5.setText("Administrator\'s Email");
		label5.setTabIndex(1);
		b5.setBackColor(Color.WINDOW);
		b5.setCursor(Cursor.IBEAM);
		b5.setLocation(new Point(210, 200));
		b5.setSize(new Point(190, 23));
		b5.setTabIndex(2);
		b5.setTabStop(false);
		b5.setText("");
		b5.setAutoSize(false);
		b5.setBorderStyle(BorderStyle.FIXED_SINGLE);
		b5.setReadOnly(true);
		button1.setLocation(new Point(275, 250));
		button1.setSize(new Point(130, 25));
		button1.setTabIndex(11);
		button1.setTabStop(true);
		button1.setText("Close");
		button1.addOnClick(new EventHandler(this.button1_click));
		this.setNewControls(new Control[] {
			button1, 
			b5, 
			label5, 
			b4, 
			label4, 
			label2, 
			b2, 
			b1, 
			label1, 
			label3, 
			b3});
	}
	// NOTE: End of form designer support code

	public static class ClassInfo extends Form.ClassInfo
	{
		// Class properties
	}
}
