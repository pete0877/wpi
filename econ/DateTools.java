//DateTools.java
import java.util.Date;					 
import java.lang.Integer;

public class DateTools
{
	public DateTools() {}		
	
	public String getMonthName(int number)
	{
		// Return month name as string (from value 0..11)
		if (number == 0) return ("January");
		if (number == 1) return ("February");
		if (number == 2) return ("March");
		if (number == 3) return ("April");
		if (number == 4) return ("May");
		if (number == 5) return ("June");
		if (number == 6) return ("July");
		if (number == 7) return ("August");
		if (number == 8) return ("September");
		if (number == 9) return ("October");
		if (number == 10) return ("November");
		if (number == 11) return ("December");		
		return ("");
	}
}
