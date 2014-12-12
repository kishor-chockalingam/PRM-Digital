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
package com.hybris.mobile.activity;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;


import com.hybris.mobile.R;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class ImageZoomActivity extends HybrisActivity {

	private ImageViewTouch mImageView;
	private String mUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		handleIntent(getIntent());
		setContentView(R.layout.image_view);
		}
	
	private void handleIntent(Intent intent) {
		if(intent.hasExtra("url")) {
			mUrl = intent.getStringExtra("url");
		}
	}

	// Override with an empty method so that the menu is not populated
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
	
	@Override
	public void onContentChanged()
	{
		super.onContentChanged();
		ImageViewTouch imageView = (ImageViewTouch) findViewById(R.id.imageView);		
		UrlImageViewHelper.setUrlDrawable(imageView, mUrl, R.drawable.loading_drawable);
		mImageView = imageView;
	}

	public ImageViewTouch getImageView() {
		return mImageView;
	}

	public void setImageView(ImageViewTouch mImageView) {
		this.mImageView = mImageView;
	}

}
