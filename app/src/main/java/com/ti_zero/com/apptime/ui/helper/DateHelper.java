package com.ti_zero.com.apptime.ui.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by anstue on 1/6/18.
 */

public class DateHelper {

    private static String dateTimeFormat ="yyyy-MM-dd HH:mm";
    private static String dateFormat ="yyyy-MM-dd";
    private static String timeFormat ="HH:mm";
    private static SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat(dateTimeFormat, Locale.US);
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);
    private static SimpleDateFormat simpleTimeFormat = new SimpleDateFormat(timeFormat, Locale.US);

    public static Date getDateFromString(String date) throws ParseException {
        return simpleDateTimeFormat.parse(date);
    }

    public static String getStringFromDate(Date date) {
        return simpleDateFormat.format(date);
    }

    public static String getStringFromTime(Date date) {
        return simpleTimeFormat.format(date);
    }
}
