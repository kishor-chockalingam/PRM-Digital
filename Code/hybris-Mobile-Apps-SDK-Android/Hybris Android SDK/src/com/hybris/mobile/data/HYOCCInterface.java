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

import com.hybris.mobile.model.FacetValue;


/**
 * This protocol describes the API calls to communicate with the OCC layer of Hybris. Use these methods to interact with
 * the stores, products, customer's informations, etc.
 * 
 */
public interface HYOCCInterface
{

	/**
	 * Get supported languages.
	 * 
	 * Relates to: {site}/languages
	 * 
	 * @return
	 * @throws Exception
	 */
	public String languages() throws Exception;


	/**
	 * Get supported currencies.
	 * 
	 * Relates to:{site}/currencies
	 * 
	 * @return
	 * @throws Exception
	 */
	public String currencies() throws Exception;


	/**
	 * Get support country codes.
	 * 
	 * Relates to:{site}/deliverycountries
	 * 
	 * @return
	 * @throws Exception
	 */
	public String deliveryCountries() throws Exception;


	/**
	 * Get supported card types.
	 * 
	 * Relates to:{site}/cardtypes
	 * 
	 * @return
	 * @throws Exception
	 */
	public String cardTypes() throws Exception;


	/**
	 * Get supported titles.
	 * 
	 * Relates to: {site}/titles
	 * 
	 * @return
	 * @throws Exception
	 */
	public String titles() throws Exception;

	/**
	 * Get the spelling suggestions for the provided query text
	 * 
	 * @param text
	 * @return
	 * @throws Exception
	 */
	public String spellingSuggestions(String text) throws Exception;

	/**
	 * Get the stores at a specific location.
	 * 
	 * Relates to: {site}/stores/
	 * 
	 * @param longitude
	 * @param latitude
	 * @param radius
	 * @param accuracy
	 * @param currentPage
	 * @return
	 * @throws Exception
	 */
	public String storesFromLocation(double longitude, double latitude, float radius, float accuracy, int currentPage)
			throws Exception;

	/**
	 * Get the stores using a free text search
	 * 
	 * Relates to: {site}/stores/
	 * 
	 * @param text
	 * @param currentPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public String stores(String text, int currentPage, int pageSize) throws Exception;

	/**
	 * Get the session cart
	 * 
	 * Relates to: {site}/cart
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getCart() throws Exception;

	/**
	 * Add item to cart
	 * 
	 * Relates to: {site}/cart/entry
	 * 
	 * @param productCode
	 * @param quantity
	 * @return
	 * @throws Exception
	 */
	public String addProductToCart(String productCode, int quantity) throws Exception;

	/**
	 * Update the quantity of an item in the cart
	 * 
	 * Relates to:{site}/cart/entry/{entryNumber}
	 * 
	 * @param quantity
	 * @param carEntryNumber
	 * @return
	 * @throws Exception
	 */
	public String updateProductInCartAtEntry(int quantity, int carEntryNumber) throws Exception;

	/**
	 * Delete a cart product entry
	 * 
	 * @param carEntryNumber
	 * @return
	 * @throws Exception
	 */
	public String deleteProductInCartAtEntry(String carEntryNumber) throws Exception;


	/**
	 * Set the delivery address
	 * 
	 * @param addressId
	 * @return
	 * @throws Exception
	 */
	public String setDeliveryAddress(String addressId) throws Exception;


	/**
	 * Delete cart delivery address
	 * 
	 * @return
	 * @throws Exception
	 */
	public String deleteCartDeliveryAddress() throws Exception;


	/**
	 * Returns all delivery modes supported for the current base store and cart delivery address
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getCartDeliveryModes() throws Exception;


	/**
	 * Set delivery mode for the current cart
	 * 
	 * @param deliveryModeId
	 * @return
	 * @throws Exception
	 */
	public String setCartDeliveryMode(String deliveryModeId) throws Exception;


	/**
	 * Removes delivery mode from the current cart
	 * 
	 * @return
	 * @throws Exception
	 */
	public String deleteCartDeliveryMode() throws Exception;


	/**
	 * Create a payment card
	 * 
	 * @param accountHolderName
	 * @param cardNumber
	 * @param cardType
	 * @param expiryMonth
	 * @param expiryYear
	 * @param shouldSave
	 * @param isDefault
	 * @param titleCode
	 * @param firstName
	 * @param lastName
	 * @param address1
	 * @param address2
	 * @param postCode
	 * @param town
	 * @param countryISOCode
	 * @return
	 * @throws Exception
	 */
	public String createPaymentInfoForCart(String accountHolderName, String cardNumber, String cardType, String expiryMonth,
			String expiryYear, boolean shouldSave, boolean isDefault, String titleCode, String firstName, String lastName,
			String address1, String address2, String postCode, String town, String countryISOCode) throws Exception;


	/**
	 * Get all the payments informations for the customer
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getPaymentInfos() throws Exception;


	/**
	 * Get a particular payment information
	 * 
	 * @param paymentId
	 * @return
	 * @throws Exception
	 */
	public String getPaymentInfo(String paymentId) throws Exception;


	/**
	 * Add the credit card payment to the current user's cart
	 * 
	 * @param paymentId
	 * @return
	 * @throws Exception
	 */
	public String setPaymentInfoForCart(String paymentId) throws Exception;


	/**
	 * Delete payment information
	 * 
	 * @param paymentId
	 * @return
	 * @throws Exception
	 */
	public String deletePaymentInfo(String paymentId) throws Exception;


	/**
	 * Authorizes a credit card payment with the CCV security code
	 * 
	 * @param securityCode
	 * @throws Exception
	 */
	public String authorizeCreditCard(String securityCode) throws Exception;


	/**
	 * Places an order based on the session cart
	 * 
	 * @return
	 * @throws Exception
	 */
	public String placeOrder() throws Exception;

	/**
	 * Returns order history data for all orders placed by the current user for the current base store. Response contains
	 * a pageable orders search result.
	 * 
	 * @param status
	 * @param currentPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public String getOrders(String status, int currentPage, int pageSize) throws Exception;


	/**
	 * Returns specific order details
	 * 
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	public String getOrder(String orderId) throws Exception;


	/**
	 * Log in the user
	 * 
	 * @param login
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public String loginUser(String login, String password) throws Exception;


	/**
	 * Logs out the current user and clears the session
	 * 
	 * @return
	 * @throws Exception
	 */
	public String logoutUser() throws Exception;


	/**
	 * Register a new customer
	 * 
	 * @param firstName
	 * @param lastName
	 * @param titleCode
	 * @param login
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public String registerCustomer(String firstName, String lastName, String titleCode, String login, String password)
			throws Exception;


	/**
	 * Creates a new address for the loggedin customer
	 * 
	 * @param firstName
	 * @param lastName
	 * @param titleCode
	 * @param addressLine1
	 * @param addressLine2
	 * @param town
	 * @param postCode
	 * @param countryISOCode
	 * @return
	 * @throws Exception
	 */
	public String createAddress(String firstName, String lastName, String titleCode, String addressLine1, String addressLine2,
			String town, String postCode, String countryISOCode) throws Exception;


	/**
	 * Set address as customer's default address
	 * 
	 * @param addressId
	 * @return
	 * @throws Exception
	 */
	public String setDefaultAddress(String addressId) throws Exception;


	/**
	 * Get customer's addresses
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getAddresses() throws Exception;


	/**
	 * Update address
	 * 
	 * @param firstName
	 * @param lastName
	 * @param titleCode
	 * @param addressLine1
	 * @param addressLine2
	 * @param town
	 * @param postCode
	 * @param countryISOCode
	 * @param addressID
	 * @return
	 * @throws Exception
	 */
	public String updateAddress(String firstName, String lastName, String titleCode, String addressLine1, String addressLine2,
			String town, String postCode, String countryISOCode, String addressID) throws Exception;


	/**
	 * Delete customer address
	 * 
	 * @param addressId
	 * @return
	 * @throws Exception
	 */
	public String deleteAddress(String addressId) throws Exception;


	/**
	 * Update customer's profile
	 * 
	 * @param firstName
	 * @param lastName
	 * @param titleCode
	 * @param language
	 * @param currency
	 * @return
	 * @throws Exception
	 */
	public String updateProfile(String firstName, String lastName, String titleCode, String language, String currency)
			throws Exception;


	/**
	 * Get a customer profile
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getProfile() throws Exception;


	/**
	 * Updates customer login id (which must be an email address)
	 * 
	 * @param newLogin
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public String updateLogin(String newLogin, String password) throws Exception;


	/**
	 * Updates customer password
	 * 
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 * @throws Exception
	 */
	public String updatePassword(String oldPassword, String newPassword) throws Exception;


	/**
	 * Request password via email
	 * 
	 * @param login
	 * @return
	 * @throws Exception
	 */
	public String requestPassword(String login) throws Exception;


	/**
	 * Gets a list of products
	 * 
	 * @param text
	 * @param sortCode
	 * @param facetsList
	 * @param selectedCategorySearchTag
	 * @param pageSize
	 * @param currentPage
	 * @return
	 * @throws Exception
	 */
	public String getProducts(String text, String sortCode, List<FacetValue> facetsList, String selectedCategorySearchTag,
			int pageSize, int currentPage) throws Exception;


	/**
	 * Returns details of a single product
	 * 
	 * @param options
	 * @param productCode
	 * @return
	 * @throws Exception
	 */
	public String getProductWithCode(String[] options, String productCode) throws Exception;


	/**
	 * Update a customer payment/billing address
	 * 
	 * @param firstName
	 * @param lastName
	 * @param titleCode
	 * @param addressLine1
	 * @param addressLine2
	 * @param town
	 * @param postCode
	 * @param countryISOCode
	 * @param isDefault
	 * @param paymentID
	 * @return
	 * @throws Exception
	 */
	public String updateBillingAddress(String firstName, String lastName, String titleCode, String addressLine1,
			String addressLine2, String town, String postCode, String countryISOCode, boolean isDefault, String paymentID)
			throws Exception;


	/**
	 * Update a customer payment information
	 * 
	 * @param accountHolderName
	 * @param cardNumber
	 * @param cardType
	 * @param expirtyMonth
	 * @param expiryYear
	 * @param shouldSave
	 * @param isDefault
	 * @param paymentID
	 * @return
	 * @throws Exception
	 */
	public String updatePaymentInfo(String accountHolderName, String cardNumber, String cardType, String expirtyMonth,
			String expiryYear, boolean shouldSave, boolean isDefault, String paymentID) throws Exception;
}
