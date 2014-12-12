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
import java.util.Iterator;
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
import com.hybris.mobile.query.QueryAddress;
import com.hybris.mobile.query.QueryPaymentInfo;
import com.hybris.mobile.utility.JsonUtils;
import com.hybris.mobile.utility.MenuUtil;
import com.jayway.jsonpath.JsonPath;


public class PaymentDetailActivity extends FormPopupSelectActivity implements RESTLoaderObserver
{
	private static final String LOG_TAG = PaymentDetailActivity.class.getSimpleName();
	private List<GenericNameCode> mTitles;
	private List<GenericNameCode> mCountries;
	private List<GenericNameCode> mCardTypes;
	private String mPaymentID = "";
	private ArrayList<String> mArraySave;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState, getString(R.string.payment_details_plist));
		setTitle(R.string.payment_details_page_title);
		setContentView(R.layout.payment_detail);
		mTitles = new ArrayList<GenericNameCode>();
		mCountries = new ArrayList<GenericNameCode>();
		mCardTypes = new ArrayList<GenericNameCode>();
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		loadTitles();
		loadCountries();
		loadCardTypes();
		handleIntent(getIntent());
		fieldsValidated();
	}

	@Override
	public void save(View view)
	{
		onMenuSubmit();
	}

	@Override
	protected void fieldsValidated()
	{
		Button btn = (Button) findViewById(R.id.btnSave);
		if (((FormAdapter) getListAdapter()).getIsValid())
		{
			btn.setEnabled(true);
			btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_main));
		}
		else
		{
			btn.setEnabled(false);
			btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_main_disabled));
		}
	}

	@SuppressWarnings("unchecked")
	private void handleIntent(Intent intent)
	{
		if (intent.hasExtra("value"))
		{
			try
			{
				JSONObject payment = new JSONObject(intent.getStringExtra("value"));
				mPaymentID = payment.getString("id");
				for (int i = 0; i < entries.size(); i++)
				{
					Hashtable<String, Object> dict = (Hashtable<String, Object>) entries.get(i);
					String path = "$." + dict.get("property").toString();
					String value = "";
					try
					{
						// We append a String a the end to handle the non String objects
						value = JsonPath.read(payment.toString(), path) + "";
					}
					catch (Exception exp)
					{
						value = "";
					}

					if (value != null)
					{
						dict.put("value", value);
					}

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

	private void loadCardTypes()
	{
		RESTLoader.execute(this, WebserviceMethodEnums.METHOD_CARDTYPES, null, this, true, true);
	}

	private void loadTitles()
	{
		RESTLoader.execute(this, WebserviceMethodEnums.METHOD_TITLES, null, this, true, true);
	}

	private void loadCountries()
	{
		RESTLoader.execute(this, WebserviceMethodEnums.METHOD_DELIVERY_COUNTRIES, null, this, true, true);
	}


	private String getCode(String name, List<GenericNameCode> listToSearch, boolean isoCode)
	{
		boolean objectFound = false;
		String returnResult = "";
		Iterator<GenericNameCode> iterObject = listToSearch.iterator();

		while (!objectFound && iterObject.hasNext())
		{
			GenericNameCode genericNameCode = iterObject.next();

			if (StringUtils.equals(genericNameCode.getName(), name))
			{
				objectFound = true;
				if (isoCode)
				{
					returnResult = genericNameCode.getIsocode();
				}
				else
				{
					returnResult = genericNameCode.getCode();
				}

			}
		}

		return returnResult;
	}

	@Override
	public void onSubmit(final ArrayList<String> array)
	{

		String titleCode = getCode(array.get(7), mTitles, false);
		String countryCode = getCode(array.get(14), mCountries, true);
		String cardCode = getCode(array.get(0), mCardTypes, false);

		if (array.get(1).toString().trim().length() == 0 || array.get(2).toString().trim().length() == 0
				|| array.get(8).toString().trim().length() == 0 || array.get(12).toString().trim().length() == 0)
		{
			Toast.makeText(getApplicationContext(), R.string.generic_missing_required_fields, Toast.LENGTH_LONG).show();
			return;
		}

		mArraySave = array;

		if (mPaymentID.length() == 0)
		{
			QueryPaymentInfo query = new QueryPaymentInfo();
			QueryAddress queryAddress = new QueryAddress();
			query.setQueryAddress(queryAddress);
			query.setAccountHolderName(array.get(2));
			query.setCardNumber(array.get(1));
			query.setCardType(cardCode);
			query.setExpiryMonth(array.get(3));
			query.setExpiryYear(array.get(4));
			query.setShouldSave(Boolean.parseBoolean(array.get(5)));
			query.setDefault(Boolean.parseBoolean(array.get(6)));
			queryAddress.setTitleCode(titleCode);
			queryAddress.setFirstName(array.get(8));
			queryAddress.setLastName(array.get(9));
			queryAddress.setAddressLine1(array.get(10));
			queryAddress.setAddressLine2(array.get(11));
			queryAddress.setPostCode(array.get(12));
			queryAddress.setTown(array.get(13));
			queryAddress.setCountryISOCode(countryCode);

			RESTLoader.execute(this, WebserviceMethodEnums.METHOD_CREATE_PAYMENT_INFO, query, this, true, true);
		}
		else
		{

			QueryPaymentInfo query = new QueryPaymentInfo();
			query.setAccountHolderName(array.get(2));
			query.setCardNumber(array.get(1));
			query.setCardType(cardCode);
			query.setExpiryMonth(array.get(3));
			query.setExpiryYear(array.get(4));
			query.setShouldSave(Boolean.parseBoolean(array.get(5)));
			query.setDefault(Boolean.parseBoolean(array.get(6)));
			query.setPaymentId(mPaymentID);

			RESTLoader.execute(this, WebserviceMethodEnums.METHOD_UPDATE_PAYMENT_INFO, query, this, true, true);
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
				case METHOD_UPDATE_BILLING_ADDRESS:
				case METHOD_CREATE_ADDRESS:
				case METHOD_CREATE_PAYMENT_INFO:
					Toast.makeText(getApplicationContext(), R.string.generic_success_message_popup, Toast.LENGTH_SHORT).show();
					finish();
					break;

				case METHOD_CARDTYPES:
					response = JsonUtils.fromJsonList(restLoaderResponse.getData(), "cardTypes", GenericNameCode.class);

					mCardTypes.addAll(response);

					List<String> cards = new ArrayList<String>();

					for (GenericNameCode genericNameCode : response)
					{
						cards.add(genericNameCode.getName());
					}

					dict = (Hashtable<String, Object>) entries.get(0);
					dict.put("values", cards);
					break;

				case METHOD_DELIVERY_COUNTRIES:
					response = JsonUtils.fromJsonList(restLoaderResponse.getData(), "countries", GenericNameCode.class);

					mCountries.addAll(response);

					List<String> countries = new ArrayList<String>();

					for (GenericNameCode genericNameCode : response)
					{
						countries.add(genericNameCode.getName());
					}

					dict = (Hashtable<String, Object>) entries.get(14);
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

					dict = (Hashtable<String, Object>) entries.get(7);
					dict.put("values", titles);
					break;

				case METHOD_UPDATE_PAYMENT_INFO:

					QueryPaymentInfo query = new QueryPaymentInfo();
					QueryAddress queryAddress = new QueryAddress();
					query.setQueryAddress(queryAddress);
					query.setDefault(Boolean.parseBoolean(mArraySave.get(6)));
					queryAddress.setFirstName(mArraySave.get(8));
					queryAddress.setLastName(mArraySave.get(9));
					queryAddress.setTitleCode(getCode(mArraySave.get(7), mTitles, false));
					queryAddress.setAddressLine1(mArraySave.get(10));
					queryAddress.setAddressLine2(mArraySave.get(11));
					queryAddress.setTown(mArraySave.get(12));
					queryAddress.setPostCode(mArraySave.get(13));
					queryAddress.setCountryISOCode(getCode(mArraySave.get(14), mCountries, true));
					query.setPaymentId(mPaymentID);

					RESTLoader.execute(this, WebserviceMethodEnums.METHOD_UPDATE_BILLING_ADDRESS, query, this, true, true);
					break;

				default:
					break;

			}
		}
		else if (restLoaderResponse.getCode() == RESTLoaderResponse.ERROR)
		{
			switch (webserviceEnumMethod)
			{
				case METHOD_UPDATE_BILLING_ADDRESS:
				case METHOD_CREATE_ADDRESS:
				case METHOD_CREATE_PAYMENT_INFO:
				case METHOD_UPDATE_PAYMENT_INFO:
					Toast.makeText(getApplicationContext(), restLoaderResponse.getData(), Toast.LENGTH_LONG).show();
					break;

				default:
					break;
			}
		}

	}
}
