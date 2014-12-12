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

import com.hybris.mobile.model.GenericName;


public class CartDeliveryAddress
{
	private String id;
	private String title;
	private String firstName;
	private String lastName;
	private String line1;
	private String line2;
	private String town;
	private String postalCode;
	private GenericName country;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public String getLine1()
	{
		return line1;
	}

	public void setLine1(String line1)
	{
		this.line1 = line1;
	}

	public String getLine2()
	{
		return line2;
	}

	public void setLine2(String line2)
	{
		this.line2 = line2;
	}

	public String getTown()
	{
		return town;
	}

	public void setTown(String town)
	{
		this.town = town;
	}

	public String getPostalCode()
	{
		return postalCode;
	}

	public void setPostalCode(String postalCode)
	{
		this.postalCode = postalCode;
	}

	public GenericName getCountry()
	{
		return country;
	}

	public void setCountry(GenericName country)
	{
		this.country = country;
	}

}
