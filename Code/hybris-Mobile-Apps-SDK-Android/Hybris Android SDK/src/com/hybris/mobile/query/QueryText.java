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


public class QueryText implements Query
{
	private String queryText = "";

	public QueryText()
	{
	}

	public String getQueryText()
	{
		return queryText;
	}

	public void setQueryText(String queryText)
	{
		this.queryText = queryText;
	}

	public static final Parcelable.Creator<QueryText> CREATOR = new Parcelable.Creator<QueryText>()
	{
		@Override
		public QueryText createFromParcel(Parcel source)
		{
			return new QueryText(source);
		}

		@Override
		public QueryText[] newArray(int size)
		{
			return new QueryText[size];
		}
	};

	public QueryText(Parcel in)
	{
		queryText = in.readString();
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(queryText);
	}

}
