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


public class QueryAddress implements Query
{

	private String firstName;
	private String lastName;
	private String titleCode;
	private String addressLine1;
	private String addressLine2;
	private String town;
	private String postCode;
	private String countryISOCode;
	private String addressId;

	public QueryAddress()
	{
	}

	public String getAddressId()
	{
		return addressId;
	}

	public void setAddressId(String addressId)
	{
		this.addressId = addressId;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public String getTitleCode()
	{
		return titleCode;
	}

	public void setTitleCode(String titleCode)
	{
		this.titleCode = titleCode;
	}

	public String getAddressLine1()
	{
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1)
	{
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2()
	{
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2)
	{
		this.addressLine2 = addressLine2;
	}

	public String getTown()
	{
		return town;
	}

	public void setTown(String town)
	{
		this.town = town;
	}

	public String getPostCode()
	{
		return postCode;
	}

	public void setPostCode(String postCode)
	{
		this.postCode = postCode;
	}

	public String getCountryISOCode()
	{
		return countryISOCode;
	}

	public void setCountryISOCode(String countryISOCode)
	{
		this.countryISOCode = countryISOCode;
	}

	public static final Parcelable.Creator<QueryAddress> CREATOR = new Parcelable.Creator<QueryAddress>()
	{
		@Override
		public QueryAddress createFromParcel(Parcel source)
		{
			return new QueryAddress(source);
		}

		@Override
		public QueryAddress[] newArray(int size)
		{
			return new QueryAddress[size];
		}
	};

	public QueryAddress(Parcel in)
	{
		firstName = in.readString();
		lastName = in.readString();
		titleCode = in.readString();
		addressLine1 = in.readString();
		addressLine2 = in.readString();
		town = in.readString();
		postCode = in.readString();
		countryISOCode = in.readString();
		addressId = in.readString();
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(firstName);
		dest.writeString(lastName);
		dest.writeString(titleCode);
		dest.writeString(addressLine1);
		dest.writeString(addressLine2);
		dest.writeString(town);
		dest.writeString(postCode);
		dest.writeString(countryISOCode);
		dest.writeString(addressId);
	}

}
