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
package com.hybris.mobile.model.store;

public class StoreAddress
{
	private String formattedAddress;
	private String phone;
	private String town;

	public String getFormattedAddress()
	{
		return formattedAddress;
	}

	public void setFormattedAddress(String formattedAddress)
	{
		this.formattedAddress = formattedAddress;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getTown()
	{
		return town;
	}

	public void setTown(String town)
	{
		this.town = town;
	}



}
