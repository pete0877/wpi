import java.util.Vector;
import java.util.Enumeration;

public class DGTEMessage
{
	private Vector parts;

	DGTEMessage() {parts = new Vector(1);}
	public void setPart(String p) {parts.addElement(p);}
	public Enumeration getParts() {return parts.elements();}
	public String getType() {return (String)parts.elementAt(0);}
	public void clear() {parts.removeAllElements();}
}
