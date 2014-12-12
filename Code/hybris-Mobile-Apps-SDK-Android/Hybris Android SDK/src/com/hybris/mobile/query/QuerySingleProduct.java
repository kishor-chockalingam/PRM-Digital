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
package com.hybris.mobile.query;

import android.os.Parcel;
import android.os.Parcelable;


public class QuerySingleProduct implements Query
{

	private String productCode;

	/**
	 * It defines the level of details requested. This parameter can have a combination of the following values, BASIC,
	 * DESCRIPTION, GALLERY, CATEGORIES, PROMOTIONS, STOCK, REVIEW, CLASSIFICATION, REFERENCES, PRICE
	 */
	private String[] options;

	public QuerySingleProduct()
	{
		super();
	}

	public String getProductCode()
	{
		return productCode;
	}

	public void setProductCode(String productCode)
	{
		this.productCode = productCode;
	}

	public String[] getOptions()
	{
		return options;
	}

	public void setOptions(String[] options)
	{
		this.options = options;
	}

	public static final Parcelable.Creator<QuerySingleProduct> CREATOR = new Parcelable.Creator<QuerySingleProduct>()
	{
		@Override
		public QuerySingleProduct createFromParcel(Parcel source)
		{
			return new QuerySingleProduct(source);
		}

		@Override
		public QuerySingleProduct[] newArray(int size)
		{
			return new QuerySingleProduct[size];
		}
	};

	public QuerySingleProduct(Parcel in)
	{
		productCode = in.readString();
		in.readStringArray(options);
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(productCode);
		dest.writeStringArray(options);
	}
}
