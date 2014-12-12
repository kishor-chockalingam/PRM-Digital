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
package com.hybris.mobile.model.cart;

import java.util.List;

import com.hybris.mobile.model.Pagination;


public class CartOrders
{
	private List<CartOrder> orders;
	private Pagination pagination;

	public Pagination getPagination()
	{
		return pagination;
	}

	public void setPagination(Pagination pagination)
	{
		this.pagination = pagination;
	}

	public List<CartOrder> getOrders()
	{
		return orders;
	}

	public void setOrders(List<CartOrder> orders)
	{
		this.orders = orders;
	}

}
