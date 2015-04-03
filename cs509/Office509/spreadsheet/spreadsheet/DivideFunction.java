// Class  : DivideFunction
// Author : George Heineman
//        : heineman@cs.wpi.edu
//        : http://www.cs.wpi.edu/~heineman
// Version: 1.0

/**
 * This class represents the Divide Function in the spreadsheet
 */

package spreadsheet;

import java.util.Enumeration;

public class DivideFunction extends Function {

  private float value = 1;

  String    divZero = new String ("#DIV/0!");

  DivideFunction (Spreadsheet ss) {
    super (ss);
  }

  /**
   * Every subclass must define this function
   *
   * @exception FunctionException if an argument is an unknown function
   */
  public float calculate () throws FunctionException { 
    boolean first = true;
    value = 0;

    try {
      while (hasMoreArguments()) {
	float arg = nextArgument();

	if (first) {
	  first = false;
	  value = arg;
	} else {
	  if (arg == 0)
	    throw new FunctionException (divZero);
	  value = value / arg;
	}
      }
      return value;
    } catch (FunctionException fe) {
      throw fe;
    } catch (ArithmeticException ae) {
      throw new FunctionException ("Unknown ArithmeticException");
    }
  }
}
