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
import android.widget.ImageView;
import android.widget.TextView;

import com.hybris.mobile.R;
import com.hybris.mobile.model.cart.CartEntry;
import com.hybris.mobile.model.cart.CartItem;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;


public class BasketAdapter extends ArrayAdapter<CartEntry>
{

	private final List<CartEntry> items;
	private final Context mContext;

	public BasketAdapter(Context context, List<CartEntry> values)
	{
		super(context, R.layout.cart_row, values);
		this.mContext = context;
		this.items = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		CartItem cartItem = items.get(position);
		View rowView = null;

		if (cartItem instanceof CartEntry)
		{
			CartEntry cartEntry = (CartEntry) cartItem;

			rowView = inflater.inflate(R.layout.cart_row, parent, false);

			TextView lblProductTitle = (TextView) rowView.findViewById(R.id.lbl_product_title);
			TextView lblProductPrice = (TextView) rowView.findViewById(R.id.lbl_price_quantity);
			TextView lblManufacturer = (TextView) rowView.findViewById(R.id.lbl_productManufacturer);
			TextView lblTotal = (TextView) rowView.findViewById(R.id.lbl_total);
			ImageView productImage = (ImageView) rowView.findViewById(R.id.img_product);

			if (StringUtils.isEmpty(cartEntry.getProduct().getManufacturer()))
			{
				lblManufacturer.setHeight(0);
			}
			else
			{
				lblManufacturer.setText(cartEntry.getProduct().getManufacturer());
			}
			lblProductTitle.setText(cartEntry.getProduct().getName());
			lblProductPrice.setText(cartEntry.getBasePrice().getFormattedValue() + " - "
					+ this.getContext().getString(R.string.cartentry_quantity_placeholder) + cartEntry.getQuantity().toString());
			lblManufacturer.setText(cartEntry.getProduct().getManufacturer());


			lblTotal.setText(cartEntry.getTotalPrice().getFormattedValue());
			UrlImageViewHelper.setUrlDrawable(productImage, cartEntry.getProduct().getThumbnail(), R.drawable.loading_drawable);
		}

		return rowView;
	}
}
