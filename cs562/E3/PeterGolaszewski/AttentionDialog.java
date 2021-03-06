import java.awt.*;
import java.net.URL;

import symantec.itools.multimedia.ImageViewer;
import symantec.itools.awt.util.dialog.ModalDialog;

public class AttentionDialog extends ModalDialog
{
	public AttentionDialog(Frame parent, String title, String message, URL iconURL)
	{
		super(parent, title);

		if (iconURL != null)
		{
			ImageViewer img = new ImageViewer(iconURL);
			add(img);
		}

		// This code is automatically generated by Visual Cafe when you add
		// components to the visual environment. It instantiates and initializes
		// the components. To modify the code, only use code syntax that matches
		// what Visual Cafe can generate, or Visual Cafe may be unable to back
		// parse your Java file into its visual environment.
		//{{INIT_CONTROLS
		setLayout(new FlowLayout(FlowLayout.LEFT,5,5));
		setBackground(new java.awt.Color(255,255,210));
		setSize(498,40);
		setVisible(false);
		add(label1);
		label1.setBackground(new java.awt.Color(255,255,210));
		label1.setBounds(5,5,14,23);
		okButton.setLabel("OK");
		add(okButton);
		okButton.setBackground(java.awt.Color.yellow);
		okButton.setBounds(24,5,31,23);
		//}}

		label1.setText(message);

		//{{REGISTER_LISTENERS
		SymAction lSymAction = new SymAction();
		okButton.addActionListener(lSymAction);
		//}}
	}

	public AttentionDialog(Frame parent)
	{
		this(parent, "Attention", "Event", null);
	}

	// Add a constructor for Interactions (ignoring modal)
	public AttentionDialog(Frame parent, boolean modal)
	{
		this(parent);
	}

	// Add a constructor for Interactions (ignoring modal)
	public AttentionDialog(Frame parent, String message, boolean modal)
	{
		this(parent, "Attention", message, null);
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

	//{{DECLARE_CONTROLS
	java.awt.Label label1 = new java.awt.Label();
	java.awt.Button okButton = new java.awt.Button();
	//}}

	class SymAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == okButton)
				okButton_ActionPerformed(event);
		}
	}

	void okButton_ActionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.

		okButton_ActionPerformed_Interaction1(event);
	}

	void okButton_ActionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			this.dispose();
		} catch (Exception e) {
		}
	}
}
