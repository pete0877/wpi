// Class  : PiFunction
// Author : George Heineman
//        : heineman@cs.wpi.edu
//        : http://www.cs.wpi.edu/~heineman
// Version: 1.0

package adapt.spreadsheet;

/**
 * This class represents the Pi Function in the spreadsheet
 */

import java.util.Enumeration;

public class PiFunction extends Function {

  private float value = (float) 3.141592635;

  PiFunction (Spreadsheet ss) {
    super (ss);
  }

  /**
   * Every subclass must define this function
   *
   * @exception FunctionException if an argument is an unknown function
   */
  public float calculate () throws FunctionException { 
    return value;
  }
}
