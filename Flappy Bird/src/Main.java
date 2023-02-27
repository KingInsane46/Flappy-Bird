import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main
{
    public static void main(String[] args)
    {
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
            imgBackground_Night = ImageIO.read(new File("flappy-bird-assets/sprites/background-night.png"));
            imgBase = ImageIO.read(new File("flappy-bird-assets/sprites/base.png"));
            imgGameOver = ImageIO.read(new File("flappy-bird-assets/sprites/gameover.png"));
            imgMessage = ImageIO.read(new File("flappy-bird-assets/sprites/message.png"));
            imgPipe_Green = ImageIO.read(new File("flappy-bird-assets/sprites/pipe-green.png"));
            imgPipe_Red = ImageIO.read(new File("flappy-bird-assets/sprites/pipe-red.png"));

            imgBlueBird_DownFlap = ImageIO.read(new File("flappy-bird-assets/sprites/bluebird-downflap.png"));
            imgBlueBird_MidFlap = ImageIO.read(new File("flappy-bird-assets/sprites/bluebird-midflap.png"));
            imgBlueBird_UpFlap = ImageIO.read(new File("flappy-bird-assets/sprites/bluebird-upflap.png"));

            imgRedBird_DownFlap = ImageIO.read(new File("flappy-bird-assets/sprites/redbird-downflap.png"));
            imgRedBird_MidFlap = ImageIO.read(new File("flappy-bird-assets/sprites/redbird-midflap.png"));
            imgRedBird_UpFlap = ImageIO.read(new File("flappy-bird-assets/sprites/redbird-upflap.png"));

            imgYellowBird_DownFlap = ImageIO.read(new File("flappy-bird-assets/sprites/yellowbird-downflap.png"));
            imgYellowBird_MidFlap = ImageIO.read(new File("flappy-bird-assets/sprites/yellowbird-midflap.png"));
            imgYellowBird_UpFlap = ImageIO.read(new File("flappy-bird-assets/sprites/yellowbird-upflap.png"));
        }
        catch (IOException e) {
            System.out.println("Failed to load all files");
            return;
        }

        GameSprite currentScoreSprite = new GameSprite(img0);
        GameSprite backgroundSprite = new GameSprite(imgBackground_Day);
        backgroundSprite.setPosition(144,250);

        JPanel panel = new JPanel()
        {
            @Override
            public void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                backgroundSprite.draw(g);
            }
        };

        JFrame frame = new JFrame("Flappy Bird");
        frame.setSize(300, 500);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setVisible(true);
    }
}

class GameSprite
{
    public int x, y, width, height;
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
    public void setPosition(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    public void draw(Graphics g)
    {
        g.drawImage(image, x-width/2, y-height/2, null);
    }
}