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

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.hybris.mobile.model.Category;
import com.hybris.mobile.model.FacetValue;
import com.hybris.mobile.model.Sort;


public class QueryProducts extends QueryList implements Query
{

	private String queryText;
	private Sort selectedSort;
	private List<FacetValue> selectedFacetValues = new ArrayList<FacetValue>();
	private Category selectedCategory;
	private boolean fromDidYouMean = false;

	public QueryProducts()
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

	public Sort getSelectedSort()
	{
		return selectedSort;
	}

	public void setSelectedSort(Sort selectedSort)
	{
		this.selectedSort = selectedSort;
	}

	public List<FacetValue> getSelectedFacetValues()
	{
		return selectedFacetValues;
	}

	public void setSelectedFacetValues(List<FacetValue> selectedFacetValues)
	{
		this.selectedFacetValues = selectedFacetValues;
	}

	public Category getSelectedCategory()
	{
		return selectedCategory;
	}

	public void setSelectedCategory(Category selectedCategory)
	{
		this.selectedCategory = selectedCategory;
	}

	public boolean isFromDidYouMean()
	{
		return fromDidYouMean;
	}

	public void setFromDidYouMean(boolean fromDidYouMean)
	{
		this.fromDidYouMean = fromDidYouMean;
	}

	public static final Parcelable.Creator<QueryProducts> CREATOR = new Parcelable.Creator<QueryProducts>()
	{
		@Override
		public QueryProducts createFromParcel(Parcel source)
		{
			return new QueryProducts(source);
		}

		@Override
		public QueryProducts[] newArray(int size)
		{
			return new QueryProducts[size];
		}
	};

	public QueryProducts(Parcel in)
	{
		super(in);
		queryText = in.readString();
		in.readList(selectedFacetValues, this.getClass().getClassLoader());
		selectedSort = in.readParcelable(this.getClass().getClassLoader());
		selectedCategory = in.readParcelable(this.getClass().getClassLoader());
		fromDidYouMean = (Boolean) in.readValue(this.getClass().getClassLoader());
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
		dest.writeList(selectedFacetValues);
		dest.writeParcelable(selectedSort, flags);
		dest.writeParcelable(selectedCategory, flags);
		dest.writeValue(fromDidYouMean);
	}
}
