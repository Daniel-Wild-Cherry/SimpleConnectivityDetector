package com.wildcherryapps.simpleconnectivitydetector;

import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

/**
 * Implementation for API above 24
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
final class MainNetworkCallback extends ConnectivityManager.NetworkCallback {

    private static final String TAG = "ConnectivityDetector";

    private final ConnectivityListener mCallback;

    private boolean mConnected;

    MainNetworkCallback(ConnectivityListener listener) {
        mCallback = listener;
        mConnected = true;
    }

    @Override
    public void onAvailable(@NonNull Network network) {
        Log.d(TAG, "Network Available");
        if (mCallback != null)
            mCallback.onNetworkAvailable(!mConnected, network);
        mConnected = true;
    }

    @Override
    public void onLost(@NonNull Network network) {
        Log.d(TAG, "Network Unavailable");
        if (mCallback != null)
            mCallback.onNetworkUnavailable();
        mConnected = false;
    }

}
