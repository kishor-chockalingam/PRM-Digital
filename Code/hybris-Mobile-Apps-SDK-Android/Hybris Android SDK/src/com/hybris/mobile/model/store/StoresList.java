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
package com.hybris.mobile.model.store;

import java.util.List;

import com.hybris.mobile.model.Pagination;


public class StoresList
{
	private List<Store> stores;
	private Pagination pagination;

	public List<Store> getStores()
	{
		return stores;
	}

	public void setStores(List<Store> stores)
	{
		this.stores = stores;
	}

	public Pagination getPagination()
	{
		return pagination;
	}

	public void setPagination(Pagination pagination)
	{
		this.pagination = pagination;
	}

}
