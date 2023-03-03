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
        BufferedImage backgroundImage = ImageIO.read(new File("flappy-bird-assets/sprites/background-day.png"));
        BufferedImage pipeImage = ImageIO.read(new File("flappy-bird-assets/sprites/pipe-green.png"));

        BufferedImage playerDown = ImageIO.read(new File("flappy-bird-assets/sprites/yellowbird-downflap.png"));

        GameObject background = new GameObject("Background");
        background.setDrawImage(backgroundImage);
        background.setPosition( 144, 256);

        GameObject pipeTop = new GameObject("Pipe Top");
        GameObject pipeBottom = new GameObject("Pipe Bottom");
        GameObject pipePrefab = new GameObject("Pipe Prefab");
        pipeTop.setDrawImage(pipeImage);
        pipeBottom.setDrawImage(pipeImage);
        pipeTop.setVisible(false);
        pipeBottom.setVisible(false);
        pipeTop.setTransform(0, -200, 180);
        pipeBottom.setTransform(0, 200, 0);
        pipePrefab.addChild(pipeTop);
        pipePrefab.addChild(pipeBottom);
        pipePrefab.setPosition((double) 288/2, (double) 512/2);

        GameObject pipe0 = pipePrefab.createCopy();
        pipe0.setAllVisible(true);

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
        frame.setSize(288, 512);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);

        while(true)
        {
            deltaTime = (double) (currentTime - previousTime) / 1000;
            previousTime = currentTime;
            currentTime = Clock.systemUTC().millis();

            pipe0.setX(pipe0.getX() - 30 * deltaTime);

            frame.repaint();
        }
    }
}

class GameObject {
    private final List<GameObject> children = new ArrayList<>();
    private BufferedImage drawImage = null;
    private String name;
    private double x = 0, y = 0, rotation = 0;
    private int width = 0, height = 0;
    private boolean visible = true;

    public GameObject(String name)
    {
        Main.objectsToDraw.add(this);
        setName(name);
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
    public void setName(String name)
    {
        this.name = name;
    }
    public String getName()
    {
        return name;
    }
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
    public GameObject getChildByName(String name)
    {
        if(this.name.equals(name))
            return this;
        for(GameObject child : children)
            if(child.getName().equals(name))
                return child;
        return null;
    }
    public void setDrawImage(BufferedImage image)
    {
        this.drawImage = image;
        width = image == null ? 0 : image.getWidth();
        height = image == null ? 0 : image.getHeight();
    }
    public BufferedImage getDrawImage()
    {
        return drawImage;
    }
    public void setVisible(boolean value)
    {
        visible = value;
    }
    public void setAllVisible(boolean value)
    {
        setVisible(value);
        for(GameObject child : children)
            child.setAllVisible(value);
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
        GameObject copy = new GameObject(getName());
        copy.setTransform(getX(), getY(), getRotation());
        copy.setDrawImage(getDrawImage());
        copy.setVisible(getVisible());
        for(GameObject child : children)
            copy.addChild(child.createCopy());
        return copy;
    }
}