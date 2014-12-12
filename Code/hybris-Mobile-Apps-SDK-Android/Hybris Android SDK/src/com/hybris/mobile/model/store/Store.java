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

import java.util.List;

import com.hybris.mobile.model.Geopoint;


public class Store
{
	private StoreAddress address;
	private List<StoreFeature> features;
	private String formattedDistance;
	private Geopoint geoPoint;
	private String name;
	private StoreOpeningHours openingHours;
	private String url;

	public StoreAddress getAddress()
	{
		return address;
	}

	public void setAddress(StoreAddress address)
	{
		this.address = address;
	}

	public List<StoreFeature> getFeatures()
	{
		return features;
	}

	public void setFeatures(List<StoreFeature> features)
	{
		this.features = features;
	}

	public String getFormattedDistance()
	{
		return formattedDistance;
	}

	public void setFormattedDistance(String formattedDistance)
	{
		this.formattedDistance = formattedDistance;
	}

	public Geopoint getGeoPoint()
	{
		return geoPoint;
	}

	public void setGeoPoint(Geopoint geoPoint)
	{
		this.geoPoint = geoPoint;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public StoreOpeningHours getOpeningHours()
	{
		return openingHours;
	}

	public void setOpeningHours(StoreOpeningHours openingHours)
	{
		this.openingHours = openingHours;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

}
