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
package com.hybris.mobile.model.cart;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hybris.mobile.model.Price;


/**
 * Represents cart object returned from a Hybris store
 * 
 */
public class Cart
{

	private int totalItems;
	private int totalUnitCount;
	private long subTotalId;
	private long totalPriceId;
	private Price subTotal;
	private Price totalDiscounts;
	private Price totalPrice;
	private Price totalPriceWithTax;
	private Price totalTax;
	private Price productDiscounts;
	private Price deliveryCost;
	private ArrayList<CartPromotion> appliedProductPromotions;
	private ArrayList<CartPromotion> potentialProductPromotions;
	private CartDeliveryAddress deliveryAddress;
	private CartDeliveryMode deliveryMode;
	private CartPaymentInfo paymentInfo;
	private ArrayList<CartEntry> entries;

	public Cart()
	{
		this.entries = new ArrayList<CartEntry>();
	}


	public Price getDeliveryCost()
	{
		return deliveryCost;
	}


	public void setDeliveryCost(Price deliveryCost)
	{
		this.deliveryCost = deliveryCost;
	}


	public Price getTotalDiscounts()
	{
		return totalDiscounts;
	}


	public void setTotalDiscounts(Price totalDiscounts)
	{
		this.totalDiscounts = totalDiscounts;
	}


	public Price getTotalPriceWithTax()
	{
		return totalPriceWithTax;
	}


	public void setTotalPriceWithTax(Price totalPriceWithTax)
	{
		this.totalPriceWithTax = totalPriceWithTax;
	}


	public Price getTotalTax()
	{
		return totalTax;
	}


	public void setTotalTax(Price totalTax)
	{
		this.totalTax = totalTax;
	}


	public Price getProductDiscounts()
	{
		return productDiscounts;
	}


	public void setProductDiscounts(Price productDiscounts)
	{
		this.productDiscounts = productDiscounts;
	}

	public int getTotalItems()
	{
		return totalItems;
	}

	public void setTotalItems(int totalItems)
	{
		this.totalItems = totalItems;
	}

	public int getTotalUnitCount()
	{
		return totalUnitCount;
	}

	public void setTotalUnitCount(int totalUnitCount)
	{
		this.totalUnitCount = totalUnitCount;
	}

	public long getSubTotalId()
	{
		return subTotalId;
	}

	public void setSubTotalId(long subTotalId)
	{
		this.subTotalId = subTotalId;
	}


	public long getTotalPriceId()
	{
		return totalPriceId;
	}


	public void setTotalPriceId(long totalPriceId)
	{
		this.totalPriceId = totalPriceId;
	}


	public Price getSubTotal()
	{
		return subTotal;
	}

	public void setSubTotal(Price subTotal)
	{
		this.subTotal = subTotal;
	}

	public Price getTotalPrice()
	{
		return totalPrice;
	}

	public void setTotalPrice(Price totalPrice)
	{
		this.totalPrice = totalPrice;
	}

	public synchronized ArrayList<CartEntry> getEntries()
	{
		return entries;
	}

	public synchronized void resetEntries()
	{
		entries = null;
	}

	public CartDeliveryAddress getDeliveryAddress()
	{
		return deliveryAddress;
	}

	public void setDeliveryAddress(CartDeliveryAddress deliveryAddress)
	{
		this.deliveryAddress = deliveryAddress;
	}

	public CartDeliveryMode getDeliveryMode()
	{
		return deliveryMode;
	}


	public void setDeliveryMode(CartDeliveryMode deliveryMode)
	{
		this.deliveryMode = deliveryMode;
	}

	public CartPaymentInfo getPaymentInfo()
	{
		return paymentInfo;
	}

	public void setPaymentInfo(CartPaymentInfo paymentInfo)
	{
		this.paymentInfo = paymentInfo;
	}

	public ArrayList<CartPromotion> getAppliedProductPromotions()
	{
		return appliedProductPromotions;
	}

	public void setAppliedProductPromotions(ArrayList<CartPromotion> appliedPromotions)
	{
		this.appliedProductPromotions = appliedPromotions;
	}

	public ArrayList<CartPromotion> getPotentialProductPromotions()
	{
		return potentialProductPromotions;
	}

	public void setPotentialProductPromotions(ArrayList<CartPromotion> potentialPromotions)
	{
		this.potentialProductPromotions = potentialPromotions;
	}

	/**
	 * This method parses the messages sent from OCC to create HTML with links recognised by the app. Note this code is
	 * brittle and relies on very specific formatting in OCC. This method is specifoc to cart promotions.
	 * 
	 * @param p
	 *           The product
	 * @return the HTML string to place as the promotions text
	 */
	public static String generatePromotionString(CartPromotion promotionsData)
	{

		// Always use the description since fired messages not reliable
		String promotionsString = promotionsData.getDescription();
		/*
		 * Builds links in the form http://m.hybris.com/123456 N.B. iOS builds links in the form appName://product/123456
		 * with the title as the product name
		 */
		ArrayList<String> links = new ArrayList<String>();
		ArrayList<String> codes = new ArrayList<String>();
		ArrayList<String> productNames = new ArrayList<String>();
		Pattern codePattern = Pattern.compile("[0-9]{6,7}");
		Scanner s1 = new Scanner(promotionsString);
		s1.useDelimiter("<a href=");
		while (s1.hasNext())
		{
			String str = s1.next();
			if (str.startsWith("$config"))
			{
				Scanner s2 = new Scanner(str);
				s2.useDelimiter("</a>");
				String link = s2.next();
				links.add(link);
			}
		}
		for (String link : links)
		{
			Scanner s3 = new Scanner(link);
			s3.useDelimiter("<b>");
			s3.next();
			while (s3.hasNext())
			{
				String str = s3.next();
				if (!str.startsWith("$"))
				{
					Scanner s4 = new Scanner(str);
					s4.useDelimiter("</b>");
					String name = s4.next();
					productNames.add(name);
				}
			}
			// Find the codes
			Matcher m = codePattern.matcher(link);
			while (m.find())
			{
				String s = m.group();
				codes.add(s);
			}
		}
		// Build the new links
		int position = 0;
		for (String link : links)
		{
			promotionsString = promotionsString.replace(link,
					String.format("\"http://m.hybris.com/%s\">%s", codes.get(position), productNames.get(position)));
			position++;
		}


		return promotionsString;
	}
}
