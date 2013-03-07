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
        int[][] A = new int[i.getWidth()][i.getHeight()];
        for(int y = 0; y < A[0].length; y++)
        {
            for(int x = 0; x < A.length; x++)
            {
                A[x][y] = i.getRGB(x, y);
            }
        }
        
        // separate into rgb components
        int W = A.length, H = A[0].length;
        double[][] Ar = new double[W][H],
                Ag = new double[W][H],
                Ab = new double[W][H];
        for(int x = 0; x < W; x++)
        {
            for(int y = 0; y < H; y++)
            {
                Ar[x][y] = A[x][y] >> 16 & 0xff;
                Ag[x][y] = A[x][y] >> 8 & 0xff;
                Ab[x][y] = A[x][y] & 0xff;
            }
        }
        
        // decompose the matrix into A = U S V
        double[][] U = new double[A.length][A[0].length],
                B = new double[A.length][A.length],
                V = new double[A.length][A.length];
        bidiagonalize(Ar, U, B, V);
    }
    
    /************** UTILITY FUNCTIONS **************/
    
    /**
     * Transposes the matrix A.  
     * @param A The matrix to transpose
     */
    public static double[][] transpose(double[][] A)
    {
        double[][] out = new double[A[0].length][A.length];
        
        for(int x = 0; x < A.length; x++)
        {
            // only iterate over the upper triangle
            for(int y = 0; y < A[x].length; y++)
            {
                out[y][x] = A[x][y];
            }
        }
        
        return out;
    }
    
    public static double[] scale(double c, double[] A)
    {
        double[] out = new double[A.length];
        for(int i = 0; i < A.length; i++)
        {
            out[i] = c*A[i];
        }
        return out;
    }
    
    // out = Ax
    public static double[] multiply(double[][] A, double[] x)
    {
        // check dimensions
        if(A.length != x.length)
        {
            throw new ArithmeticException("Invalid matrix dimensions: "+A[0].length+"x"+A.length+" times "+x.length+"x1");
        }
        
        double[] out = new double[A[0].length];
        for(int i = 0; i < out.length; i++)
        {
            // compute the ith entry of out
            double val = 0;
            for(int j = 0; j < x.length; j++)
            {
                val += A[j][i]*x[j];
            }
            out[i] = val;
        }
        
        return out;
    }
    
    public static double[] subtract(double[] A, double[] B)
    {
        if(A.length != B.length)
        {
            throw new ArithmeticException("Invalid matrix dimensions");
        }
        
        double[] out = new double[A.length];
        
        for(int i = 0; i < A.length; i++)
        {
            out[i] = A[i] - B[i];
        }
        
        return out;
    }
    
    public static void bidiagonalize(double[][] A, double[][] U, double[][] B, double[][] V)
    {
        // check dimensions
        
        
        // choose v_1 = 2 unit norm vector
        V[0][0] = 1;
        for(int i = 1; i < V[0].length; i++)
        {
            V[0][i] = 0;
        }
        
        boolean virgin = true;
        for(int k = 0;; k++)
        {
            System.out.println("k: " + k);
            // u_k = A v_k - Beta_k-1 u_k-1
            // build the kth column of U
            if(virgin)
            {
                U[k] = multiply(A, V[k]);
                virgin = false;
            }
            else
            {
                U[k] = subtract(multiply(A, V[k]), scale(B[k-1][k], U[k-1]));
            }
            
            // alpha_k = |u_k|
            double val = 0;
            for(int i = 0; i < U[k].length; i++)
            {
                val += U[k][i]*U[k][i];
            }
            B[k][k] = Math.sqrt(val);
            
            // u_k = u_k/alpha_k
            U[k] = scale(1/B[k][k], U[k]);
            
            // test for completion and break if done
            if(k+1 == A[0].length)
            {
                break;
            }
            
            // v_k+1 = A* u_k - alpha_k v_k
            V[k+1] = subtract(multiply(transpose(A), U[k]), scale(B[k][k], V[k]));
            
            // Beta_k = |v_k+1|
            val = 0;
            for(int i = 0; i < V[k+1].length; i++)
            {
                val += V[k+1][i]*V[k+1][i];
            }
            B[k+1][k] = Math.sqrt(val);
            
            // v_k+1 = v_k+1/Beta_k
            V[k+1] = scale(1/B[k+1][k], V[k+1]);
        }
    }
    
    public static void print(double[][] A)
    {
        boolean virgin = true;
        
        for(int y = 0; y < A[0].length; y++)
        {
            if(!virgin)
            {
                System.out.println("");
            }
            else
            {
                virgin = false;
            }
            
            System.out.println(A[0][y]);
            for(int x = 1; x < A.length; x++)
            {
                System.out.println("\t" + A[x][y]);
            }
        }
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
