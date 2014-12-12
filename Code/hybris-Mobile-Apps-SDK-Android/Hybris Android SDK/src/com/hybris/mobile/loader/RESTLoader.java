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
package com.hybris.mobile.loader;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.Loader;
import android.os.Bundle;

import com.hybris.mobile.R;
import com.hybris.mobile.WebserviceMethodEnums;
import com.hybris.mobile.data.WebService;
import com.hybris.mobile.query.Query;


public class RESTLoader implements LoaderCallbacks<RESTLoaderResponse>
{

	private static final String QUERY = "QUERY";
	private Activity mContext;
	private RESTLoaderObserver mRestLoaderObserver;
	private boolean mShowLoadingDialog;
	private ProgressDialog mProgressDialog;
	private WebserviceMethodEnums mWebserviceEnumMethod;

	private RESTLoader(Activity context, RESTLoaderObserver restLoaderObserver, boolean showLoadingDialog,
			WebserviceMethodEnums method)
	{
		mContext = context;
		mRestLoaderObserver = restLoaderObserver;
		mShowLoadingDialog = showLoadingDialog;
		mWebserviceEnumMethod = method;
	}

	/**
	 * Static method to make an asynchronous call to the OCC layer
	 * 
	 * @param context
	 * @param method
	 * @param query
	 * @param restLoaderObserver
	 * @param refresh
	 * @param showLoadingDialog
	 */
	public static void execute(Activity context, WebserviceMethodEnums method, Query query, RESTLoaderObserver restLoaderObserver,
			boolean refresh, boolean showLoadingDialog)
	{
		Bundle bundle = new Bundle();
		bundle.putParcelable(QUERY, query);

		// Initiating webservice layer
		WebService.initWebService(context.getApplicationContext());

		if (refresh)
		{
			context.getLoaderManager()
					.restartLoader(method.hashCode(), bundle, new RESTLoader(context, restLoaderObserver, showLoadingDialog, method))
					.forceLoad();
		}
		else
		{
			context.getLoaderManager()
					.initLoader(method.hashCode(), bundle, new RESTLoader(context, restLoaderObserver, showLoadingDialog, method))
					.forceLoad();
		}

	}

	@Override
	public Loader<RESTLoaderResponse> onCreateLoader(int id, Bundle args)
	{

		if (mShowLoadingDialog)
		{
			mContext.runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					mProgressDialog = ProgressDialog.show(mContext, mContext.getString(R.string.loading_dialog_title),
							mContext.getString(R.string.loading_dialog_message));
				}
			});
		}

		Query query = args.getParcelable(QUERY);

		return new RESTAsyncTaskLoader(mContext, query, mWebserviceEnumMethod);
	}

	@Override
	public void onLoaderReset(Loader<RESTLoaderResponse> loader)
	{
	}

	@Override
	public void onLoadFinished(Loader<RESTLoaderResponse> loader, RESTLoaderResponse data)
	{
		if (mShowLoadingDialog)
		{
			mContext.runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					mProgressDialog.dismiss();
				}
			});
		}

		// Sending back the results
		mRestLoaderObserver.onReceiveResult(data, mWebserviceEnumMethod);
	}

}
