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
package com.hybris.mobile.fragment;

import java.util.Map.Entry;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;

import com.hybris.mobile.Hybris;
import com.hybris.mobile.InternalConstants;
import com.hybris.mobile.R;
import com.hybris.mobile.SDKSettings;


public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener
{

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}

	@Override
	public void onResume()
	{
		super.onResume();

		this.getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

		SharedPreferences sp = this.getPreferenceManager().getSharedPreferences();

		for (Entry<String, ?> preference : sp.getAll().entrySet())
		{
			if (preference != null && preference.getValue() != null)
			{
				updatePreferenceSummary(preference.getKey(), preference.getValue().toString());
			}
		}

	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
	{

		if (sharedPreferences.getAll().get(key) != null)
		{
			String value = sharedPreferences.getAll().get(key).toString();

			if (key.equals(InternalConstants.KEY_PREF_BASE_URL) || key.equals(InternalConstants.KEY_PREF_SPECIFIC_BASE_URL)
					|| key.equals(InternalConstants.KEY_PREF_TOGGLE_SPECIFIC_BASE_URL)
					|| key.equals(InternalConstants.KEY_PREF_CATALOG))
			{
				SDKSettings.setSettingValue(key, value);
				Hybris.updatePreferences();
			}
			else if (key.equals(InternalConstants.KEY_PREF_GEOFENCING))
			{
				Hybris.enableGeofencing();
			}
			else if (key.equals(InternalConstants.KEY_PREF_GEOFENCING_LATITUDE)
					|| key.equals(InternalConstants.KEY_PREF_GEOFENCING_LONGITUDE)
					|| key.equals(InternalConstants.KEY_PREF_GEOFENCING_RADIUS)
					|| key.equals(InternalConstants.KEY_PREF_GEOFENCING_SPOOF_OVERRIDE))
			{
				Hybris.saveSpoofedGeolocationValuesFromPreferences();
			}

			updatePreferenceSummary(key, value);
		}

	}

	/**
	 * Update the summary on the preference view
	 * 
	 * @param key
	 * @param value
	 */
	private void updatePreferenceSummary(String key, String value)
	{
		Preference editTextPref = findPreference(key);

		// We don't show the summary for the switch views
		if (editTextPref != null && !(editTextPref instanceof SwitchPreference))
		{
			editTextPref.setSummary(value);
		}
	}

}
