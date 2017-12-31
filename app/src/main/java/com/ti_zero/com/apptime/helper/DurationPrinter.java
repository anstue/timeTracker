package com.ti_zero.com.apptime.helper;

import java.util.Locale;

/**
 * Created by uni on 12/22/17.
 */

public class DurationPrinter {

    public static String printDuration(long duration) {
        float days = (float)duration /1000 /60 /60 /24;
        long hours = (long)(duration /1000.0 /60 /60) % 24;
        long minutes = (long)(duration /1000.0 /60 ) % 60;
        long seconds = (long)(duration /1000.0 ) % 60;
        if(days>=1) {
            return String.format(Locale.US, "%.1f days", days);
        } else if(hours>=1) {
            return String.format(Locale.US, "%02dh%02dm", hours, minutes);
        } else {
            return String.format(Locale.US, "%02dm%02ds", minutes, seconds);
        }
    }
}
