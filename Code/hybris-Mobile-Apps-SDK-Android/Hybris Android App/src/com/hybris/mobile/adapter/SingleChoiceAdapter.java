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

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import com.hybris.mobile.R;


public class SingleChoiceAdapter extends ArrayAdapter<String>
{

	private int checked;

	private final List<String> objects;

	public SingleChoiceAdapter(Context context, List<String> values)
	{
		super(context, R.layout.single_line_checked, values);
		this.objects = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{

		String text = (String) objects.get(position);

		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		CheckedTextView textView = (CheckedTextView) inflater.inflate(R.layout.single_line_checked, parent, false);

		textView.setText(text);
		textView.setChecked(position == checked);

		return textView;
	}

	public int getChecked()
	{
		return checked;
	}

	public void setChecked(int checked)
	{
		this.checked = checked;
	}
}
