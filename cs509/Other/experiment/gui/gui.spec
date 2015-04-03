// CSL Specification of GUI components


// an example of a base class
component class java.awt.Scrollbar extends class java.awt.Component {


  // Indexed Properties

  // Properties
  property int Orientation;
  property int Value;
  property int Minimum;
  property int Maximum;
  property int VisibleAmount;
  property int UnitIncrement;
  property int LineIncrement;
  property int BlockIncrement;
  property int PageIncrement;


  // Listeners
  listener AdjustmentListener;  // Not part of interface

  Port java.awt.Adjustable = {
    int getOrientation ();
    void setMinimum (int);
    int getMinimum ();
    void setMaximum (int);
    int getMaximum ();
    void setUnitIncrement (int);
    int getUnitIncrement ();
    void setBlockIncrement (int);
    int getBlockIncrement ();
    void setVisibleAmount (int);
    int getVisibleAmount ();
    void setValue (int);
    int getValue ();
    void addAdjustmentListener (interface java.awt.event.AdjustmentListener);
    void removeAdjustmentListener (interface java.awt.event.AdjustmentListener);
  }

  // Public Methods
  Port publicInterface = {
    void addNotify ();
    int getVisible ();
    void setValues (int, int, int, int);
  }

  // Private Methods
    private boolean eventEnabled (class java.awt.AWTEvent);
    private void processEvent (class java.awt.AWTEvent);
    private void processAdjustmentEvent (class java.awt.event.AdjustmentEvent);
    private class java.lang.String paramString ();
    private void writeObject (class java.io.ObjectOutputStream);
    private void readObject (class java.io.ObjectInputStream);

}



component class adapt.gui.TableBean extends class java.awt.Component {

  // Indexed Properties
  indexedProperty java.lang.String TableValue (int, int);

  // Properties
  property int RowHeight;
  property int ColumnWidth;
  property java.awt.Color Foreground;
  property java.awt.Color Background;
  property java.awt.Font Font;
  property adapt.gui.TableRegion SelectedRegion;
  property int NumColumns;
  property int NumRows;
  property adapt.ComponentAdapter Adapter;


  // Listeners
  listener TableListener;
  listener ActionListener;  // Not part of interface
  listener PropertyChangeListener;  // Not part of interface

  Port java.io.Serializable = {
  }
  Port java.awt.event.MouseMotionListener = {
    void mouseDragged (class java.awt.event.MouseEvent);
    void mouseMoved (class java.awt.event.MouseEvent);
  }
  Port java.awt.event.MouseListener = {
    void mouseClicked (class java.awt.event.MouseEvent);
    void mousePressed (class java.awt.event.MouseEvent);
    void mouseReleased (class java.awt.event.MouseEvent);
    void mouseEntered (class java.awt.event.MouseEvent);
    void mouseExited (class java.awt.event.MouseEvent);
  }
  Port adapt.gui.TableListener = {
    void handleTableEvent (class adapt.gui.TableEventObject);
  }
  Port adapt.gui.TableReader = {
    class java.lang.String getTableValue (int, int);
    void setTableValue (int, int, class java.lang.String);
  }
  Port adapt.Adaptable = {
    void setAdapter (class adapt.ComponentAdapter);
    class adapt.ComponentAdapter getAdapter ();
  }

  // Public Methods
  Port publicInterface = {
    void paint (class java.awt.Graphics);
    void redraw (class java.awt.Graphics, int, int);
    boolean select (class adapt.gui.TableRegion);
    boolean select (class adapt.gui.TableElement, class adapt.gui.TableElement);
    class java.awt.Dimension getPreferredSize ();
  }

  // Private Methods
    private void clip (class adapt.gui.TableElement);
    private void deselect ();
    private void redrawRegion (int, int, int, int);
    private void redrawRegion (class adapt.gui.TableElement, class adapt.gui.TableElement);
    private void setMinimum (class adapt.gui.TableElement);
    private void setMinimum (int, int);
    private void maintainRegion ();
    private boolean inInvertedRegion (int, int);
    private class adapt.gui.TableElement convertMouseEvent (class java.awt.event.MouseEvent);
    private void fireAction (class adapt.gui.TableEventObject);
    private void sizeToFit ();

}


component class adapt.gui.ScrollbarBean extends class java.awt.Scrollbar {


  // Indexed Properties

  // Properties


  // Listeners

  Port java.io.Serializable = {
  }

  // Public Methods
  Port publicInterface = {
  }

  // Private Methods

}


component class adapt.gui.TextBean extends class java.awt.TextField {


  // Indexed Properties

  // Properties


  // Listeners
  listener TextBeanListener;  // Not part of interface

  Port java.io.Serializable = {
  }
  Port java.awt.event.KeyListener = {
    void keyTyped (class java.awt.event.KeyEvent);
    void keyPressed (class java.awt.event.KeyEvent);
    void keyReleased (class java.awt.event.KeyEvent);
  }

  // Public Methods
  Port publicInterface = {
    void setText (class java.lang.String);
    class java.awt.Dimension getPreferredSize ();
    class java.awt.Dimension preferredSize ();
    class java.awt.Dimension getMinimumSize ();
    class java.awt.Dimension minimumSize ();
  }

  // Private Methods
    private void fireAction (class adapt.gui.TextBeanEventObject);

}
