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


public class QueryCart implements Query
{

	private String productCode;
	private int cartEntryNumber;
	private int quantity;

	public QueryCart()
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

	public int getCartEntryNumber()
	{
		return cartEntryNumber;
	}

	public void setCartEntryNumber(int cartEntryNumber)
	{
		this.cartEntryNumber = cartEntryNumber;
	}

	public int getQuantity()
	{
		return quantity;
	}

	public void setQuantity(int quantity)
	{
		this.quantity = quantity;
	}

	public static final Parcelable.Creator<QueryCart> CREATOR = new Parcelable.Creator<QueryCart>()
	{
		@Override
		public QueryCart createFromParcel(Parcel source)
		{
			return new QueryCart(source);
		}

		@Override
		public QueryCart[] newArray(int size)
		{
			return new QueryCart[size];
		}
	};

	public QueryCart(Parcel in)
	{
		productCode = in.readString();
		cartEntryNumber = in.readInt();
		quantity = in.readInt();
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
		dest.writeInt(cartEntryNumber);
		dest.writeInt(quantity);
	}

}
