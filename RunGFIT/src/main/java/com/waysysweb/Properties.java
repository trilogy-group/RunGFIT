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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map.Entry;

/**
 * This class is a substitute for the regular Java Properties class which
 * seems to be defective.
 * 
 * @author William Shaffer
 * @version 12-Apr-2015
 *
 */
public class Properties {
    
    // ------------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------------
    
    private List<String> keys;
    private List<String> values;
    
    // ------------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------------
    
    public Properties() {
        keys = new ArrayList<String>();
        values = new ArrayList<String>();
        return;
    }

    // ------------------------------------------------------------------------------
    // Operations
    // ------------------------------------------------------------------------------
    
    /**
     * Return the number of properties
     * 
     * @return number of properties
     */
    public int size() {
        return keys.size();
    }
    
    /**
     * Set the value of a property
     * 
     * @param key the key to the property
     * @param value the value for the property
     */
    public void setProperty(String key, String value) {
        assert key != null;
        assert value != null;
        int index = 0;
        key = key.trim();
        value = value.trim();
        //
        // See if the property is already here
        //
        for (String item : keys) {
            if (item.equals(key)) {
                values.set(index, value);
                return;
            }
            index++;
        }
        //
        // Add the property if not already there
        //
        keys.add(key);
        values.add(value);
        return;
    }
    
    /**
     * Get the value of the property.  Return null if key is not found.
     * 
     * @param key the key of the properties
     * @return the value of the property
     */
    public String getProperty(String key) {
        String value = null;
        key = key.trim();
        int index = 0;
        for (String item : keys) {
            if (item.equals(key)) {
                value = values.get(index);
                break;
            }
            index++;
        }
        return value;
    }
    
    /**
     * Load properties
     * 
     * @param input an input file stream
     * @throws IOException 
     */
    public void load(FileInputStream stream) throws IOException {
        java.util.Properties temp = new java.util.Properties();
        temp.load(stream);
        Enumeration<Object> enumerKey =  temp.keys();
        while (enumerKey.hasMoreElements()) {
            String key = (String) enumerKey.nextElement();
            String value = temp.getProperty(key);
            //System.out.println("key=" + key + " value=" + value);
            setProperty(key, value);
        }
        return;
    }
    
    /**
     * Return an enumeration of the keys
     * 
     * @return an enumeration
     */
    public Enumeration<String> keys() {
        return Collections.enumeration(keys);
    }
}
