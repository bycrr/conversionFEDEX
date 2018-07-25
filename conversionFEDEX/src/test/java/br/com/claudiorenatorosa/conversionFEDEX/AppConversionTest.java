package br.com.claudiorenatorosa.conversionFEDEX;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AppConversionTest extends TestCase {
	
    public AppConversionTest( String testName )  {
        super( testName );
    }

    public static Test suite() {
        return new TestSuite( AppConversionTest.class );
    }

    public void testApp() {
        assertTrue( true );
    }
}
