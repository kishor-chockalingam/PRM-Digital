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
import java.util.Hashtable;

import com.dd.plist.NSArray;
import com.dd.plist.NSDictionary;
import com.dd.plist.NSString;
import com.dd.plist.PropertyListParser;
import com.hybris.mobile.Hybris;
import com.hybris.mobile.logging.LoggingUtils;
import com.hybris.mobile.model.Category;


public class CategoryManager
{

	private static final String LOG_TAG = CategoryManager.class.getSimpleName();
	private static Category sRootCategory;

	public static Category getRootCategory()
	{
		return sRootCategory;
	}

	public static void reloadCategories(InputStream data)
	{
		try
		{
			NSDictionary rootDict = (NSDictionary) PropertyListParser.parse(data);
			Hashtable<String, Object> objectInfo = new Hashtable<String, Object>();
			objectInfo.put("name", "Root Category");
			sRootCategory = Category.createCategoryFromObjectInfo(objectInfo);
			CategoryManager.processDictionary(rootDict, sRootCategory);
		}
		catch (Exception e)
		{
			LoggingUtils.e(LOG_TAG, "Error parsing data \"" + data + "\". " + e.getLocalizedMessage(), Hybris.getAppContext());
		}
	}

	private static void processDictionary(NSDictionary data, Category parent)
	{
		String[] rootKeys = data.allKeys();
		for (String name : rootKeys)
		{

			Object obj = data.objectForKey(name);
			Category toBeParent = null;
			Hashtable<String, Object> objectInfo = new Hashtable<String, Object>();

			if (obj instanceof NSArray)
			{
				NSArray a = (NSArray) obj;
				for (int i = 0; i < a.count(); i++)
				{
					if (a.objectAtIndex(i) instanceof NSString)
					{
						objectInfo.put("name", name);
						objectInfo.put("searchTag", a.objectAtIndex(i).toString());

						if (parent != null)
						{
							objectInfo.put("parent", parent);
						}

						Category cat = Category.createCategoryFromObjectInfo(objectInfo);
						toBeParent = cat;
						break;
					}
				}

				if (toBeParent != null)
				{
					for (int i = 0; i < a.count(); i++)
					{
						Object innerObj = a.objectAtIndex(i);
						if (innerObj instanceof NSDictionary)
						{
							CategoryManager.processDictionary((NSDictionary) innerObj, toBeParent);
						}
					}
				}
			}
			else if (obj instanceof NSString)
			{
				objectInfo.put("name", name);
				objectInfo.put("searchTag", obj.toString());

				if (parent != null)
				{
					objectInfo.put("parent", parent);
				}

				Category.createCategoryFromObjectInfo(objectInfo);
			}

		}
	}
}
