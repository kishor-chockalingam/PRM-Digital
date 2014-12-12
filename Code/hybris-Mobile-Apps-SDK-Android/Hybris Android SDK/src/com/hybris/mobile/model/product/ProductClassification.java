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

import com.hybris.mobile.model.GenericValue;


public class ProductClassification
{

	private String name;
	private List<Feature> features;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public List<Feature> getFeatures()
	{
		return features;
	}

	public void setFeatures(List<Feature> features)
	{
		this.features = features;
	}

	public class Feature
	{
		private String name;
		private List<GenericValue> featureValues;

		public String getName()
		{
			return name;
		}

		public void setName(String name)
		{
			this.name = name;
		}

		public List<GenericValue> getFeatureValues()
		{
			return featureValues;
		}

		public void setFeatureValues(List<GenericValue> featureValues)
		{
			this.featureValues = featureValues;
		}

	}

}
