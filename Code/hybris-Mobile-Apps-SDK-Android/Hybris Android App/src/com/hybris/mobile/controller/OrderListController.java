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
package com.hybris.mobile.controller;

import java.util.List;

import android.app.Activity;

import com.hybris.mobile.ExternalConstants;
import com.hybris.mobile.WebserviceMethodEnums;
import com.hybris.mobile.loader.RESTLoader;
import com.hybris.mobile.loader.RESTLoaderObserver;
import com.hybris.mobile.loader.RESTLoaderResponse;
import com.hybris.mobile.model.Pagination;
import com.hybris.mobile.model.cart.CartOrder;
import com.hybris.mobile.model.cart.CartOrders;
import com.hybris.mobile.query.QueryOrder;
import com.hybris.mobile.utility.JsonUtils;


public class OrderListController extends Controller implements RESTLoaderObserver
{
	private Integer mCurrentPage = 0;
	private Integer mTotalPages = 1;
	private List<CartOrder> mModel;
	private Activity mContext;

	public static final int MESSAGE_MODEL_UPDATED = 1;

	public OrderListController(List<CartOrder> model, Activity context)
	{
		mModel = model;
		mContext = context;
	}

	public List<CartOrder> getModel()
	{
		return mModel;
	}

	public int getCurrentPage()
	{
		return mCurrentPage + 1;
	}

	public int getTotalPages()
	{
		return mTotalPages;
	}

	public void fetchMore()
	{
		if ((mCurrentPage + 1) < mTotalPages)
		{
			mCurrentPage++;
			getOrders();
		}
		else
		{
			return;
		}
	}

	public void getOrders()
	{
		QueryOrder query = new QueryOrder();
		query.setStatuses("CHECKED_VALID");
		query.setCurrentPage(mCurrentPage);
		query.setPageSize(ExternalConstants.QUERY_PAGE_SIZE);

		RESTLoader.execute(mContext, WebserviceMethodEnums.METHOD_GET_ORDERS, query, this, true, true);
	}

	@Override
	public void onReceiveResult(RESTLoaderResponse restLoaderResponse, WebserviceMethodEnums webserviceEnumMethod)
	{
		if (restLoaderResponse.getCode() == RESTLoaderResponse.SUCCESS)
		{
			switch (webserviceEnumMethod)
			{
				case METHOD_GET_ORDERS:

					CartOrders cartOrders = JsonUtils.fromJson(restLoaderResponse.getData(), CartOrders.class);

					if (mCurrentPage < 1)
					{
						mModel.clear();
					}

					Pagination pagination = cartOrders.getPagination();

					if (pagination != null)
					{
						mCurrentPage = pagination.getCurrentPage();
						mTotalPages = pagination.getTotalPages();
					}

					mModel.addAll(cartOrders.getOrders());

					notifyOutboxHandlers(OrderListController.MESSAGE_MODEL_UPDATED, 0, 0, null);

					break;

				default:
					break;
			}
		}
		else if (restLoaderResponse.getCode() == RESTLoaderResponse.ERROR)
		{
			switch (webserviceEnumMethod)
			{
				case METHOD_GET_ORDERS:
					notifyOutboxHandlers(OrderListController.MESSAGE_MODEL_UPDATED, 0, 0, null);
					break;

				default:
					break;
			}
		}
	}

}
