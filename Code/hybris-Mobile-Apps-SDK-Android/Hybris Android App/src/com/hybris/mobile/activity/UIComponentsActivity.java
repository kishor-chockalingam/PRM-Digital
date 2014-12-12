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

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.hybris.mobile.R;
import com.hybris.mobile.fragment.UIComponents1Fragment;
import com.hybris.mobile.fragment.UIComponents2Fragment;
import com.hybris.mobile.utility.MenuUtil;


public class UIComponentsActivity extends FragmentActivity
{

	private ViewPager mViewPager;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_ui_components);
		mViewPager = (ViewPager) findViewById(R.id.pager);
		Fragment[] frags =
		{ new UIComponents1Fragment(), new UIComponents2Fragment() };
		FragmentPagerAdapter adapter = new TabsAdapter(getFragmentManager(), frags);
		mViewPager.setAdapter(adapter);
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
		{
			@Override
			public void onPageSelected(int position)
			{
				getActionBar().setSelectedNavigationItem(position);
			}
		});

		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		MyTabListener tabListener = new MyTabListener(mViewPager, actionBar);
		Tab tab = actionBar.newTab().setText(R.string.tab_ui_components1).setTabListener(tabListener);
		actionBar.addTab(tab);

		tab = actionBar.newTab().setText(R.string.tab_ui_components2).setTabListener(tabListener);
		actionBar.addTab(tab);
		actionBar.setSelectedNavigationItem(0);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		return MenuUtil.onOptionsItemSelected(item, this);
	}

	public static final class TabsAdapter extends FragmentPagerAdapter
	{
		private final Fragment[] mFragments;

		public TabsAdapter(FragmentManager fm, Fragment[] fragments)
		{
			super(fm);
			mFragments = fragments;
		}

		@Override
		public int getCount()
		{
			return mFragments.length;
		}

		@Override
		public Fragment getItem(int position)
		{
			return mFragments[position];
		}
	}

	public static class MyTabListener implements android.app.ActionBar.TabListener
	{

		private ViewPager mViewPager;
		private ActionBar mActionBar;

		public MyTabListener(ViewPager viewPager, ActionBar actionBar)
		{
			mViewPager = viewPager;
			this.mActionBar = actionBar;
		}

		public void onTabSelected(Tab tab, FragmentTransaction ft)
		{
			mViewPager.setCurrentItem(tab.getPosition());

			int resId = tab.getPosition() == 1 ? R.string.actionBarTitle_ui_components2 : R.string.actionBarTitle_ui_components1;
			mActionBar.setTitle(resId);
		}

		public void onTabUnselected(Tab tab, FragmentTransaction ft)
		{
		}

		public void onTabReselected(Tab tab, FragmentTransaction ft)
		{
		}
	}

}
