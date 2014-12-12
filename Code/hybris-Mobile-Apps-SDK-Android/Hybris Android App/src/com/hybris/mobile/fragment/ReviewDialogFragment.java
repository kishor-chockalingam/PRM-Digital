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
package com.hybris.mobile.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ListView;

import com.hybris.mobile.R;
import com.hybris.mobile.adapter.ReviewAdapter;
import com.hybris.mobile.model.product.Product;


public class ReviewDialogFragment extends DialogFragment
{

	private Product mProduct;
	private Context mContext;

	// Factory method
	public static ReviewDialogFragment newInstance(Product product, Context context)
	{
		ReviewDialogFragment fragment = new ReviewDialogFragment();
		fragment.setContext(context);
		fragment.setProduct(product);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);

		dialogBuilder.setTitle(R.string.reviews_dialog_title);

		// Inflate the view here instead of in onCreateView()
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		ListView listView = (ListView) inflater.inflate(R.layout.popup_list, null, false);

		ReviewAdapter adapter = new ReviewAdapter(getContext(), mProduct.getReviews());

		listView.setAdapter(adapter);
		dialogBuilder.setView(listView);

		return dialogBuilder.create();
	}

	public Context getContext()
	{
		return mContext;
	}

	public void setContext(Context context)
	{
		this.mContext = context;
	}

	public Product getProduct()
	{
		return mProduct;
	}

	public void setProduct(Product mProduct)
	{
		this.mProduct = mProduct;
	}


}
