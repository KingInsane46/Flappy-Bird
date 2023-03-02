import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

class Main
{
    static List<GameObject> objectsToDraw = new ArrayList<GameObject>();
    public static void main(String[] args) throws InterruptedException, IOException {
        BufferedImage pipeImage = ImageIO.read(new File("flappy-bird-assets/sprites/pipe-green.png"));

        GameObject pipeTop = new GameObject();
        GameObject pipeBottom = new GameObject();
        GameObject pipePrefab = new GameObject();

        pipeTop.setVisible(false);
        pipeBottom.setVisible(false);
        pipeTop.setDrawImage(pipeImage);
        pipeBottom.setDrawImage(pipeImage);
        pipeTop.setTransform(0, -200, 180);
        pipeBottom.setTransform(0, 200, 0);

        GameObject pipeTopCopy = pipeTop.createCopy();
        GameObject pipeBottomCopy = pipeBottom.createCopy();
        pipeTopCopy.setVisible(true);
        pipeBottomCopy.setVisible(true);

        pipePrefab.addChild(pipeTopCopy);
        pipePrefab.addChild(pipeBottomCopy);
        pipePrefab.setPosition(250, 250);


        long currentTime = Clock.systemUTC().millis(), previousTime = currentTime;
        double deltaTime;

        JPanel panel = new JPanel()
        {
            public void paintComponent(Graphics g)
            {
                Graphics2D g2d = (Graphics2D) g;
                for (GameObject object : objectsToDraw)
                    object.draw(g2d);
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

            //pipePrefab.setX(pipePrefab.getX()  - deltaTime);

            frame.repaint();
        }
    }
}

class GameObject {
    private BufferedImage drawImage = null;
    private List<GameObject> children = new ArrayList<GameObject>();
    private GameObject parent = null;
    private double x = 0, y = 0, rotation = 0;
    private int width = 0, height = 0;
    private boolean visible = true;

    public GameObject()
    {
        Main.objectsToDraw.add(this);
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
            child.x -= this.x - x;
        this.x = x;
    }
    public double getX()
    {
        return x;
    }
    public void setY(double y)
    {
        for(GameObject child : children)
            child.y -= this.y - y;
        this.y = y;
    }
    public double getY()
    {
        return y;
    }
    public void setRotation(double rotation)
    {
        for(GameObject child : children)
            child.rotation += this.rotation - rotation;
        this.rotation = rotation;
    }
    public double getRotation()
    {
        return rotation;
    }
    public void addChild(GameObject child)
    {
        if(children.contains(child))
            return;
        children.add(child);
        child.parent = this;
    }
    public void removeChild(GameObject child)
    {
        if(!children.contains(child))
            return;
        children.remove(child);
        child.parent = null;
    }
    public List<GameObject> getChildren()
    {
        return children;
    }

    public void setParent(GameObject parent)
    {
        if(parent == null)
            return;
        if(!parent.getChildren().contains(this))
            parent.addChild(this);
        this.parent.removeChild(this);
        this.parent = parent;
    }
    public GameObject getParent()
    {
        return parent;
    }
    public void setDrawImage(BufferedImage image)
    {
        this.drawImage = image;
        width = image.getWidth();
        height = image.getHeight();
    }
    public BufferedImage getDrawImage()
    {
        return drawImage;
    }
    public void setVisible(boolean value)
    {
        visible = value;
    }
    public boolean getVisible()
    {
        return visible;
    }
    public void draw(Graphics2D g)
    {
        for(GameObject child : children)
            child.draw(g);
        AffineTransform originalTransform = g.getTransform();
        g.translate(x, y);
        g.rotate(Math.toRadians(rotation));
        if(drawImage != null && visible)
            g.drawImage(drawImage, -width/2, -height/2, null);
        g.setTransform(originalTransform);
    }

    public GameObject createCopy()
    {
        GameObject copy = new GameObject();
        copy.setTransform(getX(), getY(), getRotation());
        copy.setDrawImage(getDrawImage());
        copy.setVisible(getVisible());
        copy.setParent(getParent());
        for(GameObject child : children)
        {
            GameObject childCopy = new GameObject();
            childCopy = child.createCopy();
            copy.addChild(childCopy);
        }

        return copy;
    }
}