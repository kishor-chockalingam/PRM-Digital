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
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

import com.hybris.mobile.R;

public class HYFormSwitchCell extends LinearLayout {

    private CheckedTextView checkedTextView;

    public HYFormSwitchCell(Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.hy_form_checkbox, this);
        checkedTextView = (CheckedTextView) findViewById(R.id.checkedTextView);
    }

    public void setCheckboxText(String string) {
        checkedTextView.setText(string);
    }

    public void setCheckboxChecked(boolean checked) {
        checkedTextView.setChecked(checked);

    }

    public boolean isCheckboxChecked() {
        return checkedTextView.isChecked();
    }

    public void toggleCheckbox() {
        checkedTextView.toggle();
    }

}
