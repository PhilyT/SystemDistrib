package com.m1miageprojet.appserveur;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppServeurTest
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppServeurTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppServeurTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testAppServeur()
    {
        assertTrue( true );
    }
}
