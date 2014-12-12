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
package com.hybris.mobile.model.cart;

import com.hybris.mobile.model.GenericNameCode;


public class CartPaymentInfo
{
	private String id;
	private String accountHolderName;
	private String cardNumber;
	private String expiryYear;
	private String expiryMonth;
	private boolean saved;
	private boolean defaultPaymentInfo;
	private CartDeliveryAddress billingAddress;
	private GenericNameCode cardType;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getAccountHolderName()
	{
		return accountHolderName;
	}

	public void setAccountHolderName(String accountHolderName)
	{
		this.accountHolderName = accountHolderName;
	}

	public String getCardNumber()
	{
		return cardNumber;
	}

	public void setCardNumber(String cardNumber)
	{
		this.cardNumber = cardNumber;
	}

	public GenericNameCode getCardType()
	{
		return cardType;
	}

	public void setCardType(GenericNameCode cardType)
	{
		this.cardType = cardType;
	}

	public String getExpiryYear()
	{
		return expiryYear;
	}

	public void setExpiryYear(String expiryYear)
	{
		this.expiryYear = expiryYear;
	}

	public String getExpiryMonth()
	{
		return expiryMonth;
	}

	public void setExpiryMonth(String expiryMonth)
	{
		this.expiryMonth = expiryMonth;
	}

	public CartDeliveryAddress getBillingAddress()
	{
		return billingAddress;
	}

	public void setBillingAddress(CartDeliveryAddress billingAddress)
	{
		this.billingAddress = billingAddress;
	}

	public boolean isSaved()
	{
		return saved;
	}

	public void setSaved(boolean saved)
	{
		this.saved = saved;
	}

	public boolean isDefaultPaymentInfo()
	{
		return defaultPaymentInfo;
	}

	public void setDefaultPaymentInfo(boolean defaultPaymentInfo)
	{
		this.defaultPaymentInfo = defaultPaymentInfo;
	}

}
