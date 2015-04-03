// Class  : Adaptable
// Author : George Heineman
//        : heineman@cs.wpi.edu
//        : http://www.cs.wpi.edu/~heineman
// Version: 1.0

package adapt;

public interface Adaptable {

  /** Sets the Component Adapter object
   *
   * @param ComponentAdapter
   *
   * @see #getAdapter
   */
  public void setAdapter (ComponentAdapter ca);

  /** 
   *
   * @return Returns the Component Adapter object
   * @see #setAdapter
   */
  public ComponentAdapter getAdapter ();
}
