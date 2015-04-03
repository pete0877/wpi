// Class  : AverageFunction
// Author : George Heineman
//        : heineman@cs.wpi.edu
//        : http://www.cs.wpi.edu/~heineman
// Version: 1.0

package spreadsheet;

/**
 * This class represents the Average Function in the spreadsheet
 */

import java.util.Enumeration;

public class AverageFunction extends Function {

  private float sum = 0;
  private int   count = 0;

  AverageFunction (Spreadsheet ss) {
    super (ss);
  }

  /**
   * Every subclass must define this function
   *
   * @exception FunctionException if an argument is an unknown function
   */
  public float calculate () throws FunctionException { 
    sum = 0;
    count = 0;

    try {
      while (hasMoreArguments()) {
	float arg = nextArgument();

	// addition
	sum = sum + arg;
	count = count + 1;
      }
      return sum / count;

    } catch (FunctionException fe) {
      throw fe;
    } catch (ArithmeticException ae) {
      throw new FunctionException ("Unknown ArithmeticException");      
    }
  }
}
