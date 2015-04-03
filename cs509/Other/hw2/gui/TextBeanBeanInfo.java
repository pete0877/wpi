// Class  : TextBeanBeanInfo
// Author : George Heineman
//        : heineman@cs.wpi.edu
//        : http://www.cs.wpi.edu/~heineman
// Version: 1.0

package gui;

/** 
 * The only thing we define in the TextBeanBeanInfo is a GIF icon.
 * 
 * NOTE: Since the name of the JavaBean is TextBean, the name of this
 *       info class must be TextBeanBeanInfo, since that is the 
 *       default name appended to the name of the class
 */

import java.beans.*;

public class TextBeanBeanInfo extends SimpleBeanInfo {

    public java.awt.Image getIcon(int iconKind) {
	if (iconKind == BeanInfo.ICON_MONO_16x16 ||
	    iconKind == BeanInfo.ICON_COLOR_16x16 ) {
	    java.awt.Image img = loadImage("TextBeanIcon16.gif");
	    return img;
	}
	if (iconKind == BeanInfo.ICON_MONO_32x32 ||
	    iconKind == BeanInfo.ICON_COLOR_32x32 ) {
	    java.awt.Image img = loadImage("TextBeanIcon32.gif");
	    return img;
	}
	return null;
    }
}
