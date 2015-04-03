// Class  : TableElement
// Author : George Heineman
//        : heineman@cs.wpi.edu
//        : http://www.cs.wpi.edu/~heineman
// Version: 1.0

package gui;

/**
 * This is a simple class to represent a table entry in the Table
 */

import java.util.StringTokenizer;
import java.io.Serializable;

public class TableElement implements Serializable {

  private static String ILLEGAL_COLUMN = "#NA#";
  private static String ILLEGAL_ROW    = "#NA#";

  private final int BAD_ROW = -1;
  private final int BAD_COLUMN = -1;

  private int row;
  private int col;

  /**
   * Constructs a Cell
   */
  public TableElement (int c, int r) {
    col = c;
    row = r;
  }
  
  public TableElement (TableElement te) {
    col = te.getColumn();
    row = te.getRow();
  }

  /**
   * Constructs a TableElement from a String
   */
  public TableElement (String info) {
    col = BAD_COLUMN;
    row = BAD_ROW;

    StringTokenizer st = new StringTokenizer (info, ":", false);
    if (st.hasMoreTokens()) {
      col = Integer.valueOf (st.nextToken()).intValue();
      if (st.hasMoreTokens()) {
	row = Integer.valueOf (st.nextToken()).intValue();
      }
    }
  }

  public boolean isValid () {
    return ((col != BAD_COLUMN) && (row != BAD_ROW));
  }

  public int getColumn () { return col; }
  public int getRow () { return row; }

  public void setColumn (int c) { col = c; }
  public void setRow (int r) { row = r; }

  public boolean isSame (TableElement te) {
    if (te == null) return false;
    return (te.getRow() == row) && (te.getColumn() == col);
  }

  public boolean equals (TableElement te) {
    return isSame (te);
  }

  public String toString () {
    return col + ":" + row;
  }

  public String getRowLabel() {
    return new String (new Integer (row).toString());
  }

  public String getColumnLabel() {
    return new String (new Integer (col).toString());
  }


}
