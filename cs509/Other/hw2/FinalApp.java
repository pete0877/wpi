// Class  : FinalApp
// Author : George Heineman
//        : heineman@cs.wpi.edu
//        : http://www.cs.wpi.edu/~heineman
// Version: 1.0

//
// This Applet combines eight Java Beans together into a working application.
// The key is the communication between the different components through 
// Events

import java.applet.Applet;
import java.awt.event.AdjustmentListener;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentEvent;
import java.awt.*;
import java.io.Serializable;
import java.util.Vector;

import spreadsheet.*;
import gui.*;
import adapt.*;

public class FinalApp extends Applet implements AdjustmentListener, TableListener, TextBeanListener, Serializable {

  // The Eight Java Bean components for this application
  Scrollbar        verticalScrollbar;
  Scrollbar        horizontalScrollbar;
  TableBean        tb;
  TableBean        tbBox;
  TableBean        tbC;
  TableBean        tbR;
  TextBean         textBean;
 

  // Default values of parameters for this Applet
  //

  /**
   * Size of the Applet Screen
   */
  private int viewHeight = 10;
  private int viewWidth = 5;

  //   Size of the Internal Spreadsheet
  private int spreadsheetHeight = 100;
  private int spreadsheetWidth = 100;

  // State variables that define the dynamic state
  private int         leftColumn;
  private int         topRow;
  private CellRegion  tableSelected;
  private CellRegion  tableRSelected;
  private CellRegion  tableCSelected;
  private Spreadsheet spreadsheet;

  private String get_row_label (int row_number) {
	return new Integer(row_number).toString();
  }
  
  private String get_column_label (int column_number) {
  	int a, b;
	String result = "", c, encoding = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	a = column_number - 1;
	while (a / 26 != 0) {
		b = a / 26;
		a = a - 26 * b;
		c = encoding.substring (b - 1, b);
		result = result.concat(c);
	}
	c = encoding.substring (a, a + 1);
	result = result.concat(c);	
	return result;
  }

  private void update_reference_box() {
	String reference = "";
	
	// check what was selected

	if (tableCSelected != null) {		
		// a column is selected		
		reference = reference.concat (get_column_label(tableCSelected.getStart().getColumn()));
		reference = reference.concat (get_row_label(tableCSelected.getStart().getRow()));
		tbBox.setTableValue(1,1,reference);	
		return;
	}
	
	if (tableRSelected != null) {		
		// a row is selected		
		reference = reference.concat (get_column_label(tableRSelected.getStart().getColumn()));
		reference = reference.concat (get_row_label(tableRSelected.getStart().getRow()));
		tbBox.setTableValue(1,1,reference);				
		return;
	}	
	
	if (tableSelected != null) {		
		// a rect. region is selected		
		reference = reference.concat (get_column_label(tableSelected.getStart().getColumn()));
		reference = reference.concat (get_row_label(tableSelected.getStart().getRow()));
		tbBox.setTableValue(1,1,reference);				
		return;
	}
	
  }
			
  private void update_text_box() {
	
	String reference = "";
		
	// check what was selected

	if (tableCSelected != null) {		
		// a column is selected		
		reference = reference.concat (get_column_label(tableCSelected.getStart().getColumn()));
		reference = reference.concat (get_row_label(tableCSelected.getStart().getRow()));
		textBean.setText(spreadsheet.getValue(reference));
		return;
	}
	
	if (tableRSelected != null) {		
		// a row is selected		
		reference = reference.concat (get_column_label(tableRSelected.getStart().getColumn()));
		reference = reference.concat (get_row_label(tableRSelected.getStart().getRow()));
		textBean.setText(spreadsheet.getValue(reference));
		return;
	}	
	
	if (tableSelected != null) {		
		// a rect. region is selected		
		reference = reference.concat (get_column_label(tableSelected.getStart().getColumn()));
		reference = reference.concat (get_row_label(tableSelected.getStart().getRow()));
		textBean.setText(spreadsheet.getValue(reference));
		return;
	}  
  }
 
  private void update_column_selector () {
		int a;	
		// Column selector:
		for (a=1; a<=viewWidth; a++) {
			tbC.setTableValue(a, 1, get_column_label(leftColumn - 1 + a));		
		}
		
		tbC.setSelectedRegion(null);		
		// see if a column is selected
		if (tableCSelected != null) {					
			// make sure the right columns are highlighted
			int start_column, end_column;
			
			start_column = tableCSelected.getStart().getColumn() - leftColumn + 1;
			end_column   = tableCSelected.getEnd().getColumn() - leftColumn + 1;
		
			// adjust for scrolling:
			if (start_column < 1) start_column = 1;
			if (end_column > viewWidth) end_column = viewWidth;
	
			// make sure that the region is visible at all:
			if ((end_column < 1) || (start_column > viewWidth)) {
				// region is not visible
			} else {			
				tbC.setSelectedRegion (new TableRegion (
							new TableElement (start_column, 1),
							new TableElement (end_column, 1)));
				tbC.repaint();
			}
		}

   }
   
  private void update_row_selector () {
		int a;	
		// Row selector:
		for (a=1; a<=viewHeight; a++) {
			tbR.setTableValue(1, a, get_row_label(topRow - 1 + a));		
		}
		
		tbR.setSelectedRegion(null);
		// see if a row is selected
		if (tableRSelected != null) {					
			// make sure the right rows are highlighted
			int start_row, end_row;
			
			start_row = tableRSelected.getStart().getRow() - topRow + 1;
			end_row   = tableRSelected.getEnd().getRow() - topRow + 1;
		
			// adjust for scrolling:
			if (start_row < 1) start_row = 1;
			if (end_row > viewHeight) end_row = viewHeight;
	
			// make sure that the region is visible at all:
			if ((end_row < 1) || (start_row > viewHeight)) {
				// region is not visible
			} else {			
				tbR.setSelectedRegion (new TableRegion (
							new TableElement (1, start_row),
							new TableElement (1, end_row)));
				tbR.repaint();							
			}
		}

   }   
   

	private void update_cell_value () {
		String reference = tbBox.getTableValue(1,1);
		spreadsheet.setValue (reference, textBean.getText());		
		spreadsheet.recalculate (new Cell (reference));
	}

  private void update_main_table () {
		int a, b;	
		String reference, value;		

		// update table element values
		for (a=1; a<=viewWidth; a++) {
			for (b=1; b<=viewHeight; b++) {
				reference = "";
				reference = reference.concat (get_column_label(leftColumn - 1 + a));
				reference = reference.concat (get_row_label(topRow - 1 + b));				
				if (spreadsheet.getValue (reference).equals("")) {
					tb.setTableValue(a, b, "");		
				} else {
					value = new String().valueOf (spreadsheet.getNumericValue (reference));
					tb.setTableValue(a, b, value);		
				}
				
			}			
		}
		
		// update the selection hightlights:

		int start_column = 1, start_row = 1;
		int end_column = 1, end_row = 1;			
		
		if (tableCSelected != null) {		
			// a column is selected		
			start_column = tableCSelected.getStart().getColumn() - leftColumn + 1;
			end_column   = tableCSelected.getEnd().getColumn() - leftColumn + 1;
			start_row	 = 1;
			end_row		 = viewHeight;
		}
		
		if (tableRSelected != null) {		
			// a row is selected		
			start_column = 1;
			end_column   = viewWidth;
			start_row	 = tableRSelected.getStart().getRow() - topRow + 1;
			end_row		 = tableRSelected.getEnd().getRow() - topRow + 1;
		}	
		
		if (tableSelected != null) {		
			// a rect. region is selected		
			start_column = tableSelected.getStart().getColumn() - leftColumn + 1;
			end_column   = tableSelected.getEnd().getColumn() - leftColumn + 1;
			start_row	 = tableSelected.getStart().getRow() - topRow + 1;
			end_row		 = tableSelected.getEnd().getRow() - topRow + 1;		
		}		

		// adjust for scrolling:
		if (start_column < 1) start_column = 1;
		if (start_row < 1) start_row = 1;		
		if (end_column > viewWidth) end_column = viewWidth;
		if (end_row > viewHeight) end_row = viewHeight;		
		
		tb.setSelectedRegion (null);		
		// make sure that the region is visible at all:
		if ((end_column < 1) || (end_row < 1) || (start_column > viewWidth) || (start_row > viewHeight)) {
			// region is not visible
		} else {			
			tb.setSelectedRegion (new TableRegion (
						new TableElement (start_column, start_row),
						new TableElement (end_column, end_row)));
			tb.repaint();
		}
   }

  public void init() {
    init_gui();
    init_components();
    
    // Add new functions to the spreadsheet
    spreadsheet.setFunction ("STDEV", new StdevFunction(spreadsheet));

    // move to square A1 in the spreadsheet
	leftColumn = 1;
	topRow = 1;	
    tableSelected = new CellRegion (new Cell (1,1), new Cell (1,1));
    tb.setSelectedRegion (new TableRegion (new TableElement (1, 1), new TableElement (1, 1)));

	// update status
    tbBox.setTableValue (1, 1, tableSelected.toString (true));

	update_reference_box();
	update_text_box();
	update_row_selector ();
	update_column_selector ();	
	update_main_table ();
	
  }

  // Creates the user interface, composed of two scrollbars 4 table beans, 
  private void init_gui() {
    Label l;
    Font  font = new Font("Dialog", Font.PLAIN, 12);
    Font  headerFont = new Font("Dialog", Font.BOLD, 12);
    FontMetrics fm = getFontMetrics(font);

    Panel p = new Panel();
    p.setLayout (new BorderLayout());

    // Search for Applet parameters
    try {
      spreadsheetWidth = Integer.valueOf(getParameter("SpreadsheetWidth")).intValue();
    } catch (Exception e) {
      // not set...
    }

    try {
      spreadsheetHeight = Integer.valueOf (getParameter("SpreadsheetHeight")).intValue();
    } catch (Exception e) {
      // not set...
    }

    try {
      viewHeight = Integer.valueOf (getParameter("ViewHeight")).intValue();
    } catch (Exception e) {
      // not set...
    }

    try {
      viewWidth = Integer.valueOf (getParameter("ViewWidth")).intValue();
    } catch (Exception e) {
      // not set...
    }

    // Construct the beans first
    tb = new TableBean (viewWidth, viewHeight);    // center
    tb.setFont (font);
    tb.setColumnWidth (fm.stringWidth("XXX,XXX.XX"));
    tb.setRowHeight   (fm.getMaxAscent() + fm.getMaxDescent());
    tb.setForeground (Color.black);
    tb.setBackground (Color.white);

    tbC = new TableBean (viewWidth, 1);
    tbC.setFont (headerFont);
    tbC.setColumnWidth (fm.stringWidth("XXX,XXX.XX"));
    tbC.setRowHeight   (fm.getMaxAscent() + fm.getMaxDescent());
    tbC.setForeground (Color.white);
    tbC.setBackground (Color.gray);

    tbR = new TableBean (1, viewHeight);
    tbR.setFont (headerFont);
    tbR.setColumnWidth (fm.stringWidth("12345"));
    tbR.setRowHeight   (fm.getMaxAscent() + fm.getMaxDescent());
    tbR.setForeground (Color.white);
    tbR.setBackground (Color.gray);

    tbBox = new TableBean (1, 1);  // Box for region
    tbBox.setFont (font);

    // Get maximum column/row labels
    String maxColumnLabel = tbC.getTableValue (spreadsheetWidth, spreadsheetHeight);
    String maxRowLabel = tbR.getTableValue (spreadsheetWidth, spreadsheetHeight);
    tbBox.setColumnWidth (fm.stringWidth(maxColumnLabel+maxRowLabel+":"+maxColumnLabel+maxRowLabel));
    tbBox.setRowHeight   (fm.getMaxAscent() + fm.getMaxDescent());

    Panel tp = new Panel();
    GridBagLayout gb = new GridBagLayout();
    GridBagConstraints gc = new GridBagConstraints ();
    tp.setLayout (gb);

    // First Line: <BlankLabel> <COLUMN HEADERS>
    gc.weightx = 1.0;
    gc.fill = GridBagConstraints.NONE;
    gc.gridwidth = GridBagConstraints.RELATIVE;
    l = new Label ("");
    gb.setConstraints (l, gc);
    tp.add (l);

    gc.weightx = 1.0;
    gc.anchor = GridBagConstraints.WEST;
    gc.fill = GridBagConstraints.NONE;
    gc.gridwidth = GridBagConstraints.REMAINDER;
    gb.setConstraints (tbC, gc);
    tp.add (tbC);

    // Second Line: <ROW HEADERS> <TableBean>
    gc.weightx = 1.0;
    gc.fill = GridBagConstraints.NONE;
    gc.gridwidth = GridBagConstraints.RELATIVE;
    gb.setConstraints (tbR, gc);
    tp.add (tbR);

    gc.fill = GridBagConstraints.NONE;
    gc.gridwidth = GridBagConstraints.REMAINDER;
    gb.setConstraints (tb, gc);
    tp.add (tb);

    // Now finally, add to the main panel
    p.add ("Center", tp);

    int maxWidth = tb.getPreferredSize().width + tbR.getPreferredSize().width;
    int maxHeight = tb.getPreferredSize().height + tbC.getPreferredSize().height;

    p.add ("West", verticalScrollbar = new Scrollbar (Scrollbar.VERTICAL, 1, viewHeight, 1, spreadsheetHeight + 1));
    verticalScrollbar.setSize (32, maxHeight);

    // This is the top of the panel
    Panel p2 = new Panel();
    gb = new GridBagLayout();
    gc = new GridBagConstraints ();
    p2.setLayout (gb);

    // p2.First Line: <TBBOX> <TEXT>
    gc.weightx = 1.0;
    gc.anchor = GridBagConstraints.WEST;
    gc.fill = GridBagConstraints.NONE;
    gc.gridwidth = GridBagConstraints.RELATIVE;
    gb.setConstraints (tbBox, gc);
    p2.add (tbBox);

    gc.anchor = GridBagConstraints.EAST;
    gc.fill = GridBagConstraints.HORIZONTAL;
    gc.gridwidth = GridBagConstraints.REMAINDER;
    textBean = new TextBean (50);
    gb.setConstraints (textBean, gc);
    p2.add (textBean);

    // p2.Second Line: <HorizontalScrollBar>
    gc.anchor = GridBagConstraints.EAST;
    gc.fill = GridBagConstraints.HORIZONTAL;
    gc.gridwidth = GridBagConstraints.REMAINDER;

    horizontalScrollbar = new Scrollbar (Scrollbar.HORIZONTAL,
    					     1, viewWidth, 1, spreadsheetWidth + 1);
    //horizontalScrollbar.setSize (32, maxWidth);
    gb.setConstraints (horizontalScrollbar, gc);
    p2.add (horizontalScrollbar);

    p.add ("North", p2);
    add (p);
  }

  /**
   * Initializes the components so they can communicate with each other.
   *
   */
  private void init_components() {
    spreadsheet = new Spreadsheet();

    // Somewhat convoluted, but in this model we create the GUI components
    // first and then hook them up...

    // Connect Scrollbars with this applet
    horizontalScrollbar.addAdjustmentListener(this);
    verticalScrollbar.addAdjustmentListener(this);

    // Connect the various TableBeans with this applet
    tb.addTableListener (this);
    tbC.addTableListener (this);
    tbR.addTableListener (this);

    // Add this so we can simply deactivate any selections in the tbBox
    tbBox.addTableListener (this);

    // Pick up on user input
    textBean.addTextBeanListener (this);
   }


  // ------------------------------
  // Methods for AdjustmentListener
  // ------------------------------
  public void adjustmentValueChanged (AdjustmentEvent e) {
    TableElement startTE, endTE;

    if (e.getSource() == horizontalScrollbar) {
		leftColumn = horizontalScrollbar.getValue();
		update_main_table ();
		update_column_selector ();		
    }

    if (e.getSource() == verticalScrollbar) {
		topRow = verticalScrollbar.getValue();
		update_main_table ();		
		update_row_selector ();
    }
  }

  // ------------------------------
  // Methods for TextBeanListener
  // ------------------------------
  public void handleTextBeanEvent (TextBeanEventObject tbeo) {
    if (tbeo.getSource() == textBean) {
		update_cell_value();		
		update_main_table ();		
    }
  }

  // ------------------------------
  // Methods for TableEventListener
  // ------------------------------
  public void handleTableEvent (TableEventObject teo) {	
    if (teo.getSource() == tb) {
		if (teo.getType() == TableEventObject.MOUSE_RELEASE) {
			TableElement startElement = teo.getFrom();
			TableElement endElement = teo.getTo();						
			
			// update current cell region:
			tableSelected = new CellRegion (
				new Cell (leftColumn - 1 + startElement.getColumn(), topRow - 1 + startElement.getRow()),
				new Cell (leftColumn - 1 + endElement.getColumn(), topRow - 1 + endElement.getRow()));			
			
			update_reference_box();			
			update_text_box();
		}		
		if (teo.getType() == TableEventObject.MOUSE_PRESS || teo.getType() == TableEventObject.MOUSE_DOWN) {
			tableRSelected = null;
			tableCSelected = null;				
			update_row_selector ();
			update_column_selector ();			
			textBean.setText("");
			TableElement startElement = teo.getFrom();
			TableElement endElement = teo.getTo();			
			Cell c1 = new Cell (leftColumn - 1 + startElement.getColumn(), topRow - 1 + startElement.getRow());
			Cell c2 = new Cell (leftColumn - 1 + endElement.getColumn(), topRow - 1 + endElement.getRow());			
			if (c1.isSame(c2)) {
				tbBox.setTableValue (1,1, c1.toString());
			} else {
				tbBox.setTableValue (1,1, c1.toString() + " x " + c2.toString());			
			}
		}		
    } else if (teo.getSource() == tbC) {				
		tbR.setSelectedRegion(null);
		if (teo.getType() == TableEventObject.MOUSE_RELEASE) {
			TableElement startElement = teo.getFrom();
			TableElement endElement = teo.getTo();						
			
			// update current cell region:
			tableCSelected = new CellRegion (
				new Cell (leftColumn - 1 + startElement.getColumn(), topRow - 1 + startElement.getRow()),
				new Cell (leftColumn - 1 + endElement.getColumn(), topRow - 1 + endElement.getRow()));			
			
			update_reference_box();			
			update_text_box();
		}		
		if (teo.getType() == TableEventObject.MOUSE_PRESS || teo.getType() == TableEventObject.MOUSE_DOWN) {
			tableRSelected = null;
			tableSelected = null;										
			textBean.setText("");
			TableElement startElement = teo.getFrom();
			TableElement endElement = teo.getTo();

			CellRegion tmp = new CellRegion (
				new Cell (leftColumn - 1 + startElement.getColumn(), topRow - 1 + startElement.getRow()),
				new Cell (leftColumn - 1 + endElement.getColumn(), topRow - 1 + endElement.getRow()));	
				
			if (tmp.isSame(tableCSelected)) { return; }

			// update current cell region:
			tableCSelected = new CellRegion (
				new Cell (leftColumn - 1 + startElement.getColumn(), topRow - 1 + startElement.getRow()),
				new Cell (leftColumn - 1 + endElement.getColumn(), topRow - 1 + endElement.getRow()));	
					
			Cell c1 = new Cell (leftColumn - 1 + startElement.getColumn(), topRow - 1 + startElement.getRow());
			Cell c2 = new Cell (leftColumn - 1 + endElement.getColumn(), topRow - 1 + endElement.getRow());			
			if (c1.isSame(c2)) {
				tbBox.setTableValue (1,1, c1.toString());
			} else {
				tbBox.setTableValue (1,1, "");			
			}
			update_main_table();
		}	
    } else if (teo.getSource() == tbR) {
		tbC.setSelectedRegion(null);
		if (teo.getType() == TableEventObject.MOUSE_RELEASE) {
			TableElement startElement = teo.getFrom();
			TableElement endElement = teo.getTo();						
			
			// update current cell region:
			tableRSelected = new CellRegion (
				new Cell (leftColumn - 1 + startElement.getColumn(), topRow - 1 + startElement.getRow()),
				new Cell (leftColumn - 1 + endElement.getColumn(), topRow - 1 + endElement.getRow()));			
			
			update_reference_box();			
			update_text_box();
		}		
		if (teo.getType() == TableEventObject.MOUSE_PRESS || teo.getType() == TableEventObject.MOUSE_DOWN) {
			tableCSelected = null;
			tableSelected = null;										
			textBean.setText("");
			TableElement startElement = teo.getFrom();
			TableElement endElement = teo.getTo();

			CellRegion tmp = new CellRegion (
				new Cell (leftColumn - 1 + startElement.getColumn(), topRow - 1 + startElement.getRow()),
				new Cell (leftColumn - 1 + endElement.getColumn(), topRow - 1 + endElement.getRow()));	
				
			if (tmp.isSame(tableRSelected)) { return; }

			// update current cell region:
			tableRSelected = new CellRegion (
				new Cell (leftColumn - 1 + startElement.getColumn(), topRow - 1 + startElement.getRow()),
				new Cell (leftColumn - 1 + endElement.getColumn(), topRow - 1 + endElement.getRow()));	
					
			Cell c1 = new Cell (leftColumn - 1 + startElement.getColumn(), topRow - 1 + startElement.getRow());
			Cell c2 = new Cell (leftColumn - 1 + endElement.getColumn(), topRow - 1 + endElement.getRow());			
			if (c1.isSame(c2)) {
				tbBox.setTableValue (1,1, c1.toString());
			} else {
				tbBox.setTableValue (1,1, "");			
			}
			update_main_table();
		}	
    }
  }

  public static void main (String args[]) {
      FinalApp fa = new FinalApp ();
      fa.init();
      fa.start();

  }
}
