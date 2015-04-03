// File    : EConstants.java
// Updated : 4/5/98
// Content : Constants Class for ECON project
// Author  : Stephen Beaulieu
// Detail  : 
//  Stores constant values used in ECON Project
// Code    :

public class EConstants 
{
	private static final int PORT = 1998;  // Port number used in Server-Client Protocol
	private static final String CALENDAR = "calendar.dat"; // Filename used to save ECalendar
	private static final String SSINFO = "server.dat";     // Filename used to save EServerProfile
	private static final String SERVERS = "data\\servers.dat";   // Filename used to save EServers
	private static final String VERSION = "1.0";      // ECON System version number

	// getPORT()
	// Purpose: Returns Class attribute
	// Returns: PORT
	// References: None	
	public int getPORT() 
	{ 
		return PORT; 
	}
	
	// getCALENDAR()
	// Purpose: Returns Class attribute
	// Returns: CALENDAR
	// References: None	
	public String getCALENDAR() 
	{ 
		return CALENDAR; 
	}
	
	// getSSINFO()
	// Purpose: Returns Class attribute
	// Returns: SSINFO
	// References: None	
	public String getSSINFO() 
	{ 
		return SSINFO; 
	}
	
	// getSERVERS()
	// Purpose: Returns Class attribute
	// Returns: SERVERS
	// References: None	
	public String getSERVERS() 
	{ 
		return SERVERS; 
	}
	
	// getVERSION()
	// Purpose: Returns Class attribute
	// Returns: VERSION
	// References: None	
	public String getVERSION() 
	{ 
		return VERSION; 
	}

}

// End of Code
