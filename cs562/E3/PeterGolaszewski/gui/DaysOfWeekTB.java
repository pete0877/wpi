// Class  : DaysOfWeekTB.java
// Author : George Heineman
//        : heineman@cs.wpi.edu
//        : http://www.cs.wpi.edu/~heineman
// Version: 1.0

package gui;

public class DaysOfWeekTB extends TableBean {
 
  /**
   * Required to be a valid Bean
   */
  public DaysOfWeekTB() {
    super (0,1);
  }

  public DaysOfWeekTB(int cols) {
    super (cols, 1);
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
      if (viewCol == 1)
	  return "Mon";
      else if (viewCol == 2)
	  return "Tue";
      else if (viewCol == 3)
	  return "Wed";
      else if (viewCol == 4)
	  return "Thu";
      else if (viewCol == 5)
	  return "Fri";
      else
	  return "***";
  }

}

