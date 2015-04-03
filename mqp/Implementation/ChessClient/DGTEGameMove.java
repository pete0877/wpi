public class DGTEGameMove
{
	String encoding;
	Vector fromLocation;
	Vector toLocation;
	Vector action;

	DGTEGameMove() {}
	DGTEGameMove(String encGameMove) {}
	
	String getEncoding() {return 
	String getFromLocation();
	String getToLocation();

	String setMove(String encGameMove);
	abstract boolean checkEncoding(String state);
	abstract String toString();	

}