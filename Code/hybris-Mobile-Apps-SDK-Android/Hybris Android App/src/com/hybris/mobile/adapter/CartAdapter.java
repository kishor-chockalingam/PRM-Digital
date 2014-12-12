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
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hybris.mobile.R;
import com.hybris.mobile.model.cart.Cart;
import com.hybris.mobile.model.cart.CartEntry;
import com.hybris.mobile.model.cart.CartItem;
import com.hybris.mobile.model.cart.CartPromotion;
import com.hybris.mobile.utility.StringUtil;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;


public class CartAdapter extends ArrayAdapter<CartItem>
{

	private final List<CartItem> items;
	private final Context mContext;

	public CartAdapter(Context context, List<CartItem> values)
	{
		super(context, R.layout.cart_row, values);
		this.mContext = context;
		this.items = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		Object cartItem = items.get(position);
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
		// Promotions
		else if (cartItem instanceof CartPromotion)
		{
			CartPromotion cartPromotion = (CartPromotion) cartItem;

			rowView = inflater.inflate(R.layout.promotion_row, parent, false);

			// Update promotions section
			TextView promotionsTextView = (TextView) rowView.findViewById(R.id.textView);
			promotionsTextView.setMovementMethod(LinkMovementMethod.getInstance());

			promotionsTextView.setText(Html.fromHtml(Cart.generatePromotionString(cartPromotion)));
			StringUtil.removeUnderlines((Spannable) promotionsTextView.getText());
		}

		return rowView;
	}
}
