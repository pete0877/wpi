public class DGTEGameState
{
	Hashtable gameState;	//Indexed by piece

	DGTEGameState() {}
	public Enumeration getPiece(String location) {}
	public void setPiece(GamePiece piece, String location) {}
	public void removePiece(GamePiece piece, String location) {}
	public String getLocation(GamePiece piece) {}
}