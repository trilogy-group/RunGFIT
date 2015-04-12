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
//  Shaffer   11-Apr-2015   Test created
//
//------------------------------------------------------------------------------
//      Package Declaration
//------------------------------------------------------------------------------

package com.waysysweb;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.waysysweb.ServiceInterface.Product;

public class WebServiceTest {

    // --------------------------------------------------------------------------
    // Fields
    // --------------------------------------------------------------------------

    private ServiceInterface service;

    private static final String TEST_SUITE = "\\git\\GfitSupport\\ClaimCenter\\unit_claim7_auto";
    private static final String REPORTS = "\\proj\\reports\\report_create_claim";
    private static final String SERVICE_URL = "http://localhost:8080/cc/ws/castlebay/GfitAPI";

    // --------------------------------------------------------------------------
    // Support Functions
    // --------------------------------------------------------------------------

    @Before
    public void setUp() {
        ServiceInterface.setProduct(Product.CLAIMCENTER);
        ServiceInterface.setURL(SERVICE_URL);
        service = ServiceInterface.getInterface();
        service.setPassword("gw");
        service.setUsername("su");
        return;
    }

    @After
    public void tearDown() {
        service = null;
        return;
    }

    // --------------------------------------------------------------------------
    // Tests
    // --------------------------------------------------------------------------

    @Test
    public void can_access_web_service() {
        String result = service.execGfitAPI(TEST_SUITE, REPORTS);
        assertEquals("Bad result of service call", "false", result);
        return;
    }
}
