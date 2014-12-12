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
import android.view.View;
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
import com.hybris.mobile.query.QueryCustomer;
import com.hybris.mobile.utility.JsonUtils;
import com.hybris.mobile.utility.MenuUtil;
import com.jayway.jsonpath.JsonPath;


public class ProfileDetailActivity extends FormActivity implements RESTLoaderObserver
{

	private static final String LOG_TAG = ProfileDetailActivity.class.getSimpleName();
	private List<GenericNameCode> mTitles;
	private List<GenericNameCode> mCurrencies;
	private List<GenericNameCode> mLanguages;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState, getString(R.string.profile_plist));
		setContentView(R.layout.profile_list);
		setTitle(R.string.profile_detail_page_title);
		mTitles = new ArrayList<GenericNameCode>();
		mCurrencies = new ArrayList<GenericNameCode>();
		mLanguages = new ArrayList<GenericNameCode>();
		loadTitles();
		loadCurrencies();
		loadLanguages();
		handleIntent(getIntent());
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


	public void updateProfile(View view)
	{
		onMenuSubmit();
	}

	@SuppressWarnings("unchecked")
	private void handleIntent(Intent intent)
	{
		if (intent.hasExtra("value"))
		{
			try
			{
				JSONObject profile = new JSONObject(intent.getStringExtra("value"));
				for (int i = 0; i < entries.size(); i++)
				{
					Hashtable<String, Object> dict = (Hashtable<String, Object>) entries.get(i);
					String path = "$." + dict.get("property").toString();
					String value = "";
					try
					{
						// We append a String a the end to handle the non String objects
						value = JsonPath.read(profile.toString(), path) + "";
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
				LoggingUtils.e(LOG_TAG, "Error parsing json. " + e.getLocalizedMessage(), Hybris.getAppContext());
			}
		}
	}

	private void loadTitles()
	{
		RESTLoader.execute(this, WebserviceMethodEnums.METHOD_TITLES, null, this, true, true);
	}

	private void loadCurrencies()
	{
		RESTLoader.execute(this, WebserviceMethodEnums.METHOD_CURRENCIES, null, this, true, true);
	}

	private void loadLanguages()
	{
		RESTLoader.execute(this, WebserviceMethodEnums.METHOD_LANGUAGES, null, this, true, true);
	}



	@Override
	public void onSubmit(ArrayList<String> array)
	{

		String titleCode = "";
		String currencyCode = "";
		String languageCode = "";

		for (GenericNameCode obj : mTitles)
		{
			if (StringUtils.equalsIgnoreCase(obj.getName(), array.get(0).toString()))
			{
				titleCode = obj.getCode();
				break;
			}
		}

		for (GenericNameCode obj : mCurrencies)
		{
			if (StringUtils.equalsIgnoreCase(obj.getName(), array.get(3).toString()))
			{
				currencyCode = obj.getIsocode();
				break;
			}
		}

		for (GenericNameCode obj : mLanguages)
		{
			if (StringUtils.equalsIgnoreCase(obj.getName(), array.get(4).toString()))
			{
				languageCode = obj.getIsocode();
				break;
			}
		}

		QueryCustomer query = new QueryCustomer();
		query.setFirstName(array.get(1));
		query.setLastName(array.get(2));
		query.setTitleCode(titleCode);
		query.setLanguage(languageCode);
		query.setCurrency(currencyCode);

		RESTLoader.execute(this, WebserviceMethodEnums.METHOD_UPDATE_PROFILE, query, this, true, true);
	}

	@Override
	public void fieldsValidated()
	{
		Button btn = (Button) findViewById(R.id.btnUpdateProfile);
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
	@Override
	public void onReceiveResult(RESTLoaderResponse restLoaderResponse, WebserviceMethodEnums webserviceEnumMethod)
	{

		if (restLoaderResponse.getCode() == RESTLoaderResponse.SUCCESS)
		{

			List<GenericNameCode> response;
			Hashtable<String, Object> dict;

			switch (webserviceEnumMethod)
			{

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

				case METHOD_CURRENCIES:

					response = JsonUtils.fromJsonList(restLoaderResponse.getData(), "currencies", GenericNameCode.class);

					mCurrencies.addAll(response);

					List<String> currencies = new ArrayList<String>();

					for (GenericNameCode genericNameCode : response)
					{
						currencies.add(genericNameCode.getName());
					}

					dict = (Hashtable<String, Object>) entries.get(3);
					dict.put("values", currencies);
					break;

				case METHOD_LANGUAGES:
					response = JsonUtils.fromJsonList(restLoaderResponse.getData(), "languages", GenericNameCode.class);

					mLanguages.addAll(response);

					List<String> languages = new ArrayList<String>();

					for (GenericNameCode genericNameCode : response)
					{
						languages.add(genericNameCode.getName());
					}

					dict = (Hashtable<String, Object>) entries.get(4);
					dict.put("values", languages);
					break;

				case METHOD_UPDATE_PROFILE:
					Toast.makeText(getApplicationContext(), R.string.generic_success_message_popup, Toast.LENGTH_SHORT).show();
					finish();
					break;

				default:
					break;
			}
		}
		else if (restLoaderResponse.getCode() == RESTLoaderResponse.ERROR)
		{
			switch (webserviceEnumMethod)
			{

				case METHOD_UPDATE_PROFILE:
					Toast.makeText(getApplicationContext(), restLoaderResponse.getData(), Toast.LENGTH_LONG).show();
					break;

				default:
					break;
			}
		}

	}
}
