// Class  : TextBeanEventObject
// Author : George Heineman
//        : heineman@cs.wpi.edu
//        : http://www.cs.wpi.edu/~heineman
// Version: 1.0

package gui;

import java.util.EventObject;

public class TextBeanEventObject extends EventObject {
  private int     terminatingCharacter;
  private String  value;
  private String  previousValue;

  public static final int TAB = 1;
  public static final int SHIFT_TAB = 2;
  public static final int RETURN = 3;
  public static final int SHIFT_RETURN = 4;

  public TextBeanEventObject (Object o, String oldValue, String newValue, int termChar) {
    super (o);
    value = newValue;
    previousValue = oldValue;
    terminatingCharacter = termChar;
  }

  public String getValue () { return value; }
  public void   setValue (String value) { this.value = value; }

  public int  getTerminatingCharacter () { return terminatingCharacter; }
  public void setTerminatingCharacter (int termChar) { terminatingCharacter = termChar; }

  public String getPreviousValue () { return previousValue; }
  public void   setPreviousValue (String value) { this.previousValue = value; }

  public String toString () {
    return new String (value);
  }

}
