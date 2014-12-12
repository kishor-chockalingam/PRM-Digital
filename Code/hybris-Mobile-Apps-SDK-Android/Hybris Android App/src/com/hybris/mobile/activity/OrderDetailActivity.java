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

import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hybris.mobile.DataConstants;
import com.hybris.mobile.Hybris;
import com.hybris.mobile.R;
import com.hybris.mobile.WebserviceMethodEnums;
import com.hybris.mobile.loader.RESTLoader;
import com.hybris.mobile.loader.RESTLoaderObserver;
import com.hybris.mobile.loader.RESTLoaderResponse;
import com.hybris.mobile.model.cart.CartDeliveryAddress;
import com.hybris.mobile.model.cart.CartDeliveryMode;
import com.hybris.mobile.model.cart.CartOrder;
import com.hybris.mobile.model.cart.CartPaymentInfo;
import com.hybris.mobile.query.QueryObjectId;
import com.hybris.mobile.utility.DateUtil;
import com.hybris.mobile.utility.JsonUtils;
import com.hybris.mobile.utility.RegexUtil;


public class OrderDetailActivity extends HybrisActivity implements RESTLoaderObserver
{
	private static final String LOG_TAG = OrderDetailActivity.class.getSimpleName();
	private CartOrder mOrderDetails;
	private String mOrderID;

	public void setmOrderID(String mOrderID)
	{
		this.mOrderID = mOrderID;
		getOrderDetails();
	}

	public void setmOrderDetails(CartOrder orderDetails)
	{
		this.mOrderDetails = orderDetails;
		updateUI();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.order_detail);
		setTitle(getString(R.string.order_completed_text));

		getActionBar().setHomeButtonEnabled(true);
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		handleIntent(getIntent());
	}

	private void handleIntent(Intent intent)
	{

		if (Hybris.isUserLoggedIn())
		{
			if (intent.hasExtra("orderDetails"))
			{
				((TextView) findViewById(R.id.lbl_thankYou)).setVisibility(View.VISIBLE);
				((TextView) findViewById(R.id.lbl_orderConfirmation)).setVisibility(View.VISIBLE);
				// TODO
				setmOrderDetails(JsonUtils.fromJson(intent.getStringExtra("orderDetails"), CartOrder.class));
			}
			if (intent.hasExtra(DataConstants.ORDER_ID))
			{
				setmOrderID(intent.getStringExtra(DataConstants.ORDER_ID));
			}
			// Call from another application (QR Code) 
			else if (StringUtils.equals(intent.getAction(), Intent.ACTION_VIEW))
			{
				setmOrderID(RegexUtil.getOrderIdFromHybrisPattern(intent.getDataString()));
			}
		}
		// User not logged in, redirection to the login page
		else
		{
			Intent loginIntent = new Intent(this, LoginActivity.class);

			// Call from another application (QR Code) 
			if (StringUtils.equals(intent.getAction(), Intent.ACTION_VIEW))
			{
				loginIntent.putExtra(DataConstants.ORDER_ID, RegexUtil.getOrderIdFromHybrisPattern(intent.getDataString()));
			}

			loginIntent.putExtra(DataConstants.INTENT_DESTINATION, DataConstants.INTENT_ORDER_DETAILS);
			loginIntent.putExtras(intent);
			startActivity(loginIntent);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		return false;
	}

	private void updateUI()
	{
		String pageTitle = getString(R.string.order_completed_text);
		if (getIntent().hasExtra(DataConstants.ORDER_ID))
		{
			pageTitle += " " + mOrderDetails.getCode();

			String strOrderStatus = "<b>" + mOrderDetails.getStatusDisplay() + "</b>";
			Calendar cal = DateUtil.fromIso8601(mOrderDetails.getCreated());
			strOrderStatus = strOrderStatus + "<br />" + getString(R.string.placed_on_placeholder_text) + " "
					+ cal.getTime().toString();
			TextView orderStatus = (TextView) findViewById(R.id.lbl_orderStatusView);
			orderStatus.setText(Html.fromHtml(strOrderStatus));
			findViewById(R.id.containerOrderStatus).setVisibility(View.VISIBLE);
			findViewById(R.id.dividerOrderStatus).setVisibility(View.VISIBLE);

		}
		setTitle(pageTitle);

		CartDeliveryAddress address = mOrderDetails.getDeliveryAddress();

		// delivery address section
		String strList = "";
		if (address != null)
		{
			String firstName = address.getTitle();
			String lastName = address.getLastName();
			String title = address.getFirstName();

			// strList = "Delivery address\n\n";
			strList = "<b>" + strList + title + " " + firstName + " " + lastName + "</b>";

			strList += StringUtils.isNotBlank(address.getLine1()) ? "<br />" + address.getLine1() : "";
			strList += StringUtils.isNotBlank(address.getLine2()) ? "<br />" + address.getLine2() : "";
			strList += StringUtils.isNotBlank(address.getTown()) ? "<br />" + address.getTown() : "";
			strList += StringUtils.isNotBlank(address.getPostalCode()) ? "<br />" + address.getPostalCode() : "";
			strList += (address.getCountry() != null && StringUtils.isNotBlank(address.getCountry().getName())) ? "<br />"
					+ address.getCountry().getName() : "";

			TextView addressView = (TextView) findViewById(R.id.lbl_addressView);
			addressView.setText(Html.fromHtml(strList));
		}

		// delivery mode section
		CartDeliveryMode deliveryMode = mOrderDetails.getDeliveryMode();
		if (deliveryMode != null)
		{
			strList = "<b>" + deliveryMode.getName() + "</b><br />" + deliveryMode.getDescription() + "<br />"
					+ deliveryMode.getDeliveryCost().getFormattedValue();
			TextView deliveryView = (TextView) findViewById(R.id.lbl_deliveryMethod);
			deliveryView.setText(Html.fromHtml(strList));
		}

		// payment section
		CartPaymentInfo payment = mOrderDetails.getPaymentInfo();
		if (payment != null)
		{
			String accountHolderName = payment.getAccountHolderName();
			String cardType = payment.getCardType().getName();
			String cardNumber = payment.getCardNumber();

			strList = "<b>" + accountHolderName + "</b><br />" + cardNumber + "<br />" + cardType;
			TextView paymentView = (TextView) findViewById(R.id.lbl_paymentDetails);
			paymentView.setText(Html.fromHtml(strList));
		}

		TextView subtotal = (TextView) findViewById(R.id.lbl_subTotal);
		subtotal.setText(mOrderDetails.getSubTotal().getFormattedValue());

		TextView totaltax = (TextView) findViewById(R.id.lbl_totalTax);
		totaltax.setText(mOrderDetails.getTotalTax().getFormattedValue());

		TextView deliveryprice = (TextView) findViewById(R.id.lbl_deliveryCharge);
		deliveryprice.setText(mOrderDetails.getDeliveryCost().getFormattedValue());

		TextView totalprice = (TextView) findViewById(R.id.lbl_totalPriceWithTax);
		totalprice.setText(mOrderDetails.getTotalPrice().getFormattedValue());

	}

	public void showBasketDetails(View view)
	{
		Intent intent = new Intent(this, BasketDetailActivity.class);
		intent.putExtra("basketDetailsOrder", JsonUtils.toJson(mOrderDetails.getEntries()));
		startActivity(intent);
	}

	public void close(View view)
	{

	}

	private void getOrderDetails()
	{
		QueryObjectId query = new QueryObjectId();
		query.setObjectId(mOrderID);
		RESTLoader.execute(this, WebserviceMethodEnums.METHOD_GET_ORDER, query, this, true, true);
	}

	@Override
	public void onReceiveResult(RESTLoaderResponse restLoaderResponse, WebserviceMethodEnums webserviceEnumMethod)
	{
		if (restLoaderResponse.getCode() == RESTLoaderResponse.SUCCESS)
		{
			switch (webserviceEnumMethod)
			{
				case METHOD_GET_ORDER:
					setmOrderDetails(JsonUtils.fromJson(restLoaderResponse.getData(), CartOrder.class));
					break;

				default:
					break;
			}
		}
		else if (restLoaderResponse.getCode() == RESTLoaderResponse.ERROR)
		{
			switch (webserviceEnumMethod)
			{
				case METHOD_GET_ORDER:
					Toast.makeText(getApplicationContext(), R.string.error_order_not_found, 40000).show();
					finish();
					break;

				default:
					break;
			}
		}
	}

}
