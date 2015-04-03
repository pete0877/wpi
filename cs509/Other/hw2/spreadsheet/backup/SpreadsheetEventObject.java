// Class  : SpreadsheetEventObject
// Author : George Heineman
//        : heineman@cs.wpi.edu
//        : http://www.cs.wpi.edu/~heineman
// Version: 1.0

package adapt.spreadsheet;

import java.util.EventObject;

public class SpreadsheetEventObject extends EventObject {
  // private information for this event
  private int         type;
  private CellRegion  cellRegion;
  private String      value;

  public final static int INSERT = 100;
  public final static int REFRESH = 101;
  public final static int NEWFUNCTION = 102;
  public final static int RECALCULATE = 103;

  public SpreadsheetEventObject (Object o, int type, CellRegion cellRegion, String value) {
    super (o);
    this.type = type;
    this.cellRegion = cellRegion;
    this.value = value;
  }

  public SpreadsheetEventObject (Object o, int type, Cell cell, String value) {
    super (o);
    this.type = type;
    this.cellRegion = new CellRegion (cell, cell);
    this.value = value;
  }

  // Access methods go here
  public int getType() {
    return type;
  }

  // Access methods go here
  public CellRegion getCellRegion() {
    return cellRegion;
  }

  public String getValue() {
    return value;
  }

  public String toString() {
    return "Event: " + cellRegion.toString() + ":" + type;
  }

}
