// Class  : DeleteOptionsDialog
// Author : Peter Golaszewski
//        : peterg@wpi.edu
// Version: 1.0

import java.awt.*;

public class DeleteOptionsDialog extends Dialog
{
    int option;
    public final int CANCEL = 0;
    public final int OK = 1;
    public final int OK_ALL_INSTANCES = 2;
    
	public DeleteOptionsDialog(Frame parent)
	{
		super(parent);

		// This code is automatically generated by Visual Cafe when you add
		// components to the visual environment. It instantiates and initializes
		// the components. To modify the code, only use code syntax that matches
		// what Visual Cafe can generate, or Visual Cafe may be unable to back
		// parse your Java file into its visual environment.
		//{{INIT_CONTROLS
		setLayout(null);
		setBackground(new java.awt.Color(255,255,221));
		setSize(430,144);
		setVisible(false);
		button1.setLabel("Yes");
		add(button1);
		button1.setBackground(java.awt.Color.yellow);
		button1.setBounds(48,96,108,24);
		button2.setLabel("Cancel");
		add(button2);
		button2.setBackground(java.awt.Color.yellow);
		button2.setBounds(276,96,111,24);
		label1.setText("Are you sure you want to cancel selected meetings?");
		add(label1);
		label1.setFont(new Font("Dialog", Font.BOLD, 12));
		label1.setBounds(12,12,360,36);
		checkbox1.setLabel("Cancel all instances of reoccuring meetings");
		add(checkbox1);
		checkbox1.setBounds(36,48,336,40);
		setTitle("Cancel Meeting(s)");
		//}}

		//{{REGISTER_LISTENERS
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		SymMouse aSymMouse = new SymMouse();
		button1.addMouseListener(aSymMouse);
		button2.addMouseListener(aSymMouse);
		//}}
	}
	
	public DeleteOptionsDialog(Frame parent, boolean modal)
	{
		this(parent);
        setModal(modal);
	}
	
	public void addNotify()
	{
  	    // Record the size of the window prior to calling parents addNotify.
	    Dimension d = getSize();

		super.addNotify();

		if (fComponentsAdjusted)
			return;

		// Adjust components according to the insets
		Insets insets = getInsets();
		setSize(insets.left + insets.right + d.width, insets.top + insets.bottom + d.height);
		Component components[] = getComponents();
		for (int i = 0; i < components.length; i++)
		{
			Point p = components[i].getLocation();
			p.translate(insets.left, insets.top);
			components[i].setLocation(p);
		}
		fComponentsAdjusted = true;
	}

    // Used for addNotify check.
	boolean fComponentsAdjusted = false;


	public DeleteOptionsDialog(Frame parent, String title, boolean modal)
	{
		this(parent, modal);
		setTitle(title);
	}

    /**
     * Shows or hides the component depending on the boolean flag b.
     * @param b  if true, show the component; otherwise, hide the component.
     * @see java.awt.Component#isVisible
     */
    public void setVisible(boolean b)
	{
		if(b)
		{
			Rectangle bounds = getParent().getBounds();
			Rectangle abounds = getBounds();
	
			setLocation(bounds.x + (bounds.width - abounds.width)/ 2,
				 bounds.y + (bounds.height - abounds.height)/2);
		}
		super.setVisible(b);
	}

	//{{DECLARE_CONTROLS
	java.awt.Button button1 = new java.awt.Button();
	java.awt.Button button2 = new java.awt.Button();
	java.awt.Label label1 = new java.awt.Label();
	java.awt.Checkbox checkbox1 = new java.awt.Checkbox();
	//}}

	class SymWindow extends java.awt.event.WindowAdapter
	{
		public void windowClosing(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == DeleteOptionsDialog.this)
				DeleteOptionsDialog_WindowClosing(event);
		}
	}
	
	void DeleteOptionsDialog_WindowClosing(java.awt.event.WindowEvent event)
	{
		setVisible(false);
	}

	class SymMouse extends java.awt.event.MouseAdapter
	{
		public void mouseReleased(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == button1)
				button1_MouseReleased(event);
			else if (object == button2)
				button2_MouseReleased(event);
		}
	}

	void button1_MouseReleased(java.awt.event.MouseEvent event)
	{
	    if (checkbox1.getState())
	        // OK and cancel all instances
            option = this.OK_ALL_INSTANCES;
		else 
		    // OK and cancel single instances
		    option = this.OK;
        hide();		    
	}

	void button2_MouseReleased(java.awt.event.MouseEvent event)
	{
	    // CANCEL
		option = this.CANCEL;	 
		hide();
	}
	
	public int getOption() {
	    return option;   
    }
}