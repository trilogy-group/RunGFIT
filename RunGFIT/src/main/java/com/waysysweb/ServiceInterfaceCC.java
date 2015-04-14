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
//  Shaffer   27-Sep-2010   File create
//  Shaffer   11-Apr-2015   Adapted to WSI web services
//
//------------------------------------------------------------------------------
//      Package Declaration
//------------------------------------------------------------------------------

package com.waysysweb;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import com.sun.xml.internal.ws.client.BindingProviderProperties;
import com.waysysweb.gfit.GfitAPI;
import com.waysysweb.gfit.GfitAPIPortType;
import com.waysysweb.gfit.WsiAuthenticationException_Exception;

//------------------------------------------------------------------------------
//Import Declarations
//------------------------------------------------------------------------------

/**
 * This class is concrete class that that is an interface to the SOAP libraries
 * for ClaimCenter.
 * 
 * @author William Shaffer
 * @version 11-Apr-2015
 * 
 */
public class ServiceInterfaceCC extends ServiceInterface {

    // ------------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------------

    private GfitAPI gfitAPI;

    private final QName serviceName;
    private final URL serviceUrl;

    // ------------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------------

    /**
     * Create an instance of the class
     * 
     * @param url
     *            the URL for the web service
     */
    public ServiceInterfaceCC(String url) {
        super();
        serviceName = new QName("http://waysysweb.com/gfit", "GfitAPI");
        try {
            serviceUrl = new URL(url);
            gfitAPI = new GfitAPI(serviceUrl, serviceName);
        } catch (MalformedURLException e) {
            String message = "URL for GfitAPI service is incorrect: " + url;
            System.out.println(message);
            throw new AssertionError(message);
        }
        return;
    }

    // ------------------------------------------------------------------------------
    // Operation
    // ------------------------------------------------------------------------------

    /**
     * Execute the GfitAPI interface. Return the result of the access.
     * 
     * @param testsuite
     *            the directory of the test suite
     * @param reports
     *            the path to the report file
     * @return "true" or "false"
     * @throws GfitException 
     */
    public String execGfitAPI(String testsuite, String reports) throws GfitException {
        GfitAPIPortType port = gfitAPI.getGfitAPISoap11Port();
        setCredentials(port);

        String result = "true";
        try {
            result = port.run(testsuite, reports);
        } catch (WsiAuthenticationException_Exception e) {
            String message = "Bad username or password. ";
            message += "username=" + getUsername();
            message += "password=" + getPassword();
            throw new GfitException(message);
        }
        return result;
    }

    /**
     * Set the credentials and timeout for the web service
     * 
     * @param port the port for the web service
     */
    private void setCredentials(GfitAPIPortType port) {
        String timeout = String.valueOf(getTimeout());
        BindingProvider bp = (BindingProvider) port;
        Map<String, Object> requestContext = bp.getRequestContext();
        requestContext.put(BindingProvider.USERNAME_PROPERTY, getUsername());
        requestContext.put(BindingProvider.PASSWORD_PROPERTY, getPassword());
        requestContext.put(BindingProviderProperties.CONNECT_TIMEOUT, timeout);
        requestContext.put(BindingProviderProperties.REQUEST_TIMEOUT, timeout);
        return;
    }

}
