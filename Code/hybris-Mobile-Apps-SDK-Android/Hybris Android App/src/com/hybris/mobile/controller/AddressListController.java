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

import com.hybris.mobile.WebserviceMethodEnums;
import com.hybris.mobile.loader.RESTLoader;
import com.hybris.mobile.loader.RESTLoaderObserver;
import com.hybris.mobile.loader.RESTLoaderResponse;
import com.hybris.mobile.model.cart.CartDeliveryAddress;
import com.hybris.mobile.query.QueryObjectId;
import com.hybris.mobile.utility.JsonUtils;


public class AddressListController extends Controller implements RESTLoaderObserver
{
	private List<CartDeliveryAddress> model;
	private Activity mContext;

	public static final int MESSAGE_MODEL_UPDATED = 1;
	public static final int MESSAGE_SET_DELIVERY_ADDRESS_SUCCESS = 2;
	public static final int MESSAGE_SET_DELIVERY_ADDRESS_ERROR = 3;

	public AddressListController(List<CartDeliveryAddress> model, Activity context)
	{
		this.model = model;
		mContext = context;
	}

	public List<CartDeliveryAddress> getModel()
	{
		return model;
	}

	public void setDeliveryAddress(String addressId)
	{
		QueryObjectId query = new QueryObjectId();
		query.setObjectId(addressId);
		RESTLoader.execute(mContext, WebserviceMethodEnums.METHOD_SET_DELIVERY_ADDRESS, query, this, true, true);
	}

	public void deleteAddress(String addressID)
	{
		QueryObjectId query = new QueryObjectId();
		query.setObjectId(addressID);
		RESTLoader.execute(mContext, WebserviceMethodEnums.METHOD_DELETE_ADDRESS, query, this, true, true);
	}

	public void getAddresses()
	{
		RESTLoader.execute(mContext, WebserviceMethodEnums.METHOD_GET_ADDRESSES, null, this, true, true);
	}

	@Override
	public void onReceiveResult(RESTLoaderResponse restLoaderResponse, WebserviceMethodEnums webserviceEnumMethod)
	{
		if (restLoaderResponse.getCode() == RESTLoaderResponse.SUCCESS)
		{
			switch (webserviceEnumMethod)
			{
				case METHOD_DELETE_ADDRESS:
					getAddresses();
					break;

				case METHOD_SET_DELIVERY_ADDRESS:
					CartDeliveryAddress cartDeliveryAddress = JsonUtils.fromJson(restLoaderResponse.getData(), "deliveryAddress",
							CartDeliveryAddress.class);

					if (cartDeliveryAddress.getId() != null)
					{
						notifyOutboxHandlers(AddressListController.MESSAGE_SET_DELIVERY_ADDRESS_SUCCESS, 0, 0, null);
					}
					else
					{
						notifyOutboxHandlers(AddressListController.MESSAGE_SET_DELIVERY_ADDRESS_ERROR, 0, 0,
								restLoaderResponse.getData());
					}

					break;

				case METHOD_GET_ADDRESSES:
					model.clear();
					model.addAll(JsonUtils.fromJsonList(restLoaderResponse.getData(), "addresses", CartDeliveryAddress.class));
					notifyOutboxHandlers(AddressListController.MESSAGE_MODEL_UPDATED, 0, 0, null);
					break;

				default:
					break;
			}
		}
		else if (restLoaderResponse.getCode() == RESTLoaderResponse.ERROR)
		{
			switch (webserviceEnumMethod)
			{
				case METHOD_DELETE_ADDRESS:
					notifyOutboxHandlers(AddressListController.MESSAGE_MODEL_UPDATED, 0, 0, null);
					break;

				default:
					break;
			}
		}
	}

}
