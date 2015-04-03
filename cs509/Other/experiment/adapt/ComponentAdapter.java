// Class  : ComponentAdapter
// Author : George Heineman
//        : heineman@cs.wpi.edu
//        : http://www.cs.wpi.edu/~heineman
// Version: 1.1

package adapt;

import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Vector;
import java.util.StringTokenizer;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

// Currently this only allows one object/method for each callback, although 
// this doesn't have to be the case.
//
public class ComponentAdapter {

  private Hashtable methodHashtable = new Hashtable(10);

  /** Represents the callback BEFORE a method is invoked
   *
   */
  public static final String BEFORE = "before";

  /** Represents the callback AFTER a method has been invoked
   *
   */
  public static final String AFTER  = "after";

  /** Represents a negotiated method invocation
   *
   */
  public static final String NEGOTIATE = "negotiate";

  /** Returns a class array of the types as contained in the space-separated
   *  list of parameters
   *
   *  @param list is a space-separated String of type names.
   * 
   *  @return Returns class array or null on error.
   */
  public static Class[] paramList (String list) {
    Vector pVector = new Vector(5);
    StringTokenizer st = new StringTokenizer (list, new String (" "), false);
    while (st.hasMoreTokens()) {
      String s = st.nextToken();
      try {
	pVector.addElement (Class.forName (s));
      } catch (ClassNotFoundException cnfe) {
	pVector.addElement (null);
      }
    }
    
    // Return array
    Class paramTypes[] = new Class[pVector.size()];
    pVector.copyInto (paramTypes);
    return paramTypes;
  }

  /**
   *   @param where is one of BEFORE or AFTER
   *   @param method is a String representing the target method
   *
   *   @return Returns the given callback set or null
   *
   *   @see #BEFORE
   *   @see #AFTER
   *   @see #insertCallback
   *   @see #removeCallback
   */
  public CallbackSet getCallbackSet (String where, String method) {
    return (CallbackSet) methodHashtable.get (where + method);
  }

  /** Inserts the given method on an object as a callback
   *
   *   @param #where is one of #BEFORE or #AFTER
   *   @param #method is a String representing the target method
   *   @param #obj is the target object that contains the callback function
   *   @param #callbackMethod is the callback method
   *
   *   @see #BEFORE
   *   @see #AFTER
   *   @see #getCallbackSet
   *   @see #removeCallback
   */
  public void insertCallback (String where, String method, Object obj, Method callbackMethod) {
    CallbackSet cs = new CallbackSet (obj, callbackMethod);
    methodHashtable.put (where + method, cs);
  }
  
  /** Removes the given method on an object as a callback
   *
   *   @param where is one of BEFORE or AFTER
   *   @param method is a String representing the target method
   *   @param obj is the target object that contains the callback function
   *   @param callbackMethod is the callback method
   *
   *   @see #getCallbackSet
   *   @see #insertCallback
   */
  public void removeCallback (String where, String method, Object obj, Method callbackMethod) {
    methodHashtable.remove (where + method);
  }

  /** Invokes the callback methods for the given method
   *
   *   @param where is one of BEFORE or AFTER
   *   @param method is a String representing the target method
   *   @param args is the list of arguments for the callback methods
   *
   *   @return Returns the result (as a <tt>java.Object</tt>) of the callback
   *       method.
   *
   *    Note: To override, return the expected value of the method's return
   *          code.  if this value is void, return null
   * 
   *          To deny, return an exception object (i.e., NOT a throw)
   *
   *          To augment, return 'this' which will be the glue code
   *          object.  Clearly, the receive will not be prepared for this
   *          and will therefore just proceed as normal.
   * 
   *   @see #BEFORE
   *   @see #AFTER
   */
  public Object invokeCallback (String where, String method, Object args[]) {
    CallbackSet cs = getCallbackSet (where, method);
    if (cs != null) {
      try {
	Object obj = cs.getObject();
	Method m   = cs.getMethod();
	//System.out.println ("method: " + m.toString());
	
	return m.invoke (obj, args);

      } catch (InvocationTargetException ite) {
	// This is a real exception generated from the special call
	System.out.println ("InvocationTargetException");
	return ite;
      } catch (Exception e) {
	// It doesn't matter what the reason, all exceptions are trapped
	System.out.println ("Exception" + e.toString());
	return e;
      }
    }
    
    // If no callback, return non-null argument to continue
    return this;
  }

  /** Invokes the appropriate negotiation methods
   *
   *   @param method is a String representing the target method
   *   @param args is the list of arguments for the callback methods
   *
   *   @return Returns the result (as a <tt>java.Object</tt>) of the callback
   *       method.
   *
   *   @see #NEGOTIATE
   */
  public Object resolve (String method, Object args[]) {
    return invokeCallback (NEGOTIATE, method, args);
  }


}
