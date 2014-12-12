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

import org.apache.commons.lang3.StringUtils;



public class ProductStock
{

	private int stockLevel;
	private ProductStockLevelStatus stockLevelStatus;


	public int getStockLevel()
	{
		return stockLevel;
	}

	public void setStockLevel(int stockLevel)
	{
		this.stockLevel = stockLevel;
	}

	public ProductStockLevelStatus getStockLevelStatus()
	{
		return stockLevelStatus;
	}

	public void setStockLevelStatus(ProductStockLevelStatus stockLevelStatus)
	{
		this.stockLevelStatus = stockLevelStatus;
	}

	public String getStockLevelTxt()
	{

		if (stockLevelStatus != null)
		{
			if (StringUtils.equalsIgnoreCase(stockLevelStatus.getCodeLowerCase(), "inStock"))
			{
				return "In Stock";
			}
			else if (StringUtils.equalsIgnoreCase(stockLevelStatus.getCodeLowerCase(), "lowStock"))
			{
				return "Low Stock";
			}
			else if (StringUtils.equalsIgnoreCase(stockLevelStatus.getCodeLowerCase(), "outOfStock"))
			{
				return "Out Of Stock";
			}
			else
			{
				return String.format("Unknown: %s", stockLevelStatus);
			}
		}

		return "";

	}

}
