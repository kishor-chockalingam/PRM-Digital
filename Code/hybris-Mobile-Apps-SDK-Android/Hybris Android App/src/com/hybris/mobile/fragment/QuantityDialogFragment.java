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
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import com.hybris.mobile.R;


public class QuantityDialogFragment extends DialogFragment
{

	private static final String MAXIMUM_QUANTITY = "maximumQuantity";
	private static final String STARTING_QUANTITY = "startingQuantity";

	private int mQuantity;
	private int mMax;
	private Context mContext;
	private NumberPicker mPicker;

	// Callback when dialog is dismissed
	public interface QuantityDialogListener
	{
		void onFinishDialog(int quantity);
	}

	// Factory method
	public static QuantityDialogFragment newInstance(int startingQuantity, int maximumQuantity, Context context)
	{
		QuantityDialogFragment fragment = new QuantityDialogFragment();
		Bundle args = new Bundle();
		args.putInt(STARTING_QUANTITY, startingQuantity);
		args.putInt(MAXIMUM_QUANTITY, maximumQuantity);
		fragment.setArguments(args);
		fragment.setContext(context);

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mQuantity = getArguments().getInt(STARTING_QUANTITY);
		mMax = getArguments().getInt(MAXIMUM_QUANTITY);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
		dialogBuilder.setTitle(R.string.choose_quantity_title);

		dialogBuilder.setPositiveButton(R.string.ok_button_label, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface arg0, int arg1)
			{
				QuantityDialogListener activity = (QuantityDialogListener) getActivity();
				activity.onFinishDialog(mPicker.getValue());
				dismiss();
			}
		});
		dialogBuilder.setNegativeButton(R.string.cancel_button_label, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface arg0, int arg1)
			{
				dismiss();
			}
		});

		// Inflate the view here instead of in onCreateView()
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View v = inflater.inflate(R.layout.quantity_fragment, null, false);
		mPicker = (NumberPicker) v.findViewById(R.id.quantityPicker);
		setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_DeviceDefault);

		mPicker.setMinValue(1);
		mPicker.setMaxValue(mMax);
		mPicker.setValue(mQuantity);
		mPicker.setWrapSelectorWheel(false);

		dialogBuilder.setView(v);

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

}
