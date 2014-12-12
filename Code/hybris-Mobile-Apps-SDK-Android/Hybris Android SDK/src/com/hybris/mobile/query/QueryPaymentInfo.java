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
package com.hybris.mobile.query;

import android.os.Parcel;
import android.os.Parcelable;


public class QueryPaymentInfo implements Query
{

	private String accountHolderName;
	private String cardNumber;
	private String cardType;
	private String expiryMonth;
	private String expiryYear;
	private boolean shouldSave;
	private boolean isDefault;
	private String paymentId;
	private QueryAddress queryAddress;

	public String getPaymentId()
	{
		return paymentId;
	}

	public void setPaymentId(String paymentId)
	{
		this.paymentId = paymentId;
	}

	public String getAccountHolderName()
	{
		return accountHolderName;
	}

	public void setAccountHolderName(String accountHolderName)
	{
		this.accountHolderName = accountHolderName;
	}

	public String getCardNumber()
	{
		return cardNumber;
	}

	public void setCardNumber(String cardNumber)
	{
		this.cardNumber = cardNumber;
	}

	public String getCardType()
	{
		return cardType;
	}

	public void setCardType(String cardType)
	{
		this.cardType = cardType;
	}

	public String getExpiryMonth()
	{
		return expiryMonth;
	}

	public void setExpiryMonth(String expiryMonth)
	{
		this.expiryMonth = expiryMonth;
	}

	public String getExpiryYear()
	{
		return expiryYear;
	}

	public void setExpiryYear(String expiryYear)
	{
		this.expiryYear = expiryYear;
	}

	public boolean isShouldSave()
	{
		return shouldSave;
	}

	public void setShouldSave(boolean shouldSave)
	{
		this.shouldSave = shouldSave;
	}

	public boolean isDefault()
	{
		return isDefault;
	}

	public void setDefault(boolean isDefault)
	{
		this.isDefault = isDefault;
	}

	public QueryAddress getQueryAddress()
	{
		return queryAddress;
	}

	public void setQueryAddress(QueryAddress queryAddress)
	{
		this.queryAddress = queryAddress;
	}

	public QueryPaymentInfo()
	{
	}

	public static final Parcelable.Creator<QueryPaymentInfo> CREATOR = new Parcelable.Creator<QueryPaymentInfo>()
	{
		@Override
		public QueryPaymentInfo createFromParcel(Parcel source)
		{
			return new QueryPaymentInfo(source);
		}

		@Override
		public QueryPaymentInfo[] newArray(int size)
		{
			return new QueryPaymentInfo[size];
		}
	};

	public QueryPaymentInfo(Parcel in)
	{
		accountHolderName = in.readString();
		cardNumber = in.readString();
		cardType = in.readString();
		expiryMonth = in.readString();
		expiryYear = in.readString();
		shouldSave = (Boolean) in.readValue(this.getClass().getClassLoader());
		isDefault = (Boolean) in.readValue(this.getClass().getClassLoader());
		paymentId = in.readString();
		queryAddress = in.readParcelable(this.getClass().getClassLoader());
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(accountHolderName);
		dest.writeString(cardNumber);
		dest.writeString(cardType);
		dest.writeString(expiryMonth);
		dest.writeString(expiryYear);
		dest.writeValue(shouldSave);
		dest.writeValue(isDefault);
		dest.writeString(paymentId);
		dest.writeParcelable(queryAddress, flags);
	}

}
