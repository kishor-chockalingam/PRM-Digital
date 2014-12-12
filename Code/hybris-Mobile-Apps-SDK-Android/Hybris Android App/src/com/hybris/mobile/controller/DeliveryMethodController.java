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
import com.hybris.mobile.model.cart.CartDeliveryMode;
import com.hybris.mobile.query.QueryObjectId;
import com.hybris.mobile.utility.JsonUtils;


public class DeliveryMethodController extends Controller implements RESTLoaderObserver
{

	private List<CartDeliveryMode> model;

	public static final int MESSAGE_MODEL_UPDATED = 1;
	public static final int MESSAGE_SET_CART_DELIVERY_MODE_SUCCESS = 2;
	public static final int MESSAGE_SET_CART_DELIVERY_MODE_ERROR = 3;

	public DeliveryMethodController(List<CartDeliveryMode> model)
	{
		this.model = model;
	}

	public List<CartDeliveryMode> getModel()
	{
		return model;
	}

	public void setCartDeliveryMode(Activity context, String code)
	{
		QueryObjectId query = new QueryObjectId();
		query.setObjectId(code);
		RESTLoader.execute(context, WebserviceMethodEnums.METHOD_SET_CART_DELIVERY_MODE, query, this, true, true);
	}

	public void getCartDeliveryModes(Activity context)
	{
		RESTLoader.execute(context, WebserviceMethodEnums.METHOD_GET_CART_DELIVERY_MODES, null, this, true, true);

		//		WebService.getWebService(Hybris.getAppContext()).getCartDeliveryModes(new CartDeliveryModesResultListener()
		//		{
		//
		//			@Override
		//			public void onException(Exception e)
		//			{
		//				notifyOutboxHandlers(DeliveryMethodController.MESSAGE_MODEL_UPDATED, 0, 0, null);
		//			}
		//
		//			@Override
		//			public void onComplete(final List<CartDeliveryMode> response)
		//			{
		//				mWorkerHandler.post(new Runnable()
		//				{
		//					@Override
		//					public void run()
		//					{
		//						synchronized (model)
		//						{
		//							model.clear();
		//
		//							model.addAll(response);
		//
		//							notifyOutboxHandlers(DeliveryMethodController.MESSAGE_MODEL_UPDATED, 0, 0, null);
		//						}
		//					}
		//				});
		//			}
		//		});
	}

	@Override
	public void onReceiveResult(RESTLoaderResponse restLoaderResponse, WebserviceMethodEnums webserviceEnumMethod)
	{
		if (restLoaderResponse.getCode() == RESTLoaderResponse.SUCCESS)
		{
			switch (webserviceEnumMethod)
			{
				case METHOD_GET_CART_DELIVERY_MODES:
					model.clear();

					model.addAll(JsonUtils.fromJsonList(restLoaderResponse.getData(), "deliveryModes", CartDeliveryMode.class));

					notifyOutboxHandlers(DeliveryMethodController.MESSAGE_MODEL_UPDATED, 0, 0, null);

					break;
				case METHOD_SET_CART_DELIVERY_MODE:
					notifyOutboxHandlers(DeliveryMethodController.MESSAGE_SET_CART_DELIVERY_MODE_SUCCESS, 0, 0, null);
					break;

				default:
					break;
			}
		}
		else if (restLoaderResponse.getCode() == RESTLoaderResponse.ERROR)
		{
			switch (webserviceEnumMethod)
			{
				case METHOD_GET_CART_DELIVERY_MODES:
					notifyOutboxHandlers(DeliveryMethodController.MESSAGE_MODEL_UPDATED, 0, 0, null);
					break;

				case METHOD_SET_CART_DELIVERY_MODE:
					notifyOutboxHandlers(DeliveryMethodController.MESSAGE_SET_CART_DELIVERY_MODE_ERROR, 0, 0,
							restLoaderResponse.getData());
					break;

				default:
					break;
			}
		}
	}

}
