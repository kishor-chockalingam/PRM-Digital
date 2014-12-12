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
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.hybris.mobile.Hybris;
import com.hybris.mobile.R;
import com.hybris.mobile.WebserviceMethodEnums;
import com.hybris.mobile.adapter.FormAdapter;
import com.hybris.mobile.loader.RESTLoader;
import com.hybris.mobile.loader.RESTLoaderObserver;
import com.hybris.mobile.loader.RESTLoaderResponse;
import com.hybris.mobile.logging.LoggingUtils;
import com.hybris.mobile.model.GenericNameCode;
import com.hybris.mobile.query.QueryAddress;
import com.hybris.mobile.utility.JsonUtils;
import com.hybris.mobile.utility.MenuUtil;
import com.jayway.jsonpath.JsonPath;


public class AddressDetailActivity extends FormPopupSelectActivity implements RESTLoaderObserver
{
	private static final String LOG_TAG = AddressDetailActivity.class.getSimpleName();
	private List<GenericNameCode> mTitles;
	private List<GenericNameCode> mCountries;
	private String mAddressID = "";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState, getString(R.string.address_plist));
		setTitle(getIntent().getExtras().getString("title"));
		setContentView(R.layout.address_detail);
		getActionBar().setHomeButtonEnabled(true);
		mTitles = new ArrayList<GenericNameCode>();
		mCountries = new ArrayList<GenericNameCode>();
		loadTitles();
		loadCountries();
		handleIntent(getIntent());
		fieldsValidated();
	}

	@Override
	protected void fieldsValidated()
	{
		Button btn = (Button) findViewById(R.id.btnSave);
		if (((FormAdapter) getListAdapter()).getIsValid())
		{
			btn.setEnabled(true);
		}
		else
		{
			btn.setEnabled(false);
		}
	}

	@SuppressWarnings("unchecked")
	private void handleIntent(Intent intent)
	{
		if (intent.hasExtra("value"))
		{

			try
			{
				JSONObject address = new JSONObject(intent.getStringExtra("value"));

				mAddressID = address.getString("id");
				for (int i = 0; i < entries.size(); i++)
				{
					Hashtable<String, Object> dict = (Hashtable<String, Object>) entries.get(i);
					String path = "$." + dict.get("property").toString();
					String value = "";
					try
					{
						// We append a String a the end to handle the non String objects
						value = JsonPath.read(address.toString(), path) + "";
					}
					catch (Exception exp)
					{
						value = "";
					}
					dict.put("value", value);
				}
			}
			catch (JSONException e)
			{
				LoggingUtils.e(LOG_TAG, "Error parsing Json. " + e.getLocalizedMessage(), Hybris.getAppContext());
			}

		}
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

	private void loadTitles()
	{
		RESTLoader.execute(this, WebserviceMethodEnums.METHOD_TITLES, null, this, true, true);
	}

	private void loadCountries()
	{
		RESTLoader.execute(this, WebserviceMethodEnums.METHOD_DELIVERY_COUNTRIES, null, this, true, true);
	}

	@Override
	public void onSubmit(ArrayList<String> array)
	{

		String titleCode = "";
		String countryCode = "";

		for (GenericNameCode obj : mTitles)
		{
			if (StringUtils.equals(obj.getName(), array.get(0).toString()))
			{
				titleCode = obj.getCode();
				break;
			}
		}

		for (GenericNameCode obj : mCountries)
		{

			if (StringUtils.equals(obj.getName(), array.get(7).toString()))
			{
				countryCode = obj.getIsocode();
				break;
			}

		}

		if (mAddressID.length() == 0)
		{
			QueryAddress query = new QueryAddress();
			query.setFirstName(array.get(1));
			query.setLastName(array.get(2));
			query.setTitleCode(titleCode);
			query.setAddressLine1(array.get(3));
			query.setAddressLine2(array.get(4));
			query.setTown(array.get(5));
			query.setPostCode(array.get(6));
			query.setCountryISOCode(countryCode);

			RESTLoader.execute(this, WebserviceMethodEnums.METHOD_CREATE_ADDRESS, query, this, true, true);
		}
		else
		{
			//else update the existing one
			QueryAddress query = new QueryAddress();
			query.setFirstName(array.get(1));
			query.setLastName(array.get(2));
			query.setTitleCode(titleCode);
			query.setAddressLine1(array.get(3));
			query.setAddressLine2(array.get(4));
			query.setTown(array.get(5));
			query.setPostCode(array.get(6));
			query.setCountryISOCode(countryCode);
			query.setAddressId(mAddressID);

			RESTLoader.execute(this, WebserviceMethodEnums.METHOD_UPDATE_ADDRESS, query, this, true, true);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onReceiveResult(RESTLoaderResponse restLoaderResponse, WebserviceMethodEnums webserviceEnumMethod)
	{

		if (restLoaderResponse.getCode() == RESTLoaderResponse.SUCCESS)
		{

			List<GenericNameCode> response;
			Hashtable<String, Object> dict;

			switch (webserviceEnumMethod)
			{
				case METHOD_CREATE_ADDRESS:
				case METHOD_UPDATE_ADDRESS:
					Toast.makeText(getApplicationContext(), R.string.generic_success_message_popup, Toast.LENGTH_SHORT).show();
					finish();
					getmAdapter().notifyDataSetChanged();
					break;

				case METHOD_DELIVERY_COUNTRIES:

					response = JsonUtils.fromJsonList(restLoaderResponse.getData(), "countries", GenericNameCode.class);

					mCountries.addAll(response);

					List<String> countries = new ArrayList<String>();

					for (GenericNameCode genericNameCode : response)
					{
						countries.add(genericNameCode.getName());
					}

					dict = (Hashtable<String, Object>) entries.get(7);
					dict.put("values", countries);
					break;

				case METHOD_TITLES:

					response = JsonUtils.fromJsonList(restLoaderResponse.getData(), "titles", GenericNameCode.class);

					mTitles.addAll(response);

					List<String> titles = new ArrayList<String>();

					for (GenericNameCode genericNameCode : response)
					{
						titles.add(genericNameCode.getName());
					}

					dict = (Hashtable<String, Object>) entries.get(0);
					dict.put("values", titles);
					break;

				default:
					break;

			}
		}
		else if (restLoaderResponse.getCode() == RESTLoaderResponse.ERROR)
		{
			switch (webserviceEnumMethod)
			{
				case METHOD_CREATE_ADDRESS:
				case METHOD_UPDATE_ADDRESS:
					Toast.makeText(getApplicationContext(), restLoaderResponse.getData(), Toast.LENGTH_LONG).show();
					break;

				default:
					break;
			}
		}

	}

}
