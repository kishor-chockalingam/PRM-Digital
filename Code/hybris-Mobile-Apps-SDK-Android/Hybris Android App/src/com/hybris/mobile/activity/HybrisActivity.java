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

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.hybris.mobile.R;
import com.hybris.mobile.logging.LoggingUtils;
import com.hybris.mobile.utility.MenuUtil;


public class HybrisActivity extends Activity
{

	private static final String LOG_TAG = HybrisActivity.class.getSimpleName();
	private ProgressDialog mProgressDialog;

	/**
	 * Handle Action Menu interactions
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		return MenuUtil.onOptionsItemSelected(item, this);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		invalidateOptionsMenu();
	}

	/**
	 * Set up the action menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuUtil.onCreateOptionsMenu(menu, this);
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setTitle(getString(R.string.loading_message));
		mProgressDialog.setCancelable(false);

	}

	/**
	 * Show/Hide progress dialog
	 * 
	 * @param show
	 * @param resMessageId
	 */
	private void showProgressDialog(final boolean show, final int resMessageId)
	{
		if (show)
		{
			if (!mProgressDialog.isShowing())
			{

				String message = getString(R.string.loading_message_default);

				try
				{
					mProgressDialog.setMessage(getString(resMessageId));
				}
				catch (Exception e)
				{
					LoggingUtils.e(LOG_TAG, e.getLocalizedMessage(), null);
				}

				mProgressDialog.setMessage(message);
				mProgressDialog.show();
			}
		}
		else
		{
			if (mProgressDialog.isShowing())
			{
				mProgressDialog.dismiss();
			}

		}
	}

	/**
	 * Show/Hide the loading view with a specific message
	 * 
	 * @param show
	 * @param resStringMessage
	 */
	public void showLoadingDialog(boolean show, int resStringMessage)
	{
		showProgressDialog(show, resStringMessage);
	}

	/**
	 * Show/Hide the loading view with the default message
	 * 
	 * @param show
	 */
	public void showLoadingDialog(final boolean show)
	{
		showProgressDialog(show, 0);
	}
}
