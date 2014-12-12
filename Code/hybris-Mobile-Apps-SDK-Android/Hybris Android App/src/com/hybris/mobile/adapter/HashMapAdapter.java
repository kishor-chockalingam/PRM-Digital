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
package com.hybris.mobile.adapter;

import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hybris.mobile.R;

public class HashMapAdapter extends BaseAdapter {

	private HashMap<String, String> mData = new HashMap<String, String>();
	private String[] mKeys;
	private Context mContext;
	
	public HashMapAdapter(Context context, HashMap<String, String> data) {
		mData = data;
		mKeys = mData.keySet().toArray(new String[data.size()]);
		mContext = context;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(mKeys[position]);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		String key = mKeys[position];
		String value = getItem(position).toString();

		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.map, parent, false);
		
		TextView leftTextView = (TextView) view.findViewById(R.id.lbl_map_key);
		TextView rightTextView = (TextView) view.findViewById(R.id.lbl_map_value);
		
		leftTextView.setText(key);
		rightTextView.setText(value);
		
		return view;
	}

	public Context getContext() {
		return mContext;
	}

	public void setContext(Context context) {
		this.mContext = context;
	}

}
