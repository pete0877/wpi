public abstract class DGTEGameSurface
{
    Vector locations;
    
    abstract public boolean isValidLocation(DGTEGameLocation gl);
    abstract public Enumeration getLocations();
    
}