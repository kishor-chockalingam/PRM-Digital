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

import android.app.AlertDialog;
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
import android.widget.Toast;

import com.hybris.mobile.R;
import com.hybris.mobile.adapter.AddressAdapter;
import com.hybris.mobile.controller.AddressListController;
import com.hybris.mobile.model.cart.CartDeliveryAddress;
import com.hybris.mobile.utility.JsonUtils;


public class AddressListActivity extends HybrisListActivity implements Handler.Callback
{

	private AddressListController controller;
	private AddressAdapter mAdapter;
	private int mSelectedPosition = -1;
	private Boolean mCanSelectAddress = false;

	protected Object mActionMode;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.address_list);
		setTitle(R.string.address_book_page_title);

		controller = new AddressListController(new ArrayList<CartDeliveryAddress>(), this);
		controller.addOutboxHandler(new Handler(this));

		mAdapter = new AddressAdapter(this, controller.getModel());
		setListAdapter(mAdapter);
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		getListView().setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> listView, View view, int position, long l)
			{

				mSelectedPosition = position;

				if (mCanSelectAddress)
				{
					String addressId = controller.getModel().get(mSelectedPosition).getId();
					controller.setDeliveryAddress(addressId);
				}
				else
				{

					// We re-create the action menu only if it has not been created yet
					if (mActionMode == null)
					{
						mActionMode = AddressListActivity.this.startActionMode(mActionModeCallback);
					}

				}
			}
		});
		handleIntent(getIntent());
	}

	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback()
	{

		public boolean onCreateActionMode(ActionMode mode, Menu menu)
		{
			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.edit_mode, menu);
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
				case R.id.menuAdd:
					addNewAddress();
					mode.finish();
					return true;
				case R.id.menuEdit:
					editAddress();
					mode.finish();
					return true;
				case R.id.menuDelete:
					deleteAddress();
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
			mAdapter.notifyDataSetChanged();
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.add_item, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				Intent intent = new Intent(this, ObjectListActivity.class);
				intent.putExtra(ObjectListActivity.INTENT_EXTRA_RESTART, true);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
				return true;
			case R.id.menuAdd:
				addNewAddress();
				return true;
			default:
				return false;
		}
	}

	private void handleIntent(Intent intent)
	{
		if (intent.hasExtra("canSelectAddress"))
		{
			mCanSelectAddress = intent.getBooleanExtra("canSelectAddress", false);
		}
		if (intent.hasExtra("selectedAddressID"))
		{
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		populateData();
	}

	@Override
	public boolean handleMessage(Message msg)
	{
		switch (msg.what)
		{
			case AddressListController.MESSAGE_MODEL_UPDATED:
				mAdapter.notifyDataSetChanged();
				return true;

			case AddressListController.MESSAGE_SET_DELIVERY_ADDRESS_ERROR:
				Toast.makeText(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_LONG).show();
				return true;

			case AddressListController.MESSAGE_SET_DELIVERY_ADDRESS_SUCCESS:
				finish();
				return true;
		}
		return false;
	}

	private void populateData()
	{
		controller.getAddresses();
	}

	public void addNewAddress()
	{
		Intent launchNewIntent = new Intent(this, AddressDetailActivity.class);
		launchNewIntent.putExtra("title", getString(R.string.add_address));
		startActivity(launchNewIntent);
	}

	public void editAddress()
	{

		if (mSelectedPosition < 0)
		{
			mSelectedPosition = mAdapter.getChecked();
		}

		if (mSelectedPosition >= 0)
		{
			Intent launchNewIntent = new Intent(this, AddressDetailActivity.class);
			CartDeliveryAddress address = controller.getModel().get(mSelectedPosition);
			launchNewIntent.putExtra("title", getString(R.string.edit_address));
			// TODO
			launchNewIntent.putExtra("value", JsonUtils.toJson(address));
			startActivity(launchNewIntent);
		}
	}

	public void deleteAddress()
	{

		if (mSelectedPosition < 0)
		{
			mSelectedPosition = mAdapter.getChecked();
		}

		if (mSelectedPosition >= 0)
		{
			final String addressID = controller.getModel().get(mSelectedPosition).getId();
			new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle(R.string.generic_title_confirm)
					.setMessage("Delete this address?").setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							mAdapter.setChecked(-1); //invalidate the selected state
							controller.deleteAddress(addressID);
						}
					}).setNegativeButton(android.R.string.no, null).show();
		}
	}
}
