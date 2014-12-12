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
package com.hybris.mobile.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Hashtable;

import org.apache.commons.lang3.StringUtils;

import android.app.DialogFragment;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.RatingBar;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.hybris.mobile.DataConstants;
import com.hybris.mobile.Hybris;
import com.hybris.mobile.InternalConstants;
import com.hybris.mobile.R;
import com.hybris.mobile.WebserviceMethodEnums;
import com.hybris.mobile.adapter.GalleryAdapter;
import com.hybris.mobile.fragment.ClassificationListFragment;
import com.hybris.mobile.fragment.QuantityDialogFragment;
import com.hybris.mobile.fragment.QuantityDialogFragment.QuantityDialogListener;
import com.hybris.mobile.fragment.ReviewDialogFragment;
import com.hybris.mobile.fragment.TextDialogFragment;
import com.hybris.mobile.loader.RESTLoader;
import com.hybris.mobile.loader.RESTLoaderObserver;
import com.hybris.mobile.loader.RESTLoaderResponse;
import com.hybris.mobile.logging.LoggingUtils;
import com.hybris.mobile.model.product.Product;
import com.hybris.mobile.model.product.ProductStockLevelStatus;
import com.hybris.mobile.nfc.NFCUtil;
import com.hybris.mobile.query.QueryCart;
import com.hybris.mobile.query.QuerySingleProduct;
import com.hybris.mobile.utility.JsonUtils;
import com.hybris.mobile.utility.MenuUtil;
import com.hybris.mobile.utility.RegexUtil;
import com.hybris.mobile.utility.StringUtil;


public abstract class AbstractProductDetailActivity extends HybrisActivity implements QuantityDialogListener, RESTLoaderObserver
{

	private static final String LOG_TAG = AbstractProductDetailActivity.class.getSimpleName();

	private int quantityToAddToCart = 1;
	protected final Handler mHandler = new Handler();
	protected Product mProduct;
	protected ShareActionProvider mShareActionProvider;

	// Required for Action menu handling
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		boolean handled = super.onOptionsItemSelected(item);
		if (!handled)
		{
			// Put custom menu items handlers here
			switch (item.getItemId())
			{

			// NFC writing is for debug use only 
				case R.id.write_tag:
					if (NFCUtil.canNFC(this))
					{
						Intent writeIntent = new Intent(this, NFCWriteActivity.class);
						NdefMessage msg = getNDEF(mProduct.getCode());
						writeIntent.putExtra(NFCWriteActivity.NDEF_MESSAGE, msg);
						startActivity(writeIntent);
					}
					else
					{
						Toast.makeText(this, R.string.error_nfc_not_supported, Toast.LENGTH_LONG).show();
					}
					return true;
				case R.id.share:

					try
					{
						Intent sendIntent = new Intent();
						sendIntent.setAction(Intent.ACTION_SEND);
						sendIntent.putExtra(Intent.EXTRA_TEXT,
								mProduct.getName() + " - " + getString(R.string.nfc_url, URLEncoder.encode(mProduct.getCode(), "UTF-8")));
						sendIntent.setType("text/plain");
						startActivity(Intent.createChooser(sendIntent, getString(R.string.share_dialog_title)));
					}
					catch (UnsupportedEncodingException e)
					{
						LoggingUtils.e(LOG_TAG, "Error trying to encode product code to UTF-8. " + e.getLocalizedMessage(),
								Hybris.getAppContext());
					}

					return true;
				default:
					return false;
			}
		}
		return handled;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate menu
		MenuUtil.onCreateProductDetailsOptionsMenu(menu, this);
		menu.removeItem(R.id.search);

		if (!getResources().getBoolean(R.bool.nfc_write_enabled))
		{
			menu.removeItem(R.id.write_tag);
		}

		// Locate MenuItem with ShareActionProvider
		MenuItem item = menu.findItem(R.id.share);

		// Fetch and store ShareActionProvider
		setShareActionProvider((ShareActionProvider) item.getActionProvider());

		return true;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_detail);

		// Allow links in promotions label
		TextView promotionsTextView = (TextView) findViewById(R.id.textViewPromotion);
		promotionsTextView.setMovementMethod(LinkMovementMethod.getInstance());

	}

	/**
	 * Handle incoming intents. Do not load a product if we already have one.
	 */
	@Override
	protected void onResume()
	{
		super.onResume();

		String[] options =
		{ InternalConstants.PRODUCT_OPTION_BASIC, InternalConstants.PRODUCT_OPTION_CATEGORIES,
				InternalConstants.PRODUCT_OPTION_CLASSIFICATION, InternalConstants.PRODUCT_OPTION_DESCRIPTION,
				InternalConstants.PRODUCT_OPTION_GALLERY, InternalConstants.PRODUCT_OPTION_PRICE,
				InternalConstants.PRODUCT_OPTION_PROMOTIONS, InternalConstants.PRODUCT_OPTION_REVIEW,
				InternalConstants.PRODUCT_OPTION_STOCK, InternalConstants.PRODUCT_OPTION_VARIANT };

		String productCode = null;

		Intent intent = getIntent();

		// Direct Call
		if (intent.hasExtra(DataConstants.PRODUCT_CODE)) //direct call from search list for example
		{
			productCode = intent.getStringExtra(DataConstants.PRODUCT_CODE);
		}
		// NFC Call
		else if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) //NFC 
		{
			Tag tag = getIntent().getExtras().getParcelable(NfcAdapter.EXTRA_TAG);

			Ndef ndef = Ndef.get(tag);
			NdefMessage message = ndef.getCachedNdefMessage();

			NdefRecord record = message.getRecords()[0];
			if (record.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(record.getType(), NdefRecord.RTD_URI))
			{
				productCode = RegexUtil.getProductCode(new String(record.getPayload(), 1, record.getPayload().length - 1));
			}
		}
		// Call from another application (QR Code) 
		else if (StringUtils.equals(intent.getAction(), Intent.ACTION_VIEW))
		{
			productCode = RegexUtil.getProductCode(intent.getDataString());
		}

		if (StringUtils.isNotEmpty(productCode))
		{
			this.enableAndroidBeam(productCode);
		}

		// Only load if we don't have a product already
		if (mProduct == null)
		{
			populateProduct(productCode, options);
		}

		invalidateOptionsMenu();
	}

	/**
	 * Enables Android Beam based NFC Sharing for the specified product code
	 * 
	 * @param id
	 */
	private void enableAndroidBeam(String productCode)
	{
		if (NFCUtil.canNFC(this))
		{
			NdefMessage msg = getNDEF(productCode);
			NfcAdapter.getDefaultAdapter(this).setNdefPushMessage(msg, this);
		}
	}

	/**
	 * Get the NDEF for a product code
	 * 
	 * @param productCode
	 * @return
	 */
	private NdefMessage getNDEF(String productCode)
	{
		NdefMessage msg = null;
		try
		{
			NdefRecord uriRecord;
			uriRecord = NdefRecord.createUri(getString(R.string.nfc_url, URLEncoder.encode(productCode, "UTF-8")));
			NdefRecord appRecord = NdefRecord.createApplicationRecord(getPackageName());
			msg = new NdefMessage(new NdefRecord[]
			{ uriRecord, appRecord });
		}
		catch (UnsupportedEncodingException e)
		{
			LoggingUtils.e(LOG_TAG, "Error trying to encode product code to UTF-8. " + e.getLocalizedMessage(),
					Hybris.getAppContext());
		}

		return msg;
	}

	/**
	 * Populates the mProduct with the options given, replacing the current mProduct object
	 * 
	 * @param productCode
	 * @param options
	 * @param startTimeOrZero
	 *           if != 0, it will be used to display the progress dialog for a minimum time (1s)
	 */
	private void populateProduct(String productCode, String[] options)
	{
		showLoadingDialog(true);

		QuerySingleProduct query = new QuerySingleProduct();
		query.setProductCode(productCode);
		query.setOptions(options);
		RESTLoader.execute(this, WebserviceMethodEnums.METHOD_GET_PRODUCT_WITH_CODE, query, this, true, false);
	}

	/**
	 * Show an image in a new activity
	 * 
	 * @param position
	 *           the index of the image
	 */
	private void viewImage(int position)
	{
		Intent viewImage = new Intent(this, ImageZoomActivity.class);
		Hashtable<String, String> urls = mProduct.getGalleryImageURLs().get(position);
		viewImage.putExtra("url", urls.get("zoom"));
		startActivity(viewImage);
	}

	/**
	 * Refresh the UI with data from the product
	 */
	public void updateUI()
	{

		// Title
		this.setTitle(mProduct.getName());

		// Images
		if (mProduct.getGalleryImageURLs() != null)
		{
			GalleryAdapter adapter = new GalleryAdapter(this, mProduct.getGalleryImageURLs());
			Gallery gallery = (Gallery) findViewById(R.id.galleryImages);
			gallery.setAdapter(adapter);

			// Set the onClick listener for the gallery
			gallery.setOnItemClickListener(new OnItemClickListener()
			{
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long arg3)
				{
					viewImage(position);

				}
			});
		}

		// Reviews
		TextView reviewTextView = (TextView) findViewById(R.id.textViewReviews);
		reviewTextView.setText(this.getResources().getString(R.string.show_reviews, mProduct.getReviews().size()));

		// Rating (stars)
		RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBarRating);
		if (mProduct.getAverageRating() != null)
		{
			ratingBar.setRating(mProduct.getAverageRating().floatValue());
		}

		// Promotions
		TextView promotionsTextView = (TextView) findViewById(R.id.textViewPromotion);
		if (mProduct.getPotentialPromotions().size() == 0)
		{
			promotionsTextView.setVisibility(View.GONE);
		}
		else
		{
			if (mProduct.getPotentialPromotions() != null && !mProduct.getPotentialPromotions().isEmpty())
			{
				promotionsTextView.setText(Html.fromHtml(Product.generatePromotionString(mProduct.getPotentialPromotions().get(0))));
				StringUtil.removeUnderlines((Spannable) promotionsTextView.getText());
			}

		}

		TextView priceTextView = (TextView) findViewById(R.id.textViewPrice);
		priceTextView.setText(mProduct.getPrice().getFormattedValue());

		// Description
		TextView descriptionTextView = (TextView) findViewById(R.id.textViewDescription);
		descriptionTextView.setText(mProduct.getDescription());

		// Stock level
		TextView stockLevelTextView = (TextView) findViewById(R.id.textViewStockLevel);
		String stockLevelText = mProduct.getStockLevelText(Hybris.getAppContext());
		if (mProduct.getStock().getStockLevel() > 0)
		{
			stockLevelText = mProduct.getStock().getStockLevel() + " "
					+ mProduct.getStockLevelText(Hybris.getAppContext()).toLowerCase();
		}
		stockLevelTextView.setText(stockLevelText);

		// Disable / Enable the add to cart button
		Button addToCartButton = (Button) findViewById(R.id.buttonAddToCart);

		Button quantityButton = (Button) findViewById(R.id.quantityButton);
		quantityButton.setText(getString(R.string.quantity_button, quantityToAddToCart));

		try
		{

			if (mProduct.getStock().getStockLevelStatus() != null
					&& StringUtils.equalsIgnoreCase(mProduct.getStock().getStockLevelStatus().getCode(),
							ProductStockLevelStatus.CODE_OUT_OF_STOCK))
			{
				addToCartButton.setEnabled(false);
				quantityButton.setEnabled(false);
				quantityButton.setText(R.string.quantity);
			}
			else
			{
				addToCartButton.setEnabled(true);
				quantityButton.setEnabled(true);
			}
		}
		catch (Exception e)
		{
		}

		invalidateOptionsMenu();
	}


	/**
	 * Show the reviews in a new activity
	 * 
	 * @param view
	 */
	public void showReviews(View view)
	{
		DialogFragment fragment = ReviewDialogFragment.newInstance(mProduct, this);
		fragment.show(getFragmentManager(), "review_fragment");
	}

	/**
	 * Add the product to the cart and reload to refresh data (e.g. promotions may change)
	 * 
	 * @param view
	 */
	public void addToCart(final View view)
	{
		QueryCart queryCart = new QueryCart();
		queryCart.setProductCode(mProduct.getCode());
		queryCart.setQuantity(quantityToAddToCart);
		showLoadingDialog(true);
		RESTLoader.execute(this, WebserviceMethodEnums.METHOD_ADD_PRODUCT_TO_CART, queryCart, this, true, false);
	}

	/**
	 * Show the description in a dialog
	 * 
	 * @param view
	 */
	public void showDescription(View view)
	{
		DialogFragment fragment = TextDialogFragment.newInstance(getString(R.string.generic_title_description),
				mProduct.getDescription(), this);
		fragment.show(getFragmentManager(), "text_fragment");
	}

	/**
	 * Show more information in a dialog
	 * 
	 * @param view
	 */
	public void showMoreInformation(View view)
	{
		DialogFragment fragment = ClassificationListFragment.newInstance(getString(R.string.more_information_label),
				mProduct.getClassifications(), this);
		fragment.show(getFragmentManager(), "classification_fragment");
	}

	/**
	 * Show delivery information in a dialog
	 * 
	 * @param view
	 */
	public void showDeliveryInformation(View view)
	{
		DialogFragment fragment = TextDialogFragment.newInstance(getString(R.string.delivery_information_label), "", this);
		fragment.show(getFragmentManager(), "text_fragment");
	}

	/**
	 * Show a number picker in a dialog
	 * 
	 * @param view
	 */
	public void showQuantityPicker(View view)
	{
		// Get the quantity
		if (mProduct.getStock().getStockLevel() > 0)
		{
			// Create and show the picker
			DialogFragment fragment = QuantityDialogFragment.newInstance(quantityToAddToCart, mProduct.getStock().getStockLevel(),
					this);
			fragment.show(getFragmentManager(), "quantity_fragment");
		}
	}

	/**
	 * Callback from quantity picker dialog
	 */
	@Override
	public void onFinishDialog(int quantity)
	{
		quantityToAddToCart = quantity;
		Button quantityButton = (Button) findViewById(R.id.quantityButton);
		quantityButton.setText(getString(R.string.quantity_button, quantity));
	}

	public ShareActionProvider getShareActionProvider()
	{
		return mShareActionProvider;
	}

	public void setShareActionProvider(ShareActionProvider mShareActionProvider)
	{
		this.mShareActionProvider = mShareActionProvider;
	}

	@Override
	public void onReceiveResult(RESTLoaderResponse restLoaderResponse, WebserviceMethodEnums webserviceEnumMethod)
	{
		if (restLoaderResponse.getCode() == RESTLoaderResponse.SUCCESS)
		{
			String jsonResult = restLoaderResponse.getData();

			switch (webserviceEnumMethod)
			{
				case METHOD_ADD_PRODUCT_TO_CART:
					ProductStockLevelStatus productStockLevelStatus = JsonUtils.fromJson(jsonResult, ProductStockLevelStatus.class);

					String errMsgToShow = null;

					if (StringUtils.equals(productStockLevelStatus.getStatusCode(), "success"))
					{
						// Repopulate the product
						String[] options =
						{ InternalConstants.PRODUCT_OPTION_PROMOTIONS };

						populateProduct(mProduct.getCode(), options);
						MenuUtil.setCartEmpty(false);
					}
					else
					{
						showLoadingDialog(false);
						errMsgToShow = getString(R.string.productDetails_couldNotAddToCart);

						if (StringUtils.endsWithIgnoreCase(productStockLevelStatus.getStatusCode(), "noStock"))
						{
							errMsgToShow += " (" + getString(R.string.stock_details_out_of_stock) + ")";
						}
						else
						{
							errMsgToShow += " (" + productStockLevelStatus.getStatusCode() + ")";
						}

						Toast.makeText(getApplicationContext(), errMsgToShow, Toast.LENGTH_SHORT).show();
						invalidateOptionsMenu();
					}

					break;

				case METHOD_GET_PRODUCT_WITH_CODE:
					showLoadingDialog(false);
					Product product = JsonUtils.fromJson(jsonResult, Product.class);
					product.populate();

					if (mProduct == null)
					{
						mProduct = product;
					}
					else
					{
						mProduct.addDetails(product);
					}

					updateUI();

				default:
					break;
			}

		}
		else if (restLoaderResponse.getCode() == RESTLoaderResponse.ERROR)
		{
			switch (webserviceEnumMethod)
			{
				case METHOD_GET_PRODUCT_WITH_CODE:
					showLoadingDialog(false);
					Toast.makeText(getApplicationContext(), R.string.error_product_not_found, Toast.LENGTH_LONG).show();
					break;

				default:
					break;
			}
		}

	}
}
