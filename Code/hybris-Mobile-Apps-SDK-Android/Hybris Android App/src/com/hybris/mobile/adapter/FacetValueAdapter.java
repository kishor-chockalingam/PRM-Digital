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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.hybris.mobile.R;
import com.hybris.mobile.model.FacetValue;

public class FacetValueAdapter extends ArrayAdapter<FacetValue> {
	private final ArrayList<FacetValue> objects;
	
	public FacetValueAdapter(Context context, int style, ArrayList<FacetValue> values) {
		super(context, style, values);
		this.objects = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		FacetValue fv = (FacetValue) objects.get(position);

		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = null;
		if (fv.getFacet().getMultiSelect()) {
			view = inflater.inflate(R.layout.two_line_list_item_multiple_choice, parent, false);
		}
		else {
			view = inflater.inflate(R.layout.two_line_list_item_single_choice, parent, false);
		}

		CheckedTextView textView = (CheckedTextView) view.findViewById(R.id.text1);
		TextView subtitleView = (TextView) view.findViewById(R.id.text2);
		
		// Add the product count
		String productCount = fv.getCount().toString() + " ";
		if (fv.getCount() == 1) {
			productCount += "Product"; 
		}
		else {
			productCount += "Products"; 
		}
		
		textView.setText(fv.getName());
		textView.setChecked(fv.getSelected());
		subtitleView.setText(productCount);
		
		return view;
	}
}
