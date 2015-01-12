package com.accenture.hybris.mobile.beacons;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


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
