package com.pega.gsea.util;
import java.util.*;
import java.text.*;

public class DateUtil {

	public static boolean isWithinSecs(AlertData a1, AlertData a2, int secs)
	{
		long deltaInSec = Math.abs(a1.getTheDate().getTime()-a2.getTheDate().getTime())/1000;
		if (deltaInSec > secs)
			return false;
		else
			return true;
	}
	
	public static String formatForTZ(Date d, String timezone)
	{
		//System.out.println("Converting timzone to  "+timezone);
		Calendar cal = Calendar.getInstance();
  		cal.setTime(d);
  		TimeZone t = TimeZone.getTimeZone(timezone);
  		//System.out.println(t.toString());
  		cal.setTimeZone(t);
  		DateFormat df = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss z");
  		df.setTimeZone(cal.getTimeZone());
  		String s =  df.format(cal.getTime());
  		//System.out.println(s);
  		return s;
	}
	
	public static Date changeTimezone(Date d , String timezone)
	{
		//System.out.println("Converting timzone to  "+timezone);
		Calendar cal = Calendar.getInstance();
  		cal.setTime(d);
  		TimeZone t = TimeZone.getTimeZone(timezone);
  		//System.out.println(t.toString());
  		cal.setTimeZone(t);
  		//System.out.println(s);
  		return cal.getTime();
	}
	 
	public static void main(String[] args)
	{
		Date startDate=null;
		Date endDate=null;
		//Sun Oct 03 2010 00:00:00 GMT-0400 (Eastern Daylight Time)
		DateFormat df = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss Z");
		try
		{ 
			startDate = df.parse("Sun Oct 03 2010 00:00:00 -0400"); 
			//System.out.println(startDate.toGMTString());
		}
		catch (Exception e)
        {
			e.printStackTrace();
        }
	}
	
}
