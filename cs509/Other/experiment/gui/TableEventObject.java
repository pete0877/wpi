// Class  : TableEventObject
// Author : George Heineman
//        : heineman@cs.wpi.edu
//        : http://www.cs.wpi.edu/~heineman
// Version: 1.0

package gui;

import java.util.EventObject;

public class TableEventObject extends EventObject {
  TableElement start;
  TableElement end;
  int  type;

  public final static int MOUSE_PRESS     = 1;
  public final static int MOUSE_DOWN      = 2;
  public final static int MOUSE_RELEASE   = 3;

  // Using the select() API
  public final static int EXTERNAL_SELECT = 4;

  public TableEventObject (Object o, int c, int r, int type) {
    super (o);
    start = new TableElement (c, r);
    end = new TableElement (c, r);
    this.type = type;
  }

  public TableEventObject (Object o, TableElement te, int type) {
    super (o);
    start = new TableElement (te);
    end = new TableElement (te);
    this.type = type;
  }

  public TableEventObject (Object o, TableElement from, TableElement to, int type) {
    super (o);
    start = from;
    end = to;
    this.type = type;
  }

  public TableEventObject (Object o, int c, int r, int c2, int r2, int type) {
    super (o);
    start = new TableElement (c, r);
    end = new TableElement (c2, r2);
    this.type = type;
  }

  public boolean isSingleElement () {
    return (start.isSame (end));
  }

  public TableElement getFrom () { return start; }
  public void setFrom (TableElement te) { start = te; }

  public TableElement getTo () { return end; }
  public void setTo (TableElement te) { end = te; }

  public int getType () { return type; }
  public void setType (int type) { this.type = type; }

  public String toString () {
    StringBuffer sb = new StringBuffer();

    sb.append (start.toString());
    if (isSingleElement())
      return (sb.toString());
    sb.append (":");
    sb.append (end.toString());
    return (sb.toString());
  }
}
