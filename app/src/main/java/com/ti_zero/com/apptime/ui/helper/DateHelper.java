package com.ti_zero.com.apptime.ui.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by anstue on 1/6/18.
 */

public class DateHelper {

    private static String dateFormat="yyyy-MM-dd HH:mm";
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);

    public static Date getDateFromString(String date) throws ParseException {
        return simpleDateFormat.parse(date);
    }
}
