// Message.java

import wfc.app.*;
import wfc.core.*;
import wfc.ui.*;

/**
* This class can take a variable number of parameters on the command
* line. Program execution begins with the main() method. The class
* constructor is not invoked unless an object of type 'Message'
* created in the main() method.
*/
public class Message extends Form
{
	private void button1_click(Object sender, Event e)
	{
		this.hide();
		this.dispose ();
	}

	public Message()
	{
		// Required for Visual J++ Form Designer support
		initForm();		

		// TODO: Add any constructor code after initForm call
	}
	
	public Message(String msg)
	{		
		initForm();		
		e1.setText (msg);		
	}

	/**
	 * The main entry point for the application. 
	 *
	 * @param args Array of parameters passed to the application
	 * via the command line.
	 */
	public static void main(String args[])
	{
		Application.run(new Message());
	}


	/**
	* NOTE: The following code is required by the Visual J++ form
	* designer.  It can be modified using the form editor.  Do not
	* modify it using the code editor.
	*/

	Container components = new Container();
	Button button1 = new Button();
	Label e1 = new Label();

	private void initForm()
	{

		this.setBackColor(Color.CONTROL);
		this.setLocation(new Point(0, 0));
		this.setSize(new Point(389, 93));
		this.setTabIndex(-1);
		this.setTabStop(true);
		this.setText("User Message");
		this.setAutoScaleBaseSize(16);
		this.setBorderStyle(FormBorderStyle.FIXED_3D);
		this.setClientSize(new Point(383, 68));
		this.setControlBox(false);
		this.setShowInTaskbar(false);
		button1.setLocation(new Point(135, 40));
		button1.setSize(new Point(120, 25));
		button1.setTabIndex(0);
		button1.setTabStop(true);
		button1.setText("Ok");
		button1.addOnClick(new EventHandler(this.button1_click));
		e1.setFont(new Font("MS Sans Serif", -13, 500, false, false, false, 0, 0));
		e1.setLocation(new Point(10, 10));
		e1.setSize(new Point(360, 30));
		e1.setText("");
		e1.setTabIndex(2);
		e1.setAlignment(HorizontalAlignment.CENTER);
		this.setNewControls(new Control[] {
			e1, 
			button1});
	}
	// NOTE: End of form designer support code

	public static class ClassInfo extends Form.ClassInfo
	{
		// TODO: Add your property and event infos here
	}
}
