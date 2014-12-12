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
package com.hybris.test;

import java.util.ArrayList;
import java.util.List;

import android.test.AndroidTestCase;

import com.hybris.mobile.InternalConstants;
import com.hybris.mobile.SDKSettings;
import com.hybris.mobile.data.HYOCCInterface;
import com.hybris.mobile.data.WebService;
import com.hybris.mobile.model.Facet;
import com.hybris.mobile.model.Price;
import com.hybris.mobile.model.Sort;
import com.hybris.mobile.model.product.Product;
import com.hybris.mobile.model.product.ProductOptionItem;
import com.hybris.mobile.model.product.ProductOptionStockLevelStatus;
import com.hybris.mobile.model.product.ProductsList;
import com.hybris.mobile.utility.JsonUtils;


public class ProductTests extends AndroidTestCase
{

	private static HYOCCInterface webService;
	private static String kLogin = "aeiou@hybris.com";
	private static String kPassword = "password";
	private static boolean setUpIsDone = false;

	public void login() throws Exception
	{
		webService.loginUser(kLogin, kPassword);
	}

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		Thread.sleep(1000);
		SDKSettings.setSettingValue(InternalConstants.KEY_PREF_CATALOG, "electronics/");

		if (!setUpIsDone)
		{
			WebService.initWebService(mContext);
			webService = WebService.getWebService();

			//			kLogin = TestUtils.generateRandomEmail();
			//
			//			// Register random customer for the tests
			//			webService.registerCustomer("test", "test", "mr", kLogin, kPassword);

			setUpIsDone = true;
		}

	}

	@Override
	protected void tearDown() throws Exception
	{
	}

	public void testSpellingSuggestion() throws Exception
	{
		assertTrue(!webService.spellingSuggestions("cafon").isEmpty());
	}

	public void testProducts() throws Exception
	{
		ArrayList<Product> allProducts = new ArrayList<Product>();

		// Get products - page size 20
		ProductsList productsList = JsonUtils.fromJson(webService.getProducts(null, null, null, null, 20, 0), ProductsList.class);

		assertTrue(productsList.getProducts().size() == 20);
		//its safe to cast
		List<Facet> facets = productsList.getFacets();
		assertTrue(facets.size() > 0);
		//its safe to cast
		List<Sort> sorts = productsList.getSorts();
		assertTrue(sorts.size() > 0);

		allProducts.addAll(productsList.getProducts());

		// Page 2
		productsList = JsonUtils.fromJson(webService.getProducts(null, null, null, null, 20, 1), ProductsList.class);

		assertTrue(productsList.getProducts().size() == 20);

		allProducts.addAll(productsList.getProducts());
		assertTrue(allProducts.size() == 40);

		// Fetch 50 products
		productsList = JsonUtils.fromJson(webService.getProducts(null, null, null, null, 50, 0), ProductsList.class);

		assertTrue(productsList.getProducts().size() == 50);

		// Specific search
		productsList = JsonUtils.fromJson(webService.getProducts("camera", null, null, null, 50, 0), ProductsList.class);

		// Check all are cameras
		//		for (Product p : productsList.getProducts())
		//		{
		//			assertTrue(p.getName().matches("(?i).*camera*"));
		//		}

		// Set a sort option
		productsList = JsonUtils.fromJson(webService.getProducts("camera",
				productsList.getSorts().get(productsList.getSorts().size() - 1).getCode(), null, null, 50, 0), ProductsList.class);

		assertTrue(productsList.getProducts().size() > 0);
	}

	public void testProductWithCode() throws Exception
	{
		// Correct code
		String options[] =
		{ "BASIC" };
		Product product = JsonUtils.fromJson(webService.getProductWithCode(options, "23355"), Product.class);

		assertTrue(!product.getCode().isEmpty());
	}

	public void testProductWithCodeIncorrect() throws Exception
	{
		// Incorrect code
		String options[] =
		{ "BASIC" };

		try
		{
			webService.getProductWithCode(options, "XXXXXsafsdfsdf4121");
		}
		catch (Exception e)
		{
			assertTrue(e != null);
		}

	}

	//	public void testProductVariantsWithCode() throws Exception
	//	{
	//
	//		SDKSettings.setSettingValue(InternalConstants.KEY_PREF_CATALOG, "apparel-uk/");
	//
	//		String options[] =
	//		{ InternalConstants.PRODUCT_OPTION_BASIC, InternalConstants.PRODUCT_OPTION_CATEGORIES,
	//				InternalConstants.PRODUCT_OPTION_CLASSIFICATION, InternalConstants.PRODUCT_OPTION_DESCRIPTION,
	//				InternalConstants.PRODUCT_OPTION_GALLERY, InternalConstants.PRODUCT_OPTION_PRICE,
	//				InternalConstants.PRODUCT_OPTION_PROMOTIONS, InternalConstants.PRODUCT_OPTION_REVIEW,
	//				InternalConstants.PRODUCT_OPTION_STOCK, InternalConstants.PRODUCT_OPTION_VARIANT };
	//
	//		Product product = JsonUtils.fromJson(webService.getProductWithCode(options, "300717444"), Product.class);
	//
	//		assertTrue(!product.getCode().isEmpty());
	//		assertEquals(11, product.getAllOptions().size());
	//
	//		assertEquals(
	//				getProductOptionItem("Size", "M", "size", "GBP", "BUY", "21.83", "£21.83", "300717444",
	//						"/Categories/Streetwear-women/T-Shirts-women/Her-Logo-SS-Women/p/300717444", null, null, "", "inStock",
	//						"instock", 32, true), product.getAllOptions().get(0));
	//		assertEquals(
	//				getProductOptionItem("Size", "L", "size", "GBP", "BUY", "21.83", "£21.83", "300717443",
	//						"/Categories/Streetwear-women/T-Shirts-women/Her-Logo-SS-Women/p/300717443", null, null, "", "outOfStock",
	//						"outofstock", 0, false), product.getAllOptions().get(1));
	//		assertEquals(
	//				getProductOptionItem("Size", "M", "size", "GBP", "BUY", "21.83", "£21.83", "300717444",
	//						"/Categories/Streetwear-women/T-Shirts-women/Her-Logo-SS-Women/p/300717444", null, null, "", "inStock",
	//						"instock", 32, false), product.getAllOptions().get(2));
	//		assertEquals(
	//				getProductOptionItem("Size", "S", "size", "GBP", "BUY", "21.83", "£21.83", "300717445",
	//						"/Categories/Streetwear-women/T-Shirts-women/Her-Logo-SS-Women/p/300717445", null, null, "", "inStock",
	//						"instock", 21, false), product.getAllOptions().get(3));
	//		assertEquals(
	//				getProductOptionItem(
	//						"Style",
	//						"sunflower",
	//						"style",
	//						"GBP",
	//						"FROM",
	//						"21.83",
	//						"£21.83",
	//						"122811_sunflower",
	//						"/Categories/Streetwear-women/T-Shirts-women/Her-Logo-SS-Women/p/122811_sunflower",
	//						"30Wx30H",
	//						"/medias/122811-5.jpg?context=bWFzdGVyOjo6aW1hZ2VzOjo6MTIyMzo6OmltYWdlL2pwZWc6OjppbWFnZXMvaDVhL2gwMS84Nzk2NjI0MDkzMjE0LmpwZzo6OjIwOWI4ZTFhOTY4YWZhZWRkYTYyZDM0OTQzYTQ5MzMyNDI3MmI4MTY3NTc4YmFmYmI3ZDJiNmM4MjFhZjJjYWI",
	//						"", "outOfStock", "outofstock", 0, true), product.getAllOptions().get(4));
	//		assertEquals(
	//				getProductOptionItem(
	//						"Style",
	//						"bright white",
	//						"style",
	//						"GBP",
	//						"FROM",
	//						"21.83",
	//						"£21.83",
	//						"122811_bright_white",
	//						"/Categories/Streetwear-women/T-Shirts-women/Her-Logo-SS-Women/p/122811_bright_white",
	//						"30Wx30H",
	//						"/medias/122811-11.jpg?context=bWFzdGVyOjo6aW1hZ2VzOjo6MTE0Mzo6OmltYWdlL2pwZWc6OjppbWFnZXMvaGYwL2hhOS84Nzk2NjI2MDU5Mjk0LmpwZzo6OjdmZjY2YzRjNWFjMTI1Y2YzY2Q5NzdhNmIyYTczN2NlOGI4MjQ3YTE1OTBkZGMyODdhM2JkZmQyMzY4YWRjZDI",
	//						"", "outOfStock", "outofstock", 0, false), product.getAllOptions().get(5));
	//		assertEquals(
	//				getProductOptionItem(
	//						"Style",
	//						"heather berry",
	//						"style",
	//						"GBP",
	//						"FROM",
	//						"21.83",
	//						"£21.83",
	//						"122811_heather_berry",
	//						"/Categories/Streetwear-women/T-Shirts-women/Her-Logo-SS-Women/p/122811_heather_berry",
	//						"30Wx30H",
	//						"/medias/122811-9.jpg?context=bWFzdGVyOjo6aW1hZ2VzOjo6MTE5MDo6OmltYWdlL2pwZWc6OjppbWFnZXMvaGFiL2hiNi84Nzk2NjI1NDAzOTM0LmpwZzo6OjgzNDdkZWVlMDA3YzZlOWU1ZTllNWFkZGMxMTU4Nzc2Njc1ODIwYzdlOThlMjdjODAwZDY4NTIxMWFhMDk4YjI",
	//						"", "outOfStock", "outofstock", 0, false), product.getAllOptions().get(6));
	//		assertEquals(
	//				getProductOptionItem(
	//						"Style",
	//						"heather peacock",
	//						"style",
	//						"GBP",
	//						"FROM",
	//						"21.83",
	//						"£21.83",
	//						"122811_heather_peacock",
	//						"/Categories/Streetwear-women/T-Shirts-women/Her-Logo-SS-Women/p/122811_heather_peacock",
	//						"30Wx30H",
	//						"/medias/122811-7.jpg?context=bWFzdGVyOjo6aW1hZ2VzOjo6MTI0NTo6OmltYWdlL2pwZWc6OjppbWFnZXMvaGE4L2hmMC84Nzk2NjI0NzQ4NTc0LmpwZzo6OmMyMGIxYTFhMDI0OTQ4NDFmMmRiMjUwMDgxOGFmNjA3OTkwMjFiYzMwN2ZiMThjYTUyOGYzZDg3ZWE4MWY0MDY",
	//						"", "outOfStock", "outofstock", 0, false), product.getAllOptions().get(7));
	//		assertEquals(
	//				getProductOptionItem(
	//						"Style",
	//						"heather sweet leaf",
	//						"style",
	//						"GBP",
	//						"FROM",
	//						"21.83",
	//						"£21.83",
	//						"122811_heather_sweet_leaf",
	//						"/Categories/Streetwear-women/T-Shirts-women/Her-Logo-SS-Women/p/122811_heather_sweet_leaf",
	//						"30Wx30H",
	//						"/medias/122811-1.jpg?context=bWFzdGVyOjo6aW1hZ2VzOjo6MTIxMTo6OmltYWdlL2pwZWc6OjppbWFnZXMvaDRlL2gwMi84Nzk2NjIyNzgyNDk0LmpwZzo6OmU1NTY4NTM0OWZjMzg0NmYzMzI5MzlhOTY1NTU0OTFlMTc5MmUzMDMyMWQ4ZWZjNjBhMDhkN2U1MjFkZTk2MDc",
	//						"", "outOfStock", "outofstock", 0, false), product.getAllOptions().get(8));
	//		assertEquals(
	//				getProductOptionItem(
	//						"Style",
	//						"sunflower",
	//						"style",
	//						"GBP",
	//						"FROM",
	//						"21.83",
	//						"£21.83",
	//						"122811_sunflower",
	//						"/Categories/Streetwear-women/T-Shirts-women/Her-Logo-SS-Women/p/122811_sunflower",
	//						"30Wx30H",
	//						"/medias/122811-5.jpg?context=bWFzdGVyOjo6aW1hZ2VzOjo6MTIyMzo6OmltYWdlL2pwZWc6OjppbWFnZXMvaDVhL2gwMS84Nzk2NjI0MDkzMjE0LmpwZzo6OjIwOWI4ZTFhOTY4YWZhZWRkYTYyZDM0OTQzYTQ5MzMyNDI3MmI4MTY3NTc4YmFmYmI3ZDJiNmM4MjFhZjJjYWI",
	//						"", "outOfStock", "outofstock", 0, false), product.getAllOptions().get(9));
	//		assertEquals(
	//				getProductOptionItem(
	//						"Style",
	//						"true black",
	//						"style",
	//						"GBP",
	//						"FROM",
	//						"21.83",
	//						"£21.83",
	//						"122811_true_black",
	//						"/Categories/Streetwear-women/T-Shirts-women/Her-Logo-SS-Women/p/122811_true_black",
	//						"30Wx30H",
	//						"/medias/122811-3.jpg?context=bWFzdGVyOjo6aW1hZ2VzOjo6MTIwMDo6OmltYWdlL2pwZWc6OjppbWFnZXMvaDkyL2hiNC84Nzk2NjIzNDM3ODU0LmpwZzo6OjJiNDlhMGJhNDE0NjY1NGI4OGQ5NTcyOGViYzA3ZDY1ODFkYzE2MTQwNmExNWViMTAxMjY4YjVjM2U1ZTZjZTg",
	//						"", "outOfStock", "outofstock", 0, false), product.getAllOptions().get(10));
	//	}

	private ProductOptionItem getProductOptionItem(String name, String value, String qualifier, String currency, String priceType,
			String price, String displayPrice, String productCode, String url, String imageFormat, String imageUrl,
			String stockLevelStatus, String stockLevelStatusCode, String stockLevelStatusCodeLowerCase, Integer stockLevel,
			boolean selected)
	{
		ProductOptionItem result = new ProductOptionItem();
		result.setName(name);
		result.setValue(value);
		result.setQualifier(qualifier);

		Price priceData = new Price();
		priceData.setCurrencyIso(currency);
		priceData.setPriceType(priceType);
		priceData.setValue(price);
		priceData.setFormattedValue(displayPrice);
		result.setPriceData(priceData);

		result.setImageFormat(imageFormat);
		result.setImageUrl(imageUrl);
		result.setCode(productCode);
		result.setUrl(url);

		ProductOptionStockLevelStatus productStockLevelStatus = new ProductOptionStockLevelStatus();
		productStockLevelStatus.setCode(stockLevelStatus);
		productStockLevelStatus.setCodeLowerCaseString(stockLevelStatusCodeLowerCase);

		result.setStockLevelStatus(productStockLevelStatus);
		result.setStockLevel(stockLevel);
		result.setSelectedOption(selected);

		return result;
	}

	private void assertEquals(ProductOptionItem expected, ProductOptionItem actual)
	{
		assertEquals(expected.getPriceData().getCurrencyIso(), actual.getPriceData().getCurrencyIso());
		assertEquals(expected.getPriceData().getFormattedValue(), actual.getPriceData().getFormattedValue());
		assertEquals(expected.getImageFormat(), actual.getImageFormat());
		assertEquals(expected.getImageUrl(), actual.getImageUrl());
		assertEquals(expected.getName(), actual.getName());
		assertEquals(expected.getPriceData().getValue(), actual.getPriceData().getValue());
		assertEquals(expected.getPriceData().getPriceType(), actual.getPriceData().getPriceType());
		assertEquals(expected.getCode(), actual.getCode());
		assertEquals(expected.getQualifier(), actual.getQualifier());
		assertEquals(expected.getUrl(), actual.getUrl());
		assertEquals(expected.getStockLevelStatus().getCode(), actual.getStockLevelStatus().getCode());
		assertEquals(expected.getStockLevelStatus().getCodeLowerCaseString(), actual.getStockLevelStatus().getCodeLowerCaseString());
		assertEquals(expected.getStockLevel(), actual.getStockLevel());
		assertEquals(expected.getValue(), actual.getValue());
		assertEquals(expected.isSelectedOption(), actual.isSelectedOption());
	}
}
