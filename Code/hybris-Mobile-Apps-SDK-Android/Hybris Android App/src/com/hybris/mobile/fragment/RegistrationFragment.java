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
package com.hybris.mobile.fragment;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hybris.mobile.R;
import com.hybris.mobile.WebserviceMethodEnums;
import com.hybris.mobile.adapter.FormAdapter.FormDataChangedListner;
import com.hybris.mobile.listener.ActionGo;
import com.hybris.mobile.loader.RESTLoader;
import com.hybris.mobile.loader.RESTLoaderObserver;
import com.hybris.mobile.loader.RESTLoaderResponse;
import com.hybris.mobile.model.GenericNameCode;
import com.hybris.mobile.query.QueryCustomer;
import com.hybris.mobile.utility.JsonUtils;


public class RegistrationFragment extends FormPopupSelectFragment implements FormDataChangedListner, RESTLoaderObserver, ActionGo
{
	private List<GenericNameCode> mTitles;
	private Context applicationContext;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState, getString(R.string.user_registration_plist));
		mTitles = new ArrayList<GenericNameCode>();

		applicationContext = getActivity().getApplicationContext();

		loadTitles();

		getAdapter().setFormDataChangedListner(this);
		getAdapter().setActionGo(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_register, null);
		view.findViewById(R.id.btnRegister).setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				validateAndSubmit();
			}
		});
		return view;
	}

	@Override
	public void onResume()
	{
		super.onResume();
		onFormDataChanged();
	}

	private void loadTitles()
	{
		RESTLoader.execute(getActivity(), WebserviceMethodEnums.METHOD_TITLES, null, this, true, true);
	}

	@Override
	public void submit()
	{
		validateAndSubmit();
	}

	private void validateAndSubmit()
	{
		if (getAdapter().validateAll())
		{
			onMenuSubmit();
		}
		else
		{
			Toast.makeText(applicationContext, R.string.register_invalidValues, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onSubmit(ArrayList<String> array)
	{

		if (!(array.get(4).equals(array.get(5))))
		{
			Toast.makeText(applicationContext, R.string.error_password_mismatch, Toast.LENGTH_LONG).show();
			return;
		}

		String titleCode = "";

		for (GenericNameCode obj : mTitles)
		{
			if (StringUtils.equals(obj.getName(), array.get(0).toString()))
			{
				titleCode = obj.getCode();
				break;
			}
		}

		QueryCustomer query = new QueryCustomer();
		query.setFirstName(array.get(1).toString());
		query.setLastName(array.get(2).toString());
		query.setTitleCode(titleCode);
		query.setLogin(array.get(3).toString());
		query.setPassword(array.get(4).toString());

		RESTLoader.execute(getActivity(), WebserviceMethodEnums.METHOD_REGISTER_CUSTOMER, query, this, true, true);
	}

	@Override
	public void onFormDataChanged()
	{
		boolean ok = getAdapter().validateAll();
		View view = getView();
		if (view != null)
		{
			view.findViewById(R.id.btnRegister).setEnabled(ok);
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
					getAdapter().notifyDataSetChanged();

					break;

				case METHOD_REGISTER_CUSTOMER:
					Toast.makeText(applicationContext, R.string.generic_success_message_popup, Toast.LENGTH_SHORT).show();
					getActivity().finish();
					break;

				default:
					break;

			}
		}
		else if (restLoaderResponse.getCode() == RESTLoaderResponse.ERROR)
		{
			switch (webserviceEnumMethod)
			{
				case METHOD_REGISTER_CUSTOMER:
					Toast.makeText(applicationContext,
							getString(R.string.generic_failed_message_popup) + " " + restLoaderResponse.getData(), Toast.LENGTH_SHORT)
							.show();
					break;

				default:
					break;
			}
		}

	}

}
