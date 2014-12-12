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
package com.hybris.mobile.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hybris.mobile.R;
import com.hybris.mobile.listener.ActionGo;


public class HYFormSecureTextEntryCell extends LinearLayout
{

	private EditText editText;
	private TextView messageLabel;

	public HYFormSecureTextEntryCell(Context context)
	{
		super(context);
		setupViewItems();
	}

	private void setupViewItems()
	{
		((Activity) getContext()).getLayoutInflater().inflate(R.layout.hy_form_secure_text_entry_cell_view, this);
		editText = (EditText) findViewById(R.id.edit_text_content);
		messageLabel = (TextView) findViewById(R.id.lbl_message);

		editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
		editText.setSingleLine();
		editText.isFocusable();
		editText.setFocusableInTouchMode(true);
		editText.setImeOptions(EditorInfo.IME_ACTION_NEXT);

		editText.setTypeface(Typeface.DEFAULT);
		editText.setTransformationMethod(new PasswordTransformationMethod());
	}

	public void setContentText(String text)
	{
		editText.setText(text);
	}

	public String getContentText()
	{
		return editText.getText().toString();
	}

	public void addContentChangedListener(TextWatcher watcher)
	{
		editText.addTextChangedListener(watcher);
	}

	public void setOnFocusChangeListener(OnFocusChangeListener listener)
	{
		editText.setOnFocusChangeListener(listener);
	}

	public void setContentTitle(String text)
	{
		editText.setHint(text);
	}

	public void setContentInputType(int type)
	{
		editText.setInputType(type);
	}

	public void setMessage(String text)
	{
		messageLabel.setText(text);
	}

	public void showMessage(Boolean show)
	{
		if (show)
		{
			messageLabel.setVisibility(View.VISIBLE);
		}
		else
		{
			messageLabel.setVisibility(View.GONE);
		}
	}

	public void setFocus()
	{
		editText.requestFocus();
	}

	/**
	 * 
	 * Add a go button to the keyboard when editing this field. Single line to avoid to go to the next line in case of
	 * clicking the go button.
	 * 
	 * @param actionGo
	 */
	public void setImeDone(final ActionGo actionGo)
	{
		editText.setImeOptions(EditorInfo.IME_ACTION_GO);
		editText.setOnKeyListener(new OnKeyListener()
		{
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{
				if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)
				{
					actionGo.submit();
					return true;
				}
				else
				{
					return false;
				}
			}
		});
	}
}
