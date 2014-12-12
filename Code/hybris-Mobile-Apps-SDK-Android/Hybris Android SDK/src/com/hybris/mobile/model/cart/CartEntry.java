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

import com.hybris.mobile.model.Price;
import com.hybris.mobile.model.product.Product;


/**
 * Represents a single entry in a cart
 * 
 */
public class CartEntry implements CartItem
{

	private Integer entryNumber;
	private Integer quantity;
	private boolean updateable;
	private long cartId;
	private Long productId;
	private Product product;
	private Price basePrice;
	private Price totalPrice;

	public Price getBasePrice()
	{
		return basePrice;
	}

	public void setBasePrice(Price basePrice)
	{
		this.basePrice = basePrice;
	}

	public Price getTotalPrice()
	{
		return totalPrice;
	}

	public void setTotalPrice(Price totalPrice)
	{
		this.totalPrice = totalPrice;
	}

	public CartEntry()
	{
	}

	public CartEntry(Integer entryNumber, Integer quantity, boolean updateable, long cartId, Long productId)
	{
		this.entryNumber = entryNumber;
		this.quantity = quantity;
		this.updateable = updateable;
		this.cartId = cartId;
		this.productId = productId;
	}

	public Integer getEntryNumber()
	{
		return entryNumber;
	}

	public void setEntryNumber(Integer entryNumber)
	{
		this.entryNumber = entryNumber;
	}

	public Integer getQuantity()
	{
		return quantity;
	}

	public void setQuantity(Integer quantity)
	{
		this.quantity = quantity;
	}

	public boolean getUpdateable()
	{
		return updateable;
	}

	public void setUpdateable(boolean updateable)
	{
		this.updateable = updateable;
	}

	public long getCartId()
	{
		return cartId;
	}

	public void setCartId(long cartId)
	{
		this.cartId = cartId;
	}

	public Long getProductId()
	{
		return productId;
	}

	public void setProductId(Long productId)
	{
		this.productId = productId;
	}

	public Product getProduct()
	{
		return product;
	}

	public void setProduct(Product product)
	{
		this.product = product;
	}

}
