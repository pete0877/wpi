import java.awt.*;

public class DGTEGameTypeDialog extends java.awt.Dialog
{
	String gameType;
	
	public DGTEGameTypeDialog(Frame parent)
	{
		super(parent);
		//{{INIT_CONTROLS
		setLayout(null);
		setBackground(java.awt.Color.white);
		setForeground(java.awt.Color.black);
		setFont(new Font("Dialog", Font.PLAIN, 12));
		setSize(360,390);
		setVisible(false);
		label1.setText("Game Type Information");
		add(label1);
		label1.setFont(new Font("Dialog", Font.BOLD, 12));
		label1.setBounds(12,12,156,24);
		add(txtName);
		txtName.setBounds(12,66,198,24);
		add(txtEncoding);
		txtEncoding.setBounds(12,126,336,24);
		add(txtCopyright);
		txtCopyright.setBounds(288,66,60,24);
		add(txtVersion);
		txtVersion.setBounds(222,66,54,24);
		add(txtCompany);
		txtCompany.setBounds(12,186,336,24);
		add(txtTdmk);
		txtTdmk.setBounds(12,246,336,24);
		label2.setText("Game Type");
		add(label2);
		label2.setBounds(12,48,66,18);
		label3.setText("Version");
		add(label3);
		label3.setBounds(222,42,54,24);
		label4.setText("Description");
		add(label4);
		label4.setBounds(12,282,72,24);
		label5.setText("Encoding");
		add(label5);
		label5.setBounds(12,102,60,24);
		label6.setText("Company/Author");
		add(label6);
		label6.setBounds(12,162,96,24);
		add(txtDesc);
		txtDesc.setBounds(12,306,336,72);
		label7.setText("Copyright");
		add(label7);
		label7.setBounds(288,42,60,24);
		label8.setText("Trademarks");
		add(label8);
		label8.setBounds(12,222,72,24);
		setTitle("DGTE Game Type");
		setResizable(false);
		//}}

		//{{REGISTER_LISTENERS
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		//}}
	}

	public DGTEGameTypeDialog(Frame parent, boolean modal)
	{
		this(parent);
		setModal(modal);;
	}
	
	public DGTEGameTypeDialog(Frame parent, boolean modal, String gt)
	{
	    this(parent, modal);
	    gameType = gt;
	    
        int lastIndex = -1;
        String[] typeParts = new String[14];
        for (int i=0; i<14; i++)
        {
            int thisIndex = gameType.indexOf('\n',lastIndex+1);
            typeParts[i] = gameType.substring(lastIndex+1,thisIndex);
            lastIndex = thisIndex;
        }
        
        txtName.setText(typeParts[1]);
        txtVersion.setText(typeParts[3]);
        txtDesc.setText(typeParts[5]);
        txtEncoding.setText(typeParts[7]);
        txtCompany.setText(typeParts[9]);
        txtCopyright.setText(typeParts[11]);
        txtTdmk.setText(typeParts[13]);
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

	public DGTEGameTypeDialog(Frame parent, String title, boolean modal)
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
			if (object == DGTEGameTypeDialog.this)
				DGTEGameTypeDialog_WindowClosing(event);
		}
	}

	void DGTEGameTypeDialog_WindowClosing(java.awt.event.WindowEvent event)
	{
		dispose();
	}
	//{{DECLARE_CONTROLS
	java.awt.Label label1 = new java.awt.Label();
	java.awt.TextField txtName = new java.awt.TextField();
	java.awt.TextField txtEncoding = new java.awt.TextField();
	java.awt.TextField txtCopyright = new java.awt.TextField();
	java.awt.TextField txtVersion = new java.awt.TextField();
	java.awt.TextField txtCompany = new java.awt.TextField();
	java.awt.TextField txtTdmk = new java.awt.TextField();
	java.awt.Label label2 = new java.awt.Label();
	java.awt.Label label3 = new java.awt.Label();
	java.awt.Label label4 = new java.awt.Label();
	java.awt.Label label5 = new java.awt.Label();
	java.awt.Label label6 = new java.awt.Label();
	java.awt.TextArea txtDesc = new java.awt.TextArea();
	java.awt.Label label7 = new java.awt.Label();
	java.awt.Label label8 = new java.awt.Label();
	//}}

}