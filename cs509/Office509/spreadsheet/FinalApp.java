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

  TextArea         debugOutput;

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

  public void init() {
    init_gui();
    init_components();
    
    // Add new functions to the spreadsheet
    spreadsheet.setFunction ("STDEV", new StdevFunction(spreadsheet));

    // move to square A1 in the spreadsheet
    tableSelected = new CellRegion (new Cell (1,1), new Cell (1,1));
    tb.setSelectedRegion (new TableRegion (new TableElement (1, 1),
					   new TableElement (1, 1)));

    // update status
    tbBox.setTableValue (1, 1, tableSelected.toString (true));
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

    // THIS IS ONLY HERE FOR DEBUGGING.  DELETE WHEN THE APPLICATION IS 
    // COMPLETE
    // --------------------------------------------vvvvvvvvvvvvvvvv
    // Third Line: <OUTPUT>
    debugOutput = new TextArea (5, 60);
    p.add ("South", debugOutput);
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
	debugOutput.append ("Horizontal Scrollbar in use...\n");
    }

    if (e.getSource() == verticalScrollbar) {
	debugOutput.append ("Vertical Scrollbar in use...\n");
    }
  }

  // ------------------------------
  // Methods for TextBeanListener
  // ------------------------------
  public void handleTextBeanEvent (TextBeanEventObject tbeo) {
    if (tbeo.getSource() == textBean) {
	debugOutput.append ("Handle TextBean event...\n");
    }
  }

  // ------------------------------
  // Methods for TableEventListener
  // ------------------------------
  public void handleTableEvent (TableEventObject teo) {
    if (teo.getSource() == tb) {
	debugOutput.append ("Handle event from main TableBean...\n");
    } else if (teo.getSource() == tbC) {
	debugOutput.append ("Handle event from Column TableBean...\n");
    } else if (teo.getSource() == tbR) {
	debugOutput.append ("Handle event from Row TableBean...\n");
    }
  }

  public static void main (String args[]) {
      FinalApp fa = new FinalApp ();
      fa.init();
      fa.show();
  }
}
