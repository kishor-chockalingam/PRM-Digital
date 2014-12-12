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

public enum BarCodeSymbologyEnums
{
	EAN_8("EAN_8"), EAN_13("EAN_13"), ITF("ITF");

	private String codeSymbology;

	private BarCodeSymbologyEnums(String codeSymbology)
	{
		this.codeSymbology = codeSymbology;
	}

	public String getCodeSymbology()
	{
		return this.codeSymbology;
	}

}
