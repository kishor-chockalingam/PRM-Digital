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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.hybris.mobile.logging.LoggingUtils;


public class DateUtil
{
	private static final String LOG_TAG = DateUtil.class.getSimpleName();
	private static final String FORMAT_ISO8601 = "yyyy-MM-dd'T'HH:mm:ssZ";
	private static final String ISO8601_Z = "Z";
	private static final String GMT_TIME_ZONE = "+00:00";

	/**
	 * Parse a Iso8601 compliant String into a Calendar
	 * 
	 * @param dateString
	 * @return
	 * @throws ParseException
	 */
	public static Calendar fromIso8601(String dateString)
	{
		// Replacing eventual Z (UTC) to the correct +00:00 recognized by SimpleDateFormat
		dateString = dateString.replace(ISO8601_Z, GMT_TIME_ZONE);

		// Removing the : from eventual timezone to be recognized by SimpleDateFormat
		try
		{
			dateString = dateString.substring(0, 22) + dateString.substring(23);
		}
		catch (IndexOutOfBoundsException e)
		{
			LoggingUtils.e(LOG_TAG, e.getLocalizedMessage(), null);
		}

		Calendar calendar = Calendar.getInstance();

		// Parsing the final date
		try
		{
			calendar.setTime(new SimpleDateFormat(FORMAT_ISO8601).parse(dateString));
		}
		catch (ParseException e)
		{
			LoggingUtils.e(LOG_TAG, e.getLocalizedMessage(), null);
		}

		return calendar;
	}
}
