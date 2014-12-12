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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hybris.mobile.R;
import com.hybris.mobile.model.cart.CartOrder;
import com.hybris.mobile.utility.DateUtil;


public class OrderAdapter extends ArrayAdapter<CartOrder>
{
	private static final String LOG_TAG = OrderAdapter.class.getSimpleName();
	private final List<CartOrder> orders;
	private final Context mContext;

	public OrderAdapter(Context context, List<CartOrder> values)
	{
		super(context, R.layout.order_row, values);
		this.mContext = context;
		this.orders = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.order_row, parent, false);

		TextView orderDate = (TextView) rowView.findViewById(R.id.order_date);
		TextView orderNumber = (TextView) rowView.findViewById(R.id.order_number);
		TextView statusValue = (TextView) rowView.findViewById(R.id.status_value);

		orderNumber.setText(mContext.getString(R.string.order_text_number, orders.get(position).getCode()));

		Calendar cal = DateUtil.fromIso8601(orders.get(position).getPlaced());
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

		orderDate.setText(mContext.getString(R.string.order_text, sdf.format(cal.getTime())));

		statusValue.setText(orders.get(position).getStatusDisplay());

		return rowView;
	}
}
