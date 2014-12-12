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
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;

import com.hybris.mobile.Hybris;
import com.hybris.mobile.WebserviceMethodEnums;
import com.hybris.mobile.loader.RESTLoader;
import com.hybris.mobile.loader.RESTLoaderObserver;
import com.hybris.mobile.loader.RESTLoaderResponse;
import com.hybris.mobile.model.Category;
import com.hybris.mobile.model.DidYouMean;
import com.hybris.mobile.model.Facet;
import com.hybris.mobile.model.GenericValue;
import com.hybris.mobile.model.Sort;
import com.hybris.mobile.model.product.Product;
import com.hybris.mobile.model.product.ProductsList;
import com.hybris.mobile.query.QueryProducts;
import com.hybris.mobile.query.QueryText;
import com.hybris.mobile.utility.JsonUtils;


public class ObjectListController extends Controller implements RESTLoaderObserver
{

	private List<String> mSearchSuggestions = new ArrayList<String>();
	private List<Object> mModel = new ArrayList<Object>();
	private List<Sort> mSorts;
	private List<Facet> mFacets;

	private String mCurrentQueryString = "";
	private QueryProducts mQuery;

	public static final int MESSAGE_MODEL_UPDATED = 1;
	public static final int MESSAGE_SEARCH_SUGGESTIONS = 2;

	public List<Object> getModel()
	{
		return mModel;
	}

	public List<String> getSearchSuggestions()
	{
		return mSearchSuggestions;
	}

	public void getSpellingSuggestions(Activity context, String queryText)
	{
		mCurrentQueryString = queryText;
		QueryText query = new QueryText();
		query.setQueryText(mCurrentQueryString);
		RESTLoader.execute(context, WebserviceMethodEnums.METHOD_SPELLING_SUGGESTIONS, query, this, true, false);
	}

	public void getCategories(Category category)
	{
		if (category != null && category.getChildCategories() != null)
		{
			mModel.clear();
			mModel.addAll(category.getChildCategories());
		}
		notifyOutboxHandlers(ObjectListController.MESSAGE_MODEL_UPDATED, 0, 0, null);
	}

	public void getProducts(QueryProducts query, Activity context, boolean showLoadingDialog)
	{
		mQuery = query;
		RESTLoader.execute(context, WebserviceMethodEnums.METHOD_GET_PRODUCTS, query, this, true, showLoadingDialog);
	}

	@Override
	public void onReceiveResult(RESTLoaderResponse restLoaderResponse, WebserviceMethodEnums webserviceEnumMethod)
	{
		if (restLoaderResponse.getCode() == RESTLoaderResponse.SUCCESS)
		{
			String jsonResult = restLoaderResponse.getData();

			switch (webserviceEnumMethod)
			{

				case METHOD_GET_PRODUCTS:

					ProductsList productsList = JsonUtils.fromJson(jsonResult, ProductsList.class);

					if (productsList != null)
					{

						// For pagination, after the first page we only want additional products 
						if (mQuery.getCurrentPage() == 0)
						{
							mModel.clear();

							// Subcategories
							getCategories(mQuery.getSelectedCategory());

							// Get Did You Mean
							if (!mQuery.isFromDidYouMean() && productsList.getSpellingSuggestion() != null
									&& StringUtils.isNotBlank(productsList.getSpellingSuggestion().getSuggestion()))
							{
								mModel.add(new DidYouMean(productsList.getSpellingSuggestion().getSuggestion()));
							}

						}

						if (productsList.getProducts() != null)
						{
							// Initializing products
							for (Product product : productsList.getProducts())
							{
								product.populate();
							}

							// Pagination
							if (productsList.getPagination() != null)
							{
								mQuery.setTotalResults(productsList.getPagination().getTotalResults());
								mQuery.setTotalPages(productsList.getPagination().getTotalPages());
								mQuery.setPageSize(productsList.getPagination().getPageSize());
								mQuery.setCurrentPage(productsList.getPagination().getCurrentPage());
							}

							// Select facet values so their facets can be added back again
							productsList.populateFacets(mQuery.getSelectedFacetValues());

							mSorts = productsList.getSorts();
							mFacets = productsList.getFacets();

							mModel.addAll(productsList.getProducts());
						}

					}

					notifyOutboxHandlers(ObjectListController.MESSAGE_MODEL_UPDATED, 0, 0, null);

				case METHOD_SPELLING_SUGGESTIONS:
					List<GenericValue> listGenericValue = JsonUtils.fromJsonList(jsonResult, "suggestions", GenericValue.class);

					mSearchSuggestions.clear();

					// Add previous searches that pass filter 
					mSearchSuggestions.addAll(filteredAndSortedPreviousSearches());

					if (listGenericValue != null)
					{
						for (GenericValue genericValue : listGenericValue)
						{
							if (!mSearchSuggestions.contains(genericValue.getValue()))
							{
								mSearchSuggestions.add(genericValue.getValue());
							}
						}
					}

					notifyOutboxHandlers(ObjectListController.MESSAGE_SEARCH_SUGGESTIONS, 0, 0, null);

				default:
					break;

			}

		}
	}

	public List<Sort> getSorts()
	{
		return mSorts;
	}

	public List<Facet> getFacets()
	{
		return mFacets;
	}

	public void addAllFilteredAndSortedPreviousSearches()
	{
		mSearchSuggestions.addAll(filteredAndSortedPreviousSearches());
	}

	/**
	 * Filters and sorts the previous searches Filtering done against the current search text.
	 * 
	 * @return list of previous searches to display
	 */
	private List<String> filteredAndSortedPreviousSearches()
	{
		Set<String> unfilteredStrings = Hybris.getPreviousSearches();

		List<String> filteredStrings = new ArrayList<String>();
		for (String s : unfilteredStrings)
		{
			if (s != null && s.startsWith(mCurrentQueryString))
			{
				filteredStrings.add(s);
			}
		}
		Collections.sort(filteredStrings);
		return filteredStrings;
	}

}
