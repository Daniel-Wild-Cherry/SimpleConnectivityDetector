package com.wildcherryapps.simpleconnectivitydetector;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

final class Utils {

    private static final String TAG = "ConnectivityDetector";

    private Utils() {}

    static NetworkInfo isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (info != null && info.isConnected())
                return info;
        }
        return null;
    }

    static void log(String message, Object... args) {
        if (ConnectivityDetector.debug)
            Log.d(TAG, String.format(message, args));
    }

}
