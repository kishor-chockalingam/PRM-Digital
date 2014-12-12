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

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hybris.mobile.R;
import com.hybris.mobile.model.cart.CartDeliveryAddress;


public class AddressAdapter extends ArrayAdapter<CartDeliveryAddress>
{

	private final List<CartDeliveryAddress> addresses;
	private int checked = -1;

	public void setChecked(int checked)
	{
		this.checked = checked;
	}

	public int getChecked()
	{
		return checked;
	}

	public AddressAdapter(Context context, List<CartDeliveryAddress> values)
	{
		super(context, R.layout.selectable_row, values);
		this.addresses = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.selectable_row, parent, false);
		TextView textView = (TextView) rowView.findViewById(android.R.id.text1);
		TextView textTitle = (TextView) rowView.findViewById(R.id.textTitle);

		String strName = "";

		CartDeliveryAddress address = addresses.get(position);

		if (StringUtils.isNotBlank(address.getLine1()))
		{
			strName = address.getLine1();
		}
		if (StringUtils.isNotBlank(address.getLine2()))
		{
			strName = strName + "\n" + address.getLine2();
		}
		if (StringUtils.isNotBlank(address.getTown()))
		{
			strName = strName + "\n" + address.getTown();
		}
		if (StringUtils.isNotBlank(address.getPostalCode()))
		{
			strName = strName + "\n" + address.getPostalCode();
		}
		if (address.getCountry() != null)
		{
			strName = strName + "\n" + address.getCountry().getName();
		}

		textTitle.setText(address.getTitle() + " " + address.getFirstName() + " " + address.getLastName());
		textTitle.setVisibility(View.VISIBLE);

		textView.setText(strName);

		return rowView;
	}
}
