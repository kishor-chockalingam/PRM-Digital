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
package com.hybris.mobile.factory.barcode.impl;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;

import com.hybris.mobile.DataConstants;
import com.hybris.mobile.activity.BarCodeScannerActivity;
import com.hybris.mobile.activity.StoreLocatorActivity;
import com.hybris.mobile.factory.barcode.IntentBarcode;


public class StoreLocatorIntentBarcodeImpl extends Intent implements IntentBarcode
{

	private Activity activity;

	/**
	 * Create the intent with the right action
	 * 
	 * @param longitude
	 * @param latitude
	 * @param radius
	 * @param activity
	 */
	public StoreLocatorIntentBarcodeImpl(String longitude, String latitude, String radius, Activity activity)
	{
		super(activity, StoreLocatorActivity.class);
		putExtra(DataConstants.STORE_LOCATOR_LAT, latitude);
		putExtra(DataConstants.STORE_LOCATOR_LONG, longitude);
		putExtra(DataConstants.STORE_LOCATOR_RADIUS_VALUE, radius);
		this.activity = activity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.mobile.factory.IntentBarcode#startActivity()
	 */
	@Override
	public void startActivity()
	{
		activity.startActivity(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.mobile.factory.barcode.IntentBarcode#checkDataAvailability(android.os.Handler)
	 */
	@Override
	public void checkDataAvailability(Handler handler)
	{
		// Nothing to check
		handler.sendEmptyMessage(BarCodeScannerActivity.MSG_DATA_AVAILABLE);
	}
}
