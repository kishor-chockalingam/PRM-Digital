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
package com.hybris.mobile.test.unit;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import android.test.ActivityInstrumentationTestCase2;

import com.hybris.mobile.BarCodeSymbologyEnums;
import com.hybris.mobile.activity.BarCodeScannerActivity;
import com.hybris.mobile.factory.barcode.IntentBarcode;
import com.hybris.mobile.factory.barcode.IntentBarcodeFactory;
import com.hybris.mobile.factory.barcode.impl.OrderDetailsIntentBarcodeImpl;
import com.hybris.mobile.factory.barcode.impl.ProductDetailsIntentBarcodeImpl;
import com.hybris.mobile.factory.barcode.impl.StoreLocatorIntentBarcodeImpl;


public class IntentBarcodeFactoryTests extends ActivityInstrumentationTestCase2<BarCodeScannerActivity>
{

	private Map<String, Map<String, String>> mapProductBarcodes = new HashMap<String, Map<String, String>>();
	private Map<String, Map<String, String>> mapOrderBarcodes = new HashMap<String, Map<String, String>>();
	private Map<String, Map<String, String>> mapStoreLocatorBarcodes = new HashMap<String, Map<String, String>>();
	private static final String OK = "OK";
	private static final String KO = "KO";


	@SuppressWarnings("deprecation")
	public IntentBarcodeFactoryTests()
	{
		super("com.hybris.mobile", BarCodeScannerActivity.class);
	}


	/**
	 * Construct the list of the different barcodes
	 */
	@Override
	protected void setUp() throws Exception
	{
		super.setUp();

		// Good product formats
		Map<String, String> mapGoodFormatProducts = new LinkedHashMap<String, String>();
		mapGoodFormatProducts.put(BarCodeSymbologyEnums.EAN_13.getCodeSymbology(), "13820800");
		mapGoodFormatProducts.put("", "0000001300");
		mapGoodFormatProducts.put("", "http://sdasdsadasdasd.com:9130/4564613324");
		mapGoodFormatProducts.put("", "products://165465768");
		mapGoodFormatProducts.put("", "1311654820800");
		mapGoodFormatProducts.put(BarCodeSymbologyEnums.ITF.getCodeSymbology(), "1380");

		// Bad product formats
		Map<String, String> mapBadFormatProducts = new LinkedHashMap<String, String>();
		mapBadFormatProducts.put(BarCodeSymbologyEnums.EAN_13.getCodeSymbology(), "13das820800");
		mapBadFormatProducts.put("", "");
		mapBadFormatProducts.put("", "http://sdasdsadasdasd.com:9130/456ss4613324");
		mapBadFormatProducts.put("", "products://16s5465768");
		mapBadFormatProducts.put("", "productss://165465768");

		mapProductBarcodes.put(OK, mapGoodFormatProducts);
		mapProductBarcodes.put(KO, mapBadFormatProducts);


		// Good order formats
		Map<String, String> mapGoodFormatOrders = new LinkedHashMap<String, String>();
		mapGoodFormatOrders.put("", "orders://6646465");
		mapGoodFormatOrders.put("", "orders://66435486465");

		// Bad order formats
		Map<String, String> mapBadFormatOrders = new LinkedHashMap<String, String>();
		mapBadFormatOrders.put("", "ordersdd://6646465");
		mapBadFormatOrders.put("", "orders://66435ssd486465");

		mapOrderBarcodes.put(OK, mapGoodFormatOrders);
		mapOrderBarcodes.put(KO, mapBadFormatOrders);

		// Good store locator formats
		Map<String, String> mapGoodFormatStoreLocator = new LinkedHashMap<String, String>();
		mapGoodFormatStoreLocator.put("", "stores://?radius=4500");
		mapGoodFormatStoreLocator.put("", "stores://?radius=1500");
		mapGoodFormatStoreLocator.put("", "stores://?longitude=132&latitude=45&radius=4500");
		mapGoodFormatStoreLocator.put("", "stores://?longitude=41&latitude=21&radius=500");

		// Good store locator formats
		Map<String, String> mapBadFormatStoreLocator = new LinkedHashMap<String, String>();
		mapBadFormatStoreLocator.put("", "stores://?radius=s4500");
		mapBadFormatStoreLocator.put("", "stores://?radsdius=1500");
		mapBadFormatStoreLocator.put("", "stores://?longitude=132&latitude=45&radsius=4500");
		mapBadFormatStoreLocator.put("", "stores://?longitude=XX&latitude=21&radius=500");
		mapBadFormatStoreLocator.put("", "storesU://?longitude=132&latitude=45&radsius=4500");
		mapBadFormatStoreLocator.put("", "stores://?longitude=32&radius=500");

		mapStoreLocatorBarcodes.put(OK, mapGoodFormatStoreLocator);
		mapStoreLocatorBarcodes.put(KO, mapBadFormatStoreLocator);

	}

	/**
	 * Well formated barcodes for products
	 */
	public void testProductDetailsIntentBarcodeImplOK()
	{

		Entry<String, String> productEntry = getRandomBarcodeValue(mapProductBarcodes.get(OK));

		String barcodeValue = productEntry.getValue();
		String barcodeSymbology = productEntry.getKey();

		IntentBarcode intentBarcode = IntentBarcodeFactory.getIntent(barcodeValue, barcodeSymbology, getActivity());

		assertEquals(ProductDetailsIntentBarcodeImpl.class, intentBarcode.getClass());

	}

	/**
	 * Bad formated barcodes for products
	 */
	public void testProductDetailsIntentBarcodeImplKO()
	{

		Entry<String, String> productEntry = getRandomBarcodeValue(mapProductBarcodes.get(KO));

		String barcodeValue = productEntry.getValue();
		String barcodeSymbology = productEntry.getKey();

		IntentBarcode intentBarcode = IntentBarcodeFactory.getIntent(barcodeValue, barcodeSymbology, getActivity());

		assertNull(intentBarcode);

	}

	/**
	 * Well formated barcodes for orders
	 */
	public void testOrderDetailsIntentBarcodeImplOK()
	{

		Entry<String, String> productEntry = getRandomBarcodeValue(mapOrderBarcodes.get(OK));

		String barcodeValue = productEntry.getValue();
		String barcodeSymbology = productEntry.getKey();

		IntentBarcode intentBarcode = IntentBarcodeFactory.getIntent(barcodeValue, barcodeSymbology, getActivity());

		assertEquals(OrderDetailsIntentBarcodeImpl.class, intentBarcode.getClass());

	}

	/**
	 * Bad formated barcodes for orders
	 */
	public void testOrderDetailsIntentBarcodeImplKO()
	{

		Entry<String, String> productEntry = getRandomBarcodeValue(mapOrderBarcodes.get(KO));

		String barcodeValue = productEntry.getValue();
		String barcodeSymbology = productEntry.getKey();

		IntentBarcode intentBarcode = IntentBarcodeFactory.getIntent(barcodeValue, barcodeSymbology, getActivity());

		assertNull(intentBarcode);

	}

	/**
	 * Well formated barcodes for store location
	 */
	public void testStoreLocatorIntentBarcodeImplOK()
	{

		Entry<String, String> productEntry = getRandomBarcodeValue(mapStoreLocatorBarcodes.get(OK));

		String barcodeValue = productEntry.getValue();
		String barcodeSymbology = productEntry.getKey();

		IntentBarcode intentBarcode = IntentBarcodeFactory.getIntent(barcodeValue, barcodeSymbology, getActivity());

		assertEquals(StoreLocatorIntentBarcodeImpl.class, intentBarcode.getClass());

	}

	/**
	 * Bad formated barcodes for store location
	 */
	public void testStoreLocatorIntentBarcodeImplKO()
	{

		Entry<String, String> productEntry = getRandomBarcodeValue(mapStoreLocatorBarcodes.get(KO));

		String barcodeValue = productEntry.getValue();
		String barcodeSymbology = productEntry.getKey();

		IntentBarcode intentBarcode = IntentBarcodeFactory.getIntent(barcodeValue, barcodeSymbology, getActivity());

		assertNull(intentBarcode);

	}

	/**
	 * Get a random Barcode value and symbology from a list of barcodes
	 * 
	 * @param mapBarcodes
	 * @return
	 */
	private Entry<String, String> getRandomBarcodeValue(Map<String, String> mapBarcodes)
	{

		// Choosing a random number to iterate the map
		Random randomGenerator = new Random();
		int randomBarcode = randomGenerator.nextInt(mapBarcodes.size());
		int i = 0;

		Iterator<Entry<String, String>> iteratorMap = mapBarcodes.entrySet().iterator();
		Entry<String, String> currentEntry = iteratorMap.next();

		while (i < randomBarcode && iteratorMap.hasNext())
		{
			currentEntry = iteratorMap.next();
			i++;
		}

		return currentEntry;

	}
}
