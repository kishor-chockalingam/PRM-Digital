package com.hybris.test;

public class TestUtils
{

	public static String generateRandomEmail() throws Exception
	{

		StringBuffer buffer = new StringBuffer();
		String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

		int charactersLength = characters.length();

		for (int i = 0; i < 10; i++)
		{
			double index = Math.random() * charactersLength;
			buffer.append(characters.charAt((int) index));
		}

		return buffer.toString() + "@hybris.com";
	}

}
