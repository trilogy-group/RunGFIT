//------------------------------------------------------------------------------
//      Compilation Unit Header
//------------------------------------------------------------------------------
//
//  Name:           RunGFIT.java
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
//  Shaffer   22-Sep-2010   File create
//  Shaffer   23-Dec-2010   Implemented Enhancement 012: support billing center
//  Shaffer   11-Apr-2015   Converted to use WSI web service standard
//
//------------------------------------------------------------------------------
//      Package Declaration
//------------------------------------------------------------------------------

package com.waysysweb;

//------------------------------------------------------------------------------
//Import Declarations
//------------------------------------------------------------------------------

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

//------------------------------------------------------------------------------
//      Public Class Declaration
//------------------------------------------------------------------------------

/**
 * This class provides a command line interface that can invoke the SuiteRunner
 * for GFIT. The command line is as follows:
 * 
 * java -jar rungfit-8.00.jar -username username -password password -testsuite
 * testsuite -reports reports -url http://url -prop prop -timeout 600
 * 
 * where:
 * 
 * username - the user name under which the client will log in
 * 
 * password - the password for the user
 * 
 * testsuite - the directory where the test suite is stored
 * 
 * reports - the file name for the reports
 * 
 * url - the URL for the Guidewire server
 * 
 * prop - the name of the properites file to use
 * 
 * timeout - the timeout for the Web service (in seconds)
 * 
 * This code requires the following .jar files from the java-api\lib directory
 * for the product:
 * 
 * gw-util.jar gw-axis.jar jaxrpc.jar commons-discovery.jar
 * commons-logging-1.1.1.jar log4j-1.2.15.jar activation.jar mailapi.jar
 * wsdl4j.jar gw-gosu-core-api.jar
 * 
 * This code requires the following .jar files from the soap-api\lib directory
 * for the product:
 * 
 * gw-soap-cc.jar
 * 
 * This code requires the following .jar file from PolicyCenter soap-api\lib
 * directory.
 * 
 * gw-soap-pc.jar
 * 
 * This code requires the following .jar file from BillingCenter soap-api\lib
 * directory.
 * 
 * gw-soap-bc.jar
 * 
 * @author William Shaffer
 * @version 3.06
 * 
 */
public class RunGFIT {
    // ------------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------------

    /** GFIT properties */
    private Properties gfitProperties;

    /** a map with a list of legal properties */
    private Map<String, String> allowedProps;

    /** the service interface for this run */
    private ServiceInterface serviceInterface;

    /** set to 1 if error is encountered. Otherwise, it is zero. */
    static private int errorNum;

    /** default timeout (seconds) for Web service */
    static private final int DEFAULT_TIMEOUT = 600;

    /** the name of the GFIT properties file */
    static private final String GFIT_PROPERTY_FILE = "gfit.properties";

    /** the RunGFIT version */
    static public final String VERSION = "8.00";

    // ------------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------------

    /**
     * Create an instance of this class.
     */
    public RunGFIT() {
        errorNum = 0;
        this.gfitProperties = new Properties();
        this.serviceInterface = null;
        //
        // Load list of legal properties
        allowedProps = new HashMap<String, String>(8);
        allowedProps.put("-username", "username");
        allowedProps.put("-password", "password");
        allowedProps.put("-testsuite", "testsuite");
        allowedProps.put("-reports", "reports");
        allowedProps.put("-url", "url");
        allowedProps.put("-prop", "prop");
        allowedProps.put("-timeout", "timeout");
        return;
    }

    // ------------------------------------------------------------------------------
    // Main Program
    // ------------------------------------------------------------------------------

    /**
     * Launch the program
     * 
     * @param args
     *            an array of strings with arguments
     */
    public static void main(String[] args) {
        errorNum = 0;
        logInfo("Begin GFIT execution - Version " + VERSION);
        RunGFIT webService = new RunGFIT();
        try {
            webService.execute(args);
        } catch (GfitException e) {
            logError(e.getMessage());
        }
        logInfo("End of GFIT execution");
        System.exit(errorNum);
    }

    // ------------------------------------------------------------------------------
    // Operation
    // ------------------------------------------------------------------------------

    /**
     * Execute a test suite
     * 
     * @param args
     *            an array of strings with arguments
     * @exception Exception
     *                thrown if there is an error
     */
    public void execute(String[] args) throws GfitException {
        assert args != null;
        //
        // Initialize the program
        //
        processArgs(args);
        determineProduct();
        //
        // Run the test suite
        //
        execTests();
        return;
    }

    /**
     * Determine the Guidewire product being worked with and initialize the
     * ServiceInterface class.
     * 
     * @throws GfitException
     *             if the product cannot be determined
     */
    public void determineProduct() throws GfitException {
        assert gfitProperties != null;
        String urlString = getProperty("url");
        if (urlString == null) {
            String message = "URL property is not set";
            throw new GfitException(message);
        }
        URL url;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            String message = "Invalid URL - " + urlString;
            throw new GfitException(message);
        }
        String prodString = url.getFile();
        if (prodString.equalsIgnoreCase("/cc")) {
            ServiceInterface.setProduct(ServiceInterface.Product.CLAIMCENTER);
        } else if (prodString.equalsIgnoreCase("/pc")) {
            ServiceInterface.setProduct(ServiceInterface.Product.POLICYCENTER);
        } else if (prodString.equalsIgnoreCase("/bc")) {
            ServiceInterface.setProduct(ServiceInterface.Product.BILLINGCENTER);
        } else {
            String message = "Unsupported product - " + prodString;
            throw new GfitException(message);
        }
        return;
    }

    // ------------------------------------------------------------------------------
    // Test Execution Operations
    // ------------------------------------------------------------------------------

    /**
     * Invoke the run method on the GfitAPI
     * 
     * @throws GfitException
     * 
     */
    protected void execTests() throws GfitException {
        //
        // Get proper service interface
        //
        String message = "";
        String url = getGfitUrl();
        ServiceInterface.setURL(url);
        serviceInterface = ServiceInterface.getInterface();
        int timeout = getTimeout();
        serviceInterface.setTimeout(timeout);
        String result = "false";
        String username = getProperty("username");
        if ((username == null) || (username.length() == 0)) {
            message = "Username is not supplied";
            throw new GfitException(message);
        }
        serviceInterface.setUsername(username);
        String password = getProperty("password");
        if ((password == null) || (password.length() == 0)) {
            message = "Password is not supplied";
            throw new GfitException(message);
        }
        serviceInterface.setPassword(password);
        //
        // Get properties for GFIT API service
        //
        String reports = getProperty("reports");
        String testSuite = getProperty("testsuite");
        //
        // Check test suite property
        //
        if (testSuite == null) {
            logError("Testsuite property is not set");
        }
        //
        // Check reports property
        //
        else if (reports == null) {
            logError("Reports property is not set");
        }
        //
        // Execute test suite
        //
        else {
            result = serviceInterface.execGfitAPI(testSuite, reports);
            if (result.equals("false")) {
                logInfo("Test completed with errors.  See test log.");
                errorNum = 1;
            } else {
                logInfo("Test completed with no errors.");
            }
        }
        return;
    }

    /**
     * Return the URL for the GFIT API. This URL will be the system URL with
     * this string appended: "/soap/GfitAPI?wsdl"
     * 
     * @return the URL for the GFIT API web service WSDL
     * @throws GfitException
     */
    public String getGfitUrl() throws GfitException {
        String gfitString = getProperty("url");
        if (gfitString != null) {
            gfitString = gfitString + "/ws/castlebay/GfitAPI";
        } else {
            String message = "Web service URL property must be set";
            throw new GfitException(message);
        }
        return gfitString;
    }

    // ------------------------------------------------------------------------------
    // Property Handling
    // ------------------------------------------------------------------------------

    /**
     * Process the arguments. Properties in the gfit.properties file are the
     * default. Users can override the defaults with arguments on the command
     * line.
     * 
     * This function guarantees that, if it completes, the properties file has
     * been processed and the command line arguments have been processed.
     * However, there is no guarantee that any particular property has been set
     * properly.
     * 
     * @param args
     *            an array of strings with arguments
     * @throws GfitException
     *             if property file cannot be loaded
     */
    public void processArgs(String[] args) throws GfitException {
        //
        // precondition: args != null
        //
        String gfitPropertiesFile = getPropertyFile(args);
        loadProperties(gfitPropertiesFile);
        checkProperties(this.gfitProperties);
        processCommandArgs(args);
        //
        // postcondition: gfitProperties != null and
        // properties file has been read and
        // command line arguments have been processed
        return;
    }

    /**
     * Return the name of the properties file based on scanning the list of
     * arguments.
     * 
     * @param args
     *            an array of strings with arguments
     * @return the name of the GFIT properties file
     */
    public String getPropertyFile(String[] args) {
        String result = GFIT_PROPERTY_FILE;
        //
        // Search for "-prop" property
        //
        int count = args.length;
        for (int i = 0; i < count; i++) {
            if (args[i].equals("-prop")) {
                //
                // Is there a following string in the array?
                //
                i++;
                if (i < count)
                    result = args[i];
                break;
            }
        }
        //
        // Postcondition: result != null
        //
        return result;
    }

    /**
     * Process command line arguments. Arguments alternate between a property
     * name and the property value.
     * 
     * @param args
     *            an array of strings with arguments
     * @throws GfitException
     */
    public void processCommandArgs(String[] args) throws GfitException {
        int count = args.length;
        String propName;
        String value;

        for (int i = 0; i < count; i += 2) {
            if (allowedProps.containsKey(args[i])) {
                propName = allowedProps.get(args[i]);
                value = args[i + 1];
                value = value.trim();
                gfitProperties.setProperty(propName, value);
            } else {
                String message = "Unknown property - " + args[i];
                throw new GfitException(message);
            }
        }
        //
        // Postcondition: all legal online arguments have been processed
        //
        return;
    }

    /**
     * Return the value of a named property.
     * 
     * @param name
     *            the name of the property
     * @return a string with the value of the property
     */
    public String getProperty(String name) {
        assert this.gfitProperties != null;
        assert name != null;

        String value = gfitProperties.getProperty(name);
        return value;
    }

    /**
     * Load properties from a property file.
     * 
     * @param fileName
     *            the name of the property file
     * @throws GfitException
     *             when properties cannot be loaded
     */
    public void loadProperties(String fileName) throws GfitException {
        //
        // Precondition: fileName != null
        //
        FileInputStream file = openPropertyFile(fileName);
        if (file != null) {
            try {
                gfitProperties.load(file);
            } catch (Exception e) {
                String message = "Could not load properties file - " + fileName;
                throw new GfitException(message);
            } finally {
                try {
                    file.close();
                } catch (IOException e) {
                    // do nothing of close throws an exception
                }
            }
        } else {
            throw new GfitException("Unable to get properties file.");
        }
        //
        // Postcondition: props != null and
        // properties file has been read
        //

        return;
    }

    /**
     * Return an instance of file input stream for a properties file.
     * 
     * @param fileName
     *            the name of the properties file
     * @return an instance of file input stream
     */
    public FileInputStream openPropertyFile(String fileName) {
        FileInputStream file;
        try {
            file = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            printError(e, "Cannot find property file - " + fileName);
            file = null;
        } catch (SecurityException e) {
            printError(e, "Cannot open existing property file - " + fileName);
            file = null;
        }
        return file;
    }

    /**
     * Check the properties in the property file to insure that they are all
     * recognized properties.
     * 
     * @param properties
     *            a property set
     * @throws GfitException
     *             if there is a property that is not restricted
     */
    public void checkProperties(Properties properties) throws GfitException {
        assert properties != null;
        String propName;
        Enumeration<String> props = properties.keys();
        while (props.hasMoreElements()) {
            propName = props.nextElement();
            if (!allowedProps.containsValue(propName)) {
                String message = "Unrecognized property in property file - "
                        + propName;
                throw new GfitException(message);
            }
        }
        return;
    }

    /**
     * Return the value of the timeout as an integer with units of seconds
     * 
     * @return the value of the timeout
     */
    public int getTimeout() {
        int result = DEFAULT_TIMEOUT;
        String prop = getProperty("timeout");
        if (prop != null) {
            try {
                result = Integer.parseInt(prop);
            } catch (NumberFormatException e) {
                String msg = "Illegal value of timeout - " + prop;
                logWarning(msg);
                result = DEFAULT_TIMEOUT;
            }
        } else
            result = DEFAULT_TIMEOUT;
        if (result <= 0) {
            logWarning("Timeout must be greater than 0 - " + prop);
            result = DEFAULT_TIMEOUT;
        }
        return result;
    }

    // ------------------------------------------------------------------------------
    // Supporting Operations
    // ------------------------------------------------------------------------------

    /**
     * Print out an error if an exception occurs. Set errorNum to 1.
     * 
     * @param e
     *            the exception
     * @param initMessage
     *            the initial message
     */
    protected void printError(Exception e, String initMessage) {
        String message = e.getMessage();
        if (isLoggerConfigured()) {
            logError(initMessage);
            logError(message);
        } else {
            System.out.println(initMessage);
            System.out.println(message);
        }
        // e.printStackTrace();
        errorNum = 1;
    }

    // ------------------------------------------------------------------------------
    // Logging Operations
    // ------------------------------------------------------------------------------

    /**
     * Return true if the logger is configured
     * 
     * @return true if the logger is configured
     */
    private static boolean isLoggerConfigured() {
        return true;
    }

    /**
     * Log information message
     * 
     * @param message
     */
    private static void logInfo(String message) {
        assert message != null;
        System.out.println("Info: " + message);
        return;
    }

    /**
     * Log warning message
     */
    private static void logWarning(String message) {
        assert message != null;
        System.out.println("Warning: " + message);
    }

    /**
     * Log error messages
     * 
     * @param message
     */
    private static void logError(String message) {
        assert message != null;
        System.out.println("Error: " + message);
        return;
    }
}
