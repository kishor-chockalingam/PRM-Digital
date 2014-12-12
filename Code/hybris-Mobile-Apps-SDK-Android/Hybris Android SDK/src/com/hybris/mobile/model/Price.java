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
package com.hybris.mobile.model;

/**
 * A Price object
 *
 */
public class Price {

    private String currencyIso;
    private String formattedValue;
    private String priceType;
    private String value;

    public Price() {
    	this.currencyIso = "";
    	this.formattedValue = "00.00";
    	this.priceType = "";
    	this.value = "";
    }

    public Price(String currencyIso, String formattedValue, String priceType, String value) {
        this.currencyIso = currencyIso;
        this.formattedValue = formattedValue;
        this.priceType = priceType;
        this.value = value;
    }

    public String getCurrencyIso() {
        return currencyIso;
    }

    public void setCurrencyIso(String currencyIso) {
        this.currencyIso = currencyIso;
    }

    public String getFormattedValue() {
        return formattedValue;
    }

    public void setFormattedValue(String formattedValue) {
        this.formattedValue = formattedValue;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
