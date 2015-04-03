// Class  : Spreadsheet
// Author : George Heineman
//        : heineman@cs.wpi.edu
//        : http://www.cs.wpi.edu/~heineman
// Version: 1.0

package adapt.spreadsheet;

// This is a simple Bean for calculating a Spreadsheet.

import java.beans.*;
import java.io.Serializable;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;

import adapt.ComponentAdapter;
import adapt.Adaptable;

public class Spreadsheet extends Object implements Adaptable {

  private Vector    refreshVector = new Vector();

  private Vector spreadsheetListeners = new Vector();

  // The results of the computations are stored in a separate hashtable
  private Hashtable nodeHashtable = new Hashtable();

  // List of installed functions
  private Hashtable functionHashtable = new Hashtable();

  // Constructs a Spreadsheet
  public Spreadsheet() {
    super();

    installFunctions();
  }
  
  // Installs a function to the spreadsheet
  //
  // @see #getFunction
  // @see #clearFunction
  // --------------------------------------------------
  public void setFunction (String name, Function f) {
    functionHashtable.put (name, f);
  }

  // Retrieves a particular function from the spreadsheet
  //
  // @see #setFunction
  // @see #clearFunction
  // --------------------------------------------------
  public Function getFunction (String name) {
    return (Function) functionHashtable.get (name);
  }

  // Clears all existing functions
  //
  // @see #getFunction
  // @see #setFunction
  // --------------------------------------------------
  public void clearFunction () {
    functionHashtable = new Hashtable ();
  }

  // Install built-in functions
  //
  // @see #getFunction
  // @see #setFunction
  // --------------------------------------------------
  public void installFunctions() {
    setFunction ("PLUS", new AddFunction(this));
    setFunction ("+", new AddFunction(this));
    setFunction ("SUM", new AddFunction(this));
    
    setFunction ("MULTIPLY", new MultiplyFunction(this));
    setFunction ("*", new MultiplyFunction(this));

    setFunction ("DIVIDE", new DivideFunction(this));
    setFunction ("/", new DivideFunction(this));

    setFunction ("MINUS", new MinusFunction(this));
    setFunction ("SUB", new MinusFunction(this));
    setFunction ("SUBTRACT", new MinusFunction(this));
    setFunction ("-", new MinusFunction(this));

    setFunction ("PI", new PiFunction(this));

    setFunction ("AVERAGE", new AverageFunction(this));
    setFunction ("AVG", new AverageFunction(this));
  }

  // Returns all Cell references in the given cell formula.  Can't return
  // nodes, since the formula may refer to cells before they become nodes
  // with values.  The only significant characters are '(' and ')' and ' '
  // so we read tokens from the string without these delimeters.
  // --------------------------------------------------
  private Vector extractCells (String value) {
    Vector v = new Vector();
    boolean added = false;
    StringTokenizer st = new StringTokenizer (value, new String ("() "), false);
    while (st.hasMoreTokens()) {
      String s = st.nextToken();

      // Check to see if CellRegion
      if (CellRegion.isCellRegion (s)) {
	// This is *WASTEFUL*.  Try to find a different strategy
	CellRegion cr = new CellRegion (s);

	while (cr.hasMoreElements()) {
	  Cell cell = (Cell) cr.nextElement();
	  v.addElement (new Cell (cell));
	  added = true;
	}
      } else if (Cell.isCell(s)) {
	Cell cell = new Cell (s);
	if (cell.isValid()) {
	  v.addElement (cell);
	  added = true;
	}
      }
    }
    
    // If none were added, return null
    if (added)
      return v;
    else
      return null;
  }

  // This node will soon be deleted, so we need to update the references
  // based upon its current value
  // --------------------------------------------------
  private void modifyLevels (Node node) {
    Vector v = extractCells (node.getValue());

    if (v == null) return;

    for (Enumeration ven = v.elements(); ven.hasMoreElements();) {
      Cell cell = (Cell) ven.nextElement();

      // Find node in hashtable
      Node n = (Node) nodeHashtable.get (cell.toString());

      // Remove cell from inverted-reference.
      if (n != null) {
	n.removeReference (cell);
      }
    }
  }

  // locate the appropriate level.  Run through the hashtable and select
  // the highest level within which the cells are referenced.
  // Also maintains inverse references for each node
  // --------------------------------------------------
  private void updateLevels (Node node) {
    Vector v = extractCells (node.getValue());
    
    // Simple case: constant value
    if (v == null) {
      node.setLevel (0);
      return;
    }

    // For all cells, determine the level (if any) of these cells
    int  nodeLevel = 0;
    Cell homeCell = node.getCell();
    for (Enumeration ven = v.elements(); ven.hasMoreElements();) {
      Cell cell = (Cell) ven.nextElement();

      // If node is already in the hashtable, we increase our level
      Node n = (Node) nodeHashtable.get (cell.toString());
      if (n != null) {
	if (n.getLevel() > nodeLevel)
	  nodeLevel = n.getLevel();
      } else {
	// Need to add node into the hashtable
	n = new Node (cell, new String("0"));
	nodeHashtable.put (cell.toString(), n);
      }
      
      // Only add this cell to the inverted-reference if not already there.
      n.addReference (homeCell);
    }

    // At this point, nodeLevel is the highest level of any node yet seen,
    // so we set the level to be one greater.  This is appropriate even if
    // none of the cells in the formula currently have node values.
    node.setLevel (nodeLevel+1);
  }

  // Returns the value of a node as labeled by the string
  //
  // @see setValue
  // @see clearValue
  // --------------------------------------------------
  public String getValue (String s) {
    Node n = getNode (s);
    if (n == null)
      return new String ("");

    return n.getValue();
  }

  // Returns the expression of a node as labeled by the string
  //
  // @see setValue
  // @see clearValue
  // --------------------------------------------------
  public Expression getExpression (Cell cell) {
    if (cell == null) return null;
    Node n = (Node) nodeHashtable.get (cell.toString());
    if (n == null) return null;

    return n.getExpression();
  }

  // Sets the value of a node given by the string
  //
  // @see setValue
  // @see clearValue
  // --------------------------------------------------
  public void setValue (String dest, String value) {
    // First delete any existing information for node
    Node node = (Node) nodeHashtable.get (dest);
    if (node == null) {
      node = new Node (new Cell(dest), value);
      node.setExpression (new Expression (value));
      nodeHashtable.put (dest, node);
    }
    else {
      node.setValue (value);
      node.setExpression (new Expression (value));
      modifyLevels (node);
    }
    
    updateLevels (node);
  }


  // Clears all nodes from the hashtable
  //
  // @see setValue
  // @see getValue
  // --------------------------------------------------
  public void setValue () {
    nodeHashtable = new Hashtable();
  }


  // Returns the floating point equivalent of the node
  //
  // @see getValue
  // --------------------------------------------------
  public float getNumericValue (String s) {
    Node n = getNode (s);
    if (n == null)
      return 0;

    return n.getNumericValue();
  }


  // Returns the given node (if it exists)
  // --------------------------------------------------
  private Node getNode (String s) {
    Cell cell = new Cell (s);
    if (! cell.isValid())
      return null;

    Node n = (Node) nodeHashtable.get (cell.toString());
    return n;
  }

  // Evaluate the constant string as a float or a cell reference
  // --------------------------------------------------
  private float evaluateConstant (String s) {

    // BEFORE-START: before Callback -----------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[1];
      args[0] = s;
      Object o = adapter.invokeCallback (ComponentAdapter.BEFORE, "evaluateConstant", args);
      
    //alter the incoming parameter
    s = (String) o;
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    try {
      Float fl = new Float (s);
      float f = fl.floatValue();
      return f;
    } catch (NumberFormatException nfe) {
      Node n = getNode (s);
      if (n == null)
	return 0;

      // make sure that this node is calculated
      recalculateSpreadsheet (n);
      return n.getNumericValue();
    }
  }

  // Evaluate the value of the given node
  // --------------------------------------------------
  private void evaluate (Node node) {
    
    // BEFORE-START: before Callback -----------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[1];
      args[0] = node;
      adapter.invokeCallback (ComponentAdapter.BEFORE, "evaluate", args);
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    float   f;
    Expression expr = node.getExpression();

    // Evaluate Constant strings.  If not a float, then check to see if
    // it is a node reference (i.e., B5).  If not, calculate as 0
    if (expr.isConstant()) {
      f = evaluateConstant (node.getValue());
      node.setNumericValue (f);
    } else {
      // Must be a function
      // If an error occurs, set value to the detailed error message
      try {
	f = calculateFunction (expr.getOperator(), expr.elements());
	node.setNumericValue (f);
      } catch (FunctionException fe) {
	// don't change the value of this node...
      }
    }

    // AFTER-START: before Callback ------------------------------------]]
    //                                                                  ]]
    if (adapter != null) {
      Object args[] = new Object[1];
      args[0] = node;
      adapter.invokeCallback (ComponentAdapter.AFTER, "evaluate", args);
    }
    //                                                                  ]]
    // AFTER-END: before Callback --------------------------------------]]
  }

  // This is here for functions to calculate arbitrary sub-functions
  // 
  // @exception FunctionException if an argument is an unknown function
  // --------------------------------------------------
  public float calculateFunction (Expression expr) throws FunctionException {
    // beforeCalculateFunction (expr);

    float result =  calculateFunction (expr.getOperator(), expr.elements());

    // afterCalculateFunction (expr);
    return result;
  }

  // Private function to calculate operation and arguments in enumeration
  //
  // @exception FunctionException if an argument is an unknown function
  // --------------------------------------------------
  private float calculateFunction (String op, Enumeration en) throws FunctionException {
    Function func = getFunction (op);
    if (func == null) {
      System.out.println ("Unable to locate function: " + op);
      return 0;   // what to do?
    }

    return func.calculateFunction (en);
  }

  // Recalculate node value
  // --------------------------------------------------
  private void recalculateSpreadsheet(Node root) {
    if (root.getRecalculation() == false) return;
    evaluate (root);
    root.setRecalculation (false);
    refreshVector.addElement (root);

    for (Enumeration ven = root.getReferences(); ven.hasMoreElements();) {
      Cell cell = (Cell) ven.nextElement();

      Node vnode = (Node) nodeHashtable.get (cell.toString());
      if (vnode != null) {
	if (vnode.getRecalculation())
	  recalculateSpreadsheet (vnode);
      }
    }    
  }

  // Traverse (depth-first) all affected nodes to mark as recalculate
  // --------------------------------------------------
  private void markForRecalculation(Node n) {
    if (n.getRecalculation()) return;  // already visited
    n.setRecalculation (true);

    for (Enumeration ven = n.getReferences(); ven.hasMoreElements();) {
      Cell cell = (Cell) ven.nextElement();
      Node vnode = (Node) nodeHashtable.get (cell.toString());
      if (vnode != null) {
	if (!vnode.getRecalculation())
	  markForRecalculation (vnode);
      }
    }    
  }

  // Iterate over refreshVector to send out events
  // --------------------------------------------------
  private void generateRefreshEvents() {

    // NEGOTIATE-START:  -----------------------------------]]
    //                                                      ]]
    if (adapter != null) {
      Object args[] = new Object[1];
      args[0] = refreshVector;
      adapter.resolve ("filterCells", args);
    }
    //                                                                  ]]
    // BEFORE-END: before Callback -------------------------------------]]

    for (Enumeration ven = refreshVector.elements(); ven.hasMoreElements();) {
      Node n = (Node) ven.nextElement();

      String s = n.getValue();
      float  f = n.getNumericValue();

      String eventString;

      // If zero and the string is not zero, then this must be a text field
      // or otherwise uncomputable entry, so return the text.
      if (f == 0) { 
	if (Expression.isExpression (s))
	  eventString = Float.toString (0);
	else
	  eventString = s;
      } else
	eventString = Float.toString (f);

      SpreadsheetEventObject seo = new SpreadsheetEventObject (this, SpreadsheetEventObject.REFRESH, n.getCell(), eventString);

      fireAction (seo);
    }
  }

  // Request to insert a given value into a destination cell
  // --------------------------------------------------
  public void insertValue (Cell dest, String value) {
    Vector savedRefreshVector = refreshVector;   // save for recursion
    refreshVector = new Vector();    

    setValue (dest.toString(), value);

    // Automatically generate refresh events upon insert.  Perhaps
    // this should be requested, instead of automatic
    Node node = getNode (dest.toString());

    markForRecalculation(node);
    recalculateSpreadsheet(node);
    generateRefreshEvents();

    // Now restore for future recursions.
    refreshVector = savedRefreshVector;
  }

  // Request to recalculate the spreadsheet starting from the given cell
  // --------------------------------------------------
  public void recalculate (Cell dest) {
    Vector savedRefreshVector = refreshVector;   // save for recursion
    refreshVector = new Vector();    

    // Automatically generate refresh events upon insert.  Perhaps
    // this should be requested, instead of automatic
    Node node = getNode (dest.toString());
    
    markForRecalculation(node);
    recalculateSpreadsheet(node);
    generateRefreshEvents();

    // Now restore for future recursions.
    refreshVector = savedRefreshVector;
  }

  // Request to refresh the given destination cell region
  // --------------------------------------------------
  public void refresh (CellRegion destRegion) {
    Vector savedRefreshVector = refreshVector;   // save for recursion
    refreshVector = new Vector();

    // Run through the refresh region ONLY to determine those
    // cells that need updates, and then send them off
    while (destRegion.hasMoreElements()) {
      Cell cell = (Cell) destRegion.nextElement();
      Node n = (Node) nodeHashtable.get (cell.toString());
      if (n != null) {
	refreshVector.addElement (n);
      }
    }	

    generateRefreshEvents();
    refreshVector.removeAllElements();

    // Now restore for future recursions.
    refreshVector = savedRefreshVector;
  }

  private void handleSpreadsheetEvent (SpreadsheetEventObject seo) {
    CellRegion destRegion = new CellRegion (seo.getCellRegion());
    Cell dest = destRegion.getStart();
    Node node;

    Vector savedRefreshVector = refreshVector;   // save for recursion
    refreshVector = new Vector();

    switch (seo.getType()) {
    case SpreadsheetEventObject.INSERT:
      String value = seo.getValue();
      setValue (dest.toString(), value);

      // Automatically generate refresh events upon insert.  Perhaps
      // this should be requested, instead of automatic
      node = getNode (dest.toString());

      refreshVector.removeAllElements();
      markForRecalculation(node);
      recalculateSpreadsheet(node);
      generateRefreshEvents();
      refreshVector.removeAllElements();
      break;

    case SpreadsheetEventObject.RECALCULATE:
      // Automatically generate refresh events upon insert.  Perhaps
      // this should be requested, instead of automatic
      node = getNode (dest.toString());

      refreshVector.removeAllElements();
      markForRecalculation(node);
      recalculateSpreadsheet(node);
      generateRefreshEvents();
      refreshVector.removeAllElements();
      break;

    case SpreadsheetEventObject.REFRESH:
      refreshVector.removeAllElements();

      // Run through the refresh region ONLY to determine those
      // cells that need updates, and then send them off
      while (destRegion.hasMoreElements()) {
	Cell cell = (Cell) destRegion.nextElement();
	Node n = (Node) nodeHashtable.get (cell.toString());
	if (n != null) {
	  refreshVector.addElement (n);
	}
      }	

      generateRefreshEvents();
      refreshVector.removeAllElements();
      break;

      // This should dynamically add new function to the spreadsheet
    case SpreadsheetEventObject.NEWFUNCTION:
      
    }

    // Now restore for future recursions.
    refreshVector = savedRefreshVector;
  }
			   
  //----------------------------------------------------------------------
  // Methods for registering/deregistering SpreadsheetListeners
  //----------------------------------------------------------------------
  public synchronized void addSpreadsheetListener(SpreadsheetListener sl) {
    spreadsheetListeners.addElement(sl);
  }

  public synchronized void removeSpreadsheetListener(SpreadsheetListener sl) {
    spreadsheetListeners.removeElement(sl);
  }

  private void fireAction(SpreadsheetEventObject seo) {
    Vector targets;
    synchronized (this) {
      targets = (Vector) spreadsheetListeners.clone();
    }

    for (int i = 0; i < targets.size(); i++) {
      SpreadsheetListener target = (SpreadsheetListener) targets.elementAt(i);
      target.handleSpreadsheetEvent (seo);
    }
  }

  // -----------------------------------------------------------
  // Everything the follows is for adaptations of this component.
  // -----------------------------------------------------------

  // ------------------------------
  // Adaptable Interface
  // ------------------------------
  private ComponentAdapter adapter = null;

  // Inserts the adapter object
  public void setAdapter (ComponentAdapter ca) { adapter = ca; }

  // Retrieves the adapter object
  public ComponentAdapter getAdapter () { return adapter; }

}
