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
package com.hybris.mobile.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.hybris.mobile.WebserviceMethodEnums;
import com.hybris.mobile.data.WebService;
import com.hybris.mobile.query.Query;
import com.hybris.mobile.query.QueryAddress;
import com.hybris.mobile.query.QueryCart;
import com.hybris.mobile.query.QueryCustomer;
import com.hybris.mobile.query.QueryObjectId;
import com.hybris.mobile.query.QueryOrder;
import com.hybris.mobile.query.QueryPaymentInfo;
import com.hybris.mobile.query.QueryProducts;
import com.hybris.mobile.query.QuerySingleProduct;
import com.hybris.mobile.query.QueryStore;
import com.hybris.mobile.query.QueryText;
import com.hybris.mobile.query.QueryUpateCredentials;


public class RESTAsyncTaskLoader extends AsyncTaskLoader<RESTLoaderResponse>
{

	private Query mQuery;
	private WebserviceMethodEnums mMethod;

	public RESTAsyncTaskLoader(Context context, Query query, WebserviceMethodEnums method)
	{
		super(context);
		mQuery = query;
		mMethod = method;
	}

	@Override
	public RESTLoaderResponse loadInBackground()
	{

		try
		{

			String result = null;

			switch (mMethod)
			{

				case METHOD_ADD_PRODUCT_TO_CART:
					result = addProductToCart((QueryCart) mQuery);
					break;

				case METHOD_AUTHORIZE_CREDIT_CARD:
					result = authorizeCreditCard((QueryObjectId) mQuery);
					break;

				case METHOD_CARDTYPES:
					result = WebService.getWebService().cardTypes();
					break;

				case METHOD_CREATE_ADDRESS:
					result = createAddress((QueryAddress) mQuery);
					break;

				case METHOD_CREATE_PAYMENT_INFO:
					result = createPaymentInfo((QueryPaymentInfo) mQuery);
					break;

				case METHOD_CURRENCIES:
					result = WebService.getWebService().currencies();
					break;

				case METHOD_DELETE_ADDRESS:
					result = deleteAddress((QueryObjectId) mQuery);
					break;

				case METHOD_DELETE_CART_DELIVERY_ADDRESS:
					result = WebService.getWebService().deleteCartDeliveryAddress();
					break;

				case METHOD_DELETE_CART_DELIVERY_MODE:
					result = WebService.getWebService().deleteCartDeliveryMode();
					break;

				case METHOD_DELETE_PAYMENT_INFO:
					result = deletePaymentInfo((QueryObjectId) mQuery);
					break;

				case METHOD_DELETE_PRODUCT_IN_CART:
					result = deleteProductInCartAtEntry((QueryObjectId) mQuery);
					break;

				case METHOD_DELIVERY_COUNTRIES:
					result = WebService.getWebService().deliveryCountries();
					break;

				case METHOD_GET_ADDRESSES:
					result = WebService.getWebService().getAddresses();
					break;

				case METHOD_GET_CART:
					result = WebService.getWebService().getCart();
					break;

				case METHOD_GET_CART_DELIVERY_MODES:
					result = WebService.getWebService().getCartDeliveryModes();
					break;

				case METHOD_GET_ORDER:
					result = getOrder((QueryObjectId) mQuery);
					break;

				case METHOD_GET_ORDERS:
					result = getOrders((QueryOrder) mQuery);
					break;

				case METHOD_GET_PAYMENT_INFO:
					result = getPaymentInfo((QueryObjectId) mQuery);
					break;

				case METHOD_GET_PAYMENT_INFOS:
					result = WebService.getWebService().getPaymentInfos();
					break;

				case METHOD_GET_PRODUCTS:
					result = getProducts((QueryProducts) mQuery);
					break;

				case METHOD_GET_PROFILE:
					result = WebService.getWebService().getProfile();
					break;

				case METHOD_GET_PRODUCT_WITH_CODE:
					result = getProductWithCode((QuerySingleProduct) mQuery);
					break;

				case METHOD_LANGUAGES:
					result = WebService.getWebService().languages();
					break;

				case METHOD_LOGOUT:
					result = WebService.getWebService().logoutUser();
					break;

				case METHOD_LOGIN:
					result = loginUser((QueryCustomer) mQuery);
					break;

				case METHOD_PLACE_ORDER:
					result = WebService.getWebService().placeOrder();
					break;

				case METHOD_REGISTER_CUSTOMER:
					result = registerCustomer((QueryCustomer) mQuery);
					break;

				case METHOD_REQUEST_PASSWORD:
					result = requestPassword((QueryObjectId) mQuery);
					break;

				case METHOD_SET_CART_DELIVERY_MODE:
					result = setCartDeliveryMode((QueryObjectId) mQuery);
					break;

				case METHOD_SET_DEFAULT_ADDRESS:
					result = setDefaultAddress((QueryObjectId) mQuery);
					break;

				case METHOD_SET_DELIVERY_ADDRESS:
					result = setDeliveryAddress((QueryObjectId) mQuery);
					break;

				case METHOD_SET_PAYMENT_INFO_FOR_CARD:
					result = setPaymentInfoForCart((QueryObjectId) mQuery);
					break;

				case METHOD_SPELLING_SUGGESTIONS:
					result = WebService.getWebService().spellingSuggestions(((QueryText) mQuery).getQueryText());
					break;

				case METHOD_STORES:
					result = stores((QueryStore) mQuery);
					break;

				case METHOD_STORES_FROM_LOCATION:
					result = storesFromLocation((QueryStore) mQuery);
					break;

				case METHOD_TITLES:
					result = WebService.getWebService().titles();
					break;

				case METHOD_UPDATE_ADDRESS:
					result = updateAddress((QueryAddress) mQuery);
					break;

				case METHOD_UPDATE_BILLING_ADDRESS:
					result = updateBillingAddress((QueryPaymentInfo) mQuery);
					break;

				case METHOD_UPDATE_LOGIN:
					result = updateLogin((QueryUpateCredentials) mQuery);
					break;

				case METHOD_UPDATE_PASSWORD:
					result = updatePassword((QueryUpateCredentials) mQuery);
					break;

				case METHOD_UPDATE_PAYMENT_INFO:
					result = updatePaymentInfo((QueryPaymentInfo) mQuery);
					break;

				case METHOD_UPDATE_PRODUCT_IN_CART:
					result = updateProductInCartAtEntry((QueryCart) mQuery);
					break;

				case METHOD_UPDATE_PROFILE:
					result = updateProfile((QueryCustomer) mQuery);
					break;

				default:
					break;

			}

			return RESTLoaderResponse.getResponse(RESTLoaderResponse.SUCCESS, result);

		}
		catch (Exception e)
		{
			return RESTLoaderResponse.getResponse(RESTLoaderResponse.ERROR, e.getLocalizedMessage());
		}

	}

	/**
	 * The following methods are wrapper to call the webservice methods with the right parameters
	 * 
	 * @param query
	 * @return
	 * @throws Exception
	 */

	private String authorizeCreditCard(QueryObjectId query) throws Exception
	{
		return WebService.getWebService().authorizeCreditCard(query.getObjectId());
	}

	private String addProductToCart(QueryCart query) throws Exception
	{
		return WebService.getWebService().addProductToCart(query.getProductCode(), query.getQuantity());
	}

	private String createAddress(QueryAddress query) throws Exception
	{
		return WebService.getWebService().createAddress(query.getFirstName(), query.getLastName(), query.getTitleCode(),
				query.getAddressLine1(), query.getAddressLine2(), query.getTown(), query.getPostCode(), query.getCountryISOCode());
	}

	private String createPaymentInfo(QueryPaymentInfo query) throws Exception
	{
		return WebService.getWebService().createPaymentInfoForCart(query.getAccountHolderName(), query.getCardNumber(),
				query.getCardType(), query.getExpiryMonth(), query.getExpiryYear(), query.isShouldSave(), query.isDefault(),
				query.getQueryAddress().getTitleCode(), query.getQueryAddress().getFirstName(),
				query.getQueryAddress().getLastName(), query.getQueryAddress().getAddressLine1(),
				query.getQueryAddress().getAddressLine2(), query.getQueryAddress().getPostCode(), query.getQueryAddress().getTown(),
				query.getQueryAddress().getCountryISOCode());
	}

	private String deleteAddress(QueryObjectId query) throws Exception
	{
		return WebService.getWebService().deleteAddress(query.getObjectId());
	}

	private String deletePaymentInfo(QueryObjectId query) throws Exception
	{
		return WebService.getWebService().deletePaymentInfo(query.getObjectId());
	}

	private String deleteProductInCartAtEntry(QueryObjectId query) throws Exception
	{
		return WebService.getWebService().deleteProductInCartAtEntry(query.getObjectId());
	}

	private String getOrder(QueryObjectId query) throws Exception
	{
		return WebService.getWebService().getOrder(query.getObjectId());
	}

	private String getPaymentInfo(QueryObjectId query) throws Exception
	{
		return WebService.getWebService().getPaymentInfo(query.getObjectId());
	}

	private String getOrders(QueryOrder query) throws Exception
	{
		return WebService.getWebService().getOrders(query.getStatuses(), query.getCurrentPage(), query.getPageSize());
	}

	private String getProducts(QueryProducts query) throws Exception
	{
		String sortCode = query.getSelectedSort() != null ? query.getSelectedSort().getCode() : null;
		String searchTag = query.getSelectedCategory() != null ? query.getSelectedCategory().getSearchTag() : null;

		return WebService.getWebService().getProducts(query.getQueryText(), sortCode, query.getSelectedFacetValues(), searchTag,
				query.getPageSize(), query.getCurrentPage());
	}

	private String getProductWithCode(QuerySingleProduct query) throws Exception
	{
		return WebService.getWebService().getProductWithCode(query.getOptions(), query.getProductCode());
	}

	private String loginUser(QueryCustomer query) throws Exception
	{
		return WebService.getWebService().loginUser(query.getLogin(), query.getPassword());
	}

	private String registerCustomer(QueryCustomer query) throws Exception
	{
		return WebService.getWebService().registerCustomer(query.getFirstName(), query.getLastName(), query.getTitleCode(),
				query.getLogin(), query.getPassword());
	}

	private String requestPassword(QueryObjectId query) throws Exception
	{
		return WebService.getWebService().requestPassword(query.getObjectId());
	}

	private String setCartDeliveryMode(QueryObjectId query) throws Exception
	{
		return WebService.getWebService().setCartDeliveryMode(query.getObjectId());
	}

	private String setDefaultAddress(QueryObjectId query) throws Exception
	{
		return WebService.getWebService().setDefaultAddress(query.getObjectId());
	}

	private String setDeliveryAddress(QueryObjectId query) throws Exception
	{
		return WebService.getWebService().setDeliveryAddress(query.getObjectId());
	}

	private String setPaymentInfoForCart(QueryObjectId query) throws Exception
	{
		return WebService.getWebService().setPaymentInfoForCart(query.getObjectId());
	}

	private String stores(QueryStore query) throws Exception
	{
		return WebService.getWebService().stores(query.getQueryText(), query.getCurrentPage(), query.getPageSize());
	}

	private String storesFromLocation(QueryStore query) throws Exception
	{
		return WebService.getWebService().storesFromLocation(query.getLongitude(), query.getLatitude(), query.getRadius(),
				query.getAccuracy(), query.getCurrentPage());
	}

	private String updateAddress(QueryAddress query) throws Exception
	{
		return WebService.getWebService().updateAddress(query.getFirstName(), query.getLastName(), query.getTitleCode(),
				query.getAddressLine1(), query.getAddressLine2(), query.getTown(), query.getPostCode(), query.getCountryISOCode(),
				query.getAddressId());
	}

	private String updateBillingAddress(QueryPaymentInfo query) throws Exception
	{
		if (query.getQueryAddress() != null)
		{
			return WebService.getWebService().updateBillingAddress(query.getQueryAddress().getFirstName(),
					query.getQueryAddress().getLastName(), query.getQueryAddress().getTitleCode(),
					query.getQueryAddress().getAddressLine1(), query.getQueryAddress().getAddressLine2(),
					query.getQueryAddress().getTown(), query.getQueryAddress().getPostCode(),
					query.getQueryAddress().getCountryISOCode(), query.isDefault(), query.getPaymentId());
		}
		else
		{
			return null;
		}
	}

	private String updateLogin(QueryUpateCredentials query) throws Exception
	{
		return WebService.getWebService().updateLogin(query.getNewValue(), query.getPassword());
	}

	private String updatePassword(QueryUpateCredentials query) throws Exception
	{
		return WebService.getWebService().updatePassword(query.getOldValue(), query.getNewValue());
	}

	private String updatePaymentInfo(QueryPaymentInfo query) throws Exception
	{
		return WebService.getWebService().updatePaymentInfo(query.getAccountHolderName(), query.getCardNumber(),
				query.getCardType(), query.getExpiryMonth(), query.getExpiryYear(), query.isShouldSave(), query.isDefault(),
				query.getPaymentId());
	}

	private String updateProductInCartAtEntry(QueryCart query) throws Exception
	{
		return WebService.getWebService().updateProductInCartAtEntry(query.getQuantity(), query.getCartEntryNumber());
	}

	private String updateProfile(QueryCustomer query) throws Exception
	{
		return WebService.getWebService().updateProfile(query.getFirstName(), query.getLastName(), query.getTitleCode(),
				query.getLanguage(), query.getCurrency());
	}

}
