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

import org.apache.commons.lang3.StringUtils;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.hybris.mobile.DataConstants;
import com.hybris.mobile.Hybris;
import com.hybris.mobile.R;
import com.hybris.mobile.WebserviceMethodEnums;
import com.hybris.mobile.factory.barcode.IntentBarcode;
import com.hybris.mobile.factory.barcode.IntentBarcodeFactory;
import com.hybris.mobile.fragment.LoginFragment;
import com.hybris.mobile.fragment.RegistrationFragment;
import com.hybris.mobile.loader.RESTLoader;
import com.hybris.mobile.loader.RESTLoaderObserver;
import com.hybris.mobile.loader.RESTLoaderResponse;
import com.hybris.mobile.query.QueryCustomer;
import com.hybris.mobile.query.QueryObjectId;
import com.hybris.mobile.utility.MenuUtil;


public class LoginActivity extends HybrisActivity implements RESTLoaderObserver
{

	private Handler mHandler;
	private EditText mEmailView;
	private EditText mPassView;
	private ViewPager mViewPager;
	private IntentBarcode mIntentBarcodeAfterLogin;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
		mViewPager = (ViewPager) findViewById(R.id.pager);
		Fragment[] frags =
		{ new LoginFragment(), new RegistrationFragment() };
		FragmentPagerAdapter adapter = new TabsAdapter(getFragmentManager(), frags);
		mViewPager.setAdapter(adapter);
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
		{
			@Override
			public void onPageSelected(int position)
			{
				getActionBar().setSelectedNavigationItem(position);
			}
		});

		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		MyTabListener tabListener = new MyTabListener(mViewPager, actionBar);
		Tab tab = actionBar.newTab().setText(R.string.tab_login).setTabListener(tabListener);
		actionBar.addTab(tab);

		tab = actionBar.newTab().setText(R.string.tab_register).setTabListener(tabListener);
		actionBar.addTab(tab);
		actionBar.setSelectedNavigationItem(0);

		// Handler to handle different asynchronous cases:
		// - When the user arrives from the scanning activity and scan a barcode that needs the user to be logged in, 
		mHandler = new Handler()
		{

			@Override
			public void handleMessage(Message msg)
			{
				switch (msg.what)
				{
				// Error retrieving the data of the scanned value, we finish the activity and display a error message
					case BarCodeScannerActivity.MSG_DATA_ERROR:
						showMessage((String) msg.obj);
						finish();
						break;

					// Data available, we can start the activity associated with the intent
					case BarCodeScannerActivity.MSG_DATA_AVAILABLE:
						if (mIntentBarcodeAfterLogin != null)
						{
							mIntentBarcodeAfterLogin.startActivity();
							finish();
						}
						break;

				}

			}

		};

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		return MenuUtil.onOptionsItemSelected(item, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		return true;
	}

	public void initViews()
	{
		mEmailView = (EditText) findViewById(R.id.txt_email);
		mPassView = (EditText) findViewById(R.id.txt_password);
	}

	public void requestPassword(View view)
	{
		if (StringUtils.isEmpty(mEmailView.getText().toString()))
		{
			showMessage(getString(R.string.please_enter_email_address));
		}
		else
		{
			QueryObjectId query = new QueryObjectId();
			query.setObjectId(mEmailView.getText().toString());
			RESTLoader.execute(this, WebserviceMethodEnums.METHOD_REQUEST_PASSWORD, query, this, true, true);
		}
	}

	public void login(View view)
	{
		QueryCustomer query = new QueryCustomer();
		query.setLogin(mEmailView.getText().toString());
		query.setPassword(mPassView.getText().toString());
		RESTLoader.execute(this, WebserviceMethodEnums.METHOD_LOGIN, query, this, true, true);
	}

	public static final class TabsAdapter extends FragmentPagerAdapter
	{
		private final Fragment[] mFragments;

		public TabsAdapter(FragmentManager fm, Fragment[] fragments)
		{
			super(fm);
			mFragments = fragments;
		}

		@Override
		public int getCount()
		{
			return mFragments.length;
		}

		@Override
		public Fragment getItem(int position)
		{
			return mFragments[position];
		}
	}

	public static class MyTabListener implements android.app.ActionBar.TabListener
	{

		private ViewPager mViewPager;
		private ActionBar mActionBar;

		public MyTabListener(ViewPager viewPager, ActionBar actionBar)
		{
			mViewPager = viewPager;
			this.mActionBar = actionBar;
		}

		public void onTabSelected(Tab tab, FragmentTransaction ft)
		{
			mViewPager.setCurrentItem(tab.getPosition());

			// Don't do it like this, because tabs use all upper case text:
			// mActionBar.setTitle(tab.getText());
			int resId = tab.getPosition() == 1 ? R.string.actionBarTitle_register : R.string.actionBarTitle_login;
			mActionBar.setTitle(resId);
		}

		public void onTabUnselected(Tab tab, FragmentTransaction ft)
		{
		}

		public void onTabReselected(Tab tab, FragmentTransaction ft)
		{
		}
	}

	/**
	 * This asynchronous task check the barcode consistency with our system: format recognition and value matching
	 * something
	 */
	private class CheckDataBeforeLaunchingIntentTask extends AsyncTask<Void, Void, Void>
	{

		@Override
		protected Void doInBackground(Void... params)
		{

			mIntentBarcodeAfterLogin = IntentBarcodeFactory.getIntent(getIntent().getStringExtra(DataConstants.INTENT_DESTINATION),
					getIntent().getExtras(), LoginActivity.this);

			// we check the data availability
			if (mIntentBarcodeAfterLogin != null)
			{
				// We check the availability of the data associated with the barcode
				mIntentBarcodeAfterLogin.checkDataAvailability(mHandler);
			}

			return null;
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			showLoadingDialog(true);
		}

		@Override
		protected void onPostExecute(Void result)
		{
			super.onPostExecute(result);
			showLoadingDialog(false);
		}

	}

	/**
	 * Show a message
	 * 
	 * @param message
	 */
	private void showMessage(String message)
	{
		Toast.makeText(Hybris.getAppContext(), message, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onReceiveResult(RESTLoaderResponse restLoaderResponse, WebserviceMethodEnums webserviceEnumMethod)
	{

		if (restLoaderResponse.getCode() == RESTLoaderResponse.SUCCESS)
		{

			switch (webserviceEnumMethod)
			{
				case METHOD_LOGIN:
					Hybris.setUserOnline(true);
					Hybris.setUsername(mEmailView.getText().toString());

					if (getCurrentFocus() != null)
					{
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
					}

					showMessage(getString(R.string.login_successful));

					// Redirection to the right intent
					if (getIntent().hasExtra(DataConstants.INTENT_DESTINATION))
					{
						new CheckDataBeforeLaunchingIntentTask().execute();
					}
					else
					{
						finish();
					}
					break;
				case METHOD_REQUEST_PASSWORD:
					showMessage(getString(R.string.new_password_sent));
					break;

				default:
					break;
			}
		}
		else if (restLoaderResponse.getCode() == RESTLoaderResponse.ERROR)
		{
			switch (webserviceEnumMethod)
			{
				case METHOD_LOGIN:
					showMessage(getString(R.string.error_login));
					break;

				default:
					break;
			}
		}
	}

}
