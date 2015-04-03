/*
	A basic Java class stub for a Win32 Console Application.
 */

import java.net.*;
import java.io.*;


public class Primes {

	public Primes () {}

    static String readString(DataInputStream in) {
  
        int messageIndex = 0;
		char[] message = new char[4096];
		String result = new String("");
		try
		{
			do {
                message[messageIndex] = (char)in.readByte();
            } while(((int)message[messageIndex] != 0) && (++messageIndex < 4095));
			result = new String(message, 0, messageIndex);   
		} catch (Exception e) {}
		return result;
     }

    static public void main(String args[]) {
        try {
            System.out.println("");
            OutputStream    outStr;
            DataInputStream inStr;
            String          s;
            Socket          socket;
        
            System.out.print("\n\nEnter prime numbers server: ");
            DataInputStream keys    = new DataInputStream(System.in);                        
                
            String ard = "";
            try {		            
		        ard = keys.readLine();	               
            } catch (Exception e) {}
        
            socket = new Socket(ard, 23);
            outStr  = socket.getOutputStream();
            inStr   = new DataInputStream(socket.getInputStream());            
            
            
            s = readString(inStr);
            System.out.println(s);
            s = readString(inStr);
            System.out.println(s);
            
            s = "CLIENT_ENCODING";        
            outStr.write(s.getBytes());
            outStr.write(0);
            
            s = "ASCII";        
            outStr.write(s.getBytes());
            outStr.write(0);
            
            s = readString(inStr);
            System.out.println(s);
            s = readString(inStr);
            System.out.println(s);            
            s = readString(inStr);
            System.out.println(s);
            s = readString(inStr);
            System.out.println(s);
            s = readString(inStr);
            System.out.println(s);
            
            s = "CLIENT_GAMETYPE";        
            outStr.write(s.getBytes());
            outStr.write(0);            
            
            s = "0";        
            outStr.write(s.getBytes());
            outStr.write(0);            
            
            s = "CLIENT_PLY";        
            outStr.write(s.getBytes());
            outStr.write(0);
            
            s = "4";        
            outStr.write(s.getBytes());
            outStr.write(0);
            
            s = readString(inStr);
            System.out.println(s);
            
            int number=1;
            byte[] message = new byte[4096];            
            String result = new String("");
            
            String stmp = "";
            while (!stmp.equals("0")) {           
                
                System.out.print("\n\nEnter number to be checked (0=quit): ");
                
                stmp = "";
		        try {		            
		            stmp = keys.readLine();	               
		        } catch (Exception e) {}
		        
		        if (!stmp.equals("0")) {
		        
		            result = stmp;
                
                    s = "CLIENT_GAMESTATE";        
                    outStr.write(s.getBytes());
                    outStr.write(0);
                    
                    s = result;
                    outStr.write(s.getBytes());
                    outStr.write(0);   
                
                    s = readString(inStr);
                    System.out.println(s);
                    s = readString(inStr);
                    System.out.println(s);            
                    s = readString(inStr);
                    System.out.println(s);
                    s = readString(inStr);
                    System.out.println(s);
                    s = readString(inStr);
                    System.out.println(s);
                    s = readString(inStr);
                    System.out.println(s);
                    s = readString(inStr);
                    System.out.println(s);
     
                }          

            }
           
            socket.close();
        } catch (Exception e) {
        }
        
    }
    


}

