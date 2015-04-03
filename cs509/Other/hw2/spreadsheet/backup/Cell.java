// Class  : Cell
// Author : George Heineman
//        : heineman@cs.wpi.edu
//        : http://www.cs.wpi.edu/~heineman
// Version: 1.0

package adapt.spreadsheet;

/**
 * This is a simple class to represent a Cell in the SpreadSheet
 */
import java.io.Serializable;

public class Cell implements Serializable {

  private static String ILLEGAL_COLUMN = "#NA#";
  private static String ILLEGAL_ROW    = "#NA#";

  private final int BAD_ROW = -1;
  private final int BAD_COLUMN = -1;

  private int row;
  private int col;

  /**
   * Constructs a Cell
   */
  public Cell(int c, int r) {
    col = c;
    row = r;
  }
  
  public Cell(Cell cell) {
    col = cell.getColumn();
    row = cell.getRow();
  }

  /**
   * Constructs a Cell from a String
   */
  public Cell(String info) {
    boolean lettersOnly = true;

    int c = 0, r = 0;
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
	}
	else 
	  break;  // Reached the ":" divider, 
      }
      if ((c == 0) || (r == 0))
	throw new Exception ("Bad Format");

      col = c;
      row = r;

    } catch (StringIndexOutOfBoundsException sbe) {
      col = BAD_COLUMN;
      row = BAD_ROW;
    } catch (Exception e) {
      col = BAD_COLUMN;
      row = BAD_ROW;
    }
  }

  public boolean isValid () {
    return ((col != BAD_COLUMN) && (row != BAD_ROW));
  }

  public static boolean isCell (String s) {
    int  i = 0;
    char ch;
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
	i++;
	if (i == s.length()) {
	  return true;    // TRUE! All done
	}
	ch = s.charAt(i);
      }
      
      return false;
    } catch (Exception e) {
      return false;
    }
  }

  public int getColumn () { return col; }
  public int getRow () { return row; }

  public void setColumn (int c) { col = c; }
  public void setRow (int r) { row = r; }

  public String getRowLabel() {
    return Integer.toString(row);
  }

  /**
   * Column is A-Z, AA-ZZ, AAA-ZZZ, etc...
   * starts counting from 1, thus subtract 1
   */
  public String getColumnLabel() {
    // This creates strings with digits '0123456789abcde...'
    String s = Integer.toString (col - 1, 26);
    StringBuffer sb = new StringBuffer();

    // Note: Because we start counting from 0, the FIRST character must
    //       be offset by -1 ONLY when it is part of a two-character
    //       combination (i.e., AA, ZZZ)

    // Convert to start from 'a' instead.  A bit tricky since 'a' does
    // not follow '9' in the ASCII table
    try {
      boolean offset = (s.length() > 1);
      for (int i = 0; i < s.length(); i++) {
	char ch = s.charAt(i);

	if ((i == 0) && offset)
	  ch = (char) (ch - 1);

	if (Character.isDigit (ch))
	  sb.append ((char) ('A' + ch - '0'));
	else
	  sb.append ((char) (ch + 10));
      }
      s = sb.toString();
      return s.toUpperCase();
    } catch (StringIndexOutOfBoundsException sbe) {
      
    }
    return new String (ILLEGAL_COLUMN);
  }

  public boolean isSame (Cell c) {
    if (c == null) return false;
    return (c.getRow() == row) && (c.getColumn() == col);
  }

  public String toString () {
    return getColumnLabel() + getRowLabel();
  }

}
