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

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.hybris.mobile.fragment.SettingsFragment;


public class SettingsActivity extends PreferenceActivity
{

	@Override
	public void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);

		// Display the fragment for the setting page
		getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();

	}

}
