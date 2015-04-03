// Class  : TextBean
// Author : George Heineman
//        : heineman@cs.wpi.edu
//        : http://www.cs.wpi.edu/~heineman
// Version: 1.0

package gui;

/**
 * This is a simple text bean that broadcasts its value as a TextBeanEvent
 */

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.Serializable;
import java.util.Vector;

public class TextBean extends TextField implements Serializable, KeyListener {

  private String lastValue;
  private Font  font = new Font ("Dialog", Font.PLAIN, 12);
    
  /**
   * Constructs a TextBean
   */
  public TextBean() {
    // default to twenty columns
    this (20);
  }

  /**
   * Constructs a TextBean
   */
  public TextBean (int columns) {
    // default to twenty columns
    super(columns);
    super.setFont (font);

    super.addKeyListener (this);
    lastValue = new String ("");
  }

  /**
   * Intercept setText() calls, to remember previous value (if any)
   *
   */
  public void setText(String newValue) {
    super.setText (newValue);
    lastValue = new String (newValue);
  }

  /**
   * Set the current font.
   *
   * @see #getFont
   */
  public void setFont(Font f) {
    super.setFont (font = f);
    repaint();
  }

  /**
   * Returns the current font.
   *
   * @see #setFont
   */
  public Font getFont() {
    return super.getFont();
  }


  // ------------------------------
  // KeyListener methods.
  // ------------------------------
  private int terminatingCharacter = -1;

  /**
   * Detect TAB/SHIFT_TAB/SHIFT_RETURN events
   *
   * Note: Ordinary RETURN events are already trapped, so we don't capture
   *       them here.
   */
  public void keyTyped (KeyEvent e) {
      // System.out.println ("DEBUG:" + e.toString());
    int mods = e.getModifiers();
    boolean shift = ((mods & InputEvent.SHIFT_MASK) == InputEvent.SHIFT_MASK);

    terminatingCharacter = -1;

    if (e.getKeyChar() == '\t') {
      if (shift)
	terminatingCharacter = TextBeanEventObject.SHIFT_TAB;
      else
	terminatingCharacter = TextBeanEventObject.TAB;
    }

    if ((e.getKeyChar() == '\n') || (e.getKeyChar() == 141)) {
      if (shift)
	terminatingCharacter = TextBeanEventObject.SHIFT_RETURN;
      else
	terminatingCharacter = TextBeanEventObject.RETURN;
    }

    if (terminatingCharacter != -1) {
	
      // The shift return is added in for some reason, so we need to remove it
      String str = getText();
      if (terminatingCharacter == TextBeanEventObject.SHIFT_RETURN) {
	if (str.length() <= 1)
	  str = new String("");
	else
	  str = str.substring (0, str.length() - 1);
      }
				      
      // Generate action Event for TextBean
      TextBeanEventObject tbeo = new TextBeanEventObject (this, lastValue, str,
							  terminatingCharacter);
      fireAction (tbeo);
    }
  }

  public void keyPressed (KeyEvent e) {
	// System.out.println ("DEBUG:" + e.toString());
    int mods = e.getModifiers();
    boolean shift = ((mods & InputEvent.SHIFT_MASK) == InputEvent.SHIFT_MASK);

    // protect against Shift/Return by un-highlighting the selection Region
    // 65421 == SHIFT_ENTER
    if ((e.getKeyChar() == '\n') || (e.getKeyChar() == KeyEvent.VK_ENTER) || (e.getKeyChar() == '\t')) 
	{
      if (shift) {
		setSelectionStart (getText().length());
		setSelectionEnd (getText().length());
      }
      String str = getText();	  
		if (str.length() <= 1)
			str = new String("");
		else
			str = str.substring (0, str.length() - 1);	  

	    terminatingCharacter = -1;
	
	    if (e.getKeyChar() == '\t') {			
	      if (shift)
		terminatingCharacter = TextBeanEventObject.SHIFT_TAB;
	      else
		terminatingCharacter = TextBeanEventObject.TAB;
	    }
	
	    if ((e.getKeyChar() == '\n') || (e.getKeyChar() == 141)) {	
	      if (shift)
		terminatingCharacter = TextBeanEventObject.SHIFT_RETURN;
	      else
		terminatingCharacter = TextBeanEventObject.RETURN;
	    }


      // Generate action Event for TextBean
      TextBeanEventObject tbeo = new TextBeanEventObject (this, lastValue, str, terminatingCharacter);
      fireAction (tbeo);
		
    }
  }

  public void keyReleased (KeyEvent e) {}



  //----------------------------------------------------------------------
  // Methods for registering/deregistering TableEventListeners
  //----------------------------------------------------------------------

  /**
   * Adds the given listener to internal list
   *
   * @see #removeTextBeanListener
   * @param tbl the TextBeanListener
   */      
  public synchronized void addTextBeanListener(TextBeanListener tbl) {
    textBeanListeners.addElement(tbl);
  }

  /**
   * Removes the given listener from internal list
   * @see #addTextBeanListener
   * @param tbl the TextBeanListener
   */      
  public synchronized void removeTextBeanListener(TextBeanListener tbl) {
    textBeanListeners.removeElement(tbl);
  }

  private void fireAction(TextBeanEventObject tbeo) {
    Vector targets;
    synchronized (this) {
      targets = (Vector) textBeanListeners.clone();
    }

    for (int i = 0; i < targets.size(); i++) {
      TextBeanListener target = (TextBeanListener) targets.elementAt(i);
      target.handleTextBeanEvent (tbeo);
    }
  }

  /**
   * These functions help within the BDK
   */
  public Dimension getPreferredSize() {
      int width = getColumns() * 8;     // estimate font width
      if (width < 200)
	  width = 200;
      return new Dimension (width, 32);
  }

  /**
   * @deprecated provided for backward compatibility with old layout managers.
   */
  public Dimension preferredSize() {
    return getPreferredSize();
  }

  public Dimension getMinimumSize() {
    return getPreferredSize();
  }

  /**
   * @deprecated provided for backward compatibility with old layout managers.
   */
  public Dimension minimumSize() {
    return getMinimumSize();
  }
  
  private Vector textBeanListeners = new Vector();
}

