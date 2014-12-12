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
package com.hybris.mobile.activity;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Toast;

import com.hybris.mobile.DataConstants;
import com.hybris.mobile.R;
import com.hybris.mobile.adapter.StoreLocatorAdapter;
import com.hybris.mobile.controller.StoreLocatorController;
import com.hybris.mobile.query.QueryStore;
import com.hybris.mobile.utility.GeolocationUtil;
import com.hybris.mobile.utility.IntentHelper;
import com.hybris.mobile.utility.RegexUtil;


public class StoreLocatorActivity extends HybrisListActivity implements SearchView.OnQueryTextListener, Handler.Callback
{

	private StoreLocatorController controller;
	private StoreLocatorAdapter mAdapter;
	private Location mCurrentLocation;
	private float mRadius;
	private boolean isLoading = false;
	private QueryStore mQuery = new QueryStore();
	private MenuItem mSearchView;
	private Handler mHandler;

	/**
	 * View lifecycle methods
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_finder);
		setTitle(R.string.store_title);

		//controller setup
		mHandler = new Handler(this);
		controller = new StoreLocatorController();
		controller.addOutboxHandler(mHandler);

		//adapter setup
		mAdapter = new StoreLocatorAdapter(this, controller.getModel());
		setListAdapter(mAdapter);

		//listview
		getListView().setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position, long l)
			{
				// Disable the click on the loading spin image
				if (!isLoading || (isLoading && position < mAdapter.getCount() - 1))
				{
					Intent intent = null;
					intent = new Intent(StoreLocatorActivity.this, StoreLocatorDetailActivity.class);
					IntentHelper.addObjectForKey(controller.getModel().get(position), DataConstants.INTENT_OBJECT_STORE);
					startActivity(intent);
				}

			}
		});
		getListView().setOnScrollListener(new OnScrollListener()
		{
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState)
			{
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
			{
				if (!isLoading)
				{
					//what is the bottom iten that is visible
					int lastInScreen = firstVisibleItem + visibleItemCount;
					//				//is the bottom item visible & not loading more already ? Load more !
					if (lastInScreen >= totalItemCount - 1)
					{
						fetchMoreStores();
					}
				}
			}
		});
	}


	@Override
	protected void onResume()
	{
		super.onResume();

		handleIntent();

		// Launching the activity from an intent, we try to look if there is a city identifier or just a radius (geolocation)
		if (getIntent().hasExtra(DataConstants.STORE_LOCATOR_RADIUS_VALUE)
				|| (getIntent().hasExtra(DataConstants.STORE_LOCATOR_LAT) && getIntent().hasExtra(DataConstants.STORE_LOCATOR_LONG) && getIntent()
						.hasExtra(DataConstants.STORE_LOCATOR_RADIUS_VALUE)))
		{
			String radiusValue = getIntent().getStringExtra(DataConstants.STORE_LOCATOR_RADIUS_VALUE);
			String latValue = getIntent().getStringExtra(DataConstants.STORE_LOCATOR_LAT);
			String longValue = getIntent().getStringExtra(DataConstants.STORE_LOCATOR_LONG);

			// Is geolocation?
			boolean isGeolocation = StringUtils.isNotEmpty(radiusValue) && StringUtils.isEmpty(latValue)
					&& StringUtils.isEmpty(longValue);

			// Geolocation, we just need the radius
			if (isGeolocation)
			{

				try
				{
					mRadius = Float.valueOf(radiusValue);

					// Getting the user coordinates
					setCurrentUserLocation();
				}
				catch (Exception e)
				{
					// do nothing, error in case of wrong radius format
				}

			}
			// Store location by coordinates, we need the longitude, latitude and the radius
			else if (StringUtils.isNotEmpty(latValue) && StringUtils.isNotEmpty(longValue) && StringUtils.isNotEmpty(radiusValue))
			{

				mRadius = Float.valueOf(radiusValue);
				float latitude = Float.valueOf(latValue);
				float longitude = Float.valueOf(longValue);

				// Creating a new location based on the coordinates
				Location location = new Location("");
				location.reset();
				location.setLatitude(latitude);
				location.setLongitude(longitude);

				fetchStoreFromParticularLocation(location);

			}
		}

	}

	/**
	 * Menu methods
	 */
	@Override
	public boolean onCreateOptionsMenu(final Menu menu)
	{
		MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.store_finder, menu);

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		MenuItem searchViewMenuItem = menu.findItem(R.id.store_search);
		this.mSearchView = searchViewMenuItem;
		SearchView searchView = ((SearchView) searchViewMenuItem.getActionView());

		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		searchView.setIconifiedByDefault(true);
		searchView.setQueryHint(getResources().getString(R.string.store_search_hint));
		searchView.setOnQueryTextListener(this);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.current_location:
				setCurrentUserLocation();
				return true;
			default:
				return false;
		}
	}

	/**
	 * Search view delegate methods
	 */
	@Override
	public boolean onQueryTextChange(String arg0)
	{
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String arg0)
	{
		clearStoreList();

		this.mQuery = new QueryStore();
		this.mCurrentLocation = null;
		mQuery.setQueryText(arg0);

		fetchStores();

		//hide keyboard after submit
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mSearchView.getActionView().getWindowToken(), 0);
		return false;
	}

	/**
	 * Controller delegate methods
	 */
	@Override
	public boolean handleMessage(Message msg)
	{
		switch (msg.what)
		{
			case StoreLocatorController.MESSAGE_MODEL_UPDATED:

				showNoStoreFoundMessage(mQuery.getTotalResults() == 0 ? true : false);

				// Boolean indicating if we should display the distance or not
				mAdapter.setUserLocationKnown(mCurrentLocation != null);

				setIsLoading(false);
				return true;

				// Error with the geolocation module
			case StoreLocatorController.MESSAGE_GEOLOCATION_ERROR:
				showLoadingDialog(false);
				setIsLoading(false);
				showErrorMessage(R.string.error_geolocation_not_able_to_geolocate);
				break;

			// We geolocated the user
			case StoreLocatorController.MESSAGE_GEOLOCATION_SUCCESS:
				showLoadingDialog(false);
				fetchStoreFromParticularLocation((Location) msg.obj);
				break;

		}
		return false;
	}

	/**
	 * Fetch stores from a particular location
	 * 
	 * @param location
	 */
	private void fetchStoreFromParticularLocation(Location location)
	{

		clearStoreList();

		mCurrentLocation = location;
		mQuery = new QueryStore();

		fetchStores();

	}

	/**
	 * Fetch more stores when the user scroll down
	 */
	private void fetchMoreStores()
	{
		if (!isLoading)
		{
			if ((mQuery.getCurrentPage() + 1) < mQuery.getTotalPages())
			{
				mQuery.setCurrentPage(mQuery.getCurrentPage() + 1);
				fetchStores();
			}
		}
	}

	/**
	 * Fetch the stores
	 * 
	 * @param obj
	 */
	private void fetchStores()
	{
		setIsLoading(true);
		showNoStoreFoundMessage(false);
		controller.getStores(mQuery, mCurrentLocation, mRadius, this);
	}

	/**
	 * Clear the store list (model and ui)
	 */
	private void clearStoreList()
	{
		controller.getModel().clear();
		mAdapter.showFooter(true);
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * Display an error message to the user
	 * 
	 * @param message
	 */
	public void showErrorMessage(int resString)
	{
		//		waitView(false);
		showNoStoreFoundMessage(false);
		Toast.makeText(StoreLocatorActivity.this, resString, 40000).show();
	}

	/**
	 * Try to set the current user location
	 */
	private void setCurrentUserLocation()
	{
		// We iconify the user search box
		expandSearchView(false);
		showNoStoreFoundMessage(false);

		if (GeolocationUtil.isLocationActivated())
		{
			showLoadingDialog(true, R.string.geolocate_getting_position);

			// We run the service that tries to geolocate the user
			GeolocationUtil.getLocation(mHandler, StoreLocatorController.MESSAGE_GEOLOCATION_SUCCESS,
					StoreLocatorController.MESSAGE_GEOLOCATION_ERROR);
		}
		else
		{
			GeolocationUtil.showDialogEnableLocationServices(this);
		}

	}

	/**
	 * Expanding or not the search view
	 * 
	 * @param show
	 */
	private void expandSearchView(boolean show)
	{
		if (mSearchView != null)
		{
			if (show)
			{
				mSearchView.expandActionView();
			}
			else
			{
				mSearchView.collapseActionView();
			}
		}

	}

	/**
	 * Show/Hide the message indicating that no store were found for the request
	 * 
	 * @param show
	 */
	private void showNoStoreFoundMessage(boolean show)
	{
		findViewById(R.id.lblStorePlaceholderText).setVisibility(show ? View.VISIBLE : View.GONE);
	}

	/**
	 * Handle calls from other activities/applications
	 */
	private void handleIntent()
	{
		// Call from another application (QR Code) 
		if (getIntent() != null && StringUtils.equals(getIntent().getAction(), Intent.ACTION_VIEW))
		{
			boolean errorData = true;

			// We try to get the radius (geolocation) from the URI
			String radiusValue = RegexUtil.getStoreLocatorGeolocateFromHybrisPattern(getIntent().getDataString());

			// If no radius found, we try to get the coordinates of the store (longitude, latitude, radius)
			if (StringUtils.isEmpty(radiusValue))
			{
				List<String> coordinates = RegexUtil.getStoreLocatorFromHybrisPattern(getIntent().getDataString());

				if (coordinates != null && coordinates.size() == 3)
				{
					errorData = false;
					getIntent().putExtra(DataConstants.STORE_LOCATOR_LONG, coordinates.get(0));
					getIntent().putExtra(DataConstants.STORE_LOCATOR_LAT, coordinates.get(1));
					getIntent().putExtra(DataConstants.STORE_LOCATOR_RADIUS_VALUE, coordinates.get(2));
				}
			}
			else
			{
				errorData = false;
				getIntent().putExtra(DataConstants.STORE_LOCATOR_RADIUS_VALUE, radiusValue);
			}

			// We didn't match anything from the URI, we display an error
			if (errorData)
			{
				Toast.makeText(getApplicationContext(), R.string.store_empty_placeholder, 20000).show();
				finish();
			}

		}
	}

	public void setIsLoading(Boolean isLoading)
	{
		this.isLoading = isLoading;

		// Show / Hide footer
		mAdapter.showFooter(isLoading);

		// Notify the list to be updated
		mAdapter.notifyDataSetChanged();
	}

}
