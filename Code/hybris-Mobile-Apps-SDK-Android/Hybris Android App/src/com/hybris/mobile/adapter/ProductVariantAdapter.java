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
import android.widget.TextView;

import com.hybris.mobile.R;
import com.hybris.mobile.model.product.ProductOptionItem;


public class ProductVariantAdapter extends ArrayAdapter<ProductOptionItem>
{

	private final List<ProductOptionItem> variants;
	private final Context mContext;

	public ProductVariantAdapter(Context context, List<ProductOptionItem> values)
	{
		super(context, R.layout.product_variant_row, values);
		this.mContext = context;
		this.variants = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.product_variant_row, parent, false);

		TextView lblVariantName = (TextView) rowView.findViewById(R.id.lbl_variant_name);
		TextView lblVariantValue = (TextView) rowView.findViewById(R.id.lbl_variant_value);
		lblVariantName.setText(variants.get(position).getName());
		lblVariantValue.setText(variants.get(position).getValue());

		return rowView;
	}
}
