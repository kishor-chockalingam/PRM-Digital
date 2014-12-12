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

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.hybris.mobile.R;
import com.hybris.mobile.WebserviceMethodEnums;
import com.hybris.mobile.loader.RESTLoader;
import com.hybris.mobile.loader.RESTLoaderObserver;
import com.hybris.mobile.loader.RESTLoaderResponse;
import com.hybris.mobile.query.QueryUpateCredentials;
import com.hybris.mobile.utility.MenuUtil;


public class ChangePasswordActivity extends Activity implements RESTLoaderObserver
{

	private EditText mCurrentPassword;
	private EditText mNewPassword;
	private EditText mConfirmPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.change_password);
		setTitle(R.string.change_password);
		getActionBar().setHomeButtonEnabled(true);

		mCurrentPassword = (EditText) findViewById(R.id.txt_current_password);
		mNewPassword = (EditText) findViewById(R.id.txt_new_password);
		mConfirmPassword = (EditText) findViewById(R.id.txt_confirm_password);

		mCurrentPassword.setOnFocusChangeListener(new View.OnFocusChangeListener()
		{
			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				setColor(mCurrentPassword, hasFocus);
			}
		});

		mNewPassword.setOnFocusChangeListener(new View.OnFocusChangeListener()
		{
			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				setColor(mNewPassword, hasFocus);
			}
		});

		mConfirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener()
		{
			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				setColor(mConfirmPassword, hasFocus);
			}
		});

		mConfirmPassword.setOnKeyListener(new OnKeyListener()
		{
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{
				if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)
				{
					changePassword(null);
					return true;
				}
				else
				{
					return false;
				}
			}
		});
	}

	private void setColor(EditText ediText, boolean hasFocus)
	{
		if (!hasFocus)
		{
			ediText.setTextColor(ChangePasswordActivity.this.getResources().getColor(R.color.textMedium));
		}
		else
		{
			ediText.setTextColor(ChangePasswordActivity.this.getResources().getColor(R.color.textHighlighted));
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		return MenuUtil.onOptionsItemSelected(item, this);
	}

	public void changePassword(View view)
	{
		String currentPassword = mCurrentPassword.getText().toString();
		String newPassword = mNewPassword.getText().toString();
		String confirmPassword = mConfirmPassword.getText().toString();

		if (newPassword.length() > 0 && confirmPassword.length() > 0 && currentPassword.length() > 0
				&& newPassword.equals(confirmPassword))
		{

			QueryUpateCredentials query = new QueryUpateCredentials();
			query.setOldValue(currentPassword);
			query.setNewValue(newPassword);

			RESTLoader.execute(this, WebserviceMethodEnums.METHOD_UPDATE_PASSWORD, query, this, true, true);
		}
		else
		{
			Toast.makeText(getApplicationContext(), R.string.generic_failed_try_again_message_popup, Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onReceiveResult(RESTLoaderResponse restLoaderResponse, WebserviceMethodEnums webserviceEnumMethod)
	{
		if (restLoaderResponse.getCode() == RESTLoaderResponse.SUCCESS)
		{
			switch (webserviceEnumMethod)
			{
				case METHOD_UPDATE_PASSWORD:
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
					Toast.makeText(getApplicationContext(), R.string.generic_success_message_popup, Toast.LENGTH_SHORT).show();
					finish();
					break;

				default:
					break;
			}
		}
		else if (restLoaderResponse.getCode() == RESTLoaderResponse.ERROR)
		{
			switch (webserviceEnumMethod)
			{
				case METHOD_UPDATE_PASSWORD:
					Toast.makeText(getApplicationContext(), R.string.generic_failed_try_again_message_popup, Toast.LENGTH_SHORT)
							.show();
					break;

				default:
					break;
			}
		}
	}

}
