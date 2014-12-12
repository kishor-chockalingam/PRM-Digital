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

import com.hybris.mobile.model.Price;


public class ProductOptionItem
{
	private String name;
	private String value;
	private String qualifier;
	private String imageUrl;
	private String imageFormat;
	private ProductOptionStockLevelStatus stockLevelStatus;
	private List<ProductOptionVariant> variantOptionQualifiers;
	private Integer stockLevel;
	private Price priceData;
	private String code = "";
	private String url = "";
	private boolean selectedOption;

	public List<ProductOptionVariant> getVariantOptionQualifiers()
	{
		return variantOptionQualifiers;
	}

	public void setVariantOptionQualifiers(List<ProductOptionVariant> variantOptionQualifiers)
	{
		this.variantOptionQualifiers = variantOptionQualifiers;
	}

	public Price getPriceData()
	{
		return priceData;
	}

	public void setPriceData(Price priceData)
	{
		this.priceData = priceData;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public String getQualifier()
	{
		return qualifier;
	}

	public void setQualifier(String qualifier)
	{
		this.qualifier = qualifier;
	}

	public String getImageUrl()
	{
		return imageUrl;
	}

	public void setImageUrl(String imageUrl)
	{
		this.imageUrl = imageUrl;
	}

	public String getImageFormat()
	{
		return imageFormat;
	}

	public void setImageFormat(String imageFormat)
	{
		this.imageFormat = imageFormat;
	}

	public ProductOptionStockLevelStatus getStockLevelStatus()
	{
		return stockLevelStatus;
	}

	public Integer getStockLevel()
	{
		return stockLevel;
	}

	public void setStockLevel(Integer stockLevel)
	{
		this.stockLevel = stockLevel;
	}

	public void setStockLevelStatus(ProductOptionStockLevelStatus stockLevelStatus)
	{
		this.stockLevelStatus = stockLevelStatus;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public boolean isSelectedOption()
	{
		return selectedOption;
	}

	public void setSelectedOption(boolean selectedOption)
	{
		this.selectedOption = selectedOption;
	}

}
