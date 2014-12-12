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

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hybris.mobile.Hybris;
import com.hybris.mobile.R;
import com.hybris.mobile.WebserviceMethodEnums;
import com.hybris.mobile.loader.RESTLoader;
import com.hybris.mobile.loader.RESTLoaderObserver;
import com.hybris.mobile.loader.RESTLoaderResponse;
import com.hybris.mobile.model.UserProfile;
import com.hybris.mobile.utility.JsonUtils;
import com.hybris.mobile.utility.SwipeDetector;


public class AccountListActivity extends HybrisListActivity implements RESTLoaderObserver
{

	private UserProfile mProfileDetail;
	private ArrayList<String> mLinks;
	private ArrayAdapter<String> mAdapter;

	private TextView mProfileName;
	private TextView mUsername;

	private String mOrderHistory = "Order History";
	private String mUpdateProfile = "Update Profile";
	private String mAddressBook = "Address Book";
	private String mPaymentDetails = "Payment Details";
	private String mChangePassword = "Change Password";
	private String mLogin = "Login";
	private String mLogout = "Logout";

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_list);
		setTitle(R.string.account_page_title);

		mLinks = new ArrayList<String>();
		mAdapter = new ArrayAdapter<String>(this, R.layout.row_singleline_with_icon, R.id.textViewSingleLine, mLinks);
		setListAdapter(mAdapter);

		getListView().setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position, long l)
			{
				Intent intent = null;
				if (StringUtils.equals(mLinks.get(position), mLogin))
				{
					intent = new Intent(AccountListActivity.this, LoginActivity.class);
					startActivity(intent);
				}
				else if (StringUtils.equals(mLinks.get(position), mLogout))
				{
					logoutUser();
				}
				else if (StringUtils.equals(mLinks.get(position), mOrderHistory))
				{
					intent = new Intent(AccountListActivity.this, OrderListActivity.class);
					startActivity(intent);
				}
				else if (StringUtils.equals(mLinks.get(position), mAddressBook))
				{
					intent = new Intent(AccountListActivity.this, AddressListActivity.class);
					startActivity(intent);
				}
				else if (StringUtils.equals(mLinks.get(position), mChangePassword))
				{
					intent = new Intent(AccountListActivity.this, ChangePasswordActivity.class);
					startActivity(intent);
				}
				else if (StringUtils.equals(mLinks.get(position), mUpdateProfile))
				{
					intent = new Intent(AccountListActivity.this, ProfileDetailActivity.class);
					// TODO
					intent.putExtra("value", JsonUtils.toJson(mProfileDetail));
					startActivity(intent);
				}
				else if (StringUtils.equals(mLinks.get(position), mPaymentDetails))
				{
					intent = new Intent(AccountListActivity.this, PaymentListActivity.class);
					startActivity(intent);
				}
				else
				{
					return;
				}
			}
		});

		LinearLayout layoutSwipe = (LinearLayout) findViewById(R.id.layoutSwipe);
		layoutSwipe.setOnTouchListener(new SwipeDetector(new SwipeDetector.OnSwipeListener()
		{

			@Override
			public void onTopToBottomSwipe()
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onRightToLeftSwipe()
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onLeftToRightSwipe()
			{
				Intent intent = new Intent(AccountListActivity.this, SettingsActivity.class);
				startActivity(intent);
			}

			@Override
			public void onBottomToTopSwipe()
			{
				// TODO Auto-generated method stub

			}
		}));
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		RESTLoader.execute(this, WebserviceMethodEnums.METHOD_GET_PROFILE, null, this, true, true);
	}

	private void updateUI()
	{
		mProfileName = (TextView) findViewById(R.id.lbl_user_profile_name);
		mUsername = (TextView) findViewById(R.id.lbl_username);
		if (Hybris.isUserLoggedIn())
		{
			mProfileName.setText(mProfileDetail.getName());
			mProfileName.setVisibility(View.VISIBLE);
			mUsername.setText(Hybris.getUsername());

			mLinks.clear();
			mLinks.add(mOrderHistory);
			mLinks.add(mUpdateProfile);
			mLinks.add(mAddressBook);
			mLinks.add(mPaymentDetails);
			mLinks.add(mChangePassword);
			mLinks.add(mLogout);
			mAdapter.notifyDataSetChanged();
		}
		else
		{
			mProfileName.setText(R.string.not_logged_in);
			mProfileName.setVisibility(View.VISIBLE);
			mUsername.setText("");
			mUsername.setVisibility(View.GONE);

			mLinks.clear();
			mLinks.add(0, mLogin);
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		return true;
	}

	private void logoutUser()
	{
		RESTLoader.execute(this, WebserviceMethodEnums.METHOD_LOGOUT, null, this, true, true);
	}

	@Override
	public void onReceiveResult(RESTLoaderResponse restLoaderResponse, WebserviceMethodEnums webserviceEnumMethod)
	{
		if (restLoaderResponse.getCode() == RESTLoaderResponse.SUCCESS)
		{
			switch (webserviceEnumMethod)
			{

				case METHOD_GET_PROFILE:
					mProfileDetail = JsonUtils.fromJson(restLoaderResponse.getData(), UserProfile.class);
					updateUI();
					break;

				case METHOD_LOGOUT:
					Hybris.setUserOnline(false);
					Hybris.setUsername("");
					updateUI();
					break;

				default:
					break;
			}
		}
		else if (restLoaderResponse.getCode() == RESTLoaderResponse.ERROR)
		{
			switch (webserviceEnumMethod)
			{
				case METHOD_GET_PROFILE:
					logoutUser();
					break;

				case METHOD_LOGOUT:
					Hybris.setUserOnline(false);
					Hybris.setUsername("");
					updateUI();
					break;

				default:
					break;
			}
		}
	}
}
