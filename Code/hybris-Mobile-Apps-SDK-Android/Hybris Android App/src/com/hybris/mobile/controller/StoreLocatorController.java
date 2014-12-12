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
package com.hybris.mobile.controller;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.location.Location;

import com.hybris.mobile.WebserviceMethodEnums;
import com.hybris.mobile.loader.RESTLoader;
import com.hybris.mobile.loader.RESTLoaderObserver;
import com.hybris.mobile.loader.RESTLoaderResponse;
import com.hybris.mobile.model.store.Store;
import com.hybris.mobile.model.store.StoresList;
import com.hybris.mobile.query.QueryStore;
import com.hybris.mobile.utility.GeolocationUtil;
import com.hybris.mobile.utility.JsonUtils;


public class StoreLocatorController extends Controller implements RESTLoaderObserver
{

	private List<Store> model;
	private QueryStore mQuery;

	public static final int MESSAGE_MODEL_UPDATED = 1;
	public static final int MESSAGE_GEOLOCATION_SUCCESS = 2;
	public static final int MESSAGE_GEOLOCATION_ERROR = 3;

	public StoreLocatorController()
	{
		this.model = new ArrayList<Store>();
	}

	public List<Store> getModel()
	{
		return model;
	}

	public void getStores(QueryStore query, Location location, float radius, Activity context)
	{
		mQuery = query;

		// Location specified
		if (location != null)
		{

			float radiusToUse = GeolocationUtil.getDefaultRadius();

			// If we have a specified radius for searching, we use it 
			if (radius > 0)
			{
				radiusToUse = radius;
			}

			query.setRadius(radiusToUse);
			query.setAccuracy(location.getAccuracy());
			query.setLongitude(location.getLongitude());
			query.setLatitude(location.getLatitude());

			RESTLoader.execute(context, WebserviceMethodEnums.METHOD_STORES_FROM_LOCATION, query, this, true, false);
		}
		else if (query != null)
		{
			RESTLoader.execute(context, WebserviceMethodEnums.METHOD_STORES, query, this, true, false);
		}

	}

	@Override
	public void onReceiveResult(RESTLoaderResponse restLoaderResponse, WebserviceMethodEnums webserviceEnumMethod)
	{
		if (restLoaderResponse.getCode() == RESTLoaderResponse.SUCCESS)
		{
			String jsonResult = restLoaderResponse.getData();
			StoresList storesList = JsonUtils.fromJson(jsonResult, StoresList.class);

			if (storesList.getPagination() != null)
			{
				mQuery.setTotalPages(storesList.getPagination().getTotalPages());
				mQuery.setTotalResults(storesList.getPagination().getTotalResults());
			}

			model.addAll(storesList.getStores());

			notifyOutboxHandlers(ObjectListController.MESSAGE_MODEL_UPDATED, 0, 0, null);

		}
		else if (restLoaderResponse.getCode() == RESTLoaderResponse.ERROR)
		{
			notifyOutboxHandlers(ObjectListController.MESSAGE_MODEL_UPDATED, 0, 0, null);
		}
	}

}
