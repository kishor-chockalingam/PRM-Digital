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
package com.hybris.mobile.data;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import android.content.Context;
import android.os.Bundle;

import com.hybris.mobile.InternalConstants;
import com.hybris.mobile.R;
import com.hybris.mobile.SDKSettings;
import com.hybris.mobile.logging.LoggingUtils;
import com.hybris.mobile.model.TokenLogin;
import com.hybris.mobile.utility.CookieUtils;
import com.hybris.mobile.utility.JsonUtils;


/**
 * Helper class that encapsulates the access_token refreshing and provides other auth information.
 * 
 * This class does not need to be public for users of the SDK.
 */
public class WebServiceAuthProvider
{
	private static final String LOG_TAG = WebServiceAuthProvider.class.getSimpleName();

	// Trust every server - don't check for any certificate
	final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier()
	{
		public boolean verify(String hostname, SSLSession session)
		{
			return true;
		}
	};

	private static void trustAllHosts()
	{
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[]
		{ new X509TrustManager()
		{
			public java.security.cert.X509Certificate[] getAcceptedIssuers()
			{
				return new java.security.cert.X509Certificate[] {};
			}

			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException
			{
			}

			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException
			{
			}
		} };

		// Install the all-trusting trust manager
		try
		{
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_TAG, "Error with SSL connection. " + e.getLocalizedMessage(), null);
		}
	}

	public static String tokenURL()
	{
		if(SDKSettings.getSettingValue(InternalConstants.KEY_PREF_TOGGLE_SPECIFIC_BASE_URL).equalsIgnoreCase("true")) {
			LoggingUtils.i(LOG_TAG, "token URL: " + SDKSettings.getSettingValue(InternalConstants.KEY_PREF_BASE_URL) + "/bncwebservices/oauth/token");
			return SDKSettings.getSettingValue(InternalConstants.KEY_PREF_BASE_URL) + "/bncwebservices/oauth/token";
		} else {
			LoggingUtils.i(LOG_TAG, "token URL: " + SDKSettings.getSettingValue(InternalConstants.KEY_PREF_BASE_URL) + "/rest/oauth/token");
			return SDKSettings.getSettingValue(InternalConstants.KEY_PREF_BASE_URL) + "/rest/oauth/token";
		}
	}

	public static String callbackURL()
	{
		return SDKSettings.getSettingValue(InternalConstants.KEY_PREF_BASE_URL) + "/oauth2_callback";
	}

	private static String readFromStream(InputStream in) throws IOException
	{
		StringBuilder sb = new StringBuilder();
		BufferedReader r = new BufferedReader(new InputStreamReader(in), 1000);
		for (String line = r.readLine(); line != null; line = r.readLine())
		{
			sb.append(line);
		}
		in.close();
		return sb.toString();
	}

	public static void saveTokens(Context context, TokenLogin tokenLogin)
	{
		SDKSettings.setSharedPreferenceString(context, "access_token", tokenLogin.getAccess_token());
		SDKSettings.setSharedPreferenceString(context, "refresh_token", tokenLogin.getRefresh_token());
		SDKSettings.setSharedPreferenceString(context, "expires_in", tokenLogin.getExpires_in());
		SDKSettings.setSharedPreferenceString(context, "issued_on", DateFormat.getDateTimeInstance().format(new Date()));
	}

	public static void clearTokens(Context context)
	{
		SDKSettings.setSharedPreferenceString(context, "access_token", "");
		SDKSettings.setSharedPreferenceString(context, "refresh_token", "");
		SDKSettings.setSharedPreferenceString(context, "expires_in", "");
		SDKSettings.setSharedPreferenceString(context, "issued_on", "");

		// Clear cookies
		CookieUtils.clearCookies();
	}

	/**
	 * Returns a hint
	 * 
	 * @return
	 */
	public static boolean tokenExpiredHint(Context context)
	{
		try
		{
			long interval = new Date().getTime()
					- DateFormat.getDateTimeInstance().parse(SDKSettings.getSharedPreferenceString(context, "issued_on")).getTime();
			long expires_in = Long.parseLong(SDKSettings.getSharedPreferenceString(context, "expires_in"));

			return interval > expires_in;
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_TAG, "Error parsing date \"" + SDKSettings.getSharedPreferenceString(context, "issued_on") + "\". "
					+ e.getLocalizedMessage(), null);
			return true;
		}
	}

	public static String encodePostBody(Bundle parameters)
	{
		if (parameters == null)
		{
			return "";
		}

		StringBuilder sb = new StringBuilder();

		for (Iterator<String> i = parameters.keySet().iterator(); i.hasNext();)
		{
			String key = i.next();
			Object parameter = parameters.get(key);
			if (!(parameter instanceof String))
			{
				continue;
			}

			try
			{
				sb.append(URLEncoder.encode(key, "UTF-8"));
				sb.append("=");
				sb.append(URLEncoder.encode((String) parameter, "UTF-8"));
			}
			catch (UnsupportedEncodingException e)
			{
				// TODO report parse error
			}

			if (i.hasNext())
			{
				sb.append("&");
			}
		}

		return sb.toString();
	}


	/// Request a new refresh token based on the current tokens stored in NSUserDefaults
	static void refreshAccessToken(Context context) throws IOException
	{
		LoggingUtils.d("Auth", "Refreshing Access Token");
		HttpsURLConnection connection = (HttpsURLConnection) new URL(tokenURL()).openConnection();
		//		connection.setRequestProperty("Accept-Language", Hybris.getSharedPreferenceString("web_services_language_preference") + ";q=1");
		connection.setRequestProperty("Accept", "application/json");
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.setRequestProperty("Connection", "Keep-Alive");
		connection.setRequestProperty("User-Agent", System.getProperties().getProperty("http.agent") + " OCCAndroidSDK");
		connection.setHostnameVerifier(DO_NOT_VERIFY);
		trustAllHosts();

		// Body
		Bundle postBody = new Bundle();
		postBody.putString("grant_type", "refresh_token");
		postBody.putString("refresh_token", SDKSettings.getSharedPreferenceString(context, "refresh_token"));
		postBody.putString("client_id", SDKSettings.getConstant(context, R.string.client_id));
		postBody.putString("client_secret", SDKSettings.getConstant(context, R.string.client_secret));
		postBody.putString("redirect_uri", WebServiceAuthProvider.callbackURL());

		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.connect();
		OutputStream os = new BufferedOutputStream(connection.getOutputStream());
		os.write(encodePostBody(postBody).getBytes());
		os.flush();

		String response = "";
		try
		{
			response = readFromStream(connection.getInputStream());
		}
		catch (FileNotFoundException e)
		{
			response = readFromStream(connection.getErrorStream());
		}
		finally
		{
			connection.disconnect();
		}

		// Parse and set the values
		// TODO
		WebServiceAuthProvider.saveTokens(context, JsonUtils.fromJson(response, TokenLogin.class));
	}

}
