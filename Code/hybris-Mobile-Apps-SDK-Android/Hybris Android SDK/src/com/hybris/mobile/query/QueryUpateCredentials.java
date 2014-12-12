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


public class QueryUpateCredentials implements Query
{
	private String oldValue;
	private String newValue;
	private String password;

	public QueryUpateCredentials()
	{
		super();
	}

	public String getOldValue()
	{
		return oldValue;
	}

	public void setOldValue(String oldValue)
	{
		this.oldValue = oldValue;
	}

	public String getNewValue()
	{
		return newValue;
	}

	public void setNewValue(String newValue)
	{
		this.newValue = newValue;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public static final Parcelable.Creator<QueryUpateCredentials> CREATOR = new Parcelable.Creator<QueryUpateCredentials>()
	{
		@Override
		public QueryUpateCredentials createFromParcel(Parcel source)
		{
			return new QueryUpateCredentials(source);
		}

		@Override
		public QueryUpateCredentials[] newArray(int size)
		{
			return new QueryUpateCredentials[size];
		}
	};

	public QueryUpateCredentials(Parcel in)
	{
		oldValue = in.readString();
		newValue = in.readString();
		password = in.readString();
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(oldValue);
		dest.writeString(newValue);
		dest.writeString(password);
	}

}
