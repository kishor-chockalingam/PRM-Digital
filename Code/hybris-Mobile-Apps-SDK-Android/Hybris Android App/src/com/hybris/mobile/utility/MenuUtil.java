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
package com.hybris.mobile.utility;


import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.hybris.mobile.R;
import com.hybris.mobile.activity.AccountListActivity;
import com.hybris.mobile.activity.BarCodeScannerActivity;
import com.hybris.mobile.activity.CartListActivity;
import com.hybris.mobile.activity.ObjectListActivity;
import com.hybris.mobile.activity.StoreLocatorActivity;
import com.hybris.mobile.activity.UIComponentsActivity;


public class MenuUtil
{

	private static boolean cartEmpty = true;

	public static void setCartEmpty(Boolean empty)
	{
		cartEmpty = empty;
	}

	public static boolean onCreateOptionsMenu(Menu menu, Activity activity)
	{
		MenuInflater inflater = activity.getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		MenuItem cartItem = menu.findItem(R.id.cart);
		if (cartEmpty)
		{
			cartItem.setIcon(R.drawable.ic_menu_cart_empty);
		}
		else
		{
			cartItem.setIcon(R.drawable.ic_menu_cart_full);
		}

		return true;
	}

	public static boolean onCreateProductDetailsOptionsMenu(Menu menu, Activity activity)
	{
		MenuInflater inflater = activity.getMenuInflater();
		inflater.inflate(R.menu.product_details, menu);

		MenuItem cartItem = menu.findItem(R.id.cart);
		if (cartEmpty)
		{
			cartItem.setIcon(R.drawable.ic_menu_cart_empty);
		}
		else
		{
			cartItem.setIcon(R.drawable.ic_menu_cart_full);
		}

		return true;
	}

	public static boolean onOptionsItemSelected(MenuItem item, Activity activity)
	{
		Intent launchNewIntent = null;

		switch (item.getItemId())
		{
			case android.R.id.home:
				Intent intent = new Intent(activity, ObjectListActivity.class);
				intent.putExtra(ObjectListActivity.INTENT_EXTRA_RESTART, true);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				activity.startActivity(intent);
				activity.finish();
				return true;
			case R.id.refine:
				((ObjectListActivity) activity).showFilter(null);
				return true;
			case R.id.cart:
				launchNewIntent = new Intent(activity, CartListActivity.class);
				activity.startActivity(launchNewIntent);
				return true;
			case R.id.account:
				launchNewIntent = new Intent(activity, AccountListActivity.class);
				activity.startActivity(launchNewIntent);
				return true;
			case R.id.scan:
				launchNewIntent = new Intent(activity, BarCodeScannerActivity.class);
				activity.startActivity(launchNewIntent);
				return true;
			case R.id.store_finder:
				launchNewIntent = new Intent(activity, StoreLocatorActivity.class);
				activity.startActivity(launchNewIntent);
				return true;
			case R.id.ui_components:
				launchNewIntent = new Intent(activity, UIComponentsActivity.class);
				activity.startActivity(launchNewIntent);
				return true;
			default:
				return false;
		}
	}

}
