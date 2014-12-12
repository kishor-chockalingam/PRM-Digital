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
import android.widget.TextView;
import android.widget.Toast;

import com.hybris.mobile.R;
import com.hybris.mobile.adapter.PaymentAdapter;
import com.hybris.mobile.controller.PaymentListController;
import com.hybris.mobile.model.cart.CartPaymentInfo;
import com.hybris.mobile.utility.JsonUtils;


public class PaymentListActivity extends HybrisListActivity implements Handler.Callback
{

	private PaymentListController controller;
	private PaymentAdapter mAdapter;
	private int mSelectedPosition = -1;
	private Boolean mCanSelectPayment = false;
	protected Object mActionMode;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.payment_list);
		setTitle(R.string.payment_list_page_title);

		if (!getIntent().hasExtra("canSelectPayment"))
		{
			// the AccountListActivity started this activity
			((TextView) findViewById(R.id.textView1)).setText(R.string.select_update_payment);
		}
		controller = new PaymentListController(new ArrayList<CartPaymentInfo>(), this);
		controller.addOutboxHandler(new Handler(this));

		mAdapter = new PaymentAdapter(this, controller.getModel());
		setListAdapter(mAdapter);
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		getListView().setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> listView, View view, int position, long l)
			{

				if (mActionMode != null)
				{
					return;
				}
				mSelectedPosition = position;

				if (mCanSelectPayment)
				{
					String paymentID;
					paymentID = controller.getModel().get(mSelectedPosition).getId();
					controller.setPaymentInfoForCart(paymentID);
				}
				else
				{
					// We re-create the action menu only if it has not been created yet
					if (mActionMode == null)
					{
						mActionMode = PaymentListActivity.this.startActionMode(mActionModeCallback);
					}

					return;
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
			if (!mCanSelectPayment)
			{
				menu.removeItem(R.id.menuAdd);
			}
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
					addNewPayment();
					mode.finish();
					return true;
				case R.id.menuEdit:
					editPayment();
					mode.finish();
					return true;
				case R.id.menuDelete:
					deletePayment();
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
		if (mCanSelectPayment)
		{
			MenuInflater inflater = this.getMenuInflater();
			inflater.inflate(R.menu.edit_mode, menu);
			menu.removeItem(R.id.menuDelete);
			menu.removeItem(R.id.menuEdit);
		}
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
				addNewPayment();
				return true;
			default:
				return false;
		}
	}

	private void handleIntent(Intent intent)
	{
		if (intent.hasExtra("canSelectPayment"))
		{
			mCanSelectPayment = intent.getBooleanExtra("canSelectPayment", true);
		}
		else
		{
			mCanSelectPayment = false;
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
			case PaymentListController.MESSAGE_MODEL_UPDATED:
				mAdapter.notifyDataSetChanged();
				return true;

			case PaymentListController.MESSAGE_SET_PAYMENT_INFO_SUCCESS:
				finish();
				return true;

			case PaymentListController.MESSAGE_SET_PAYMENT_INFO_ERROR:
				Toast.makeText(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_LONG).show();
				return true;
		}
		return false;
	}

	private void populateData()
	{
		controller.getPayments();
	}

	public void addNewPayment()
	{
		Intent launchNewIntent = new Intent(this, PaymentDetailActivity.class);
		startActivity(launchNewIntent);
	}

	public void editPayment()
	{

		if (mSelectedPosition < 0)
		{
			mSelectedPosition = mAdapter.getChecked();
		}

		if (mSelectedPosition >= 0)
		{
			Intent launchNewIntent = new Intent(this, PaymentDetailActivity.class);
			CartPaymentInfo payment = controller.getModel().get(mSelectedPosition);
			// TODO
			launchNewIntent.putExtra("value", JsonUtils.toJson(payment));
			startActivity(launchNewIntent);
		}
	}

	public void deletePayment()
	{

		if (mSelectedPosition < 0)
		{
			mSelectedPosition = mAdapter.getChecked();
		}

		if (mSelectedPosition >= 0)
		{
			final String paymentID = controller.getModel().get(mSelectedPosition).getId();
			new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle(R.string.generic_title_confirm)
					.setMessage("Delete this Payment Info?")
					.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							mAdapter.setChecked(-1); //invalidate the selected state
							controller.deletePayment(paymentID);
						}
					}).setNegativeButton(android.R.string.no, null).show();

		}
	}
}
