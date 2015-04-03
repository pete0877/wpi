import java.util.Vector;
import java.util.Enumeration;

public interface MessagePipeConnection
{
	abstract public void addMessagePipeConnection(DGTEMessagePipe mp);
	abstract public void handleMessage(DGTEMessage m);
	abstract public void pipeMessage(DGTEMessage m);
}
