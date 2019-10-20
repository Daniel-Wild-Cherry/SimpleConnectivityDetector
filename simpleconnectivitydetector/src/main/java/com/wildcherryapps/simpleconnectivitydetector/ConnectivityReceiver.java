package com.wildcherryapps.simpleconnectivitydetector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Implementation for API below 24
 */
public class ConnectivityReceiver extends BroadcastReceiver {

    private static final String TAG = "ConnectivityDetector";

    private List<ConnectivityListener> mListeners;
    private boolean mConnected;

    public ConnectivityReceiver() {
        mListeners = new LinkedList<>();
        mConnected = true;
    }

    public ConnectivityReceiver(ConnectivityListener listener) {
        this();
        mListeners.add(listener);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction() == null) return;
        if (!intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) return;

        boolean connected = mConnected;
        NetworkInfo info;

        if ((info = NetworkUtils.isAvailable(context)) != null) {
            Log.d(TAG, "Network Available");
            for (ConnectivityListener listener: mListeners) {
                if (listener != null)
                    listener.onNetworkAvailable(!connected, info);
            }
            mConnected = true;
        } else {
            Log.d(TAG, "Network Unavailable");
            for (ConnectivityListener listener: mListeners) {
                if (listener != null)
                    listener.onNetworkUnavailable();
            }
            mConnected = false;
        }

    }

}
