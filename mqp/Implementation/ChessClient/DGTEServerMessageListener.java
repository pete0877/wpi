import java.util.Vector;
import java.util.Enumeration;
import java.net.*;
import java.io.*;

public class DGTEServerMessageListener extends java.lang.Thread implements MessagePipeConnection
{
    private DGTEClientServerProtocol csp;
    private Vector messagePipes;
	private Socket soc;
    
    DGTEServerMessageListener(DGTEClientServerProtocol c) 
    {
        csp = c;
        messagePipes = new Vector(1);
    }
    
    public void setSocket(Socket s)
    {
        soc = s;
    }
    
   	public void run()
	{
		int messageIndex;
		char message[];
		String messagePart;
		int sendCount = 0;
		boolean receivingMessage = false;
		DGTEMessage newMessage = new DGTEMessage();

		messageIndex = 0;
		message = new char[4096];

		DataInputStream in;
		try
		{
			in = new DataInputStream(soc.getInputStream());

			while(true)
			{
				do
				{
					message[messageIndex] = (char)in.readByte();
				}
				while(((int)message[messageIndex] != 0) && (++messageIndex < 4095));
				messagePart = new String(message, 0, messageIndex);
				if(csp.isMessageType(messagePart))
				{
					if(receivingMessage = true)
					{
						//do something to handle incomplete message
					}
					receivingMessage = true;
					newMessage.clear();
					newMessage.setPart(messagePart);
					sendCount = csp.getMessageParts(messagePart)-1;
				}
				else if(receivingMessage)
				{
					newMessage.setPart(messagePart);
					sendCount--;
				}

				if(receivingMessage && sendCount == 0)
				{
					pipeMessage(newMessage);
					receivingMessage = false;
				}

				messageIndex = 0;
			}
		}
		catch(IOException e)
		{
		    DGTEMessage error = new DGTEMessage();
		    error.setPart("CLIENT_ERROR");
		    error.setPart("Some Client IO Error");
		    pipeMessage(error);
		}
	}

	//MessagePipeConnection methods
	public void addMessagePipeConnection(DGTEMessagePipe mp)
	{
		messagePipes.addElement(mp);
	}
	public void handleMessage(DGTEMessage m)
	{//not used
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