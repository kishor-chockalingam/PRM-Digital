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


public class Sort implements Parcelable
{
	private boolean selected;
	private String name;
	private String code;
	
	public Sort(boolean selected, String name, String code)
	{
		super();
		this.selected = selected;
		this.name = name;
		this.code = code;
	}

	public boolean getSelected()
	{
		return selected;
	}

	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public static final Parcelable.Creator<Sort> CREATOR = new Parcelable.Creator<Sort>()
	{
		@Override
		public Sort createFromParcel(Parcel source)
		{
			return new Sort(source);
		}

		@Override
		public Sort[] newArray(int size)
		{
			return new Sort[size];
		}
	};

	public Sort(Parcel in)
	{
		selected = (Boolean) in.readValue(null);
		name = in.readString();
		code = in.readString();
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeValue(selected);
		dest.writeString(name);
		dest.writeString(code);
	}
}
