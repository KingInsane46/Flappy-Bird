import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

class Main
{
    public static void main(String[] args) throws InterruptedException {
        GameObject test = new GameObject();
        GameObject test1 = new GameObject();
        test.setPosition(50, 50);
        test1.setPosition(50, 100);

        long currentTime = Clock.systemUTC().millis(), previousTime = currentTime;
        double deltaTime;

        JPanel panel = new JPanel()
        {
            public void paintComponent(Graphics g)
            {
                Graphics2D g2d = (Graphics2D) g;
                test.draw(g2d);
                test1.draw(g2d);
            }
        };
        JFrame frame= new JFrame("Flappy Bird");
        frame.add(panel);
        frame.setSize(500, 500);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);

        while(true)
        {
            deltaTime = (double) (currentTime - previousTime) / 1000;
            previousTime = currentTime;
            currentTime = Clock.systemUTC().millis();



            frame.repaint();
            TimeUnit.MILLISECONDS.sleep(10);
        }
    }
}

class GameObject {
    List<GameObject> children;
    private double x, y, rotation;
    private BufferedImage drawImage;

    public GameObject() {
        children = new ArrayList<GameObject>();
        x = 0;
        y = 0;
        rotation = 0;
    }
    public void setTransform(double x, double y, double rotation)
    {
        setPosition(x, y);
        setRotation(rotation);
    }
    public void setPosition(double x, double y)
    {
        setX(x);
        setY(y);
    }
    public void setX(double x)
    {
        for(GameObject child : children)
            child.x += this.x - x;
        this.x = x;
    }
    public double getX()
    {
        return x;
    }
    public void setY(double y)
    {
        for(GameObject child : children)
            child.y += this.y - y;
        this.y = y;
    }
    public double getY() { return y; }
    public void setRotation(double rotation)
    {
        for(GameObject child : children)
            child.rotation += this.rotation - rotation;
        this.rotation = rotation;
    }
    public double getRotation() { return rotation; }
    public void addChild(GameObject child)
    {
        if(children.contains(child))
            return;
        children.add(child);
    }
    public void removeChild(GameObject child)
    {
        if(!children.contains(child))
            return;
        children.remove(child);
    }
    public List<GameObject> getChildren()
    {
        return children;
    }
    public void setDrawImage(BufferedImage image)
    {
        this.drawImage = image;
    }
    public BufferedImage getDrawImage()
    {
        return drawImage;
    }
    public void draw(Graphics2D g)
    {
        AffineTransform originalTransform = g.getTransform();
        g.translate(x, y);
        g.rotate(Math.toRadians(rotation));
        if(drawImage != null)
            g.drawImage(drawImage,0, 0, null);
        g.setTransform(originalTransform);
    }
}