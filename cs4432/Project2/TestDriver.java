/* FILE:        TestDriver.java
 * CLASS:       TestDriver
 * PACKAGE:     
 * VERSION:     1.0 
 * UPDATED:     4/21/00
 * AUTHOR:      Peter Golaszewski <peterg@wpi.edu>
 */
 
import java.io.*;
import java.util.*;
import java.lang.*;
import TransactionManager.*;

public class TestDriver {

    // CONSTRUCTORS
    /***************************************************************************
    *   Default constructor
    */      
	public TestDriver () {}

    // METHODS
    /***************************************************************************
    *   The main driver procedure
    */          
    static public void main(String args[]) {
      
        System.out.println("\n____________________________________________________\n\n");
        System.out.println("Object Transaction Manager, v1.0");
        System.out.println("Peter Golaszewski, <peterg@wpi.edu>");
        System.out.println("WPI, CS 4432, D00, Project 2\n");
        TManager tmanager = new TManager();
        System.out.println("Type 'help' for list of commands");
        String userCommand = new String("");                        
        DataInputStream userInput = new DataInputStream(System.in);         
        while (!userCommand.equals("QUIT")) {
            // get user command
            System.out.print("\n > ");
            try {		                 
		        userCommand = userInput.readLine();	               
            } catch (Exception e) {
                System.out.println("\nEXCEPTION: Input stream error\n");
                userCommand = "QUIT";    
            }            
            userCommand = userCommand.toUpperCase();
            System.out.print("\n[" + userCommand + "]\n");
            if (userCommand.equals("QUIT")) {
                // user wants to quit the driver
                System.out.println("\n____________________________________________________\n\n");
            } else if (userCommand.equals("HELP")) {
                // user needs help
                System.out.println("\nCommands:");                                
                System.out.println("   help   - prints this help screen");                                
                System.out.println("   quit   - terminates the dirver");                                
                System.out.println("   print  - prints the current transaction table");                                
                System.out.println("   ST T   - start transaction T (integer)");                                
                System.out.println("   ET T   - end transaction T ");                                
                System.out.println("   SL T O - transaction T requests shared lock on object O (string)");                                
                System.out.println("   XL T O - transaction T requests exclusive lock on object O");                                                
                System.out.println("   UO T O - transaction T releases lock on object O");                                                                            
            } else if (userCommand.equals("PRINT")) {
                // print the transaction tree (for debug / grade purposes only)
                tmanager.printTTable();
            } else {    
                tmanager.parseStringCommand(userCommand);
                // other message .. must be redirected to the manager:
                String message = new String("");                
                while (message!=null) {
                    System.out.println(message);
                    message = tmanager.getMessageLine();
                }                    
            }
                
        }
        
    }
}

