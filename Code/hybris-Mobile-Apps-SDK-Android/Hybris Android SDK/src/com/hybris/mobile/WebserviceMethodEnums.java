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
package com.hybris.mobile;

public enum WebserviceMethodEnums
{
	METHOD_ADD_PRODUCT_TO_CART, METHOD_AUTHORIZE_CREDIT_CARD, METHOD_CARDTYPES, METHOD_CREATE_ADDRESS, METHOD_CREATE_PAYMENT_INFO, METHOD_CURRENCIES, METHOD_DELETE_ADDRESS, METHOD_DELETE_CART_DELIVERY_ADDRESS, METHOD_DELETE_CART_DELIVERY_MODE, METHOD_DELETE_PAYMENT_INFO, METHOD_DELETE_PRODUCT_IN_CART, METHOD_DELIVERY_COUNTRIES, METHOD_GET_ADDRESSES, METHOD_GET_CART, METHOD_GET_CART_DELIVERY_MODES, METHOD_GET_ORDER, METHOD_GET_ORDERS, METHOD_GET_PAYMENT_INFO, METHOD_GET_PAYMENT_INFOS, METHOD_GET_PRODUCTS, METHOD_GET_PROFILE, METHOD_GET_PRODUCT_WITH_CODE, METHOD_LANGUAGES, METHOD_LOGIN, METHOD_LOGOUT, METHOD_PLACE_ORDER, METHOD_REGISTER_CUSTOMER, METHOD_REQUEST_PASSWORD, METHOD_SET_CART_DELIVERY_MODE, METHOD_SET_DEFAULT_ADDRESS, METHOD_SET_DELIVERY_ADDRESS, METHOD_SET_PAYMENT_INFO_FOR_CARD, METHOD_SPELLING_SUGGESTIONS, METHOD_STORES, METHOD_STORES_FROM_LOCATION, METHOD_TITLES, METHOD_UPDATE_ADDRESS, METHOD_UPDATE_BILLING_ADDRESS, METHOD_UPDATE_LOGIN, METHOD_UPDATE_PASSWORD, METHOD_UPDATE_PAYMENT_INFO, METHOD_UPDATE_PRODUCT_IN_CART, METHOD_UPDATE_PROFILE;

	public int getCode()
	{
		return this.hashCode();
	}

}
