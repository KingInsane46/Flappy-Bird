import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.time.Clock;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Main
{
    public static void main(String[] args) throws InterruptedException {
        final int SCREEN_WIDTH = 305, SCREEN_HEIGHT = 552;
        BufferedImage img0, img1, img2, img3, img4, img5, img6, img7, img8, img9;
        BufferedImage imgBackground_Day, imgBackground_Night;
        BufferedImage imgBase;
        BufferedImage imgGameOver, imgMessage;
        BufferedImage imgPipe_Green, imgPipe_Red;
        BufferedImage imgBlueBird_DownFlap, imgBlueBird_MidFlap, imgBlueBird_UpFlap;
        BufferedImage imgRedBird_DownFlap, imgRedBird_MidFlap, imgRedBird_UpFlap;
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
        GameSprite currentScore = new GameSprite(img0);
        GameSprite player = new GameSprite(imgYellowBird_DownFlap);

        background.setPosition(0,0);
        currentScore.setPosition(SCREEN_WIDTH/2,SCREEN_HEIGHT/4);
        player.setPosition(SCREEN_WIDTH/2, SCREEN_HEIGHT/2);

        JPanel panel = new JPanel()
        {
            public void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                background.draw(g);
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

        final long ANIMATION_SPEED_MILLI = 200;
        long currentTime = Clock.systemUTC().millis();
        long previousCurrentTime = currentTime;
        long nextPlayerFrameChangeTime = currentTime + ANIMATION_SPEED_MILLI;
        int currentPlayerFrame = 0;
        final double GRAVITY = 20;
        double deltaTime, moveDirection = 0;
        boolean gameStarted = false;

        while(true)
        {
            deltaTime = (double) (currentTime - previousCurrentTime) / 1000;
            previousCurrentTime = currentTime;
            currentTime = Clock.systemUTC().millis();


            if(playerInput.getSpaceBarDown())
            {
                gameStarted = true;
                if(moveDirection < 0)
                    moveDirection = GRAVITY;
                else
                    moveDirection += GRAVITY;
            }

            if(gameStarted)
            {
                player.y -= deltaTime * moveDirection * 3;
                moveDirection -= deltaTime*GRAVITY;
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

            frame.repaint();
        }
    }
}

class GameSprite
{
    public double x, y;
    public int width, height;
    BufferedImage image;
    public GameSprite(BufferedImage image)
    {
        setImage(image);
        x = 0;
        y = 0;
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
        g.drawImage(image, (int) (x + 0.5), (int) (y + 0.5), null);
    }

    public void drawCenter(Graphics g)
    {
        g.drawImage(image,(int) (x + 0.5) - width/2,(int) (y + 0.5) - height/2, null);
    }
}

class PlayerInput extends KeyAdapter
{
    final int SPACE_BAR_KEY_CODE = 32;
    boolean spaceBarPressed = false, spaceBarReleased = true;

    public void keyPressed(KeyEvent event)
    {
        int keyCode = event.getKeyCode();
        if(keyCode == SPACE_BAR_KEY_CODE)
        {
            spaceBarPressed = true;
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