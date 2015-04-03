// Class  : TextBeanListener
// Author : George Heineman
//        : heineman@cs.wpi.edu
//        : http://www.cs.wpi.edu/~heineman
// Version: 1.0

package gui;

import java.util.EventListener;

public interface TextBeanListener extends EventListener {

  void handleTextBeanEvent (TextBeanEventObject tbeo);

}
