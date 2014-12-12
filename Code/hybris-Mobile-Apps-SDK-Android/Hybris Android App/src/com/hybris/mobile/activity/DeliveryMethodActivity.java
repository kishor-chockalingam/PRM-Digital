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

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.hybris.mobile.R;
import com.hybris.mobile.adapter.DeliveryMethodAdapter;
import com.hybris.mobile.controller.DeliveryMethodController;
import com.hybris.mobile.model.cart.CartDeliveryMode;


public class DeliveryMethodActivity extends HybrisListActivity implements OnItemClickListener, Handler.Callback
{

	private DeliveryMethodController controller;
	private DeliveryMethodAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setTitle(R.string.delivery_method_page_title);
		setContentView(R.layout.delivery_list);

		controller = new DeliveryMethodController(new ArrayList<CartDeliveryMode>());
		controller.addOutboxHandler(new Handler(this));

		mAdapter = new DeliveryMethodAdapter(this, controller.getModel());
		setListAdapter(mAdapter);

		getListView().setSelector(R.drawable.list_selector);
		getListView().setOnItemClickListener(this);

		populateData();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		return false;
	}

	private void populateData()
	{
		controller.getCartDeliveryModes(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
	{
		CartDeliveryMode mode = controller.getModel().get(position);
		controller.setCartDeliveryMode(this, mode.getCode());

		//		WebService.getWebService(Hybris.getAppContext()).setCartDeliveryMode(mode.getCode(), new CartDeliveryModeResultListener()
		//		{
		//
		//			@Override
		//			public void onException(final Exception e)
		//			{
		//				runOnUiThread(new Runnable()
		//				{
		//					@Override
		//					public void run()
		//					{
		//						waitView(false);
		//						Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
		//					}
		//				});
		//			}
		//
		//			@Override
		//			public void onComplete(CartDeliveryMode response)
		//			{
		//				waitView(false);
		//				finish();
		//			}
		//		});

	}

	@Override
	public boolean handleMessage(Message msg)
	{
		switch (msg.what)
		{
			case DeliveryMethodController.MESSAGE_MODEL_UPDATED:
				//				runOnUiThread(new Runnable()
				//				{
				//					@Override
				//					public void run()
				//					{
				//						waitView(false);
				mAdapter.notifyDataSetChanged();
				//					}
				//				});
				return true;

			case DeliveryMethodController.MESSAGE_SET_CART_DELIVERY_MODE_SUCCESS:
				finish();
				return true;

			case DeliveryMethodController.MESSAGE_SET_CART_DELIVERY_MODE_ERROR:
				Toast.makeText(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_LONG).show();
				return true;

		}
		return false;
	}

}
