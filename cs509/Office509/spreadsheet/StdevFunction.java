// Class  : StdevFunction
// Author : George Heineman
//        : heineman@cs.wpi.edu
//        : http://www.cs.wpi.edu/~heineman
// Version: 1.0

/**
 * This class represents the Standard Deviation Function in the spreadsheet
 *
 */

import java.util.Enumeration;
import java.util.Vector;
import java.lang.Float;
import spreadsheet.*;

public class StdevFunction extends Function {

  private Vector values;
  private int    count;
  private float  sum;

  StdevFunction (Spreadsheet ss) {
    super (ss);
  }

  /**
   * Every subclass must define this function
   */
  public float calculate () throws FunctionException { 
    values = new Vector ();
    sum = 0;
    count = 0;
    try {
      while (hasMoreArguments()) {
	float arg = nextArgument();

	// put values into vector, from which we determine standard deviation
	values.addElement (new Float(arg));
	sum += arg;
	count++;
      }

      if (count == 0) {
	throw new FunctionException ("Zero elements");
      }
      if (count == 1) {
	throw new FunctionException ("Only one element");
      }

      // At this point, the values vector contains all elements in the 
      // list.  Calculate Stdev from the vector
      float avg = sum/count;
      sum = 0;
      for (Enumeration ven = values.elements(); ven.hasMoreElements();) {
	Float fl = (Float) ven.nextElement();
	float f = fl.floatValue();
	sum = sum + (f - avg)*(f-avg);
      }

      sum = sum / (count - 1);
      return (float) Math.sqrt (sum);

    } catch (FunctionException fe) {
      throw fe;
    } catch (ArithmeticException ae) {
      throw new FunctionException ("Unknown ArithmeticException");      
    }
  }
}
