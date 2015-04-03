import java.util.Vector;
import java.util.Enumeration;
import java.awt.*;

public class DGTEGamePlayer extends java.awt.TextArea implements MessagePipeConnection   
{
    /*
        #define neutral 0
        #define white   1
        #define black   2
        
        #define vacant 0
        #define pawn   1
        #define knight 2
        #define bishop 3
        #define rook   4
        #define queen  5
        #define king   6
    
        gamePieces go from a8 to h1
        same with colors
    */
    
    private Vector messagePipes;
    private DGTEClientServerProtocol csp;
    
    private String gamePieces;
    private String gameColors;
    private String computerColor;
    private boolean userTurn;
    private boolean gameStarted;
        
    DGTEGamePlayer(DGTEClientServerProtocol c)
    {
        csp = c;
        messagePipes = new Vector(1);
        init();
        
		//{{INIT_CONTROLS
		setEditable(false);
		setBackground(java.awt.Color.white);
		setForeground(java.awt.Color.black);
		setFont(new Font("MonoSpaced", Font.BOLD, 10));
		setSize(0,0);
		//}}

	    //displayState();
	}

    public void init()
    {
        gamePieces = new String("4236532411111111000000000000000000000000000000001111111142365324");
        gameColors =new String("1111111111111111000000000000000000000000000000002222222222222222");
        computerColor = new String("1");
        userTurn = true;
        gameStarted = false;
    }
    
    public void makeMove(String move)
    {
        if(move.length() == 4)
        {
            int frcol, tocol;
            switch (move.charAt(0))
            {
                case 'a': frcol = 0; break;
                case 'b': frcol = 1; break;
                case 'c': frcol = 2; break;
                case 'd': frcol = 3; break;
                case 'e': frcol = 4; break;
                case 'f': frcol = 5; break;
                case 'g': frcol = 6; break;
                case 'h': frcol = 7; break;
                default: frcol = 0; break;
            }
            switch (move.charAt(2))
            {
                case 'a': tocol = 0; break;
                case 'b': tocol = 1; break; 
                case 'c': tocol = 2; break;
                case 'd': tocol = 3; break;
                case 'e': tocol = 4; break;
                case 'f': tocol = 5; break;
                case 'g': tocol = 6; break;
                case 'h': tocol = 7; break;
                default: tocol = 0; break;
            }
                
            int frrow = Integer.decode(move.substring(1, 2)).intValue() - 1;
            int torow = Integer.decode(move.substring(3, 4)).intValue() - 1;
            int frloc = frrow*8 + frcol;
            int toloc = torow*8 + tocol;
            char[] newPieces = new char[64];
            char[] newColors = new char[64];
            gamePieces.getChars(0, 64, newPieces, 0);
            gameColors.getChars(0, 64, newColors, 0);
            newPieces[toloc] = newPieces[frloc];
            newPieces[frloc] = '0';
            newColors[toloc] = newColors[frloc];
            newColors[frloc] = '0';
            gamePieces = new String(newPieces);
            gameColors = new String(newColors);
        }
    }
    
    
    public void displayState()
    {
        setText("");
        appendText("\n  |---------------------------------------|\n");
        for(int i=0; i<8; i++)
        {
            switch (i)
            {
                case 0: appendText("1"); break;
                case 1: appendText("2"); break; 
                case 2: appendText("3"); break;
                case 3: appendText("4"); break;
                case 4: appendText("5"); break;
                case 5: appendText("6"); break;
                case 6: appendText("7"); break;
                case 7: appendText("8"); break;
            }
            appendText(" |");
            for(int j=0; j<8; j++)
            {
                appendText(" ");
                char piece = gamePieces.charAt(i*8 + j);
                if(piece == '6') appendText("K");
                else if(piece == '5') appendText("Q");
                else if(piece == '4') appendText("R");
                else if(piece == '3') appendText("B");
                else if(piece == '2') appendText("N");
                else if(piece == '1') appendText("P");
                else if(piece == '0')
                {
                    if ((i % 2) == (j % 2)) appendText(" ");
                    else appendText(":");
                }
                
                char color = gameColors.charAt(i*8 + j);
                if(color == '0')
                {
                    if ((i % 2) == (j % 2)) appendText(" ");
                    else appendText(":");
                }
                else if(color == '1') appendText("w");
                else if(color == '2') appendText("b");
                
                appendText(" |");
            }
            appendText("\n  |---------------------------------------|\n");
        }
        appendText("     a    b    c    d    e    f    g    h |\n");
    }

    public String getState()
    {
        return computerColor + gamePieces + gameColors;
    }

    public void setState(String state)
    {
        if(state.length() == 129)
        {
            gamePieces = state.substring(1, 65);
            gameColors = state.substring(65, 129);
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

    public void handleMessage(DGTEMessage m)
    {
		if(csp.isValid(m))
		{
            if(m.getType().equals("CLIENT_CONNECTIONAVAILABLE"))
		    {
		        DGTEMessage n = new DGTEMessage();
                n.setPart("CLIENT_ENCODING");
                n.setPart("MQP");
   	            pipeMessage(n);
		    }
		    if(m.getType().equals("SERVER_STARTGAME"))
		    {
                init();
                gameStarted = true;
		        displayState();
		        //if computer goes first, send it state
		        //else wait for user to move
		        if(!userTurn)
		        {
		            DGTEMessage n = new DGTEMessage();
                    n.setPart("CLIENT_GAMESTATE");
                    n.setPart(getState());
   	                pipeMessage(n);
   	            }
		    }
		    if(m.getType().equals("SERVER_MOVE"))
		    {
                if(!userTurn && gameStarted)
                {
                    Enumeration e = m.getParts();
                    e.nextElement();
                    e.nextElement();
                    makeMove((String)e.nextElement());
                    displayState();
                    userTurn = !userTurn;
                }
		    }
		    if(m.getType().equals("CLIENT_MOVE"))
		    {
                if(userTurn && gameStarted)
                {
                    Enumeration e = m.getParts();
                    e.nextElement();
                    
                    makeMove((String)e.nextElement());
                    displayState();
                    userTurn = !userTurn;
                    
                    //send new board to server
                    DGTEMessage n = new DGTEMessage();
                    n.setPart("CLIENT_GAMESTATE");
                    n.setPart(getState());
   	                pipeMessage(n);
                }
		    }
		}
	}

    public void addMessagePipeConnection(DGTEMessagePipe mp)
    {
		messagePipes.addElement(mp);
	}
	//{{DECLARE_CONTROLS
	//}}
}