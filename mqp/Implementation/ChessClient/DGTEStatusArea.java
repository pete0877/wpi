import java.awt.*;
import java.util.Enumeration;

public class DGTEStatusArea extends java.awt.TextArea implements MessagePipeConnection
{
	public void addMessagePipeConnection(DGTEMessagePipe mp){}
	public void handleMessage(DGTEMessage m)
	{
	    String msg;
	    for(Enumeration e=m.getParts(); e.hasMoreElements();)
	    {
            msg = (String)e.nextElement();
            appendText(msg);
            appendText(" ");
	    }
	    appendText("\n");
	}
	public void pipeMessage(DGTEMessage m){}
}
