// Class  : CallbackSet
// Author : George Heineman
//        : heineman@cs.wpi.edu
//        : http://www.cs.wpi.edu/~heineman
// Version: 1.1

package adapt;

import java.lang.reflect.Method;

// Stores callback information
public class CallbackSet {
  private Method method;
  private Object obj;

  public CallbackSet (Object obj, Method m) {
    this.obj    = obj;
    this.method = m;
  }

  public Method getMethod () { return method; }
  public void   setMethod (Method m) { method = m; }

  public Object getObject () { return obj; }
  public void   setObject (Object obj) { this.obj = obj; }
}


