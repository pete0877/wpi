import java.awt.*;
import symantec.itools.awt.ImagePanel;
import java.util.Vector;
import java.util.Enumeration;

public class DGTEConfigDialog extends java.awt.Dialog implements MessagePipeConnection
{
	private Vector messagePipes;
	private Vector gameTypes;
	
	public DGTEConfigDialog(Frame parent)
	{
		super(parent);
		messagePipes = new Vector(1);
		gameTypes = new Vector(2);
		
		//{{INIT_CONTROLS
		setLayout(null);
		setSize(372,222);
		setVisible(false);
		label1.setText("Server Configuration");
		add(label1);
		label1.setFont(new Font("Dialog", Font.BOLD, 12));
		label1.setBounds(72,12,132,24);
		label2.setText("Game Configuration");
		add(label2);
		label2.setFont(new Font("Dialog", Font.BOLD, 12));
		label2.setBounds(72,114,120,24);
		label3.setText("Location");
		add(label3);
		label3.setBounds(72,60,60,24);
		label4.setText("Port");
		add(label4);
		label4.setBounds(216,60,36,24);
		add(txtLocation);
		txtLocation.setBounds(72,84,138,24);
		add(txtPort);
		txtPort.setBounds(216,84,42,24);
		btnConnect.setLabel("Connect");
		add(btnConnect);
		btnConnect.setBounds(264,84,96,24);
		label5.setText("Game Types");
		add(label5);
		label5.setBounds(72,132,84,24);
		add(cboGameTypes);
		cboGameTypes.setBounds(72,156,258,21);
		btnStartGame.setLabel("Start Game");
		add(btnStartGame);
		btnStartGame.setBounds(108,186,96,24);
		btnCancel.setLabel("Cancel");
		add(btnCancel);
		btnCancel.setBounds(216,186,96,24);
		btnAboutType.setLabel("?");
		add(btnAboutType);
		btnAboutType.setForeground(java.awt.Color.blue);
		btnAboutType.setFont(new Font("Dialog", Font.BOLD, 12));
		btnAboutType.setBounds(336,156,24,24);
		add(imageCanvas1);
		imageCanvas1.setBackground(java.awt.Color.lightGray);
		imageCanvas1.setBounds(12,12,50,200);
		label6.setText("Status: ");
		add(label6);
		label6.setFont(new Font("Dialog", Font.PLAIN, 12));
		label6.setBounds(72,36,48,24);
		lblStatus.setText("Disconnected");
		add(lblStatus);
		lblStatus.setForeground(java.awt.Color.black);
		lblStatus.setFont(new Font("Dialog", Font.BOLD, 12));
		lblStatus.setBounds(120,36,84,24);
		setTitle("DGTEConfigDialog");
		setResizable(false);
		//}}
    
		//{{REGISTER_LISTENERS
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		SymMouse aSymMouse = new SymMouse();
		btnCancel.addMouseListener(aSymMouse);
		btnConnect.addMouseListener(aSymMouse);
		btnStartGame.addMouseListener(aSymMouse);
		SymAction lSymAction = new SymAction();
		btnCancel.addActionListener(lSymAction);
		btnStartGame.addActionListener(lSymAction);
		btnConnect.addActionListener(lSymAction);
		btnAboutType.addActionListener(lSymAction);
		//}}
	    
	    imageCanvas1.setImage("configMenu.jpg");
	}

	public DGTEConfigDialog(Frame parent, boolean modal)
	{
		this(parent);
		setModal(modal);;
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

	public DGTEConfigDialog(Frame parent, String title, boolean modal)
	{
		this(parent, modal);
		setTitle(title);
	}

	public void setVisible(boolean b)
	{
		if (b)
		{
			Rectangle bounds = getParent().getBounds();
			Rectangle abounds = getBounds();

			setLocation(bounds.x + (bounds.width - abounds.width)/ 2,
				bounds.y + (bounds.height - abounds.height)/2);
		}
		super.setVisible(b);
	}

	class SymWindow extends java.awt.event.WindowAdapter
	{
		public void windowClosing(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == DGTEConfigDialog.this)
				DGTEConfigDialog2_WindowClosing(event);
		}
	}

	void DGTEConfigDialog2_WindowClosing(java.awt.event.WindowEvent event)
	{
		dispose();
	}
	//{{DECLARE_CONTROLS
	java.awt.Label label1 = new java.awt.Label();
	java.awt.Label label2 = new java.awt.Label();
	java.awt.Label label3 = new java.awt.Label();
	java.awt.Label label4 = new java.awt.Label();
	java.awt.TextField txtLocation = new java.awt.TextField();
	java.awt.TextField txtPort = new java.awt.TextField();
	java.awt.Button btnConnect = new java.awt.Button();
	java.awt.Label label5 = new java.awt.Label();
	java.awt.Choice cboGameTypes = new java.awt.Choice();
	java.awt.Button btnStartGame = new java.awt.Button();
	java.awt.Button btnCancel = new java.awt.Button();
	java.awt.Button btnAboutType = new java.awt.Button();
	ImageCanvas imageCanvas1 = new ImageCanvas();
	java.awt.Label label6 = new java.awt.Label();
	java.awt.Label lblStatus = new java.awt.Label();
	//}}

    public void addMessagePipeConnection(DGTEMessagePipe mp) 
    {
        messagePipes.addElement(mp);
    }
	
	public void handleMessage(DGTEMessage m) 
	{
	    if(m.getType().equals("SERVER_GAMETYPE"))
	    {
	        Enumeration e = m.getParts();
	        e.nextElement();
	        int index = Integer.parseInt((String)e.nextElement());
            String s = (String)e.nextElement();	        
	        gameTypes.insertElementAt(s, index);

            int lastIndex = -1;
            String[] typeParts = new String[14];
            for (int i=0; i<14; i++)
            {
                int thisIndex = s.indexOf('\n',lastIndex+1);
                typeParts[i] = s.substring(lastIndex+1,thisIndex);
                lastIndex = thisIndex;
            }
            cboGameTypes.insert(typeParts[1]+" "+typeParts[3], index);
	    }
	    if(m.getType().equals("CLIENT_CONNECTIONLOST"))
	    {
	        lblStatus.setText("Disconnected");
	    }
	    if(m.getType().equals("CLIENT_CONNECTIONAVAILABLE"))
	    {
	        lblStatus.setText("Connected");
	    }
	}
	
	public void pipeMessage(DGTEMessage m) 
	{
	    for(Enumeration e=messagePipes.elements(); e.hasMoreElements();)
	    {
	        DGTEMessagePipe mp = (DGTEMessagePipe)e.nextElement();
	        mp.handleMessage(m, this);
	    }
	}


	class SymMouse extends java.awt.event.MouseAdapter
	{
		public void mouseClicked(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == btnCancel)
				btnCancel_MouseClicked(event);
			else if (object == btnConnect)
				btnConnect_MouseClicked(event);
			else if (object == btnStartGame)
				btnStartGame_MouseClicked(event);
		}
	}

	void btnCancel_MouseClicked(java.awt.event.MouseEvent event)
	{
		// to do: code goes here.
	}

	void btnConnect_MouseClicked(java.awt.event.MouseEvent event)
	{
		// to do: code goes here.
	}

	void btnStartGame_MouseClicked(java.awt.event.MouseEvent event)
	{
		// to do: code goes here.
	}

	class SymAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == btnCancel)
				btnCancel_ActionPerformed(event);
			else if (object == btnStartGame)
				btnStartGame_ActionPerformed(event);
			else if (object == btnConnect)
				btnConnect_ActionPerformed(event);
			else if (object == btnAboutType)
				btnAboutType_ActionPerformed(event);
		}
	}

	void btnCancel_ActionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		btnCancel_ActionPerformed_Interaction1(event);
	}

	void btnCancel_ActionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			DGTEMessage m = new DGTEMessage();
			m.setPart("CLIENT_DISCONNECT");
			pipeMessage(m);
			setVisible(false);
		} catch (Exception e) {
		}
	}

	void btnStartGame_ActionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		btnStartGame_ActionPerformed_Interaction1(event);
	}

	void btnStartGame_ActionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		if(cboGameTypes.getSelectedIndex() != -1)
		{
    		try {
			    DGTEMessage m = new DGTEMessage();
			    m.setPart("CLIENT_GAMETYPE");
			    m.setPart(String.valueOf(cboGameTypes.getSelectedIndex()));
			    pipeMessage(m);
			    m.clear();
			    m.setPart("CLIENT_PLY");
			    m.setPart("2");
			    pipeMessage(m);
			    setVisible(false);
		    } catch (Exception e) {
		    }
        }
	}

	void btnConnect_ActionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		btnConnect_ActionPerformed_Interaction1(event);
	}

	void btnConnect_ActionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			DGTEMessage m = new DGTEMessage();
	    	m.setPart("CLIENT_DISCONNECT");
    	    pipeMessage(m);
		    m.clear();
		    m.setPart("CLIENT_CONNECT");
		    m.setPart(txtLocation.getText());
		    m.setPart(txtPort.getText());
   			pipeMessage(m);
    	} catch (Exception e) {
		}
	}

	void btnAboutType_ActionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		btnAboutType_ActionPerformed_Interaction1(event);
	}

	void btnAboutType_ActionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		if (cboGameTypes.getSelectedIndex() != -1)
		{
		    try {
    			// DGTEGameTypeDialog Create and show as modal
			    (new DGTEGameTypeDialog(((Frame)getParent()), true, (String)gameTypes.elementAt(cboGameTypes.getSelectedIndex()))).setVisible(true);
		    } catch (Exception e) {
		    }
		}
	}
}