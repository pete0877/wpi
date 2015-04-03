// Class  : SpreadsheetBeanInfo
// Author : George Heineman
//        : heineman@cs.wpi.edu
//        : http://www.cs.wpi.edu/~heineman
// Version: 1.0

package spreadsheet;

/** 
 * The only thing we define in the SpreadSheet BeanInfo is a GIF icon.
 */

import java.beans.*;

public class SpreadsheetBeanInfo extends SimpleBeanInfo {

    public java.awt.Image getIcon(int iconKind) {
	if (iconKind == BeanInfo.ICON_MONO_16x16 ||
	    iconKind == BeanInfo.ICON_COLOR_16x16 ) {
	    java.awt.Image img = loadImage("SpreadsheetIcon16.gif");
	    return img;
	}
	if (iconKind == BeanInfo.ICON_MONO_32x32 ||
	    iconKind == BeanInfo.ICON_COLOR_32x32 ) {
	    java.awt.Image img = loadImage("SpreadsheetIcon32.gif");
	    return img;
	}
	return null;
    }
}
