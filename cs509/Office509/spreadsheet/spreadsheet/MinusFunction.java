// Class  : MinusFunction
// Author : George Heineman
//        : heineman@cs.wpi.edu
//        : http://www.cs.wpi.edu/~heineman
// Version: 1.0

package spreadsheet;

/**
 * This class represents the Minus Function in the spreadsheet
 */

import java.util.Enumeration;

public class MinusFunction extends Function {

  private float value = 0;

  MinusFunction (Spreadsheet ss) {
    super (ss);
  }

  /**
   * Every subclass must define this function
   *
   * @exception FunctionException if an argument is an unknown function
   */
  public float calculate () throws FunctionException { 
    value = 0;
    boolean first = true;
    boolean unary = true;

    try {
      while (hasMoreArguments()) {
	float arg = nextArgument();

	if (first) {
	  value = arg;
	  first = false;
	} else {
	  // Subtraction
	  value = value - arg;
	  unary = false;
	}
      }

      // take care of unary operations
      if (unary)
	return 0 - value;
      else
	return value;
    } catch (FunctionException fe) {
      throw fe;
    } catch (ArithmeticException ae) {
      throw new FunctionException ("Unknown ArithmeticException");      
    }
  }
}
