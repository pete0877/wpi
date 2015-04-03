import java.awt.*;
import java.util.*;

public class DGTEMoveText extends java.awt.TextField implements MessagePipeConnection
{
    private Vector messagePipes;    
    
    DGTEMoveText() {messagePipes = new Vector(1);
		//{{REGISTER_LISTENERS
		SymKey aSymKey = new SymKey();
		this.addKeyListener(aSymKey);
		//}}
	}
    
	public void addMessagePipeConnection(DGTEMessagePipe mp){messagePipes.addElement(mp);}
	public void handleMessage(DGTEMessage m){}
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

	class SymKey extends java.awt.event.KeyAdapter
	{
		public void keyPressed(java.awt.event.KeyEvent event)
		{
			Object object = event.getSource();
			if (object == DGTEMoveText.this)
				DGTEMoveText_KeyPressed(event);
		}
	}

	void DGTEMoveText_KeyPressed(java.awt.event.KeyEvent event)
	{
		// to do: code goes here.
			 
		DGTEMoveText_KeyPressed_Interaction1(event);
	}

	void DGTEMoveText_KeyPressed_Interaction1(java.awt.event.KeyEvent event)
	{
		try {
            if(event.getKeyText(event.getKeyCode()).equals("Enter"))
            {
               DGTEMessage n = new DGTEMessage();
               n.setPart("CLIENT_MOVE");
               n.setPart(getText());
   	           pipeMessage(n);
   	           setText("");
            }
        } catch (Exception e) {
		}
	}
}