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
package com.hybris.test;

import android.test.AndroidTestCase;

import com.hybris.mobile.data.HYOCCInterface;
import com.hybris.mobile.data.WebService;


public class MiscTests extends AndroidTestCase
{

	private HYOCCInterface webService;

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		Thread.sleep(1000);
		WebService.initWebService(mContext);
		webService = WebService.getWebService();
	}

	@Override
	protected void tearDown() throws Exception
	{
		super.tearDown();
		webService = null;
	}

	public void testGetLanguages() throws Exception
	{
		assertTrue(!webService.languages().isEmpty());
	}


	public void testGetCurrencies() throws Exception
	{
		assertTrue(!webService.currencies().isEmpty());
	}


	public void testGetCountries() throws Exception
	{
		assertTrue(!webService.deliveryCountries().isEmpty());
	}


	public void testGetCardTypes() throws Exception
	{
		assertTrue(!webService.cardTypes().isEmpty());
	}


	public void testGetTitles() throws Exception
	{
		assertTrue(!webService.titles().isEmpty());
	}


	public void testGetStoresFromString() throws Exception
	{
		assertTrue(!webService.stores("tokyo", 0, 20).isEmpty());
	}

	public void testGetStoresFromLocation() throws Exception
	{
		assertTrue(!webService.storesFromLocation(35.65, 139.69, 5000, 0, 0).isEmpty());
	}

}
