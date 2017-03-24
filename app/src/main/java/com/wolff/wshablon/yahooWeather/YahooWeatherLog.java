package com.wolff.wshablon.yahooWeather;

import android.util.Log;

/**
 * Created by wolff on 24.03.2017.
 */
class YahooWeatherLog {

    public static final String TAG = "YWeatherGetter4a";
    public static boolean isDebuggable = true;

    public static void setDebuggable(final boolean isDebuggable) {
        YahooWeatherLog.isDebuggable = isDebuggable;
    }

    public static void d(final String tag, final String message) {
        if (!isDebuggable) return;
        Log.d(tag, message);
    }

    public static void d(final String message) {
        if (!isDebuggable) return;
        Log.d(TAG, message);
    }

    public static void printStack(final Exception e) {
        if (!isDebuggable) return;
        e.printStackTrace();
    }

}
