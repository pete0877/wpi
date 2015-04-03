public abstract class DGTEGameEncoding
{
    protected String encodingType;
    protected String encodingVersion;
    
    abstract public boolean isValidGameState(String encGameState);
    abstract public boolean isValidGameMove(String encGameState);
    abstract public DGTEGameMove decodeMove(String gm);
    abstract public DGTEGameState decodeState(String gs);
    abstract public String encodeMove(DGTEGameMove gm);
    abstract public String encodeState(DGTEGameState gs);
        
    public String getType() {return encodingType;}
    public String getVersion() {return encodingVersion;}
}