package com.hybris.mobile.beacons;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.hybris.mobile.Hybris;
import com.hybris.mobile.InternalConstants;
import com.hybris.mobile.SDKSettings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.support.v4.app.NotificationCompat.Action;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.hybris.mobile.*;

import android.app.Activity;

import com.hybris.mobile.loader.RESTLoader;
import com.hybris.mobile.loader.RESTLoaderObserver;
import com.hybris.mobile.loader.RESTLoaderResponse;


public class BeaconReceiver extends BroadcastReceiver {


	private final String TAG =  BeaconReceiver.class.getSimpleName();
    
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.i(TAG,"Received boot completed msg, starting beacons bg service");
		Intent localintent = new Intent(context, BeaconBgService.class);
		localintent.putExtra(Constants.COMMAND, Constants.REQUEST_ALL_REGION);
		Log.i(TAG,"Sending intent to start beacons bg service");
		context.startService(localintent);
	}
}
