// Class  : Node
// Author : George Heineman
//        : heineman@cs.wpi.edu
//        : http://www.cs.wpi.edu/~heineman
// Version: 1.0

package adapt.spreadsheet;

/**
 * This is a simple class to represent a Node for computation in spreadsheet
 *
 * Who knows whether a node has a #DIV/0! error, or something else?
 * When is it reset?
 */

import java.util.Vector;
import java.util.Enumeration;

//
// The level for a node is:
//    0: if a constant number value
//    1: if the formula only referring to constant values
//    2: if the formula refers to nodes of level 0 and 1
//    etc...

public class Node {

  private Cell        cell;
  private String      value;
  private int         level;
  private Vector      refs;            // Those cells that Reference this node
  private boolean     recalculate;
  private Expression  expr;
  private float       numericValue;    // Calculated value

  /**
   * Constructs a Node
   */
  public Node(Cell cell, String value, int level) {
    this.cell = cell;
    this.value = value;
    this.level = level;
    this.refs = new Vector();
    this.recalculate = false;
  }
  
  public Node(Cell cell, String value) {
    this (cell, value, 0);
  }

  public Cell getCell () { return cell; }
  public void setCell (Cell cell) { this.cell = cell; }

  public String getValue () { return value; } 
  public void setValue (String value) { this.value = value; }

  public float getNumericValue () { return numericValue; } 
  public void setNumericValue (float value) { numericValue = value; }

  public Expression getExpression () { return expr; } 
  public void setExpression (Expression expr) { this.expr = expr; }

  public int getLevel () { return level; } 
  public void setLevel (int level) { this.level = level; }

  public boolean getRecalculation () { return recalculate; } 
  public void setRecalculation (boolean b) { recalculate = b; }

  public void addReference (Cell cell) {
    if (refs.contains (cell))
      return;
    refs.addElement (cell); 
  }
  public void removeReference (Cell cell) {
    refs.removeElement (cell);
  }
  public Enumeration getReferences () {
    return refs.elements();
  }

  public boolean isSame (Node n) {
    if (n == null) return false;
    return cell.isSame (n.getCell());
  }

  public boolean sameLevel (Node n) {
    if (n == null) return false;
    return n.getLevel() == level;
  }

  public boolean higherLevel (Node n) {
    if (n == null) return false;
    return level > n.getLevel();
  }

  public String toString () {
    return cell.toString();
  }

}
