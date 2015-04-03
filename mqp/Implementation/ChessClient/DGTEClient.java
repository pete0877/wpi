import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.net.*;
import java.io.*;

public class DGTEClient implements MessagePipeConnection
{
	private DGTEServerMessageListener sml;
	private Vector messagePipes;
	private Socket soc;
	private String host;
	private int port;
	private DGTEClientServerProtocol csp;

	DGTEClient(DGTEClientServerProtocol p) 
	{
	    csp = p;
	    messagePipes = new Vector();
	    sml = new DGTEServerMessageListener(csp);
	}

	public boolean connect(String h, int p)
	{
	    host = h;
	    port = p;
	    try 
	    {   
	        soc = new Socket(host, port);
            sml.setSocket(soc);
            sml.start();
    	    return true;
	    }
	    catch(Exception e) 
	    {
	        DGTEMessage m = new DGTEMessage();
	        m.setPart(new String("CLIENT_ERROR"));
	        m.setPart(new String("Could not connect to server."));
	        pipeMessage(m);
	    }
	    return false;
	}

	public boolean disconnect()
	{
	    try 
	    {
       	    DGTEMessage m = new DGTEMessage();
   	        m.setPart("CLIENT_CONNECTIONLOST");
   	        pipeMessage(m);

	        sml.stop();
	        soc.close();
	    }
	    catch(Exception e)
	    {
	    	System.out.println(e.getMessage());
	    	return false;
	    }
	    return true;
	}

	private void sendMessage(DGTEMessage m)
	{
		OutputStream out;
		String messagePart;

		try
		{
			out = soc.getOutputStream();
			for(Enumeration e=m.getParts(); e.hasMoreElements();)
			{
				messagePart = (String)e.nextElement();
				//System.out.println(messagePart);
				for(int i=0; i<messagePart.length(); i++)
				{
					out.write(messagePart.charAt(i));
				}
				out.write((char)0);
				out.flush();
			}
		}
		catch (Exception e)
		{
		}
	}

	//MessagePipeConnection methods
	public void addMessagePipeConnection(DGTEMessagePipe mp)
	{
		messagePipes.addElement(mp);
		//attach ServerMessageListener to pipe
		sml.addMessagePipeConnection(mp);
	}
	public void handleMessage(DGTEMessage m)
	{
		if(csp.isValid(m))
		{
			//ignore client error message
			//but send valid ones to server
			if(!csp.isInternalMessageType(m.getType()) && csp.isClientMessageType(m.getType()))
			{
				sendMessage(m);
			}
		    else
		    {
		        if(m.getType().equals("CLIENT_CONNECT"))
		        {
		            Enumeration e = m.getParts();
		            e.nextElement();
		            host = (String)e.nextElement();
		            port = Integer.parseInt((String)e.nextElement());
		            if (connect(host,port))
		            {
               	        DGTEMessage n = new DGTEMessage();
            	        n.setPart("CLIENT_CONNECTIONAVAILABLE");
    	                handleMessage(n);
    	                pipeMessage(n);
    	            }
    	        }
		        else if(m.getType().equals("CLIENT_DISCONNECT"))
		        {
		            disconnect();
		        }
		        else if(m.getType().equals("SERVER_ERROR"))
		        {
   	                disconnect();
		        }
		        else if(m.getType().equals("SERVER_DISCONNECT"))
		        {
   	                disconnect();
		        }
		    }
		}
	}
	public void pipeMessage(DGTEMessage m)
	{
		Vector targets;
		synchronized (this)
		{
			targets = (Vector) messagePipes.clone();
		}
		for(Enumeration e=targets.elements(); e.hasMoreElements();)
		{
			DGTEMessagePipe mp = (DGTEMessagePipe)e.nextElement();
			mp.handleMessage(m, this);
		}
	}
}
