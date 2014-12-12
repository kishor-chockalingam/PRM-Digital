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

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.hybris.mobile.Hybris;
import com.hybris.mobile.R;
import com.hybris.mobile.adapter.CartAdapter;
import com.hybris.mobile.controller.CartController;
import com.hybris.mobile.fragment.QuantityDialogFragment;
import com.hybris.mobile.fragment.QuantityDialogFragment.QuantityDialogListener;
import com.hybris.mobile.model.cart.Cart;
import com.hybris.mobile.model.cart.CartEntry;


public class CartListActivity extends HybrisListActivity implements Handler.Callback, QuantityDialogListener
{

	private CartController controller;
	private CartAdapter mAdapter;
	private int mCartEntryNumber;
	private int mSelectedPosition = -1;
	private ActionMode mActionMode;

	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback()
	{

		public boolean onCreateActionMode(ActionMode mode, Menu menu)
		{
			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.cart_menu, menu);
			return true;
		}

		public boolean onPrepareActionMode(ActionMode mode, Menu menu)
		{
			return false;
		}

		public boolean onActionItemClicked(ActionMode mode, MenuItem item)
		{
			switch (item.getItemId())
			{
				case R.id.menuEdit:
					showQuantityPicker();
					mode.finish();
					return true;
				case R.id.menuDelete:
					deleteEntry();
					mode.finish();
					return true;
				default:
					return false;
			}
		}

		public void onDestroyActionMode(ActionMode mode)
		{
			mActionMode = null;
			mSelectedPosition = -1;
			getListView().setItemChecked(getListView().getCheckedItemPosition(), false);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.cart_list);
		setTitle(R.string.cart_list_page_title);

		controller = new CartController(new Cart(), this);
		controller.addOutboxHandler(new Handler(this));

		mAdapter = new CartAdapter(this, controller.getModel());
		setListAdapter(mAdapter);

		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		getListView().setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> listView, View view, int position, long l)
			{

				mSelectedPosition = position;

				// We re-create the action menu only if it has not been created yet
				if (mActionMode == null)
				{
					mActionMode = CartListActivity.this.startActionMode(mActionModeCallback);
				}

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		return true;
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		populateData();
	}

	private void populateData()
	{
		showLoadingDialog(true);
		controller.loadCart();
	}

	@Override
	public boolean handleMessage(Message msg)
	{
		switch (msg.what)
		{
			case CartController.MESSAGE_SHOW_QUANTITY_DIALOG:
				CartEntry ce = (CartEntry) controller.getModel().get(mCartEntryNumber);
				DialogFragment fragment = QuantityDialogFragment.newInstance(ce.getQuantity(), (Integer) msg.obj,
						CartListActivity.this);
				fragment.show(getFragmentManager(), "quantity_fragment");
				return true;

			case CartController.MESSAGE_CART_UPDATED:

				TextView qty = (TextView) findViewById(R.id.lbl_cart_quantity);
				TextView totalPrice = (TextView) findViewById(R.id.lbl_cart_total_price);
				try
				{
					qty.setText(controller.getCart().getTotalUnitCount() + " items in total");
					totalPrice.setText(controller.getCart().getSubTotal().getFormattedValue());
					if (controller.getCart().getTotalUnitCount() == 0)
					{
						qty.setText(R.string.no_items_in_cart);
						findViewById(R.id.containerButton).setVisibility(View.GONE);
					}
				}
				catch (Exception exp)
				{
					qty.setText(R.string.no_items_in_cart);
					findViewById(R.id.btnCheckout).setVisibility(4);
				}
				mAdapter.notifyDataSetChanged();
				invalidateOptionsMenu();

				showLoadingDialog(false);

				return true;
		}
		return false;
	}

	public void deleteEntry()
	{
		final int selectedPosition = mSelectedPosition;

		new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle(R.string.generic_title_confirm)
				.setMessage("Delete this entry?").setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						showLoadingDialog(true);
						controller.deleteEntry(selectedPosition);
					}
				}).setNegativeButton(android.R.string.no, null).show();
	}

	public void showQuantityPicker()
	{
		mCartEntryNumber = mSelectedPosition;
		CartEntry ce = (CartEntry) controller.getModel().get(mCartEntryNumber);
		controller.loadQuantity(ce.getProduct().getCode());
	}

	@Override
	public void onFinishDialog(int quantity)
	{
		showLoadingDialog(true);
		controller.updateCart(mCartEntryNumber, quantity);
	}

	public void showCheckoutActivity(View view)
	{
		if ((controller.getModel().size() > 0) && Hybris.isUserLoggedIn())
		{
			Intent launchNewIntent = new Intent(this, CheckoutActivity.class);
			startActivity(launchNewIntent);
		}
		else if (!Hybris.isUserLoggedIn())
		{
			Intent launchNewIntent = new Intent(this, LoginActivity.class);
			startActivity(launchNewIntent);
		}
	}

}
