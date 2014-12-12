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


public class QueryObjectId implements Query
{
	private String objectId = "";

	public QueryObjectId()
	{
	}

	public String getObjectId()
	{
		return objectId;
	}

	public void setObjectId(String objectId)
	{
		this.objectId = objectId;
	}

	public static final Parcelable.Creator<QueryObjectId> CREATOR = new Parcelable.Creator<QueryObjectId>()
	{
		@Override
		public QueryObjectId createFromParcel(Parcel source)
		{
			return new QueryObjectId(source);
		}

		@Override
		public QueryObjectId[] newArray(int size)
		{
			return new QueryObjectId[size];
		}
	};

	public QueryObjectId(Parcel in)
	{
		objectId = in.readString();
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(objectId);
	}

}
