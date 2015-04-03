// Class  : Wrapper
// Author : George Heineman
//        : heineman@cs.wpi.edu
//        : http://www.cs.wpi.edu/~heineman
// Version: 1.0
      
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

import java.io.Serializable;
import java.util.Vector;
import java.util.Hashtable;
import java.text.DecimalFormat;

import gui.*;

public class Wrapper extends Panel implements Serializable, MouseMotionListener, MouseListener, TableListener {
	protected TableBean tb;
	protected Hashtable formatStrings;
	protected Hashtable originalValues;		
	private Vector tableListeners;
	private PropertyChangeSupport changes = new PropertyChangeSupport(this);

    public Wrapper() { this (1, 1); }
  
    /**
     * Constructs the Wrapper
     *
     * @param #cols the width 
     * @param #rows the height
     */

    public Wrapper (int cols, int rows) {
		int a, b;
		String key;
		setLayout (null);
		
		// Initialize the formatting strings array and the original values array
		formatStrings = new Hashtable();
		originalValues = new Hashtable();
		for (a = 1; a <= cols; a++) {
			for (b = 1; b <= rows; b++) {
				key = cols + ":" + rows;
				formatStrings.put(key, "");
				originalValues.put(key, "");
			}				
		}		
		
		
		tb = new TableBean (cols, rows);
		tb.setBounds (0, 0, tb.getPreferredSize().width,
			      tb.getPreferredSize().height);
		add (tb);  // make wrapper a container

		// create listeners table:
		tableListeners = new Vector();	
	
		// redirect events away from TableBean...
		tb.removeMouseListener (tb);
		tb.removeMouseMotionListener (tb);
	
		// ... to the wrapper.  The events are mouse clicks and moves.
		// Wrapper will manually re-direct these events into internal tb.
		tb.addMouseListener(this);
		tb.addMouseMotionListener(this);		
		
		tb.addTableListener(this);	
		
	}


	/**
	     * Format a number according to a format string and return it as a string.
     */
	private String formatNumber(double number, String format)	{
		if ((format==null) || (format.equals(""))) {
			return new Double(number).toString();			
		}		
		String fmt = format.trim();
		DecimalFormat formatter = new DecimalFormat ();
		if (fmt.indexOf(".") == -1) { fmt = fmt.concat(".0"); }
		int decimal_part = new Integer(fmt.substring(fmt.indexOf(".") + 1, fmt.length())).intValue();
		int integer_part = new Integer(fmt.substring(0, fmt.indexOf("."))).intValue();
		formatter.applyPattern("#.");				
		String tmp, prefix = "";		
		int a;
		if (integer_part == 0) {
			tmp = ".";
		} else {
			tmp = "#.";
			for (a = 1; a<=integer_part - formatter.format(number).length() + 1; a++) { prefix = prefix + " "; }			
		}
		for (a = 1; a<=decimal_part; a++) { tmp = tmp + "0"; }
		formatter.applyPattern(tmp);
		return prefix + formatter.format(number);
	}	

	/**
	     * Sets the formatting for a particular table element:
     */
	public void setFormat(int viewCol, int viewRow, String val) throws BadFormattingStringException {		
		String key = viewCol + ":" + viewRow;
	
		//check if the format string is null or empty:
		if ((val == null) || val.equals("")) {
			// reset the format string and bring back the original value
			formatStrings.put(key, "");
			tb.setTableValue(viewCol, viewRow, (String) originalValues.get(key));
			return;
		}		
		
		// the format string is not empty, so check if it is correct:
		Float format = null;
		try {				
			//if (val.indexOf(".") == -1) { val.concat(".0"); }
			format = new Float(val);
		} catch (NumberFormatException e) {
			throw new BadFormattingStringException();			
		}				
		
		// yes, it's correct, so set the new format for the element:		
		formatStrings.put(key, val);		
		
		// check if the original value is a floating point:
		if (originalValues.get(key)!=null) {
			Float value = null;
			try {
				value = new Float((String) originalValues.get(key));							
			} catch (NumberFormatException e) {
				value = null;
			}
			if (value != null) {
				// the table element contains a numerical value, so we
				// update the table bean with the new formatted string:
				tb.setTableValue(viewCol, viewRow, (String) formatNumber(value.doubleValue(), val));
			}
		}
    }
	
	/**
	     * Returns the formatting for a particular table element:
     */
	public String getFormat(int viewCol, int viewRow) {		
		String key = viewCol + ":" + viewRow;	
		return (String) formatStrings.get(key);
    }	

    /**
     * Sets the value for a particular table entry
     *
     * @param viewCol the column location
     * @param viewRow the row location
     * @param val the value
     * @see #getTableValue
     */
    public void setTableValue(int viewCol, int viewRow, String val) {		
		String key = viewCol + ":" + viewRow;
		String fmt = (String) formatStrings.get(key);
		originalValues.put(key, val);		
		// check if any formatting needs to be done at all:
		if ((fmt == null) || (fmt.equals("")) || (val == null) || (val.equals(""))) {
			// no formatting was assigned, or the new value is empty,
			// so we simply display the new value in preserved format:
			tb.setTableValue (viewCol, viewRow, val);
			return;
		}
		
		// some formatting exists for the element and the new value is not empty:
		// get the float value of the formatting string:
		Float format;
		try {
			format = new Float((String) fmt);							
		} catch (NumberFormatException e) { return; }		
		
		// we need to check if the value of the element is a number:
		String tmp = val;		
		Float value;
		try {
			value = new Float((String) originalValues.get(key));							
			tmp = (String) formatNumber(value.doubleValue(), fmt);
		} catch (NumberFormatException e) {}
		tb.setTableValue (viewCol, viewRow, tmp);
	}

    /**
     * Retrieves the value for a particular table entry
     *
     * @param viewCol the column location
     * @param viewRow the row location
     * @see #setTableValue
     */
    public String getTableValue(int viewCol, int viewRow) {
		String key = viewCol + ":" + viewRow;
		return (String) originalValues.get(key);		
    }

    /**
     * Clear all values in the Table.
     *
     * @see #setTableValue
     * @see #getTableValue
     */
    public void clearTableValue () {
	tb.clearTableValue();
    }

    /**
     * Returns the height of a table Row
     * 
     * @see #setRowHeight
     */
    public int getRowHeight() {
	return tb.getRowHeight();
    }

    /**
     * Set the height of a table Row.  This is a bound property
     *
     * @param newHeight the height
     * @see #getRowHeight
     */
    public void setRowHeight(int newHeight) {
	tb.setRowHeight (newHeight);
    }

    /**
     * Returns the width of a table Column
     * 
     * @see #setColumnWidth
     */
    public int getColumnWidth() {
	return tb.getColumnWidth();
    }

    /**
     * Set the width of a table Column.  This is a bound property
     *
     * @param newWidth the new width
     * @see #getColumnWidth
     */
    public void setColumnWidth(int newWidth) {
	tb.setColumnWidth (newWidth);
    }

    /**
     * Returns the foreground color for the bean
     * 
     * @see #setForeground
     */
    public Color getForeground() {
	return tb.getForeground();
    }

    /**
     * Set the color the table is drawn with.  This is a bound property.
     * 
     * @see #getForeground
     */
    public void setForeground(Color c) {
	tb.setForeground (c);
    }

    /**
     * Returns the background color for the Bean
     * 
     * @see #setBackground
     */
    public Color getBackground() {
	return tb.getBackground();
    }

    /**
     * Set the background color of the table.  This is a bound property.
     *
     * @see #getBackground
     */
    public void setBackground(Color c) {
	tb.setBackground (c);
    }

    /**
     * Set the current font.  This is a bound property.
     *
     * @see #getFont
     */
    public void setFont(Font f) {
	tb.setFont (f);
    }

    /**
     * Returns the current font.  This is a bound property.
     *
     * @see #setFont
     */
    public Font getFont() {
	return tb.getFont ();
    }

    /**
     * Paint the Table Bean.
     */
    public synchronized void paint(Graphics g) {
	tb.paint (g);
    }

    /**
     * Returns the selected TableRegion
     *
     * @see #setSelectedRegion
     */
    public TableRegion getSelectedRegion () {
	return tb.getSelectedRegion();
    }

    /**
     * Sets the selected region to be the specified TableRegion.
     *
     * @see #getSelectedRegion
     */
    public void setSelectedRegion (TableRegion tr) {
	tb.setSelectedRegion (tr);
    }

    /**
     * Select an invertedRegion; If unchanged, return false, otherwise true.
     * 
     * @param     sTE   start TableElement
     * @param     eTE   end TableElement
     * @returns   true  if a new region has been selected; false otherwise
     */
    public void setSelectedRegion (TableElement sTE, TableElement eTE) {
	tb.setSelectedRegion (sTE, eTE);
    }

	public void handleTableEvent (TableEventObject teo) {			
		TableEventObject new_teo = new TableEventObject (this, teo.getFrom(), teo.getTo(), teo.getType());		
		fireAction(new_teo);
	}
	
	  /**
	   * The primary interface for sending TableEventObject events to listening components
	   *
	   * @param   teo   The TableEventObject
	   *
	   * @see #addPropertyChangeListener
	   * @see #removePropertyChangeListener
	   * @see #tableListeners
	   */
	  private void fireAction(TableEventObject teo) {
		Vector targets;
		synchronized (this) {
		  targets = (Vector) tableListeners.clone();
		}

		for (int i = 0; i < targets.size(); i++) {
		  TableListener target = (TableListener) targets.elementAt(i);
		  target.handleTableEvent (teo);
		}
	  }	

    //-----------------------------
    // MouseListener methods.
    // ----------------------------
    /**
     * Ignore mouseClicked events.
     */
    public void mouseClicked(MouseEvent evt) { 
	tb.mouseClicked (evt);
    }

    /**
     * Process mousePressed events.
     */
    public void mousePressed(MouseEvent evt) { 
	tb.mousePressed (evt);
    }

    /**
     * Process mouseReleased events.
     */
    public void mouseReleased(MouseEvent evt) { 
	tb.mouseReleased (evt);
    }

    /**
     * Ignore mouseEntered events.
     */
    public void mouseEntered(MouseEvent evt) { 
	tb.mouseEntered (evt);
    }

    /**
     * Ignore mouseExited events.
     */
    public void mouseExited(MouseEvent evt) {
	tb.mouseExited (evt);
    }

    //-----------------------------
    // MouseMotionListener methods.
    // ----------------------------
    /**
     * Process mouseDragged events.
     */
    public void mouseDragged (MouseEvent evt) { 
	tb.mouseDragged (evt);
    }

    /**
     * Ignore mouseMoved events.
     */
    public void mouseMoved (MouseEvent evt) { 
	tb.mouseMoved (evt);
    }


    //----------------------------------------------------------
    // Methods for registering/deregistering TableEventListeners
    //----------------------------------------------------------

    /**
     * Adds a new TableListener.
     */    
	public synchronized void addTableListener(TableListener tl) {
	    tableListeners.addElement(tl);
	}

    /**
     * Removes an existing TableListener
     */
    public synchronized void removeTableListener(TableListener tl) {
	    tableListeners.removeElement(tl);
  	}

	public void addPropertyChangeListener(PropertyChangeListener l) {
  	  changes.addPropertyChangeListener(l);
	}
	
    /** 
     * Remove this PropertyChangeListener from the buttons internal list.  
     */
	public void removePropertyChangeListener(PropertyChangeListener l) {
	    changes.removePropertyChangeListener(l);
	}



    /** 
     * Set the number of columns to table.  This is a bound property.
     */
    public void setNumColumns(int newCols) {
	tb.setNumColumns (newCols);
    }
  
    /** 
     * Return the number of columns
     */
    public int getNumColumns() {
	return tb.getNumColumns();
    }

    /** 
     * Set the number of rows to table.  This is a bound property.
     */
    public void setNumRows(int newRows) {
	tb.setNumRows (newRows);
    }

    /** 
     * Return the number of rows
     */
    public int getNumRows() {
	return tb.getNumRows();
    }

    /**
     * Detetermines the base size of this component when shown on the screen.
     */
    public Dimension getPreferredSize() {
	return tb.getPreferredSize();
    }
}
