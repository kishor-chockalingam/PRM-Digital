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

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.hybris.mobile.R;
import com.hybris.mobile.adapter.FacetListAdapter;
import com.hybris.mobile.adapter.FacetValueAdapter;
import com.hybris.mobile.adapter.SortAdapter;
import com.hybris.mobile.model.Facet;
import com.hybris.mobile.model.FacetValue;
import com.hybris.mobile.model.Sort;


public class FilterDialogFragment extends DialogFragment
{

	private Context mContext;
	private List<Sort> mSorts;
	private List<Facet> mFacets;
	private List<FacetValue> mSelectedFacetValues;
	private Sort mSelectedSort;

	// Callback when dialog is dismissed
	public interface FilterDialogListener
	{
		void onFiltersCancelled();

		void onFiltersHaveChanged(Sort selectedSort);
	}

	// Factory method
	public static FilterDialogFragment newInstance(Context context, List<Sort> sorts, List<Facet> facets, List<FacetValue> selectedFacetValues, Sort selectedSort)
	{
		FilterDialogFragment fragment = new FilterDialogFragment();
		fragment.mContext = context;
		fragment.mSorts = sorts;
		fragment.mFacets = facets;
		fragment.mSelectedFacetValues = selectedFacetValues;
		fragment.mSelectedSort = selectedSort;
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

		dialogBuilder.setTitle(R.string.filter_dialog_title);

		dialogBuilder.setNegativeButton(R.string.cancel_button_label, new DialogInterface.OnClickListener()
		{

			public void onClick(DialogInterface arg0, int arg1)
			{
				FilterDialogListener activity = (FilterDialogListener) getActivity();
				activity.onFiltersCancelled();
				dismiss();
			}
		});


		// Inflate the view here instead of in onCreateView()
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		ListView listView = (ListView) inflater.inflate(R.layout.popup_list, null, false);

		// OnClickListener
		listView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long arg)
			{
				Object object = adapter.getAdapter().getItem(position);
				if (object.getClass() == FacetValue.class)
				{
					FacetValue fv = (FacetValue) object;
					Facet f = fv.getFacet();

					// Deselect
					if (fv.getSelected())
					{
						fv.setSelected(false);
						mSelectedFacetValues.remove(fv);
					}
					// Select
					else
					{
						// Multi-select check
						if (!f.getMultiSelect())
						{
							for (FacetValue otherFacetValue : mSelectedFacetValues)
							{
								if (otherFacetValue.getFacet() == f)
								{
									mSelectedFacetValues.remove(otherFacetValue);
								}
							}
						}
						fv.setSelected(true);
						mSelectedFacetValues.add(fv);
					}
				}
				else if (object.getClass() == Sort.class)
				{
					Sort selectedSort = (Sort) object;

					if (!selectedSort.getSelected())
					{
						for (Sort otherSort : mSorts)
						{
							otherSort.setSelected(false);
						}
						
						mSelectedSort = new Sort(true, selectedSort.getName(), selectedSort.getCode());
					}
				}

				// Dismiss and let listener know
				FilterDialogListener activity = (FilterDialogListener) getActivity();
				activity.onFiltersHaveChanged(mSelectedSort);

				dismiss();
			}
		});

		FacetListAdapter adapter = new FacetListAdapter(mContext);

		// Create the sort section first
		ArrayList<Sort> sortSection = new ArrayList<Sort>();
		for (Sort s : mSorts)
		{
			sortSection.add(s);
		}
		adapter.setSortAdapter(new SortAdapter(mContext, android.R.layout.simple_list_item_single_choice, sortSection));

		// Create the sections, one for each facet
		for (Facet f : mFacets)
		{
			ArrayList<FacetValue> section = new ArrayList<FacetValue>();
			for (FacetValue fv : f.getValues())
			{
				section.add(fv);
			}
			adapter.addSection(f, new FacetValueAdapter(mContext,
					f.getMultiSelect() ? R.layout.two_line_list_item_multiple_choice : R.layout.two_line_list_item_single_choice,
					section));
		}

		listView.setAdapter(adapter);
		dialogBuilder.setView(listView);

		return dialogBuilder.create();
	}

}
