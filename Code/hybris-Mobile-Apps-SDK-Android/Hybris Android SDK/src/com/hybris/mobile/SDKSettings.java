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

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


/**
 * @author philip
 * 
 */
public final class SDKSettings
{

	public static final String client_id = "mobile_android";
	public static final String client_secret = "secret";

	private static final Map<String, String> settings = new HashMap<String, String>();

	/**
	 * Helper for getting saved strings
	 * 
	 * @return string
	 */
	public static String getSharedPreferenceString(Context context, String key)
	{
		PreferenceManager.getDefaultSharedPreferences(context);
		String s = PreferenceManager.getDefaultSharedPreferences(context).getString(key, "");
		return s;
	}


	/**
	 * Helper for saving strings
	 * 
	 * @param key
	 *           The key
	 * @param value
	 *           The value
	 */
	public static void setSharedPreferenceString(Context context, String key, String value)
	{
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putString(key, value);
		editor.commit();
	}


	/**
	 * Helper for constants
	 * 
	 * @param key
	 *           a resource e.g. R.string.client_id
	 * 
	 */
	public static String getConstant(Context context, int key)
	{
		return context.getResources().getString(key, "");
	}


	public static void setSettingValue(String settingName, String value)
	{
		settings.put(settingName, value);
	}

	public static String getSettingValue(String settingName)
	{
		return settings.get(settingName);
	}

}
