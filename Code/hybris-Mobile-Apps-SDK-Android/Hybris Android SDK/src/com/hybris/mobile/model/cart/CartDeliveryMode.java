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


public class CartDeliveryMode
{
	private String code;
	private String name;
	private String description;
	private DeliveryCost deliveryCost;

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public DeliveryCost getDeliveryCost()
	{
		return deliveryCost;
	}

	public void setDeliveryCost(DeliveryCost deliveryCost)
	{
		this.deliveryCost = deliveryCost;
	}

	public class DeliveryCost
	{
		private String formattedValue;

		public String getFormattedValue()
		{
			return formattedValue;
		}

		public void setFormattedValue(String formattedValue)
		{
			this.formattedValue = formattedValue;
		}

	}

}
