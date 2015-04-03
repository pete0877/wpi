// Class  : FunctionException
// Author : George Heineman
//        : heineman@cs.wpi.edu
//        : http://www.cs.wpi.edu/~heineman
// Version: 1.0

package adapt.spreadsheet;

/**
 * This class captures any exceptions that may occur when evaluating 
 * functions
 */

public class FunctionException extends Exception {

  public FunctionException() {
    super();
  }

  public FunctionException (String s) {
    super (s);
  }
}
