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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hybris.mobile.R;

public class HYFormTextSelectionCell extends LinearLayout {
    private TextView textView;

    public HYFormTextSelectionCell(Context context) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.hy_form_pseudo_spinner, this);
        textView = (TextView) findViewById(R.id.textViewTitle);
    }

    public void setSpinnerText(String string) {
        textView.setText(string);
    }

}
