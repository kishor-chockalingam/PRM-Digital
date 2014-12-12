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

public class Country{
   	private String isocode;
   	private String name;

 	public String getIsocode(){
		return this.isocode;
	}
	public void setIsocode(String isocode){
		this.isocode = isocode;
	}
 	public String getName(){
		return this.name;
	}
	public void setName(String name){
		this.name = name;
	}
}
