package com.wolff.wshablon.yahooWeather;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by wolff on 24.03.2017.
 */

public class NetworkUtils {

    private static NetworkUtils sInstance = new NetworkUtils();

    public static NetworkUtils getInstance() {
        return sInstance;
    }

    public static boolean isConnected(final Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }
}
