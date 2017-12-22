package com.ti_zero.com.apptime.helper;

/**
 * Created by uni on 12/22/17.
 */

public class DurationPrinter {

    public static String printDuration(long duration) {
        long days = duration /1000 /60 /60 /24;
        long hours = (long)(duration /1000.0 /60 /60) % 24;
        long minutes = (long)(duration /1000.0 /60 ) % 60;
        long seconds = (long)(duration /1000.0 ) % 60;
        return String.format("%d, %02d:%02d:%02d", days, hours, minutes, seconds);
    }
}
