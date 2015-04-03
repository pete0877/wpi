// Class  : HoursOfDayTB.java
// Author : George Heineman
//        : heineman@cs.wpi.edu
//        : http://www.cs.wpi.edu/~heineman
// Version: 1.0

package gui;

public class HoursOfDayTB extends TableBean {
 
  /**
   * Required to be a valid Bean
   */
  public HoursOfDayTB() {
    super (1,0);
  }

  public HoursOfDayTB(int rows) {
    super (1, rows);
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
      StringBuffer sb = new StringBuffer();
      int hour = 8 + viewRow / 2;
      if (hour > 12) hour = hour - 12;

      boolean halfHour = ((viewRow % 2) == 0);

      if (halfHour) 
	  sb.append ("  :30");
      else {
	  if (hour < 10) sb.append (" ");
	  sb.append (hour);
	  sb.append (":00");
      }
      return sb.toString();
  }

}

