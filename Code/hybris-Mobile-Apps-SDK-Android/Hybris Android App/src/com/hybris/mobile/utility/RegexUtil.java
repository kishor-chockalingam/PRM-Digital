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
package com.hybris.mobile.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.hybris.mobile.Hybris;
import com.hybris.mobile.R;


public class RegexUtil
{

	// Regex for an order code from the hybris pattern
	private static Pattern hybrisOrderIdPattern = Pattern.compile(Hybris.getAppContext().getString(
			R.string.regex_hybris_order_id_scan_pattern));

	// Regex for a store locator with geolocation from the hybris pattern
	private static Pattern hybrisStoreLocatorGeolocatePattern = Pattern.compile(Hybris.getAppContext().getString(
			R.string.regex_hybris_store_locator_geolocate_scan_pattern));

	// Regex for a store locator from the hybris pattern
	private static Pattern hybrisStoreLocatorPattern = Pattern.compile(Hybris.getAppContext().getString(
			R.string.regex_hybris_store_locator_scan_pattern));

	/**
	 * Try to identify and return a product code from the value and according to the different regex
	 * 
	 * @param value
	 * @return
	 */
	public static String getProductCode(String value)
	{
		String productCode = "";

		// Getting the hybris product code regex patterns
		String[] listRegexProducts = Hybris.getAppContext().getResources().getStringArray(R.array.regex_hybris_product);

		if (listRegexProducts != null)
		{

			boolean isProductMatching = false;
			int i = 0;

			// We continue while we have no match for the value
			while (!isProductMatching && i < listRegexProducts.length)
			{

				Pattern productPattern = Pattern.compile(listRegexProducts[i]);

				productCode = applyPattern(productPattern, value, 1).get(0);

				isProductMatching = StringUtils.isNotEmpty(productCode);

				i++;
			}

		}

		return productCode;

	}

	/**
	 * Return a order code from the value according to the regex "regex_hybris_order_code_scan_pattern" in regex.xml
	 * 
	 * @param value
	 * @return
	 */
	public static String getOrderIdFromHybrisPattern(String value)
	{
		return applyPattern(hybrisOrderIdPattern, value, 1).get(0);
	}

	/**
	 * Return a radius to use from the value according to the regex "regex_hybris_store_locator_scan_pattern" in
	 * regex.xml
	 * 
	 * @param value
	 * @return
	 */
	public static String getStoreLocatorGeolocateFromHybrisPattern(String value)
	{
		return applyPattern(hybrisStoreLocatorGeolocatePattern, value, 1).get(0);
	}

	/**
	 * Return a position identifier from the value according to the regex "regex_hybris_store_locator_scan_pattern" in
	 * regex.xml
	 * 
	 * @param value
	 * @return
	 */
	public static List<String> getStoreLocatorFromHybrisPattern(String value)
	{
		return applyPattern(hybrisStoreLocatorPattern, value, 3);
	}

	/**
	 * Apply a regex pattern on a value and return the match
	 * 
	 * @param pattern
	 * @param value
	 * @return
	 */
	private static List<String> applyPattern(Pattern pattern, String value, int numberOfElementToMatch)
	{

		List<String> returnValues = new ArrayList<String>();

		Matcher matcher = pattern.matcher(value);

		if (matcher.matches())
		{

			if (matcher.groupCount() == numberOfElementToMatch)
			{
				for (int i = 1; i <= numberOfElementToMatch; i++)
				{
					returnValues.add(matcher.group(i));
				}
			}

		}

		if (returnValues.isEmpty())
		{
			returnValues.add("");
		}

		return returnValues;

	}
}
