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


public class QueryCustomer implements Query
{
	private String firstName;
	private String lastName;
	private String titleCode;
	private String login;
	private String password;
	private String language;
	private String currency;

	public QueryCustomer()
	{
		super();
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public String getTitleCode()
	{
		return titleCode;
	}

	public void setTitleCode(String titleCode)
	{
		this.titleCode = titleCode;
	}

	public String getLogin()
	{
		return login;
	}

	public void setLogin(String login)
	{
		this.login = login;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getLanguage()
	{
		return language;
	}

	public void setLanguage(String language)
	{
		this.language = language;
	}

	public String getCurrency()
	{
		return currency;
	}

	public void setCurrency(String currency)
	{
		this.currency = currency;
	}

	public static final Parcelable.Creator<QueryCustomer> CREATOR = new Parcelable.Creator<QueryCustomer>()
	{
		@Override
		public QueryCustomer createFromParcel(Parcel source)
		{
			return new QueryCustomer(source);
		}

		@Override
		public QueryCustomer[] newArray(int size)
		{
			return new QueryCustomer[size];
		}
	};

	public QueryCustomer(Parcel in)
	{
		firstName = in.readString();
		lastName = in.readString();
		titleCode = in.readString();
		login = in.readString();
		password = in.readString();
		language = in.readString();
		currency = in.readString();
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(firstName);
		dest.writeString(lastName);
		dest.writeString(titleCode);
		dest.writeString(login);
		dest.writeString(password);
		dest.writeString(language);
		dest.writeString(currency);
	}

}
