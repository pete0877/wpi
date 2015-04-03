import java.awt.*;

public class ImageCanvas extends java.awt.Canvas
{
    private Image img;

    public ImageCanvas(){}

    public void setImage(String fileName)
    {
        String imageFile = System.getProperty("user.dir") + "/" + fileName;
        img = getToolkit().getImage(imageFile);
        
        MediaTracker mt = new MediaTracker(this);
        mt.addImage(img, 0);
        try {
            mt.waitForAll();
        } catch(InterruptedException e) {}
    }
    
    public void paint(Graphics g)
    {
        if(img != null)
        {
            g.drawImage(img, 0, 0, this);
        }
        
    }
}