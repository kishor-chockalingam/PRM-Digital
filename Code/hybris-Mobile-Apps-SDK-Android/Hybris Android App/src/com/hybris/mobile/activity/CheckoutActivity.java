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

import org.apache.commons.lang3.StringUtils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hybris.mobile.R;
import com.hybris.mobile.controller.CartController;
import com.hybris.mobile.model.cart.Cart;
import com.hybris.mobile.model.cart.CartDeliveryAddress;
import com.hybris.mobile.model.cart.CartDeliveryMode;
import com.hybris.mobile.model.cart.CartPaymentInfo;
import com.hybris.mobile.utility.JsonUtils;


public class CheckoutActivity extends HybrisActivity implements Handler.Callback
{

	private CartController controller;
	private int colorStepDone;
	private int colorStepCurrent;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		getActionBar().setHomeButtonEnabled(true);
		setContentView(R.layout.checkout_list);
		setTitle(R.string.checkout_page_title);

		controller = new CartController(new Cart(), this);
		controller.addOutboxHandler(new Handler(this));

		colorStepDone = getResources().getColor(R.color.textHighlightedDark);
		colorStepCurrent = getResources().getColor(R.color.textHighlighted);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		populateData();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		return false;
	}

	private void populateData()
	{
		showLoadingDialog(true);
		controller.loadCart();
	}

	@Override
	public boolean handleMessage(final Message msg)
	{
		//		waitView(false);
		switch (msg.what)
		{
			case CartController.MESSAGE_CART_UPDATED:
				updateUI();
				showLoadingDialog(false);
				break;

			case CartController.MESSAGE_ORDER_PLACED_ERROR:
				Toast.makeText(getApplicationContext(), ((Exception) msg.obj).getLocalizedMessage(), Toast.LENGTH_LONG).show();
				break;

			case CartController.MESSAGE_ORDER_PLACED_RESPONSE:
				Intent intent = new Intent(CheckoutActivity.this, OrderDetailActivity.class);
				// TODO
				intent.putExtra("orderDetails", JsonUtils.toJson(msg.obj));
				startActivity(intent);

				// Dismiss this activity
				finish();
				return true;
		}
		return false;
	}

	private void updateUI()
	{
		CartDeliveryAddress address = controller.getCart().getDeliveryAddress();

		//delivery address section
		String strList = "";
		TextView addressView = (TextView) findViewById(R.id.lbl_addressView);
		TextView deliveryView = (TextView) findViewById(R.id.lbl_deliveryMethod);
		TextView paymentView = (TextView) findViewById(R.id.lbl_paymentDetails);
		if (address != null)
		{
			String firstName = address.getFirstName();
			String lastName = address.getLastName();
			String title = address.getTitle();

			strList = "<b>" + strList + title + " " + firstName + " " + lastName + "</b>";

			strList += StringUtils.isNotBlank(address.getLine1()) ? "<br />" + address.getLine1() : "";
			strList += StringUtils.isNotBlank(address.getLine2()) ? "<br />" + address.getLine2() : "";
			strList += StringUtils.isNotBlank(address.getTown()) ? "<br />" + address.getTown() : "";
			strList += StringUtils.isNotBlank(address.getPostalCode()) ? "<br />" + address.getPostalCode() : "";
			strList += (address.getCountry() != null && StringUtils.isNotBlank(address.getCountry().getName())) ? "<br />"
					+ address.getCountry().getName() : "";

			addressView.setText(Html.fromHtml(strList));
			((TextView) findViewById(R.id.textView1)).setTextColor(colorStepDone);

			((TextView) findViewById(R.id.textView3)).setTextColor(colorStepCurrent);
			deliveryView.setVisibility(View.VISIBLE);

			((ImageButton) findViewById(R.id.imageButtonDeliveryAddress)).setImageResource(R.drawable.accept);
			((ImageButton) findViewById(R.id.imageButtonDeliveryMethod)).setVisibility(View.VISIBLE);
		}

		//delivery mode section
		CartDeliveryMode deliveryMode = controller.getCart().getDeliveryMode();
		if (deliveryMode != null)
		{

			strList = "<b>" + deliveryMode.getName() + "</b><br />" + deliveryMode.getDescription() + "<br />"
					+ deliveryMode.getDeliveryCost().getFormattedValue();
			deliveryView.setText(Html.fromHtml(strList));
			((TextView) findViewById(R.id.textView3)).setTextColor(colorStepDone);

			((TextView) findViewById(R.id.textView4)).setTextColor(colorStepCurrent);
			paymentView.setVisibility(View.VISIBLE);

			((ImageButton) findViewById(R.id.imageButtonDeliveryMethod)).setImageResource(R.drawable.accept);
			((ImageButton) findViewById(R.id.imageButtonPaymentDetails)).setVisibility(View.VISIBLE);
		}

		//payment section
		CartPaymentInfo payment = controller.getCart().getPaymentInfo();
		if (payment != null)
		{
			String accountHolderName = payment.getAccountHolderName();
			String cardType = payment.getCardType().getName();
			String cardNumber = payment.getCardNumber();

			strList = "<b>" + accountHolderName + "<b> <br />" + cardNumber + "<br />" + cardType;
			paymentView.setText(Html.fromHtml(strList));
			((TextView) findViewById(R.id.textView4)).setTextColor(colorStepDone);

			((ImageButton) findViewById(R.id.imageButtonPaymentDetails)).setImageResource(R.drawable.accept);
		}

		TextView items = (TextView) findViewById(R.id.textView5);
		items.setText(controller.getCart().getTotalItems() + " " + getString(R.string.items_label));

		TextView subtotal = (TextView) findViewById(R.id.lbl_subTotal);
		subtotal.setText(controller.getCart().getSubTotal().getFormattedValue());

		TextView discounts = (TextView) findViewById(R.id.lbl_totalDiscounts);
		discounts.setText(getString(R.string.discount, controller.getCart().getTotalDiscounts().getFormattedValue()));

		TextView totaltax = (TextView) findViewById(R.id.lbl_totalTax);
		totaltax.setText(controller.getCart().getTotalTax().getFormattedValue());

		TextView totalprice = (TextView) findViewById(R.id.lbl_totalPrice);
		totalprice.setText(controller.getCart().getTotalPrice().getFormattedValue());

		boolean stepsComplete = address != null && deliveryMode != null && payment != null;
		RelativeLayout containerTerms = (RelativeLayout) findViewById(R.id.containerTerms);
		containerTerms.setVisibility(stepsComplete ? View.VISIBLE : View.GONE);

		CheckBox checkBoxTerms = (CheckBox) findViewById(R.id.checkBoxTerms);
		boolean readyToConfirm = stepsComplete && checkBoxTerms.isChecked();
		findViewById(R.id.buttonConfirmOrder).setEnabled(readyToConfirm);

	}

	public void selectAddress(View view)
	{
		Intent intent = new Intent(this, AddressListActivity.class);
		intent.putExtra("canSelectAddress", true);
		startActivity(intent);
	}

	public void selectMode(View view)
	{
		Cart cart = controller.getCart();
		if (cart.getDeliveryAddress() != null)
		{
			Intent intent = new Intent(this, DeliveryMethodActivity.class);
			startActivity(intent);
		}
	}

	public void selectPayment(View view)
	{
		Cart cart = controller.getCart();
		if (cart.getDeliveryAddress() != null && cart.getDeliveryMode() != null)
		{
			Intent intent = new Intent(this, PaymentListActivity.class);
			intent.putExtra("canSelectPayment", true);
			startActivity(intent);
		}
	}

	public void showBasketDetails(View view)
	{
		Intent intent = new Intent(this, BasketDetailActivity.class);
		intent.putExtra("basketDetailsCart", "");
		startActivity(intent);
	}

	public void selectTermsAndConditions(View view)
	{
		if (view.getId() == R.id.containerTerms)
		{
			CheckBox checkBoxTerms = (CheckBox) findViewById(R.id.checkBoxTerms);
			checkBoxTerms.setChecked(!checkBoxTerms.isChecked());
		}
		updateUI();
	}

	public void confirmOrder(View view)
	{
		new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_info).setTitle(R.string.generic_title_confirm)
				.setMessage("Confirm this order?").setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						controller.placeOrder();
					}
				}).setNegativeButton(android.R.string.no, null).show();
	}

}
