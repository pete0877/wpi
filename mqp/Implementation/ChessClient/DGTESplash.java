import java.awt.*;

public class DGTESplash extends java.awt.Frame
{
	private Image splash;
	
	public DGTESplash()
	{
        String imageFile = System.getProperty("user.dir") + "/splash.gif";
        splash = getToolkit().getImage(imageFile);
        
        MediaTracker mt = new MediaTracker(this);
        mt.addImage(splash, 0);
        try {
            mt.waitForAll();
        } catch(InterruptedException e) {}
		
		//{{INIT_CONTROLS
		setLayout(null);
		setSize(400,200);
		setVisible(false);
		setTitle("DGTE Client");
		setResizable(false);
		//}}

		//{{REGISTER_LISTENERS
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		//}}

		//{{INIT_MENUS
		//}}
	}

	public DGTESplash(String title)
	{
		this();
		setTitle(title);
	}
	public void setVisible(boolean b)
	{
		if(b)
		{
			setLocation(50, 50);
		}
	super.setVisible(b);
	}

	static public void main (String args[])
	{
		(new DGTESplash()).setVisible(true);
	}

    public void paint(Graphics g)
    {
        g.drawImage(splash, (this.getSize().width-400)/2, (this.getSize().height-200)-2, this);
    }

	public void addNotify()
	{
		// Record the size of the window prior to calling parents addNotify.
		Dimension d = getSize();

		super.addNotify();

		if (fComponentsAdjusted)
			return;

		// Adjust components according to the insets
		Insets ins = getInsets();
		setSize(ins.left + ins.right + d.width, ins.top + ins.bottom + d.height);
		Component components[] = getComponents();
		for (int i = 0; i < components.length; i++)
			{
			Point p = components[i].getLocation();
			p.translate(ins.left, ins.top);
			components[i].setLocation(p);
		}
		fComponentsAdjusted = true;
	}

	// Used for addNotify check.
	boolean fComponentsAdjusted = false;

	class SymWindow extends java.awt.event.WindowAdapter
	{
		public void windowClosing(java.awt.event.WindowEvent event)
		{
		Object object = event.getSource();
		if (object == DGTESplash.this)
			DGTESplash_WindowClosing(event);
		}
	}

	void DGTESplash_WindowClosing(java.awt.event.WindowEvent event)
	{
		dispose();		 // dispose of the Frame.
	}
	//{{DECLARE_CONTROLS
	//}}

	//{{DECLARE_MENUS
	//}}

}