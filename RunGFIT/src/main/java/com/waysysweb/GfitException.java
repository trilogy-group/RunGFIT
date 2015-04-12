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
//  Shaffer   11-Apr-2015   Class created
//
//------------------------------------------------------------------------------
//      Package Declaration
//------------------------------------------------------------------------------

package com.waysysweb;

/**
 * This class is a general exception class to using in the RunGFIT program.
 * 
 * @author William Shaffer
 * @version 12-Apr-2015
 *
 */
public class GfitException extends Exception {

	/**
	 * serial id
	 */
	private static final long serialVersionUID = -9053421099407402551L;

	// ------------------------------------------------------------------------------
	// Package Declaration
	// ------------------------------------------------------------------------------
	
	/**
	 * Create an instance of this class
	 * 
	 * @param message the error message associated with this exception
	 */
	public GfitException(String message) {
		super(message);
		return;
	}
}
