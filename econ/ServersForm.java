// ServersForm.java

import wfc.app.*;
import wfc.core.*;
import wfc.ui.*;
import java.net.*;
import java.io.*;	

/**
* This class can take a variable number of parameters on the command
* line. Program execution begins with the main() method. The class
* constructor is not invoked unless an object of type 'ServersForm'
* created in the main() method.
*/
public class ServersForm extends Form
{
	EServerList Servers;
	int ServerPort; 
	
	private void button3_click(Object sender, Event e)
	{
		String s0 = ""+e2.getText ();
		s0 = s0.trim ();
		if (s0.equals ("")) 
		{
			ErrorForm newForm1 = new ErrorForm("E012", "Please enter the Server Address.");
			newForm1.showDialog ();			
		} else {
			// Here we need to check if the server is online, 
			// and get SSI
			EServerProfile ser0 = new EServerProfile ();
						 
				Socket socket = null;
				EError error1 = new EError(0,"");
				ObjectInputStream is = null;
				ObjectOutputStream os= null;
				boolean serverAdded = false;
				String DNS;
				
				DNS = e2.getText();
				
				try {
					
					socket = new Socket(DNS, ServerPort);
					os = new ObjectOutputStream(socket.getOutputStream());
					is = new ObjectInputStream(socket.getInputStream());
					Integer cmd1 = new Integer(4);
					os.writeObject(cmd1);					
					os.flush();
					
					error1 = (EError) is.readObject();					
					
					if (error1.getEErrorNumber ()!= 0) 
					{						
						ErrorForm form7 = new ErrorForm(error1);
						form7.showDialog ();						
					} else
					{

						ser0 = (EServerProfile) is.readObject();						
						ser0.setDNSname (DNS);
						serverAdded = true;
					}							
					socket.close();
				}
				catch (Exception netexep)
				{
					EError newError1 = new EError(1,"Unable to Establish a Network Connection");
					ErrorForm newForm8 = new ErrorForm (newError1);
					newForm8.showDialog();										
				}				
				
				
				if (serverAdded) 
				{
			
					e1.addItem (ser0.getDescription ());
			
					e2.setText ("");
			
					Servers.addServer (ser0);
					if (Servers.getSize ()==1)
					{
						Servers.setCurrentServerIndex (0);	
						e1.setSelectedIndex (0);
					}						
			
					Servers.setChanged (true);
					
				}
			
		}
	}

	private void button2_click(Object sender, Event e)
	{
		if (e1.getItemCount() == 0)
		{
			Message msg1 = new Message("No server selected.");		
			msg1.showDialog();	
		} else {
			
			if (Servers.getCurrentServerIndex() == e1.getSelectedIndex ())
			{
				Servers.setChanged(true);			
				Servers.setCurrentServerIndex(0);	
			}
			Servers.deleteServer (e1.getSelectedIndex ());				
			e1.removeItem (e1.getSelectedIndex ());
			Message msg1 = new Message("Server deleted.");		
			msg1.showDialog();				
			if (Servers.getSize ()>0) 
			{
				e1.setSelectedIndex (Servers.getCurrentServerIndex ());		
			}
		}
	}
	
	private void button1_click(Object sender, Event e)
	{		
		if (e1.getItemCount() == 0)
		{
			Message msg1 = new Message("No server selected.");		
			msg1.showDialog();	
		} else {
			Servers.setCurrentServerIndex (e1.getSelectedIndex());
			Message msg1 = new Message("Server set as current.");		
			msg1.showDialog();
			Servers.setChanged(true);
		}
	}

	private void button4_click(Object sender, Event e)
	{
		this.hide();
		this.dispose ();
	}

	public ServersForm()
	{		
		initForm();
		Servers.setChanged (false);
	}

	public ServersForm(EServerList ser, int port)
	{	
		ServerPort = port;
		Servers = ser;
		Servers.setChanged(false);
		initForm();		
		int a;
		String des = new String();
		e1.removeAll ();
		for (a=0; a<Servers.getSize(); a++)
		{
			des = Servers.getServer(a).getDescription ();
			e1.addItem (des);
		}
		if (Servers.getSize ()>0) 
		{
			e1.setSelectedIndex (Servers.getCurrentServerIndex ());		
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
		Application.run(new ServersForm());
	}


	/**
	* NOTE: The following code is required by the Visual J++ form
	* designer.  It can be modified using the form editor.  Do not
	* modify it using the code editor.
	*/

	Container components = new Container();
	Edit e2 = new Edit();
	ListBox e1 = new ListBox();
	Button button1 = new Button();
	Button button2 = new Button();
	Button button3 = new Button();
	Button button4 = new Button();
	Label label1 = new Label();

	private void initForm()
	{

		this.setBackColor(Color.CONTROL);
		this.setLocation(new Point(0, 0));
		this.setSize(new Point(490, 209));
		this.setTabIndex(-1);
		this.setTabStop(true);
		this.setText("ECON Servers");
		this.setAutoScaleBaseSize(16);
		this.setClientSize(new Point(482, 182));
		this.setControlBox(false);
		this.setShowInTaskbar(false);
		e2.setBackColor(Color.WINDOW);
		e2.setCursor(Cursor.IBEAM);
		e2.setLocation(new Point(310, 120));
		e2.setSize(new Point(160, 23));
		e2.setTabIndex(0);
		e2.setTabStop(true);
		e2.setText("");
		e1.setLocation(new Point(10, 10));
		e1.setSize(new Point(290, 166));
		e1.setTabIndex(5);
		e1.setTabStop(false);
		e1.setIntegralHeight(true);
		e1.setItemHeight(16);
		e1.setItems(new Object[] {});
		button1.setLocation(new Point(310, 10));
		button1.setSize(new Point(160, 25));
		button1.setTabIndex(3);
		button1.setTabStop(true);
		button1.setText("Set as Current");
		button1.addOnClick(new EventHandler(this.button1_click));
		button2.setLocation(new Point(310, 40));
		button2.setSize(new Point(160, 25));
		button2.setTabIndex(2);
		button2.setTabStop(true);
		button2.setText("Delete Server");
		button2.addOnClick(new EventHandler(this.button2_click));
		button3.setLocation(new Point(310, 70));
		button3.setSize(new Point(160, 25));
		button3.setTabIndex(1);
		button3.setTabStop(true);
		button3.setText("Add Server");
		button3.addOnClick(new EventHandler(this.button3_click));
		button4.setLocation(new Point(310, 150));
		button4.setSize(new Point(160, 25));
		button4.setTabIndex(4);
		button4.setTabStop(true);
		button4.setText("Close");
		button4.addOnClick(new EventHandler(this.button4_click));
		label1.setLocation(new Point(310, 100));
		label1.setSize(new Point(160, 20));
		label1.setText("Server Address:");
		label1.setTabIndex(6);
		this.setNewControls(new Control[] {
			label1, 
			button4, 
			button3, 
			button2, 
			button1, 
			e1, 
			e2});
	}
	// NOTE: End of form designer support code

	public static class ClassInfo extends Form.ClassInfo
	{
		// TODO: Add your property and event infos here
	}
}
