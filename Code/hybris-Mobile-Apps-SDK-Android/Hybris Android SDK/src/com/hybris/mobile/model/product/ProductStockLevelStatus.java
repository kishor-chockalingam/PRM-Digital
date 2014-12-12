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

public class ProductStockLevelStatus
{

	public static final String CODE_OUT_OF_STOCK = "Out Of Stock";

	private String statusCode;
	private String codeLowerCase;
	private String code;

	public String getStatusCode()
	{
		return statusCode;
	}

	public void setStatusCode(String statusCode)
	{
		this.statusCode = statusCode;
	}

	public String getCodeLowerCase()
	{
		return codeLowerCase;
	}

	public void setCodeLowerCase(String codeLowerCase)
	{
		this.codeLowerCase = codeLowerCase;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

}
