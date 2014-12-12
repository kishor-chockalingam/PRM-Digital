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

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Hashtable;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.dd.plist.NSArray;
import com.dd.plist.NSDictionary;
import com.dd.plist.NSNumber;
import com.dd.plist.NSObject;
import com.dd.plist.NSString;
import com.dd.plist.PropertyListParser;
import com.hybris.mobile.Hybris;
import com.hybris.mobile.InternalConstants;
import com.hybris.mobile.R;
import com.hybris.mobile.adapter.FormAdapter;
import com.hybris.mobile.listener.SubmitListener;
import com.hybris.mobile.logging.LoggingUtils;
import com.hybris.mobile.view.HYFormTextSelectionCell;


public class FormActivity extends HybrisListActivity implements SubmitListener
{

	private static final String LOG_TAG = FormActivity.class.getSimpleName();
	private FormAdapter mAdapter;
	public ArrayList<Object> entries;

	protected void onCreate(Bundle savedInstanceState, String plistFile)
	{
		super.onCreate(savedInstanceState);

		entries = new ArrayList<Object>();
		mAdapter = new FormAdapter(this, entries, new FormAdapter.FocusChangeListner()
		{
			@Override
			public void fieldsValidated()
			{
				FormActivity.this.fieldsValidated();
			}

		});
		setListAdapter(mAdapter);
		try
		{
			AssetManager am = this.getAssets();
			InputStream fs = am.open(plistFile);
			NSArray formElements = (NSArray) PropertyListParser.parse(fs);
			for (int i = 0; i < formElements.count(); i++)
			{
				parse(formElements.objectAtIndex(i), null, null);
			}
			mAdapter.notifyDataSetChanged();
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_TAG, "Error parsing plist file \"" + plistFile + "\". " + e.getLocalizedMessage(),
					Hybris.getAppContext());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.form_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.menuSubmit:
				onMenuSubmit();
				return true;
			default:
				return false;
		}
	}

	@SuppressWarnings("unchecked")
	public void onMenuSubmit()
	{

		if (!mAdapter.getIsValid())
		{
			return;
		}

		ArrayList<String> bun = new ArrayList<String>();
		for (Object obj : entries)
		{
			Hashtable<String, Object> values = (Hashtable<String, Object>) obj;
			if (values.get("value") != null)
			{
				bun.add(values.get("value").toString());
			}
			else
			{
				bun.add("");
			}
		}

		onSubmit(bun);
	}

	//Convert NSDictionary and NSArray to native HashMap and ArrayList
	@SuppressWarnings(
	{ "unchecked" })
	private void parse(NSObject formObject, Object parent, String strKey)
	{
		if (formObject instanceof NSDictionary)
		{
			NSDictionary dict = (NSDictionary) formObject;
			Hashtable<String, Object> mapedDict = new Hashtable<String, Object>();
			String[] keys = dict.allKeys();
			for (String key : keys)
			{
				parse(dict.objectForKey(key), mapedDict, key);
			}
			if (parent == null)
			{
				entries.add(mapedDict);
			}
			else
			{
				((Hashtable<String, Object>) parent).put(strKey, mapedDict);
			}
		}
		else if (formObject instanceof NSArray)
		{
			NSArray arr = (NSArray) formObject;
			ArrayList<Object> mappedArr = new ArrayList<Object>();
			for (int i = 0; i < arr.count(); i++)
			{
				parse(arr.objectAtIndex(i), mappedArr, "");
			}
			((Hashtable<String, Object>) parent).put(strKey, mappedArr);
		}
		else if (formObject instanceof NSString || formObject instanceof NSNumber)
		{
			if (parent instanceof Hashtable<?, ?>)
			{
				Hashtable<String, Object> map = (Hashtable<String, Object>) parent;
				map.put(strKey, formObject);
			}
			else if (parent instanceof ArrayList<?>)
			{
				ArrayList<Object> map = (ArrayList<Object>) parent;
				map.add(formObject);
			}
		}
	}

	//Result from the value picker view
	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data)
	{
		if (requestCode == InternalConstants.PICKER_RESULT_CODE && resultCode == RESULT_OK && data.hasExtra("selected"))
		{
			Hashtable<String, Object> obj = (Hashtable<String, Object>) entries.get(data.getIntExtra("position", 0));
			obj.put("value", data.getStringExtra("selected"));
			mAdapter.notifyDataSetChanged();
		}
		else
		{
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	//Show the picker view
	@SuppressWarnings(
	{ "rawtypes", "unchecked" })
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);
		Hashtable<String, Object> obj = (Hashtable<String, Object>) entries.get(position);
		String className = "com.hybris.mobile.view." + obj.get("cellIdentifier").toString();
		try
		{
			Class cell = Class.forName(className);
			Constructor constructor = cell.getConstructor(new Class[]
			{ Context.class });
			Object someObj = constructor.newInstance(this);

			if (someObj instanceof HYFormTextSelectionCell)
			{
				ArrayList<String> values = (ArrayList<String>) ((Hashtable<String, Object>) entries.get(position)).get("values");
				Intent intent = new Intent(FormActivity.this, FormPickerActivity.class);
				intent.putStringArrayListExtra("values", values);
				intent.putExtra("position", position);
				startActivityForResult(intent, InternalConstants.PICKER_RESULT_CODE);
			}
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_TAG, "Error loading class \"" + className + "\". " + e.getLocalizedMessage(), Hybris.getAppContext());
		}
	}

	//SubmitListener method
	@Override
	public void onSubmit(ArrayList<String> bundle)
	{

	}

	public void fieldsValidated()
	{

	}
}
