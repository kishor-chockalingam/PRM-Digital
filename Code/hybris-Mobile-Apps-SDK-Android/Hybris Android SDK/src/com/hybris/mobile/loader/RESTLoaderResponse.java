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
package com.hybris.mobile.loader;

public class RESTLoaderResponse
{

	public static final int ERROR = 0;
	public static final int SUCCESS = 1;
	private int code;
	private String data;

	private RESTLoaderResponse(int code, String data)
	{
		this.code = code;
		this.data = data;
	}

	public static RESTLoaderResponse getResponse(int code, String data)
	{
		return new RESTLoaderResponse(code, data);
	}

	public int getCode()
	{
		return code;
	}

	public void setCode(int code)
	{
		this.code = code;
	}

	public String getData()
	{
		return data;
	}

	public void setData(String data)
	{
		this.data = data;
	}

}
