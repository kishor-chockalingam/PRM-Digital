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

import java.io.Serializable;

public class Option implements Serializable {
	private static final long serialVersionUID = -8318187589569508200L;

	private String value;
	private String title;
	private boolean defaultOption;
	public Option(){
		this("", "");
	}
	public Option(String value, String title){
		setValue(value);
		setTitle(title);
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public boolean isDefaultOption() {
		return defaultOption;
	}
	public void setDefaultOption(boolean defaultOption) {
		this.defaultOption = defaultOption;
	}
}
