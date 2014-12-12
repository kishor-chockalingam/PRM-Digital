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
package com.hybris.mobile.data;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;

import com.hybris.mobile.InternalConstants;
import com.hybris.mobile.SDKSettings;
import com.hybris.mobile.logging.LoggingUtils;
import com.hybris.mobile.model.FacetValue;
import com.hybris.mobile.model.TokenLogin;
import com.hybris.mobile.model.product.ProductStockLevelStatus;
import com.hybris.mobile.utility.JsonUtils;


public final class WebService implements HYOCCInterface
{
	private static final String LOG_CAT = WebService.class.getSimpleName();
	private static HYOCCInterface mWebService;
	private Context mContext;

	private WebService(Context context)
	{
		super();
		mContext = context;
	}

	public static void initWebService(Context context)
	{
		if (mWebService == null)
		{
			mWebService = new WebService(context);
		}
	}

	public static HYOCCInterface getWebService() throws Exception
	{
		if (mWebService == null)
		{
			LoggingUtils.e(LOG_CAT, "You must call initWebService first", null);
			throw new Exception("You must call initWebService first");
		}

		return mWebService;
	}

	private static String baseUrl()
	{
		//return String.format("%s/rest/v1", SDKSettings.getSettingValue(InternalConstants.KEY_PREF_BASE_URL));
		return String.format("%s/bncwebservices/v1", SDKSettings.getSettingValue(InternalConstants.KEY_PREF_BASE_URL));
	}

	private static String urlForLanguages()
	{
		return String.format("%s/%slanguages/", baseUrl(), SDKSettings.getSettingValue(InternalConstants.KEY_PREF_CATALOG));
	}

	private static String urlForCurrencies()
	{
		return String.format("%s/%scurrencies/", baseUrl(), SDKSettings.getSettingValue(InternalConstants.KEY_PREF_CATALOG));
	}

	private static String urlForTitles()
	{
		return String.format("%s/%stitles/", baseUrl(), SDKSettings.getSettingValue(InternalConstants.KEY_PREF_CATALOG));
	}

	private static String urlForCardTypes()
	{
		return String.format("%s/%scardtypes/", baseUrl(), SDKSettings.getSettingValue(InternalConstants.KEY_PREF_CATALOG));
	}

	private static String urlForDeliveryCountries()
	{
		return String.format("%s/%sdeliverycountries/", baseUrl(), SDKSettings.getSettingValue(InternalConstants.KEY_PREF_CATALOG));
	}

	private static String urlForProducts()
	{
		return String.format("%s/%sproducts/", baseUrl(), SDKSettings.getSettingValue(InternalConstants.KEY_PREF_CATALOG));
	}

	private static String urlForCart()
	{
		return String.format("%s/%scart/", baseUrl(), SDKSettings.getSettingValue(InternalConstants.KEY_PREF_CATALOG));
	}

	private static String urlForOrders()
	{
		return String.format("%s/%sorders/", baseUrl(), SDKSettings.getSettingValue(InternalConstants.KEY_PREF_CATALOG));
	}

	private static String urlForCartEntry()
	{
		return String.format("%s/%scart/entry", baseUrl(), SDKSettings.getSettingValue(InternalConstants.KEY_PREF_CATALOG));
	}

	private static String urlForCustomer()
	{
		return String.format("%s/%scustomers/", baseUrl(), SDKSettings.getSettingValue(InternalConstants.KEY_PREF_CATALOG));
	}

	private static String urlForStores()
	{
		return String.format("%s/%sstores/", baseUrl(), SDKSettings.getSettingValue(InternalConstants.KEY_PREF_CATALOG));
	}

	@Override
	public String languages() throws Exception
	{
		try
		{
			return WebServiceDataProvider.getResponse(mContext, urlForLanguages(), false, "GET", null);
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_CAT, e.getLocalizedMessage(), null);
			throw new Exception(e);
		}
	}

	@Override
	public String currencies() throws Exception
	{
		try
		{
			return WebServiceDataProvider.getResponse(mContext, urlForCurrencies(), false, "GET", null);
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_CAT, e.getLocalizedMessage(), null);
			throw new Exception(e);
		}
	}

	@Override
	public String titles() throws Exception
	{
		try
		{
			return WebServiceDataProvider.getResponse(mContext, urlForTitles(), false, "GET", null);
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_CAT, e.getLocalizedMessage(), null);
			throw new Exception(e);
		}
	}

	@Override
	public String cardTypes() throws Exception
	{
		try
		{
			return WebServiceDataProvider.getResponse(mContext, urlForCardTypes(), false, "GET", null);
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_CAT, e.getLocalizedMessage(), null);
			throw new Exception(e);
		}
	}

	@Override
	public String deliveryCountries() throws Exception
	{
		try
		{
			return WebServiceDataProvider.getResponse(mContext, urlForDeliveryCountries(), false, "GET", null);
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_CAT, e.getLocalizedMessage(), null);
			throw new Exception(e);
		}
	}

	@Override
	public String spellingSuggestions(String text) throws Exception
	{
		try
		{
			String suggestionURL = String.format("%ssuggest?term=%s&max=10", urlForProducts(), text);
			return WebServiceDataProvider.getResponse(mContext, suggestionURL, false, "GET", null);
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_CAT, e.getLocalizedMessage(), null);
			throw new Exception(e);
		}
	}

	@Override
	public String stores(String text, int currentPage, int pageSize) throws Exception
	{
		try
		{
			String storeURL = String.format("%s?query=%s&options=HOURS&currentPage=%s&pageSize=%s", urlForStores(), text,
					currentPage, pageSize);
			return WebServiceDataProvider.getResponse(mContext, storeURL, false, "GET", null);
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_CAT, e.getLocalizedMessage(), null);
			throw new Exception(e);
		}
	}

	@Override
	public String storesFromLocation(double longitude, double latitude, float radius, float accuracy, int currentPage)
			throws Exception
	{
		try
		{
			String storeURL = String.format("%s?longitude=%f&latitude=%f&radius=%f&accuracy=%f&options=HOURS&currentPage=%s",
					urlForStores(), longitude, latitude, radius, accuracy, currentPage);
			return WebServiceDataProvider.getResponse(mContext, storeURL, false, "GET", null);
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_CAT, e.getLocalizedMessage(), null);
			throw new Exception(e);
		}
	}

	@Override
	public String getCart() throws Exception
	{
		try
		{
			return WebServiceDataProvider.getResponse(mContext, urlForCart(), false, "GET", null);
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_CAT, e.getLocalizedMessage(), null);
			throw new Exception(e);
		}
	}

	@Override
	public String addProductToCart(String productCode, int quantity) throws Exception
	{
		Bundle parameters = new Bundle();
		parameters.putString("code", productCode);
		parameters.putString("qty", quantity + "");
		try
		{
			return WebServiceDataProvider.getResponse(mContext, urlForCartEntry(), false, "POST", parameters);
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_CAT, e.getLocalizedMessage(), null);
			throw new Exception(e);
		}
	}

	@Override
	public String updateProductInCartAtEntry(int quantity, int carEntryNumber) throws Exception
	{
		Bundle parameters = new Bundle();
		parameters.putString("qty", quantity + "");
		try
		{
			String response = WebServiceDataProvider.getResponse(mContext, urlForCartEntry() + "/" + carEntryNumber, false, "PUT",
					parameters);

			ProductStockLevelStatus productStockLevelStatus = JsonUtils.fromJson(response, ProductStockLevelStatus.class);

			if (productStockLevelStatus != null && StringUtils.equalsIgnoreCase(productStockLevelStatus.getStatusCode(), "success"))
			{
				return response;
			}
			else
			{
				throw new Exception(response);
			}

		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_CAT, e.getLocalizedMessage(), null);
			throw new Exception(e);
		}
	}

	@Override
	public String deleteProductInCartAtEntry(String carEntryNumber) throws Exception
	{
		try
		{
			String response = WebServiceDataProvider.getResponse(mContext, urlForCartEntry() + "/" + carEntryNumber, false,
					"DELETE", null);

			ProductStockLevelStatus productStockLevelStatus = JsonUtils.fromJson(response, ProductStockLevelStatus.class);

			if (productStockLevelStatus != null && StringUtils.equalsIgnoreCase(productStockLevelStatus.getStatusCode(), "success"))
			{
				return response;
			}
			else
			{
				throw new Exception(response);
			}

		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_CAT, e.getLocalizedMessage(), null);
			throw new Exception(e);
		}
	}

	@Override
	public String getCartDeliveryModes() throws Exception
	{
		try
		{
			String url = urlForCart() + "deliverymodes";
			return WebServiceDataProvider.getResponse(mContext, url, true, "GET", null);
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_CAT, e.getLocalizedMessage(), null);
			throw new Exception(e);
		}
	}

	@Override
	public String setCartDeliveryMode(String deliveryModeId) throws Exception
	{
		try
		{
			String url = urlForCart() + "deliverymodes/" + deliveryModeId;
			return WebServiceDataProvider.getResponse(mContext, url, true, "PUT", null);
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_CAT, e.getLocalizedMessage(), null);
			throw new Exception(e);
		}
	}

	@Override
	public String deleteCartDeliveryMode() throws Exception
	{

		try
		{
			String url = urlForCart() + "deliverymodes";
			String response = WebServiceDataProvider.getResponse(mContext, url, true, "DELETE", null);

			JSONObject object = new JSONObject(response);
			if (!object.has("deliveryModes"))
			{
				return response;
			}
			else
			{
				throw new Exception(response);
			}
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_CAT, e.getLocalizedMessage(), null);
			throw new Exception(e);
		}
	}

	@Override
	public String placeOrder() throws Exception
	{
		try
		{
			String url = urlForCart() + "placeorder";
			return WebServiceDataProvider.getResponse(mContext, url, true, "POST", null);
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_CAT, e.getLocalizedMessage(), null);
			throw new Exception(e);
		}
	}

	@Override
	public String setDeliveryAddress(String addressId) throws Exception
	{
		try
		{
			String url = urlForCart() + "address/delivery/" + addressId;
			return WebServiceDataProvider.getResponse(mContext, url, true, "PUT", null);
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_CAT, e.getLocalizedMessage(), null);
			throw new Exception(e);
		}
	}

	@Override
	public String deleteCartDeliveryAddress() throws Exception
	{

		try
		{
			String url = urlForCart() + "address/delivery/";
			return WebServiceDataProvider.getResponse(mContext, url, true, "DELETE", null);
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_CAT, e.getLocalizedMessage(), null);
			throw new Exception(e);
		}
	}

	@Override
	public String getOrders(String status, int currentPage, int pageSize) throws Exception
	{
		try
		{
			String queryString = "statuses=" + status;
			queryString += "&currentPage=" + currentPage;
			queryString += "&pageSize=" + pageSize;

			String url = urlForOrders() + "?" + queryString;
			return WebServiceDataProvider.getResponse(mContext, url, true, "GET", null);
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_CAT, e.getLocalizedMessage(), null);
			throw new Exception(e);
		}
	}

	@Override
	public String getOrder(String orderId) throws Exception
	{
		try
		{
			String url = urlForOrders() + orderId;
			return WebServiceDataProvider.getResponse(mContext, url, true, "GET", null);
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_CAT, e.getLocalizedMessage(), null);
			throw new Exception(e);
		}
	}

	@Override
	public String createPaymentInfoForCart(String accountHolderName, String cardNumber, String cardType, String expiryMonth,
			String expiryYear, boolean shouldSave, boolean isDefault, String titleCode, String firstName, String lastName,
			String address1, String address2, String postCode, String town, String countryISOCode) throws Exception
	{
		try
		{
			String url = urlForCart() + "paymentinfo";

			Bundle postBody = new Bundle();
			postBody.putString("accountHolderName", accountHolderName);
			postBody.putString("cardNumber", cardNumber);
			postBody.putString("cardType", cardType);
			postBody.putString("expiryMonth", expiryMonth);
			postBody.putString("expiryYear", expiryYear);
			postBody.putString("saved", Boolean.toString(shouldSave));
			postBody.putString("defaultPaymentInfo", Boolean.toString(isDefault));
			postBody.putString("billingAddress.titleCode", titleCode);
			postBody.putString("billingAddress.firstName", firstName);
			postBody.putString("billingAddress.lastName", lastName);
			postBody.putString("billingAddress.line1", address1);
			postBody.putString("billingAddress.line2", address2);
			postBody.putString("billingAddress.postalCode", postCode);
			postBody.putString("billingAddress.town", town);
			postBody.putString("billingAddress.country.isocode", countryISOCode);

			return WebServiceDataProvider.getResponse(mContext, url, true, "POST", postBody);
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_CAT, e.getLocalizedMessage(), null);
			throw new Exception(e);
		}
	}

	@Override
	public String getPaymentInfo(String paymentId) throws Exception
	{
		try
		{
			String url = urlForCustomer() + "current/paymentinfos/" + paymentId;
			return WebServiceDataProvider.getResponse(mContext, url, true, "GET", null);
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_CAT, e.getLocalizedMessage(), null);
			throw new Exception(e);
		}
	}

	@Override
	public String setPaymentInfoForCart(String paymentId) throws Exception
	{
		try
		{
			String url = urlForCart() + "paymentinfo/" + paymentId;
			return WebServiceDataProvider.getResponse(mContext, url, true, "PUT", null);
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_CAT, e.getLocalizedMessage(), null);
			throw new Exception(e);
		}
	}

	@Override
	public String authorizeCreditCard(String securityCode) throws Exception
	{
		try
		{
			String url = urlForCart() + "authorize";
			Bundle postBody = new Bundle();
			postBody.putString("securityCode", securityCode);
			String response = WebServiceDataProvider.getResponse(mContext, url, true, "POST", postBody);
			JSONObject object = new JSONObject(response);

			if (object.has("code"))
			{
				return response;
			}
			else
			{
				throw new Exception(response);
			}
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_CAT, e.getLocalizedMessage(), null);
			throw new Exception(e);
		}
	}

	@Override
	public String getPaymentInfos() throws Exception
	{
		try
		{
			String url = urlForCustomer() + "current/paymentinfos";
			return WebServiceDataProvider.getResponse(mContext, url, true, "GET", null);
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_CAT, e.getLocalizedMessage(), null);
			throw new Exception(e);
		}
	}

	@Override
	public String deletePaymentInfo(String paymentId) throws Exception
	{
		try
		{
			String url = urlForCustomer() + "current/paymentinfos/" + paymentId;
			String response = WebServiceDataProvider.getResponse(mContext, url, true, "DELETE", null);

			if (StringUtils.isNotBlank(response))
			{
				throw new Exception(response);
			}
			else
			{
				return response;
			}
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_CAT, e.getLocalizedMessage(), null);
			throw new Exception(e);
		}
	}

	@Override
	public String loginUser(String login, String password) throws Exception
	{
		String response;
		Bundle postBody = new Bundle();
		postBody.putString("grant_type", "password");
		postBody.putString("username", login);
		postBody.putString("password", password);
		try
		{
			response = WebServiceDataProvider.getLoginResponse(postBody);

			TokenLogin tokenLogin = JsonUtils.fromJson(response, TokenLogin.class);

			if (tokenLogin != null && StringUtils.isNotBlank(tokenLogin.getAccess_token()))
			{
				WebServiceAuthProvider.saveTokens(mContext, tokenLogin);
				return response;
			}
			else
			{
				throw new Exception(response);
			}

		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_CAT, e.getLocalizedMessage(), null);
			throw new Exception(e);
		}
	}

	@Override
	public String logoutUser() throws Exception
	{
		try
		{
			String response = WebServiceDataProvider.getLogoutResponse(mContext);

			// Clear data regardless of server response
			WebServiceAuthProvider.clearTokens(mContext);

			if (JsonUtils.has(response, "success"))
			{
				return response;
			}
			else
			{
				throw new Exception(response);
			}
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_CAT, e.getLocalizedMessage(), null);
			throw new Exception(e);
		}
	}

	@Override
	public String registerCustomer(String firstName, String lastName, String titleCode, String login, String password)
			throws Exception
	{
		try
		{
			String response = this.getCustomerToken();
			TokenLogin tokenLogin = JsonUtils.fromJson(response, TokenLogin.class);
			String credentialToken = tokenLogin.getAccess_token();

			Bundle customerDetails = new Bundle();
			customerDetails.putString("firstName", firstName);
			customerDetails.putString("lastName", lastName);
			customerDetails.putString("titleCode", titleCode);
			customerDetails.putString("login", login);
			customerDetails.putString("password", password);

			response = WebServiceDataProvider.getClientCredentialsResponse(mContext, urlForCustomer(), credentialToken, "POST",
					customerDetails);

			if (StringUtils.isBlank(response))
			{
				return response;
			}
			else
			{
				throw new Exception(response);
			}

		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_CAT, e.getLocalizedMessage(), null);
			throw new Exception(e);
		}
	}

	@Override
	public String createAddress(String firstName, String lastName, String titleCode, String addressLine1, String addressLine2,
			String town, String postCode, String countryISOCode) throws Exception
	{
		Bundle postBody = new Bundle();
		postBody.putString("titleCode", titleCode);
		postBody.putString("firstName", firstName);
		postBody.putString("lastName", lastName);
		postBody.putString("line1", addressLine1);
		postBody.putString("line2", addressLine2);
		postBody.putString("town", town);
		postBody.putString("postalCode", postCode);
		postBody.putString("country.isocode", countryISOCode);
		try
		{
			String addressURL = urlForCustomer() + "current/addresses";
			return WebServiceDataProvider.getResponse(mContext, addressURL, true, "POST", postBody);
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_CAT, e.getLocalizedMessage(), null);
			throw new Exception(e);
		}
	}

	@Override
	public String setDefaultAddress(String addressId) throws Exception
	{
		try
		{
			String addressURL = urlForCustomer() + "current/addresses/default/";
			String response = WebServiceDataProvider.getResponse(mContext, addressURL + addressId, true, "PUT", null);

			if (response.length() > 0)
			{
				throw new Exception(response);
			}
			else
			{
				return response;
			}
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_CAT, e.getLocalizedMessage(), null);
			throw new Exception(e);
		}
	}

	@Override
	public String getAddresses() throws Exception
	{
		try
		{
			String addressURL = urlForCustomer() + "current/addresses/";
			return WebServiceDataProvider.getResponse(mContext, addressURL, true, "GET", null);
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_CAT, e.getLocalizedMessage(), null);
			throw new Exception(e);
		}
	}

	@Override
	public String updateAddress(String firstName, String lastName, String titleCode, String addressLine1, String addressLine2,
			String town, String postCode, String countryISOCode, String addressID) throws Exception
	{
		Bundle postBody = new Bundle();
		postBody.putString("titleCode", titleCode);
		postBody.putString("firstName", firstName);
		postBody.putString("lastName", lastName);
		postBody.putString("line1", addressLine1);
		postBody.putString("line2", addressLine2);
		postBody.putString("town", town);
		postBody.putString("postalCode", postCode);
		postBody.putString("country.isocode", countryISOCode);
		try
		{
			String addressURL = urlForCustomer() + "current/addresses/" + addressID;
			return WebServiceDataProvider.getResponse(mContext, addressURL, true, "PUT", postBody);
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_CAT, e.getLocalizedMessage(), null);
			throw new Exception(e);
		}
	}

	@Override
	public String deleteAddress(String addressId) throws Exception
	{
		String response;
		try
		{
			String addressURL = urlForCustomer() + "current/addresses/" + addressId;
			response = WebServiceDataProvider.getResponse(mContext, addressURL, true, "DELETE", null);

			if (response.length() > 0)
			{
				throw new Exception(response);
			}
			else
			{
				return response;
			}
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_CAT, e.getLocalizedMessage(), null);
			throw new Exception(e);
		}
	}

	@Override
	public String updateProfile(String firstName, String lastName, String titleCode, String language, String currency)
			throws Exception
	{

		Bundle postBody = new Bundle();
		postBody.putString("titleCode", titleCode);
		postBody.putString("firstName", firstName);
		postBody.putString("lastName", lastName);
		postBody.putString("language", language);
		postBody.putString("currency", currency);
		try
		{
			String addressURL = urlForCustomer() + "current/profile/";
			return WebServiceDataProvider.getResponse(mContext, addressURL, true, "POST", postBody);
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_CAT, e.getLocalizedMessage(), null);
			throw new Exception(e);
		}
	}

	@Override
	public String getProfile() throws Exception
	{
		try
		{
			String profileURL = urlForCustomer() + "current/";
			return WebServiceDataProvider.getResponse(mContext, profileURL, true, "GET", null);
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_CAT, e.getLocalizedMessage(), null);
			throw new Exception(e);
		}
	}

	@Override
	public String updatePassword(String oldPassword, String newPassword) throws Exception
	{
		try
		{
			String response;
			Bundle postBody = new Bundle();
			postBody.putString("old", oldPassword);
			postBody.putString("new", newPassword);

			String profileURL = urlForCustomer() + "current/password";
			response = WebServiceDataProvider.getResponse(mContext, profileURL, true, "POST", postBody);

			if (response.length() > 0)
			{
				throw new Exception(response);
			}
			else
			{
				return response;
			}
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_CAT, e.getLocalizedMessage(), null);
			throw new Exception(e);
		}
	}

	@Override
	public String updateLogin(String newLogin, String password) throws Exception
	{
		try
		{
			String response;
			Bundle postBody = new Bundle();
			postBody.putString("newLogin", newLogin);
			postBody.putString("password", password);

			String profileURL = urlForCustomer() + "current/login";
			response = WebServiceDataProvider.getResponse(mContext, profileURL, true, "POST", postBody);
			if (response.length() > 0)
			{
				throw new Exception(response);
			}
			else
			{
				return response;
			}
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_CAT, e.getLocalizedMessage(), null);
			throw new Exception(e);
		}
	}

	@Override
	public String requestPassword(String login) throws Exception
	{
		try
		{
			String response = this.getCustomerToken();
			TokenLogin tokenLogin = JsonUtils.fromJson(response, TokenLogin.class);
			String credentialToken = tokenLogin.getAccess_token();

			Bundle postBody = new Bundle();
			postBody.putString("login", login);

			String profileURL = urlForCustomer() + "current/forgottenpassword";
			response = WebServiceDataProvider.getClientCredentialsResponse(mContext, profileURL, credentialToken, "POST", postBody);

			if (StringUtils.isNotBlank(response))
			{
				return response;
			}
			else
			{
				throw new Exception(response);
			}

		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_CAT, e.getLocalizedMessage(), null);
			throw new Exception(e);
		}
	}

	@Override
	public String getProducts(String text, String sortCode, List<FacetValue> facetsList, String selectedCategorySearchTag,
			int pageSize, int currentPage) throws Exception
	{
		try
		{
			StringBuffer searchQuery = new StringBuffer();
			String generatedQuery = null;
			generatedQuery = generateQueryTagForProduct(facetsList, selectedCategorySearchTag);

			if (StringUtils.isNotBlank(text))
			{
				searchQuery.append(text);
			}

			if (StringUtils.isNotBlank(generatedQuery))
			{
				searchQuery.append(":");
				if (StringUtils.isNotBlank(sortCode))
				{
					searchQuery.append(sortCode);
				}
				searchQuery.append(":");
				searchQuery.append(generatedQuery);
			}
			else
			{
				searchQuery.append(":");
				searchQuery.append("toprated");
			}
			Bundle parameters = new Bundle();
			parameters.putString("query", searchQuery.toString());
			parameters.putString("pageSize", pageSize + "");
			parameters.putString("currentPage", currentPage + "");
			String productURL = urlForProducts();
			return WebServiceDataProvider.getResponse(mContext, productURL, false, "GET", parameters);

		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_CAT, e.getLocalizedMessage(), null);
			throw new Exception(e);
		}
	}

	@Override
	public String getProductWithCode(String[] options, String productCode) throws Exception
	{
		try
		{
			String optionsString = "";

			if (options != null)
			{
				for (String option : options)
				{
					if (optionsString.length() == 0)
					{
						optionsString = option;
					}
					else
					{
						optionsString = optionsString + "," + option;
					}
				}
			}


			if (optionsString.length() > 0)
			{
				optionsString = "?options=" + optionsString;
			}
			else
			{
				optionsString = "";
			}

			String productURL = urlForProducts() + productCode + optionsString;
			String response = WebServiceDataProvider.getResponse(mContext, productURL, false, "GET", null);

			if (StringUtils.isBlank(response))
			{
				throw new Exception();
			}
			else
			{
				return response;
			}

		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_CAT, e.getLocalizedMessage(), null);
			throw new Exception(e);
		}
	}

	@Override
	public String updateBillingAddress(String firstName, String lastName, String titleCode, String addressLine1,
			String addressLine2, String town, String postCode, String countryISOCode, boolean isDefault, String paymentID)
			throws Exception
	{
		try
		{
			Bundle postBody = new Bundle();
			postBody.putString("firstName", firstName);
			postBody.putString("lastName", lastName);
			postBody.putString("titleCode", titleCode);
			postBody.putString("line1", addressLine1);
			postBody.putString("line2", addressLine2);
			postBody.putString("town", town);
			postBody.putString("postalCode", postCode);
			postBody.putString("country.isocode", countryISOCode);
			postBody.putString("defaultPaymentInfo", Boolean.toString(isDefault));
			String url = urlForCustomer() + "current/paymentinfos/" + paymentID + "/address";
			String response = WebServiceDataProvider.getResponse(mContext, url, true, "POST", postBody);

			if (response.length() > 0)
			{
				throw new Exception(response);
			}
			else
			{
				return response;
			}
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_CAT, e.getLocalizedMessage(), null);
			throw new Exception(e);
		}
	}

	@Override
	public String updatePaymentInfo(String accountHolderName, String cardNumber, String cardType, String expirtyMonth,
			String expiryYear, boolean shouldSave, boolean isDefault, String paymentID) throws Exception
	{
		try
		{
			Bundle postBody = new Bundle();
			postBody.putString("accountHolderName", accountHolderName);
			postBody.putString("cardNumber", cardNumber);
			postBody.putString("cardType", cardType);
			postBody.putString("expiryMonth", expirtyMonth);
			postBody.putString("expiryYear", expiryYear);
			postBody.putString("saved", Boolean.toString(shouldSave));
			postBody.putString("defaultPaymentInfo", Boolean.toString(isDefault));
			String url = urlForCustomer() + "current/paymentinfos/" + paymentID;
			String response = WebServiceDataProvider.getResponse(mContext, url, true, "PUT", postBody);

			if (response.length() > 0)
			{
				throw new Exception(response);
			}
			else
			{
				return response;
			}
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_CAT, e.getLocalizedMessage(), null);
			throw new Exception(e);
		}
	}

	/**
	 * Generate the query facet tags
	 * 
	 * @param query
	 * @return
	 */
	private String generateQueryTagForProduct(List<FacetValue> facetsList, String selectedCategorySearchTag)
	{
		String facetTag = "";
		String categoryTag = "";

		if (facetsList != null && !facetsList.isEmpty())
		{
			for (FacetValue fv : facetsList)
			{
				facetTag = facetTag + ":" + fv.getFacet().getInternalName() + ":" + fv.getValue();
			}

		}

		if (StringUtils.isNotBlank(selectedCategorySearchTag))
		{
			categoryTag = selectedCategorySearchTag;
		}

		return categoryTag + facetTag;
	}

	/**
	 * Get the token associated with the current logged customer
	 * 
	 * @return
	 * @throws Exception
	 */
	private String getCustomerToken() throws Exception
	{
		Bundle postBody = new Bundle();
		postBody.putString("grant_type", "client_credentials");
		postBody.putString("client_id", SDKSettings.client_id);
		postBody.putString("client_secret", SDKSettings.client_secret);
		try
		{
			return WebServiceDataProvider.getResponse(mContext, WebServiceAuthProvider.tokenURL(), false, "POST", postBody);
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_CAT, e.getLocalizedMessage(), null);
			throw new Exception(e);
		}
	}

}
