/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.Frame;
import java.awt.image.BufferedImage;
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
        double[][] A = null;
        double[][] U = null;
        double[][] B = null;
        double[][] V = null;
        Driver.bidiagonalize(A, U, B, V);
    }
}
