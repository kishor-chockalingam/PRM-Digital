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

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.hybris.mobile.DataConstants;
import com.hybris.mobile.ExternalConstants;
import com.hybris.mobile.Hybris;
import com.hybris.mobile.R;
import com.hybris.mobile.adapter.ObjectAdapter;
import com.hybris.mobile.adapter.SortOptionsAdapter;
import com.hybris.mobile.controller.ObjectListController;
import com.hybris.mobile.data.CategoryManager;
import com.hybris.mobile.fragment.FilterDialogFragment;
import com.hybris.mobile.fragment.FilterDialogFragment.FilterDialogListener;
import com.hybris.mobile.model.Category;
import com.hybris.mobile.model.DidYouMean;
import com.hybris.mobile.model.Sort;
import com.hybris.mobile.model.product.Product;
import com.hybris.mobile.query.QueryProducts;


public class ObjectListActivity extends HybrisListActivity implements SearchView.OnQueryTextListener, Handler.Callback,
		FilterDialogListener
{

	private static final String INTENT_EXTRA_CATEGORY = "EXTRA_CATEGORY";
	private static final String INTENT_EXTRA_QUERY = "INTENT_EXTRA_QUERY";
	private static final String INTENT_EXTRA_SEARCH = "INTENT_EXTRA_SEARCH";
	private static final String INTENT_EXTRA_FROM_DID_YOU_MEAN = "INTENT_EXTRA_FROM_DID_YOU_MEAN";
	public static final String INTENT_EXTRA_RESTART = "INTENT_EXTRA_RESTART";

	/**
	 * Controller for getting data form the web service
	 */
	private ObjectListController controller;

	/**
	 * Local reference to the main list view adapter Got from the ObjectListController
	 */
	private ObjectAdapter mAdapter;

	/**
	 * Loading flag
	 */
	private boolean isLoading = true;

	/**
	 * Current search string
	 */
	private String mCurrentQueryString = "";

	/**
	 * Query object
	 */
	private QueryProducts mQuery;

	/**
	 * Search textview
	 */
	private TextView mTextView;

	/**
	 * Search suggestions list view
	 */
	private ListView mSearchList;

	/**
	 * Search suggestions timer
	 */
	private long mStartTime = 0;


	/**
	 * Dialof for the splash screen
	 */
	protected Dialog mSplashDialog;


	/**
	 * Search suggestion hanfler
	 */
	private Handler mSearchHandler = new Handler();

	private boolean restart = false;

	/**
	 * Runnable for updating search suggestions Fetches suggestions from the server and previous searches from local
	 * storage and concatanates.
	 */
	private Runnable mRunSearchSuggestions = new Runnable()
	{
		@Override
		public void run()
		{
			if (System.currentTimeMillis() - mStartTime > ExternalConstants.SEARCH_SUGGESTION_DELAY)
			{

				controller.getSpellingSuggestions(ObjectListActivity.this, mCurrentQueryString);
			}
			else
			{
				mSearchHandler.postDelayed(this, 100);
			}
		}
	};

	@Override
	public void onResume()
	{
		super.onResume();

		// HockeyApp integration for crash reports & updates
		checkForCrashes();

		if (isLastActivity())
		{
			// Prevent pagination in home view
			setIsLoading(!ExternalConstants.PRODUCTS_IN_SHOP_FRONT);

			// Hide header
			View header = (View) findViewById(R.id.header_view);
			header.setVisibility(View.GONE);
		}

		populateData();
	}


	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		// Main view
		getActionBar().hide();
		setContentView(R.layout.object_list);

		if (getIntent().hasExtra(ObjectListActivity.INTENT_EXTRA_RESTART))
		{
			restart = getIntent().getExtras().getBoolean(ObjectListActivity.INTENT_EXTRA_RESTART);
			getIntent().getExtras().remove(ObjectListActivity.INTENT_EXTRA_RESTART);
		}
		final ImageView splashImage = (ImageView) findViewById(R.id.imageViewSplash);
		splashImage.setVisibility(View.GONE);
		if (isLastActivity() && !restart)
		{

			// HockeyApp integration for crash reports & updates
			checkForUpdates();

			splashImage.setVisibility(View.VISIBLE);
			splashImage.bringToFront();
			new Handler().postDelayed(new Runnable()
			{
				@Override
				public void run()
				{
					getActionBar().show();
					splashImage.setVisibility(View.GONE);
				}
			}, 2000);
		}
		else
		{
			getActionBar().show();
			splashImage.setVisibility(View.GONE);
		}

		// Banner
		if (!isLastActivity() && !restart)
		{
			findViewById(R.id.imageViewBanner).setVisibility(View.GONE);
		}

		mTextView = (TextView) findViewById(R.id.header_view);
		controller = new ObjectListController();
		controller.addOutboxHandler(new Handler(this));
		mQuery = new QueryProducts();
		mAdapter = new ObjectAdapter(this, controller.getModel());
		setListAdapter(mAdapter);

		// onClick listener
		getListView().setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position, long l)
			{

				if (!isLoading || (isLoading && position < mAdapter.getCount() - 1))
				{

					Intent intent = null;

					// Product
					if (mAdapter.getItem(position) instanceof Product)
					{

						intent = new Intent(ObjectListActivity.this, ProductDetailActivity.class);

						intent.putExtra(DataConstants.PRODUCT_CODE, ((Product) getListAdapter().getItem(position)).getCode());
						startActivity(intent);
					}
					// Category
					else if (mAdapter.getItem(position) instanceof Category)
					{
						intent = new Intent(ObjectListActivity.this, ObjectListActivity.class);
						mQuery = new QueryProducts();
						Category selectedCategory = (Category) mAdapter.getItem(position);
						mQuery.setSelectedCategory(selectedCategory);

						intent.putExtra(INTENT_EXTRA_CATEGORY, true);
						intent.putExtra(INTENT_EXTRA_QUERY, mQuery);
						startActivity(intent);
					}
					// Did You Mean?
					else if (mAdapter.getItem(position) instanceof DidYouMean)
					{
						DidYouMean didYouMean = (DidYouMean) mAdapter.getItem(position);
						Hybris.addPreviousSearch(didYouMean.getName());

						intent = new Intent(ObjectListActivity.this, ObjectListActivity.class);
						intent.putExtra(INTENT_EXTRA_SEARCH, didYouMean.getName());
						intent.putExtra(INTENT_EXTRA_FROM_DID_YOU_MEAN, true);
						startActivity(intent);
					}
				}
			}

		});


		if (!isCategoryPage())
		{
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
							fetchMoreProducts();
						}
					}
				}
			});
		}

		// Create the search suggestions list
		controller.addAllFilteredAndSortedPreviousSearches();
		mSearchList = (ListView) findViewById(R.id.searchList);
		mSearchList.setAdapter(new SortOptionsAdapter(this, controller.getSearchSuggestions()));
		mSearchList.setVisibility(View.GONE);
		mSearchList.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long arg)
			{
				String searchString = (String) mSearchList.getItemAtPosition(position);
				onQueryTextSubmit(searchString);
			}
		});

	}

	private boolean isCategoryPage()
	{

		Category root = mQuery.getSelectedCategory();

		if (isLastActivity() || restart)
		{
			return !ExternalConstants.PRODUCTS_IN_SHOP_FRONT;
		}
		else if (!ExternalConstants.PRODUCTS_IN_CATEGORY_VIEW && root != null && root.getChildCategories().size() > 0)
		{
			return true;
		}

		return false;
	}

	/**
	 * Populate the data depending on different cases
	 */
	private void populateData()
	{
		Category root = null;
		boolean continueLoading = true;

		// Coming from an Intent
		if (getIntent() != null)
		{
			if (getIntent().hasExtra(INTENT_EXTRA_CATEGORY))
			{
				this.mQuery = getIntent().getParcelableExtra(INTENT_EXTRA_QUERY);

				if (mQuery != null && mQuery.getSelectedCategory() != null)
				{
					this.setTitle(this.mQuery.getSelectedCategory().getName());
				}

			}
			else if (getIntent().hasExtra(INTENT_EXTRA_SEARCH))
			{
				String strQuery = getIntent().getStringExtra(INTENT_EXTRA_SEARCH);
				QueryProducts query = new QueryProducts();
				if (strQuery.length() > 0)
				{
					query.setQueryText(strQuery);
					this.mQuery = query;
					this.mCurrentQueryString = strQuery;

					if (getIntent().getBooleanExtra(INTENT_EXTRA_FROM_DID_YOU_MEAN, false))
					{
						mQuery.setFromDidYouMean(true);
					}

					controller.getProducts(mQuery, this, false);
					continueLoading = false;
				}
				else
				{
					query.setQueryText("");
					this.mQuery = query;
					this.mCurrentQueryString = "";
				}

			}
		}

		if (continueLoading)
		{
			if (isLastActivity() || restart)
			{
				// Prevent pagination in home view
				setIsLoading(!ExternalConstants.PRODUCTS_IN_SHOP_FRONT);

				// Hide header
				View header = (View) findViewById(R.id.header_view);
				header.setVisibility(View.GONE);

				root = CategoryManager.getRootCategory();
				if (root != null)
				{
					mQuery.setSelectedCategory(root);
				}

				if (ExternalConstants.PRODUCTS_IN_SHOP_FRONT)
				{
					controller.getProducts(mQuery, this, false);
				}
				else
				{
					controller.getCategories(mQuery.getSelectedCategory());
				}
			}
			else
			{
				root = mQuery.getSelectedCategory();

				// Subcategory but we don' t show the products
				if (!ExternalConstants.PRODUCTS_IN_CATEGORY_VIEW && root != null && root.getChildCategories() != null
						&& !root.getChildCategories().isEmpty())
				{
					// Hide header
					View header = (View) findViewById(R.id.header_view);
					header.setVisibility(View.GONE);

					// Hide footer
					this.mAdapter.showFooter(false);

					controller.getCategories(mQuery.getSelectedCategory());
				}
				else
				{
					controller.getProducts(mQuery, this, false);
				}
			}
		}

	}

	/**
	 * Handle Action Menu interactions
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		boolean handled = super.onOptionsItemSelected(item);
		if (!handled)
		{
			switch (item.getItemId())
			{
				case R.id.search:
					mSearchList.setVisibility(View.VISIBLE);
					mSearchList.bringToFront();
					onSearchRequested();
					return true;
				default:
					return false;
			}
		}
		return handled;
	}

	/**
	 * Set up the action menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);

		// Set up the search box and table
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		searchView.setIconifiedByDefault(false);
		searchView.setQueryHint(getResources().getString(R.string.search_products_hint));

		// Workaround to set the color of the searchView text
		LinearLayout linearLayout1 = (LinearLayout) searchView.getChildAt(0);
		LinearLayout linearLayout2 = (LinearLayout) linearLayout1.getChildAt(2);
		LinearLayout linearLayout3 = (LinearLayout) linearLayout2.getChildAt(1);
		AutoCompleteTextView autoComplete = (AutoCompleteTextView) linearLayout3.getChildAt(0);
		autoComplete.setTextColor(getResources().getColor(R.color.editTextDark));

		searchView.setOnQueryTextListener(this);
		searchView.setOnQueryTextFocusChangeListener(new OnFocusChangeListener()
		{

			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				if (hasFocus)
				{
					mSearchList.setVisibility(View.VISIBLE);
				}
				else
				{
					mSearchList.setVisibility(View.GONE);
				}
			}
		});

		// Remove the refine buttin on the homepage
		if (isLastActivity() || restart)
		{
			menu.removeItem(R.id.refine);
		}
		else
		{
			// Set the color of the refine button
			if (mQuery.getSelectedFacetValues().isEmpty())
			{
				menu.findItem(R.id.refine).setIcon(R.drawable.ic_menu_filter);
			}
			else
			{
				menu.findItem(R.id.refine).setIcon(R.drawable.ic_menu_filter_active);
			}
		}

		return true;
	}

	@Override
	public boolean onQueryTextChange(String query)
	{
		mCurrentQueryString = query;

		if (query.length() == 0)
		{
			controller.getSearchSuggestions().clear();
			controller.addAllFilteredAndSortedPreviousSearches();
		}

		// Restart the handler
		mSearchHandler.removeCallbacks(mRunSearchSuggestions);
		mStartTime = System.currentTimeMillis();
		mSearchHandler.postDelayed(mRunSearchSuggestions, 2000);

		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String strQuery)
	{
		Hybris.addPreviousSearch(strQuery);
		Intent intent = new Intent(this, ObjectListActivity.class);
		intent.putExtra(INTENT_EXTRA_SEARCH, strQuery);

		startActivity(intent);
		return false;
	}

	@Override
	public boolean handleMessage(Message msg)
	{
		boolean returnValue = false;

		switch (msg.what)
		{
			case ObjectListController.MESSAGE_MODEL_UPDATED:

				// Build the header message
				String headerString = "";
				if (mQuery.getTotalResults() == 0)
				{
					headerString = getString(R.string.no_results);
				}
				else if (mQuery.getTotalResults() == 1)
				{
					headerString = getString(R.string.one_result);
				}
				// if all loaded... (-1 for the footer)
				else if ((mQuery.getTotalResults() == mAdapter.getCount() - 1) || (mQuery.getPageSize() >= mQuery.getTotalResults()))
				{
					headerString = getString(R.string.n_results, mQuery.getTotalResults());
				}
				else
				{
					String currentlyShown = "";
					if (((mQuery.getCurrentPage() + 1) * mQuery.getPageSize()) > mQuery.getTotalResults())
					{
						currentlyShown = getString(R.string.n_results, mQuery.getTotalResults());
					}
					else
					{
						currentlyShown = getString(R.string.n_of_m_results,
								String.valueOf(((mQuery.getCurrentPage() + 1) * mQuery.getPageSize())), mQuery.getTotalResults());
					}
					headerString = currentlyShown;
				}

				setIsLoading(false);
				mTextView.setText(headerString);

				returnValue = true;

				break;

			case ObjectListController.MESSAGE_SEARCH_SUGGESTIONS:
				((BaseAdapter) mSearchList.getAdapter()).notifyDataSetChanged();
				break;
		}

		return returnValue;
	}

	private void fetchMoreProducts()
	{
		if (!isLoading)
		{

			if ((mQuery.getCurrentPage() + 1) < mQuery.getTotalPages())
			{
				mQuery.setCurrentPage(mQuery.getCurrentPage() + 1);

				setIsLoading(true);
				controller.getProducts(mQuery, this, false);
			}

		}
	}

	/**
	 * Show the filter dialog
	 * 
	 * @param originating
	 *           view
	 */
	public void showFilter(View view)
	{

		if (controller.getFacets() != null)
		{
			// Create and show the picker
			DialogFragment fragment = FilterDialogFragment.newInstance(this, controller.getSorts(), controller.getFacets(),
					mQuery.getSelectedFacetValues(), mQuery.getSelectedSort());
			fragment.show(getFragmentManager(), "filter_fragment");
		}
	}

	/*
	 * FilterDialogListener
	 */

	/**
	 * Callback for filter dialog with change
	 */
	@Override
	public void onFiltersHaveChanged(Sort selectedSort)
	{
		mQuery.setSelectedSort(selectedSort);
		controller.getModel().clear();
		mQuery.setCurrentPage(0);

		invalidateOptionsMenu();
		controller.getProducts(mQuery, this, true);
	}

	/**
	 * Callback for filter dialog cancelled
	 */
	@Override
	public void onFiltersCancelled()
	{
	}

	/**
	 * HockeyApp integration
	 */
	private void checkForCrashes()
	{
		CrashManager.register(this, getString(R.string.hockeyapp_key));
	}

	/**
	 * HockeyApp integration
	 */
	private void checkForUpdates()
	{
		// Remove this for store builds!
		UpdateManager.register(this, getString(R.string.hockeyapp_key));
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
