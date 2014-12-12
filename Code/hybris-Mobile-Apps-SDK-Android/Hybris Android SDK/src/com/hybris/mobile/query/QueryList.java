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

import com.hybris.mobile.ExternalConstants;


public class QueryList implements Query
{
	private int currentPage;
	private int pageSize = ExternalConstants.QUERY_PAGE_SIZE;
	private int totalResults;
	private int totalPages = 1;

	public QueryList()
	{
	}

	public int getCurrentPage()
	{
		return currentPage;
	}

	public void setCurrentPage(int currentPage)
	{
		this.currentPage = currentPage;
	}

	public int getPageSize()
	{
		return pageSize;
	}

	public void setPageSize(int pageSize)
	{
		this.pageSize = pageSize;
	}

	public int getTotalResults()
	{
		return totalResults;
	}

	public void setTotalResults(int totalResults)
	{
		this.totalResults = totalResults;
	}

	public int getTotalPages()
	{
		return totalPages;
	}

	public void setTotalPages(int totalPages)
	{
		this.totalPages = totalPages;
	}

	public static final Parcelable.Creator<QueryList> CREATOR = new Parcelable.Creator<QueryList>()
	{
		@Override
		public QueryList createFromParcel(Parcel source)
		{
			return new QueryList(source);
		}

		@Override
		public QueryList[] newArray(int size)
		{
			return new QueryList[size];
		}
	};

	public QueryList(Parcel in)
	{
		currentPage = in.readInt();
		pageSize = in.readInt();
		totalResults = in.readInt();
		totalPages = in.readInt();
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeInt(currentPage);
		dest.writeInt(pageSize);
		dest.writeInt(totalResults);
		dest.writeInt(totalPages);
	}

}
