/**
 * 
 */
package com.hybris.mobile.beacons;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import android.app.Activity;
import android.app.IntentService;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.BeaconManager.MonitoringListener;
import com.estimote.sdk.Utils.*;
import com.estimote.sdk.utils.L;
import com.hybris.mobile.Hybris;
import com.hybris.mobile.beacons.BeaconsReceiverNotifier;

/**
 * @author vijay
 * 
 */
public class BeaconBgService extends IntentService {
	
	private static final String TAG = BeaconBgService.class.getSimpleName();
	
	private Region entryRegion;
	private Region exitRegion;
	private Region cameraDeptRegion;
	private BeaconManager beaconManager;
	private static final int REQUEST_ENABLE_BT = 1234;

	public BeaconBgService() {
		super("BeaconBgService");
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		try {
			if (beaconManager != null) {
				beaconManager.setMonitoringListener(null);
				beaconManager
						.stopMonitoring(Constants.ALL_ESTIMOTE_BEACONS_REGION);
				beaconManager.disconnect();
			}
		} catch (RemoteException e) {
			Log.e(TAG, "Error closing monitoring: " + e);
		}
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return START_STICKY;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		L.enableDebugLogging(false);
		Log.i(TAG, "Calling onCreate");
		entryRegion = new Region("rid", Constants.ENTRYPROXIMITY_UUID.toString(),
				Constants.STORE_MAJOR, Constants.STORE_ENTRY_MINOR);
		entryRegion = new Region("rid", Constants.ENTRYPROXIMITY_UUID.toString(),
				Constants.STORE_MAJOR, Constants.STORE_EXIT_MINOR);
		beaconManager = new BeaconManager(this);
		beaconManager.setBackgroundScanPeriod(TimeUnit.SECONDS.toMillis(2), 0);
		Log.i(TAG, "registering monitor listener");
		beaconManager.setMonitoringListener(myMonitorLisnter);
		Log.i(TAG, "Calling connectTOService_onCreate");
		connectToService(Constants.ALL_ESTIMOTE_BEACONS_REGION);
	}

	private MonitoringListener myMonitorLisnter = new MonitoringListener() {

		@Override
		public void onExitedRegion(Region arg0) {
			// TODO Auto-generated method stub
			if(arg0 == null) {
				return;
			}
			Log.i(TAG, "Calling onExitedRegion");
			//if (arg0.getProximityUUID() == exitRegion.getProximityUUID()) {
				// exiting store
				Log.i(TAG, "Matched storeExit id");
				pushNotification(Constants.RESULT_EXIT);
			//}
		}

		@Override
		public void onEnteredRegion(Region arg0, List<Beacon> arg1) {
			// TODO Auto-generated method stub
			Log.i(TAG, "Calling onEnteredRegion");
			Log.i(TAG, "found beacons count: " + arg1.size());
			Log.i(TAG, "Entry UUID: " + entryRegion.getProximityUUID());
			//Log.i(TAG, "Arg1- UUID: " + arg1.get(0).getProximityUUID());
			if(arg1.size() > 0) {
				if (arg1.size() == 1) {
					if (entryRegion.getProximityUUID().contentEquals(
							arg1.get(0).getProximityUUID())) {
						// entering store
						Log.i(TAG, "Matched storeEntry id");
						pushNotification(Constants.RESULT_ENTRY);
					}
				} else {
					for (Beacon beacon : arg1) {
						if (entryRegion.getProximityUUID().contentEquals(
								beacon.getProximityUUID())) {
							Log.i(TAG, "Matched in list of beacons");
							pushNotification(Constants.RESULT_ENTRY);
						} else if (cameraDeptRegion.getProximityUUID()
								.contentEquals(beacon.getProximityUUID())) {
							Log.i(TAG, "Matched to camera dept in store");
							pushNotification(Constants.RESULT_CAMERA_ENTRY);
						}
					}
				}
			}
		}
	};

	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	protected void onHandleIntent(Intent intent) {
		// recive region info from client
		// monitor the region to listen to entry/exit
		// send intents for entry/exit of regions
		// Bundle data = intent.getExtras();
		//
		// switch (data.getInt(Constants.COMMAND,0)) {
		//
		// case Constants.REQUEST_ALL_REGION:
		// // call define region
		// // all region - generic
		// // range the beacons
		// Log.i(TAG,"Calling connect to service");
		// connectToService(Constants.ALL_ESTIMOTE_BEACONS_REGION);
		// break;
		// case Constants.REQUEST_ENTRY:
		// // call define region
		// entryRegion = defineRegion(data);
		// // monitor region
		// connectToService(entryRegion);
		// break;
		// case Constants.REQUEST_EXIT:
		// exitRegion = defineRegion(data);
		// connectToService(exitRegion);
		// break;
		// default:
		// break;
		Log.i(TAG, "Calling connectTOService_handleIntent");
		connectToService(Constants.ALL_ESTIMOTE_BEACONS_REGION);
		// }
	}

	// private Region defineRegion(Bundle data) {
	// Region region;
	// String regionId = data.getString(Constants.REGIONID);
	// int major = data.getInt(Constants.KEY_MAJOR, 0);
	// int minor = data.getInt(Constants.KEY_MINOR, 0);
	// String proximity_Uuid = data.getString(Constants.PROXIMITY_UUID);
	// region = new Region(regionId, proximity_Uuid, major, minor);
	// return region;
	// }

	private void connectToService(final Region region) {
		Log.d(TAG, "scanning for beacon devices");
		beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
			@Override
			public void onServiceReady() {
				try {
					beaconManager.startMonitoring(region);
					Log.d(TAG, "start monitorning all beacons in region");
				} catch (RemoteException e) {
					Log.e(TAG,
							"Can not start ranging of beacons "
									+ e.getMessage());
				}
			}
		});
	}

	private void pushNotification(int result) {
		Log.i(TAG,"Sending notification intent: "+ result);
		Intent intent = new Intent(this, BeaconsReceiverNotifier.class);
	    intent.setAction(Constants.CUSTOM_INTENT);
		intent.putExtra(Constants.COMMAND, result);
		intent.putExtra(Constants.DATA, result);
		//LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
		this.sendBroadcast(intent);
	}
}
