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

public class TableBean extends java.awt.Component implements Serializable, MouseMotionListener, MouseListener, TableReader {
  
  /**
   * Constructs a TableBean
   */
  public TableBean() {
    		this (1, 1);
		//{{INIT_CONTROLS
		setNumRows(0);
		setBorder(0);
		setNumColumns(0);
		setPadding(0);
		setSize(0,0);
		//}}

	}
	//{{DECLARE_CONTROLS
	//}}


  // Some constants
  private static final int minRow = 1;
  private static final int minColumn = 1;
  public static final int defaultWidth = 50;
  public static final int defaultHeight = 12;
  public static final int minimumWidth = 10;
  public static final int minimumHeight = 5;

  /**
   * properties of this table bean
   */
  protected int     numRows = 0;
  protected int     numColumns = 0;
  protected int     border = 2;
  protected int     padding = 2;
  protected boolean editable = true;
  protected int     columnWidths[];
  protected int     rowHeights[];
  protected int     absoluteColumnValues[];
  protected int     absoluteRowHeights[];
  
  protected Color   foreColor = Color.black;
  protected Color   backColor = Color.lightGray;
  protected Font    font = new Font ("Dialog", Font.PLAIN, 12);

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

    // auto initialize values
    columnWidths = new int[numColumns];
    absoluteColumnValues = new int[numColumns];
    for (int i = 0; i < numColumns; i++)
	columnWidths[i] = defaultWidth;

    rowHeights = new int[numRows];
    absoluteRowHeights = new int[numRows];
    for (int i = 0; i < numRows; i++)
	rowHeights[i] = defaultHeight;

    recalculateColumnValues(minColumn);
    recalculateRowValues(minRow);
    sizeToFit();

    // Don't forget to track for mouse clicks and moves
    addMouseListener(this);
    addMouseMotionListener(this);
  }

  /**
   * Returns the unique hashcode based on the (c,r) value.
   */
  protected String hashKey (int viewCol, int viewRow) {
      return viewCol + ":" + viewRow;
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

    String key = hashKey (viewCol, viewRow);
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
    String key = hashKey (viewCol, viewRow);
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
   * Set the height of a table Row.  This is a bound property
   *
   * @param newHeight the height
   * @see #getRowHeight
   */
  public void setRowHeight(int newHeight) {
    for (int i = 0; i < numRows; i++) {
       int oldHeight = rowHeights[i];
       rowHeights[i] = newHeight;
       changes.firePropertyChange("rowHeight-" + i,
    		       new Integer(oldHeight), new Integer(newHeight));
    }    			       
    recalculateRowValues(minRow);
    sizeToFit(); 			       
  }

  /**
   * Set the height of a table Row.  This is a bound property
   *
   * @param newWidth the new height
   * @see #getRowHeight
   */
  public void setRowHeight(int row, int newHeight) {
    if (row < minRow) return;
    if (row > minRow + numRows) return;
    
    int oldHeight = rowHeights[row-1];
    rowHeights[row-1] = newHeight;

    recalculateRowValues(row);
    sizeToFit();
    changes.firePropertyChange("rowHeight-" + row,
    			       new Integer(oldHeight), new Integer(newHeight));
  }

  /**
   * Set the width of a table Column.  This is a bound property
   *
   * @param newWidth the new width
   * @see #getColumnWidth
   */
  public void setColumnWidth(int newWidth) {
    for (int i = 0; i < numColumns; i++) {
       int oldWidth = columnWidths[i];
       columnWidths[i] = newWidth;
       
       changes.firePropertyChange("columnWidth-" + i,
    		       new Integer(oldWidth), new Integer(newWidth));
    }
    recalculateColumnValues(minColumn);
    sizeToFit();
  }

  /**
   * Set the width of a table Column.  This is a bound property
   *
   * @param newWidth the new width
   * @see #getColumnWidth
   */
  public void setColumnWidth(int col, int newWidth) {
    if (col < minColumn) return;
    if (col > minColumn + numColumns) return;
    
    int oldWidth = columnWidths[col-1];
    columnWidths[col-1] = newWidth;

    recalculateColumnValues(col);
    sizeToFit();
    changes.firePropertyChange("columnWidth-" + col,
    			       new Integer(oldWidth), new Integer(newWidth));
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

    g.setColor(backColor);
    g.fill3DRect(0, 0, w - 1, h - 1, false);

    g.setColor(foreColor);
    g.setFont(getFont());

    redrawRegion (minColumn, minColumn + numColumns - 1,
		  minRow, minRow + numRows - 1);
  }

  protected synchronized void redraw (Graphics g, int viewCol, int viewRow) {
    boolean inverted = inInvertedRegion (viewCol, viewRow);

    if (inverted)
      g.setColor(foreColor);
    else
      g.setColor(backColor);

    int topCorner = absoluteColumnValues[viewCol-1];
    int leftCorner = absoluteRowHeights[viewRow-1];
	g.fillRect (topCorner, leftCorner, 2*border+columnWidths[viewCol-1], 2*border+rowHeights[viewRow-1]);

    g.setColor(Color.black);
    g.drawRect (topCorner, leftCorner,
		2*border+columnWidths[viewCol-1], 2*border+rowHeights[viewRow-1]);

    if (inverted)
      g.setColor (backColor);
    else
      g.setColor (foreColor);

    String s = getTableValue (viewCol, viewRow);
    if (s != null)
      g.drawString (s,
		    border + topCorner,
		    border + leftCorner + rowHeights[viewRow-1]);
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
    if (startTE.isValid()) {
      startTE = nullElement;
      endTE = nullElement;
      redrawRegion (minRegionColumn, maxRegionColumn, minRegionRow, maxRegionRow);
    }
    
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
    if (maxRegionRow < minRegionRow) {
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
    if (maxRegionColumn < minRegionColumn) {
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

    int downCol = numColumns;
    for (int i = 0; i < numColumns; i++) {
        if (absoluteColumnValues[i] > realX) {
            downCol = i;
            break;
        }
    }

    int downRow = numRows;
    for (int i = 0; i < numRows; i++) {
        if (absoluteRowHeights[i] > realY) {
            downRow = i;
            break;
        }
    }
    
    return new TableElement (downCol, downRow);
  }

  /**
   * Utility function to detect MouseEvent objects that fall on a column. If so,
   * returns the column being modified or -1 if no column being modified.
   */
  private int selectedColumn (MouseEvent evt) {
    int realX = evt.getX();

    for (int i = 1; i < numColumns; i++) {
        if (Math.abs (absoluteColumnValues[i] - realX) <= 2) {
            return i;
        }
    }
    if (Math.abs (absoluteColumnValues[numColumns-1]+2*border+columnWidths[numColumns-1] - realX) <= 2) {
        return numColumns;
    }
    
    return -1;
  }

  /**
   * Utility function to detect MouseEvent objects that fall on a row. If so,
   * returns the row being modified or -1 if no row being modified.
   */
  private int selectedRow (MouseEvent evt) {
    int realY = evt.getY();

    for (int i = 1; i < numRows; i++) {
        if (Math.abs (absoluteRowHeights[i] - realY) <= 2) {
            return i;
        }
    }
    if (Math.abs (absoluteRowHeights[numRows-1]+2*border+rowHeights[numRows-1] - realY) <= 2) {
        return numRows;
    }
    
    return -1;
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
   * Unless we are moving a column/row boundary. Then we trap and treat internally
   * @param  evt   the MouseEvent object.
   *
   * @see #fireAction
   */
  public void mousePressed(MouseEvent evt) {
    if (capableOfEditing && !currentlyEditing) {
        currentlyEditing = true;
    } else
	mousePressedInCell (evt);
  }

  /**
   * Processes mouse event once it is known to be in a cell
   */
  protected void mousePressedInCell (MouseEvent evt) {
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
    if (capableOfEditing && currentlyEditing) {
        currentlyEditing = false;
        currentlyEditingColumn = -1;
        currentlyEditingRow = -1;
    } else
	mouseReleasedInCell (evt);
  }

  /**
   * Processes mouse event once it is known to be in a cell
   */
  protected void mouseReleasedInCell (MouseEvent evt) {
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
    if (currentlyEditing) {
        if (currentlyEditingColumn != -1) {
            setColumnWidth (currentlyEditingColumn,
	        Math.max (minimumWidth,
	        evt.getX() - absoluteColumnValues[currentlyEditingColumn-1]));
        } else if (currentlyEditingRow != -1) {
            setRowHeight (currentlyEditingRow, 
                Math.max (minimumHeight,
		evt.getY() - absoluteRowHeights[currentlyEditingRow-1]));
        }
    } else 
	mouseDraggedInCell (evt);
  }

  /**
   * Processes mouse event once it is known to be in a cell
   */
  protected void mouseDraggedInCell (MouseEvent evt) {
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
  protected boolean capableOfEditing = false;   // determines when this was set...
  protected boolean currentlyEditing = false;   // determines whether we are in a drag situation
  protected int currentlyEditingRow = -1;
  protected int currentlyEditingColumn = -1;
    
  public void mouseMoved (MouseEvent evt) {
    // only allow cursor changes when editable Property is on.
    if (! editable) return;
    
    // if over square, normal cursor
    int col, row;
    currentlyEditingColumn = -1;
    currentlyEditingRow = -1;

    if ((col = selectedColumn (evt)) != -1) {
        currentlyEditingColumn = col;
        // if over lines, move to || cursor
        setCursor (Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        if (!currentlyEditing)
          capableOfEditing = true;
    } else if ((row = selectedRow (evt)) != -1) {
        currentlyEditingRow = row;
        // if over lines, move to || cursor
        setCursor (Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        if (!currentlyEditing)
          capableOfEditing = true;
    } else {
        setCursor (Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        if (!currentlyEditing)
          capableOfEditing = false;
    }
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
    int oldOnes[] = columnWidths;
    columnWidths = new int [numColumns];
    absoluteColumnValues = new int [numColumns]; // must recalculate this now
			       
    for (int i = 0; i < numColumns; i++) {
        columnWidths[i] = defaultWidth;
        if (i < oldCols) columnWidths[i] = oldOnes[i];
    }
    recalculateColumnValues (minColumn);
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
    int oldOnes[] = rowHeights;
    rowHeights = new int [numRows];
    absoluteRowHeights = new int [numRows];
    
    for (int i = 0; i < numRows; i++) {
        rowHeights[i] = defaultHeight;
        if (i < oldRows) rowHeights[i] = oldOnes[i];
    }

    recalculateRowValues (minRow);
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
   * Note: The preferred size will always be the initial size of the component
   * @see #columnWidth;
   * @see #rowHeight;
   */
  public Dimension getPreferredSize() {
    Dimension d = new Dimension (padding +
				 absoluteColumnValues[numColumns-1] +
				 columnWidths[numColumns-1] +
				 2*border + 
				 padding,
				 padding +
				 absoluteRowHeights[numRows-1] + 
				 rowHeights[numRows-1] + 
				 2*border +
				 padding);
    return d;
  }

  /**
   * Dynamically refresh the GUI object if any of its key
   * parameters are changed.
   */
  private void sizeToFit() {
    Dimension d = getPreferredSize();
    Rectangle here = getBounds();
    setBounds (here.x, here.y, d.width, d.height);

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

	static public void main(String args[])
	{
		class DriverFrame extends java.awt.Frame implements TableListener {
		    protected TableBean tb;
		    public void handleTableEvent (TableEventObject teo) {
		        System.out.println ("Received:" + teo);
		    }
		    
			public DriverFrame() {
				addWindowListener(new java.awt.event.WindowAdapter() {
					public void windowClosing(java.awt.event.WindowEvent event)
					{
						dispose();	  // free the system resources
						System.exit(0); // close the application
					}
				});
				this.setLayout(new java.awt.BorderLayout());
				tb = new TableBean (5,4);
				tb.setTableValue (1,1, "TableBean");
				tb.setTableValue (1,2, "heineman@cs.wpi.edu");
				tb.setBorder(2);
				tb.setPadding(2);
				this.add(tb);
				tb.addTableListener (this);
				Dimension d = tb.getPreferredSize();

				this.setSize (d.width + 6, d.height+26);
				}
		}

		new DriverFrame().show();
	}

  /**
   * Set the padding the table is drawn with.  This is a bound property.
   *
   * @see #getForeground
   */
  public void setPadding(int i) {
    Integer old = new Integer (border);
    Integer newB = new Integer (i);
    padding = i;
    recalculateRowValues (minRow);
    recalculateColumnValues (minColumn);
    sizeToFit();
    changes.firePropertyChange("padding", old, newB);
  }


  /**
   * Returns the padding offset for the bean. This is the number of pixels
   * inset the boxes within which the actual text resides.
   *
   * @see #setPadding
   */
  public int getPadding() {
    return padding;
  }

  private void recalculateColumnValues (int minC) {
    if (minC < minColumn) return;
    for (int i = minC; i < numColumns; i++) {
      if (i > 0)
        absoluteColumnValues[i] = absoluteColumnValues[i-1] + columnWidths[i-1] + 2*border;
      else
        absoluteColumnValues[i] = padding;
    }
  }
  
  private void recalculateRowValues (int minR) {
    if (minR < minRow) return;

    for (int i = minR - 1; i < numRows; i++) {
      if (i > 0)
        absoluteRowHeights[i] = absoluteRowHeights[i-1] + rowHeights[i-1] + 2*border;
      else
        absoluteRowHeights[i] = padding;
    }
  }
  
  /**
   * Set the border the table is drawn with.  This is a bound property.
   *
   * @see #getForeground
   */
  public void setBorder(int b) {
    Integer old = new Integer (border);
    Integer newB = new Integer (b);
    border = b;
    recalculateRowValues (minRow);
    recalculateColumnValues (minColumn);
    sizeToFit();
    changes.firePropertyChange("border", old, newB);
  }
	
  /**
   * Returns the border offset for the bean. This is the number of pixels
   * inset around the entire frame
   *
   * @see #setBorder
   */
  public int getBorder() {
    return border;
  }



  /**
   * Determines whether this bean is Editable (meaning moving row/columns)
   *
   * @see #setEditable
   */
  public boolean getEditable() {
    return editable;
  }

  /**
   * Set the editable property.
   *
   * @see #getEditable
   */
  public void setEditable(boolean b) {
    editable = b;
  }
	
	
}
