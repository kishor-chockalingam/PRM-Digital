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
import com.hybris.mobile.model.cart.CartPaymentInfo;


public class PaymentAdapter extends ArrayAdapter<CartPaymentInfo>
{

	private final List<CartPaymentInfo> payments;
	private int checked = -1;

	public void setChecked(int checked)
	{
		this.checked = checked;
	}

	public int getChecked()
	{
		return checked;
	}

	public PaymentAdapter(Context context, List<CartPaymentInfo> values)
	{
		super(context, R.layout.selectable_row, values);
		this.payments = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.selectable_row, parent, false);
		TextView textView = (TextView) rowView.findViewById(android.R.id.text1);
		TextView textTitle = (TextView) rowView.findViewById(R.id.textTitle);

		CartPaymentInfo payment = payments.get(position);

		textTitle.setText(payment.getAccountHolderName());
		textTitle.setVisibility(View.VISIBLE);
		textView.setText(payment.getCardNumber() + "\n" + payment.getCardType().getName());

		return rowView;
	}
}
