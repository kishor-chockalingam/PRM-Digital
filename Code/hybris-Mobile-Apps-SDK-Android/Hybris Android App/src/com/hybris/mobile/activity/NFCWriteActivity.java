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
package com.hybris.mobile.activity;

import java.io.IOException;

import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.hybris.mobile.Hybris;
import com.hybris.mobile.R;
import com.hybris.mobile.logging.LoggingUtils;
import com.hybris.mobile.nfc.NFCDialogFragment;
import com.hybris.mobile.nfc.NFCForegroundUtil;
import com.hybris.mobile.nfc.NFCUtil;


public class NFCWriteActivity extends FragmentActivity
{
	private static final String LOG_CAT = NFCWriteActivity.class.getSimpleName();
	public static final String NDEF_MESSAGE = "NDEF_MESSAGE";
	private NdefMessage msg;
	private NFCForegroundUtil nfcForegroundUtil = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nfc_write);

		// acquire tag sent to this activity using intent extra
		Intent i = getIntent();
		if (!i.hasExtra(NDEF_MESSAGE))
		{
			Toast.makeText(this, R.string.error_nfc_no_ndef_message_sent, Toast.LENGTH_LONG).show();
			finish();
		}

	}

	@Override
	protected void onResume()
	{
		super.onResume();

		nfcForegroundUtil = new NFCForegroundUtil(this);
		msg = (NdefMessage) getIntent().getParcelableExtra(NDEF_MESSAGE);
		nfcForegroundUtil.enableForeground();

	}

	@Override
	protected void onNewIntent(Intent intent)
	{
		super.onNewIntent(intent);

		Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

		// default failed
		setResult(RESULT_CANCELED);

		if (NFCUtil.supportsNdef(tag))
		{
			Ndef ndef = Ndef.get(tag);

			try
			{
				int maxSize = ndef.getMaxSize();
				int messageSize = this.msg.toByteArray().length;

				if (ndef.isWritable() && maxSize > messageSize)
				{
					ndef.connect();
					ndef.writeNdefMessage(this.msg);

					if (getResources().getBoolean(R.bool.nfc_read_only))
					{
						if (ndef.canMakeReadOnly())
						{
							boolean success = ndef.makeReadOnly();
							if (!success)
								Toast.makeText(this, "Unable to make tag readonly!", Toast.LENGTH_LONG).show();
							else
								Toast.makeText(this, "Tag is now readonly!", Toast.LENGTH_LONG).show();
						}
						else
						{
							Toast.makeText(this, "This tag cannot be made readonly!", Toast.LENGTH_LONG).show();
						}
					}

					setResult(RESULT_OK);
					showDialogFragmentWithMessage(R.string.nfc_tag_written);
				}
				else
				{
					showDialogFragmentWithMessage(R.string.error_nfc_readonly_size);
				}
			}
			catch (IOException e)
			{
				LoggingUtils.e(LOG_CAT, getString(R.string.error_nfc_io), e, Hybris.getAppContext());

			}
			catch (FormatException e)
			{
				LoggingUtils.e(LOG_CAT, getString(R.string.error_nfc_format), e, Hybris.getAppContext());
			}
			finally
			{
				try
				{
					ndef.close();
				}
				catch (IOException e)
				{
				}
			}

		}
		else if (NFCUtil.supportsNdefFormatable(tag))
		{
			NdefFormatable ndefFormatable = NdefFormatable.get(tag);

			try
			{
				ndefFormatable.connect();

				if (getResources().getBoolean(R.bool.nfc_read_only))
				{
					ndefFormatable.formatReadOnly(this.msg);
				}
				else
				{
					ndefFormatable.format(this.msg);
				}

				setResult(RESULT_OK);
				showDialogFragmentWithMessage(R.string.nfc_tag_written);
			}
			catch (IOException e)
			{
				LoggingUtils.e(LOG_CAT, getString(R.string.error_nfc_io), e, Hybris.getAppContext());
			}
			catch (FormatException e)
			{
				LoggingUtils.e(LOG_CAT, getString(R.string.error_nfc_format), e, Hybris.getAppContext());
			}
			finally
			{
				try
				{
					ndefFormatable.close();
				}
				catch (IOException e)
				{
				}
			}

		}
		else
		{
			showDialogFragmentWithMessage(R.string.error_nfc_no_ndef);
		}

	}

	private void showDialogFragmentWithMessage(int message)
	{
		NFCDialogFragment dialogFragment = NFCDialogFragment.newInstance(message);
		dialogFragment.show(getFragmentManager(), "dialog");
	}

	public void doOKClick()
	{
		finish();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		nfcForegroundUtil.disableForeground();
	}

}
