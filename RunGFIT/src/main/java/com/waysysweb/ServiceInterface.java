//------------------------------------------------------------------------------
//      Compilation Unit Header
//------------------------------------------------------------------------------
//
//  Name:           ServiceInterface.java
//  Author:         William A. Shaffer
//  Package:        com.waysysweb
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
//  Shaffer   27-Sep-2010   File create
//  Shaffer   13-Feb-2011   Updated to correct initialization of webservice
//  Shaffer   11-Apr-2015   Upgraded to use WSI web service
//
//------------------------------------------------------------------------------
//      Package Declaration
//------------------------------------------------------------------------------

package com.waysysweb;

//------------------------------------------------------------------------------
//Import Declarations
//------------------------------------------------------------------------------

/**
 * This class is an abstract class that serves as the parent for subclasses that
 * interface to the Guidewire services. There will be a subclass for each
 * Guidewire product, since the Guidewire SOAP libraries and classes are
 * specific to each product.
 * 
 * @author William Shaffer
 * @version 12-Apr-2015
 * 
 */
abstract public class ServiceInterface {

    // ------------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------------

    /** List of Guidewire products */
    public enum Product {
        /** ClaimCenter */
        CLAIMCENTER,
        /** PolicyCenter */
        POLICYCENTER,
        /** BillingCenter */
        BILLINGCENTER
    }

    /** the user name */
    protected String username;

    /**
     * password
     */
    protected String password;

    /** the url for the web service */
    private static String url = "";

    /** the timeout for the web service */
    protected int timeout;

    /** the Guidewire product being used */
    static protected Product product = Product.CLAIMCENTER;

    // ------------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------------

    /**
     * Create an instance of the class
     */
    public ServiceInterface() {
        username = "";
        password = "";
        timeout = 600; /* default value is 10 minutes */
        return;
    }

    // ------------------------------------------------------------------------------
    // Properties
    // ------------------------------------------------------------------------------

    /**
     * Return the user name for logging into the web service
     * 
     * @return the user name for the web service
     */
    public String getUsername() {
        assert username.length() > 0;
        return username;
    }

    /**
     * Set the user name for logging into the web service
     * 
     * @param value
     *            the value of the username
     */
    public void setUsername(String value) {
        assert value != null;
        assert value.length() > 0;
        username = value;
        return;
    }

    /**
     * Return the password
     * 
     * @return the password for the web service
     */
    public String getPassword() {
        assert password.length() > 0;
        return password;
    }

    /**
     * Set the password
     * 
     * @param value
     *            the new value of the password
     */
    public void setPassword(String value) {
        assert value != null;
        assert value.length() > 0;
        password = value;
        return;
    }

    /**
     * Set the value of the timeout
     * 
     * @param value
     *            the new value of the timeout
     * @throws GfitException
     */
    public void setTimeout(int value) throws GfitException {
        if (timeout > 0) {
            timeout = value;
        } else {
            throw new GfitException("Timeout must be greater than 0 - " + value);
        }
        return;
    }

    /**
     * Return the value of the timeout in seconds
     * 
     * @return the value of the timeout
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * Return the a string with the URL for the web service
     * 
     * @return the URL for the web service
     */
    public static String getURL() {
        assert url.length() > 0;
        return url;
    }

    public static void setURL(String value) {
        assert value != null;
        assert value.length() > 0;
        url = value;
        return;
    }

    /**
     * Set the value of the product.
     * 
     * @param prod
     *            the enumeration specifying the product
     */
    public static void setProduct(Product prod) {
        product = prod;
        return;
    }

    // ------------------------------------------------------------------------------
    // Operation
    // ------------------------------------------------------------------------------

    /**
     * Execute the GfitAPI interface. Return the result of the access.
     * 
     * @param url
     *            the URL for the GfitAPI web service
     * @param testsuite
     *            the directory of the test suite
     * @param reports
     *            the path to the report file
     * @return "true" or "false"
     */
    abstract public String execGfitAPI(String testsuite, String reports);

    // ------------------------------------------------------------------------------
    // Instance Retrieval
    // ------------------------------------------------------------------------------

    /**
     * Return the correct instance of the service interface.
     * 
     * Precondition: product <> null Postconditon: webservice
     * 
     * @return the service interface based on the specified product
     */
    static public ServiceInterface getInterface() {
        assert product != null;
        ServiceInterface webservice = new ServiceInterfaceCC(url);
        return webservice;
    }

    // ------------------------------------------------------------------------------
    // Support functions
    // ------------------------------------------------------------------------------

    /**
     * Log information messages
     * 
     * @param message
     */
    public void logInfo(String message) {
        return;
    }

    /**
     * Log error messages
     * 
     * @param message
     */
    public void logError(String message) {
        return;
    }
}
