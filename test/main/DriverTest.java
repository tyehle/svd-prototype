/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.Frame;
import java.awt.image.BufferedImage;
import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Tobin
 */
public class DriverTest
{
    final double [][] fat = new double[][] {{1, 4}, {2, 5}, {3, 6}};
    final double [][] tall = new double[][] {{1, 3, 5}, {2, 4, 6}};
    final double [][] square = new double[][] {{1, 3}, {2, 4}};
    
    final double delta = .0001;
        
    public DriverTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }

    /**
     * Test of transpose method, of class Driver.
     */
    @Test
    public void testTranspose()
    {
        System.out.println("transpose");
        double[][] expResult = new double[][] {{1, 2}, {3, 4}};
        double[][] result = Driver.transpose(square);
        assertArrayEquals(expResult, result);
        
        expResult = new double[][] {{1, 2, 3}, {4, 5, 6}};
        result = Driver.transpose(fat);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of scale method, of class Driver.
     */
    @Test
    public void testScale()
    {
        System.out.println("scale");
        double c = 4;
        double[] A = new double[] {1, 2};
        double[] expResult = new double[] {4, 8};
        double[] result = Driver.scale(c, A);
        assertArrayEquals(expResult, result, delta);
        
        c = 0;
        expResult = new double[] {0, 0};
        result = Driver.scale(c, A);
        assertArrayEquals(expResult, result, delta);
        
        c = -2;
        expResult = new double[] {-2, -4};
        result = Driver.scale(c, A);
        assertArrayEquals(expResult, result, delta);
    }

    /**
     * Test of multiply method, of class Driver.
     */
    @Test
    public void testMultiply_doubleArrArr_doubleArr()
    {
        System.out.println("multiply");
        double[][] A = square;
        double[] x = new double[] {1, 5};
        double[] expResult = new double[] {11, 23};
        double[] result = Driver.multiply(A, x);
        assertArrayEquals(expResult, result, delta);
    }

    /**
     * Test of multiply method, of class Driver.
     */
    @Test
    public void testMultiply_doubleArrArr_doubleArrArr()
    {
        System.out.println("multiply");
        double[][] A = tall;
        double[][] B = square;
        double[][] expResult = new double[][] {{7, 15, 23}, {10, 22, 34}};
        double[][] result = Driver.multiply(A, B);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of subtract method, of class Driver.
     */
    @Test
    public void testSubtract_doubleArr_doubleArr()
    {
        System.out.println("subtract");
        double[] A = new double[] {1, 4};
        double[] B = new double[] {4, 2};
        double[] expResult = new double[] {-3, 2};
        double[] result = Driver.subtract(A, B);
        assertArrayEquals(expResult, result, delta);
    }

    /**
     * Test of subtract method, of class Driver.
     */
    @Test
    public void testSubtract_doubleArrArr_doubleArrArr()
    {
        System.out.println("subtract");
        double[][] A = tall;
        double[][] B = new double[][] {{2, 3, 5}, {2, 1, 4}};
        double[][] expResult = new double[][] {{-1, 0, 0}, {0, 3, 2}};
        double[][] result = Driver.subtract(A, B);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of bidiagonalize method, of class Driver.
     */
    @Test
    public void testBidiagonalize()
    {
        System.out.println("bidiagonalize");
        double[][] A = tall;
        double[][] U = new double[2][3];
        double[][] B = new double[2][2];
        double[][] V = new double[2][2];
        Driver.bidiagonalize(A, U, B, V);
        
        double[][] f = Driver.multiply(Driver.multiply(U, B), V);
        assertDoubleArrayEquals(A, f, delta);
        
        for(int i = 1; i < 200; i++)
        {
            A = generate(i);
            U = new double[i][i];
            B = new double[i][i];
            V = new double[i][i];
            Driver.bidiagonalize(A, U, B, V);
            double[][] delta = Driver.subtract(Driver.multiply(Driver.multiply(U, B), V), A);
            double maxD = 0;
            for(double[] arr : delta)
            {
                for(double d : arr)
                {
                    d = Math.abs(d);
                    if(d > maxD)
                    {
                        maxD = d;
                    }
                }
            }
            System.out.println(i+"\t"+maxD);
        }
    }
    
    private static double[][] generate(int n)
    {
        Random r = new Random();
        double[][] out = new double[n][n];
        for (int i = 0; i < out.length; i++)
        {
            for (int j = 0; j < out[i].length; j++)
            {
                out[i][j] = r.nextDouble() * 255;
            }
        }
        return out;
    }
    
    public static void assertDoubleArrayEquals(double[][] expected, double[][] actual, double delta)
    {
        if(expected.length != actual.length)
        {
            fail("Array dimension mismatch");
        }
        
        for(int i = 0; i < expected.length; i++)
        {
            assertArrayEquals(expected[i], actual[i], delta);
        }
    }
}
