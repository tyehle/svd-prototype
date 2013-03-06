package main;

import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

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
     * Transposes the matrix A.  
     * @param A The matrix to transpose
     */
    public static <T> void transpose(T[][] A)
    {
        for(int m = 0; m < A.length; m++)
        {
            // only iterate over the upper triangle
            for(int n = m; n < A[m].length; n++)
            {
                // swap Amn with Anm
                T temp = A[m][n];
                A[m][n] = A[n][m];
                A[n][m] = temp;
            }
        }
    }
    /**
     * Multiplies A and B.  The output is the product A*B; if this operation is
     * not possible because of the dimensions of A and B then this will return
     * null.  This function assumes A and B are rectangular matrices.  If this
     * is not the case it may throw an exception.
     * @param A The left operand
     * @param B The right operand
     * @return The product A*B, or null if that is not possible
     */
    public static double[][] multiply(double[][] A, double[][] B)
    {
        if(A[0].length != B.length)
        {
            return null;
        }
        
        double[][] out = new double[A.length][B[0].length];
        for(int m = 0; m < out.length; m++)
        {
            for(int n = 0; n < out[m].length; n++)
            {
                // compute the out[m][n] entry
                double val = 0;
                for(int i = 0; i < A[m].length; i++)
                {
                    val += A[m][i]*B[i][n];
                }
                out[m][n] = val;
            }
        }
        
        return out;
    }
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
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            System.err.println("Error setting LAF: " + e);
        }
    }
}
