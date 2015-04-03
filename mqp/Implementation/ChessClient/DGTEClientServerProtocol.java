import java.util.Vector;
import java.util.Hashtable;

public abstract class DGTEClientServerProtocol
{
	protected String version;
	protected Vector messageTypes;
	protected Hashtable numMessageParts;
	protected Hashtable messageDescriptions;

	abstract public boolean isMessageType(String type);
	abstract public int getMessageParts(String type);
	abstract public String getMessageDescription(String type);
	abstract public boolean isValid(DGTEMessage m);
	abstract public boolean isClientMessageType(String type);
	abstract public boolean isServerMessageType(String type);
	abstract public boolean isInternalMessageType(String type);
}
