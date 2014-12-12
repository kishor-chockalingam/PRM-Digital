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
import com.hybris.mobile.model.cart.CartDeliveryAddress;
import com.hybris.mobile.model.cart.CartPaymentInfo;
import com.hybris.mobile.utility.JsonUtils;


public class CustomerTests extends AndroidTestCase
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

	// Your server must have the following set in ycommercewebservices-web-spring.xml
	// <property name="accessTokenValiditySeconds" value="2" />
	public void testRefreshToken() throws Exception
	{
		// login
		login();

		// Wait
		Thread.currentThread();
		Thread.sleep(5000);

		// Get Address (should still work as SDK will refresh token automatically
		webService.getAddresses();
	}

	public void testCustomerPaymentInfo() throws Exception
	{
		login();

		webService.getCart();

		// Create payment
		CartPaymentInfo cartPaymentInfo = JsonUtils.fromJson(webService.createPaymentInfoForCart("name", "0000000000000000",
				"visa", "01", "2016", true, true, "mr", "Fname", "Lname", "address line 1", "address line 2", "postcode", "town",
				"GB"), "paymentInfo", CartPaymentInfo.class);

		// Get a specific paymentInfo
		webService.getPaymentInfo(cartPaymentInfo.getId());

		// Delete a specific paymentInfo
		webService.deletePaymentInfo(cartPaymentInfo.getId());

		// Get paymentInfo again
		webService.getPaymentInfo(cartPaymentInfo.getId());
	}

	public void testChangePassword() throws Exception
	{
		login();

		webService.updatePassword(kPassword, "passwordNew");

		// revert password
		webService.updatePassword("passwordNew", kPassword);
	}

	public void testCustomerProfile() throws Exception
	{
		login();

		// Update profile
		webService.updateProfile("FName", "LName", "mr", "en", "GBP");

		// Get profile
		webService.getProfile();
	}

	public void testCustomerAddresses() throws Exception
	{
		login();

		// Get addresses
		List<CartDeliveryAddress> listAddressesOld = JsonUtils.fromJsonList(webService.getAddresses(), "addresses",
				CartDeliveryAddress.class);

		// Create address
		CartDeliveryAddress address = JsonUtils.fromJson(
				webService.createAddress("Fname", "Lname", "mr", "address line 1", "address line 2", "town", "postcode", "GB"),
				CartDeliveryAddress.class);

		// Update address
		webService.updateAddress("Fname 2", "Lname 2", "mr", "address line 1 2", "address line 2 2", "town 2", "postcode 2", "GB",
				address.getId());

		// Get addresses
		List<CartDeliveryAddress> listAddresses = JsonUtils.fromJsonList(webService.getAddresses(), "addresses",
				CartDeliveryAddress.class);

		assertTrue(listAddresses.size() == listAddressesOld.size() + 1);

		// delete address
		webService.deleteAddress(address.getId());

		// Get addresses
		listAddresses = JsonUtils.fromJsonList(webService.getAddresses(), "addresses", CartDeliveryAddress.class);

		assertTrue(listAddresses.size() == listAddressesOld.size());
	}

	public void testCustomerBillingInfo() throws Exception
	{
		login();

		webService.getCart();

		// Get payments
		List<CartPaymentInfo> listPaymentInfos = JsonUtils.fromJsonList(webService.getPaymentInfos(), "paymentInfos",
				CartPaymentInfo.class);

		// Create payment
		CartPaymentInfo cartPaymentInfo = JsonUtils.fromJson(webService.createPaymentInfoForCart("name", "0000000000000000",
				"visa", "01", "2016", true, true, "mr", "Fname", "Lname", "address line 1", "address line 2", "postcode", "town",
				"GB"), "paymentInfo", CartPaymentInfo.class);

		// Get payments
		listPaymentInfos = JsonUtils.fromJsonList(webService.getPaymentInfos(), "paymentInfos", CartPaymentInfo.class);

		String paymentID = listPaymentInfos.get(0).getId();

		// Get a specific paymentInfo
		cartPaymentInfo = JsonUtils.fromJson(webService.getPaymentInfo(paymentID), CartPaymentInfo.class);

		assertTrue(cartPaymentInfo.getId().equals(cartPaymentInfo.getId()));

		// Delete payment
		webService.deletePaymentInfo(paymentID);
	}

	public void testUpdateCustomerPaymentInfo() throws Exception
	{
		login();

		webService.getCart();

		// Create payment
		CartPaymentInfo cartPaymentInfo = JsonUtils.fromJson(webService.createPaymentInfoForCart("name", "0000000000000000",
				"visa", "01", "2016", true, true, "mr", "Fname", "Lname", "address line 1", "address line 2", "postcode", "town",
				"GB"), "paymentInfo", CartPaymentInfo.class);

		// Get payments
		List<CartPaymentInfo> listPaymentInfos = JsonUtils.fromJsonList(webService.getPaymentInfos(), "paymentInfos",
				CartPaymentInfo.class);

		String paymentID = listPaymentInfos.get(0).getId();

		// Update payment info
		webService.updatePaymentInfo("Name new", "1111111111111111", "visa", "12", "1999", true, true, paymentID);

		// Get a specific paymentInfo
		cartPaymentInfo = JsonUtils.fromJson(webService.getPaymentInfo(paymentID), CartPaymentInfo.class);

		assertTrue(cartPaymentInfo.getCardNumber().equals("************1111")
				&& cartPaymentInfo.getAccountHolderName().equals("Name new"));
	}
}
