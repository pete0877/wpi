// File    : EServerList.java
// Updated : 4/13/98
// Content : EServerList Class for ECON project
// Author  : Peter Golaszewski
// Detail  : Stores list of registered ECON servers available to the client.
//			 Also stores reference to the current server.
// Code    :

// Imported classes:
import java.util.Vector;					   
import java.util.Enumeration;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.Integer;						

public class EServerList
{
	private Vector Servers = new Vector();	// List of registered servers
	private int CurrentServerIndex;			// index of the current server
	private boolean Changed = false;	

	public void setChanged (boolean b0)
	{		
		Changed = b0;
	}

	public boolean wasChanged ()
	{		
		return Changed;
	}
	
	// public int getSize ()
	// Purpose: Returns class attribute.
	// Returns: number of servers registered with the Client
	// References: None	
	public int getSize ()
	{
		return Servers.size();
	}
	
	// public int getCurrentServerIndex ()
	// Purpose: Returns class attribute.
	// Returns: CurrentServerIndex
	// References: None
	public int getCurrentServerIndex ()
	{
		return CurrentServerIndex;		
	}

	// public void setCurrentServerIndex (int i1)
	// Purpose: Sets CurrentServerIndex to i1
	// Returns: Nothing
	// References: None
	public void setCurrentServerIndex (int i1)
	{
		CurrentServerIndex = i1;
	}	

	// public void addServer(EServerProfile s1)
	// Purpose: Appends s1 to Servers
	// Returns: Nothing
	// References: EServerProfile
	public void addServer(EServerProfile s1)
	{
		Servers.addElement (s1);
	}
	
	// public EServerProfile getServer(int i1)
	// Purpose: Returns i1'th EServerProfile from the Servers list
	// Returns: Nothing
	// References: EServerProfile
	public EServerProfile getServer(int i1)
	{
		EServerProfile s1 = (EServerProfile) Servers.elementAt(i1);	
		return s1;
	}
	
	// public void deleteServer(int i1)
	// Purpose: Returns i1'th EServerProfile from the Servers list
	// Returns: Nothing
	// References: EServerProfile
	public void deleteServer(int i1)
	{
			Servers.removeElementAt (i1);
	}	
	
	// public void load(String s1) 
	// Purpose: Loads class attributes from local file with s1 filename
	// Returns: Nothing
	// References: EConstants
	public void load(String s1) 
	{
		Servers = new Vector();
		try {
			Integer i = new Integer(0);						
			FileInputStream istream = new FileInputStream(s1);
			ObjectInputStream p = new ObjectInputStream(istream);
			Servers = (Vector) p.readObject();
			i = (Integer) p.readObject();				
			this.CurrentServerIndex= i.intValue();				
			istream.close(); 
		}
		catch (IOException e)
		{
			//Error: Could not open file - ow well
		}
		catch (ClassNotFoundException ex)
		{
			//Error: Could not read file - ow well
		}
	}

	// public void save(String s1) 
	// Purpose: Saves class attributes to local file with s1 filename
	// Returns: Nothing
	// References: EConstants
	public void save(String s1) 	
	{		
		try {
			Integer i = new Integer(this.CurrentServerIndex);			
			FileOutputStream ostream = new FileOutputStream(s1);
			ObjectOutputStream p = new ObjectOutputStream(ostream);
			p.writeObject(Servers);
			p.writeObject(i);			
			ostream.close(); 
		}
		catch (IOException e)
		{
			//Error: Could not save server list - ow well
		}			
		
	}
	
}

// End of Code