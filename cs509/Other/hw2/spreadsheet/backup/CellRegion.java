// Class  : CellRegion
// Author : George Heineman
//        : heineman@cs.wpi.edu
//        : http://www.cs.wpi.edu/~heineman
// Version: 1.0

package adapt.spreadsheet;

/**
 * This is a simple class to represent a Cell Region in the spreadsheet
 */

import java.util.Enumeration;
import java.io.Serializable;

//
// It is assumed (and can be easily checked) that startCell is
// above and to the left of endCell.
public class CellRegion implements Enumeration, Serializable {
  private Cell startCell = null;
  private Cell endCell = null;
  private Cell currentCell = null;

  private Cell badCell = new Cell (-1, -1);

  /**
   * Constructs a CellRegion from two cells
   */
  public CellRegion(Cell sc, Cell ec) {
    startCell = sc;
    endCell = ec;
    currentCell = new Cell (sc);
  }

  /**
   * Constructs a CellRegion from another CellRegion
   */
  public CellRegion(CellRegion cr) {
    startCell = new Cell(cr.getStart());
    endCell = new Cell (cr.getEnd());
    currentCell = new Cell (startCell);
  }

  /**
   * In regions of one cell (i.e., A1:A1) we need to iterate over the
   * single cell, so termination is signaled by a null pointer
   *
   * @see @nextElement
   */
  public boolean hasMoreElements() {
    if (currentCell == null) return false;
    return true;
  }

  /**
   * Iterate horizontally, then vertically through the region
   *
   * @see #hasMoreElements
   */
  public Object nextElement () {
    Cell cell = new Cell (currentCell);
    int  nextColumn = cell.getColumn() + 1;
    int  nextRow = cell.getRow() + 1;

    // Still fits within column
    if (nextColumn <= endCell.getColumn())
      currentCell.setColumn (nextColumn);
    else {
      // Bump down to the next row
      if (nextRow <= endCell.getRow()) {
	currentCell.setColumn (startCell.getColumn());
	currentCell.setRow (nextRow);
      }
      else {
	// All Done
	currentCell = null;
      }
    }
    return cell;
  }

  /**
   * Constructs a CellRegion from a String
   */
  public CellRegion(String info) {
    boolean lettersOnly = true;

    int c = 0, r = 0;
    startCell = null;
    endCell = null;
    try {
      for (int i = 0; i < info.length(); i++) {
	char ch = info.charAt(i);
	if (Character.isLetter(ch)) {
	  if (!lettersOnly) 
	    throw new Exception("Bad Format");
	  c = c * 26 + (int) (ch - 'A' + 1);
	}
	else if (Character.isDigit(ch)) {
	  if (lettersOnly) {
	    if (c == 0) throw new Exception ("Bad Format");
	    lettersOnly = false;
	  }

	  r = r * 10 + (int) (ch - '0');
	} else if (ch == ':') {
	  if (c == 0) throw new Exception ("Bad Format");
	  startCell = new Cell (c, r);
	  c = 0;
	  r = 0;
	  lettersOnly = true;
	}
      }
      if (c == 0) throw new Exception ("Bad Format");
      endCell = new Cell (c, r);

      // no ':' divider, so must be a singleton region
      if (startCell == null)
	startCell = new Cell (c, r);

      // Make sure to start iteration off at the start
      // Create a new cell since it will be updated in place.
      currentCell = new Cell (startCell);
    } catch (StringIndexOutOfBoundsException sbe) {
      startCell = badCell;
      endCell = badCell;
      currentCell = badCell;
    } catch (Exception e) {
      startCell = badCell;
      endCell = badCell;
      currentCell = badCell;
    }
  }

  public Cell getStart () { return startCell; }
  public void setStart (Cell sc) { startCell = sc; }

  public Cell getEnd () { return endCell; }
  public void setEnd (Cell ec) { endCell = ec; }

  public boolean isSame (CellRegion cr) {
    if (cr == null) return false;
    if (cr.getStart().isSame (startCell))
      if (cr.getEnd().isSame (endCell))
	return true;

    return false;
  }

  public boolean isValid () {
    return ((startCell != badCell) && (endCell != badCell));
  }

  // Sequence of: Letters Numbers ':' Letters Numbers
  public static boolean isCellRegion (String s) {
    int   i = 0;
    char  ch;

    try {
      ch = s.charAt(i);
      
      // Letters
      if (!Character.isLetter(ch)) return false;
      while (Character.isLetter(ch)) {
	ch = s.charAt(++i);
      }
      
      // Digits
      if (!Character.isDigit(ch)) return false;
      while (Character.isDigit(ch)) {
	ch = s.charAt(++i);
      }
      
      // Colon
      if (ch != ':') return false;
      ch = s.charAt(++i);
      
      // Letters
      if (!Character.isLetter(ch)) return false;
      while (Character.isLetter(ch)) {
	ch = s.charAt(++i);
      }
      
      // Digits
      if (!Character.isDigit(ch)) return false;
      while (Character.isDigit(ch)) {
	i++;
	if (i == s.length()) {
	  return true;     // TRUE! All done
	}

	ch = s.charAt(i);
      }
      
      return false;
    } catch (Exception e) {

      return false;
    }
  }

  public String toString () {
    if (startCell == null) return "0";
    String s = startCell.toString() + ":" + endCell.toString();
    if (currentCell == null) return (s + " [done]");
    return s + " [next:" + currentCell.toString() + "]";
  }

  public String toString (boolean isShort) {
    if (!isShort) return toString ();
    if (startCell == null) return "0";
    if (startCell.isSame (endCell))
      return startCell.toString();
    return startCell.toString() + ":" + endCell.toString();
  }

}

