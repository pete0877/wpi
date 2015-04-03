// Class  : SampleWrapperApp
// Author : George Heineman
//        : heineman@cs.wpi.edu
//        : http://www.cs.wpi.edu/~heineman
// Version: 1.0

//
import java.applet.Applet;
import java.awt.event.AdjustmentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.*;
import java.io.Serializable;
import java.util.Vector;

import gui.*;
import adapt.*;

public class SampleWrapperApp extends Applet implements TableListener, Serializable, ActionListener {

    // The Eight Java Bean components for this application
    Wrapper          wr;
    TextField        input;
	TextField        fmt_input;    
    
  public void init() {
    init_gui();
    init_components();
    
    // start off at first location
    wr.setSelectedRegion (new TableRegion (new TableElement (1, 1),
					   new TableElement (1, 1)));

  }

  // Creates the user interface, composed of two scrollbars 4 table beans, 
  private void init_gui() {
    Label l;
    Font  font = new Font("Dialog", Font.PLAIN, 12);
    Font  headerFont = new Font("Dialog", Font.BOLD, 12);
    FontMetrics fm = getFontMetrics(font);

    Panel p = new Panel(new GridLayout (4,2));

    p.add (new Label ("TableBean:"));
    wr = new Wrapper (3, 5);     // center
    p.add (wr);

    p.add (new Label ("Enter Value here"));
    input = new TextField (5);
    p.add (input);

	p.add (new Label ("Enter format here:"));
	fmt_input = new TextField (5);
    p.add (fmt_input);
	
    add (p);
    setSize (500,300);
    setVisible(true);
  }

  /**
   * Initializes the components so they can communicate with each other.
   *
   */
  private void init_components() {
      // when wrapper generates events, we want to know about it
      wr.addTableListener (this);	  
      input.addActionListener (this);
	  fmt_input.addActionListener (this);
   }


  // ------------------------------
  // Methods for TableEventListener
  // ------------------------------
  public void handleTableEvent (TableEventObject teo) {
    if (teo.getSource() == wr) {
	// Note that this method isn't called now.  How will you fix this?	
	if (teo.getType() == TableEventObject.MOUSE_PRESS) {
		TableRegion tr = wr.getSelectedRegion();
		TableElement te = tr.getStart();		
		input.setText(wr.getTableValue (te.getColumn(), te.getRow()));
		fmt_input.setText(wr.getFormat (te.getColumn(), te.getRow()));	  		  	
      }	
    }
  }

  // ------------------------------
  // Methods for TableEventListener
  // ------------------------------
  public void actionPerformed (ActionEvent ae) {
		if (ae.getSource() == input) {	  
			TableRegion tr = wr.getSelectedRegion();
			TableElement te = tr.getStart();	  
			wr.setTableValue (te.getColumn(), te.getRow(), ae.getActionCommand());
			try {
				wr.setFormat (te.getColumn(), te.getRow(), fmt_input.getText());	  
			} catch (BadFormattingStringException e) {}	  	
		}
		if (ae.getSource() == fmt_input) {	  
			TableRegion tr = wr.getSelectedRegion();
			TableElement te = tr.getStart();	  	  
			try {
				wr.setFormat (te.getColumn(), te.getRow(), fmt_input.getText());	  
			} catch (BadFormattingStringException e) {}	
      }	  
  }

  public static void main (String args[]) {
      SampleWrapperApp sa = new SampleWrapperApp ();
      sa.init();
      sa.setVisible(true);
  }
}
