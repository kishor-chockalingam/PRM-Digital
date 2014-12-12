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

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;

import com.hybris.mobile.InternalConstants;
import com.hybris.mobile.WebserviceMethodEnums;
import com.hybris.mobile.loader.RESTLoader;
import com.hybris.mobile.loader.RESTLoaderObserver;
import com.hybris.mobile.loader.RESTLoaderResponse;
import com.hybris.mobile.model.cart.Cart;
import com.hybris.mobile.model.cart.CartEntry;
import com.hybris.mobile.model.cart.CartItem;
import com.hybris.mobile.model.cart.CartOrder;
import com.hybris.mobile.model.product.Product;
import com.hybris.mobile.query.QueryCart;
import com.hybris.mobile.query.QueryObjectId;
import com.hybris.mobile.query.QuerySingleProduct;
import com.hybris.mobile.utility.JsonUtils;
import com.hybris.mobile.utility.MenuUtil;


public class CartController extends Controller implements RESTLoaderObserver
{

	private Cart mCart;
	private List<CartItem> mModel;
	private Activity mContext;

	public static final int MESSAGE_CART_UPDATED = 1;
	public static final int MESSAGE_SHOW_QUANTITY_DIALOG = 2;
	public static final int MESSAGE_ORDER_PLACED_RESPONSE = 3;
	public static final int MESSAGE_ORDER_PLACED_ERROR = 4;

	public CartController(Cart cart, Activity context)
	{
		mContext = context;
		mCart = cart;
		mModel = new ArrayList<CartItem>();
	}

	public Cart getCart()
	{
		return mCart;
	}

	public List<CartItem> getModel()
	{
		return mModel;
	}

	public void deleteEntry(int cartEntryNum)
	{
		QueryObjectId queryCart = new QueryObjectId();
		queryCart.setObjectId(cartEntryNum + "");
		RESTLoader.execute(mContext, WebserviceMethodEnums.METHOD_DELETE_PRODUCT_IN_CART, queryCart, this, true, false);
	}

	public void updateCart(int cartEntryNumber, int quantity)
	{
		QueryCart queryCart = new QueryCart();
		queryCart.setCartEntryNumber(cartEntryNumber);
		queryCart.setQuantity(quantity);
		RESTLoader.execute(mContext, WebserviceMethodEnums.METHOD_UPDATE_PRODUCT_IN_CART, queryCart, this, true, false);
	}

	public void loadCart()
	{
		// We first load the profile of the user
		RESTLoader.execute(mContext, WebserviceMethodEnums.METHOD_GET_PROFILE, null, this, true, false);
	}

	public void loadQuantity(String productCode)
	{
		QuerySingleProduct query = new QuerySingleProduct();
		query.setProductCode(productCode);

		String[] options =
		{ InternalConstants.PRODUCT_OPTION_STOCK };

		query.setOptions(options);

		RESTLoader.execute(mContext, WebserviceMethodEnums.METHOD_GET_PRODUCT_WITH_CODE, query, this, true, true);
	}

	public void placeOrder()
	{
		RESTLoader.execute(mContext, WebserviceMethodEnums.METHOD_PLACE_ORDER, null, this, true, true);
	}

	@Override
	public void onReceiveResult(RESTLoaderResponse restLoaderResponse, WebserviceMethodEnums webserviceEnumMethod)
	{
		if (restLoaderResponse.getCode() == RESTLoaderResponse.SUCCESS)
		{
			String jsonResult = restLoaderResponse.getData();

			switch (webserviceEnumMethod)
			{

				case METHOD_GET_PROFILE:
					// We load the cart after identifying the user with the server
					RESTLoader.execute(mContext, WebserviceMethodEnums.METHOD_GET_CART, null, this, true, false);
					break;

				case METHOD_PLACE_ORDER:

					CartOrder cartOrder = JsonUtils.fromJson(jsonResult, CartOrder.class);

					if (StringUtils.isNotBlank(cartOrder.getCode()))
					{
						notifyOutboxHandlers(CartController.MESSAGE_ORDER_PLACED_RESPONSE, 0, 0, cartOrder);
					}
					else
					{
						notifyOutboxHandlers(CartController.MESSAGE_ORDER_PLACED_ERROR, 0, 0, jsonResult);
					}

					break;

				case METHOD_UPDATE_PRODUCT_IN_CART:
				case METHOD_DELETE_PRODUCT_IN_CART:
					loadCart();
					break;

				case METHOD_GET_CART:
					mCart = JsonUtils.fromJson(jsonResult, Cart.class);

					// Populate the cart entries
					if (mCart != null && mCart.getEntries() != null)
					{

						for (CartEntry cartEntry : mCart.getEntries())
						{
							if (cartEntry.getProduct() != null)
							{
								cartEntry.getProduct().populate();
							}
						}

					}

					mModel.clear();

					for (CartEntry entry : mCart.getEntries())
					{
						mModel.add(entry);
					}
					if (mCart.getPotentialProductPromotions() != null)
					{
						mModel.addAll(mCart.getPotentialProductPromotions());
					}
					if (mCart.getAppliedProductPromotions() != null)
					{
						mModel.addAll(mCart.getAppliedProductPromotions());
					}

					// Update cart value
					MenuUtil.setCartEmpty(mCart.getEntries().isEmpty());

					notifyOutboxHandlers(CartController.MESSAGE_CART_UPDATED, 0, 0, null);
					break;

				case METHOD_GET_PRODUCT_WITH_CODE:
					Product product = JsonUtils.fromJson(restLoaderResponse.getData(), Product.class);

					if (product != null && product.getStock() != null)
					{
						notifyOutboxHandlers(CartController.MESSAGE_SHOW_QUANTITY_DIALOG, 0, 0, product.getStock().getStockLevel());
					}
					break;

				default:
					break;
			}

		}
		else if (restLoaderResponse.getCode() == RESTLoaderResponse.ERROR)
		{

			switch (webserviceEnumMethod)
			{

			// theres an issue with OCC, returns error on deleting an entry even when its a success. Dont know what the actual response is like.
				case METHOD_GET_PROFILE:
					// We load the cart after identifying the user with the server
					RESTLoader.execute(mContext, WebserviceMethodEnums.METHOD_GET_CART, null, this, true, false);
					break;
				case METHOD_DELETE_PRODUCT_IN_CART:
				case METHOD_GET_CART:
					notifyOutboxHandlers(CartController.MESSAGE_CART_UPDATED, 0, 0, null);
					break;
				default:
					break;
			}
		}
	}

}
