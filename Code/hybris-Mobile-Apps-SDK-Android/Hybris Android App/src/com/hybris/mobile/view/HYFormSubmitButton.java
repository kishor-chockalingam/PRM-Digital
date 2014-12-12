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

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;

import com.hybris.mobile.R;

public class HYFormSubmitButton extends LinearLayout {

	private Button button;

    public HYFormSubmitButton(Context context) {
		super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.hy_form_button_submit, this);
        button = (Button) findViewById(R.id.buttonSubmit);
	}

    public void setButtonText(String string) {
        button.setText(string);
    }

    public void setOnButtonClickListener(OnClickListener onClickListener) {
        button.setOnClickListener(onClickListener);
    }

}
