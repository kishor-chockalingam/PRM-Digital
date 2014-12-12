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

import java.util.List;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.hybris.mobile.R;
import com.hybris.mobile.logging.LoggingUtils;
import com.hybris.mobile.utility.MenuUtil;


public class HybrisListActivity extends ListActivity
{

	private static final String LOG_TAG = HybrisListActivity.class.getSimpleName();
	private ProgressDialog mProgressDialog;
	private List<ActivityManager.RunningTaskInfo> mTaskList;

	/**
	 * Handle Action Menu interactions
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		return MenuUtil.onOptionsItemSelected(item, this);
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

		ActivityManager mngr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		mTaskList = mngr.getRunningTasks(10);

	}

	@Override
	protected void onResume()
	{
		super.onResume();
		if (isLastActivity())
		{
			getActionBar().setHomeButtonEnabled(false);
		}
		else
		{
			getActionBar().setHomeButtonEnabled(true);
		}
		invalidateOptionsMenu();

	}

	/**
	 * Show/Hide progress dialog
	 * 
	 * @param show
	 * @param resMessageId
	 */
	private void showProgressDialog(boolean show, final int resMessageId)
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

	protected Boolean isLastActivity()
	{
		if (mTaskList.get(0).numActivities == 1 && mTaskList.get(0).topActivity.getClassName().equals(this.getClass().getName()))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public void onBackPressed()
	{
		boolean restart = false;
		if (getIntent().hasExtra(ObjectListActivity.INTENT_EXTRA_RESTART))
		{
			restart = getIntent().getExtras().getBoolean(ObjectListActivity.INTENT_EXTRA_RESTART);
		}
		if (isLastActivity() || restart)
		{
			new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle(R.string.exit_title)
					.setMessage(R.string.exit_text).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							Intent intent = new Intent(Intent.ACTION_MAIN);
							intent.addCategory(Intent.CATEGORY_HOME);
							startActivity(intent);
							finish();
						}

					}).setNegativeButton(android.R.string.no, null).show();
		}
		else
		{
			super.onBackPressed();
		}
	}

}
