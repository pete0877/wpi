// Class  : TableListener
// Author : George Heineman
//        : heineman@cs.wpi.edu
//        : http://www.cs.wpi.edu/~heineman
// Version: 1.0

package gui;

import java.util.EventListener;

public interface TableListener extends EventListener {

  void handleTableEvent (TableEventObject teo);

}
