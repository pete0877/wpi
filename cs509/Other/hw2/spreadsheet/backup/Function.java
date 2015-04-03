// Class  : Function
// Author : George Heineman
//        : heineman@cs.wpi.edu
//        : http://www.cs.wpi.edu/~heineman
// Version: 1.0

/**
 * This class represents a Function in the spreadsheet
 */

package adapt.spreadsheet;

import java.util.Enumeration;
import java.io.Serializable;

abstract public class Function implements Serializable, Cloneable {

  private Spreadsheet ss;
  private Enumeration argList;
  private CellRegion  cellRegion;     // enumerated set of cells
  private Cell        lastCell;
  
  /**
   * Needs the spreadsheet in case expression contains references to 
   * other spreadsheet cells
   *
   * Must declare as 'public' for external subclasses to package to
   * be allowed to use.
   */
  public Function (Spreadsheet ss) {
    this.ss = ss;
  }

  /**
   * calls the spreadsheet to calculate expression
   *
   * @exception FunctionException if an argument is an unknown function
   */
  float calculateFunction (Expression exp) throws FunctionException {
    return ss.calculateFunction (exp);
  }

  /**
   * calls the spreadsheet to retrieve value
   */
  float getValue (Cell cell) {
    return ss.getNumericValue (cell.toString());
  }

  public boolean hasMoreArguments() {
    if (cellRegion == null)
      return argList.hasMoreElements();

    // If more in list, then we certainly have more to go
    if (argList.hasMoreElements())
      return true;

    // If the last entry in the list was a cellRegion, we must check to
    // see the status of that item
    return cellRegion.hasMoreElements();
  }

  private float processCellRegion () {
    if (cellRegion.hasMoreElements()) {
      Cell cell = (Cell) cellRegion.nextElement();
      return getValue(cell);
    } else {
      // Should never have been added in the first place.
      cellRegion = null;
      try {
	return nextArgument();
      } catch (FunctionException fe) {
	// What to do?
	return 0;  
      }
    }
  }

  private String processCellRegionAsString () {
    if (cellRegion.hasMoreElements()) {
      Cell cell = (Cell) cellRegion.nextElement();
      return cell.toString();
    } else {
      // Should never have been added in the first place.
      cellRegion = null;
      try {
	return nextStringArgument();
      } catch (FunctionException fe) {
	// What to do?
	return new String ("");  
      }
    }
  }

  /**
   * Returns the next argument for this function
   *
   * @exception FunctionException if an argument is an unknown function
   */
  public float nextArgument() throws FunctionException {
    float f;

    if (cellRegion == null) {
      Object obj = argList.nextElement();

      if (obj instanceof Expression) {
	try {
	  f = calculateFunction ((Expression) obj);
	  return f;
	} catch (FunctionException fe) {
	  throw fe;
	}
      }
      
      // Process Cell (i.e. B7) as a single item
      String value = (String) obj;
      if (value.indexOf (':') == -1) {
	Cell cell = new Cell (value);
	if (cell.isValid()) {
	  f = getValue(cell);
	  return f;
	}
      }

      // Process CellRegions (i.e., A9:D23) one cell at a time
      if (value.indexOf (':') != -1) {
	CellRegion cr = new CellRegion (value);
	if (cr.isValid()) {
	  cellRegion = cr;
	  f = processCellRegion();
	  return f;
	}
      }

      // Process numbers (and strings) as single items
      // Must be a number (or a string) so evaluate as is
      try {
	Float fl = new Float ((String) obj);
	f = fl.floatValue();
      } catch (NumberFormatException nfe) {
	f = 0;
      }
      return f;
    } else {
      return processCellRegion();
    }
  }

  /**
   * Returns the next argument for this function as a String
   *
   * @exception FunctionException if an argument is an unknown function
   */
  public String nextStringArgument() throws FunctionException {
    if (cellRegion == null) {
      Object obj = argList.nextElement();

      if (obj instanceof Expression) {
	try {
	  float f = calculateFunction ((Expression) obj);
	  return Float.toString (f);
	} catch (FunctionException fe) {
	  throw fe;
	}
      }
      
      String value = (String) obj;
      // Process Cell (i.e. B7) as a single item
      if (value.indexOf (':') == -1) {
	return value;
      }

      // Process CellRegions (i.e., A9:D23) one cell at a time
      if (value.indexOf (':') != -1) {
	CellRegion cr = new CellRegion (value);
	if (cr.isValid()) {
	  cellRegion = cr;
	  return processCellRegionAsString();
	}
      }

      return (String) obj;
    } else
      return processCellRegionAsString ();
  }


  /**
   * Every subclass must define calculate (Enumeration en)
   *
   * @exception FunctionException if an argument is an unknown function
   */
  protected abstract float calculate () throws FunctionException;


  /**
   *
   */
  private float calculateFunctionInstance (Enumeration args) throws FunctionException {
    argList = args;
    try {
      return calculate ();
    } catch (FunctionException fe) {
      throw fe;
    }    
  }

  /**
   * Calculates the given function given an enumeration of arguments
   *
   * because we can have nested functions, like (+ (+ 1 2) (+ 3 4)) we need 
   * to evaluate each function in its own object.
   *
   * @exception FunctionException if an argument is an unknown function
   */
  public float calculateFunction (Enumeration args) throws FunctionException {
    try  {
      Function f = (Function) this.clone();
      return f.calculateFunctionInstance (args);
    
    } catch (java.lang.CloneNotSupportedException cnse) {
      throw new FunctionException ("unable to clone function");
    }
  }
}
