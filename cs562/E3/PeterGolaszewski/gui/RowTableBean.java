// Class  : RowTableBean
// Author : George Heineman
//        : heineman@cs.wpi.edu
//        : http://www.cs.wpi.edu/~heineman
// Version: 1.0

package gui;

public class RowTableBean extends TableBean {
  
  private TableElement element = null;
  private int          topRow = 1;

  /**
   * Required to be a valid Bean
   */
  public RowTableBean() {
    super (1,0);
  }

  public RowTableBean(int rows) {
    super (1,rows);
  }

  /** 
   * Set the topRow of table.  This is a bound property.
   * 
   * @see #getTopRow
   */      
  public void setTopRow(int newRow) {
    topRow = newRow;
  }

  /** 
   * Return the topRow in table
   * 
   * @see #setTopRow
   */
  public int getTopRow() {
    return topRow;
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
    int actualRow = viewRow + topRow - 1;
    if (element == null)
      element= new TableElement (viewCol, actualRow);
    else
      element.setRow (actualRow);

    return new String (new Integer (actualRow).toString());
  }
}

