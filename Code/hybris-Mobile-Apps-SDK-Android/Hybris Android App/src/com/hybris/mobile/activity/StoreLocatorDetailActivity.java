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

import android.app.ActionBar;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hybris.mobile.DataConstants;
import com.hybris.mobile.Hybris;
import com.hybris.mobile.R;
import com.hybris.mobile.api.geofence.GeofenceUtils;
import com.hybris.mobile.api.geofence.data.GeofenceObject;
import com.hybris.mobile.api.geofence.geofencable.Geofencable;
import com.hybris.mobile.api.geofence.geofencable.GeofenceJsonSharedPreferences;
import com.hybris.mobile.controller.StoreLocatorController;
import com.hybris.mobile.model.store.Store;
import com.hybris.mobile.model.store.StoreFeature;
import com.hybris.mobile.model.store.StoreWeekDay;
import com.hybris.mobile.utility.GeolocationUtil;
import com.hybris.mobile.utility.IntentHelper;


public class StoreLocatorDetailActivity extends FragmentActivity implements Handler.Callback
{
	private Store store = null;
	private Location currentLocation = null;
	private Geofencable geofenceJsonSharedPreferences;

	@Override
	protected void onResume()
	{
		super.onResume();

		if (GeolocationUtil.isLocationActivated())
		{
			// We run the service that tries to geolocate the user
			GeolocationUtil.getLocation(new Handler(this), StoreLocatorController.MESSAGE_GEOLOCATION_SUCCESS,
					StoreLocatorController.MESSAGE_GEOLOCATION_ERROR);
		}
		else
		{
			GeolocationUtil.showDialogEnableLocationServices(this);
		}

		// Trying to find if we have a store id coming from a geofence notification
		if (getIntent() != null && getIntent().getExtras() != null
				&& StringUtils.isNotEmpty(getIntent().getExtras().getString(DataConstants.INTENT_OBJECT_STORE)))
		{
			String storeId = getIntent().getExtras().getString(DataConstants.INTENT_OBJECT_STORE);

			GeofenceObject geofenceObject = geofenceJsonSharedPreferences.getGeofenceObject(storeId);

			// We get the store from the cache with the specified id
			store = (Store) geofenceObject.getAssociatedObject();
		}
		else
		{
			// We get the cached store
			store = (Store) IntentHelper.getObjectForKey(DataConstants.INTENT_OBJECT_STORE);

			// We re-save the object again because the above method removes it and in case of coming back to this activity we need to retrieve the store
			IntentHelper.addObjectForKey(store, DataConstants.INTENT_OBJECT_STORE);
		}

		loadData();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_detail);
		setTitle(R.string.store_details_title);

		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);

		// Creating the geofencable object for geofencing the store
		geofenceJsonSharedPreferences = GeofenceJsonSharedPreferences.createGeofencable(getApplicationContext(),
				DataConstants.PREFIX_GEOFENCE_SHARED_PREFERENCES);

	}

	@Override
	public void onPause()
	{
		super.onPause();
	}


	private void loadData()
	{
		TextView storeName = (TextView) findViewById(R.id.lblStoreName);
		TextView address = (TextView) findViewById(R.id.lblStoreAddress);
		Button phone = (Button) findViewById(R.id.buttonStorePhone);
		TextView openningHours = (TextView) findViewById(R.id.lblStoreOpenningTimes);
		TextView features = (TextView) findViewById(R.id.lblFeatures);

		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
		GoogleMap map = mapFragment.getMap();

		if (map != null)
		{
			map.setMyLocationEnabled(true);
		}

		storeName.setText(store.getName());

		if (store.getAddress() != null)
		{
			address.setText(store.getAddress().getFormattedAddress().replace(", ", "\n").replace(",", "\n"));
			phone.setText(store.getAddress().getPhone());
		}

		//features
		String strFeatures = "";

		if (store.getFeatures() != null)
		{
			for (StoreFeature storeFeature : store.getFeatures())
			{
				if (StringUtils.isEmpty(strFeatures))
				{
					strFeatures = storeFeature.getValue();
				}
				else
				{
					strFeatures = strFeatures + "\n" + storeFeature.getValue();
				}
			}
		}

		features.setText(strFeatures);

		//openning hours
		String strOpenHrs = "";

		if (store.getOpeningHours() != null && store.getOpeningHours().getWeekDays() != null)
		{

			for (StoreWeekDay storeWeekDay : store.getOpeningHours().getWeekDays())
			{

				if (!StringUtils.isEmpty(strOpenHrs))
				{
					strOpenHrs = strOpenHrs + "\n";
				}

				if (storeWeekDay.isClosed())
				{
					strOpenHrs += storeWeekDay.getWeekDay() + " Closed";
				}
				else if (storeWeekDay.getClosingTime() != null && storeWeekDay.getOpeningTime() != null)
				{
					strOpenHrs += storeWeekDay.getWeekDay() + " " + storeWeekDay.getOpeningTime().getFormattedHour() + " - "
							+ storeWeekDay.getClosingTime().getFormattedHour();
				}

			}

		}

		openningHours.setText(strOpenHrs);


		//marker and initial location
		if (store.getGeoPoint() != null)
		{
			float lat = Float.parseFloat(store.getGeoPoint().getLatitude());
			float lng = Float.parseFloat(store.getGeoPoint().getLongitude());

			LatLng storeLocation = new LatLng(lat, lng);
			CameraPosition cameraPosition = new CameraPosition.Builder().target(storeLocation).zoom(17).build();

			if (map != null)
			{
				map.addMarker(new MarkerOptions().position(storeLocation).title(store.getName())
						.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker)));
				map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
			}

		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		// Clicking on the geofence icon
			case R.id.geofence_store:

				// If the geofence functionnality is not activated we display an error message
				if (!Hybris.isGeofencingActivated())
				{
					Toast.makeText(this, R.string.error_geofence_not_activated, Toast.LENGTH_LONG);
				}
				else
				{
					// If the geofence is already activated for that store, clicking on it will disable the geofence
					if (geofenceJsonSharedPreferences.getGeofenceObject(store.getName()) != null)
					{
						item.setIcon(R.drawable.ic_menu_location_default);
						enableGeofence(false);
					}
					// In the other case we activate it
					else
					{
						item.setIcon(R.drawable.ic_menu_location_active);
						enableGeofence(true);
					}

				}

				return true;
			default:
				return false;
		}
	}

	public void onClickPhone(View v)
	{
		boolean validNr = false;

		if (store.getAddress() != null)
		{
			String phoneNr = store.getAddress().getPhone();
			if (phoneNr != null && phoneNr.trim().length() > 0)
			{
				Uri telUrl = Uri.parse("tel:" + phoneNr.trim());
				Intent intent = new Intent(Intent.ACTION_DIAL, telUrl);
				startActivity(intent);
				validNr = true;
			}
		}

		if (!validNr)
		{
			Toast.makeText(getApplicationContext(), R.string.error_invalidPhoneNumber, Toast.LENGTH_SHORT).show();
		}
	}

	public void showDirection(View view)
	{
		if (currentLocation == null)
		{
			Toast.makeText(this, R.string.enable_location_services, Toast.LENGTH_LONG).show();
		}
		else
		{
			if (store.getGeoPoint() != null)
			{
				String daddr = store.getGeoPoint().getLatitude() + "," + store.getGeoPoint().getLongitude();
				String saddr = Double.toString(currentLocation.getLatitude()) + "," + Double.toString(currentLocation.getLongitude());

				Intent i = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(getString(R.string.maps_url_directions, saddr,
						daddr)));
				startActivity(i);
			}
			else
			{
				Toast.makeText(this, R.string.store_no_latitude_longitude, Toast.LENGTH_LONG).show();
			}
		}

	}

	public void showInMaps(View view)
	{

		if (store.getGeoPoint() != null)
		{
			Intent i = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(getString(R.string.maps_url_query, 
					store.getAddress().getFormattedAddress())));
			startActivity(i);
		}

	}

	@Override
	public boolean handleMessage(Message msg)
	{
		switch (msg.what)
		{
		// Error with the geolocation module
			case StoreLocatorController.MESSAGE_GEOLOCATION_ERROR:
				Toast.makeText(this, R.string.error_geolocation_not_able_to_geolocate, Toast.LENGTH_LONG).show();
				break;

			// We geolocated the user
			case StoreLocatorController.MESSAGE_GEOLOCATION_SUCCESS:
				currentLocation = (Location) msg.obj;
				break;
		}

		return false;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.store_details, menu);

		MenuItem geofenceItem = menu.findItem(R.id.geofence_store);

		// We display an active icon if there is a geofence for this store AND the geofencing functionnality is activated
		if (geofenceJsonSharedPreferences.getGeofenceObject(store.getName()) != null && Hybris.isGeofencingActivated())
		{
			geofenceItem.setIcon(R.drawable.ic_menu_location_active);
		}
		// Otherwise we display the default icon
		else
		{
			geofenceItem.setIcon(R.drawable.ic_menu_location_default);
		}

		return true;
	}

	/**
	 * Enable / Disable the geofence for the store
	 * 
	 * @param enable
	 */
	private void enableGeofence(boolean enable)
	{

		Handler handler = new Handler();

		if (enable)
		{

			if (Hybris.isGeofencingActivated())
			{

				GeofenceObject geofence = new GeofenceObject();
				geofence.setId(store.getName());
				geofence.setExpirationDuration(Geofence.NEVER_EXPIRE);

				if (store.getGeoPoint() != null)
				{
					geofence.setLatitude(Double.valueOf(store.getGeoPoint().getLatitude()));
					geofence.setLongitude(Double.valueOf(store.getGeoPoint().getLongitude()));
				}

				geofence.setRadius(1000);
				geofence.setTransitionType(Geofence.GEOFENCE_TRANSITION_ENTER);
				geofence.setAssociatedObjectFullClassName(Store.class.getCanonicalName());
				geofence.setAssociatedObject(store);
				geofence.setIntentClassDestination(StoreLocatorDetailActivity.class.getCanonicalName());
				geofence.setIntentObjectKeyName(DataConstants.INTENT_OBJECT_STORE);
				geofence.setNotificationTitle(Hybris.getAppContext().getString(R.string.geofence_notification_store_title));
				geofence.setNotificationText(Hybris.getAppContext().getString(R.string.geofence_notification_store_text,
						store.getName()));

				GeofenceUtils.monitorGeofence(geofence, geofenceJsonSharedPreferences, handler, 1, 2);
			}
			else
			{
				Toast.makeText(this, R.string.error_geofence_not_activated, Toast.LENGTH_LONG);
			}

		}
		else
		{
			GeofenceUtils.removeGeofenceMonitoring(geofenceJsonSharedPreferences, handler, 1, 2, store.getName());
		}

	}

}
