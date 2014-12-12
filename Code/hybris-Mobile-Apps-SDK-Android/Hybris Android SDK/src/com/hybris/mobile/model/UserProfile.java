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
package com.hybris.mobile.model;

public class UserProfile
{

	private String name;
	private String firstName;
	private String lastName;
	private String titleCode;
	private GenericNameCode currency;
	private GenericNameCode language;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
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

	public String getTitleCode()
	{
		return titleCode;
	}

	public void setTitleCode(String titleCode)
	{
		this.titleCode = titleCode;
	}

	public GenericNameCode getCurrency()
	{
		return currency;
	}

	public void setCurrency(GenericNameCode currency)
	{
		this.currency = currency;
	}

	public GenericNameCode getLanguage()
	{
		return language;
	}

	public void setLanguage(GenericNameCode language)
	{
		this.language = language;
	}

}
