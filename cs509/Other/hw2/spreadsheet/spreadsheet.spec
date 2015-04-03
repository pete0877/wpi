// CSL Specification of Spreadsheet components

component class adapt.spreadsheet.Spreadsheet extends class java.lang.Object {


  // Indexed Properties
  indexedProperty adapt.spreadsheet.Function Function (class java.lang.String);
  indexedProperty java.lang.String Value (class java.lang.String);
  indexedProperty java.lang.String Value (class java.lang.String);

  // Properties
  property adapt.ComponentAdapter Adapter;


  // Listeners
  listener SpreadsheetListener;  // Not part of interface

  Port adapt.Adaptable = {
    void setAdapter (class adapt.ComponentAdapter);
    class adapt.ComponentAdapter getAdapter ();
  }

  // Public Methods
  Port publicInterface = {
    void installFunctions ();
    class adapt.spreadsheet.Expression getExpression (class adapt.spreadsheet.Cell);
    float getNumericValue (class java.lang.String);
    float calculateFunction (class adapt.spreadsheet.Expression);
    void insertValue (class adapt.spreadsheet.Cell, class java.lang.String);
    void recalculate (class adapt.spreadsheet.Cell);
    void refresh (class adapt.spreadsheet.CellRegion);
  }

  // Private Methods
    private class java.util.Vector extractCells (class java.lang.String);
    private void modifyLevels (class adapt.spreadsheet.Node);
    private void updateLevels (class adapt.spreadsheet.Node);
    private class adapt.spreadsheet.Node getNode (class java.lang.String);
    private float evaluateConstant (class java.lang.String);
    private void evaluate (class adapt.spreadsheet.Node);
    private float calculateFunction (class java.lang.String, interface java.util.Enumeration);
    private void recalculateSpreadsheet (class adapt.spreadsheet.Node);
    private void markForRecalculation (class adapt.spreadsheet.Node);
    private void generateRefreshEvents ();
    private void handleSpreadsheetEvent (class adapt.spreadsheet.SpreadsheetEventObject);
    private void fireAction (class adapt.spreadsheet.SpreadsheetEventObject);

}
