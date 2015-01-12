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
package com.hybris.mobile.activity;

import org.apache.commons.lang3.StringUtils;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.accenture.hybris.mobile.beacons.BeaconBgService;
import com.accenture.hybris.mobile.beacons.Constants;
import com.hybris.mobile.DataConstants;
import com.hybris.mobile.Hybris;
import com.hybris.mobile.InternalConstants;
import com.hybris.mobile.R;
import com.hybris.mobile.factory.barcode.IntentBarcode;
import com.hybris.mobile.factory.barcode.IntentBarcodeFactory;


/**
 * Activity for the barcode scanner
 */
public class BarCodeScannerActivity extends HybrisActivity implements Callback
{
	private final String TAG = BarCodeScannerActivity.class.getSimpleName();

	public static final int MSG_DATA_ERROR = 1;
	public static final int MSG_DATA_AVAILABLE = 2;
	public static final int MSG_NOT_LOGGED_IN = 3;
	public static final int MSG_CANCEL_SCAN = 4;
	private static final String SCAN_RESULT = "SCAN_RESULT";
	private static final String SCAN_RESULT_FORMAT = "SCAN_RESULT_FORMAT";

	// We keep a static boolean indicating whether or not the scanner is running
	private boolean isScannerRunning = false;

	// The main object for recognizing a displaying barcodes.
	private Handler mHandler = new Handler(this);
	private IntentBarcode mIntentBarcode;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		// Launching the scanner
		launchScannerIntent();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode == RESULT_OK)
		{
			String contents = data.getStringExtra(SCAN_RESULT);
			String format = data.getStringExtra(SCAN_RESULT_FORMAT);

			Log.i(TAG, "Contents: " + contents);
			Log.i(TAG, "format: " + format);
			String isUseSpecificBaseUrl = Hybris.getSharedPreferenceString(InternalConstants.KEY_PREF_TOGGLE_SPECIFIC_BASE_URL); 
			
			
			if(StringUtils.equals(isUseSpecificBaseUrl, String.valueOf(false))) {
				Log.i(TAG, "calling default handler for url: " + contents);
			// We run the task to check the format and data availability of the barcode scanned
				new CheckBarcodeFormatAndValueTask().execute(contents, format);
			} else {
				isScannerRunning = false;
				WebView myWebView = new WebView(this);
				myWebView = (WebView) findViewById(R.layout.app_web_view);
				Log.i(TAG, "calling webview with url: " + contents);
				myWebView.loadUrl(contents);
				WebSettings webSettings = myWebView.getSettings();
				webSettings.setJavaScriptEnabled(true);
			}
		}
		else if (resultCode == RESULT_CANCELED)
		{
			Message msg = new Message();
			msg.what = BarCodeScannerActivity.MSG_CANCEL_SCAN;
			mHandler.sendMessage(msg);
		}
	}

	/**
	 * Launch the scanner intent
	 */
	private void launchScannerIntent()
	{
		if (!isScannerRunning)
		{
			isScannerRunning = true;
			Intent scannerIntent = new Intent("com.hybris.mobile.SCAN");
			startActivityForResult(scannerIntent, DataConstants.QR_RESULT_CODE);
		}
	}

	/**
	 * Construct the intent that returns the values to the activity that called the Barcode Scanner Activity
	 */
	private void launchIntent()
	{
		// Launching the intent associated with the scanned barcode
		mIntentBarcode.startActivity();
	}

	/**
	 * Show an error message
	 * 
	 * @param errorMessage
	 */
	private void showErrorMessage(final String errorMessage)
	{
		// We resume the scanning activity before
		launchScannerIntent();

		// Displaying the error message
		Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean handleMessage(Message msg)
	{

		isScannerRunning = false;

		switch (msg.what)
		{

		// Cancelling the scan, we finish the activity
			case MSG_CANCEL_SCAN:
				finish();
				break;

			// Error retrieving the data
			case MSG_DATA_ERROR:
				showErrorMessage((String) msg.obj);
				break;

			// Data available, we can construct the intent and redirect the user
			case MSG_DATA_AVAILABLE:
			case MSG_NOT_LOGGED_IN:
				launchIntent();
				break;

		}
		return false;
	}

	/**
	 * This asynchronous task check the barcode consistency with our system: format recognition and value matching
	 * something
	 */
	private class CheckBarcodeFormatAndValueTask extends AsyncTask<String, Void, Void>
	{

		@Override
		protected Void doInBackground(String... params)
		{

			// We try to match the barcode with our intern implementation
			mIntentBarcode = IntentBarcodeFactory.getIntent(params[0], params[1], BarCodeScannerActivity.this);

			// The barcode is recognized by the application, we check the data availability
			if (mIntentBarcode != null)
			{
				// We check the availability of the data associated with the barcode
				mIntentBarcode.checkDataAvailability(mHandler);
			}
			// The barcode is not covered yet by the application
			else
			{
				Message msg = new Message();
				msg.what = BarCodeScannerActivity.MSG_DATA_ERROR;
				msg.obj = Hybris.getAppContext().getString(R.string.error_barcode_no_action, params[0]);
				Log.i(TAG,"barcode not covered by application: " + msg.obj);
				mHandler.sendMessage(msg);
			}

			return null;
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();

			// We display the loading view at the beginning of the task
			//			showLoadingDialog(true);
		}

		@Override
		protected void onPostExecute(Void result)
		{
			super.onPostExecute(result);
		}

	}

}
