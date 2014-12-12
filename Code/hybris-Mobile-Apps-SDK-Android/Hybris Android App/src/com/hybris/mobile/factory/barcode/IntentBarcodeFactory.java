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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.os.Bundle;

import com.hybris.mobile.BarCodeSymbologyEnums;
import com.hybris.mobile.DataConstants;
import com.hybris.mobile.factory.barcode.impl.OrderDetailsIntentBarcodeImpl;
import com.hybris.mobile.factory.barcode.impl.ProductDetailsIntentBarcodeImpl;
import com.hybris.mobile.factory.barcode.impl.StoreLocatorIntentBarcodeImpl;
import com.hybris.mobile.utility.RegexUtil;


public class IntentBarcodeFactory
{

	/**
	 * Get the associated IntentBarcode according to the barcodeValue and barcodeSymbology. Different cases, see the file
	 * regex.xml to know how to identify each ones.
	 * 
	 * @param barcodeValue
	 * @param barcodeSymbology
	 * @param activity
	 * @return
	 */
	public static IntentBarcode getIntent(String barcodeValue, String barcodeSymbology, Activity activity)
	{

		IntentBarcode intentBarcode = null;

		// Identifying a product code
		String productCode = getProductCode(barcodeValue, barcodeSymbology);

		if (StringUtils.isNotEmpty(productCode))
		{
			intentBarcode = new ProductDetailsIntentBarcodeImpl(productCode, activity);
		}
		else
		{

			// Identifying a order id
			String orderId = RegexUtil.getOrderIdFromHybrisPattern(barcodeValue);

			if (StringUtils.isNotEmpty(orderId))
			{
				intentBarcode = new OrderDetailsIntentBarcodeImpl(orderId, activity);
			}
			else
			{

				// Identifying a location for a store (either user or store coordinates)
				List<String> positionValues = getStoreLocatorLocationValues(barcodeValue);

				// 3 values returned: longitude, latitude, radius
				// 2 cases:
				// - geolocation: just the radius is returned (index 2)
				// - store coordinates: we return the longitude (0), latitude (1) and radius (2)
				if (positionValues != null
						&& positionValues.size() == 3
						&& (StringUtils.isNotEmpty(positionValues.get(2)) || (StringUtils.isNotEmpty(positionValues.get(1)))
								&& StringUtils.isNotEmpty(positionValues.get(2))))
				{
					intentBarcode = new StoreLocatorIntentBarcodeImpl(positionValues.get(0), positionValues.get(1),
							positionValues.get(2), activity);
				}

			}

		}

		return intentBarcode;
	}


	/**
	 * Get the associated IntentBarcode according to the intentType
	 * 
	 * @param intentType
	 * @param intentExtras
	 * @param activity
	 * @return
	 */
	public static IntentBarcode getIntent(String intentType, Bundle intentExtras, Activity activity)
	{
		IntentBarcode intentBarcode = null;

		if (StringUtils.isNotEmpty(intentType))
		{

			if (StringUtils.equals(DataConstants.INTENT_ORDER_DETAILS, intentType))
			{
				intentBarcode = new OrderDetailsIntentBarcodeImpl(intentExtras.getString(DataConstants.ORDER_ID), activity);
			}

		}

		return intentBarcode;
	}

	/**
	 * Return a list of position (Longitude, Latitude, Radius) if the barcode value matches one of the pre-configured
	 * regular expression
	 * 
	 * @param barcodeValue
	 * @return
	 */
	private static List<String> getStoreLocatorLocationValues(String barcodeValue)
	{

		List<String> listPositionValues = new ArrayList<String>(3);

		// Checking if the is a geolocating pattern
		String radiusValue = RegexUtil.getStoreLocatorGeolocateFromHybrisPattern(barcodeValue);

		if (StringUtils.isNotEmpty(radiusValue))
		{
			listPositionValues.add("");
			listPositionValues.add("");
			listPositionValues.add(radiusValue);
		}
		else
		{

			// Store locator position value from the hybris pattern
			List<String> positionValues = RegexUtil.getStoreLocatorFromHybrisPattern(barcodeValue);

			if (positionValues != null && !positionValues.isEmpty() && positionValues.size() == 3)
			{
				listPositionValues.add(positionValues.get(0));
				listPositionValues.add(positionValues.get(1));
				listPositionValues.add(positionValues.get(2));
			}

		}

		return listPositionValues;

	}

	/**
	 * Return the product value if the barcode value matches one of the pre-configured regular expression
	 * 
	 * @param barcodeValue
	 * @param barcodeSymbology
	 * @return
	 */
	private static String getProductCode(String barcodeValue, String barcodeSymbology)
	{

		// For product codes, we have to remove the leading/trailing 0 of specific barcode symbologies

		// These barcodes have leading '0's and a trailing '0' that needs to be accounted for (stripped out).
		if (StringUtils.equals(barcodeSymbology, BarCodeSymbologyEnums.EAN_13.getCodeSymbology()))
		{
			barcodeValue = StringUtils.stripStart(barcodeValue, "0");
			barcodeValue = StringUtils.removeEnd(barcodeValue, "0");
		}
		// These barcodes have leading '0's that needs to be accounted for (stripped out).
		else if (StringUtils.equals(barcodeSymbology, BarCodeSymbologyEnums.ITF.getCodeSymbology()))
		{
			barcodeValue = StringUtils.stripStart(barcodeValue, "0");
		}
		// These barcodes have a trailing '0' that needs to be accounted for (stripped out).
		else if (StringUtils.equals(barcodeSymbology, BarCodeSymbologyEnums.EAN_8.getCodeSymbology()))
		{
			barcodeValue = StringUtils.removeEnd(barcodeValue, "0");
		}

		// Trying to get a product code from the barcode value
		return RegexUtil.getProductCode(barcodeValue);

	}
}
