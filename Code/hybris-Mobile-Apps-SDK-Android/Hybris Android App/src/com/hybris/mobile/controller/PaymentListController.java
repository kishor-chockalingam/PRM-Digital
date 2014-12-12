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
import com.hybris.mobile.model.cart.CartPaymentInfo;
import com.hybris.mobile.query.QueryObjectId;
import com.hybris.mobile.utility.JsonUtils;


public class PaymentListController extends Controller implements RESTLoaderObserver
{

	private List<CartPaymentInfo> mModel;
	public static final int MESSAGE_MODEL_UPDATED = 1;
	public static final int MESSAGE_SET_PAYMENT_INFO_SUCCESS = 2;
	public static final int MESSAGE_SET_PAYMENT_INFO_ERROR = 3;
	private Activity mContext;

	public PaymentListController(List<CartPaymentInfo> model, Activity context)
	{
		mModel = model;
		mContext = context;
	}

	public List<CartPaymentInfo> getModel()
	{
		return mModel;
	}

	public void setPaymentInfoForCart(String paymentID)
	{
		QueryObjectId query = new QueryObjectId();
		query.setObjectId(paymentID);
		RESTLoader.execute(mContext, WebserviceMethodEnums.METHOD_SET_PAYMENT_INFO_FOR_CARD, query, this, true, true);
	}

	public void deletePayment(String paymentID)
	{
		QueryObjectId query = new QueryObjectId();
		query.setObjectId(paymentID);
		RESTLoader.execute(mContext, WebserviceMethodEnums.METHOD_DELETE_PAYMENT_INFO, query, this, true, true);
	}

	public void getPayments()
	{
		RESTLoader.execute(mContext, WebserviceMethodEnums.METHOD_GET_PAYMENT_INFOS, null, this, true, true);
	}

	@Override
	public void onReceiveResult(RESTLoaderResponse restLoaderResponse, WebserviceMethodEnums webserviceEnumMethod)
	{
		if (restLoaderResponse.getCode() == RESTLoaderResponse.SUCCESS)
		{
			switch (webserviceEnumMethod)
			{
				case METHOD_DELETE_PAYMENT_INFO:
					getPayments();
					break;

				case METHOD_GET_PAYMENT_INFOS:
					mModel.clear();
					mModel.addAll(JsonUtils.fromJsonList(restLoaderResponse.getData(), "paymentInfos", CartPaymentInfo.class));
					notifyOutboxHandlers(PaymentListController.MESSAGE_MODEL_UPDATED, 0, 0, null);
					break;

				case METHOD_SET_PAYMENT_INFO_FOR_CARD:
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
				case METHOD_DELETE_PAYMENT_INFO:
				case METHOD_GET_PAYMENT_INFOS:
					notifyOutboxHandlers(PaymentListController.MESSAGE_MODEL_UPDATED, 0, 0, null);
					break;

				case METHOD_SET_PAYMENT_INFO_FOR_CARD:
					notifyOutboxHandlers(DeliveryMethodController.MESSAGE_SET_CART_DELIVERY_MODE_ERROR, 0, 0,
							restLoaderResponse.getData());
					break;
				default:
					break;
			}
		}
	}

}
