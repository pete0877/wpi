// Class  : MultiplyFunction
// Author : George Heineman
//        : heineman@cs.wpi.edu
//        : http://www.cs.wpi.edu/~heineman
// Version: 1.0


/**
 * This class represents the Multiply Function in the spreadsheet
 */

package spreadsheet;

import java.util.Enumeration;

public class MultiplyFunction extends Function {
  private float product;

  MultiplyFunction (Spreadsheet ss) {
    super (ss);
  }

  /**
   * Every subclass must define this function
   *
   * @exception FunctionException if an argument is an unknown function
   */
  public float calculate () throws FunctionException { 
    product = 1;

    try {
      while (hasMoreArguments()) {
	float arg = nextArgument();

	// multiplication
	product = product * arg;
      }
      return product;
    } catch (FunctionException fe) {
      throw fe;
    } catch (ArithmeticException ae) {
      throw new FunctionException ("Unknown ArithmeticException");
    }
 }
}

