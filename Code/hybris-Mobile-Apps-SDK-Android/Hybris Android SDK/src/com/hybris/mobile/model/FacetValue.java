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
package com.hybris.mobile.model;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * A facet value, for example "Canon" or "Blue"
 * 
 */
public class FacetValue implements Parcelable
{
	private Facet facet;
	private boolean selected;
	private Integer count;
	private String query;
	private String name;
	private String value;

	public boolean getSelected()
	{
		return selected;
	}

	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}

	public Integer getCount()
	{
		return count;
	}

	public void setCount(Integer count)
	{
		this.count = count;
	}

	public String getQuery()
	{
		return query;
	}

	public void setQuery(String query)
	{
		this.query = query;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Facet getFacet()
	{
		return facet;
	}

	public void setFacet(Facet facet)
	{
		this.facet = facet;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public static final Parcelable.Creator<FacetValue> CREATOR = new Parcelable.Creator<FacetValue>()
	{
		@Override
		public FacetValue createFromParcel(Parcel source)
		{
			return new FacetValue(source);
		}

		@Override
		public FacetValue[] newArray(int size)
		{
			return new FacetValue[size];
		}
	};

	public FacetValue(Parcel in)
	{
		facet = in.readParcelable(null);
		selected = (Boolean) in.readValue(null);
		count = in.readInt();
		query = in.readString();
		name = in.readString();
		value = in.readString();
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeParcelable(facet, flags);
		dest.writeValue(selected);
		dest.writeInt(count);
		dest.writeString(query);
		dest.writeString(name);
		dest.writeString(value);
	}
}
