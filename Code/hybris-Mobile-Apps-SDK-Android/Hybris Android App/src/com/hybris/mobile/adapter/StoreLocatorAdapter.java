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
import com.hybris.mobile.model.store.Store;


public class StoreLocatorAdapter extends ArrayAdapter<Store>
{

	private final List<Store> stores;
	private boolean showFooter;
	private boolean userLocationKnown = false;

	public StoreLocatorAdapter(Context context, List<Store> values)
	{
		super(context, R.layout.store_finder_row, values);
		this.stores = values;
		this.showFooter = false;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.store_finder_row, parent, false);

		TextView textView = (TextView) rowView.findViewById(R.id.lblStoreName);
		TextView town = (TextView) rowView.findViewById(R.id.lblStoreTown);

		// Footer view
		if (this.showFooter && position == this.getCount() - 1)
		{
			rowView = inflater.inflate(R.layout.footer_view, parent, false);
			TextView footer = (TextView) rowView.findViewById(R.id.footer_view);
			footer.setText(R.string.loading_message);
			return rowView;
		}

		Store store = this.stores.get(position);
		textView.setText(store.getName());

		if (store.getAddress() != null)
		{
			town.setText(store.getAddress().getTown());
		}

		// We display the distance only if we know the user location
		if (userLocationKnown)
		{
			TextView distance = (TextView) rowView.findViewById(R.id.lblStoreDistance);
			distance.setText(store.getFormattedDistance());
		}

		return rowView;
	}

	@Override
	public int getCount()
	{
		if (this.showFooter)
		{
			return super.getCount() + 1;
		}
		else
		{
			return super.getCount();
		}
	}

	public void showFooter(boolean show)
	{
		this.showFooter = show;
	}

	public void setUserLocationKnown(boolean userLocationKnown)
	{
		this.userLocationKnown = userLocationKnown;
	}

}
