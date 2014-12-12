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

import java.util.Date;

import com.hybris.mobile.utility.DateUtil;


public class ProductReview
{

	private String comment;
	private String date;
	private String headline;
	private ProductReviewPrincipal principal;
	private long productId;
	private float rating;

	public float getRating()
	{
		return rating;
	}

	public void setRating(float rating)
	{
		this.rating = rating;
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	public String getDate()
	{
		return date;
	}

	public void setDate(String date)
	{
		this.date = date;
	}

	public Date getDateTime()
	{
		return DateUtil.fromIso8601(this.date).getTime();
	}

	public String getHeadline()
	{
		return headline;
	}

	public void setHeadline(String headline)
	{
		this.headline = headline;
	}

	public ProductReviewPrincipal getPrincipal()
	{
		return principal;
	}

	public void setPrincipal(ProductReviewPrincipal principal)
	{
		this.principal = principal;
	}

	public long getProductId()
	{
		return productId;
	}

	public void setProductId(long productId)
	{
		this.productId = productId;
	}

}
