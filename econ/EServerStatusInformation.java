// File    : EServerStatusInformation.java
// Updated : 4/16/98
// Content : EServerStatusInformation Class for ECON project
// Author  : Carl Nielsen
// Detail  : Stores information about the server.
// Code    :

import java.io.Serializable;

//////////////////////////////////////////////////////////////////////////////
// Class EServerStatusInformation

public class EServerStatusInformation implements Serializable
{
	private String Description;
	private String Comments;
	private String AdminName;
	private String AdminEMail;
	private String AdminTelNumber;
	private String AdminPassword;
	private String DNSname;

	public EServerStatusInformation()
	{
		Description = "";
		Comments = "";
		AdminName = "";
		AdminEMail = "";
		AdminTelNumber = "";
		AdminPassword = "";
		DNSname = "";
	}
							
	public EServerStatusInformation(String s1, String s2, String s3, String s4, String s5, String s6, String s7)
	{
		Description = new String(s1);
		Comments = new String(s2);
		AdminName = new String(s3);
		AdminEMail = new String(s4);
		AdminTelNumber = new String(s5);
		AdminPassword = new String(s6);
		DNSname = new String(s7);
	}

 	public void setDescription(String s)
	{
		Description = new String(s);
	}

	public void setComments(String s)
	{
		Comments = new String(s);
	}

	public void setAdminName(String s)
	{
		AdminName = new String(s);
	}

	public void setAdminEMail(String s)
	{
		AdminEMail = new String(s);
	}

	public void setAdminTelNumber(String s)
	{
		AdminTelNumber = new String(s);
	}

	public void setAdminPassword(String s)
	{
		AdminPassword = new String(s);
	}

	public void setDNSname(String s)
	{
		DNSname = new String(s);
	}

	public String getDescription()
	{
		return Description;
	}

	public String getComments()
	{
		return Comments;
	}

	public String getAdminName()
	{
		return AdminName;
	}

	public String getAdminEMail()
	{
		return AdminEMail;
	}

	public String getAdminTelNumber()
	{
		return AdminTelNumber;
	}

	public String getAdminPassword()
	{
		return AdminPassword;
	}

	public String getDNSname()
	{
		return DNSname;
	}
}

// End of code ///////////////////////////////////////////////////////////////