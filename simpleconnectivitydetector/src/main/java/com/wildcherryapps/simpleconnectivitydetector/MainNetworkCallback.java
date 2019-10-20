package com.wildcherryapps.simpleconnectivitydetector;

import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

/**
 * Implementation for API above 24
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
final class MainNetworkCallback extends ConnectivityManager.NetworkCallback {

    private final ConnectivityListener mListener;

    private boolean mConnected;

    MainNetworkCallback(ConnectivityListener listener) {
        mListener = listener;
        mConnected = true;
    }

    @Override
    public void onAvailable(@NonNull Network network) {
        Utils.log("Network Available");
        if (mListener != null)
            mListener.onNetworkAvailable(!mConnected, network);
        mConnected = true;
    }

    @Override
    public void onLost(@NonNull Network network) {
        Utils.log("Network Unavailable");
        if (mListener != null)
            mListener.onNetworkUnavailable();
        mConnected = false;
    }

}
