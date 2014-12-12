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

import java.util.List;

import com.hybris.mobile.model.Price;


public class CartOrder
{

	private String code;
	private String placed;
	private String statusDisplay;
	private String created;
	private CartDeliveryAddress deliveryAddress;
	private CartDeliveryMode deliveryMode;
	private CartPaymentInfo paymentInfo;
	private Price subTotal;
	private Price totalTax;
	private Price deliveryCost;
	private Price totalPrice;
	private List<CartEntry> entries;

	public List<CartEntry> getEntries()
	{
		return entries;
	}

	public void setEntries(List<CartEntry> entries)
	{
		this.entries = entries;
	}

	public String getCreated()
	{
		return created;
	}

	public void setCreated(String created)
	{
		this.created = created;
	}

	public CartDeliveryAddress getDeliveryAddress()
	{
		return deliveryAddress;
	}

	public void setDeliveryAddress(CartDeliveryAddress deliveryAddress)
	{
		this.deliveryAddress = deliveryAddress;
	}

	public CartDeliveryMode getDeliveryMode()
	{
		return deliveryMode;
	}

	public void setDeliveryMode(CartDeliveryMode deliveryMode)
	{
		this.deliveryMode = deliveryMode;
	}

	public CartPaymentInfo getPaymentInfo()
	{
		return paymentInfo;
	}

	public void setPaymentInfo(CartPaymentInfo paymentInfo)
	{
		this.paymentInfo = paymentInfo;
	}

	public Price getSubTotal()
	{
		return subTotal;
	}

	public void setSubTotal(Price subTotal)
	{
		this.subTotal = subTotal;
	}

	public Price getTotalTax()
	{
		return totalTax;
	}

	public void setTotalTax(Price totalTax)
	{
		this.totalTax = totalTax;
	}

	public Price getDeliveryCost()
	{
		return deliveryCost;
	}

	public void setDeliveryCost(Price deliveryCost)
	{
		this.deliveryCost = deliveryCost;
	}

	public Price getTotalPrice()
	{
		return totalPrice;
	}

	public void setTotalPrice(Price totalPrice)
	{
		this.totalPrice = totalPrice;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getPlaced()
	{
		return placed;
	}

	public void setPlaced(String placed)
	{
		this.placed = placed;
	}

	public String getStatusDisplay()
	{
		return statusDisplay;
	}

	public void setStatusDisplay(String statusDisplay)
	{
		this.statusDisplay = statusDisplay;
	}

}
