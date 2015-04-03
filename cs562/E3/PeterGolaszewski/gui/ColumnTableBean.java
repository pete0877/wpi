// Class  : ColumnTableBean
// Author : George Heineman
//        : heineman@cs.wpi.edu
//        : http://www.cs.wpi.edu/~heineman
// Version: 1.0

package gui;

public class ColumnTableBean extends TableBean {
  
  private TableElement element = null;
  private int          leftColumn = 1;

  /**
   * Required to be a valid Bean
   */
  public ColumnTableBean() {
    super (0,1);
  }

  public ColumnTableBean(int cols) {
    super (cols, 1);
  }
  
  /** 
   * Set the leftColumn of table.
   * 
   * @see #getLeftColumn
   */      
  public void setLeftColumn(int newCol) {
    leftColumn = newCol;
  }

  /** 
   * Return the leftColumn in table
   * 
   * @see #setLeftColumn
   */
  public int getLeftColumn() {
    return leftColumn;
  }


  /**
   * No operation
   */
  public void setTableValue(int col, int row, String val) { }

  /**
   * Retrieves the value for a particular table entry
   *
   */
  public String getTableValue(int viewCol, int viewRow) {
    int actualCol = viewCol + leftColumn - 1;
    if (element == null)
      element = new TableElement (actualCol, viewRow);
    else
      element.setColumn (actualCol);

    return getColumnLabel (actualCol);
  }

  /**
   * Column is A-Z, AA-ZZ, AAA-ZZZ, etc...
   * starts counting from 1, thus subtract 1
   */
  private String getColumnLabel(int col) {
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
      // As a last resort, just return a string of the column
      return new String (new Integer (col).toString());
    }
  }


}

