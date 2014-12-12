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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;


public class ExceptionHandler
{

	public static String LOG_TAG = ExceptionHandler.class.getSimpleName();

	private static String[] stackTraceFileList = null;

	public static boolean register(Context context)
	{
		LoggingUtils.i(LOG_TAG, "Registering default exceptions handler");
		// Get information about the Package
		PackageManager pm = context.getPackageManager();
		try
		{
			PackageInfo pi;
			// Version
			pi = pm.getPackageInfo(context.getPackageName(), 0);
			RA.APP_VERSION = pi.versionName;
			// Package name
			RA.APP_PACKAGE = pi.packageName;
			// Files dir for storing the stack traces
			RA.FILES_PATH = context.getFilesDir().getAbsolutePath();
			// Device model
			RA.PHONE_MODEL = android.os.Build.MODEL;
			// Android version
			RA.ANDROID_VERSION = android.os.Build.VERSION.RELEASE;
		}
		catch (NameNotFoundException e)
		{
			LoggingUtils.e(LOG_TAG, "No package found for \"" + context.getPackageName() + "\"" + e.getLocalizedMessage(), context);
		}

		LoggingUtils.i(LOG_TAG, "TRACE_VERSION: " + RA.TraceVersion);
		LoggingUtils.d(LOG_TAG, "APP_VERSION: " + RA.APP_VERSION);
		LoggingUtils.d(LOG_TAG, "APP_PACKAGE: " + RA.APP_PACKAGE);
		LoggingUtils.d(LOG_TAG, "FILES_PATH: " + RA.FILES_PATH);
		LoggingUtils.d(LOG_TAG, "URL: " + RA.URL);

		boolean stackTracesFound = false;
		// We'll return true if any stack traces were found
		if (searchForStackTraces().length > 0)
		{
			stackTracesFound = true;
		}

		new Thread()
		{
			@Override
			public void run()
			{
				// First of all transmit any stack traces that may be lying around
				submitStackTraces();
				UncaughtExceptionHandler currentHandler = Thread.getDefaultUncaughtExceptionHandler();
				if (currentHandler != null)
				{
					LoggingUtils.d(LOG_TAG, "current handler class=" + currentHandler.getClass().getName());
				}
				// don't register again if already registered
				if (!(currentHandler instanceof DefaultExceptionHandler))
				{
					// Register default exceptions handler
					Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(currentHandler));
				}
			}
		}.start();

		return stackTracesFound;
	}

	public static void register(Context context, String url)
	{
		LoggingUtils.i(LOG_TAG, "Registering default exceptions handler: " + url);
		// Use custom URL
		RA.URL = url;
		// Call the default register method
		register(context);
	}

	private static String[] searchForStackTraces()
	{
		if (stackTraceFileList != null)
		{
			return stackTraceFileList;
		}
		File dir = new File(RA.FILES_PATH + "/");
		dir.mkdir();
		FilenameFilter filter = new FilenameFilter()
		{
			public boolean accept(File dir, String name)
			{
				return name.endsWith(".stacktrace");
			}
		};
		return (stackTraceFileList = dir.list(filter));
	}

	public static void submitStackTraces()
	{
		try
		{
			LoggingUtils.d(LOG_TAG, "Looking for exceptions in: " + RA.FILES_PATH);
			String[] list = searchForStackTraces();
			if (list != null && list.length > 0)
			{
				LoggingUtils.d(LOG_TAG, "Found " + list.length + " stacktrace(s)");
				for (int i = 0; i < list.length; i++)
				{
					String filePath = RA.FILES_PATH + "/" + list[i];
					// Extract the version from the filename: "packagename-version-...."
					String version = list[i].split("-")[0];
					LoggingUtils.d(LOG_TAG, "Stacktrace in file '" + filePath + "' belongs to version " + version);
					// Read contents of stacktrace
					StringBuilder contents = new StringBuilder();
					BufferedReader input = new BufferedReader(new FileReader(filePath));
					String line = null;
					String androidVersion = null;
					String phoneModel = null;
					while ((line = input.readLine()) != null)
					{
						if (androidVersion == null)
						{
							androidVersion = line;
							continue;
						}
						else if (phoneModel == null)
						{
							phoneModel = line;
							continue;
						}
						contents.append(line);
						contents.append(System.getProperty("line.separator"));
					}
					input.close();
					String stacktrace;
					stacktrace = contents.toString();
					LoggingUtils.d(LOG_TAG, "Transmitting stack trace: " + stacktrace);
					// Transmit stack trace with POST request
					DefaultHttpClient httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost(RA.URL);
					List<NameValuePair> nvps = new ArrayList<NameValuePair>();
					nvps.add(new BasicNameValuePair("package_name", RA.APP_PACKAGE));
					nvps.add(new BasicNameValuePair("package_version", version));
					nvps.add(new BasicNameValuePair("phone_model", phoneModel));
					nvps.add(new BasicNameValuePair("android_version", androidVersion));
					nvps.add(new BasicNameValuePair("stacktrace", stacktrace));
					httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
					httpClient.execute(httpPost);
				}
			}
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_TAG, "Error submitting stack traces. " + e.getLocalizedMessage(), null);
		}
		finally
		{
			String[] list = searchForStackTraces();
			for (int i = 0; i < list.length; i++)
			{
				File file = new File(RA.FILES_PATH + "/" + list[i]);
				file.delete();
			}
		}
	}
}
