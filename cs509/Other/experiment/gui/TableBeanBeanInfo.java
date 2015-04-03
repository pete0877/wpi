// Class  : TableBeanBeanInfo
// Author : George Heineman
//        : heineman@cs.wpi.edu
//        : http://www.cs.wpi.edu/~heineman
// Version: 1.0

package gui;

/** 
 * The only thing we define in the TableBean BeanInfo is a GIF icon.
 *
 * NOTE: Since the name of the JavaBean is TableBean, the name of this
 *       info class must be TableBeanBeanInfo, since that is the 
 *       default name appended to the name of the class
 */

import java.beans.*;

public class TableBeanBeanInfo extends SimpleBeanInfo {

    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor background =
			new PropertyDescriptor("background", beanClass);
	    PropertyDescriptor foreground =
			new PropertyDescriptor("foreground", beanClass);
	    PropertyDescriptor font =
			new PropertyDescriptor("font", beanClass);
            PropertyDescriptor rowHeight =
			new PropertyDescriptor("rowHeight", beanClass);
            PropertyDescriptor columnWidth =
			new PropertyDescriptor("columnWidth", beanClass);
            PropertyDescriptor numRows =
			new PropertyDescriptor("numRows", beanClass);
            PropertyDescriptor numColumns =
			new PropertyDescriptor("numColumns", beanClass);
            PropertyDescriptor selectedRegion =
			new PropertyDescriptor("selectedRegion", beanClass);

            background.setBound(true);
            foreground.setBound(true);
            font.setBound(true);
            columnWidth.setBound(true);
            rowHeight.setBound(true);
            numRows.setBound(true);
            numColumns.setBound(true);
            selectedRegion.setBound(true);

            PropertyDescriptor rv[] = {background, foreground,
				       font, 
				       columnWidth, rowHeight,
				       numRows, numColumns, selectedRegion };
            return rv;
        } catch (IntrospectionException e) {
            throw new Error(e.toString());
        }
    }


    public int getDefaultPropertyIndex() {
      return 0; 
    }



    public java.awt.Image getIcon(int iconKind) {
	if (iconKind == BeanInfo.ICON_MONO_16x16 ||
	    iconKind == BeanInfo.ICON_COLOR_16x16 ) {
	    java.awt.Image img = loadImage("TableBeanIcon16.gif");
	    return img;
	}
	if (iconKind == BeanInfo.ICON_MONO_32x32 ||
	    iconKind == BeanInfo.ICON_COLOR_32x32 ) {
	    java.awt.Image img = loadImage("TableBeanIcon32.gif");
	    return img;
	}
	return null;
    }

    private final static Class beanClass = TableBean.class;
}
