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

import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.provider.Settings;

import com.hybris.mobile.Hybris;
import com.hybris.mobile.R;
import com.hybris.mobile.logging.LoggingUtils;


public class GeolocationUtil implements LocationListener, Runnable
{
	private static final String LOG_TAG = GeolocationUtil.class.getSimpleName();
	private LocationManager mLocationManager;
	private int mTrial = 0;
	private int mMsgHandlerSuccess;
	private int mMsgHandlerError;
	private Handler mHandler;

	// Handler and thread to run the task
	private Handler mWorkerHandler;
	private HandlerThread mWorkerHandlerThread = new HandlerThread("Worker Thread");
	private boolean mStarted = false;
	private boolean mSuccess = false;

	/**
	 * Private constructor, the Geolocation must be launched with GeolocationUtil.getLocation() static method
	 * 
	 * @param handler
	 * @param msgHandlerSuccess
	 * @param msgHandlerError
	 */
	private GeolocationUtil(Handler handler, int msgHandlerSuccess, int msgHandlerError)
	{
		super();
		mWorkerHandlerThread.start();
		mWorkerHandler = new Handler(mWorkerHandlerThread.getLooper());
		mHandler = handler;
		mMsgHandlerSuccess = msgHandlerSuccess;
		mMsgHandlerError = msgHandlerError;

	}

	/**
	 * Get the location of a user and return the result with an handler and the specified message id
	 * 
	 * @param handler
	 * @param msgHandlerSuccess
	 * @param msgHandlerError
	 */
	public static void getLocation(Handler handler, int msgHandlerSuccess, int msgHandlerError)
	{
		GeolocationUtil geolocationUtil = new GeolocationUtil(handler, msgHandlerSuccess, msgHandlerError);
		geolocationUtil.mWorkerHandler.post(geolocationUtil);
	}

	/**
	 * Method launching the service to geolocate the user
	 */
	private void geolocateUser()
	{

		// If the service is not started, we launch it
		if (!mStarted)
		{
			mStarted = true;
			mLocationManager = (LocationManager) Hybris.getAppContext().getSystemService(Context.LOCATION_SERVICE);

			Criteria criteria = new Criteria();
			criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
			criteria.setAccuracy(Criteria.NO_REQUIREMENT);

			// Listening to all the geolocation providers configurer in the AndroidManifest file
			List<String> providers = mLocationManager.getProviders(criteria, true);
			if (providers != null && !providers.isEmpty())
			{
				for (String provider : providers)
				{
					mLocationManager.requestLocationUpdates(provider, 0, 0, this);
				}
			}
			// No provider found
			else
			{
				mHandler.sendEmptyMessage(mMsgHandlerError);
			}
		}

	}

	@Override
	public void onLocationChanged(final Location location)
	{
		// Stoping to listen to the user location
		mLocationManager.removeUpdates(this);
		mSuccess = true;

		// Sending the message via the handler
		Message msg = new Message();
		msg.what = mMsgHandlerSuccess;
		msg.obj = location;
		mHandler.sendMessage(msg);
	}

	@Override
	public void onStatusChanged(final String provider, final int status, final Bundle extras)
	{
	}

	@Override
	public void onProviderEnabled(String provider)
	{
	}

	@Override
	public void onProviderDisabled(String provider)
	{
	}

	/**
	 * Method to call the geolocation process
	 */
	@Override
	public void run()
	{

		// If we polled more than X seconds, we stop the geolocation service and send an update via the handler
		if (mTrial > getGeolocationMaxSecondsTrial() && !mSuccess)
		{

			// Stop worker thread
			mWorkerHandlerThread.getLooper().quit();

			// Trying to get the last known location
			Location lastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

			if (lastKnownLocation != null)
			{
				onLocationChanged(lastKnownLocation);
			}
			// No location at all
			else
			{
				mLocationManager.removeUpdates(this);
				mHandler.sendEmptyMessage(mMsgHandlerError);
			}

		}
		// We continue trying to geolocate the user
		else if (!mSuccess)
		{
			geolocateUser();
			mTrial++;

			// The process is updated every sec
			mWorkerHandler.postDelayed(this, 1000);
		}

	}

	/**
	 * Return the number of second to poll the geolocation service
	 * 
	 * @return
	 */
	private static int getGeolocationMaxSecondsTrial()
	{
		return Integer.valueOf(Hybris.getAppContext().getString(R.string.geolocation_max_seconds_trial));
	}

	/**
	 * Default radius to use for geolocation on the application
	 * 
	 * @return
	 */
	public static int getDefaultRadius()
	{
		return Integer.valueOf(Hybris.getAppContext().getString(R.string.geolocation_default_radius));
	}

	/**
	 * Return true if the device is configured to enable geolocation
	 * 
	 * @return
	 */
	public static boolean isLocationActivated()
	{
		LocationManager locationManager = (LocationManager) (LocationManager) Hybris.getAppContext().getSystemService(
				Context.LOCATION_SERVICE);
		boolean isGps = false;
		boolean isNetwork = false;

		try
		{
			isGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_TAG, e.getLocalizedMessage(), null);
		}

		try
		{
			isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_TAG, e.getLocalizedMessage(), null);
		}

		return isGps || isNetwork;
	}

	/**
	 * Show a dialog notifying that the location services are disabled, and offering the choice to the user to jump into
	 * the location settings
	 * 
	 * @param context
	 */
	public static void showDialogEnableLocationServices(final Context context)
	{
		Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle(context.getResources().getString(R.string.location_services_not_enabled_title_dialog));
		dialog.setMessage(context.getResources().getString(R.string.location_services_not_enabled_msg_dialog));
		dialog.setPositiveButton(R.string.location_services_not_enabled_msg_button_enable, new DialogInterface.OnClickListener()
		{

			@Override
			public void onClick(DialogInterface paramDialogInterface, int paramInt)
			{
				Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				context.startActivity(myIntent);
			}
		});

		dialog.setNegativeButton(R.string.location_services_not_enabled_msg_button_cancel, new DialogInterface.OnClickListener()
		{

			@Override
			public void onClick(DialogInterface paramDialogInterface, int paramInt)
			{
			}
		});
		dialog.show();
	}

}
