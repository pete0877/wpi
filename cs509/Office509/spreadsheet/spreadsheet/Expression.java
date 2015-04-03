// Class  : Expression
// Author : George Heineman
//        : heineman@cs.wpi.edu
//        : http://www.cs.wpi.edu/~heineman
// Version: 1.0

package spreadsheet;

// No error handling exists for this class.  Do not give an expression with
// un-balanced parentheses!

import java.util.Vector;
import java.util.Enumeration;
import java.util.StringTokenizer;

public class Expression {
  // The real information for this expression
  String operator;
  Vector operands;

  private String rawValue;
  private boolean valid;

  public String toString () {
    if (operands == null)
      return "[Expression: " + rawValue + "," + "operator:" + operator +
	"operands:null";

    return "[Expression: " + rawValue + "," + "operator:" + operator +
      "operands:" + operands.toString() + "]";
  }

  public final String ERROR = new String ("#ERROR#");
  public final String CONSTANT = new String ("CONST");

  public final String LPAREN = new String ("(");
  public final String RPAREN = new String (")");
  public final String SPACE  = new String (" ");

  /**
   * This is used for creating placeholder for invalid expression
   */
  private Expression (boolean b) {
    valid = b;
  }

  // This just stores the expression for later
  public Expression (String value) {
    rawValue = value;
    parseExpression (value);
  }

  /**
   * Simple check if this is an expression
   */
  public static boolean isExpression (String s) {
    if (s == null) return false;
    try {
      if (s.charAt (0) != '(')
	return false;
      if (s.charAt (s.length() - 1) != ')')
	return false;
      return true;
    } catch (StringIndexOutOfBoundsException aioobe) {
      return false;
    }
  }

  public boolean isConstant () { 
    if (operator == null) return true;
    return operator.equals (CONSTANT);
  }

  /**
   * This strips off all SPACE tokens
   */
  private String nextToken (StringTokenizer st) {
    while (true) {
      if (st.hasMoreTokens()) {
	String tok = st.nextToken();
	if (!tok.equals (SPACE))
	  return tok;
      } else
	return null;
    }
  }

  /**
   * Do rudimentary error checking
   */
  private Expression (StringTokenizer st) {
    operator = nextToken (st);
    if (operator == null)
      valid = false;
    else
      // This parses up until and including the Right Paren
      operands = parseVector (st);

    if (operands == null)
      valid = false;
  }

  private Vector parseVector (StringTokenizer st) {
    Vector v = new Vector();
    int    idx = 0;

    while (st.hasMoreTokens()) {
      String token = nextToken (st);
      if (token == null) return null;

      // If reached end of the vector, return what we have so far
      if (token.equals (RPAREN)) {
	break;
      } else if (token.equals (LPAREN)) {
	// This is a sub-expression, so parse it and add into vector
	Expression subE = new Expression (st);
	v.addElement (subE);
      } else {
	// Simply add into vector of operands
	v.addElement (token);
      }
    }
    return v;
  }

  private boolean parseExpression (String s) {
    valid = false;
    StringTokenizer st = new StringTokenizer (s, "() ", true);
    if (!st.hasMoreTokens()) return false;

    String token = nextToken(st);
    if (token == null) return false;

    if (token.equals (LPAREN)) {
      token = nextToken (st);
      if (token == null)
	return false;

      // Cannot have nesting at first level
      if (token.equals (LPAREN)) return false;

      operator = token;
      operands = parseVector (st);
      valid = true;
      return true;
    } else {
      // This must be the whole value then
      operator = new String (CONSTANT);
      operands = new Vector();
      operands.addElement (s);
      return true;
    }
  }

  public String getOperator () {
    return operator;
  }

  public Enumeration elements () {
    return operands.elements();
  }

  public boolean isValid () {
    return valid;
  }

}
