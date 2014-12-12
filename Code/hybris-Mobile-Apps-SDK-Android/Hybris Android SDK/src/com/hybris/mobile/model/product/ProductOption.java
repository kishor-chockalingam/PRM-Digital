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


public class ProductOption
{
	private List<ProductOptionItem> options;
	private ProductOptionItem selected;

	public List<ProductOptionItem> getOptions()
	{
		return options;
	}

	public void setOptions(List<ProductOptionItem> options)
	{
		this.options = options;
	}

	public ProductOptionItem getSelected()
	{
		return selected;
	}

	public void setSelected(ProductOptionItem selected)
	{
		this.selected = selected;
	}

}
