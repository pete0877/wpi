// Class  : TableBean
// Author : George Heineman
//        : heineman@cs.wpi.edu
//        : http://www.cs.wpi.edu/~heineman
// Version: 1.0


package gui;

// deals with adaptable interface
//
// CallBacks:
//
//   "TableBean()"
//   "TableBean(int,int)"
//   "setTableValue(int,int,String)"    # but note that StringBuffer is passed
//   "getTableValue(int,int)"
//   "clearTableValue()"
//   "getRowHeight()"
//   "setRowHeight(int)"
//   "getColumnWidth()"
//   "setColumnWidth(int)"
//   "getForeground()"
//   "setForeground(Color)"
//   "getBackground()"
//   "setBackground(Color)"
//   "getFont()"
//   "setFont(Font)"
//   "paint(Graphics)"
//   "redraw(Graphics,int,int)"
//   "getSelectedRegion()"
//   "setSelectedRegion(TableRegion)"
//   "setSelectedRegion(TableElement,TableElement)"
//   "clip(TableElement)"
//   "deselect()"
//   "redrawRegion(int,int,int,int)"
//   "redrawRegion(TableElement,TableElement)"
//   "setMinimum(TableElement)"
//   "maintainRegion()"
//   "inInvertedRegion(int,int)"
//   "convertMouseEvent(MouseEvent)"
//   "mouseClicked(MouseEvent)"
//   "mousePressed(MouseEvent)"
//   "mouseReleased(MouseEvent)"
//   "mouseEntered(MouseEvent)"
//   "mouseExited(MouseEvent)"
//   "mouseDragged(MouseEvent)"
//   "mouseMoved(MouseEvent)"
//   "addTableListener(TableListener)"
//   "removeTableListener(TableListener)"
//   "fireAction(TableEventObject)"
//   "addPropertyChangeListener(PropertyChangeListener)"
//   "removePropertyChangeListener(PropertyChangeListener)"
//   "getNumColumns()"
//   "setNumColumns(int)"
//   "getNumRows()"
//   "setNumRows(int)"
//   "getPreferredSizet()"
//   "sizeToFit()"
//
// ----------------------------------------
import adapt.ComponentAdapter;
import adapt.Adaptable;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

import java.io.Serializable;
import java.util.Vector;
import java.util.Hashtable;

public class TableBean extends Component implements Serializable, MouseMotionListener, MouseListener, TableReader, Adaptable {
  
  // Some constants
  private static final int minRow = 1;
  private static final int minColumn = 1;

  /**
   * properties of this table bean
   */
  private int   numRows = 0;
  private int   numColumns = 0;
  private int   rowHeight = 12;
  private int   columnWidth = 50;
  private Color foreColor = Color.black;
  private Color backColor = Color.lightGray;
  private Font  font = new Font ("Dialog", Font.PLAIN, 12);

  /**
   * Constructs a TableBean 
   */
  public TableBean() {

    this (1, 1);

    // BEFORE-START: before Callback -----------------------------------]]
    //      Constructor cannot return prematurely                       ]]
    //      until we have a better definition of what happens           ]]
    //                                                                  ]]
    if (adapter != null) {
      Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "TableBean()", null);
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      adapter.invokeCallback (ComponentAdapter.AFTER, "TableBean()", null);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]
  }
  
  /**
   * Constructs a TableBean 
   *
   * @param #cols the width 
   * @param #rows the height
   */
  public TableBean(int cols, int rows) {
    super ();

    // BEFORE-START: before Callback -----------------------------------]]
    //      Constructor cannot return prematurely                       ]]
    //      until we have a better definition of what happens           ]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[2];
      args[0] = new Integer (cols);
      args[1] = new Integer (rows);
      Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "TableBean(int,int)", args);

      // [Deny] Do not process this method
      if (o instanceof Exception) return;

      // [Override] Leave now (constructor cannot return any other value)
      if (o instanceof TableBean) return;
      if (o == null) return;

      // [Augment] arguments can be modified and used by this function
      cols = ((Integer) args[0]).intValue();
      rows = ((Integer) args[1]).intValue();
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    super.setFont (font);
    super.setBackground(backColor);
    super.setForeground(foreColor);

    // Start off with these dimensions
    numRows = rows;
    numColumns = cols;

    sizeToFit();

    // Don't forget to track for mouse clicks and moves
    addMouseListener(this);
    addMouseMotionListener(this);

    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[2];
      args[0] = new Integer (cols);
      args[1] = new Integer (rows);

      adapter.invokeCallback (ComponentAdapter.AFTER, "TableBean(int,int)", args);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]
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

    // BEFORE-START: before Callback -----------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[3];
      args[0] = new Integer (viewCol);
      args[1] = new Integer (viewRow);
      args[2] = new StringBuffer (val);

      Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "setTableValue(int,int,String)", args);

      // [Deny] Do not process this method
      if (o instanceof Exception) return;

      // [Override] Leave now (as a void function, there is no return)
      if (o == null) return;

      // [Augment] arguments can be modified and used by this function
      viewCol = ((Integer) args[0]).intValue();
      viewRow = ((Integer) args[1]).intValue();
      val = ((StringBuffer) args[2]).toString();
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    if ((viewCol < minColumn) || (viewCol > numColumns)) return;
    if ((viewRow < minRow) || (viewRow > numRows)) return;

    String key = viewCol + ":" + viewRow;
    if (val == null)
      values.remove (key); 
   else {
      values.put (key, val);
    }

    // Under certain circumstances, this component may not be visible, so we
    // must protect against it.
    Graphics g = getGraphics();
    if (g != null) redraw (g, viewCol, viewRow);

    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[3];
      args[0] = new Integer (viewCol);
      args[1] = new Integer (viewRow);
      args[2] = new String (val);
      adapter.invokeCallback (ComponentAdapter.AFTER, "setTableValue(int,int,String)", args);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]
  }

  /**
   * Retrieves the value for a particular table entry
   *
   * @param viewCol the column location
   * @param viewRow the row location
   * @see #setTableValue
   */
  public String getTableValue(int viewCol, int viewRow) {

    // BEFORE-START: before Callback -----------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[2];
      args[0] = new Integer(viewCol);
      args[1] = new Integer(viewRow);
      Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "getTableValue(int,int)", args);
      
      // [Deny] Do not process this method
      if (o instanceof Exception) return null;

      // [Override] Leave now with this value
      if (o instanceof String) return ((String) o);

      // [Augment] arguments can be modified and used by this function
      viewCol = ((Integer) args[0]).intValue();
      viewRow = ((Integer) args[1]).intValue();
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    String key = viewCol + ":" + viewRow;
    String val = (String) values.get (key);

    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[2];
      args[0] = new Integer (viewCol);
      args[1] = new Integer (viewRow);
      adapter.invokeCallback (ComponentAdapter.AFTER, "getTableValue(int,int)", args);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]

    return (val); 
  }

  /**
   * Clear all values in the Table.
   *
   * @see #setTableValue
   * @see #getTableValue
   */
  public void clearTableValue () {

    // BEFORE-START: before Callback -----------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "clearTableValue()", null);
      
      // [Deny] Do not process this method
      if (o instanceof Exception) return;

      // [Override] Leave now with this value
      if (o == null) return;

      // [Augment] arguments can be modified and used by this function
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    values = new Hashtable ();

    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      adapter.invokeCallback (ComponentAdapter.AFTER, "clearTableValue()", null);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]
  }


  /**
   * Returns the height of a table Row
   * 
   * @see #setRowHeight
   */
  public int getRowHeight() {

    // BEFORE-START: before Callback -----------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "getRowHeight()", null);
      
      // [Deny] Do not process this method
      if (o instanceof Exception) return 0;

      // [Override] Leave now with this value
      if (o instanceof Integer) return ((Integer) o).intValue();
      
      // [Augment] arguments can be modified and used by this function
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      adapter.invokeCallback (ComponentAdapter.AFTER, "getRowHeight()", null);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]
    return rowHeight;
  }

  /**
   * Set the height of a table Row.  This is a bound property
   *
   * @param newHeight the height
   * @see #getRowHeight
   */
  public void setRowHeight(int newHeight) {

    // BEFORE-START: before Callback -----------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[1];
      args[0] = new Integer (newHeight);
      Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "setRowHeight(int)", args);
      
      // [Deny] Do not process this method
      if (o instanceof Exception) return;

      // [Override] Leave now
      if (o == null) return;

      // [Augment] arguments can be modified and used by this function
      newHeight = ((Integer) args[0]).intValue();
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    int oldHeight = rowHeight;
    rowHeight = newHeight;
    sizeToFit();
    changes.firePropertyChange("rowHeight",
    			       new Integer(oldHeight), new Integer(rowHeight));


    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[1];
      args[0] = new Integer (newHeight);
      adapter.invokeCallback (ComponentAdapter.AFTER, "setRowHeight(int)", args);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]
  }

  /**
   * Returns the width of a table Column
   * 
   * @see #setColumnWidth
   */
  public int getColumnWidth() {

    // BEFORE-START: before Callback -----------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "getColumnWidth()", null);
      
      // [Deny] Do not process this method
      if (o instanceof Exception) return 0;

      // [Override] Leave now with this value
      if (o instanceof Integer) return ((Integer) o).intValue();

      // [Augment] arguments can be modified and used by this function
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      adapter.invokeCallback (ComponentAdapter.AFTER, "getColumnWidth()", null);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]

    return columnWidth;
  }

  /**
   * Set the width of a table Column.  This is a bound property
   *
   * @param newWidth the new width
   * @see #getColumnWidth
   */
  public void setColumnWidth(int newWidth) {

    // BEFORE-START: before Callback -----------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[1];
      args[0] = new Integer (newWidth);
      Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "setColumnWidth(int)", args);

      // [Deny] Do not process this method
      if (o instanceof Exception) return;
      
      // [Override] Leave now 
      if (o == null) return;

      // [Augment] arguments can be modified and used by this function
      newWidth = ((Integer) args[0]).intValue();
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    int oldWidth = columnWidth;
    columnWidth = newWidth;
    sizeToFit();
    changes.firePropertyChange("columnWidth",
    			       new Integer(oldWidth), new Integer(columnWidth));

    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[1];
      args[0] = new Integer (newWidth);
      adapter.invokeCallback (ComponentAdapter.AFTER, "setColumnWidth(int)", args);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]
  }

  /**
   * Returns the foreground color for the bean
   * 
   * @see #setForeground
   */
  public Color getForeground() {
    // BEFORE-START: before Callback -----------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "getForeground()", null);
      
      // [Deny] Do not process this method
      if (o instanceof Exception) return null;

      // [Override] Leave now (constructor cannot return any other value)
      if (o instanceof Color) return ((Color) o);

      // [Augment] arguments can be modified and used by this function
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      adapter.invokeCallback (ComponentAdapter.AFTER, "getForeground()", null);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]

    return super.getForeground();
  }

  /**
   * Set the color the table is drawn with.  This is a bound property.
   * 
   * @see #getForeground
   */
  public void setForeground(Color c) {

    // BEFORE-START: before Callback -----------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[1];
      args[0] = c;
      Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "setForeground(Color)", args);
      
      // [Deny] Do not process this method
      if (o instanceof Exception) return;

      // [Override] Leave now 
      if (o == null) return;

      // [Augment] arguments can be modified and used by this function
      c = ((Color) args[0]);
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    Color old = foreColor;
    super.setForeground (foreColor = c);
    changes.firePropertyChange("foreground", old, c);
    repaint();

    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[1];
      args[0] = c;
      adapter.invokeCallback (ComponentAdapter.AFTER, "setForeground(Color)", args);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]
  }

  /**
   * Returns the background color for the Bean
   * 
   * @see #setBackground
   */
  public Color getBackground() {

    // BEFORE-START: before Callback -----------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "getBackground()", null);
      
      // [Deny] Do not process this method
      if (o instanceof Exception) return null;

      // [Override] Leave now
      if (o instanceof Color) return ((Color) o);

      // [Augment] arguments can be modified and used by this function
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      adapter.invokeCallback (ComponentAdapter.AFTER, "getBackground()", null);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]

    return super.getBackground();
  }

  /**
   * Set the background color of the table.  This is a bound property.
   *
   * @see #getBackground
   */
  public void setBackground(Color c) {

    // BEFORE-START: before Callback -----------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[1];
      args[0] = c;
      Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "setBackground(Color)", args);
      
      // [Deny] Do not process this method
      if (o instanceof Exception) return;

      // [Override] Leave now 
      if (o == null) return;

      // [Augment] arguments can be modified and used by this function
      c = ((Color) args[0]);
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    Color old = backColor;
    super.setBackground (backColor = c);
    changes.firePropertyChange("background", old, c);
    repaint();

    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[1];
      args[0] = c;
      adapter.invokeCallback (ComponentAdapter.AFTER, "setBackground(Color)", args);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]
  }

  /**
   * Set the current font.  This is a bound property.
   *
   * @see #getFont
   */
  public void setFont(Font f) {
    // BEFORE-START: before Callback -----------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[1];
      args[0] = f;
      Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "setFont(Font)", args);

      // [Deny] Do not process this method
      if (o instanceof Exception) return;

      // [Override] Leave now 
      if (o == null) return;

      // [Augment] arguments can be modified and used by this function
      f = ((Font) args[0]);
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    Font old = getFont();
    super.setFont (font = f);
    repaint();
    changes.firePropertyChange("font", old, f);

    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[1];
      args[0] = f;
      adapter.invokeCallback (ComponentAdapter.AFTER, "setFont(Font)", args);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]
  }

  /**
   * Returns the current font.  This is a bound property.
   *
   * @see #setFont
   */
  public Font getFont() {

    // BEFORE-START: before Callback -----------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "getFont()", null);
      
      // [Deny] Do not process this method
      if (o instanceof Exception) return null;

      // [Override] Leave now 
      if (o instanceof Font) return ((Font) o);

      // [Augment] arguments can be modified and used by this function
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      adapter.invokeCallback (ComponentAdapter.AFTER, "getFont()", null);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]

    return super.getFont();
  }

  /**
   * Paint the Table Bean.
   */
  public synchronized void paint(Graphics g) {

    // BEFORE-START: before Callback -----------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[1];
      args[0] = g;
      Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "paint(Graphics)", args);
      
      // [Deny] Do not process this method
      if (o instanceof Exception) return;

      // [Override] Leave now 
      if (o == null) return;

      // [Augment] arguments can be modified and used by this function
      g = ((Graphics) args[0]);
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    int w = getSize().width;
    int h = getSize().height;
    FontMetrics fm = getFontMetrics(getFont());

    g.setColor(backColor);
    g.fill3DRect(0, 0, w - 1, h - 1, false);

    g.setColor(foreColor);
    g.setFont(getFont());
    
    // Draw the rows/columns, but only those that are visible
    for (int viewRow = minRow; viewRow <= minRow + numRows - 1; viewRow++)
      for (int viewCol = minColumn; viewCol <= minColumn + numColumns - 1; viewCol++)
	redraw (g, viewCol, viewRow);

    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[1];
      args[0] = g;
      adapter.invokeCallback (ComponentAdapter.AFTER, "paint(Graphics)", args);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]
  }

  protected synchronized void redraw (Graphics g, int viewCol, int viewRow) {

    // BEFORE-START: before Callback -----------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[3];
      args[0] = g;
      args[1] = new Integer (viewCol);
      args[2] = new Integer (viewRow);
      Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "redraw(Graphics,int,int)", args);
      
      // [Deny] Do not process this method
      if (o instanceof Exception) return;

      // [Override] Leave now 
      if (o == null) return;

      // [Augment] arguments can be modified and used by this function
      g = ((Graphics) args[0]);
      viewCol = ((Integer) args[1]).intValue();
      viewRow = ((Integer) args[2]).intValue();
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    boolean inverted = inInvertedRegion (viewCol, viewRow);

    if (inverted)
      g.setColor(foreColor);
    else
      g.setColor(backColor);

    int topCorner = (columnWidth+TEXT_XPAD)*(viewCol-1);
    int leftCorner = (rowHeight+TEXT_YPAD)*(viewRow-1);
    g.fillRect (topCorner, leftCorner,
		(columnWidth+TEXT_XPAD), (rowHeight+TEXT_YPAD));

    g.setColor(Color.black);
    g.drawRect (topCorner, leftCorner,
		(columnWidth+TEXT_XPAD), (rowHeight+TEXT_YPAD));

    if (inverted)
      g.setColor (backColor);
    else
      g.setColor (foreColor);

    String s = getTableValue (viewCol, viewRow);
    if (s != null)
      g.drawString (s,
		    TEXT_XPAD + topCorner,
		    (rowHeight+TEXT_YPAD)*viewRow - TEXT_YPAD);

    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[3];
      args[0] = g;
      args[1] = new Integer (viewCol);
      args[2] = new Integer (viewRow);
      adapter.invokeCallback (ComponentAdapter.AFTER, "redraw(Graphics,int,int)", args);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]
  }

  //-----------------------
  // MouseListener methods.
  // ----------------------
  private TableElement startTE = new TableElement (-1, -1);   // null
  private TableElement endTE = new TableElement (-1, -1);     // null

  private int  minRegionRow;
  private int  maxRegionRow;
  private int  minRegionColumn;
  private int  maxRegionColumn;

  private TableElement nullElement = new TableElement (-1, -1);
  private TableRegion  nullRegion = new TableRegion (nullElement, nullElement);

  // If these are true, then the region has moved in the appropriate
  // axis (i.e., the user has moved up and/or to the left).
  private boolean  negX, negY;

  /**
   * Returns the selected TableRegion
   *
   * @see #setSelectedRegion
   */
  public TableRegion getSelectedRegion () {

    // BEFORE-START: before Callback -----------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "getSelectedRegion()", null);

      // [Deny] Do not process this method
      if (o instanceof Exception) return null;

      // [Override] Leave now 
      if (o instanceof TableRegion) return ((TableRegion) o);

      // [Augment] arguments can be modified and used by this function
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    TableRegion tr = nullRegion;
    if (! startTE.equals(nullElement)) {
	tr = new TableRegion (startTE, endTE);
    }

    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
	adapter.invokeCallback (ComponentAdapter.AFTER, "getSelectedRegion()", null);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]

    return tr;
  }

  /**
   * Sets the selected region to be the specified TableRegion.
   *
   * @see #getSelectedRegion
   */
  public void setSelectedRegion (TableRegion tr) {

    // BEFORE-START: before Callback -----------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
	Object args[] = new Object[1];
	args[0] = new TableRegion (tr);
	Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "setSelectedRegion(TableRegion)", args);

      // [Deny] Do not process this method
      if (o instanceof Exception) return;

      // [Override] Leave now 
      if (o == null) return;

      // [Augment] arguments can be modified and used by this function
      tr = (TableRegion) args[0];
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    if (tr == null) {
      deselect();
    } else {
	TableRegion old = new TableRegion (startTE, endTE);
	TableRegion newOne = new TableRegion (tr);

	startTE = new TableElement (tr.getStart());
	clip (startTE);
    
	endTE = new TableElement (tr.getEnd());
	clip (endTE);

	setMinimum(startTE);
	maintainRegion();
    }

    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
	Object args[] = new Object[1];
	args[0] = new TableRegion (tr);
	adapter.invokeCallback (ComponentAdapter.AFTER, "setSelectedRegion(TableRegion)", args);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]
  }

  /**
   * Select an invertedRegion; If unchanged, return false, otherwise true.
   * 
   * @param     sTE   start TableElement
   * @param     eTE   end TableElement
   * @returns   true  if a new region has been selected; false otherwise
   */
  public void setSelectedRegion (TableElement sTE, TableElement eTE) {

    // BEFORE-START: before Callback -----------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[2];
      args[0] = new TableElement (sTE);
      args[1] = new TableElement (eTE);

      Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "setSelectedRegion(TableElement,TableElement)", args);

      // [Deny] Do not process this method
      if (o instanceof Exception) return;

      // [Override] Leave now 
      if (o == null) return;

      // [Augment] arguments can be modified and used by this function
      sTE = (TableElement) args[0];
      eTE = (TableElement) args[1];
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    setSelectedRegion (new TableRegion (sTE, eTE));

    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
	Object args[] = new Object[2];
	args[0] = new TableElement (sTE);
	args[1] = new TableElement (eTE);
	adapter.invokeCallback (ComponentAdapter.AFTER, "setSelectedRegion(TableElement,TableElement)", args);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]
  }

  /**
   * clips the TableElement to fit within Table
   */
  private void clip (TableElement element) {

    // BEFORE-START: before Callback -----------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
	Object args[] = new Object[1];
	args[0] = new TableElement (element);

	Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "clip(TableElement)", args);

      // [Deny] Do not process this method
      if (o instanceof Exception) return;

      // [Override] Leave now 
      if (o == null) return;

      // [Augment] arguments can be modified and used by this function
      element = (TableElement) args[0];
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    if (element.getColumn() < minColumn)
      element.setColumn(minColumn);
    if (element.getRow() < minRow)
      element.setColumn(minRow);

    if (element.getColumn() > numColumns)
      element.setColumn(numColumns);
    if (element.getRow() > numRows)
      element.setColumn(numRows);

    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
	Object args[] = new Object[2];
	args[0] = new TableElement (element);
	adapter.invokeCallback (ComponentAdapter.AFTER, "clip(TableElement)", args);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]
  }

  /**
   * Deselect any invertedRegion.
   *
   * Used internally to clean up.
   */
  private void deselect() {

    // BEFORE-START: before Callback -----------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
	Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "deselect()", null);

      // [Deny] Do not process this method
      if (o instanceof Exception) return;

      // [Override] Leave now 
      if (o == null) return;

      // [Augment] arguments can be modified and used by this function
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    startTE = nullElement;
    endTE = nullElement;
    redrawRegion (minRegionColumn, maxRegionColumn, minRegionRow, maxRegionRow);

    minRegionColumn = numColumns;
    maxRegionColumn = minColumn;
    minRegionRow = numRows;
    maxRegionRow = minRow;

    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
	adapter.invokeCallback (ComponentAdapter.AFTER, "deselect()", null);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]
  }

  /**
   * Redraws the given region based on ABSOLUTE COORDINATES, not
   * current screen coordinates
   */
  private void redrawRegion(int cmin, int cmax, int rmin, int rmax) {

    // BEFORE-START: before Callback -----------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
	Object args[] = new Object[4];
	args[0] = new Integer (cmin);
	args[1] = new Integer (cmax);
	args[2] = new Integer (rmin);
	args[3] = new Integer (rmax);
	Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "redrawRegion(int,int,int,int)", args);

      // [Deny] Do not process this method
      if (o instanceof Exception) return;

      // [Override] Leave now 
      if (o == null) return;

      // [Augment] arguments can be modified and used by this function
      cmin = ((Integer) args[0]).intValue();
      cmax = ((Integer) args[1]).intValue();
      rmin = ((Integer) args[2]).intValue();
      rmax = ((Integer) args[3]).intValue();
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    Graphics g = getGraphics();
    for (int c = cmin; c <= cmax; c++) 
      for (int r = rmin; r <= rmax; r++) {
	redraw (g, c, r);
      }

    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
	Object args[] = new Object[4];
	args[0] = new Integer (cmin);
	args[1] = new Integer (cmax);
	args[2] = new Integer (rmin);
	args[3] = new Integer (rmax);
	adapter.invokeCallback (ComponentAdapter.AFTER, "redrawRegion(int,int,int,int)", args);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]
  }

  /** 
   * redraws the given region based on start/end TableElements
   * 
   */
  private void redrawRegion(TableElement min, TableElement max) {

    // BEFORE-START: before Callback -----------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
	Object args[] = new Object[2];
	args[0] = new TableElement (min);
	args[1] = new TableElement (max);
	Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "redrawRegion(TableElement,TableElement)", args);

      // [Deny] Do not process this method
      if (o instanceof Exception) return;

      // [Override] Leave now 
      if (o == null) return;

      // [Augment] arguments can be modified and used by this function
      min = (TableElement) args[0];
      max = (TableElement) args[1];
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    redrawRegion (min.getColumn(), max.getColumn(), min.getRow(), max.getRow());

    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
	Object args[] = new Object[2];
	args[0] = new TableElement (min);
	args[1] = new TableElement (max);
	adapter.invokeCallback (ComponentAdapter.AFTER, "redrawRegion(TableElement,TableElement)", args);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]
  }

  private void setMinimum(TableElement element) {

    // BEFORE-START: before Callback -----------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
	Object args[] = new Object[1];
	args[0] = new TableElement (element);
	Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "setMinimum(TableElement)", args);

      // [Deny] Do not process this method
      if (o instanceof Exception) return;

      // [Override] Leave now 
      if (o == null) return;

      // [Augment] arguments can be modified and used by this function
      element = (TableElement) args[0];
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    minRegionColumn = element.getColumn();
    minRegionRow = element.getRow();
    maxRegionColumn = minRegionColumn;
    maxRegionRow = minRegionRow;

    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
	Object args[] = new Object[1];
	args[0] = new TableElement (element);
	adapter.invokeCallback (ComponentAdapter.AFTER, "setMinimum(TableElement)", args);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]
  }

  /**
   * For optimal computation, maintain the bounds of the inverted region.
   * 
   * The trick is to only redraw what is new.  This gives us a flicker-free experience
   */
  private void maintainRegion() {

    // BEFORE-START: before Callback -----------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
	Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "maintainRegion()", null);

      // [Deny] Do not process this method
      if (o instanceof Exception) return;

      // [Override] Leave now 
      if (o == null) return;

      // [Augment] arguments can be modified and used by this function
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    int oldMinRow    = minRegionRow;
    int oldMaxRow    = maxRegionRow;
    int oldMinColumn = minRegionColumn;
    int oldMaxColumn = maxRegionColumn;

    // nothing to maintain
    if (startTE.equals(nullElement)) return;

    // Fix row
    minRegionRow = startTE.getRow();
    maxRegionRow = endTE.getRow();
    negY = false;
    if (maxRegionRow < minRegionRow) {
      negY = true;
      int tmp = maxRegionRow;
      maxRegionRow = minRegionRow;
      minRegionRow = tmp;
    }

    // verify max value:
    if (maxRegionRow > numRows)
      maxRegionRow = numRows;

    int newMinRow = oldMinRow, newMaxRow = oldMaxRow;
    
    // update 
    if (oldMinRow < minRegionRow) {
      redrawRegion (oldMinColumn, oldMaxColumn, oldMinRow, minRegionRow);
    }
    if (minRegionRow < oldMinRow) {
      redrawRegion (oldMinColumn, oldMaxColumn, minRegionRow, oldMinRow);
      newMinRow = minRegionRow;
    }

    // update 
    if (oldMaxRow > maxRegionRow) {
      redrawRegion (oldMinColumn, oldMaxColumn, maxRegionRow, oldMaxRow);
    }
    if (maxRegionRow > oldMaxRow) {
      redrawRegion (oldMinColumn, oldMaxColumn, oldMaxRow, maxRegionRow);
      newMaxRow = maxRegionRow;
    }

    // Now that we have fixed the row, we never use oldMaxRow/oldMinRow again, but 
    // use newMinRow, newMaxRow
    
    // Fix column
    minRegionColumn = startTE.getColumn();
    maxRegionColumn = endTE.getColumn();
    negX = false;
    if (maxRegionColumn < minRegionColumn) {
      negX = true;
      int tmp = maxRegionColumn;
      maxRegionColumn = minRegionColumn;
      minRegionColumn = tmp;
    }

    // verify max value:
    if (maxRegionColumn > numColumns)
      maxRegionColumn = numColumns;

    // update 
    if (oldMinColumn < minRegionColumn) {
      redrawRegion (oldMinColumn, minRegionColumn, newMinRow, newMaxRow);
    }
    if (minRegionColumn < oldMinColumn) {
      redrawRegion (minRegionColumn, oldMinColumn, newMinRow, newMaxRow);
    }

    // update 
    if (oldMaxColumn > maxRegionColumn) {
      redrawRegion (maxRegionColumn, oldMaxColumn, newMinRow, newMaxRow);
    }
    if (maxRegionColumn > oldMaxColumn) {
      redrawRegion (oldMaxColumn, maxRegionColumn, newMinRow, newMaxRow);
    }

    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
	adapter.invokeCallback (ComponentAdapter.AFTER, "maintainRegion()", null);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]
  }

  /**
   * Returns true if the given <c,r> element is within the inverted region
   */
  private boolean inInvertedRegion(int c, int r) {

    // BEFORE-START: before Callback -----------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
	Object args[] = new Object[2];
	args[0] = new Integer (c);
	args[1] = new Integer (r);
	Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "inInvertedRegion(int,int)", args);

	// [Deny] Do not process this method
	if (o instanceof Exception) return false;

	// [Override] Leave now 
	if (o instanceof Boolean) return ((Boolean) o).booleanValue();

	// [Augment] arguments can be modified and used by this function
	c = ((Integer) args[0]).intValue();
	r = ((Integer) args[1]).intValue();
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    // no region
    boolean retValue = true;

    if (startTE.equals(nullElement)) {
	retValue = false;
    } else if ((c < minRegionColumn) || (c > maxRegionColumn)) {
	retValue = false;
    } else if ((r < minRegionRow)    || (r > maxRegionRow)) {
	retValue = false;
    }

    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
	Object args[] = new Object[2];
	args[0] = new Integer (c);
	args[1] = new Integer (r);
	adapter.invokeCallback (ComponentAdapter.AFTER, "inInvertedRegion(int,int)", args);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]

    return retValue;
  }

  /**
   * Utility function to convert MouseEvent objects into TableElement positions.
   */
  private TableElement convertMouseEvent (MouseEvent evt) {

    // BEFORE-START: before Callback -----------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
	Object args[] = new Object[1];
	args[0] = evt;
	Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "convertMouseEvent(MouseEvent)", args);

	// [Deny] Do not process this method
	if (o instanceof Exception) return null;

	// [Override] Leave now 
	if (o instanceof TableElement) return ((TableElement) o);

	// [Augment] arguments can be modified and used by this function
	evt = (MouseEvent) args[0];
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    int realX = evt.getX();
    int realY = evt.getY();
    if (realX < 0) realX = 0;
    if (realY < 0) realY = 0;
    int downCol = 1+ (realX / (columnWidth + TEXT_XPAD));
    int downRow = 1+ (realY / (rowHeight + TEXT_YPAD));

    if (downCol > numColumns)
      downCol = numColumns;
    if (downRow > numRows)
      downRow = numRows;

    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
	Object args[] = new Object[1];
	args[0] = evt;
	adapter.invokeCallback (ComponentAdapter.AFTER, "convertMouseEvent(MouseEvent)", args);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]
    return new TableElement (downCol, downRow);
  }

  //-----------------------------
  // MouseListener methods.
  // ----------------------------
  /**
   * Ignore mouseClicked events.
   */
  public void mouseClicked(MouseEvent evt) { 
    // BEFORE-START: before Callback -----------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[1];
      args[0] = evt;

      Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "mouseClicked(MouseEvent)", args);

      // [Deny] Do not process this method
      if (o instanceof Exception) return;

      // [Override] Leave now 
      if (o == null) return;

      // [Augment] arguments can be modified and used by this function
      evt = (MouseEvent) args[0];
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[1];
      args[0] = evt;
      adapter.invokeCallback (ComponentAdapter.AFTER, "mouseClicked(MouseEvent)", args);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]
  }

  /**
   * Process mousePressed events.
   *
   * Every mousePressed event turns into a TableEventObject of type MOUSE_PRESS.
   *
   * @param  evt   the MouseEvent object.
   *
   * @see #fireAction
   */
  public void mousePressed(MouseEvent evt) { 
    // BEFORE-START: before Callback -----------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[1];
      args[0] = evt;
      Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "mousePressed(MouseEvent)", args);

      // [Deny] Do not process this method
      if (o instanceof Exception) return;

      // [Override] Leave now 
      if (o == null) return;

      // [Augment] arguments can be modified and used by this function
      evt = (MouseEvent) args[0];
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    deselect();

    startTE = convertMouseEvent (evt);
    endTE = new TableElement (startTE);

    setMinimum(startTE);
    redrawRegion (startTE, startTE);
    TableEventObject teo = new TableEventObject (this, 
						 minRegionColumn, minRegionRow,
						 maxRegionColumn, maxRegionRow,
						 TableEventObject.MOUSE_PRESS);
    fireAction (teo);

    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[1];
      args[0] = evt;
      adapter.invokeCallback (ComponentAdapter.AFTER, "mousePressed()", args);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]
  }

  /**
   * Process mouseReleased events.
   *
   * Every mouseReleased event turns into a TableEventObject of type MOUSE_RELEASE.
   *
   * @param  evt   the MouseEvent object.
   *
   * @see #fireAction
   */
  public void mouseReleased(MouseEvent evt) { 
    // BEFORE-START: before Callback -----------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[1];
      args[0] = evt;
      Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "mouseReleased(MouseEvent)", args);

      // [Deny] Do not process this method
      if (o instanceof Exception) return;

      // [Override] Leave now 
      if (o == null) return;

      // [Augment] arguments can be modified and used by this function
      evt = (MouseEvent) args[0];
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    endTE = convertMouseEvent (evt);

    maintainRegion();
    TableEventObject teo = new TableEventObject (this, 
						 minRegionColumn, minRegionRow,
						 maxRegionColumn, maxRegionRow,
						 TableEventObject.MOUSE_RELEASE);
    fireAction (teo);

    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[1];
      args[0] = evt;
      adapter.invokeCallback (ComponentAdapter.AFTER, "mouseReleased()", args);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]
  }

  /**
   * Ignore mouseEntered events.
   */
  public void mouseEntered(MouseEvent evt) { 
    // BEFORE-START: before Callback -----------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[1];
      args[0] = evt;
      Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "mouseEntered(MouseEvent)", args);

      // [Deny] Do not process this method
      if (o instanceof Exception) return;

      // [Override] Leave now 
      if (o == null) return;

      // [Augment] arguments can be modified and used by this function
      evt = (MouseEvent) args[0];
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[1];
      args[0] = evt;
      adapter.invokeCallback (ComponentAdapter.AFTER, "mouseEntered(MouseEvent)", args);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]
  }

  /**
   * Ignore mouseExited events.
   */
  public void mouseExited(MouseEvent evt) {
    // BEFORE-START: before Callback -----------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[1];
      args[0] = evt;
      Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "mouseExited(MouseEvent)", args);

      // [Deny] Do not process this method
      if (o instanceof Exception) return;

      // [Override] Leave now 
      if (o == null) return;

      // [Augment] arguments can be modified and used by this function
      evt = (MouseEvent) args[0];
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[1];
      args[0] = evt;
      adapter.invokeCallback (ComponentAdapter.AFTER, "mouseExited(MouseEvent)", args);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]
  }

  //-----------------------------
  // MouseMotionListener methods.
  // ----------------------------

  /**
   * Process mouseDragged events.
   *
   * Every mouseDragged event turns into a TableEventObject of type MOUSE_DOWN.
   *
   * @param  evt   the MouseEvent object.
   *
   * @see #fireAction
   */
  public void mouseDragged (MouseEvent evt) { 

    // BEFORE-START: before Callback -----------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[1];
      args[0] = evt;
      Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "mouseDragged(MouseEvent)", args);

      // [Deny] Do not process this method
      if (o instanceof Exception) return;

      // [Override] Leave now 
      if (o == null) return;

      // [Augment] arguments can be modified and used by this function
      evt = (MouseEvent) args[0];
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    endTE = convertMouseEvent (evt);

    maintainRegion();
    TableEventObject teo = new TableEventObject (this, 
						 minRegionColumn, minRegionRow,
						 maxRegionColumn, maxRegionRow,
						 TableEventObject.MOUSE_DOWN);
    fireAction (teo);

    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[1];
      args[0] = evt;
      adapter.invokeCallback (ComponentAdapter.AFTER, "mouseDragged(MouseEvent)", args);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]
  }

  /**
   * Ignore mouseMoved events.
   */
  public void mouseMoved (MouseEvent evt) { 

    // BEFORE-START: before Callback -----------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[1];
      args[0] = evt;
      Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "mouseMoved(MouseEvent)", args);

      // [Deny] Do not process this method
      if (o instanceof Exception) return;

      // [Override] Leave now 
      if (o == null) return;

      // [Augment] arguments can be modified and used by this function
      evt = (MouseEvent) args[0];
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[1];
      args[0] = evt;
      adapter.invokeCallback (ComponentAdapter.AFTER, "mouseMoved(MouseEvent)", args);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]
  }

  //----------------------------------------------------------
  // Methods for registering/deregistering TableEventListeners
  //----------------------------------------------------------

  /**
   * Adds a new TableListener.
   *
   * @param   tl   The TableListener
   *
   * @see #removePropertyChangeListener
   * @see #tableListeners
   */
  public synchronized void addTableListener(TableListener tl) {

    // BEFORE-START: before Callback -----------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[1];
      args[0] = tl;
      Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "addTableListener(TableListener)", args);

      // [Deny] Do not process this method
      if (o instanceof Exception) return;

      // [Override] Leave now 
      if (o == null) return;

      // [Augment] arguments can be modified and used by this function
      tl = (TableListener) args[0];
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    tableListeners.addElement(tl);

    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[1];
      args[0] = tl;
      adapter.invokeCallback (ComponentAdapter.AFTER, "addTableListener(TableListener)", args);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]
  }

  /**
   * Removes an existing TableListener
   *
   * @param   tl   The TableListener
   *
   * @see #addPropertyChangeListener
   * @see #tableListeners
   */
  public synchronized void removeTableListener(TableListener tl) {

    // BEFORE-START: before Callback -----------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[1];
      args[0] = tl;
      Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "removeTableListener(TableListener)", args);

      // [Deny] Do not process this method
      if (o instanceof Exception) return;

      // [Override] Leave now 
      if (o == null) return;

      // [Augment] arguments can be modified and used by this function
      tl = (TableListener) args[0];
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    tableListeners.removeElement(tl);

    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[1];
      args[0] = tl;
      adapter.invokeCallback (ComponentAdapter.AFTER, "removeTableListener(TableListener)", args);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]
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

    // BEFORE-START: before Callback -----------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[1];
      args[0] = teo;
      Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "fireAction(TableEventObject)", args);

      // [Deny] Do not process this method
      if (o instanceof Exception) return;

      // [Override] Leave now 
      if (o == null) return;

      // [Augment] arguments can be modified and used by this function
      teo = (TableEventObject) args[0];
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    Vector targets;
    synchronized (this) {
      targets = (Vector) tableListeners.clone();
    }

    for (int i = 0; i < targets.size(); i++) {
      TableListener target = (TableListener) targets.elementAt(i);
      target.handleTableEvent (teo);
    }

    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[1];
      args[0] = teo;
      adapter.invokeCallback (ComponentAdapter.AFTER, "fireAction(TableEventObject)", args);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]
  }

  /**
   * The specified PropertyChangeListeners <b>propertyChange</b> method will
   * be called each time the value of any bound property is changed.
   *
   * @see #removePropertyChangeListener
   * @param l the PropertyChangeListener
   */      
  public void addPropertyChangeListener(PropertyChangeListener l) {

    // BEFORE-START: before Callback -----------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[1];
      args[0] = l;
      Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "addPropertyChangeListener(PropertyChangeListener)", args);

      // [Deny] Do not process this method
      if (o instanceof Exception) return;

      // [Override] Leave now 
      if (o == null) return;

      // [Augment] arguments can be modified and used by this function
      l = (PropertyChangeListener) args[0];
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    changes.addPropertyChangeListener(l);

    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[1];
      args[0] = l;
      adapter.invokeCallback (ComponentAdapter.AFTER, "addPropertyChangeListener(PropertyChangeListener)", args);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]
  }

  /** 
   * Remove this PropertyChangeListener from the buttons internal list.  
   * If the PropertyChangeListener isn't on the list, silently do nothing.
   * 
   * @see #addPropertyChangeListener
   * @param l the PropertyChangeListener
   */      
  public void removePropertyChangeListener(PropertyChangeListener l) {

    // BEFORE-START: before Callback -----------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[1];
      args[0] = l;
      Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "removePropertyChangeListener(PropertyChangeListener)", args);

      // [Deny] Do not process this method
      if (o instanceof Exception) return;

      // [Override] Leave now 
      if (o == null) return;

      // [Augment] arguments can be modified and used by this function
      l = (PropertyChangeListener) args[0];
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    changes.removePropertyChangeListener(l);

    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[1];
      args[0] = l;
      adapter.invokeCallback (ComponentAdapter.AFTER, "removePropertyChangeListener(PropertyChangeListener)", args);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]
  }

  /** 
   * Set the number of columns to table.  This is a bound property.
   * 
   * @see #getNumColumns
   */      
  public void setNumColumns(int newCols) {

    // BEFORE-START: before Callback -----------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[1];
      args[0] = new Integer (newCols);
      Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "setNumColumns(int)", args);

      // [Deny] Do not process this method
      if (o instanceof Exception) return;

      // [Override] Leave now 
      if (o == null) return;

      // [Augment] arguments can be modified and used by this function
      newCols = ((Integer) args[0]).intValue();
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    int oldCols = numColumns;
    numColumns = newCols;
    sizeToFit();
    changes.firePropertyChange("numColumns",
			       new Integer(oldCols), new Integer(newCols));

    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[1];
      args[0] = new Integer (newCols);
      adapter.invokeCallback (ComponentAdapter.AFTER, "setNumColumns(int)", args);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]
  }
  
  /** 
   * Return the number of columns
   * 
   * @see #setNumColumns
   */
  public int getNumColumns() {

    // BEFORE-START: before Callback -----------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "getNumColumns()", null);

      // [Deny] Do not process this method
      if (o instanceof Exception) return 0;

      // [Override] Leave now 
      if (o instanceof Integer) return ((Integer) o).intValue();

      // [Augment] arguments can be modified and used by this function
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      adapter.invokeCallback (ComponentAdapter.AFTER, "getNumColumns()", null);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]
    return numColumns;
  }
  
  /** 
   * Set the number of rows to table.  This is a bound property.
   * 
   * @see #getNumRows
   */      
  public void setNumRows(int newRows) {

    // BEFORE-START: before Callback -----------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[1];
      args[0] = new Integer (newRows);
      Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "setNumRows(int)", args);

      // [Deny] Do not process this method
      if (o instanceof Exception) return;

      // [Override] Leave now 
      if (o == null) return;

      // [Augment] arguments can be modified and used by this function
      newRows = ((Integer) args[0]).intValue();
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    int oldRows = numRows;
    numRows = newRows;
    sizeToFit();
    changes.firePropertyChange("numRows",
			       new Integer(oldRows), new Integer(newRows));

    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[1];
      args[0] = new Integer (newRows);
      adapter.invokeCallback (ComponentAdapter.AFTER, "setNumRows()", args);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]
  }
  
  /** 
   * Return the number of rows
   * 
   * @see #setNumRows
   */
  public int getNumRows() {
    // BEFORE-START: before Callback -----------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "getNumRows()", null);

      // [Deny] Do not process this method
      if (o instanceof Exception) return 0;

      // [Override] Leave now 
      if (o instanceof Integer) return ((Integer) o).intValue();

      // [Augment] arguments can be modified and used by this function
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      adapter.invokeCallback (ComponentAdapter.AFTER, "getNumRows()", null);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]
    return numRows;
  }

  /**
   * Detetermines the base size of this component when shown on the screen.
   *
   * @see #columnWidth;
   * @see #rowHeight;
   */
  public Dimension getPreferredSize() {

    // BEFORE-START: before Callback -----------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "getPreferredSize()", null);

      // [Deny] Do not process this method
      if (o instanceof Exception) return null;

      // [Override] Leave now 
      if (o instanceof Dimension)  return ((Dimension) o);

      // [Augment] arguments can be modified and used by this function
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    // Allow 2 pixel for borders
    Dimension d =  new Dimension(numColumns * (columnWidth+TEXT_XPAD) + 4,
				 numRows * (rowHeight+TEXT_YPAD) + 4);

    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      adapter.invokeCallback (ComponentAdapter.AFTER, "getPreferredSize()", null);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]

    return d;
  }

  /**
   * This method will dynamically refresh the GUI object if any of its key
   * parameters are changed.
   */
  private void sizeToFit() {

    // BEFORE-START: before Callback -----------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "sizeToFit()", null);

      // [Deny] Do not process this method
      if (o instanceof Exception) return;

      // [Override] Leave now 
      if (o == null) return;

      // [Augment] arguments can be modified and used by this function
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]
    Dimension d = getPreferredSize();
    setBounds (0, 20, d.width, d.height);
    Component p = getParent();
    if (p != null) {
      p.invalidate();
      p.doLayout();
    }

    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      adapter.invokeCallback (ComponentAdapter.AFTER, "sizeToFit()", null);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]
  }

  /**
   * The supporting object to help with PropertyChange events
   */
  private PropertyChangeSupport changes = new PropertyChangeSupport(this);

  /**
   * The vector that contains the list of TableListeners.
   */
  private Vector tableListeners = new Vector();

  /**
   * The Hashtable that stores all Strings to be displayed.
   */
  private Hashtable values = new Hashtable ();

  /**
   * Horizontal Border offset 
   */
  static final int TEXT_XPAD = 2;

  /**
   * Vertical Border offset 
   */
  static final int TEXT_YPAD = 2;

  // ------------------------------
  // Adaptable Interface
  // ------------------------------
  private ComponentAdapter adapter = null;

  // Inserts the adapter object
  public void setAdapter (ComponentAdapter ca) { adapter = ca; }

  // Retrieves the adapter object
  public ComponentAdapter getAdapter () { return adapter; }
}
