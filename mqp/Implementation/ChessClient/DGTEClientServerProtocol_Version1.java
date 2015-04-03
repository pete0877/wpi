import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;

public class DGTEClientServerProtocol_Version1 extends DGTEClientServerProtocol
{
	DGTEClientServerProtocol_Version1()
	{
		version = new String("1.0");
		messageTypes = new Vector(13);
		numMessageParts = new Hashtable(13);
		messageDescriptions = new Hashtable(13);

		String SERVER_MESSAGE = new String("SERVER_MESSAGE");
		String SERVER_NUMGAMETYPES = new String("SERVER_NUMGAMETYPES");
		String SERVER_GAMETYPE = new String("SERVER_GAMETYPE");
		String SERVER_STARTGAME = new String("SERVER_STARTGAME");
		String SERVER_WAITTIME = new String("SERVER_WAITTIME");
		String SERVER_MOVE = new String("SERVER_MOVE");
		String SERVER_ENDGAME = new String("SERVER_ENDGAME");
		String SERVER_ERROR = new String("SERVER_ERROR");
		String CLIENT_ENCODING = new String("CLIENT_ENCODING");
		String CLIENT_GAMETYPE = new String("CLIENT_GAMETYPE");
		String CLIENT_PLY = new String("CLIENT_PLY");
		String CLIENT_GAMESTATE = new String("CLIENT_GAMESTATE");
		String CLIENT_MOVE = new String("CLIENT_MOVE");
		String CLIENT_ERROR = new String("CLIENT_ERROR");
		String CLIENT_CONNECT = new String("CLIENT_CONNECT");
		String CLIENT_CONNECTIONAVAILABLE = new String("CLIENT_CONNECTIONAVAILABLE");
		String CLIENT_DISCONNECT = new String("CLIENT_DISCONNECT");
		String CLIENT_CONNECTIONLOST = new String("CLIENT_CONNECTIONLOST");

		messageTypes.addElement(SERVER_MESSAGE);
		messageTypes.addElement(SERVER_NUMGAMETYPES);
		messageTypes.addElement(SERVER_GAMETYPE);
		messageTypes.addElement(SERVER_STARTGAME);
		messageTypes.addElement(SERVER_WAITTIME);
		messageTypes.addElement(SERVER_MOVE);
		messageTypes.addElement(SERVER_ENDGAME);
		messageTypes.addElement(SERVER_ERROR);
		messageTypes.addElement(CLIENT_ENCODING);
		messageTypes.addElement(CLIENT_GAMETYPE);
		messageTypes.addElement(CLIENT_PLY);
		messageTypes.addElement(CLIENT_GAMESTATE);
		messageTypes.addElement(CLIENT_MOVE);
		messageTypes.addElement(CLIENT_ERROR);
		messageTypes.addElement(CLIENT_CONNECT);
		messageTypes.addElement(CLIENT_CONNECTIONAVAILABLE);
		messageTypes.addElement(CLIENT_DISCONNECT);
		messageTypes.addElement(CLIENT_CONNECTIONLOST);

		numMessageParts.put(SERVER_MESSAGE,new Integer(2));
		numMessageParts.put(SERVER_NUMGAMETYPES,new Integer(2));
		numMessageParts.put(SERVER_GAMETYPE,new Integer(3));
		numMessageParts.put(SERVER_STARTGAME,new Integer(1));
		numMessageParts.put(SERVER_WAITTIME,new Integer(2));
		numMessageParts.put(SERVER_MOVE,new Integer(5));
		numMessageParts.put(SERVER_ENDGAME,new Integer(1));
		numMessageParts.put(SERVER_ERROR,new Integer(2));
		numMessageParts.put(CLIENT_ENCODING,new Integer(2));
		numMessageParts.put(CLIENT_GAMETYPE,new Integer(2));
		numMessageParts.put(CLIENT_PLY,new Integer(2));
		numMessageParts.put(CLIENT_GAMESTATE,new Integer(2));
		numMessageParts.put(CLIENT_MOVE,new Integer(2));
		numMessageParts.put(CLIENT_ERROR,new Integer(2));
		numMessageParts.put(CLIENT_CONNECT, new Integer(3));
		numMessageParts.put(CLIENT_CONNECTIONAVAILABLE, new Integer(1));
		numMessageParts.put(CLIENT_DISCONNECT, new Integer(1));
		numMessageParts.put(CLIENT_CONNECTIONLOST, new Integer(1));

		messageDescriptions.put(SERVER_MESSAGE,"No Comment");
		messageDescriptions.put(SERVER_NUMGAMETYPES,"No Comment");
		messageDescriptions.put(SERVER_GAMETYPE,"No Comment");
		messageDescriptions.put(SERVER_STARTGAME,"No Comment");
		messageDescriptions.put(SERVER_WAITTIME,"No Comment");
		messageDescriptions.put(SERVER_MOVE,"No Comment");
		messageDescriptions.put(SERVER_ENDGAME,"No Comment");
		messageDescriptions.put(SERVER_ERROR,"No Comment");
		messageDescriptions.put(CLIENT_ENCODING,"No Comment");
		messageDescriptions.put(CLIENT_GAMETYPE,"No Comment");
		messageDescriptions.put(CLIENT_PLY,"No Comment");
		messageDescriptions.put(CLIENT_GAMESTATE,"No Comment");
		messageDescriptions.put(CLIENT_MOVE,"No Comment");
		messageDescriptions.put(CLIENT_ERROR,"No Comment");
		messageDescriptions.put(CLIENT_CONNECT,"No Comment");
		messageDescriptions.put(CLIENT_CONNECTIONAVAILABLE,"No Comment");
		messageDescriptions.put(CLIENT_DISCONNECT,"No Comment");
		messageDescriptions.put(CLIENT_CONNECTIONLOST,"No Comment");
	}

	public boolean isMessageType(String type)
	{
		if(messageTypes.contains(type)) return true;
		return false;
	}
	public int getMessageParts(String type)
	{
		if(isMessageType(type))
		{
			Integer i = (Integer)numMessageParts.get(type);
			return i.intValue();
		}
		return 0;
	}
	public String getMessageDescription(String type)
	{
		if(isMessageType(type)) return (String)messageDescriptions.get(type);
		return new String("");
	}
	public boolean isValid(DGTEMessage m)
	{
		boolean checkType = true;
		int messageParts = 0;
		String messageType = new String("");
		for(Enumeration e=m.getParts(); e.hasMoreElements();)
		{
			if(checkType)
			{
				if(!isMessageType(messageType = (String)e.nextElement())) return false;
				messageParts++;
				checkType = false;
			}
			else
			{
				e.nextElement();
				messageParts++;
			}
		}
		if(getMessageParts(messageType) == messageParts) return true;
		return false;
	}
	public boolean isClientMessageType(String type)
	{
		if(isInternalMessageType(type)) return false;		
		else if(type.startsWith("CLIENT")) return true;
		else return false;
	}
	public boolean isServerMessageType(String type)
	{
		if(type.startsWith("SERVER")) return true;
		return false;
	}
	public boolean isInternalMessageType(String type)
	{
		if(type.equals("CLIENT_MOVE") || type.equals("CLIENT_ERROR") || type.equals("CLIENT_CONNECT") || type.equals("CLIENT_CONNECTIONAVAILABLE") || type.equals("CLIENT_DISCONNECT")) return true;
		return false;
	}

}
