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
package com.hybris.mobile.model.product;

public class ProductImage
{

	private String format;
	private String url;
	private String imageType;
	private int galleryIndex;

	public String getFormat()
	{
		return format;
	}

	public void setFormat(String format)
	{
		this.format = format;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getImageType()
	{
		return imageType;
	}

	public void setImageType(String imageType)
	{
		this.imageType = imageType;
	}

	public int getGalleryIndex()
	{
		return galleryIndex;
	}

	public void setGalleryIndex(int galleryIndex)
	{
		this.galleryIndex = galleryIndex;
	}

}
