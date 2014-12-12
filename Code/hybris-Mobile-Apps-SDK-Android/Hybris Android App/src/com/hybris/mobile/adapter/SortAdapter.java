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

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import com.hybris.mobile.model.Sort;

public class SortAdapter extends ArrayAdapter<Sort> {
	private final ArrayList<Sort> objects;
	
	public SortAdapter(Context context, int style, ArrayList<Sort> values) {
		super(context, style, values);
		this.objects = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CheckedTextView v = (CheckedTextView) super.getView(position, convertView, parent);
		Sort s = (Sort) objects.get(position);
		v.setText(s.getName());
		v.setChecked(s.getSelected());
		return v;
	}
}
