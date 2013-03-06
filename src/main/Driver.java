package main;

import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;

/**
 *
 * @author Tobin
 */
public class Driver
{
    public static void main(String[] args)
    {
        System.out.println("Hello World");
        
        // load an image file
        setBestLAF(); // set the look and feel to something more pretty
        BufferedImage i = getImageWithPreview(null); // get the image
        
        // if the user hit the x, or cancel close the program
        if(i == null)
        {
            System.exit(0);
        }
        
        // save the image as a matrix
        int[][] matrix = new int[i.getHeight()][i.getWidth()];
        for(int m = 0; m < matrix.length; m++)
        {
            for(int n = 0; n < matrix[m].length; n++)
            {
                matrix[m][n] = i.getRGB(n, m);
            }
        }
        
        // decompose the matrix into U S V
    }
    
    /************** UTILITY FUNCTIONS **************/
    
    /**
     * Gets an image from a file using a FileDialog.  This implementation
     * provides image previews, but no file filtering.  The user can select any
     * file, even a non image file and press ok.  If this happens there is an
     * error message and the user is asked to select a valid image file.  If the
     * user presses cancel then this will return null.
     * @param parent The parent frame
     * @return The image, or null if the user pressed cancel
     */
    public static BufferedImage getImageWithPreview(Frame parent)
    {
        FileDialog chooser = new FileDialog(parent, "Open Image", FileDialog.LOAD);
        chooser.setVisible(true);
        // The user pressed cancel
        if(chooser.getFile() == null)
        {
            return null;
        }
        
        File f = new File(chooser.getDirectory()+chooser.getFile());
        chooser.dispose(); // get rid of the resources needed for the window
        // Try to read the file, and loop this method if there is an error
        try
        {
            return ImageIO.read(f);
        }
        catch (IOException ex)
        {
            JOptionPane.showMessageDialog(parent, "Error reading image.  Choose a valid image.", "Image", JOptionPane.ERROR_MESSAGE);
            return getImageWithPreview(parent);
        }
    }
    /**
     * Set the best available look-and-feel into use.
     */
    public static void setBestLAF()
    {
        /*
         * Set the look-and-feel.  On Linux, Motif/Metal is sometimes incorrectly used
         * which is butt-ugly, so if the system l&f is Motif/Metal, we search for a few
         * other alternatives.
         */
        try
        {
            // Set system L&F
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // Check whether we have an ugly L&F
            LookAndFeel laf = UIManager.getLookAndFeel();
            if (laf == null || laf.getName().matches(".*[mM][oO][tT][iI][fF].*") || laf.getName().matches(".*[mM][eE][tT][aA][lL].*"))
            {

                // Search for better LAF
                UIManager.LookAndFeelInfo[] info = UIManager.getInstalledLookAndFeels();

                String lafNames[] =
                {
                        ".*[gG][tT][kK].*",
                        ".*[wW][iI][nN].*",
                        ".*[mM][aA][cC].*",
                        ".*[aA][qQ][uU][aA].*",
                        ".*[nN][iI][mM][bB].*"
                };

                lf: for (String lafName: lafNames)
                {
                    for (UIManager.LookAndFeelInfo l: info)
                    {
                        if (l.getName().matches(lafName))
                        {
                            UIManager.setLookAndFeel(l.getClassName());
                            break lf;
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            System.err.println("Error setting LAF: " + e);
        }
    }
}
