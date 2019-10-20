package com.wildcherryapps.simpleconnectivitydetector;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

final class NetworkUtils {

    private NetworkUtils() {}

    static NetworkInfo isAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (info != null && info.isConnected())
                return info;
        }
        return null;
    }

}
