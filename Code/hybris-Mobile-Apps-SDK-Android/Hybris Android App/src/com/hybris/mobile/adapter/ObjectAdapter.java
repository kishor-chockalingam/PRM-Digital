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
import android.widget.ImageView;
import android.widget.TextView;

import com.hybris.mobile.Hybris;
import com.hybris.mobile.R;
import com.hybris.mobile.logging.LoggingUtils;
import com.hybris.mobile.model.Category;
import com.hybris.mobile.model.DidYouMean;
import com.hybris.mobile.model.product.Product;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;


public class ObjectAdapter extends ArrayAdapter<Object>
{
	private static final String LOG_CAT = ObjectAdapter.class.getSimpleName();
	private final Context context;
	private final List<Object> objects;
	private boolean showFooter;

	public ObjectAdapter(Context context, List<Object> values)
	{
		super(context, R.layout.product_row, values);
		this.context = context;
		this.objects = values;
		this.showFooter = true;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = null;

		// Footer view
		if (this.showFooter && position == this.getCount() - 1)
		{
			rowView = inflater.inflate(R.layout.footer_view, parent, false);
			TextView footer = (TextView) rowView.findViewById(R.id.footer_view);
			footer.setText(R.string.loading_message);
			return rowView;
		}

		if (this.objects.get(position) instanceof Product)
		{
			rowView = inflater.inflate(R.layout.product_row, parent, false);
			ImageView imageView = (ImageView) rowView.findViewById(R.id.img_product);
			TextView name = (TextView) rowView.findViewById(R.id.lbl_product_title);
			TextView brand = (TextView) rowView.findViewById(R.id.lbl_productManufacturer);
			TextView price = (TextView) rowView.findViewById(R.id.lbl_price);
			TextView stock = (TextView) rowView.findViewById(R.id.lbl_stock);

			Product product = (Product) objects.get(position);
			name.setText(product.getName());
			brand.setText(product.getManufacturer());
			price.setText(product.getPrice().getFormattedValue());
			stock.setText(product.getStockLevelText(Hybris.getAppContext()));
			imageView.setVisibility(View.VISIBLE);
			String url = product.getThumbnail();

			UrlImageViewHelper.setUrlDrawable(imageView, url, R.drawable.product_cell_placeholder, null);
			imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		}
		else if (this.objects.get(position) instanceof Category)
		{
			rowView = inflater.inflate(R.layout.row_singleline_with_icon, parent, false);
			TextView textView = (TextView) rowView.findViewById(R.id.textViewSingleLine);

			Category category = (Category) objects.get(position);
			textView.setText(category.getName());
		}
		else if (this.objects.get(position) instanceof DidYouMean)
		{
			rowView = inflater.inflate(R.layout.didyoumean_row, parent, false);
			TextView textView = (TextView) rowView.findViewById(R.id.label);

			DidYouMean didYouMean = (DidYouMean) objects.get(position);
			textView.setText(context.getResources().getString(R.string.did_you_mean, didYouMean.getName()));
		}
		else
		{
			LoggingUtils.d(LOG_CAT, "No view found for \"" + this.objects.get(position).getClass().getCanonicalName() + "\".");
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

}
