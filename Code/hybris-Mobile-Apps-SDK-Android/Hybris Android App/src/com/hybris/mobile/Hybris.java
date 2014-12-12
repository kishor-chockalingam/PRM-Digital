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
package com.hybris.mobile;

import java.io.IOException;
import java.io.InputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.Geofence;
import com.hybris.mobile.activity.StoreLocatorDetailActivity;
import com.hybris.mobile.api.geofence.GeofenceUtils;
import com.hybris.mobile.api.geofence.data.GeofenceObject;
import com.hybris.mobile.api.geofence.geofencable.Geofencable;
import com.hybris.mobile.api.geofence.geofencable.GeofenceJsonSharedPreferences;
import com.hybris.mobile.data.CategoryManager;
import com.hybris.mobile.logging.LoggingUtils;
import com.hybris.mobile.model.store.Store;
import com.hybris.mobile.utility.JsonUtils;
import com.hybris.mobile.utility.StringUtil;


/**
 * @author philip
 * 
 */
public class Hybris extends Application
{

	private static final String LOG_TAG = Hybris.class.getSimpleName();
	private static Context context;

	public void onCreate()
	{
		super.onCreate();

		// TODO - Delete this too heavy exception process not adapted for Android?
		//		ExceptionHandler.register(this);

		// Saving the application context
		setContext(this);

		// Load preference defaults
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

		// Updating the webservices URL from the preferences
		updateWebServicesUrl();

		// Enable / Disable geofencing
		enableGeofencing();

		SDKSettings.setSettingValue(
				InternalConstants.KEY_PREF_CATALOG,
				StringUtil.replaceIfNull(Hybris.getSharedPreferenceString(InternalConstants.KEY_PREF_CATALOG),
						SDKSettings.getSettingValue(InternalConstants.KEY_PREF_CATALOG)));

		SDKSettings.setSettingValue(
				InternalConstants.KEY_PREF_LANGUAGE,
				StringUtil.replaceIfNull(Hybris.getSharedPreferenceString(InternalConstants.KEY_PREF_LANGUAGE),
						SDKSettings.getSettingValue(InternalConstants.KEY_PREF_LANGUAGE)));

		CookieManager cookieManager = new CookieManager();
		CookieHandler.setDefault(cookieManager);

		populateCategories();

	}

	/**
	 * Save the context
	 * 
	 * @param context
	 */
	private void setContext(Context context)
	{
		Hybris.context = context;
	}

	/**
	 * Method to activate the geofencing monitoring
	 */
	public static void enableGeofencing()
	{
		// TODO - handle the success/error coming within the handler
		Handler handler = new Handler();

		if (isGooglePlayServicesConnected())
		{
			// Enable geofence capabilities
			if (isGeofencingActivated())
			{
				GeofenceUtils.enableGeofencesMonitoring(GeofenceJsonSharedPreferences.createGeofencable(Hybris.getAppContext(),
						DataConstants.PREFIX_GEOFENCE_SHARED_PREFERENCES), handler, 1, 2);
			}
			// Disable geofence capabilities
			else
			{
				GeofenceUtils.disableGeofencesMonitoring(GeofenceJsonSharedPreferences.createGeofencable(Hybris.getAppContext(),
						DataConstants.PREFIX_GEOFENCE_SHARED_PREFERENCES), handler, 1, 2);
			}
		}
		else
		{
			Toast.makeText(Hybris.getAppContext(), R.string.error_geofence_lib_not_available, 40000);
		}

	}


	/**
	 * TODO - Geofencing spoofed location
	 */
	public static void saveSpoofedGeolocationValuesFromPreferences()
	{
		Handler handler = new Handler();

		if (Boolean.valueOf(Hybris.getSharedPreferenceString(InternalConstants.KEY_PREF_GEOFENCING_SPOOF_OVERRIDE)))
		{
			double lat = 0;
			double longi = 0;
			float rad = 0;

			try
			{
				lat = Double.valueOf(getSharedPreferences().getString(InternalConstants.KEY_PREF_GEOFENCING_LATITUDE, ""));
				longi = Double.valueOf(getSharedPreferences().getString(InternalConstants.KEY_PREF_GEOFENCING_LONGITUDE, ""));
				rad = Float.valueOf(getSharedPreferences().getString(InternalConstants.KEY_PREF_GEOFENCING_RADIUS, ""));
			}
			catch (Exception e)
			{
			}

			if (lat != 0 && longi != 0 && rad != 0)
			{

				GeofenceObject geofence = new GeofenceObject();
				geofence.setId("spoofedGeofence");
				geofence.setExpirationDuration(Geofence.NEVER_EXPIRE);
				geofence.setLatitude(lat);
				geofence.setLongitude(longi);
				geofence.setRadius(rad);
				geofence.setTransitionType(Geofence.GEOFENCE_TRANSITION_ENTER);

				String tmpJson = "{\"geoPoint\":{\"longitude\":11.585745,\"latitude\":48.155121},\"storeImages\":[{\"url\":\"\\/medias\\/electronics-store-365x246-01.jpg?context=bWFzdGVyOjo6aW1hZ2VzOjo6MTA3NTE5Ojo6aW1hZ2UvanBlZzo6OmltYWdlcy9oMDIvaDYyLzg3OTY2ODc0MDA5OTAuanBnOjo6OTkzNWM1NTk5YjhmYjU3Y2QzNDc1NWFmMDBlM2NkMzk2YjFkZTlmYjI5Y2VkZjNkZGNhOGZkN2UxMWE4ZTBhMw\",\"format\":\"store\"},{\"url\":\"\\/medias\\/electronics-store-65x65-01.jpg?context=bWFzdGVyOjo6aW1hZ2VzOjo6MjA2OTM6OjppbWFnZS9qcGVnOjo6aW1hZ2VzL2g5Zi9oMDQvODc5NjY4NzU5NzU5OC5qcGc6OjpjZGEzMjdmZjI1NmMxMTE2ODNlYTBmYTg0MTA1YjAzNjkyMWJkNTViYWFjMmI3NTRmZmZlYTZhNmU3MjZlMDRk\",\"format\":\"cartIcon\"}],\"openingHours\":{\"specialDays\":[{\"closed\":true,\"date\":\"2013-09-16 04:00:00.0 UTC\",\"openingTime\":{\"minute\":0,\"hour\":0},\"formattedDate\":\"16\\/09\\/13\",\"name\":\"Respect for the Aged Day\",\"closingTime\":{\"minute\":0,\"hour\":0}},{\"closed\":true,\"date\":\"2013-09-23 04:00:00.0 UTC\",\"openingTime\":{\"minute\":0,\"hour\":0},\"formattedDate\":\"23\\/09\\/13\",\"name\":\"Autumnal Equinox Day\",\"closingTime\":{\"minute\":0,\"hour\":0}},{\"closed\":true,\"date\":\"2013-10-14 04:00:00.0 UTC\",\"openingTime\":{\"minute\":0,\"hour\":0},\"formattedDate\":\"14\\/10\\/13\",\"name\":\"Health-Sports Day\",\"closingTime\":{\"minute\":0,\"hour\":0}},{\"closed\":true,\"date\":\"2013-11-03 04:00:00.0 UTC\",\"openingTime\":{\"minute\":0,\"hour\":0},\"formattedDate\":\"03\\/11\\/13\",\"name\":\"Culture Day\",\"closingTime\":{\"minute\":0,\"hour\":0}},{\"closed\":true,\"date\":\"2013-11-23 05:00:00.0 UTC\",\"openingTime\":{\"minute\":0,\"hour\":0},\"formattedDate\":\"23\\/11\\/13\",\"name\":\"Labour Thanksgiving Day\",\"closingTime\":{\"minute\":0,\"hour\":0}},{\"closed\":true,\"date\":\"2013-12-23 05:00:00.0 UTC\",\"openingTime\":{\"minute\":0,\"hour\":0},\"formattedDate\":\"23\\/12\\/13\",\"name\":\"The Emperor's Birthday\",\"closingTime\":{\"minute\":0,\"hour\":0}}],\"weekDays\":[{\"weekDay\":\"Mon\",\"openingTime\":{\"minute\":0,\"formattedHour\":\"09:00\",\"hour\":9},\"closed\":false,\"closingTime\":{\"minute\":0,\"formattedHour\":\"20:00\",\"hour\":8}},{\"weekDay\":\"Tue\",\"openingTime\":{\"minute\":0,\"formattedHour\":\"09:00\",\"hour\":9},\"closed\":false,\"closingTime\":{\"minute\":0,\"formattedHour\":\"20:00\",\"hour\":8}},{\"weekDay\":\"Wed\",\"openingTime\":{\"minute\":0,\"formattedHour\":\"09:00\",\"hour\":9},\"closed\":false,\"closingTime\":{\"minute\":0,\"formattedHour\":\"20:00\",\"hour\":8}},{\"weekDay\":\"Thu\",\"openingTime\":{\"minute\":0,\"formattedHour\":\"09:00\",\"hour\":9},\"closed\":false,\"closingTime\":{\"minute\":0,\"formattedHour\":\"20:00\",\"hour\":8}},{\"weekDay\":\"Fri\",\"openingTime\":{\"minute\":0,\"formattedHour\":\"09:00\",\"hour\":9},\"closed\":false,\"closingTime\":{\"minute\":0,\"formattedHour\":\"20:00\",\"hour\":8}},{\"weekDay\":\"Sat\",\"openingTime\":{\"minute\":0,\"formattedHour\":\"10:00\",\"hour\":10},\"closed\":false,\"closingTime\":{\"minute\":0,\"formattedHour\":\"20:00\",\"hour\":8}},{\"weekDay\":\"Sun\",\"closed\":true}],\"code\":\"electronics-japan-standard-hours\"},\"address\":{\"id\":\"8796144107543\",\"phone\":\"+00 0000 0000\",\"postalCode\":\"000000\",\"formattedAddress\":\"Street Name, Street Number, 000000, Test Town, Germany\",\"town\":\"Test Town\",\"line1\":\"Street Name\",\"line2\":\"Street Number\",\"country\":{\"isocode\":\"DE\",\"name\":\"Germany\"}},\"name\":\"Test Geolocation Notification hybris\",\"features\":[{\"value\":\"Wheelchair Access\",\"key\":\"wheelchair\"},{\"value\":\"Creche\",\"key\":\"creche\"},{\"value\":\"Buy Online Pick Up In Store\",\"key\":\"buyOnlinePickupInStore\"}],\"formattedDistance\":\"3.7 Km\",\"url\":\"\\/store\\/Test Geolocation Notification hybris?lat=48.1367203&long=11.576754&q=munich\"}";
				Store store = JsonUtils.fromJson(tmpJson, Store.class);

				geofence.setAssociatedObjectFullClassName(Store.class.getCanonicalName());
				geofence.setAssociatedObject(store);
				geofence.setIntentClassDestination(StoreLocatorDetailActivity.class.getCanonicalName());
				geofence.setIntentObjectKeyName(DataConstants.INTENT_OBJECT_STORE);
				geofence.setNotificationTitle(Hybris.getAppContext().getString(R.string.geofence_notification_store_title));
				geofence.setNotificationText(Hybris.getAppContext().getString(R.string.geofence_notification_store_text,
						store.getName()));

				// Saving the object for future 
				Geofencable geofenceJsonSharedPreferences = GeofenceJsonSharedPreferences.createGeofencable(Hybris.getAppContext(),
						DataConstants.PREFIX_GEOFENCE_SHARED_PREFERENCES);
				geofenceJsonSharedPreferences.saveGeofence(geofence);

				// We force the activation on the geofencing
				GeofenceUtils.enableGeofencesMonitoring(geofenceJsonSharedPreferences, handler, 1, 2);
			}

		}
		else
		{
			// We enable/disable the geofencing with the normal method to not disable it by mistake if geofencing is switc
			enableGeofencing();
		}


	}

	public static void clearCookies()
	{
		CookieManager cookieManager = (CookieManager) CookieHandler.getDefault();
		cookieManager.getCookieStore().removeAll();
	}

	/**
	 * Shared Application context for preferences
	 * 
	 * @return application context
	 */
	public static Context getAppContext()
	{
		return Hybris.context;
	}


	public static SharedPreferences getSharedPreferences()
	{
		return PreferenceManager.getDefaultSharedPreferences(getAppContext());
	}

	/**
	 * Helper for getting saved strings
	 * 
	 * @return string
	 */
	public static String getSharedPreferenceString(String key)
	{
		if (getAppContext() != null)
		{
			Object value = getSharedPreferences().getAll().get(key);
			return value != null ? value.toString() : "";
		}
		else
		{
			LoggingUtils.e(LOG_TAG, "Context is null!", getAppContext());
			return "";
		}
	}

	/**
	 * Helper for saving strings
	 * 
	 * @param key
	 *           The key
	 * @param value
	 *           The value
	 */
	public static void setSharedPreferenceString(String key, String value)
	{
		if (Hybris.context != null)
		{
			SharedPreferences.Editor editor = getSharedPreferences().edit();
			editor.putString(key, value);
			editor.commit();
		}
	}


	/**
	 * Helper for constants
	 * 
	 * @param key
	 *           a resource e.g. R.string.client_id
	 * 
	 */
	public static String getConstant(int key)
	{
		if (Hybris.context != null)
		{
			return Hybris.context.getResources().getString(key, "");
		}
		else
		{
			return "";
		}
	}

	/**
	 * User Information data and methods
	 */

	private static HashSet<String> sPreviousSearches = new HashSet<String>();

	public static Set<String> getPreviousSearches()
	{

		// Load from user defaults
		if (Hybris.context != null)
		{
			Set<String> savedStrings = getSharedPreferences().getStringSet("savedSearches", null);
			if (savedStrings != null)
			{
				sPreviousSearches.addAll(savedStrings);
			}
		}

		return sPreviousSearches;
	}

	public static void setUserOnline(Boolean isLoggedIn)
	{
		Hybris.setSharedPreferenceString("user_online", isLoggedIn.toString());
	}

	public static Boolean isUserLoggedIn()
	{
		return Boolean.valueOf(Hybris.getSharedPreferenceString("user_online"));
	}

	public static void setUsername(String username)
	{
		Hybris.setSharedPreferenceString("username", username);
	}

	public static String getUsername()
	{
		return Hybris.getSharedPreferenceString("username");
	}

	public static void addPreviousSearch(String searchString)
	{
		// Trim if full
		if (getPreviousSearches().size() > ExternalConstants.MAX_PREVIOUS_SEARCHES)
		{

			// Removing the first element of the list
			Iterator<String> iterPreviousSearch = getPreviousSearches().iterator();

			if (iterPreviousSearch != null && iterPreviousSearch.hasNext())
			{
				iterPreviousSearch.next();
				iterPreviousSearch.remove();
			}

		}
		// Add
		getPreviousSearches().add(searchString);
		// Save
		if (Hybris.context != null)
		{
			SharedPreferences.Editor editor = getSharedPreferences().edit();
			editor.putStringSet("savedSearches", sPreviousSearches);
			editor.commit();
		}
	}

	public static void clearPreviousSearches()
	{
		if (sPreviousSearches != null)
		{
			sPreviousSearches.clear();
			// Save
			if (Hybris.context != null)
			{
				SharedPreferences.Editor editor = getSharedPreferences().edit();
				editor.putStringSet("savedSearches", sPreviousSearches);
				editor.commit();
			}
		}
	}

	private static void populateCategories()
	{
		String storeName = SDKSettings.getSettingValue(InternalConstants.KEY_PREF_CATALOG);
		try
		{
			AssetManager am = context.getAssets();
			if (storeName == null || "".equals(storeName))
			{
				storeName = "electronics";
			}
			storeName = "categories." + storeName.substring(0, storeName.indexOf("/")) + ".plist";
			InputStream fs = am.open(storeName);
			CategoryManager.reloadCategories(fs);
		}
		catch (IOException e)
		{
			LoggingUtils.e(LOG_TAG, "Error opening file \"" + storeName + "\". " + e.getLocalizedMessage(), getAppContext());
		}
	}

	/**
	 * Method to update the Webservices URL according to the preferences
	 */
	private static void updateWebServicesUrl()
	{
		String isUsingSpecificBaseUrl = Hybris.getSharedPreferenceString(InternalConstants.KEY_PREF_TOGGLE_SPECIFIC_BASE_URL);
		String specificBaseUrl = Hybris.getSharedPreferenceString(InternalConstants.KEY_PREF_SPECIFIC_BASE_URL);

		// If the user chose to use a specific URL and if this URL is not empty, we use it
		if (StringUtils.equals(isUsingSpecificBaseUrl, String.valueOf(true)) && StringUtils.isNotBlank(specificBaseUrl))
		{
			SDKSettings.setSettingValue(InternalConstants.KEY_PREF_BASE_URL, specificBaseUrl);
		}
		// Otherwise we use the one from the preferences
		else
		{
			SDKSettings.setSettingValue(
					InternalConstants.KEY_PREF_BASE_URL,
					StringUtil.replaceIfNull(Hybris.getSharedPreferenceString(InternalConstants.KEY_PREF_BASE_URL),
							SDKSettings.getSettingValue(InternalConstants.KEY_PREF_BASE_URL)));
		}
	}

	/**
	 * Update the application preferences
	 */
	public static void updatePreferences()
	{
		populateCategories();
		updateWebServicesUrl();
	}

	/**
	 * 
	 * Return true is the geofencing is activated on the application
	 * 
	 * @return
	 */
	public static Boolean isGeofencingActivated()
	{
		return Boolean.valueOf(Hybris.getSharedPreferenceString(InternalConstants.KEY_PREF_GEOFENCING));
	}

	/**
	 * Return true if the google play services is available on the device
	 * 
	 * @return
	 */
	public static boolean isGooglePlayServicesConnected()
	{
		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(Hybris.getAppContext());
		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

}
