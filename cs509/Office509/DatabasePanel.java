import java.awt.*;
import java.beans.*;
import java.util.*;

import database.*;
import gui.*;

public class DatabasePanel extends java.awt.Panel
{
    protected DatabaseInterface dbi = null;
    protected Frame1 frame1 = null;
    protected int numRows = 0;
    protected int numColumns = 0;
    protected int topRow = 1;
    protected int leftColumn = 1;
    protected gui.TableBean tb, tbR, tbC;
    protected int maxTuple = 0;
    
	public DatabasePanel(DatabaseInterface dbi, Frame1 frame)
	{
		super();
		//{{INIT_CONTROLS
		setLayout(null);
		setSize(540,330);
		topButton = new java.awt.Button();
		bottomButton = new java.awt.Button();
		
		upButton = new java.awt.Button();
		upButton.setLabel("^");
		upButton.setBounds(384,84,24,24);
		upButton.setBackground(new Color(12632256));
		add(upButton);
		downButton = new java.awt.Button();
		downButton.setLabel("v");
		downButton.setBounds(384,132,24,24);
		downButton.setBackground(new Color(12632256));
		add(downButton);
		insertButton = new java.awt.Button();
		insertButton.setLabel("insert");
		insertButton.setBounds(12,240,48,24);
		insertButton.setBackground(new Color(12632256));
		add(insertButton);
		deleteButton = new java.awt.Button();
		deleteButton.setLabel("delete");
		deleteButton.setBounds(72,240,48,24);
		deleteButton.setBackground(new Color(12632256));
		add(deleteButton);		

		filterButton = new java.awt.Button();
		filterButton.setLabel("filter");
		filterButton.setBounds(132,240,48,24);
		filterButton.setBackground(new Color(12632256));
		add(filterButton);
		
		newSchemaButton = new java.awt.Button();
		newSchemaButton.setLabel("new schema");
		newSchemaButton.setBounds(192,240,84,24);
		newSchemaButton.setBackground(new Color(12632256));
		add(newSchemaButton);
		clearAllButton = new java.awt.Button();
		clearAllButton.setLabel("clear all");
		clearAllButton.setBounds(288,240,60,24);
		clearAllButton.setBackground(new Color(12632256));
		add(clearAllButton);
		schemaField = new java.awt.TextField();
		schemaField.setBounds(192,276,156,32);
		add(schemaField);
		label1 = new java.awt.Label("Field specification:");
		label1.setBounds(72,276,108,12);
		label1.setFont(new Font("Dialog", Font.ITALIC, 10));
		add(label1);
		
        label2 = new java.awt.Label("NOTE: Read Doc/readme.txt for syntax definition!");
		label2.setBounds(10,290,300,60);
		label2.setFont(new Font("Dialog", Font.BOLD, 12));
		add(label2);		
	
		outputArea = new java.awt.TextArea();
		outputArea.setBounds(12,12,336,216);
		add(outputArea);
		leftButton = new java.awt.Button();
		leftButton.setLabel("<");
		leftButton.setBounds(360,108,24,24);
		leftButton.setBackground(new Color(12632256));
		add(leftButton);
		rightButton = new java.awt.Button();
		rightButton.setLabel(">");
		rightButton.setBounds(408,108,24,24);
		rightButton.setBackground(new Color(12632256));
		add(rightButton);
		//}}

        this.dbi = dbi;
		//{{REGISTER_LISTENERS
		SymAction lSymAction = new SymAction();
		topButton.addActionListener(lSymAction);
		upButton.addActionListener(lSymAction);
		downButton.addActionListener(lSymAction);
		bottomButton.addActionListener(lSymAction);
		insertButton.addActionListener(lSymAction);
		deleteButton.addActionListener(lSymAction);
		newSchemaButton.addActionListener(lSymAction);
		clearAllButton.addActionListener(lSymAction);
		leftButton.addActionListener(lSymAction);
		rightButton.addActionListener(lSymAction);
		
		filterButton.addActionListener(lSymAction);
		//}}
    	
    	this.dbi = dbi;
    	this.frame1 = frame;
    	
        // put in the TableBeans
        numRows = 15;
        numColumns = 6;
        outputArea.setVisible(false);
		tbR = new gui.TableBean (1,numRows);
		tbR.setColumnWidth(48);
		tbR.setBackground(Color.darkGray);
		tbR.setForeground(Color.white);
		tbR.setLocation(2,14);
		add(tbR);
		
		tbC = new gui.TableBean (numColumns, 1);
		tbC.setColumnWidth(48);
		tbC.setBackground(Color.darkGray);
		tbC.setForeground(Color.white);
		tbC.setLocation (52, 0);
		add(tbC);

		tb = new gui.TableBean (numColumns, numRows);
		tb.setColumnWidth(48);
		tb.setLocation (52, 14);
		add(tb);
		gui.TableElement te = new gui.TableElement(1,1);
		
		// Output into the output area
		updateColumns();
		updateRows();
    	updateDatabaseInformation();

		tb.doLayout();
	}

	//{{DECLARE_CONTROLS
	java.awt.Button topButton;
	java.awt.Button bottomButton;
	java.awt.Button upButton;
	java.awt.Button downButton;
	java.awt.Button insertButton;
	java.awt.Button deleteButton;
	java.awt.Button newSchemaButton;
	java.awt.Button clearAllButton;
	java.awt.TextField schemaField;
	java.awt.Label label1;
	java.awt.Label label2;
	java.awt.Label label3;
	java.awt.Label label4;
	java.awt.Label label5;
	java.awt.Label label6;
	java.awt.Label label7;
	java.awt.Label label8;
	java.awt.TextArea outputArea;
	java.awt.Button leftButton;
	java.awt.Button rightButton;
	
	java.awt.Button filterButton;
	//}}


	class SymAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == topButton)
				topButton_ActionPerformed(event);
			else if (object == upButton)
				upButton_ActionPerformed(event);
			else if (object == downButton)
				downButton_ActionPerformed(event);
			else if (object == bottomButton)
				bottomButton_ActionPerformed(event);
			else if (object == insertButton)
				insertButton_ActionPerformed(event);
			else if (object == deleteButton)
				deleteButton_ActionPerformed(event);
			else if (object == newSchemaButton)
				newSchemaButton_ActionPerformed(event);
			else if (object == clearAllButton)
				clearAllButton_ActionPerformed(event);
			else if (object == leftButton)
				leftButton_ActionPerformed(event);
			else if (object == rightButton)
				rightButton_ActionPerformed(event);
			else if (object == filterButton)
				filterButton_ActionPerformed(event);
		}
	}
	

	void filterButton_ActionPerformed(java.awt.event.ActionEvent event)
	{
        String s = schemaField.getText();
		schemaField.setText("");		
		Vector query = parseSpecs(s);		
		if (query!=null) {		
            Enumeration en = null;
            try {
			    en = dbi.query(query);
         	} catch (DatabaseException dbe) {         	    
         	    System.err.println ("unable to filter for records:" + dbe.getMessage());         	
		        return;		        
         	}
         	if (en == null) {
         	    System.err.println ("unable to filter for records.");
		        return;		    
		    }
    	    tbR.clearTableValue();
            tb.clearTableValue();   
            int i = 1;
            for (; en.hasMoreElements(); ) {
    		    RecordInterface ri = (RecordInterface) en.nextElement();
    		    if (i > topRow + numRows) break;
    		    if (i >= topRow) {
    		        tbR.setTableValue (1, i-topRow+1, Integer.toString (i));
    		        updateSingleRow (i-topRow+1, ri);
    		    }
    		    i++;
            }
		    tbR.repaint();
		    tb.repaint();
		} else {
		    updateRows();		    
		}		
	}

	void topButton_ActionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		//{{CONNECTION
		// Set the cursor... HAND_CURSOR
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		//}}
	}

	void upButton_ActionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		//{{CONNECTION
		if (topRow == 1) return;
		topRow--;
		updateRows();
		updateDatabaseInformation();
		//}}
	}

	void downButton_ActionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		//{{CONNECTION
		if (topRow + numRows > maxTuple) return;
		topRow++;
		updateRows();
		updateDatabaseInformation();
		//}}
	}

	void bottomButton_ActionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		//{{CONNECTION
		// Set the cursor... HAND_CURSOR
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		//}}
	}

	void insertButton_ActionPerformed(java.awt.event.ActionEvent event)
	{
        String s = schemaField.getText();
		schemaField.setText("");		
		Vector query = parseSpecs(s);		
		if (query==null) {
		    System.err.println ("You have to specify the primary key value.");
		    return;
        }
		
		try {
			FreeRecord fr = dbi.getTemplate();
			String PKN = fr.getPrimaryField().getName();
			Enumeration defFields = fr.getFields();
			Vector result = new Vector();
			while (defFields.hasMoreElements()) {
			    Field defField = (Field) defFields.nextElement();
			    Enumeration insFields = query.elements();
			    while (insFields.hasMoreElements()) {
			        Field insField = (Field) insFields.nextElement();   
			        if (insField.getName().equals(defField.getName())) { defField.setValue(insField.getValue()); }
			    }
			    result.addElement(defField);
			}
		    fr = new FreeRecord(result, PKN);
    		dbi.addRecord (fr);
    		maxTuple++;
    		// always adjust to show on screen last one added.
    		if (maxTuple > topRow + numRows) {
    		    topRow = maxTuple - numRows + 1;
    		}
    		updateSingleRow(maxTuple, fr);
    		updateRows();
    	} catch (DatabaseException dbe) {
    	    System.err.println ("Unable to add Record: " + dbe.getMessage());
    	} catch (Exception e) {
			System.err.println ("Unable to add Record: " + e.getMessage());
		}
		
		// anytime an object is updated, must call this
		frame1.updateObjectState();
		//}}
	}

	void deleteButton_ActionPerformed(java.awt.event.ActionEvent event)
	{
        String s = schemaField.getText();
		schemaField.setText("");		
		Vector query = parseSpecs(s);		
		if (query!=null) {		
            Enumeration en = null;
            try {
			    en = dbi.query(query);
         	} catch (DatabaseException dbe) {         	    
         	    System.err.println ("unable to filter for records:" + dbe.getMessage());         	
		        return;		        
         	}
         	if (en == null) {
         	    System.err.println ("unable to filter for records.");
		        return;		    
		    }    	    
		    tbR.clearTableValue();
            tb.clearTableValue();            
            for (; en.hasMoreElements(); ) {
    		    RecordInterface ri = (RecordInterface) en.nextElement();
    		    try {
			        dbi.deleteRecord(ri);
         	    } catch (DatabaseException dbe) {         
         	        System.err.println ("unable to delete a record:" + dbe.getMessage());         	         	                            
		            updateRows();
    	            updateDatabaseInformation();    	
		            frame1.updateObjectState();	
		            return;		    
         	    }    		    
            }
            tbR.repaint();
            tb.repaint();
            updateRows();
            updateDatabaseInformation();    	
		    frame1.updateObjectState();	
		} 	
    }

	void newSchemaButton_ActionPerformed(java.awt.event.ActionEvent event)
	{
        String s = schemaField.getText();
		schemaField.setText("");		
		Vector fields = parseSpecs(s);		
		if ((fields==null) || (fields.size()<1)) {
		    System.err.println ("You have to specify at least one field.");
		    return;
        }
        Field f = (Field) fields.elementAt(0);
		try {
    		FreeRecord fr = new FreeRecord(fields, f.getName());
    		dbi.setTemplate (fr);
    		maxTuple = 0;
    		updateColumns();
    		updateDatabaseInformation();
    		frame1.updateObjectState();
    	} catch (DatabaseException dbe) {
    	    System.err.println ("unable to create schema: " + dbe.getMessage());
    	}
	}

	void clearAllButton_ActionPerformed(java.awt.event.ActionEvent event)
	{		
        dbi = new database.Database();		
        maxTuple = 0;
        updateColumns();
		updateRows();
    	updateDatabaseInformation();    	
		frame1.updateObjectState();	
		
	}
	
	protected void updateColumns () {		
	    if (dbi == null) {
	        tbC.clearTableValue();
	        tbC.repaint();
	        return;
	    }
	    FreeRecord fr = dbi.getTemplate();        
	    if (fr == null) {
	        tbC.clearTableValue();
            tbC.repaint();
	        return;
        }                
		int i = 1;
        for (Enumeration en = fr.getFields(); en.hasMoreElements(); ) {
            Field f = (Field) en.nextElement();
            if (i >= leftColumn) {
                if (i <= leftColumn + numColumns) {
                    tbC.setTableValue (i-leftColumn+1, 1, f.getName());
                }
            }
            i++;
        }
        // tbC.repaint();
	}
	
    protected void updateSingleRow (int r, RecordInterface ri) {
        int i = 1;
        for (Enumeration en = ri.getFields (); en.hasMoreElements(); ) {
            Field f = (Field) en.nextElement();
            if (i >= leftColumn) {
                if (i <= leftColumn + numColumns) {
                    tb.setTableValue (i - leftColumn + 1, r, f.getValue());
                }
            }
            i++;
        }
    }
	
	
	protected void updateRows() {
	    maxTuple = 0;
        if (dbi == null) {
            tbR.clearTableValue();
            tb.clearTableValue();
            tbR.repaint();
            tb.repaint();
            return;
        }
	    FreeRecord fr = dbi.getTemplate();        
	    if (fr == null) {
	        tbR.clearTableValue();
            tb.clearTableValue();
            tbR.repaint();
            tb.repaint();
            return;
        }        
	    
	    int i = 1;
	    try {
		Enumeration en = dbi.query(new Vector());
		if (en == null) return;
    		for (; en.hasMoreElements(); ) {
    		    maxTuple++;
    		    RecordInterface ri = (RecordInterface) en.nextElement();
    		    if (i > topRow + numRows) break;
    		    if (i >= topRow) {
    		        tbR.setTableValue (1, i-topRow+1, Integer.toString (i));
    		        updateSingleRow (i-topRow+1, ri);
    		    }
    		    i++;
            }
    	} catch (DatabaseException dbe) {
    	    tbR.clearTableValue();
            tb.clearTableValue();
            tbR.repaint();
            tb.repaint();
    	    System.err.println ("Unable to access the Database:" + dbe.getMessage());
    	}
        maxTuple = 0;
	}
	    
	protected void updateDatabaseInformation () {
	    if (dbi == null) return;
		int ct = 0;
		try {
		    Enumeration en = dbi.query((Field) null);
		    if (en == null) return;
		    for ( ;en.hasMoreElements(); ) {
    		    ct++;
    		    en.nextElement();
    		}
    		//outputArea.append (ct + " objects.\n");
    		maxTuple = ct;
    	} catch (DatabaseException dbe) {
    	    System.err.println ("DB-error: " + dbe.getMessage());
    	}
	}

	void leftButton_ActionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		//{{CONNECTION
		if (leftColumn == 1) return;
		leftColumn--;
		updateColumns();
		updateRows();
		updateDatabaseInformation();
		//}}
	}

	void rightButton_ActionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		//{{CONNECTION
		FreeRecord fr = dbi.getTemplate();
		if (fr == null) return;
        if (leftColumn + numColumns <= countFields (fr)) {
            leftColumn++;
    		updateColumns();
    		updateRows();    		
    		updateDatabaseInformation();
    	}
		//}}
	}
	
	protected int countFields (FreeRecord fr) {
		int ct = 0;
		for (Enumeration en = fr.getFields(); en.hasMoreElements(); ) {
		    ct++;
		    en.nextElement();
		}
		return ct;
	}
	
	protected Vector parseSpecs(String s) {
        // parses following syntax
        //   fieldname1|fieldvalue1, fieldname2|fieldvalue2, fieldname3|fieldvalue3, ...
        // into a vector of fields:        
        Vector query = new Vector ();
        if ((s==null) || (s.trim().equals(""))) { return null; }
        
        StringTokenizer st = new StringTokenizer (s, ",", false);
        if (st.countTokens()<1) { return null; }
        while (st.hasMoreElements()) {
            Field tmpField = new Field();
            String criteria = (String) st.nextElement();
            criteria = " " + criteria + " ";
            StringTokenizer fieldSpec = new StringTokenizer (criteria, "|", false);
            if (fieldSpec.countTokens()<2) { return null; }
            String field = (String) fieldSpec.nextElement();
		    String value = (String) fieldSpec.nextElement();
		    field = field.trim();
		    value = value.trim();
            if ((field==null) || (value==null) || field.equals("")) { return null; }
		    tmpField.init(field, value);
            query.addElement(tmpField);
        }   
        return query;
	}	
}
