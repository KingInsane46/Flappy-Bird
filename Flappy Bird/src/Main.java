/*import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.io.File;
import java.io.IOException;
import java.text.AttributedCharacterIterator;
import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Main
{
    public static void main(String[] args) throws InterruptedException {
        final int SCREEN_WIDTH = 305, SCREEN_HEIGHT = 552;

        BufferedImage img0, img1, img2, img3, img4, img5, img6, img7, img8, img9;
        BufferedImage imgBackground_Day;
        BufferedImage imgBase;
        BufferedImage imgGameOver, imgMessage;
        BufferedImage imgPipe_Green;
        BufferedImage imgYellowBird_DownFlap, imgYellowBird_MidFlap, imgYellowBird_UpFlap;

        try {
            img0 = ImageIO.read(new File("flappy-bird-assets/sprites/0.png"));
            img1 = ImageIO.read(new File("flappy-bird-assets/sprites/1.png"));
            img2 = ImageIO.read(new File("flappy-bird-assets/sprites/2.png"));
            img3 = ImageIO.read(new File("flappy-bird-assets/sprites/3.png"));
            img4 = ImageIO.read(new File("flappy-bird-assets/sprites/4.png"));
            img5 = ImageIO.read(new File("flappy-bird-assets/sprites/5.png"));
            img6 = ImageIO.read(new File("flappy-bird-assets/sprites/6.png"));
            img7 = ImageIO.read(new File("flappy-bird-assets/sprites/7.png"));
            img8 = ImageIO.read(new File("flappy-bird-assets/sprites/8.png"));
            img9 = ImageIO.read(new File("flappy-bird-assets/sprites/9.png"));

            imgBackground_Day = ImageIO.read(new File("flappy-bird-assets/sprites/background-day.png"));
            imgBase = ImageIO.read(new File("flappy-bird-assets/sprites/base.png"));
            imgGameOver = ImageIO.read(new File("flappy-bird-assets/sprites/gameover.png"));
            imgMessage = ImageIO.read(new File("flappy-bird-assets/sprites/message.png"));
            imgPipe_Green = ImageIO.read(new File("flappy-bird-assets/sprites/pipe-green.png"));

            imgYellowBird_DownFlap = ImageIO.read(new File("flappy-bird-assets/sprites/yellowbird-downflap.png"));
            imgYellowBird_MidFlap = ImageIO.read(new File("flappy-bird-assets/sprites/yellowbird-midflap.png"));
            imgYellowBird_UpFlap = ImageIO.read(new File("flappy-bird-assets/sprites/yellowbird-upflap.png"));
        }
        catch (IOException e) {
            System.out.println("Failed to load all files");
            return;
        }

        GameSprite background = new GameSprite(imgBackground_Day);
        GameSprite base0 = new GameSprite(imgBase);
        GameSprite base1 = new GameSprite(imgBase);
        GameSprite pipeTop = new GameSprite(imgPipe_Green);
        GameSprite pipeBottom = new GameSprite(imgPipe_Green);
        GameSprite currentScore = new GameSprite(img0);
        GameSprite player = new GameSprite(imgYellowBird_DownFlap);

        final long ANIMATION_SPEED_MILLI = 200;
        long currentTime = Clock.systemUTC().millis();
        long previousCurrentTime = currentTime;
        long nextPlayerFrameChangeTime = currentTime + ANIMATION_SPEED_MILLI;
        int currentPlayerFrame = 0;
        final double GRAVITY = 100, MOVE_SPEED = -75; //Pixels Per Second
        final double POSITION_RESET = SCREEN_WIDTH + (double) background.width/2;
        double deltaTime, moveDirection = 0;
        boolean gameStarted = false;

        GameContainer pipe0 = new GameContainer();
        pipeTop.y = -100;
        pipeBottom.y = 100;
        pipe0.addGameSprite(pipeTop);
        pipe0.addGameSprite(pipeBottom);
        pipe0.setPosition(30, (double) SCREEN_HEIGHT/2);

        background.setPosition(0,0);
        base0.setPosition((double) SCREEN_WIDTH/2, SCREEN_HEIGHT - base0.height+20);
        base1.setPosition((double) SCREEN_WIDTH/2 + base1.width, SCREEN_HEIGHT - base0.height+20);
        currentScore.setPosition((double) SCREEN_WIDTH/2, (double) SCREEN_HEIGHT/4);
        player.setPosition((double) SCREEN_WIDTH/2, (double) SCREEN_HEIGHT/2);

        JPanel panel = new JPanel()
        {
            public void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                background.draw(g);
                //base0.drawCenter(g);
                //base1.drawCenter(g);
                pipe0.draw(g);
                currentScore.drawCenter(g);
                player.drawCenter(g);
            }
        };

        PlayerInput playerInput = new PlayerInput();

        JFrame frame = new JFrame("Flappy Bird");
        frame.add(panel);
        frame.addKeyListener(playerInput);
        frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);


        while(true)
        {
            deltaTime = (double) (currentTime - previousCurrentTime) / 1000;
            previousCurrentTime = currentTime;
            currentTime = Clock.systemUTC().millis();

            //Move Player
            if(playerInput.getSpaceBarDown())
            {
                gameStarted = true;
                moveDirection = GRAVITY;
            }
            if(gameStarted)
            {
                player.y -= deltaTime * moveDirection;
                moveDirection -= GRAVITY * deltaTime;
            }

            //Animate Player
            if(nextPlayerFrameChangeTime < currentTime)
            {
                nextPlayerFrameChangeTime = currentTime + ANIMATION_SPEED_MILLI;
                switch(++currentPlayerFrame%3)
                {
                    case 0 -> player.setImage(imgYellowBird_DownFlap);
                    case 1 -> player.setImage(imgYellowBird_UpFlap);
                    case 2 -> player.setImage(imgYellowBird_MidFlap);
                }
            }

            //Move Ground
            base0.x -= MOVE_SPEED * deltaTime;
            if(base0.x < (double) -base0.width/2)
                base0.x = POSITION_RESET;

            base1.x -= MOVE_SPEED * deltaTime;
            if(base1.x < (double) -base1.width/2)
                base1.x = POSITION_RESET;

            //Move Pipes
            pipe0.setPosition(pipe0.x + MOVE_SPEED*deltaTime, pipe0.y);
            System.out.println((int) pipe0.x);
            if(pipe0.x - (double) pipe0.width > 0)
            {
                pipe0.setPosition(POSITION_RESET, pipe0.y);
            }
            frame.repaint();
        }
    }
}

class GameSprite
{
    public double x, y;
    public int width, height;
    public boolean visible;
    Graphics graphics2D;
    BufferedImage image;
    public GameSprite(BufferedImage image)
    {
        setImage(image);
        x = 0;
        y = 0;
        visible = true;
    }
    public void setImage(BufferedImage image)
    {
        this.image = image;
        width = image.getWidth();
        height = image.getHeight();
    }
    public void setPosition(double x, double y)
    {
        this.x = x;
        this.y = y;
    }
    public void draw(Graphics g)
    {
        if(visible)
            g.drawImage(image, (int) (x + 0.5), (int) (y + 0.5), null);
    }
    public void drawCenter(Graphics g)
    {
        if(visible)
            g.drawImage(image,(int) (x + 0.5) - width/2,(int) (y + 0.5) - height/2, null);
    }
}
class GameContainer
{
    public double x, y;
    int width, height;
    List<GameSprite> children;
    public GameContainer()
    {
        children = new ArrayList<GameSprite>();
        x = 0;
        y = 0;
        width = 0;
        height = 0;
    }
    public void draw(Graphics g)
    {
        for(GameSprite sprite : children)
            sprite.draw(g);
    }

    public void addGameSprite(GameSprite sprite)
    {
        if(!children.contains(sprite))
        {
            children.add(sprite);
            for(GameSprite sprite1 : children)
            {
                if(sprite1.width > width)
                    width = sprite1.width;
                if(sprite1.height > height)
                    height = sprite1.height;
            }
        }
    }
    public void setPosition(double x, double y)
    {
        for(GameSprite sprite : children)
        {
            sprite.x -= (this.x - x);
            sprite.y -= (this.y - y);
        }
        this.x = x;
        this.y = y;
    }
}
class PlayerInput extends KeyAdapter
{
    final int SPACE_BAR_KEY_CODE = 32;
    boolean spaceBarPressed = false, spaceBarReleased = true;
    public void keyPressed(KeyEvent event)
    {
        int keyCode = event.getKeyCode();
        if(keyCode == SPACE_BAR_KEY_CODE && spaceBarReleased)
        {
            spaceBarPressed = true;
            spaceBarReleased = false;
        }
    }
    public void keyReleased(KeyEvent event)
    {
        int keyCode = event.getKeyCode();
        if(keyCode == SPACE_BAR_KEY_CODE)
        {
            spaceBarReleased = true;
        }
    }
    public boolean getSpaceBarDown()
    {
        boolean temp = spaceBarPressed;
        spaceBarPressed = false;
        return temp;
    }
}

*/

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class Main
{
    public static void main(String[] args)
    {

    }
}

class GameObject
{
    List<GameObject> children;
    double x, y;
    public GameObject()
    {
        children = new ArrayList<GameObject>();
        x = 0;
        y = 0;
    }
}