public class DGTEGamePiece
{
	private Color pieceColor;
	private String pieceType;
	
	DGTEGamePiece() {}
	DGTEGamePiece(Color c, String t)
	{
	    pieceColor = c;
	    pieceType = t;
	}

	void setColor(Color c) {pieceColor = c;}
	void setType(String t) {pieceType = t;}
	
	Color getColor() {return pieceColor;}
	String getType() {return pieceType;}
}