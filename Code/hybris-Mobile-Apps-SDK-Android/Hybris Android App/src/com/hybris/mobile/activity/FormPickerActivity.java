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

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hybris.mobile.R;


public class FormPickerActivity extends HybrisListActivity
{

	private ArrayList<String> mLinks;
	private ArrayAdapter<String> mAdapter;
	private int mPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setTitle(R.string.generic_title_select_value);
		mLinks = new ArrayList<String>();
		mAdapter = new ArrayAdapter<String>(this, R.layout.standard_row, android.R.id.text1, mLinks);
		setListAdapter(mAdapter);
		handleIntent(getIntent());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		return false;
	}

	private void handleIntent(Intent intent)
	{
		if (intent.hasExtra("values"))
		{
			mLinks.addAll(intent.getStringArrayListExtra("values"));
			mAdapter.notifyDataSetChanged();
		}
		if (intent.hasExtra("position"))
		{
			mPosition = intent.getIntExtra("position", 0);
		}
	}

	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);

		Intent data = new Intent();
		data.putExtra("selected", mLinks.get(position).toString());
		data.putExtra("position", this.mPosition);
		setResult(RESULT_OK, data);
		super.finish();
	}
}
