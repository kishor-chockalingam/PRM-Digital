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
import com.hybris.mobile.activity.ProductDetailActivity;
import com.hybris.mobile.factory.barcode.IntentBarcode;
import com.hybris.mobile.loader.RESTLoader;
import com.hybris.mobile.loader.RESTLoaderObserver;
import com.hybris.mobile.loader.RESTLoaderResponse;
import com.hybris.mobile.query.QuerySingleProduct;


public class ProductDetailsIntentBarcodeImpl extends Intent implements IntentBarcode, RESTLoaderObserver
{

	private Activity activity;
	private Handler mHandler;

	/**
	 * Create the intent with the right action
	 * 
	 * @param productCode
	 * @param activity
	 */
	public ProductDetailsIntentBarcodeImpl(String productCode, Activity activity)
	{
		super(activity, ProductDetailActivity.class);
		putExtra(DataConstants.PRODUCT_CODE, productCode);
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

		QuerySingleProduct query = new QuerySingleProduct();
		query.setProductCode(getExtras().getString(DataConstants.PRODUCT_CODE));
		RESTLoader.execute(activity, WebserviceMethodEnums.METHOD_GET_PRODUCT_WITH_CODE, query, this, true, true);
	}

	@Override
	public void onReceiveResult(RESTLoaderResponse restLoaderResponse, WebserviceMethodEnums webserviceEnumMethod)
	{
		if (restLoaderResponse.getCode() == RESTLoaderResponse.SUCCESS)
		{

			switch (webserviceEnumMethod)
			{

			// Product found
				case METHOD_GET_PRODUCT_WITH_CODE:
					mHandler.sendEmptyMessage(BarCodeScannerActivity.MSG_DATA_AVAILABLE);
				default:
					break;
			}

		}
		else if (restLoaderResponse.getCode() == RESTLoaderResponse.ERROR)
		{
			switch (webserviceEnumMethod)
			{
			// No product found
				case METHOD_GET_PRODUCT_WITH_CODE:
					Message msg = new Message();
					msg.what = BarCodeScannerActivity.MSG_DATA_ERROR;
					msg.obj = Hybris.getAppContext().getString(R.string.error_barcode_no_product_found);
					mHandler.sendMessage(msg);
					break;

				default:
					break;
			}
		}

	}
}
