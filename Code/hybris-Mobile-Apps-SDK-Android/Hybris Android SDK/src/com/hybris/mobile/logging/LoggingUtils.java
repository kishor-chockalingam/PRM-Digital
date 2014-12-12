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
package com.hybris.mobile.logging;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.hybris.mobile.BuildConfig;


public class LoggingUtils
{

	public static void d(String tag, String msg)
	{
		if (BuildConfig.DEBUG)
		{
			Log.d(tag, msg);
		}
	}

	public static void d(String tag, String msg, Throwable tr)
	{
		if (BuildConfig.DEBUG)
		{
			Log.d(tag, msg, tr);
		}
	}

	public static void e(String tag, String msg, Context context)
	{
		if (BuildConfig.DEBUG)
		{
			Log.e(tag, msg);
		}

		if (context != null)
		{
			Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
		}

	}

	public static void e(String tag, String msg, Throwable tr, Context context)
	{
		if (BuildConfig.DEBUG)
		{
			Log.e(tag, msg, tr);
		}

		if (context != null)
		{
			Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
		}
	}

	public static void i(String tag, String msg)
	{
		if (BuildConfig.DEBUG)
		{
			Log.i(tag, msg);
		}
	}

	public static void i(String tag, String msg, Throwable tr)
	{
		if (BuildConfig.DEBUG)
		{
			Log.i(tag, msg, tr);
		}
	}

}
