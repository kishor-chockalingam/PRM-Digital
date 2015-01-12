package com.accenture.hybris.mobile.beacons;

import com.estimote.sdk.Region;

public class Constants {			
	public static final int REQUEST_ALL_REGION = 1;
    public static final int REQUEST_ENTRY = 2;
    public static final int REQUEST_EXIT = 3;
    
    public static final int RESULT_ENTRY = 4;
    public static final int RESULT_EXIT = 5;
    public static final int RESULT_CAMERA_ENTRY = 6;

    public static final String FILTER = "com.hybris.mobile.MONITOR_REGION";
    public static final String COMMAND = "COMMAND";
    public static final String DATA = "DATA";
    
    public static final String REGIONID = "REGIONID";
    public static final String PROXIMITY_UUID ="PROXIMITY_UUID";
    public static final String KEY_MAJOR = "KEY_MAJOR";
    public static final String KEY_MINOR = "KEY_MINOR";
    public static final Region ALL_ESTIMOTE_BEACONS_REGION = new Region("rid", null, null, null);
    public static final String ENTRYPROXIMITY_UUID = "eda4b92b07ab4ca48ecd43d53123bd98";
    //public static final String ENTRYPROXIMITY_UUID = "eda4b92b-07ab-4ca4-8ecd-43d53123bd98";
    //9644f07f-d1b3-4e91-ac18-d5ed89bd2f77
    public static final int STORE_MAJOR = 2222;
    public static final int STORE_ENTRY_MINOR = 1234;
    public static final int STORE_EXIT_MINOR = 4321;
    public static final String CUSTOM_INTENT="com.hybris.mobile.beacons.BeaconsBgService.action.NOTIFY";
    
}
