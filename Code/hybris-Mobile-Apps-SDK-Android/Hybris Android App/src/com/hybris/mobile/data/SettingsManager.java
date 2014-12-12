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
package com.hybris.mobile.data;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.dd.plist.NSArray;
import com.dd.plist.NSDictionary;
import com.dd.plist.PropertyListParser;
import com.hybris.mobile.Hybris;
import com.hybris.mobile.logging.LoggingUtils;


public class SettingsManager
{
	private static final String LOG_TAG = SettingsManager.class.getSimpleName();

	public static List<Setting> getSettings(InputStream data)
	{
		List<Setting> result = new ArrayList<Setting>();
		try
		{
			NSDictionary rootDict = (NSDictionary) PropertyListParser.parse(data);
			Hashtable<String, Object> objectInfo = new Hashtable<String, Object>();
			objectInfo.put("name", "Root Category");
			result = SettingsManager.processDictionary(rootDict);
		}
		catch (Exception ex)
		{
			LoggingUtils.e(LOG_TAG, "Error parsing data \"" + data.toString() + "\". " + ex.getLocalizedMessage(),
					Hybris.getAppContext());
		}
		return result;
	}

	private static List<Setting> processDictionary(NSDictionary data)
	{
		List<Setting> result = new ArrayList<Setting>();
		String[] rootKeys = data.allKeys();
		for (String name : rootKeys)
		{
			Object obj = data.objectForKey(name);
			if (obj instanceof NSArray)
			{
				NSArray a = (NSArray) obj;
				for (int i = 0; i < a.count(); i++)
				{
					if (a.objectAtIndex(i) instanceof NSDictionary)
					{
						NSDictionary dict = ((NSDictionary) a.objectAtIndex(i));
						if (dict.objectForKey("Titles") != null)
						{
							NSArray titles = ((NSArray) ((NSDictionary) a.objectAtIndex(i)).objectForKey("Titles"));
							NSArray values = ((NSArray) ((NSDictionary) a.objectAtIndex(i)).objectForKey("Values"));
							Setting setting = new Setting();
							setting.setName((((NSDictionary) a.objectAtIndex(i)).objectForKey("Name")).toString());
							setting.setTitle((((NSDictionary) a.objectAtIndex(i)).objectForKey("Title")).toString());
							setting.setDefaultValue((((NSDictionary) a.objectAtIndex(i)).objectForKey("DefaultValue")).toString());
							for (int j = 0; j < titles.count(); j++)
							{
								Option option = new Option();
								option.setTitle(titles.objectAtIndex(j).toString());
								option.setValue(values.objectAtIndex(j).toString());
								if (option.getValue().equals(setting.getDefaultValue()))
								{
									option.setDefaultOption(true);
								}
								setting.getOptions().add(option);
							}
							result.add(setting);
						}
					}
				}
			}
		}
		return result;
	}
}
