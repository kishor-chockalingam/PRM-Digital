package com.accenture.hybris.mobile.beacons;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.hybris.mobile.Hybris;
import com.hybris.mobile.InternalConstants;
import com.hybris.mobile.SDKSettings;

import android.os.AsyncTask;
import android.util.Log;

public class CustomerUuidPost extends AsyncTask<Void, Void, Void> {

	private final String TAG = CustomerUuidPost.class.getSimpleName(); 
	@Override
	protected Void doInBackground(Void ...params) {
		try {
	
			Log.i(TAG,"Starting to send UUID to webserver");
			// TODO Auto-generated method stub
			HttpClient httpclient = new DefaultHttpClient();
			//String surl="";// = Hybris.getSharedPreferenceString(InternalConstants.KEY_PREF_SPECIFIC_BASE_URL);
			
			String surl = (String) String.format("%s/bncwebservices/v1/electronics/CustomerStoreLogin/9644f07f-d1b3-4e91-ac18-d5ed89bd2f77/Chiba/%s",
					SDKSettings.getSettingValue(InternalConstants.KEY_PREF_BASE_URL),
					Hybris.getSharedPreferenceString(InternalConstants.KEY_PREF_UUID) );
			//URL serviceurl = new URL(surl);
			
			//HttpPost httppost = new HttpPost(serviceurl);
			Log.i(TAG, "URL : " + surl);
			HttpResponse response = httpclient.execute(new HttpGet(surl));
			StatusLine statusLine = response.getStatusLine();
			if(statusLine.getStatusCode() == HttpStatus.SC_OK){
			    ByteArrayOutputStream out = new ByteArrayOutputStream();
			    response.getEntity().writeTo(out);
			    out.close();
			    String responseString = out.toString();
			    Log.i(TAG,"response string is: " + responseString);
			    //Whatever you wanna do with the response
			} else{
			    //Close the connection.
			    response.getEntity().getContent().close();
			    throw new IOException(statusLine.getReasonPhrase());
			}
		} catch (ClientProtocolException ce) {
			Log.e(TAG,"Client Proto error: " + ce.getLocalizedMessage());
		} catch (IOException ie)  {
			Log.e(TAG, "IO Exception error: " + ie.getLocalizedMessage());
			Log.e(TAG, "Same IO Exception error: " + ie.getMessage());
			
		}
		return null;
		
	}

}
