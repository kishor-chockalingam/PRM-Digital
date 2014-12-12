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
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.hybris.mobile.R;


public class TextDialogFragment extends DialogFragment
{

	private Context mContext;
	private String mTitle;
	private String mText;

	// Factory method
	public static TextDialogFragment newInstance(String title, String text, Context context)
	{
		TextDialogFragment fragment = new TextDialogFragment();
		fragment.setContext(context);
		fragment.setTitle(title);
		fragment.setText(text);
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

		dialogBuilder.setTitle(getTitle());

		// Inflate the view here instead of in onCreateView()
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		TextView view = (TextView) inflater.inflate(R.layout.dialog_text, null, false);
		view.setMovementMethod(new ScrollingMovementMethod());
		view.setText(mText);

		dialogBuilder.setView(view);
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

	public String getTitle()
	{
		return mTitle;
	}

	public void setTitle(String title)
	{
		this.mTitle = title;
	}

	public String getText()
	{
		return mText;
	}

	public void setText(String text)
	{
		this.mText = text;
	}

}
