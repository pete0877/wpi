// AboutForm.java

import wfc.app.*;
import wfc.core.*;
import wfc.ui.*;

/**
* This class can take a variable number of parameters on the command
* line. Program execution begins with the main() method. The class
* constructor is not invoked unless an object of type 'AboutForm'
* created in the main() method.
*/
public class AboutForm extends Form
{
	private void button1_click(Object sender, Event e)
	{
		this.hide();
		this.dispose ();
	}

	public AboutForm()
	{
		// Required for Visual J++ Form Designer support
		initForm();		

		// TODO: Add any constructor code after initForm call
	}

	/**
	 * The main entry point for the application. 
	 *
	 * @param args Array of parameters passed to the application
	 * via the command line.
	 */
	public static void main(String args[])
	{
		Application.run(new AboutForm());
	}


	/**
	* NOTE: The following code is required by the Visual J++ form
	* designer.  It can be modified using the form editor.  Do not
	* modify it using the code editor.
	*/

	Container components = new Container();
	Label label1 = new Label();
	Label label2 = new Label();
	Label label3 = new Label();
	Button button1 = new Button();

	private void initForm()
	{

		this.setBackColor(Color.CONTROL);
		this.setLocation(new Point(0, 0));
		this.setSize(new Point(267, 163));
		this.setTabIndex(-1);
		this.setTabStop(true);
		this.setText("About ECON");
		this.setAutoScaleBaseSize(16);
		this.setBorderStyle(FormBorderStyle.FIXED_3D);
		this.setClientSize(new Point(261, 138));
		this.setControlBox(false);
		this.setShowInTaskbar(false);
		label1.setFont(new Font("Arial", -25, 700, false, false, false, 0, 0));
		label1.setLocation(new Point(10, 10));
		label1.setSize(new Point(160, 40));
		label1.setText("ECON Client");
		label1.setTabIndex(0);
		label2.setLocation(new Point(10, 80));
		label2.setSize(new Point(240, 20));
		label2.setText("Version 1.0 - Freeware");
		label2.setTabIndex(2);
		label3.setLocation(new Point(10, 50));
		label3.setSize(new Point(240, 20));
		label3.setText("by Deep Ocean Software, 1998");
		label3.setTabIndex(1);
		button1.setLocation(new Point(60, 110));
		button1.setSize(new Point(140, 25));
		button1.setTabIndex(4);
		button1.setTabStop(true);
		button1.setText("Close");
		button1.addOnClick(new EventHandler(this.button1_click));
		this.setNewControls(new Control[] {
			button1, 
			label3, 
			label2, 
			label1});
	}
	// NOTE: End of form designer support code

	public static class ClassInfo extends Form.ClassInfo
	{
		// TODO: Add your property and event infos here
	}
}
