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

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.TextView;

import com.hybris.mobile.DataConstants;
import com.hybris.mobile.R;
import com.hybris.mobile.adapter.OrderAdapter;
import com.hybris.mobile.controller.OrderListController;
import com.hybris.mobile.model.cart.CartOrder;


public class OrderListActivity extends HybrisListActivity implements Handler.Callback
{

	private OrderListController controller;
	private OrderAdapter mAdapter;
	private Boolean isLoading = false;
	private TextView mFooterView;
	private Runnable loadMoreListItems = new Runnable()
	{
		@Override
		public void run()
		{
			if (isLoading)
			{
				return;
			}
			if (controller.getCurrentPage() < controller.getTotalPages())
			{
				isLoading = true;
				controller.fetchMore();
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_history_list);
		setTitle(R.string.title_order_history_activity);

		controller = new OrderListController(new ArrayList<CartOrder>(), this);
		controller.addOutboxHandler(new Handler(this));

		mAdapter = new OrderAdapter(this, controller.getModel());
		setListAdapter(mAdapter);
		mFooterView = (TextView) findViewById(R.id.lbl_pagination);

		getListView().setOnScrollListener(new OnScrollListener()
		{
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState)
			{
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
			{
				//what is the bottom iten that is visible
				int lastInScreen = firstVisibleItem + visibleItemCount;
				//is the bottom item visible & not loading more already ? Load more !
				if ((lastInScreen == totalItemCount) && !(isLoading))
				{
					Thread thread = new Thread(null, loadMoreListItems);
					thread.start();
				}
			}
		});
		getListView().setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position, long l)
			{
				Intent intent = new Intent(OrderListActivity.this, OrderDetailActivity.class);
				CartOrder singleOrder = controller.getModel().get(position);
				intent.putExtra(DataConstants.ORDER_ID, singleOrder.getCode());
				startActivity(intent);
			}
		});
		populateData();
	}

	@Override
	public boolean handleMessage(Message msg)
	{
		switch (msg.what)
		{
			case OrderListController.MESSAGE_MODEL_UPDATED:
				//				runOnUiThread(new Runnable()
				//				{
				//					@Override
				//					public void run()
				//					{
				//						waitView(false);
				isLoading = false;
				if (controller.getTotalPages() != 0)
				{
					mFooterView.setText(getString(R.string.generic_n_of_m_results_page, controller.getCurrentPage(),
							controller.getTotalPages()));
				}
				else
				{
					mFooterView.setText(R.string.no_orders);
				}
				mAdapter.notifyDataSetChanged();
				//					}
				//				});
				return true;
		}
		return false;
	}

	private void populateData()
	{
		//		waitView(true);
		controller.getOrders();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		return true;
	}

	public void showBasketDetails(View view)
	{
		Intent intent = new Intent(this, BasketDetailActivity.class);
		intent.putExtra("basketDetailsCart", "");
		startActivity(intent);
	}

}
