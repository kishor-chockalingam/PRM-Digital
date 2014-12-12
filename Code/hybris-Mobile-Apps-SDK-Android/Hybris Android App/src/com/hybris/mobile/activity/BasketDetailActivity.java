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
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;

import com.hybris.mobile.R;
import com.hybris.mobile.adapter.BasketAdapter;
import com.hybris.mobile.controller.CartController;
import com.hybris.mobile.model.cart.Cart;
import com.hybris.mobile.model.cart.CartEntry;
import com.hybris.mobile.model.cart.CartItem;
import com.hybris.mobile.utility.JsonUtils;


public class BasketDetailActivity extends HybrisListActivity implements Handler.Callback
{

	private List<CartEntry> mProducts;
	private BasketAdapter mAdapter;
	private CartController controller;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		//setContentView(R.layout.cart_list);
		setTitle(R.string.basket_details_page_title);

		mProducts = new ArrayList<CartEntry>();
		mAdapter = new BasketAdapter(this, mProducts);
		setListAdapter(mAdapter);

		controller = new CartController(new Cart(), this);
		controller.addOutboxHandler(new Handler(this));

		handleIntent(getIntent());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		return false;
	}

	private void handleIntent(Intent intent)
	{

		if (intent.hasExtra("basketDetailsOrder"))
		{
			String details = intent.getStringExtra("basketDetailsOrder");

			mProducts.addAll(JsonUtils.fromJsonList(details, CartEntry.class));

			for (CartEntry cartEntry : mProducts)
			{
				cartEntry.getProduct().populate();
			}

			mAdapter.notifyDataSetChanged();
		}
		else if (intent.hasExtra("basketDetailsCart"))
		{
			//			waitView(true);
			controller.loadCart();
		}

	}

	@Override
	public boolean handleMessage(Message msg)
	{
		//		waitView(false);
		switch (msg.what)
		{
			case CartController.MESSAGE_CART_UPDATED:
				updateUI();
				return true;
		}
		return false;
	}

	private void updateUI()
	{
		for (int i = 0; i < controller.getModel().size(); i++)
		{
			CartItem item = controller.getModel().get(i);
			if (item instanceof CartEntry)
			{
				mProducts.add((CartEntry) item);
			}
		}
		mAdapter.notifyDataSetChanged();
	}
}
