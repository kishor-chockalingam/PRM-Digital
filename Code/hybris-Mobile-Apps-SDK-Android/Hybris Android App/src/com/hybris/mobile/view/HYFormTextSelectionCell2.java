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

import java.util.Hashtable;
import java.util.List;

import com.hybris.mobile.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

/**
 * Currently unused, but may be used instead of {@link HYFormTextSelectionCell} in the future (rename). This one uses a
 * standard spinner.
 */
public class HYFormTextSelectionCell2 extends LinearLayout implements OnItemSelectedListener {

    private TextView title;
    private Spinner spinner;
    private Hashtable<String, Object> data;

    public HYFormTextSelectionCell2(Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.hy_form_spinner, this);
        title = (TextView) findViewById(R.id.textViewTitle);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
    }

    public void init(Hashtable<String, Object> obj) {
        data = obj;
        title.setText(obj.get("title").toString());

        boolean hasValues = obj.get("values") instanceof List<?>;
        if (hasValues) {
            @SuppressWarnings({ "rawtypes", "unchecked" })
            SpinnerAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item,
                    android.R.id.text1, (List<?>) obj.get("values"));
            spinner.setAdapter(adapter);
        }
        spinner.setVisibility(hasValues ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        data.put("value", spinner.getSelectedItem());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        data.remove("value");
    }
}
