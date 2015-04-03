// Class  : TableRegion
// Author : George Heineman
//        : heineman@cs.wpi.edu
//        : http://www.cs.wpi.edu/~heineman
// Version: 1.0

package gui;

/**
 * This is a simple class to represent a rectangular region in the table
 */

import java.util.Enumeration;
import java.io.Serializable;

//
// It is assumed (and can be easily checked) that startTE is
// above and to the left of endTE.
public class TableRegion implements Enumeration, Serializable {
  private TableElement startTE = null;
  private TableElement endTE = null;
  private TableElement currentTE = null;

  private TableElement badElement = new TableElement (-1, -1);

  /**
   * Constructs a TableRegion from two TableElements
   */
  public TableRegion(TableElement sTE, TableElement eTE) {
    startTE = new TableElement (sTE);
    endTE = new TableElement (eTE);
    currentTE = new TableElement (sTE);
  }

  /**
   * Constructs a TableRegion from another TableRegion
   */
  public TableRegion(TableRegion tr) {
    startTE = new TableElement(tr.getStart());
    endTE = new TableElement (tr.getEnd());
    currentTE = new TableElement (startTE);
  }

  /**
   * In regions of one (i.e., 1:1) we need to iterate over the
   * single element, so termination is signaled by a null pointer
   *
   * @see @nextElement
   */
  public boolean hasMoreElements() {
    if (currentTE == null) return false;
    return true;
  }

  /**
   * Iterate horizontally, then vertically through the region
   *
   * @see #hasMoreElements
   */
  public Object nextElement () {
    TableElement element = new TableElement (currentTE);
    int  nextColumn = element.getColumn() + 1;
    int  nextRow = element.getRow() + 1;

    // Still fits within column
    if (nextColumn <= endTE.getColumn())
      currentTE.setColumn (nextColumn);
    else {
      // Bump down to the next row
      if (nextRow <= endTE.getRow()) {
	currentTE.setColumn (startTE.getColumn());
	currentTE.setRow (nextRow);
      }
      else {
	// All Done
	currentTE = null;
      }
    }
    return element;
  }

  public TableElement getStart () { return startTE; }
  public void setStart (TableElement sTE) { startTE = sTE; }

  public TableElement getEnd () { return endTE; }
  public void setEnd (TableElement eTE) { endTE = eTE; }

  public boolean equals (TableRegion tr) {
    if (tr == null) return false;
    if (tr.getStart().isSame (startTE))
      if (tr.getEnd().isSame (endTE))
	return true;

    return false;
  }

  public boolean isSame (TableRegion tr) {
    if (tr == null) return false;
    if (tr.getStart().isSame (startTE))
      if (tr.getEnd().isSame (endTE))
	return true;

    return false;
  }

  public boolean isValid () {
    return ((startTE != badElement) && (endTE != badElement));
  }

  public static boolean isTableRegion (String s) {
    return (s.indexOf (':') != -1);
  }

  public String toString () {
    if (startTE == null) return "0";
    String s = startTE.toString() + ":" + endTE.toString();
    if (currentTE == null) return (s + " [done]");
    return s + " [next:" + currentTE.toString() + "]";
  }

  public String toString (boolean isShort) {
    if (!isShort) return toString ();
    if (startTE == null) return "0";
    if (startTE.isSame (endTE))
      return startTE.toString();
    return startTE.toString() + ":" + endTE.toString();
  }

}
