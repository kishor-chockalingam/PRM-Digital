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


public class QueryOrder extends QueryList implements Query
{

	/**
	 * CANCELLED, CHECKED_VALID
	 */
	private String statuses;

	public QueryOrder()
	{
	}

	public String getStatuses()
	{
		return statuses;
	}

	public void setStatuses(String statuses)
	{
		this.statuses = statuses;
	}

	public static final Parcelable.Creator<QueryOrder> CREATOR = new Parcelable.Creator<QueryOrder>()
	{
		@Override
		public QueryOrder createFromParcel(Parcel source)
		{
			return new QueryOrder(source);
		}

		@Override
		public QueryOrder[] newArray(int size)
		{
			return new QueryOrder[size];
		}
	};

	public QueryOrder(Parcel in)
	{
		super(in);
		statuses = in.readString();
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		super.writeToParcel(dest, flags);
		dest.writeString(statuses);
	}

}
