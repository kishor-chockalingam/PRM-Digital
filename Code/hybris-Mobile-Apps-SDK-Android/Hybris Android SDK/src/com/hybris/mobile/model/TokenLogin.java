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

public class TokenLogin
{

	private String access_token;
	private String refresh_token;
	private String expires_in;

	public String getAccess_token()
	{
		return access_token;
	}

	public void setAccess_token(String access_token)
	{
		this.access_token = access_token;
	}

	public String getRefresh_token()
	{
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token)
	{
		this.refresh_token = refresh_token;
	}

	public String getExpires_in()
	{
		return expires_in;
	}

	public void setExpires_in(String expires_in)
	{
		this.expires_in = expires_in;
	}

}
