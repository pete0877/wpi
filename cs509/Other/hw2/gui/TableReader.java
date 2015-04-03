// Class  : TableReader
// Author : George Heineman
//        : heineman@cs.wpi.edu
//        : http://www.cs.wpi.edu/~heineman
// Version: 1.0

package gui;

import java.util.*;

public interface TableReader {
  String getTableValue(int col, int row);
  void setTableValue(int col, int row, String value);
}


