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
package com.hybris.mobile.logging;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Random;


public class DefaultExceptionHandler implements UncaughtExceptionHandler
{

	private static final String LOG_TAG = DefaultExceptionHandler.class.getSimpleName();
	private UncaughtExceptionHandler defaultExceptionHandler;

	public DefaultExceptionHandler(UncaughtExceptionHandler pDefaultExceptionHandler)
	{
		defaultExceptionHandler = pDefaultExceptionHandler;
	}

	public void uncaughtException(Thread t, Throwable e)
	{
		final Writer result = new StringWriter();

		Random generator = new Random();
		int random = generator.nextInt(99999);
		String filename = RA.FILES_PATH + "/" + RA.APP_VERSION + "-" + Integer.toString(random) + ".stacktrace";

		try
		{
			BufferedWriter bos = new BufferedWriter(new FileWriter(filename));
			bos.write(RA.ANDROID_VERSION + "\n");
			bos.write(RA.PHONE_MODEL + "\n");
			bos.write(result.toString());
			bos.flush();
			bos.close();
		}
		catch (IOException ex)
		{
			LoggingUtils.e(LOG_TAG, "Error writing to file \"" + filename + "\". " + ex.getLocalizedMessage(), null);
		}

		defaultExceptionHandler.uncaughtException(t, e);
	}
}
