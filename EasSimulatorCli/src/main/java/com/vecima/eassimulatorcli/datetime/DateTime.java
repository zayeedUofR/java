/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vecima.eassimulatorcli.datetime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
/**
 *
 * @author zayeed
 */
public class DateTime {
    public static String formatDateTime(String dateTime) throws ParseException {
        //Sun Oct 07 20:58:34 CST 2018
        return _12HourFormat(dateTime.substring(11, 16)) + " ON " + dateTime.substring(4, 10) + ", " + dateTime.substring(24, 28);
    }
    public static String getTime(String dateTime) throws ParseException {
        return _12HourFormat(dateTime.substring(11, 16));
    }
    public static String _12HourFormat(String time) throws ParseException {
        return LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm")).format(DateTimeFormatter.ofPattern("hh:mm a"));
    }
    public static int caluculateEventStartTime() throws ParseException {
        long referenceTime = 315986400L;   // Jan 06 1980 00:00:00.000  SCTE-18
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy HH:mm:ss.SSS zzz");
        String currentTime = dateFormat.format(today);
        // parse() parses text from the beginning of the given string to produce a date.
        Date date = dateFormat.parse(currentTime);
        // getTime() returns the number of milliseconds since January 1, 1970, 00:00:00 GMT represented by this Date object.
        long epochTime = date.getTime() / 1000;           //converting to seconds from mili
        if(epochTime > 4294967295L){                      // max 32 bit int = 4294967295L
            epochTime = 1542912499L;
        }
        
        return (int) Math.abs(epochTime - referenceTime);
        
    }
    
}
