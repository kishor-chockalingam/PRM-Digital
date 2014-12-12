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
import android.os.Message;

import com.hybris.mobile.DataConstants;
import com.hybris.mobile.Hybris;
import com.hybris.mobile.R;
import com.hybris.mobile.WebserviceMethodEnums;
import com.hybris.mobile.activity.BarCodeScannerActivity;
import com.hybris.mobile.activity.LoginActivity;
import com.hybris.mobile.activity.OrderDetailActivity;
import com.hybris.mobile.factory.barcode.IntentBarcode;
import com.hybris.mobile.loader.RESTLoader;
import com.hybris.mobile.loader.RESTLoaderObserver;
import com.hybris.mobile.loader.RESTLoaderResponse;
import com.hybris.mobile.query.QueryObjectId;


public class OrderDetailsIntentBarcodeImpl extends Intent implements IntentBarcode, RESTLoaderObserver
{

	private Activity activity;
	private Handler mHandler;

	/**
	 * Create the intent with the right action
	 * 
	 * @param orderId
	 * @param activity
	 */
	public OrderDetailsIntentBarcodeImpl(String orderId, Activity activity)
	{
		super(activity, OrderDetailActivity.class);

		if (!Hybris.isUserLoggedIn())
		{
			setClass(activity, LoginActivity.class);
			putExtra(DataConstants.INTENT_DESTINATION, DataConstants.INTENT_ORDER_DETAILS);
		}

		putExtra(DataConstants.ORDER_ID, orderId);
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
		mHandler = handler;

		if (Hybris.isUserLoggedIn())
		{
			QueryObjectId query = new QueryObjectId();
			query.setObjectId(getExtras().getString(DataConstants.ORDER_ID));
			RESTLoader.execute(activity, WebserviceMethodEnums.METHOD_GET_ORDER, query, this, true, true);
		}
		else
		{
			handler.sendEmptyMessage(BarCodeScannerActivity.MSG_NOT_LOGGED_IN);
		}
	}

	@Override
	public void onReceiveResult(RESTLoaderResponse restLoaderResponse, WebserviceMethodEnums webserviceEnumMethod)
	{
		if (restLoaderResponse.getCode() == RESTLoaderResponse.SUCCESS)
		{
			switch (webserviceEnumMethod)
			{
				case METHOD_GET_ORDER:
					mHandler.sendEmptyMessage(BarCodeScannerActivity.MSG_DATA_AVAILABLE);
					break;

				default:
					break;
			}
		}
		else if (restLoaderResponse.getCode() == RESTLoaderResponse.ERROR)
		{
			switch (webserviceEnumMethod)
			{
				case METHOD_GET_ORDER:
					Message msg = new Message();
					msg.what = BarCodeScannerActivity.MSG_DATA_ERROR;
					msg.obj = Hybris.getAppContext().getString(R.string.error_barcode_no_order_found);
					mHandler.sendMessage(msg);
					break;

				default:
					break;
			}
		}
	}
}
