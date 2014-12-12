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
import android.nfc.NfcAdapter;
import android.nfc.Tag;

public class NFCUtil {


	public static boolean supportsNdef(Tag tag) {

		String techs[] = tag.getTechList();
		for (String tech : techs) {
			if (tech.equals("android.nfc.tech.Ndef"))
				return true;
		}

		return false;
	}

	public static boolean supportsNdefFormatable(Tag tag) {

		String techs[] = tag.getTechList();
		for (String tech : techs) {
			if (tech.equals("android.nfc.tech.NdefFormatable"))
				return true;
		}

		return false;
	}

	public static boolean canNFC(Activity activity) {
		NfcAdapter adapter = NfcAdapter.getDefaultAdapter(activity);
		
		if (adapter == null)
			return false;
		
		return adapter.isEnabled();
	}

}
