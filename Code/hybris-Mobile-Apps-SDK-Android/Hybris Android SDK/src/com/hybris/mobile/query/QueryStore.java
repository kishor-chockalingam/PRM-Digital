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


public class QueryStore extends QueryList implements Query
{
	private String queryText;
	private double longitude;
	private double latitude;
	private float radius;
	private float accuracy;

	public QueryStore()
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

	public double getLongitude()
	{
		return longitude;
	}

	public void setLongitude(double longitude)
	{
		this.longitude = longitude;
	}

	public double getLatitude()
	{
		return latitude;
	}

	public void setLatitude(double latitude)
	{
		this.latitude = latitude;
	}

	public float getRadius()
	{
		return radius;
	}

	public void setRadius(float radius)
	{
		this.radius = radius;
	}

	public float getAccuracy()
	{
		return accuracy;
	}

	public void setAccuracy(float accuracy)
	{
		this.accuracy = accuracy;
	}

	public static final Parcelable.Creator<QueryStore> CREATOR = new Parcelable.Creator<QueryStore>()
	{
		@Override
		public QueryStore createFromParcel(Parcel source)
		{
			return new QueryStore(source);
		}

		@Override
		public QueryStore[] newArray(int size)
		{
			return new QueryStore[size];
		}
	};

	public QueryStore(Parcel in)
	{
		super(in);
		queryText = in.readString();
		longitude = in.readDouble();
		latitude = in.readDouble();
		radius = in.readFloat();
		accuracy = in.readFloat();
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
		dest.writeString(queryText);
		dest.writeDouble(longitude);
		dest.writeDouble(latitude);
		dest.writeFloat(radius);
		dest.writeFloat(accuracy);
	}

}
