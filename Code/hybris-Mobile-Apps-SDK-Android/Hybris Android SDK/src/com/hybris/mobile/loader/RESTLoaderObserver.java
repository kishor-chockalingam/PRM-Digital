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
package com.hybris.mobile.loader;

import com.hybris.mobile.WebserviceMethodEnums;


public interface RESTLoaderObserver
{
	/**
	 * Method to get back the results from the OCC layer
	 * 
	 * @param restLoaderResponse
	 * @param webserviceEnumMethod
	 */
	public void onReceiveResult(RESTLoaderResponse restLoaderResponse, WebserviceMethodEnums webserviceEnumMethod);
}
