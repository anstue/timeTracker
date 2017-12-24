package com.ti_zero.com.apptime.helper;

import android.util.Log;

/**
 * Created by uni on 12/23/17.
 */

public class Logging {


    public static void logError(LogTag tag, String message) {
        Log.e(tag.getKey(), message);
    }

    public static void logInfo(LogTag tag, String message) {
        Log.i(tag.getKey(), message);
    }

    public static void logDebug(LogTag tag, String message) {
        Log.d(tag.getKey(), message);
    }
}
