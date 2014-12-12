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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.util.Base64;

import com.hybris.mobile.CatalogEnums;
import com.hybris.mobile.CurrencyEnums;
import com.hybris.mobile.InternalConstants;
import com.hybris.mobile.SDKSettings;
import com.hybris.mobile.logging.LoggingUtils;


public class WebServiceDataProvider
{
	private static final String LOG_TAG = WebServiceDataProvider.class.getSimpleName();

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

	// Static helper methods

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
				LoggingUtils.e(LOG_TAG,
						"Error encoding \"" + key + "\" or \"" + parameter + "\" to UTF-8 ." + e.getLocalizedMessage(), null);
			}

			if (i.hasNext())
			{
				sb.append("&");
			}
		}
		return sb.toString();
	}

	private static String baseUrl(Context context)
	{
		return String.format("%s/rest/v1", SDKSettings.getSharedPreferenceString(context, "web_services_base_url_preference"));
	}

	private static String urlForLogout(Context context)
	{
		return String.format("%s/customers/current/logout", baseUrl(context));
	}

	public static HttpURLConnection createConnection(URL url) throws IOException
	{
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("Accept", "application/json");
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.setRequestProperty("Connection", "Keep-Alive");
		connection.setRequestProperty("User-Agent", System.getProperties().getProperty("http.agent") + " OCCAndroidSDK");
		return connection;
	}

	public static HttpsURLConnection createSecureConnection(URL url) throws IOException
	{
		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		connection.setRequestProperty("Accept", "application/json");
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.setRequestProperty("Connection", "Keep-Alive");
		connection.setRequestProperty("User-Agent", System.getProperties().getProperty("http.agent") + " OCCAndroidSDK");
		return connection;
	}

	private static String addParameters(Context context, String urlAsString)
	{
		String updatedUrlAsString = "";
		String spacer = "?";
		if (urlAsString.contains("?"))
		{
			spacer = "&";
		}

		// Currency settings
		String currency = CurrencyEnums.USD.getCurrency();

		if (StringUtils.equals(SDKSettings.getSharedPreferenceString(context, InternalConstants.KEY_PREF_CATALOG),
				CatalogEnums.APPARELUK.getCatalog()))
		{
			// TODO - not supported by the apparel-uk catalog
			currency = "";
		}

		updatedUrlAsString = urlAsString + spacer + "lang="
				+ SDKSettings.getSharedPreferenceString(context, "web_services_language_preference") + "&curr=" + currency;


		return updatedUrlAsString;
	}

	/**
	 * Synchronous call to the OCC web services
	 * 
	 * @param url
	 *           The url
	 * @param isAuthorizedRequest
	 *           Whether this request requires the authorization token sending
	 * @param httpMethod
	 *           method type (GET, PUT, POST, DELETE)
	 * @param httpBody
	 *           Data to be sent in the body (Can be empty)
	 * @return The data from the server as a string, in almost all cases JSON
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ProtocolException
	 */
	public static String getResponse(Context context, String url, Boolean isAuthorizedRequest, String httpMethod, Bundle httpBody)
			throws MalformedURLException, IOException, ProtocolException, JSONException
	{
		// Refresh if necessary
		if (isAuthorizedRequest && WebServiceAuthProvider.tokenExpiredHint(context))
		{
			WebServiceAuthProvider.refreshAccessToken(context);
		}

		boolean refreshLimitReached = false;
		int refreshed = 0;

		String response = "";
		while (!refreshLimitReached)
		{
			// If we have refreshed max number of times, we will not do so again
			if (refreshed == 1)
			{
				refreshLimitReached = true;
			}

			// Make the connection and get the response
			OutputStream os;
			HttpURLConnection connection;

			if (httpMethod.equals("GET") && httpBody != null && !httpBody.isEmpty())
			{
				url = url + "?" + encodePostBody(httpBody);
			}
			URL requestURL = new URL(addParameters(context, url));

			if (StringUtils.equalsIgnoreCase(requestURL.getProtocol(), "https"))
			{
				trustAllHosts();
				HttpsURLConnection https = createSecureConnection(requestURL);
				https.setHostnameVerifier(DO_NOT_VERIFY);
				if (isAuthorizedRequest)
				{
					String authValue = "Bearer " + SDKSettings.getSharedPreferenceString(context, "access_token");
					https.setRequestProperty("Authorization", authValue);
				}
				connection = https;
			}
			else
			{
				connection = createConnection(requestURL);
			}
			connection.setRequestMethod(httpMethod);

			if (!httpMethod.equals("GET") && !httpMethod.equals("DELETE"))
			{
				connection.setDoOutput(true);
				connection.setDoInput(true);
				connection.connect();

				if (httpBody != null && !httpBody.isEmpty())
				{
					os = new BufferedOutputStream(connection.getOutputStream());
					os.write(encodePostBody(httpBody).getBytes());
					os.flush();
				}
			}

			response = "";
			try
			{
				LoggingUtils.d(LOG_TAG, connection.toString());
				response = readFromStream(connection.getInputStream());
			}
			catch (FileNotFoundException e)
			{
				LoggingUtils.e(LOG_TAG, "Error reading stream \"" + connection.getInputStream() + "\". " + e.getLocalizedMessage(),
						context);
				response = readFromStream(connection.getErrorStream());
			}
			finally
			{
				connection.disconnect();
			}

			// Allow for calls to return nothing
			if (response.length() == 0)
			{
				return "";
			}

			// Check for JSON parsing errors (will throw JSONException is can't be parsed)
			JSONObject object = new JSONObject(response);

			// If no error, return response
			if (!object.has("error"))
			{
				return response;
			}
			// If there is a refresh token error, refresh the token
			else if (object.getString("error").equals("invalid_token"))
			{
				if (refreshLimitReached)
				{
					// Give up
					return response;
				}
				else
				{
					// Refresh the token
					WebServiceAuthProvider.refreshAccessToken(context);
					refreshed++;
				}
			}
		} // while(!refreshLimitReached)

		// There is an error other than a refresh error, so return the response
		return response;
	}

	/**
	 * Synchronous call to get a new client credentials token, required for creating new account
	 * 
	 * @param url
	 * @param clientCredentialsToken
	 * @param httpMethod
	 * @param httpBody
	 * @return The data from the server as a string, in almost all cases JSON
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ProtocolException
	 * @throws JSONException
	 */
	public static String getClientCredentialsResponse(Context context, String url, String clientCredentialsToken,
			String httpMethod, Bundle httpBody) throws MalformedURLException, IOException, ProtocolException, JSONException
	{
		boolean refreshLimitReached = false;
		int refreshed = 0;

		String response = "";
		while (!refreshLimitReached)
		{
			// If we have refreshed max number of times, we will not do so again
			if (refreshed == 1)
			{
				refreshLimitReached = true;
			}

			HttpsURLConnection connection = createSecureConnection(new URL(addParameters(context, url)));
			trustAllHosts();
			connection.setHostnameVerifier(DO_NOT_VERIFY);
			connection.setRequestMethod(httpMethod);
			connection.setDoOutput(true);
			connection.setDoInput(true);
			String authValue = "Bearer " + clientCredentialsToken;
			connection.setRequestProperty("Authorization", authValue);
			connection.connect();

			if (!httpBody.isEmpty())
			{
				OutputStream os = new BufferedOutputStream(connection.getOutputStream());
				os.write(encodePostBody(httpBody).getBytes());
				os.flush();
			}

			try
			{
				LoggingUtils.d(LOG_TAG, connection.toString());
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

			// Allow for calls to return nothing
			if (response.length() == 0)
			{
				return "";
			}

			// Check for JSON parsing errors (will throw JSONException is can't be parsed)
			JSONObject object = new JSONObject(response);

			// If no error, return response
			if (!object.has("error"))
			{
				return response;
			}
			// If there is a refresh token error, refresh the token
			else if (object.getString("error").equals("invalid_token"))
			{
				if (refreshLimitReached)
				{
					// Give up
					return response;
				}
				else
				{
					// Refresh the token
					WebServiceAuthProvider.refreshAccessToken(context);
					refreshed++;
				}
			}
		} // while(!refreshLimitReached)

		return response;
	}


	/**
	 * Synchronous call for logging in
	 * 
	 * @param httpBody
	 * @return The data from the server as a string, in almost all cases JSON
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws JSONException
	 */
	public static String getLoginResponse(Bundle httpBody) throws MalformedURLException, IOException
	{
		String response = "";
		URL url = new URL(WebServiceAuthProvider.tokenURL());
		trustAllHosts();
		HttpsURLConnection connection = createSecureConnection(url);
		connection.setHostnameVerifier(DO_NOT_VERIFY);
		String authString = "mobile_android:secret";
		String authValue = "Basic " + Base64.encodeToString(authString.getBytes(), Base64.NO_WRAP);

		connection.setRequestMethod("POST");
		connection.setRequestProperty("Authorization", authValue);
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.connect();

		OutputStream os = new BufferedOutputStream(connection.getOutputStream());
		os.write(encodePostBody(httpBody).getBytes());
		os.flush();
		try
		{
			LoggingUtils.d(LOG_TAG, connection.toString());
			response = readFromStream(connection.getInputStream());
		}
		catch (MalformedURLException e)
		{
			LoggingUtils
					.e(LOG_TAG, "Error reading stream \"" + connection.getInputStream() + "\". " + e.getLocalizedMessage(), null);
		}
		catch (IOException e)
		{
			response = readFromStream(connection.getErrorStream());
		}
		finally
		{
			connection.disconnect();
		}

		return response;
	}


	/**
	 * Synchronous call for logging out
	 * 
	 * @return The data from the server as a string, in almost all cases JSON
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws JSONException
	 */
	public static String getLogoutResponse(Context context) throws MalformedURLException, IOException, JSONException
	{
		// Refresh if necessary
		if (WebServiceAuthProvider.tokenExpiredHint(context))
		{
			WebServiceAuthProvider.refreshAccessToken(context);
		}

		boolean refreshLimitReached = false;
		int refreshed = 0;

		String response = "";
		while (!refreshLimitReached)
		{
			// If we have refreshed max number of times, we will not do so again
			if (refreshed == 1)
			{
				refreshLimitReached = true;
			}


			URL url = new URL(urlForLogout(context));
			HttpsURLConnection connection = createSecureConnection(url);
			trustAllHosts();
			connection.setHostnameVerifier(DO_NOT_VERIFY);
			String authValue = "Bearer " + SDKSettings.getSharedPreferenceString(context, "access_token");

			connection.setRequestMethod("POST");
			connection.setRequestProperty("Authorization", authValue);
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.connect();

			try
			{
				LoggingUtils.d(LOG_TAG, "LogoutResponse" + connection.toString());
				response = readFromStream(connection.getInputStream());
			}
			catch (MalformedURLException e)
			{
				LoggingUtils.e(LOG_TAG, "Error reading stream \"" + connection.getInputStream() + "\". " + e.getLocalizedMessage(),
						null);
			}
			catch (IOException e)
			{
				response = readFromStream(connection.getErrorStream());
			}
			finally
			{
				connection.disconnect();
			}

			// Allow for calls to return nothing
			if (response.length() == 0)
			{
				return "";
			}

			// Check for JSON parsing errors (will throw JSONException is can't be parsed)
			JSONObject object = new JSONObject(response);

			// If no error, return response
			if (!object.has("error"))
			{
				return response;
			}
			// If there is a refresh token error, refresh the token
			else if (object.getString("error").equals("invalid_token"))
			{
				if (refreshLimitReached)
				{
					// Give up
					return response;
				}
				else
				{
					// Refresh the token
					WebServiceAuthProvider.refreshAccessToken(context);
					refreshed++;
				}
			}
		} // while(!refreshLimitReached)

		return response;
	}

}
