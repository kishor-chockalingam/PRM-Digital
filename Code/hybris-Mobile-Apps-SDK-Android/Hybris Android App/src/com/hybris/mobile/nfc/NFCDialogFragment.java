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

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.hybris.mobile.R;
import com.hybris.mobile.activity.NFCWriteActivity;


public class NFCDialogFragment extends DialogFragment
{

	public static NFCDialogFragment newInstance(int message)
	{
		NFCDialogFragment frag = new NFCDialogFragment();
		Bundle args = new Bundle();
		args.putInt("message", message);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		int message = getArguments().getInt("message");

		return new AlertDialog.Builder(getActivity()).setTitle(R.string.nfc_dialog_title).setMessage(message).setCancelable(false)
				.setPositiveButton(R.string.nfc_dialog_ok, new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int whichButton)
					{
						NFCWriteActivity activity = (NFCWriteActivity) getActivity();
						dialog.dismiss();
						activity.doOKClick();
					}
				}).create();
	}

}
