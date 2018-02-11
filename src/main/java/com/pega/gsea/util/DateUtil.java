package com.pega.gsea.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

    public static boolean isWithinSecs(AlertData a1, AlertData a2, int secs) {
        long deltaInSec = Math.abs(a1.getTheDate().getTime() - a2.getTheDate().getTime()) / 1000;
        if (deltaInSec > secs) {
            return false;
        } else {
            return true;
        }
    }

    public static String formatForTZ(Date date, String timezone) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        TimeZone timeZone = TimeZone.getTimeZone(timezone);
        cal.setTimeZone(timeZone);
        DateFormat df = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss z");
        df.setTimeZone(cal.getTimeZone());
        String retValue = df.format(cal.getTime());
        return retValue;
    }

    public static Date changeTimezone(Date date, String timezone) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        TimeZone timeZone = TimeZone.getTimeZone(timezone);
        cal.setTimeZone(timeZone);
        return cal.getTime();
    }

    public static void main(String[] args) {
        Date startDate = null;
        Date endDate = null;
        //Sun Oct 03 2010 00:00:00 GMT-0400 (Eastern Daylight Time)
        DateFormat df = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss Z");
        try {
            startDate = df.parse("Sun Oct 03 2010 00:00:00 -0400");
            //System.out.println(startDate.toGMTString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
