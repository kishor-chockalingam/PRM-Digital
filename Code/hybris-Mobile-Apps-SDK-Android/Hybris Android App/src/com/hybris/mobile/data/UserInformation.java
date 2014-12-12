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

import java.util.ArrayList;

import com.hybris.mobile.ExternalConstants;

public class UserInformation {

	private static ArrayList<String> sPreviousSearches;

	public static ArrayList<String> getPreviousSearches() {
		if (sPreviousSearches == null) {
			sPreviousSearches = new ArrayList<String>();
			// Load from user defaults
			
		}
		
		return sPreviousSearches;
	}

	
	public static void addPreviousSearch(String searchString) {
		// Trim if full
		if (getPreviousSearches().size() > ExternalConstants.MAX_PREVIOUS_SEARCHES) {
			getPreviousSearches().remove(0);
		}
		// Add
		getPreviousSearches().add(searchString);
		// Save
		
	}
	
	public static void clearPreviousSearches() {
		if (sPreviousSearches != null) {
			sPreviousSearches.clear();
			// Save
			
		}
	}
	
}
