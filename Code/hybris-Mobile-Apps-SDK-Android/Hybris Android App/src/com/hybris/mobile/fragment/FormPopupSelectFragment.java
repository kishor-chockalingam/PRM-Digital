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

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Hashtable;

import android.app.Activity;
import android.app.ListFragment;
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
import com.hybris.mobile.activity.FormPopupSelectActivity;
import com.hybris.mobile.adapter.FormAdapter;
import com.hybris.mobile.fragment.AddressFragment.DoneClickListener;
import com.hybris.mobile.listener.SubmitListener;
import com.hybris.mobile.logging.LoggingUtils;
import com.hybris.mobile.view.HYFormTextSelectionCell;


/** Analogous to {@link FormPopupSelectActivity}. */
public class FormPopupSelectFragment extends ListFragment implements SubmitListener
{

	private static final String LOG_TAG = FormPopupSelectFragment.class.getSimpleName();
	private FormAdapter mAdapter;
	public ArrayList<Object> entries;

	protected void onCreate(Bundle savedInstanceState, String plistFile)
	{
		super.onCreate(savedInstanceState);

		entries = new ArrayList<Object>();
		mAdapter = new FormAdapter(getActivity(), entries, new FormAdapter.FocusChangeListner()
		{
			@Override
			public void fieldsValidated()
			{
				FormPopupSelectFragment.this.fieldsValidated();
			}
		});
		setListAdapter(mAdapter);
		try
		{
			AssetManager am = getActivity().getAssets();
			InputStream fs = am.open(plistFile);
			NSArray formElements = (NSArray) PropertyListParser.parse(fs);
			for (int i = 0; i < formElements.count(); i++)
			{
				parse(formElements.objectAtIndex(i), null, null);
			}
			mAdapter.notifyDataSetChanged();
		}
		catch (Exception ex)
		{
			throw new RuntimeException("Config error with " + plistFile, ex);
		}

	}

	protected void fieldsValidated()
	{

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.form_menu, menu);
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
			mAdapter.notifyDataSetChanged();
		}
		else
		{

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
	}

	public void save(View view)
	{
		onMenuSubmit();
	}

	// Convert NSDictionary and NSArray to native HashMap and ArrayList
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

	// Result from the value picker view
	@SuppressWarnings("unchecked")
	@Override
	public void onActivityResult(final int requestCode, final int resultCode, final Intent data)
	{
		if (requestCode == InternalConstants.PICKER_RESULT_CODE && resultCode == Activity.RESULT_OK)
		{
			runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					if (data.hasExtra("selected"))
					{
						Hashtable<String, Object> obj = (Hashtable<String, Object>) entries.get(data.getIntExtra("position", 0));
						obj.put("value", data.getStringExtra("selected"));
						mAdapter.notifyDataSetChanged();
					}
				}
			});
		}
		else
		{
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	// Show the picker view
	@SuppressWarnings(
	{ "rawtypes", "unchecked" })
	@Override
	public void onListItemClick(ListView l, View v, final int position, long id)
	{

		super.onListItemClick(l, v, position, id);
		Hashtable<String, Object> obj = (Hashtable<String, Object>) entries.get(position);
		String className = "com.hybris.mobile.view." + obj.get("cellIdentifier").toString();
		try
		{
			Class cell = Class.forName(className);
			Constructor constructor = cell.getConstructor(new Class[]
			{ Context.class });
			Object someObj = constructor.newInstance(getActivity());

			if (someObj instanceof HYFormTextSelectionCell)
			{

				ArrayList<String> values = (ArrayList<String>) ((Hashtable<String, Object>) entries.get(position)).get("values");
				final String[] stringValues = (String[]) values.toArray(new String[values.size()]);

				AddressFragment fragment = new AddressFragment(stringValues, new DoneClickListener()
				{
					@Override
					public void onClick(int valuePosition)
					{
						if (valuePosition < 0)
							return;

						((Hashtable<String, Object>) entries.get(position)).put("value", stringValues[valuePosition]);
						mAdapter.notifyDataSetChanged();
						mAdapter.notifyFormDataChangedListner();
					}
				});
				fragment.show(getFragmentManager(), "");

			}
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_TAG, "Error loading class \"" + className + "\". " + e.getLocalizedMessage(), Hybris.getAppContext());
		}

	}

	// SubmitListener method
	@Override
	public void onSubmit(ArrayList<String> bundle)
	{

	}

	public FormAdapter getAdapter()
	{
		return mAdapter;
	}

	public void setAdapter(FormAdapter mAdapter)
	{
		this.mAdapter = mAdapter;
	}

	public void runOnUiThread(Runnable runnable)
	{
		final Activity activity = getActivity();
		if (activity != null)
		{
			activity.runOnUiThread(runnable);
		}
		else
		{
			LoggingUtils.e(LOG_TAG, "No activity available. Please check the fragment's live cycle.", Hybris.getAppContext());
		}
	}
}
