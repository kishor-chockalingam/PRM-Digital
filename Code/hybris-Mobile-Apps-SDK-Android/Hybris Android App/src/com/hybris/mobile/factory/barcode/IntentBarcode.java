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
package com.hybris.mobile.factory.barcode;

import android.os.Handler;


public interface IntentBarcode
{
	/**
	 * Method that start the activity associated with the intent
	 */
	public void startActivity();

	/**
	 * Method to check the availability of the data associated with this intent
	 * 
	 * @param handler
	 */
	public void checkDataAvailability(Handler handler);

}
