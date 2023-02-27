import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        File png1 = new File("flappy-bird-assets/sprites/1.png");

        JLabel lab0 = new JLabel();
        lab0.setIcon(new ImageIcon(ImageIO.read(new File("flappy-bird-assets/sprites/0.png"))));

        JFrame frame = new JFrame("Flappy Bird");
        frame.setSize(500, 750);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.add(lab0);

        frame.setVisible(true);
    }
}