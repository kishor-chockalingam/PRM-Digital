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

public class ProductOptionVariant
{
	private String name;
	private String value;
	private String qualifier;
	private ProductImage image;

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

	public ProductImage getImage()
	{
		return image;
	}

	public void setImage(ProductImage image)
	{
		this.image = image;
	}

}
