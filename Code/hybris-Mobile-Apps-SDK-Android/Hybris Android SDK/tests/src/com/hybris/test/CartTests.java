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
package com.hybris.test;

import java.util.List;

import android.test.AndroidTestCase;

import com.hybris.mobile.data.HYOCCInterface;
import com.hybris.mobile.data.WebService;
import com.hybris.mobile.model.cart.Cart;
import com.hybris.mobile.model.cart.CartDeliveryAddress;
import com.hybris.mobile.model.cart.CartDeliveryMode;
import com.hybris.mobile.model.cart.CartOrder;
import com.hybris.mobile.model.cart.CartOrders;
import com.hybris.mobile.model.cart.CartPaymentInfo;
import com.hybris.mobile.query.QueryObjectId;
import com.hybris.mobile.utility.JsonUtils;


public class CartTests extends AndroidTestCase
{

	private static HYOCCInterface webService;
	private static String kLogin = "aeiou@hybris.com";
	private static String kPassword = "password";
	private static boolean setUpIsDone = false;

	public void login() throws Exception
	{
		webService.loginUser(kLogin, kPassword);
	}

	@Override
	protected void setUp() throws Exception
	{
		Thread.sleep(1000);

		if (!setUpIsDone)
		{
			WebService.initWebService(mContext);
			webService = WebService.getWebService();

			//			kLogin = TestUtils.generateRandomEmail();
			//
			//			// Register random customer for the tests
			//			webService.registerCustomer("test", "test", "mr", kLogin, kPassword);

			setUpIsDone = true;
		}

	}

	@Override
	protected void tearDown() throws Exception
	{
	}

	/**
	 * Get the current cart
	 * 
	 * @throws Exception
	 */
	public void testCreateCart() throws Exception
	{
		webService.getCart();
	}

	public void testDeleteCartDeliveryAddress() throws Exception
	{
		login();

		// Create address
		CartDeliveryAddress address = JsonUtils.fromJson(
				webService.createAddress("Fname", "Lname", "mr", "address line 1", "address line 2", "town", "postcode", "GB"),
				CartDeliveryAddress.class);

		// Set delivery address
		QueryObjectId queryObjectId = new QueryObjectId();
		queryObjectId.setObjectId(address.getId());
		webService.setDeliveryAddress(address.getId());

		Cart cart = JsonUtils.fromJson(webService.getCart(), Cart.class);

		assertTrue(cart.getDeliveryAddress().getId().equals(address.getId()));

		webService.deleteCartDeliveryAddress();

		cart = JsonUtils.fromJson(webService.getCart(), Cart.class);

		assertTrue(cart.getDeliveryAddress() == null);

	}

	public void testAddToCart() throws Exception
	{

		// 1. Get the current cart
		String response = webService.getCart();

		Cart cart = JsonUtils.fromJson(response, Cart.class);

		//	2. Add product to cart
		webService.addProductToCart("23355", 1);

		response = webService.getCart();
		Cart newCart = JsonUtils.fromJson(response, Cart.class);

		// Check count has increased
		assertTrue(cart.getTotalUnitCount() + 1 == newCart.getTotalItems());
	}

	public void testUpdateProductInCart() throws Exception
	{

		// 1. Get the current cart
		String response = webService.getCart();

		//	2. Add product to cart
		webService.addProductToCart("23355", 1);

		// 3. Update
		webService.updateProductInCartAtEntry(2, 0);

		response = webService.getCart();
		Cart cart = JsonUtils.fromJson(response, Cart.class);

		// Check count == 2
		assertTrue(cart.getEntries().get(0).getQuantity() == 2);
	}

	public void testCartDeliveryAddress() throws Exception
	{
		login();

		// Create address
		CartDeliveryAddress address = JsonUtils.fromJson(
				webService.createAddress("Fname", "Lname", "mr", "address line 1", "address line 2", "town", "postcode", "GB"),
				CartDeliveryAddress.class);

		// Set delivery address
		QueryObjectId queryObjectId = new QueryObjectId();
		queryObjectId.setObjectId(address.getId());
		webService.setDeliveryAddress(address.getId());

		Cart cart = JsonUtils.fromJson(webService.getCart(), Cart.class);

		assertTrue(cart.getDeliveryAddress().getId().equals(address.getId()));
	}

	public void testCartPaymentInfo() throws Exception
	{
		login();

		// Create payment
		webService.createPaymentInfoForCart("name", "0000000000000000", "visa", "01", "2016", true, true, "mr", "Fname", "Lname",
				"address line 1", "address line 2", "postcode", "town", "GB");
	}

	public void testPlaceOrder() throws Exception
	{
		login();

		// Add product
		webService.addProductToCart("23355", 1);

		// Create address
		CartDeliveryAddress cartDeliveryAddress = JsonUtils.fromJson(
				webService.createAddress("Fname", "Lname", "mr", "address line 1", "address line 2", "town", "postcode", "GB"),
				CartDeliveryAddress.class);

		// Set delivery address
		webService.setDeliveryAddress(cartDeliveryAddress.getId());

		// Get delivery modes
		List<CartDeliveryMode> cartDeliveryModes = JsonUtils.fromJsonList(webService.getCartDeliveryModes(), "deliveryModes",
				CartDeliveryMode.class);

		// Set delivery mode
		webService.setCartDeliveryMode(cartDeliveryModes.get(0).getCode());

		// Create payment
		CartPaymentInfo cartPaymentInfo = JsonUtils.fromJson(webService.createPaymentInfoForCart("name", "0000000000000000",
				"visa", "01", "2016", true, true, "mr", "Fname", "Lname", "address line 1", "address line 2", "postcode", "town",
				"GB"), "paymentInfo", CartPaymentInfo.class);

		webService.setPaymentInfoForCart(cartPaymentInfo.getId());

		// Checkout
		webService.placeOrder();

		// Get all orders
		CartOrders cartOrders = JsonUtils.fromJson(webService.getOrders("CHECKED_VALID", 0, 20), CartOrders.class);

		assertTrue(!cartOrders.getOrders().isEmpty());

		// Get specific order
		CartOrder cartOrder = JsonUtils.fromJson(webService.getOrder(cartOrders.getOrders().get(0).getCode()), CartOrder.class);

		assertTrue(!cartOrder.getCode().isEmpty());
	}

}
