/*******************************************************************************
 * [y] hybris Platform
 *  
 *   Copyright (c) 2000-2013 hybris AG
 *   All rights reserved.
 *  
 *   This software is the confidential and proprietary information of hybris
 *   ("Confidential Information"). You shall not disclose such Confidential
 *   Information and shall use it only in accordance with the terms of the
 *   license agreement you entered into with hybris.
 ******************************************************************************/
package com.hybris.mobile.utility;

import java.net.CookieHandler;
import java.net.CookieManager;

public class CookieUtils {

	public static void setup() {
	    CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
    }

	public static void clearCookies() {
		CookieManager cookieManager = (CookieManager) CookieHandler.getDefault();
		cookieManager.getCookieStore().removeAll();
	    }
	
}
