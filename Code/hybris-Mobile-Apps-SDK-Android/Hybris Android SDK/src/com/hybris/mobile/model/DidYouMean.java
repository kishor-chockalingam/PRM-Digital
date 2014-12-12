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

public class DidYouMean
{

	private String mName;

	public DidYouMean(String spellingSuggestion)
	{
		mName = spellingSuggestion;
	}

	public String getName()
	{
		return mName;
	}

	public void setName(String name)
	{
		this.mName = name;
	}

}
