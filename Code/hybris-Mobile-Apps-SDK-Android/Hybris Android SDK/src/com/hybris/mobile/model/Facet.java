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

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * A Facet in the store, for example "Cameras" or "Color"
 * 
 */
public class Facet implements Parcelable
{

	private boolean multiSelect;
	private boolean category;
	private ArrayList<FacetValue> values;
	private String name;
	private String internalName;

	public ArrayList<FacetValue> getValues()
	{
		return values;
	}

	public void setValues(ArrayList<FacetValue> values)
	{
		this.values = values;
	}

	public boolean getMultiSelect()
	{
		return multiSelect;
	}

	public void setMultiSelect(boolean multiSelect)
	{
		this.multiSelect = multiSelect;
	}

	public boolean getCategory()
	{
		return category;
	}

	public void setCategory(boolean category)
	{
		this.category = category;
	}


	public Facet()
	{
		values = new ArrayList<FacetValue>();
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getInternalName()
	{
		return internalName;
	}

	public void setInternalName(String internalName)
	{
		this.internalName = internalName;
	}

	public static final Parcelable.Creator<Facet> CREATOR = new Parcelable.Creator<Facet>()
	{
		@Override
		public Facet createFromParcel(Parcel source)
		{
			return new Facet(source);
		}

		@Override
		public Facet[] newArray(int size)
		{
			return new Facet[size];
		}
	};

	public Facet(Parcel in)
	{
		multiSelect = (Boolean) in.readValue(null);
		category = (Boolean) in.readValue(null);
		in.readList(values, null);
		name = in.readString();
		internalName = in.readString();
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeValue(multiSelect);
		dest.writeValue(category);
		dest.writeList(values);
		dest.writeString(name);
		dest.writeString(internalName);
	}
}
