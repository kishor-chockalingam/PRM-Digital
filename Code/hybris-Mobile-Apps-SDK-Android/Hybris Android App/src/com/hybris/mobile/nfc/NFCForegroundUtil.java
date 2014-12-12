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
package com.hybris.mobile.nfc;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;


public class NFCForegroundUtil
{

	private NfcAdapter nfc = null;
	private Activity activity;
	private PendingIntent intent;


	public NFCForegroundUtil(Activity activity)
	{
		super();
		this.activity = activity;
		nfc = NfcAdapter.getDefaultAdapter(activity.getApplicationContext());

		intent = PendingIntent.getActivity(activity, 0, new Intent(activity, activity.getClass()), 0);
	}

	public void enableForeground()
	{
		if (nfc != null)
		{
			nfc.enableForegroundDispatch(activity, intent, null, null);
		}
	}

	public void disableForeground()
	{
		if (nfc != null)
		{
			nfc.disableForegroundDispatch(activity);
		}
	}

	public NfcAdapter getNfc()
	{
		return nfc;
	}

}
