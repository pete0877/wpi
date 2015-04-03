// ErrorForm.java

import wfc.app.*;
import wfc.core.*;
import wfc.ui.*;

/**
* This class can take a variable number of parameters on the command
* line. Program execution begins with the main() method. The class
* constructor is not invoked unless an object of type 'ErrorForm'
* created in the main() method.
*/
public class ErrorForm extends Form
{
	private void button1_click(Object sender, Event e)
	{
		this.hide();
		this.dispose ();
	}

	public ErrorForm()
	{
		initForm();		
	}

	public ErrorForm(EError e1)
	{
		initForm();		
		this.EN.setText ("" + e1.getEErrorNumber ());
		this.ED.setText (e1.getEErrorDescription ());
	}
	
	public ErrorForm(String n1, String d1)
	{
		initForm();		
		this.EN.setText (n1);
		this.ED.setText (d1);
	}	
	
	/**
	 * The main entry point for the application. 
	 *
	 * @param args Array of parameters passed to the application
	 * via the command line.
	 */
	public static void main(String args[])
	{
		Application.run(new ErrorForm());
	}


	/**
	* NOTE: The following code is required by the Visual J++ form
	* designer.  It can be modified using the form editor.  Do not
	* modify it using the code editor.
	*/

	Container components = new Container();
	Label label1 = new Label();
	Label label2 = new Label();
	Button button1 = new Button();
	Label EN = new Label();
	Label ED = new Label();

	private void initForm()
	{

		this.setBackColor(Color.CONTROL);
		this.setLocation(new Point(0, 0));
		this.setSize(new Point(416, 154));
		this.setTabIndex(-1);
		this.setTabStop(true);
		this.setText("ECON Client Error");
		this.setAutoScaleBaseSize(16);
		this.setBorderStyle(FormBorderStyle.FIXED_3D);
		this.setClientSize(new Point(410, 129));
		this.setControlBox(false);
		this.setShowInTaskbar(false);
		label1.setLocation(new Point(10, 10));
		label1.setSize(new Point(120, 30));
		label1.setText("Error number:");
		label1.setTabIndex(1);
		label1.setAlignment(HorizontalAlignment.RIGHT);
		label2.setLocation(new Point(10, 40));
		label2.setSize(new Point(120, 30));
		label2.setText("Error description:");
		label2.setTabIndex(0);
		label2.setAlignment(HorizontalAlignment.RIGHT);
		button1.setLocation(new Point(150, 100));
		button1.setSize(new Point(120, 25));
		button1.setTabIndex(2);
		button1.setTabStop(true);
		button1.setText("Ok");
		button1.addOnClick(new EventHandler(this.button1_click));
		EN.setLocation(new Point(140, 10));
		EN.setSize(new Point(260, 20));
		EN.setText("");
		EN.setTabIndex(5);
		ED.setLocation(new Point(140, 40));
		ED.setSize(new Point(260, 50));
		ED.setText("");
		ED.setTabIndex(3);
		this.setNewControls(new Control[] {
			ED, 
			EN, 
			button1, 
			label2, 
			label1});
	}
	// NOTE: End of form designer support code

	public static class ClassInfo extends Form.ClassInfo
	{
		// TODO: Add your property and event infos here
	}
}
