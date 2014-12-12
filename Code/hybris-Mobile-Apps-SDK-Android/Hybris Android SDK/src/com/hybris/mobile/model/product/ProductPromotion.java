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
package com.hybris.mobile.model.product;

import java.util.List;


public class ProductPromotion
{

	private String description;
	private List<String> firedMessages;
	private List<String> couldFireMessages;

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public List<String> getFiredMessages()
	{
		return firedMessages;
	}

	public void setFiredMessages(List<String> firedMessages)
	{
		this.firedMessages = firedMessages;
	}

	public List<String> getCouldFireMessages()
	{
		return couldFireMessages;
	}

	public void setCouldFireMessages(List<String> couldFireMessages)
	{
		this.couldFireMessages = couldFireMessages;
	}



}
