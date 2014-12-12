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
import java.util.List;

public class Setting {
	private String name;
	private String title;
	private List<Option> options = new ArrayList<Option>();
	private String defaultValue;

	public Setting(){
		this("", "");
	}
	public Setting(String name) {
		this(name, "");
	}
	public Setting(String name, String defaultValue){
		setTitle(name);
		setDefaultValue(defaultValue);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<Option> getOptions(){
		return options;
	}
	public void setOptions(List<Option> options){
		this.options = options;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getDefaultTitle(){
		String result = "";
		for (Option option: options) {
			if (option.isDefaultOption()){
				result = option.getTitle();
				break;
			}
		}
		return result;
	}
}
