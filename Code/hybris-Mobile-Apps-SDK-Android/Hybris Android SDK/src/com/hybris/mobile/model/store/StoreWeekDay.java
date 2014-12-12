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


public class StoreWeekDay
{

	private String weekDay;
	private boolean closed;
	private StoreHours openingTime;
	private StoreHours closingTime;

	public String getWeekDay()
	{
		return weekDay;
	}

	public void setWeekDay(String weekDay)
	{
		this.weekDay = weekDay;
	}

	public boolean isClosed()
	{
		return closed;
	}

	public void setClosed(boolean closed)
	{
		this.closed = closed;
	}

	public StoreHours getOpeningTime()
	{
		return openingTime;
	}

	public void setOpeningTime(StoreHours openingTime)
	{
		this.openingTime = openingTime;
	}

	public StoreHours getClosingTime()
	{
		return closingTime;
	}

	public void setClosingTime(StoreHours closingTime)
	{
		this.closingTime = closingTime;
	}

}
