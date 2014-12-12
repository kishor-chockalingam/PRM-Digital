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
package com.hybris.mobile;

/**
 * Constants internal to Hybris web services
 * 
 * @author philip
 * 
 */
public final class InternalConstants
{
	public static final String PRODUCT_OPTION_BASIC = "BASIC";
	public static final String PRODUCT_OPTION_DESCRIPTION = "DESCRIPTION";
	public static final String PRODUCT_OPTION_GALLERY = "GALLERY";
	public static final String PRODUCT_OPTION_CATEGORIES = "CATEGORIES";
	public static final String PRODUCT_OPTION_PROMOTIONS = "PROMOTIONS";
	public static final String PRODUCT_OPTION_STOCK = "STOCK";
	public static final String PRODUCT_OPTION_REVIEW = "REVIEW";
	public static final String PRODUCT_OPTION_CLASSIFICATION = "CLASSIFICATION";
	public static final String PRODUCT_OPTION_REFERENCES = "REFERENCES"; /// This causes an OCC error! (enum not defined)
	public static final String PRODUCT_OPTION_PRICE = "PRICE";
	public static final String PRODUCT_OPTION_VARIANT = "VARIANT_FULL";

	public static final int PICKER_RESULT_CODE = 10;

	// Application preferences
	public static final String KEY_PREF_BASE_URL = "web_services_base_url_preference";
	public static final String KEY_PREF_SPECIFIC_BASE_URL = "web_services_specific_base_url";
	public static final String KEY_PREF_TOGGLE_SPECIFIC_BASE_URL = "web_services_toggle_specific_base_url";
	public static final String KEY_PREF_CATALOG = "web_services_catalog_url_suffix_preference";
	public static final String KEY_PREF_LANGUAGE = "web_services_language_preference";
	public static final String KEY_PREF_GEOFENCING = "geofencing_capability";
	public static final String KEY_PREF_GEOFENCING_DEFAULT_RADIUS = "geofencing_default_radius";
	public static final String KEY_PREF_GEOFENCING_LATITUDE = "geofencing_latitude";
	public static final String KEY_PREF_GEOFENCING_LONGITUDE = "geofencing_longitude";
	public static final String KEY_PREF_GEOFENCING_RADIUS = "geofencing_radius";
	public static final String KEY_PREF_GEOFENCING_SPOOF_OVERRIDE = "geofencing_spoof_override";
	public static final String KEY_PREF_DEVICE_ID = "deviceid";
	public static final String KEY_PREF_UUID = "uuid";
	

	// Intents
	public static final String INTENT_NAME_BARCODE_SCANNER = "com.hybris.mobile.SCAN";

}
