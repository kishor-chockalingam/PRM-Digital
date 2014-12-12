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


public class Address
{
	private Country country;
	private String formattedAddress;
	private String id;
	private String line1;
	private String line2;
	private String phone;
	private String postalCode;
	private String town;

	public Country getCountry()
	{
		return this.country;
	}

	public void setCountry(Country country)
	{
		this.country = country;
	}

	public String getFormattedAddress()
	{
		return this.formattedAddress;
	}

	public void setFormattedAddress(String formattedAddress)
	{
		this.formattedAddress = formattedAddress;
	}

	public String getId()
	{
		return this.id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getLine1()
	{
		return this.line1;
	}

	public void setLine1(String line1)
	{
		this.line1 = line1;
	}

	public String getLine2()
	{
		return this.line2;
	}

	public void setLine2(String line2)
	{
		this.line2 = line2;
	}

	public String getPhone()
	{
		return this.phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getPostalCode()
	{
		return this.postalCode;
	}

	public void setPostalCode(String postalCode)
	{
		this.postalCode = postalCode;
	}

	public String getTown()
	{
		return this.town;
	}

	public void setTown(String town)
	{
		this.town = town;
	}
}
