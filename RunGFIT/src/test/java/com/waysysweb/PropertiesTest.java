//------------------------------------------------------------------------------
//      Compilation Unit Header
//------------------------------------------------------------------------------
//
//  Name:           ServiceInterfaceCC.java
//  Author:         William A. Shaffer
//  Package:        com.waysysweb.gfit
//
//  Copyright (c) 2010, 2015 Waysys, LLC. All Rights Reserved.
//
//
//  Waysys MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
//  THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
//  TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
//  PARTICULAR PURPOSE, OR NON-INFRINGEMENT. Waysys SHALL NOT BE LIABLE FOR
//  ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
//  DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
//
//  For further information, contact Waysys LLC at wshaffer@waysysweb.com
//  or 800-622-5315 (USA).
//
//------------------------------------------------------------------------------
//      Maintenance History
//------------------------------------------------------------------------------
//
//  Person    Date          Change
//  ------    -----------   ----------------------------------------------------
//
//  Shaffer   12-Apr-2015   Class created
//
//------------------------------------------------------------------------------
//      Package Declaration
//------------------------------------------------------------------------------

package com.waysysweb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This class tests the Properties class.
 * 
 * @author William Shaffer
 * @version 12-Apr-2015
 *
 */
public class PropertiesTest {
    
    // --------------------------------------------------------------------------
    // Fields
    // --------------------------------------------------------------------------
    
    private Properties properties;
    
    // --------------------------------------------------------------------------
    // Support Functions
    // --------------------------------------------------------------------------

    @Before
    public void setUp() {
        properties = new Properties();
        return;
    }

    @After
    public void tearDown() {
        properties = null;
        return;
    }

    // --------------------------------------------------------------------------
    // Tests
    // --------------------------------------------------------------------------
    
    /**
     * Test that a value can be added and retrieved.
     */
    @Test
    public void can_set_property() {
        properties.setProperty("url", "abc");
        assertEquals("Bad count ", 1, properties.size());
        assertEquals("Bad return value", "abc", properties.getProperty("url"));
        return;
    }
    
    /**
     * Test that a property file can be loaded
     * @throws IOException 
     */
    @Test
    public void can_load_property_file() throws IOException {
        String fileName =  "./src/main/resources/gfit.properties";
        FileInputStream file = new FileInputStream(fileName);
        properties.load(file);
        assertEquals("Bad load count", 6, properties.size());
        assertEquals("Bad url", "http://localhost:8080/cc", 
                properties.getProperty("url"));
        return;
    }
    
    /**
     * Test of the keys enumeration
     * @throws IOException 
     */
    @Test
    public void can_use_enumeration() throws IOException {
        String fileName =  "./src/main/resources/gfit.properties";
        FileInputStream file = new FileInputStream(fileName);
        properties.load(file);
        Enumeration<String> enumerKey = properties.keys();
        while (enumerKey.hasMoreElements()) {
            String key = enumerKey.nextElement();
            String value = properties.getProperty(key);
            assertTrue("bad key", !value.equals(""));
        }
    }
}
