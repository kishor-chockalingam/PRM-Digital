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
package com.hybris.mobile;

public enum CatalogEnums
{
	ELECTRONICS("electronics/"), APPARELUK("apparel-uk/");

	private String catalog;

	private CatalogEnums(String catalog)
	{
		this.catalog = catalog;
	}

	public String getCatalog()
	{
		return this.catalog;
	}

}
