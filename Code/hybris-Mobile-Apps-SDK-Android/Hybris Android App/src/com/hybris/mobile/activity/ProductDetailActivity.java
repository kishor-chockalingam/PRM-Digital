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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.hybris.mobile.DataConstants;
import com.hybris.mobile.R;
import com.hybris.mobile.adapter.ProductVariantAdapter;
import com.hybris.mobile.fragment.ProductVariantDialogFragment;
import com.hybris.mobile.fragment.ProductVariantDialogFragment.DoneClickListener;
import com.hybris.mobile.model.product.ProductOptionItem;


public class ProductDetailActivity extends AbstractProductDetailActivity
{

	private List<ProductOptionItem> mLinks;
	private List<String> mVariantOptions;
	private ProductVariantAdapter mAdapter;
	private ListView mListView;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_detail);

		getActionBar().setHomeButtonEnabled(true);

		// Allow links in promotions label
		TextView promotionsTextView = (TextView) findViewById(R.id.textViewPromotion);
		promotionsTextView.setMovementMethod(LinkMovementMethod.getInstance());

		mLinks = new ArrayList<ProductOptionItem>();
		mVariantOptions = new ArrayList<String>();
		mAdapter = new ProductVariantAdapter(this, mLinks);
		mListView = (ListView) findViewById(R.id.list_variants);
		mListView.setAdapter(mAdapter);

		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position, long l)
			{
				int selected = -1;
				mVariantOptions.clear();
				for (ProductOptionItem option : mProduct.getAllOptions())
				{
					if (option.isSelectedOption())
					{
						continue;
					}
					if (option.getQualifier().equals(mLinks.get(position).getQualifier()))
					{
						if (!mVariantOptions.contains(getVariantOption(option)))
						{
							mVariantOptions.add(getVariantOption(option));
							if (option.getValue().equals(mLinks.get(position).getValue()))
							{
								selected = mVariantOptions.size() - 1;
							}
						}
					}
				}
				ProductVariantDialogFragment fragment = new ProductVariantDialogFragment(mLinks.get(position).getQualifier(), mLinks
						.get(position).getName(), selected, mVariantOptions.toArray(new String[0]), new DoneClickListener()
				{
					@Override
					public void onClick(String qualifier, int position)
					{
						if (position < 0)
							return;
						for (ProductOptionItem option : mProduct.getAllOptions())
						{
							if (option.getQualifier().equals(qualifier))
							{
								if (getVariantOption(option).equals(mVariantOptions.get(position)))
								{
									if (!StringUtils.equals(mProduct.getCode(), option.getCode()))
									{
										Intent intent = new Intent(ProductDetailActivity.this, ProductDetailActivity.class);
										intent.putExtra(DataConstants.PRODUCT_CODE, option.getCode());
										startActivity(intent);
										finish();
										break;
									}
								}
							}
						}
					}
				});
				fragment.show(getFragmentManager(), "");
			}


		});
	}

	private String getVariantOption(ProductOptionItem option)
	{
		String result = option.getValue();
		if (!option.getQualifier().equals("style"))
		{
			String stockLevelStatusCode = getString(R.string.stock_details_in_stock).toLowerCase();
			if (option.getStockLevel() != null && option.getStockLevel() <= 0)
			{
				stockLevelStatusCode = getString(R.string.stock_details_out_of_stock).toLowerCase();
			}

			String stockLevel = "0";
			if (option.getStockLevel() != null)
			{
				stockLevel = option.getStockLevel().toString();
			}
			result += " - " + stockLevel + " " + stockLevelStatusCode;
		}

		return result;
	}

	@Override
	public void updateUI()
	{
		super.updateUI();
		mLinks.addAll(getSelectedOptions());
		if (mLinks != null && !mLinks.isEmpty())
		{
			((LinearLayout) mListView.getParent()).setVisibility(View.VISIBLE);
			// Adjust the ListView's height: temporary workaround to make the list work inside a ScrollView.
			int height = getResources().getDimensionPixelSize(R.dimen.listItemTwoLinesHeight) * mLinks.size();
			LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, height);
			mListView.setLayoutParams(params);
			mAdapter.notifyDataSetChanged();
		}
		else
		{
			((LinearLayout) mListView.getParent()).setVisibility(View.GONE);
		}
	}

	private List<ProductOptionItem> getSelectedOptions()
	{
		List<ProductOptionItem> result = new ArrayList<ProductOptionItem>();
		Map<String, ProductOptionItem> select = new HashMap<String, ProductOptionItem>();
		for (ProductOptionItem option : mProduct.getAllOptions())
		{

			if (option.getVariantOptionQualifiers() != null && !option.getVariantOptionQualifiers().isEmpty())
			{
				ProductOptionItem o = new ProductOptionItem();
				o.setName(option.getName());
				o.setQualifier(option.getQualifier());

				if (StringUtils.isNotBlank(o.getName()))
				{
					o.setValue(getString(R.string.variants_select) + " " + o.getName());
				}

				o.setSelectedOption(true);
				select.put(o.getQualifier(), o);
				if (option.isSelectedOption())
				{
					result.add(option);
				}
			}

		}
		for (ProductOptionItem option : result)
		{
			select.remove(option.getQualifier());
		}
		// Stock level
		TextView stockLevelTextView = (TextView) findViewById(R.id.textViewStockLevel);
		if (select.values().size() > 0)
		{
			stockLevelTextView.setText(((ProductOptionItem) select.values().toArray()[0]).getValue());
		}

		result.addAll(select.values());
		Collections.sort(result, new Comparator<ProductOptionItem>()
		{
			@Override
			public int compare(ProductOptionItem lhs, ProductOptionItem rhs)
			{
				return lhs.getName().compareTo(rhs.getName());
			}
		});
		return result;
	}

}
