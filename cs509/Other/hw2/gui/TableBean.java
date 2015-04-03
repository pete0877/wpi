// Class  : TableBean
// Author : George Heineman
//        : heineman@cs.wpi.edu
//        : http://www.cs.wpi.edu/~heineman
// Version: 1.0

package gui;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

import java.io.Serializable;
import java.util.Vector;
import java.util.Hashtable;

import adapt.ComponentAdapter;
import adapt.Adaptable;

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
  }
  
  /**
   * Constructs a TableBean 
   *
   * @param #cols the width 
   * @param #rows the height
   */
  public TableBean(int cols, int rows) {
    super ();
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
      Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "getTableValue", args);
      
      // Simply return this value instead
      String retString = (String) o;
      return retString;
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    String key = viewCol + ":" + viewRow;
    String val = (String) values.get (key);
    return (val); 
  }

  /**
   * Clear all values in the Table.
   *
   * @see #setTableValue
   * @see #getTableValue
   */
  public void clearTableValue () {
    values = new Hashtable ();
  }


  /**
   * Returns the height of a table Row
   * 
   * @see #setRowHeight
   */
  public int getRowHeight() {
    return rowHeight;
  }

  /**
   * Set the height of a table Row.  This is a bound property
   *
   * @param newHeight the height
   * @see #getRowHeight
   */
  public void setRowHeight(int newHeight) {
    int oldHeight = rowHeight;
    rowHeight = newHeight;
    sizeToFit();
    changes.firePropertyChange("rowHeight",
    			       new Integer(oldHeight), new Integer(rowHeight));
  }

  /**
   * Returns the width of a table Column
   * 
   * @see #setColumnWidth
   */
  public int getColumnWidth() {
    return columnWidth;
  }

  /**
   * Set the width of a table Column.  This is a bound property
   *
   * @param newWidth the new width
   * @see #getColumnWidth
   */
  public void setColumnWidth(int newWidth) {
    int oldWidth = columnWidth;
    columnWidth = newWidth;
    sizeToFit();
    changes.firePropertyChange("columnWidth",
    			       new Integer(oldWidth), new Integer(columnWidth));
  }

  /**
   * Returns the foreground color for the bean
   * 
   * @see #setForeground
   */
  public Color getForeground() {
    return super.getForeground();
  }

  /**
   * Set the color the table is drawn with.  This is a bound property.
   * 
   * @see #getForeground
   */
  public void setForeground(Color c) {
    Color old = foreColor;
    super.setForeground (foreColor = c);
    changes.firePropertyChange("foreground", old, c);
    repaint();
  }

  /**
   * Returns the background color for the Bean
   * 
   * @see #setBackground
   */
  public Color getBackground() {
    return super.getBackground();
  }

  /**
   * Set the background color of the table.  This is a bound property.
   *
   * @see #getBackground
   */
  public void setBackground(Color c) {
    Color old = backColor;
    super.setBackground (backColor = c);
    changes.firePropertyChange("background", old, c);
    repaint();
  }

  /**
   * Set the current font.  This is a bound property.
   *
   * @see #getFont
   */
  public void setFont(Font f) {
    Font old = getFont();
    super.setFont (font = f);
    repaint();
    changes.firePropertyChange("font", old, f);
  }

  /**
   * Returns the current font.  This is a bound property.
   *
   * @see #setFont
   */
  public Font getFont() {
    return super.getFont();
  }

  /**
   * Paint the Table Bean.
   */
  public synchronized void paint(Graphics g) {
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
  }

  protected synchronized void redraw (Graphics g, int viewCol, int viewRow) {
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
    if (startTE.equals(nullElement))
      return nullRegion;
    return new TableRegion (startTE, endTE);
  }

  /**
   * Sets the selected region to be the specified TableRegion.
   *
   * @see #getSelectedRegion
   */
  public void setSelectedRegion (TableRegion tr) {
    if (tr == null) {
      deselect();
      return;
    }

    TableRegion old = new TableRegion (startTE, endTE);
    TableRegion newOne = new TableRegion (tr);

    startTE = new TableElement (tr.getStart());
    clip (startTE);
    
    endTE = new TableElement (tr.getEnd());
    clip (endTE);

    setMinimum(startTE);
    maintainRegion();
  }

  /**
   * Select an invertedRegion; If unchanged, return false, otherwise true.
   * 
   * @param     sTE   start TableElement
   * @param     eTE   end TableElement
   * @returns   true  if a new region has been selected; false otherwise
   */
  public void setSelectedRegion (TableElement sTE, TableElement eTE) {
    setSelectedRegion (new TableRegion (sTE, eTE));
  }

  /**
   * clips the TableElement to fit within Table
   */
  private void clip (TableElement element) {
    if (element.getColumn() < minColumn)
      element.setColumn(minColumn);
    if (element.getRow() < minRow)
      element.setColumn(minRow);

    if (element.getColumn() > numColumns)
      element.setColumn(numColumns);
    if (element.getRow() > numRows)
      element.setColumn(numRows);
  }

  /**
   * Deselect any invertedRegion.
   *
   * Used internally to clean up.
   */
  private void deselect() {
    startTE = nullElement;
    endTE = nullElement;
    redrawRegion (minRegionColumn, maxRegionColumn, minRegionRow, maxRegionRow);

    minRegionColumn = numColumns;
    maxRegionColumn = minColumn;
    minRegionRow = numRows;
    maxRegionRow = minRow;
  }

  /**
   * Redraws the given region based on ABSOLUTE COORDINATES, not
   * current screen coordinates
   */
  private void redrawRegion(int cmin, int cmax, int rmin, int rmax) {
    Graphics g = getGraphics();
    for (int c = cmin; c <= cmax; c++) 
      for (int r = rmin; r <= rmax; r++) {
	redraw (g, c, r);
      }
  }

  /** 
   * redraws the given region based on start/end TableElements
   * 
   */
  private void redrawRegion(TableElement min, TableElement max) {
    redrawRegion (min.getColumn(), max.getColumn(), min.getRow(), max.getRow());
  }

  private void setMinimum(TableElement element) {
    minRegionColumn = element.getColumn();
    minRegionRow = element.getRow();
    maxRegionColumn = minRegionColumn;
    maxRegionRow = minRegionRow;
  }

  /**
   * For optimal computation, maintain the bounds of the inverted region.
   * 
   * The trick is to only redraw what is new.  This gives us a flicker-free experience
   */
  private void maintainRegion() {
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
  }

  /**
   * Returns true if the given <c,r> element is within the inverted region
   */
  private boolean inInvertedRegion(int c, int r) {
    // no region
    if (startTE.equals(nullElement)) return false;

    if ((c < minRegionColumn) || (c > maxRegionColumn)) return false;
    if ((r < minRegionRow)    || (r > maxRegionRow)) return false;      
    return true;
  }

  /**
   * Utility function to convert MouseEvent objects into TableElement positions.
   */
  private TableElement convertMouseEvent (MouseEvent evt) {
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
    return new TableElement (downCol, downRow);
  }

  //-----------------------------
  // MouseListener methods.
  // ----------------------------
  /**
   * Ignore mouseClicked events.
   */
  public void mouseClicked(MouseEvent evt) { }

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
    endTE = convertMouseEvent (evt);

    maintainRegion();
    TableEventObject teo = new TableEventObject (this, 
						 minRegionColumn, minRegionRow,
						 maxRegionColumn, maxRegionRow,
						 TableEventObject.MOUSE_RELEASE);
    fireAction (teo);
  }

  /**
   * Ignore mouseEntered events.
   */
  public void mouseEntered(MouseEvent evt) { }

  /**
   * Ignore mouseExited events.
   */
  public void mouseExited(MouseEvent evt) { }

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
    endTE = convertMouseEvent (evt);

    maintainRegion();
    TableEventObject teo = new TableEventObject (this, 
						 minRegionColumn, minRegionRow,
						 maxRegionColumn, maxRegionRow,
						 TableEventObject.MOUSE_DOWN);
    fireAction (teo);
  }

  /**
   * Ignore mouseMoved events.
   */
  public void mouseMoved (MouseEvent evt) { }

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
    tableListeners.addElement(tl);
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
    tableListeners.removeElement(tl);
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

  /**
   * The specified PropertyChangeListeners <b>propertyChange</b> method will
   * be called each time the value of any bound property is changed.
   *
   * @see #removePropertyChangeListener
   * @param l the PropertyChangeListener
   */      
  public void addPropertyChangeListener(PropertyChangeListener l) {
    changes.addPropertyChangeListener(l);
  }

  /** 
   * Remove this PropertyChangeListener from the buttons internal list.  
   * If the PropertyChangeListener isn't on the list, silently do nothing.
   * 
   * @see #addPropertyChangeListener
   * @param l the PropertyChangeListener
   */      
  public void removePropertyChangeListener(PropertyChangeListener l) {
    changes.removePropertyChangeListener(l);
  }

  /** 
   * Set the number of columns to table.  This is a bound property.
   * 
   * @see #getNumColumns
   */      
  public void setNumColumns(int newCols) {
    int oldCols = numColumns;
    numColumns = newCols;
    sizeToFit();
    changes.firePropertyChange("numColumns",
			       new Integer(oldCols), new Integer(newCols));
  }
  
  /** 
   * Return the number of columns
   * 
   * @see #setNumColumns
   */
  public int getNumColumns() {
    return numColumns;
  }
  
  /** 
   * Set the number of rows to table.  This is a bound property.
   * 
   * @see #getNumRows
   */      
  public void setNumRows(int newRows) {
    int oldRows = numRows;
    numRows = newRows;
    sizeToFit();
    changes.firePropertyChange("numRows",
			       new Integer(oldRows), new Integer(newRows));
  }
  
  /** 
   * Return the number of rows
   * 
   * @see #setNumRows
   */
  public int getNumRows() {
    return numRows;
  }

  /**
   * Detetermines the base size of this component when shown on the screen.
   *
   * @see #columnWidth;
   * @see #rowHeight;
   */
  public Dimension getPreferredSize() {
    // Allow 2 pixel for borders
    Dimension d =  new Dimension(numColumns * (columnWidth+TEXT_XPAD) + 4,
				 numRows * (rowHeight+TEXT_YPAD) + 4);
    return d;
  }

  /**
   * This method will dynamically refresh the GUI object if any of its key
   * parameters are changed.
   */
  private void sizeToFit() {
    Dimension d = getPreferredSize();
    setBounds (0, 20, d.width, d.height);
    Component p = getParent();
    if (p != null) {
      p.invalidate();
      p.doLayout();
    }
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
