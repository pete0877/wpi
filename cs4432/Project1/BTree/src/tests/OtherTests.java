/* FILE:        OtherTests.java
 * CLASS:       OtherTests
 * PACKAGE:     tests
 * VERSION:     1.0 
 * UPDATED:     4/20/00
 * AUTHOR:      Peter Golaszewski <peterg@wpi.edu>
 * NOTE:        I created this test file to help me debug my B+ tree.
 *              I found it more useful then the origianal test driver.
 */
 
package tests;

import java.io.*;
import java.util.*;
import java.lang.*;
import heap.*;
import bufmgr.*;
import diskmgr.*;
import global.*;
import btree.*;

public class OtherTests implements GlobalConst{

    public static void main(String [] argvs) {	
		System.out.print("\n");
		System.out.print("============> Creating database:\n");      
		// database settings:
 		String dbpath 	= "DATA.txt";
		SystemDefs sysdef = new SystemDefs(dbpath, 50, 50, "Clock");          
        BTreeFile btree;
        try {
            System.out.print("============> Printing DB page map:\n");
            SystemDefs.JavabaseDB.dump_space_map();            
            
			System.out.print("============> Creating integer B+ tree:\n");
            btree = new BTreeFile("myTree", AttrType.attrInteger, 4, 0);
            
            System.out.print("============> Adding 70 keys to the tree:\n");
			for (int a=1; a<=70; a++) btree.insert(new IntegerKey(a), new RID(new PageId(a), a));
					
            System.out.print("============> Printing the tree:\n");
			btree.print();
			
			System.out.print("============> Closing B+ tree:\n");
			btree.close();
			
			System.out.print("============> Reopening the B+ tree:\n");
			btree = new BTreeFile("myTree");
			
			System.out.print("============> Printing the tree:\n");
			btree.print();			
			
			System.out.print("============> Deleting keys 20 and 40:\n");
			btree.Delete(new IntegerKey(20), new RID(new PageId(20), 20));
			btree.Delete(new IntegerKey(40), new RID(new PageId(40), 40));
			
			System.out.print("============> Scanning keys 10 through 50:\n");
			IntegerKey lo = new IntegerKey (10);
			IntegerKey hi = new IntegerKey (50);
			BTFileScan scan = btree.new_scan(lo, hi);			
			KeyDataEntry scan_data = scan.get_next();
			System.out.print(" Keys: ");      
			while (scan_data!=null) {
                System.out.print(scan_data.key.toString() + ",");      
                scan_data = scan.get_next();
			}			
			scan.destroyBTreeFileScan();
			System.out.print("\n");      
			
			System.out.print("============> Deleting keys greater or equal to 60:\n");
			lo = new IntegerKey (60);			
			scan = btree.new_scan(lo, null);			
			try {
			    while (true) scan.delete_current();
			} catch (Exception dummy) {}			
			System.out.print("\n");   		
			
			System.out.print("============> Printing the tree:\n");
			btree.print();		
			
			System.out.print("============> Destroying the B+ tree:\n");
			btree.destroyFile();			
			
			System.out.print("============> Printing DB page map:\n");
            SystemDefs.JavabaseDB.dump_space_map();
			
			System.out.print("============> Done.\n");      	
			
        } catch (Exception e) {
			System.out.print("\n#########################################\n");      
            System.out.print("EXCEPTION: " + e.getMessage() + "\n");      
        }
    }  
}

