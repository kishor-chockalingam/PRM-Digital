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
package com.hybris.mobile.controller;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.os.Message;


public class Controller
{

	private final List<Handler> outboxHandlers = new ArrayList<Handler>();

	public final void addOutboxHandler(Handler handler)
	{
		outboxHandlers.add(handler);
	}

	public final void removeOutboxHandler(Handler handler)
	{
		outboxHandlers.remove(handler);
	}

	protected final void notifyOutboxHandlers(int what, int arg1, int arg2, Object obj)
	{
		if (!outboxHandlers.isEmpty())
		{
			for (Handler handler : outboxHandlers)
			{
				Message msg = Message.obtain(handler, what, arg1, arg2, obj);
				msg.sendToTarget();
			}
		}
	}
}
