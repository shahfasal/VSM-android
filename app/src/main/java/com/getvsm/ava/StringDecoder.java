package com.getvsm.ava;

import java.net.URLDecoder;

public class StringDecoder {
	public static String decode(String encodedString)  {
		
		String result=new String();
		try
		{
			result=URLDecoder.decode(encodedString, "UTF-8");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			return result;
		}
	}
}
