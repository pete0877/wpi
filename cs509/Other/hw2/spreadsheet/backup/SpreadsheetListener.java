// Class  : SpreadsheetListener
// Author : George Heineman
//        : heineman@cs.wpi.edu
//        : http://www.cs.wpi.edu/~heineman
// Version: 1.0


package adapt.spreadsheet;

import java.util.EventListener;

public interface SpreadsheetListener extends EventListener {
  void handleSpreadsheetEvent (SpreadsheetEventObject seo);
}
