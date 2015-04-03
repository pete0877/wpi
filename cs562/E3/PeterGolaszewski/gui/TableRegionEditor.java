// Class  : TableRegionEditor
// Author : George Heineman
//        : heineman@cs.wpi.edu
//        : http://www.cs.wpi.edu/~heineman
// Version: 1.0

package gui;

import java.awt.*;
import java.util.StringTokenizer;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.beans.*;

public class TableRegionEditor extends Panel implements PropertyEditor, KeyListener {

  private TextField c1, r1, c2, r2;

  // The TableRegion being edited.
  private TableRegion tr;
  private TableRegion oldTr;

  private PropertyChangeSupport support = new PropertyChangeSupport(this);


  public TableRegionEditor() {
    setLayout(new GridLayout(4,2));
    
    add (new Label ("Column1:"));
    add (c1 = new TextField ("-1", 3));
    c1.addKeyListener (this);

    add (new Label ("Row1:"));
    add (r1 = new TextField ("-1", 3));
    r1.addKeyListener (this);
    
    add (new Label ("Column2:"));
    add (c2 = new TextField ("-1", 3));
    c2.addKeyListener (this);
    
    add (new Label ("Row2:"));
    add (r2 = new TextField ("-1", 3));
    r2.addKeyListener (this);

    // Construct the default TableRegion
    tr = new TableRegion (new TableElement (-1, -1),
			  new TableElement (-1, -1));

    oldTr = new TableRegion (new TableElement (-1, -1),
			     new TableElement (-1, -1));
  }

  // ------------------------------
  // KeyListener Interface
  // ------------------------------
  public void keyPressed (KeyEvent ke) {}
  public void keyReleased (KeyEvent ke) {}
  public void keyTyped (KeyEvent ke) {
    TableElement te;
    int newValue = -1;

    int intC1, intR1, intC2, intR2;
    try {
       intC1 = Integer.valueOf(c1.getText()).intValue();
       intR1 = Integer.valueOf(r1.getText()).intValue();
       intC2 = Integer.valueOf(c2.getText()).intValue();
       intR2 = Integer.valueOf(r2.getText()).intValue();
    } catch (NumberFormatException nfe) {
        // Not sure what to do if changing values temporarily result in non-integers
        return;
    }

    System.out.println ("iC1:" + intC1 + "iR1:" + intR1 +
			"iC2:" + intC2 + "iR2:" + intR2);

    if (ke.getSource() == c1) {
      int newc1 = Integer.valueOf(c1.getText()).intValue();
      newValue = newc1;
      if (newValue >= 0) {
	    te = tr.getStart();
	    te.setColumn (newc1);
      }

    } else if (ke.getSource() == r1) {
      int newr1 = Integer.valueOf(r1.getText()).intValue();
      newValue = newr1;
      if (newValue >= 0) {
	    te = tr.getStart();
	    te.setRow (newr1);
      }

    } else if (ke.getSource() == c2) {
      int newc2 = Integer.valueOf(c2.getText()).intValue();
      newValue = newc2;
      if (newValue >= 0) { 
    	te = tr.getEnd();
	    te.setColumn (newc2);
      }

    } else if (ke.getSource() == r2) {
      int newr2 = Integer.valueOf(r2.getText()).intValue();
      newValue = newr2;
      if (newValue >= 0) { 
    	te = tr.getEnd();
	    te.setRow (newr2);
      }
    }

    changeTableRegion();
  }
  
  public void setValue(Object o) {
    // This should not be changed by this editor.  Make a copy
    oldTr = tr;
    tr = new TableRegion ((TableRegion) o);
    if (!oldTr.equals (tr))
      changeTableRegion ();

    // update the fields
    c1.setText (Integer.toString (tr.getStart().getColumn()));
    r1.setText (Integer.toString (tr.getStart().getRow()));
    c2.setText (Integer.toString (tr.getEnd().getColumn()));
    r2.setText (Integer.toString (tr.getEnd().getRow()));
  }
  
  public String getJavaInitializationString() {
    return "new gui.TableRegion(" +
      "new gui.TableElement (" + c1.getText() + "," + r1.getText() +")," +
      "new gui.TableElement (" + c2.getText() + "," + r2.getText() +"))";
  }

  private void changeTableRegion() {
    support.firePropertyChange("selectedRegion", null, null);
  }

  public Object getValue() {
    return tr;
  }

  public boolean isPaintable() {
    return true;
  }

  public void paintValue(java.awt.Graphics gfx, java.awt.Rectangle box) {
  }

  public String getAsText() {
    return c1.getText() + "," + r1.getText() + "," + c2.getText() + "," + r2.getText();
  }

  public void setAsText(String newValue) {
    StringTokenizer st = new StringTokenizer (newValue, ",", false);
    if (st.hasMoreTokens()) {
      c1.setText(st.nextToken());
      int newc1 = Integer.valueOf(c1.getText()).intValue();
      tr.getStart().setColumn (newc1);
    }

    if (st.hasMoreTokens()) {
      r1.setText(st.nextToken());
      int newr1 = Integer.valueOf(r1.getText()).intValue();
      tr.getStart().setRow (newr1);
    }

    if (st.hasMoreTokens()) {
      c2.setText(st.nextToken());
      int newc2 = Integer.valueOf(c2.getText()).intValue();
      tr.getEnd().setColumn (newc2);
    }

    if (st.hasMoreTokens()) {
      r2.setText(st.nextToken());
      int newr2 = Integer.valueOf(r2.getText()).intValue();
      tr.getEnd().setRow (newr2);
    }

    changeTableRegion();    // this should update the bean accordingly.
  }

  public String[] getTags() {
    return null;
  }

  public java.awt.Component getCustomEditor() {
    return this;
  }

  public boolean supportsCustomEditor() {
    return true;
  }

  public void addPropertyChangeListener(PropertyChangeListener l) {
    support.addPropertyChangeListener(l);
  }
  
  public void removePropertyChangeListener(PropertyChangeListener l) {
    support.removePropertyChangeListener(l);
  }

}

