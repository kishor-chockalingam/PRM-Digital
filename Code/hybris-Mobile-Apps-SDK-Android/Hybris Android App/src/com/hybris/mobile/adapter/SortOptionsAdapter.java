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
import java.util.Set;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hybris.mobile.Hybris;


/**
 * Specialization of the simple_list_item_1 array adapter, that styles any items in saved searches
 * 
 * @author philip
 * 
 */
public class SortOptionsAdapter extends ArrayAdapter<String>
{

	public SortOptionsAdapter(Context context, List<String> values)
	{
		super(context, android.R.layout.simple_list_item_1, values);
	}

	@Override
	/// Makes bold any terms that are previous searches
	public View getView(int position, View convertView, ViewGroup parent)
	{
		TextView textView = (TextView) super.getView(position, convertView, parent);
		Set<String> previousSearches = Hybris.getPreviousSearches();
		if (previousSearches.contains(textView.getText()))
		{
			textView.setTypeface(null, Typeface.BOLD);
		}
		else
		{
			textView.setTypeface(null, Typeface.NORMAL);
		}
		return textView;
	}

}
