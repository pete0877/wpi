// File    : EError.java
// Updated : 4/16/98
// Content : EError Class for ECON project
// Author  : Stephen Beaulieu
// Detail  : 
//  Used in communication between class interfaces to report 
//  action result. If Number is 0 then there is no error.
// Code    :

import java.io.Serializable;

//////////////////////////////////////////////////////////////////////////////
// Class EError

public class EError implements Serializable
{
	private int Number;          // Error number
	private String Description;  // Error description

	public EError()
	{
		Number = 0;
		Description = "";
	}
	
	public EError(int i1, String s1)
	{
		setEError(i1, s1);
	}

	public void setEError(int i1, String s1) 
	{
		Number = i1;
		Description = s1;
	}

	public int getEErrorNumber() 
	{ 
		return Number; 
	}
	
	public String getEErrorDescription() 
	{ 
		return Description; 
	}	
}

// End of code ///////////////////////////////////////////////////////////////