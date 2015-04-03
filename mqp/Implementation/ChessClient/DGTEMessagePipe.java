import java.util.Vector;
import java.util.Enumeration;

public class DGTEMessagePipe
{
	private class PendingMessage
	{
	    public DGTEMessage pm;
	    public MessagePipeConnection origin;
	    public MessagePipeConnection dest;
	    
	    PendingMessage(DGTEMessage m, MessagePipeConnection o, MessagePipeConnection d)
	    {
	        pm = m;
	        origin = o;
	        dest = d;
	    }
	}

	private Vector pendingMessages;
	private Vector messagePipeConnections;

	DGTEMessagePipe() 
	{
	    messagePipeConnections = new Vector();
	    pendingMessages = new Vector();
	}
	
	public void handleMessage(DGTEMessage m, MessagePipeConnection origin)
	{
		Vector targets;
		MessagePipeConnection mpc;

		synchronized (this)
		{
			targets = (Vector) messagePipeConnections.clone();
		}
		targets.removeElement(origin);

		for(Enumeration e=targets.elements(); e.hasMoreElements();)
			pendingMessages.addElement(new PendingMessage(m,origin,(MessagePipeConnection)e.nextElement()));

        while(!pendingMessages.isEmpty())
        {
    		PendingMessage pm = (PendingMessage)pendingMessages.elementAt(0);
            pendingMessages.removeElementAt(0);
		    pm.dest.handleMessage(pm.pm);
		}
	}

	public void addMessagePipeConnection(MessagePipeConnection mpc)
	{
		messagePipeConnections.addElement(mpc);
	}

	public void removeMessagePipeConnection(MessagePipeConnection mpc)
	{
		messagePipeConnections.removeElement(mpc);
	}
	//{{DECLARE_CONTROLS
	//}}
}
