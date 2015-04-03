////////////////////////////////////////////////////////////////////////////
// Class:       Driver
// Package:     N/A
// Author:      Peter Golaszewski
// Date:        12/12/99
// Description: Used to test the database component

import database.*;
import java.io.*;
import java.awt.*;
import java.util.*;

class Driver {    
    public static void main (String[] args) {
        System.out.print("\nTEST CASES FOR THE DATABASE COMPONENT:");
        try {            
            
            // Creating new FreeRecord:
            System.out.print("\n\n Creating new FreeRecord:");
            Vector v = new Vector();
            database.Field f;
            f = new database.Field();
            f.init("ID", "");
            v.addElement(f);            
            f = new database.Field();
            f.init("First Name", "");
            v.addElement(f);
            f = new database.Field();
            f.init("SN", "000-00-0000");
            v.addElement(f);
            database.FreeRecord fr = new database.FreeRecord (v,"ID");            
            System.out.print("\n  result: {" + fr.asString() + "}");         
            
            // Create new database object from the FreeRecord:
            System.out.print("\n\n Create new database object from the FreeRecord:");
            database.Database DBC = new database.Database(fr);            
            System.out.print("\n  result: {" + DBC.asString() + "}");         
            
            // Get record template:
            System.out.print("\n\n Get record template:");
            database.FreeRecord template = DBC.getTemplate();
            System.out.print("\n  result: {" + template.asString() + "}");    
            
            // Adding new record:
            System.out.print("\n\n Adding new record:");
            v = new Vector();            
            f = new database.Field();
            f.init("ID", "1");
            v.addElement(f);            
            f = new database.Field();
            f.init("First Name", "Peter");
            v.addElement(f);
            f = new database.Field();
            f.init("SN", "123-45-6789");
            v.addElement(f);
            fr = new database.FreeRecord (v,"ID");            
            DBC.addRecord(fr);
            System.out.print("\n  result: {" + DBC.asString() + "}");         

            // Adding new record:
            System.out.print("\n\n Adding new record:");
            v = new Vector();            
            f = new database.Field();
            f.init("ID", "2");
            v.addElement(f);            
            f = new database.Field();
            f.init("First Name", "Peter");
            v.addElement(f);
            f = new database.Field();
            f.init("SN", "111-22-3333");
            v.addElement(f);
            fr = new database.FreeRecord (v,"ID");            
            DBC.addRecord(fr);
            System.out.print("\n  result: {" + DBC.asString() + "}");              
            
            // Adding new record:
            System.out.print("\n\n Adding new record:");
            v = new Vector();            
            f = new database.Field();
            f.init("ID", "3");
            v.addElement(f);            
            f = new database.Field();
            f.init("First Name", "Mark");
            v.addElement(f);
            f = new database.Field();
            f.init("SN", "999-88-7777");
            v.addElement(f);
            fr = new database.FreeRecord (v,"ID");            
            DBC.addRecord(fr);
            System.out.print("\n  result: {" + DBC.asString() + "}");              
            
            // Adding new record:
            System.out.print("\n\n Adding new record:");
            v = new Vector();            
            f = new database.Field();
            f.init("ID", "4");
            v.addElement(f);            
            f = new database.Field();
            f.init("First Name", "John");
            v.addElement(f);
            f = new database.Field();
            f.init("SN", "999-88-7777");
            v.addElement(f);
            fr = new database.FreeRecord (v,"ID");            
            DBC.addRecord(fr);
            System.out.print("\n  result: {" + DBC.asString() + "}");          
            
            // Fetching all Peter records:
            System.out.print("\n\n Fetching all Peter records:");            
            f = new database.Field();
            f.init("First Name", "Peter");            
            Enumeration i = DBC.query(f);            
            System.out.print("\n  result: {");
            database.FreeRecord tmpRecord;
            while (i.hasMoreElements()) {
                tmpRecord = (database.FreeRecord) i.nextElement();
                System.out.print("\nRETURNED RECORD: " + tmpRecord.asString());
            }
            System.out.print("}");          

            // Fetching all Peter records with ID of 2:
            System.out.print("\n\n Fetching all Peter records with ID of 2:");            
            f = new database.Field();
            f.init("First Name", "Peter");            
            database.Field f2 = new database.Field();
            f2.init("ID", "2");            
            v = new Vector();
            v.addElement(f);
            v.addElement(f2);            
            i = DBC.query(v);            
            System.out.print("\n  result: {");
            while (i.hasMoreElements()) {
                tmpRecord = (database.FreeRecord) i.nextElement();
                System.out.print("\nRETURNED RECORD: " + tmpRecord.asString());
            }
            System.out.print("}");       
            
            // Serializing DBC instance:
            System.out.print("\n\n Serializing DBC instance:");            
            Vector tmpState = (Vector) DBC.getState();            
            System.out.print("\n  result: DONE");              
            
            // Creating empty DBC and setting the template:
            System.out.print("\n\n Creating empty DBC and setting the template:");            
            v = new Vector();            
            f = new database.Field();
            f.init("SOMEID", "");
            v.addElement(f);            
            template = new database.FreeRecord(v, "SOMEID");            
            DBC = new database.Database();            
            DBC.setTemplate(template);
            System.out.print("\n  result: {" + DBC.asString() + "}");         
            
            // Bringing back serialized DBC state:
            System.out.print("\n\n Bringing back serialized DBC state:");                        
            DBC = new database.Database(tmpState);            
            System.out.print("\n  result: {" + DBC.asString() + "}");                 
            
            // Fetching all Peter records with ID of 1:
            System.out.print("\n\n Fetching all Peter records with ID of 1:");            
            f = new database.Field();
            f.init("First Name", "Peter");            
            f2 = new database.Field();
            f2.init("ID", "1");            
            v = new Vector();
            v.addElement(f);
            v.addElement(f2);            
            i = DBC.query(v);            
            System.out.print("\n  result: {");            
            tmpRecord = null;
            while (i.hasMoreElements()) {
                tmpRecord = (database.FreeRecord) i.nextElement();
                System.out.print("\nRETURNED RECORD: " + tmpRecord.asString());
            }
            System.out.print("}");          
            
            // Updating the SN of the obtained record:
            System.out.print("\n\n Updating the SN of the obtained record:");            
            f = new database.Field();
            f.init("SN", "999-99-9999");  
            DBC.updateRecord(tmpRecord, f);
            System.out.print("\n  result: {" + DBC.asString() + "}"); 

            // Fetching all Mark records with ID of 3:
            System.out.print("\n\n Fetching all Mark records with ID of 3:");            
            f = new database.Field();
            f.init("First Name", "Mark");            
            f2 = new database.Field();
            f2.init("ID", "3");            
            v = new Vector();
            v.addElement(f);
            v.addElement(f2);            
            i = DBC.query(v);            
            System.out.print("\n  result: {");            
            tmpRecord = null;
            while (i.hasMoreElements()) {
                tmpRecord = (database.FreeRecord) i.nextElement();
                System.out.print("\nRETURNED RECORD: " + tmpRecord.asString());
            }
            System.out.print("}");   

            // Update the Name and SN of the obtained record:
            System.out.print("\n\n Update the Name and SN of the obtained record:");            
            f = new database.Field();
            f.init("SN", "###-##-####");  
            f2 = new database.Field();
            f2.init("First Name", "---------");  
            v = new Vector ();
            v.addElement(f);
            v.addElement(f2);
            DBC.updateRecord(tmpRecord, v);
            System.out.print("\n  result: {" + DBC.asString() + "}");             
            
            // Fetch the record with ID of 3:
            System.out.print("\n\n Fetch the record with ID of 3:");            
            f = new database.Field();
            f.init("ID", "3");                        
            i = DBC.query(f);            
            System.out.print("\n  result: {");            
            tmpRecord = null;
            while (i.hasMoreElements()) {
                tmpRecord = (database.FreeRecord) i.nextElement();
                System.out.print("\nRETURNED RECORD: " + tmpRecord.asString());
            }
            System.out.print("}");   
            
            // Delete the obtained record:
            System.out.print("\n\n Delete the obtained record:");                        
            DBC.deleteRecord(tmpRecord);
            System.out.print("\n  result: {" + DBC.asString() + "}");               

            // Delete all Peters:
            System.out.print("\n\n Delete all Peters:");                        
            f = new database.Field();
            f.init("First Name", "Peter");              
            DBC.deleteRecord(f);
            System.out.print("\n  result: {" + DBC.asString() + "}");                             
                         
            // Adding new record:
            System.out.print("\n\n Adding new record:");
            v = new Vector();            
            f = new database.Field();
            f.init("ID", "5");
            v.addElement(f);            
            f = new database.Field();
            f.init("First Name", "John");
            v.addElement(f);
            f = new database.Field();
            f.init("SN", "999-88-7777");
            v.addElement(f);
            fr = new database.FreeRecord (v,"ID");            
            DBC.addRecord(fr);
            System.out.print("\n  result: {" + DBC.asString() + "}");             

            // Adding new record:
            System.out.print("\n\n Adding new record:");
            v = new Vector();            
            f = new database.Field();
            f.init("ID", "6");
            v.addElement(f);            
            f = new database.Field();
            f.init("First Name", "Dude");
            v.addElement(f);
            f = new database.Field();
            f.init("SN", "999-88-7777");
            v.addElement(f);
            fr = new database.FreeRecord (v,"ID");            
            DBC.addRecord(fr);
            System.out.print("\n  result: {" + DBC.asString() + "}");   

            // Delete all Johns with 999-88-7777 SN:
            System.out.print("\n\n Delete all Peters:");                        
            f = new database.Field();
            f.init("First Name", "Peter");              
            v = new Vector();
            v.addElement(f);
            DBC.deleteRecord(v);
            System.out.print("\n  result: {" + DBC.asString() + "}");                             

            // Delete all records:
            System.out.print("\n\n Delete all records:");                        
            DBC.deleteRecord(new Vector());
            System.out.print("\n  result: {" + DBC.asString() + "}");     
            
            
        } catch (database.DatabaseException e) {
            System.out.print("\n           Exception caught: " + e.getMessage());    
        }           
        System.out.print("\n\nDONE WITH ALL TEST CASES.");
    }  
}

// END OF FILE
////////////////////////////////////////////////////////////////////////////