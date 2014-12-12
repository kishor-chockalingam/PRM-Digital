package com.hybris.mobile.beacons;

import java.io.IOException;

import com.hybris.mobile.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


public class BeaconsReceiverNotifier extends BroadcastReceiver {
	private NotificationManager notificationManager;
	private static final int NOTIFICATION_ID = 123;
	private final String TAG =  BeaconsReceiverNotifier.class.getSimpleName();
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals(Constants.CUSTOM_INTENT)) {
		// TODO Auto-generated method stub
			notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			Log.d(TAG, "Got message");
			Bundle data = intent.getExtras();
			switch (data.getInt(Constants.COMMAND, 0)) {
			case Constants.RESULT_ENTRY:
				int dentry = data.getInt(Constants.DATA, 0);
				Log.d(TAG, "notifying welcome message");
				createNotification(context, R.string.welcome_promo, "Welcome", dentry);
				// hit webservice with UUId of device
				try {
					sendCustomerStoreInfo(context);
				} catch (IOException ie) {
					Log.e(TAG, "Error " + ie);
				}
				break;
			case Constants.RESULT_EXIT:
				int dexit = data.getInt(Constants.DATA, 0);
				Log.d(TAG, "notifying thankyou message");
				createNotification(context, R.string.goodbye_promo, "Thanks You", dexit);
				break;
			default:
				break;
			}
		}
	}
	private void createNotification(Context context,int rid, String title,int code) {

		Intent notifyIntent;
		if(code == Constants.RESULT_ENTRY) {
			notifyIntent = new Intent(context, com.hybris.mobile.activity.ObjectListActivity.class);
		} else { //if( code == Constants.RESULT_EXIT) 
			notifyIntent = new Intent(context, com.hybris.mobile.activity.ObjectListActivity.class);
		}
		notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivities(context, 0,
				new Intent[] { notifyIntent },
				PendingIntent.FLAG_UPDATE_CURRENT);
		Notification notification = new Notification.Builder(context)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(title).setContentText(context.getText(rid))
				.setAutoCancel(true).setContentIntent(pendingIntent).build();
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_LIGHTS;
		Log.d(TAG, "showing notification msg");
		notificationManager.notify(NOTIFICATION_ID, notification);
	}

	private void sendCustomerStoreInfo(Context context) throws IOException {

		CustomerUuidPost customerUuidPost = new CustomerUuidPost();
		Log.d(TAG, "creating asyn task to hit UUID webservice");
		customerUuidPost.execute();
	}

}
