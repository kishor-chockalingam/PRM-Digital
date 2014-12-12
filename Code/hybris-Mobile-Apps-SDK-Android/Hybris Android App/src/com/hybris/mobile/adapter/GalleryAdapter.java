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
import java.util.Hashtable;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.hybris.mobile.R;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;


public class GalleryAdapter extends BaseAdapter
{

	private Context galleryContext;
	private List<String> urls;

	public GalleryAdapter(Context c, List<Hashtable<String, String>> urls)
	{
		galleryContext = c;

		this.urls = new ArrayList<String>();

		if (urls != null)
		{
			for (Hashtable<String, String> hashtable : urls)
			{
				this.urls.add(hashtable.get("product"));
			}
		}
	}

	@Override
	public int getCount()
	{
		return this.urls.size();
	}

	@Override
	public Object getItem(int position)
	{
		return this.urls.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{

		final ImageView imageView;
		if (convertView == null)
			imageView = new ImageView(galleryContext);
		else
			imageView = (ImageView) convertView;

		UrlImageViewHelper.setUrlDrawable(imageView, this.urls.get(position), R.drawable.product_cell_placeholder, null);

		imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		imageView.setLayoutParams(new Gallery.LayoutParams(400, 400));

		return imageView;
	}

	public Context getGalleryContext()
	{
		return galleryContext;
	}

	public void setGalleryContext(Context galleryContext)
	{
		this.galleryContext = galleryContext;
	}

}
