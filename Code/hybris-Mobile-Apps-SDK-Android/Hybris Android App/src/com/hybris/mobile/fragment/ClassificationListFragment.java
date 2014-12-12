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

import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ListView;

import com.hybris.mobile.R;
import com.hybris.mobile.adapter.HashMapAdapter;
import com.hybris.mobile.adapter.SeparatedListAdapter;
import com.hybris.mobile.model.product.ProductClassification;
import com.hybris.mobile.model.product.ProductClassification.Feature;


public class ClassificationListFragment extends DialogFragment
{

	private Context mContext;
	private String mTitle;
	private List<ProductClassification> classifications;

	// Factory method
	public static ClassificationListFragment newInstance(String title, List<ProductClassification> classifications, Context context)
	{
		ClassificationListFragment fragment = new ClassificationListFragment();
		fragment.setContext(context);
		fragment.setTitle(title);
		fragment.setClassifications(classifications);
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

		dialogBuilder.setTitle(R.string.generic_title_more_information);

		// Inflate the view here instead of in onCreateView()
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		ListView listView = (ListView) inflater.inflate(R.layout.popup_list, null, false);

		SeparatedListAdapter adapter = new SeparatedListAdapter(getContext());

		// Create the sections, one for each classification
		if (classifications != null)
		{
			for (ProductClassification classification : classifications)
			{
				HashMap<String, String> featureMap = new HashMap<String, String>();

				if (classification.getFeatures() != null)
				{
					for (Feature feature : classification.getFeatures())
					{
						if (feature.getFeatureValues() != null && !feature.getFeatureValues().isEmpty())
						{
							featureMap.put(feature.getName(), feature.getFeatureValues().get(0).getValue());
						}

					}
				}
				adapter.addSection(classification.getName(), new HashMapAdapter(this.getContext(), featureMap));

			}
		}


		listView.setAdapter(adapter);
		dialogBuilder.setView(listView);

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

	public List<ProductClassification> getClassifications()
	{
		return classifications;
	}

	public void setClassifications(List<ProductClassification> classifications)
	{
		this.classifications = classifications;
	}


}
