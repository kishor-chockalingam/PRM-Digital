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

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.EditText;

import com.hybris.mobile.R;
import com.hybris.mobile.activity.LoginActivity;


public class LoginFragment extends Fragment implements TextWatcher
{

	private View mLoginButton;
	private EditText mEmailView;
	private EditText mPasswordView;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_login, container, false);
		mLoginButton = view.findViewById(R.id.btn_login);
		mEmailView = (EditText) view.findViewById(R.id.txt_email);
		mPasswordView = (EditText) view.findViewById(R.id.txt_password);
		mEmailView.addTextChangedListener(this);
		mPasswordView.addTextChangedListener(this);

		mPasswordView.setOnKeyListener(new OnKeyListener()
		{
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{
				if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)
				{
					((LoginActivity) getActivity()).login(null);
					return true;
				}
				else
				{
					return false;
				}
			}
		});

		updateLoginButtonEnabledState();
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		if (getActivity() instanceof LoginActivity)
		{
			((LoginActivity) getActivity()).initViews();
		}
	}

	@Override
	public void afterTextChanged(Editable s)
	{
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after)
	{
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count)
	{
		updateLoginButtonEnabledState();
	}

	private void updateLoginButtonEnabledState()
	{
		boolean hasText = mEmailView.getText().length() > 0 && mPasswordView.getText().length() > 0;
		mLoginButton.setEnabled(hasText);
	}

}
