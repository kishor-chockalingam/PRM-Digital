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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.hybris.mobile.model.Facet;
import com.hybris.mobile.model.GenericNameCode;
import com.hybris.mobile.model.GenericValue;
import com.hybris.mobile.model.cart.CartDeliveryAddress;
import com.hybris.mobile.model.cart.CartDeliveryMode;
import com.hybris.mobile.model.cart.CartEntry;
import com.hybris.mobile.model.cart.CartPaymentInfo;


public final class JsonUtils
{

	private static final Gson gson = new Gson();

	/**
	 * Return true if the json has the property
	 * 
	 * @param json
	 * @param property
	 * @return
	 */
	public static boolean has(String json, String property)
	{
		JsonParser parser = new JsonParser();
		JsonObject jsonObject = parser.parse(json).getAsJsonObject();

		return jsonObject.get(property) != null && StringUtils.isNotBlank(jsonObject.get(property).getAsString());
	}

	/**
	 * Get the java object associated to the JSON string
	 * 
	 * @param json
	 * @param className
	 * @return
	 */
	public static <T> T fromJson(String json, Class<T> className)
	{
		return gson.fromJson(json, className);
	}

	/**
	 * Get the java object associated to the JSON string within the property name
	 * 
	 * @param json
	 * @param property
	 * @param className
	 * @return
	 */
	public static <T> T fromJson(String json, String property, Class<T> className)
	{
		JsonParser parser = new JsonParser();
		JsonObject jsonObject = parser.parse(json).getAsJsonObject();

		if (jsonObject != null && jsonObject.get(property) != null)
		{
			return gson.fromJson(jsonObject.get(property), className);
		}
		else
		{
			return null;
		}

	}

	/**
	 * Get the java object List associated to the JSON string
	 * 
	 * @param json
	 * @param className
	 * @return
	 */
	public static <T> List<T> fromJsonList(String json, Class<T> className)
	{

		// TODO - find a way so parameterize the TypeToken directly with the class specified as className
		Type listType = getAssociatedTypeFromClass(className);

		if (listType != null)
		{
			return gson.fromJson(json, listType);
		}
		else
		{
			return new ArrayList<T>();
		}

	}

	/**
	 * Get the java object List associated to the JSON string within the property name
	 * 
	 * @param json
	 * @param property
	 * @param className
	 * @return
	 */
	public static <T> List<T> fromJsonList(String json, String property, Class<T> className)
	{
		JsonParser parser = new JsonParser();
		JsonObject jsonObject = parser.parse(json).getAsJsonObject();

		Type listType = getAssociatedTypeFromClass(className);

		if (listType != null)
		{
			return gson.fromJson(jsonObject.get(property), listType);
		}
		else
		{
			return new ArrayList<T>();
		}

	}

	/**
	 * Convert the java object to a Json String
	 * 
	 * @param object
	 * @return
	 */
	public static String toJson(Object object)
	{
		return gson.toJson(object);
	}

	/**
	 * Return the Type associated to the Class
	 * 
	 * @param <T>
	 * 
	 * @param className
	 * @return
	 */
	private static <T> Type getAssociatedTypeFromClass(Class<T> className)
	{
		Type listType = null;

		if (className.equals(CartEntry.class))
		{
			listType = new TypeToken<List<CartEntry>>()
			{}.getType();
		}
		else if (className.equals(Facet.class))
		{
			listType = new TypeToken<List<Facet>>()
			{}.getType();
		}
		else if (className.equals(GenericNameCode.class))
		{
			listType = new TypeToken<List<GenericNameCode>>()
			{}.getType();
		}
		else if (className.equals(GenericValue.class))
		{
			listType = new TypeToken<List<GenericValue>>()
			{}.getType();
		}
		else if (className.equals(CartDeliveryMode.class))
		{
			listType = new TypeToken<List<CartDeliveryMode>>()
			{}.getType();
		}
		else if (className.equals(CartPaymentInfo.class))
		{
			listType = new TypeToken<List<CartPaymentInfo>>()
			{}.getType();
		}
		else if (className.equals(CartDeliveryAddress.class))
		{
			listType = new TypeToken<List<CartDeliveryAddress>>()
			{}.getType();
		}

		return listType;
	}

}
