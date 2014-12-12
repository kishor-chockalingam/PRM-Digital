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
import java.util.Hashtable;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;


public class Category implements Parcelable
{
	private String searchTag;
	private List<Category> childCategories = new ArrayList<Category>();
	private String name;

	public Category()
	{
		super();
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getSearchTag()
	{
		return searchTag;
	}

	public void setSearchTag(String searchTag)
	{
		this.searchTag = searchTag;
	}

	public List<Category> getChildCategories()
	{
		return childCategories;
	}

	public void setChildCategories(List<Category> childCategories)
	{
		this.childCategories = childCategories;
	}

	public static Category createCategoryFromObjectInfo(Hashtable<String, Object> objectInfo)
	{
		Category category = new Category();

		category.childCategories = new ArrayList<Category>();
		category.name = (String) objectInfo.get("name");
		Category parent = (Category) objectInfo.get("parent");
		if (parent != null)
		{
			parent.childCategories.add(category);
		}
		category.searchTag = (String) objectInfo.get("searchTag");

		return category;
	}

	public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>()
	{
		@Override
		public Category createFromParcel(Parcel source)
		{
			return new Category(source);
		}

		@Override
		public Category[] newArray(int size)
		{
			return new Category[size];
		}
	};

	public Category(Parcel in)
	{
		searchTag = in.readString();
		in.readList(childCategories, this.getClass().getClassLoader());
		name = in.readString();
	}

	@Override
	public int describeContents()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(searchTag);
		dest.writeList(childCategories);
		dest.writeString(name);
	}

}
