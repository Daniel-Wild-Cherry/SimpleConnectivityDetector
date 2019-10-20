package com.wildcherryapps.simpleconnectivitydetector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;

/**
 * Implementation for API below 24
 */
final class ConnectivityReceiver extends BroadcastReceiver {

    private ConnectivityListener mListener;
    private boolean mConnected;

    public ConnectivityReceiver() {
        mConnected = true;
    }

    public ConnectivityReceiver(ConnectivityListener listener) {
        this();
        mListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction() == null) return;
        if (!intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) return;

        boolean connected = mConnected;
        NetworkInfo info;

        if ((info = Utils.isNetworkAvailable(context)) != null) {
            Utils.log("Network Available");
            if (mListener != null)
                mListener.onNetworkAvailable(!connected, info);
            mConnected = true;
        } else {
            Utils.log("Network Unavailable");
            if (mListener != null)
                mListener.onNetworkUnavailable();
            mConnected = false;
        }

    }

}
