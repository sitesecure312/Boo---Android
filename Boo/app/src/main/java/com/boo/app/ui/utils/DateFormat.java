package com.boo.app.ui.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Created by razir on 5/26/2016.
 */
public class DateFormat {
    static DateTimeFormatter fmtIn = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    static DateTimeFormatter dateTimeFormatter = ISODateTimeFormat.dateOptionalTimeParser();


    public static DateTime parseDate(String date) {
        if (date == null) {
            return DateTime.now();
        }
        fmtIn = fmtIn.withZone(DateTimeZone.UTC);
        DateTime dateTime = fmtIn.parseDateTime(date);
        return dateTime;
    }

    public static String getISODateTime(DateTime date) {
        return fmtIn.print(date);
    }

    public static String getTimeExpired(DateTime date) {
        DateTime now= DateTime.now();
        DateTime oneHour = now.minusHours(1);
        if (date.isAfter(oneHour)) {
            Minutes minsObj= Minutes.minutesBetween(date, now);
            int min = minsObj.getMinutes();
            return min + "m";

        } else {
            DateTime oneDay = now.minusDays(1);
            if (date.isAfter(oneDay)) {
                Hours hoursObj = Hours.hoursBetween(date, now);
                int hours = hoursObj.getHours();
                return hours + "h";
            }
            DateTime oneMonth = now.minusMonths(1);
            if (date.isAfter(oneMonth)) {
                Days daysObj = Days.daysBetween(date, now);
                int days = daysObj.getDays();
                return days + "d";
            }
        }
        return "";
    }



}
