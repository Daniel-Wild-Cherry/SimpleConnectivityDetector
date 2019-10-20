package com.wildcherryapps.simpleconnectivitydetector;

import android.net.Network;
import android.net.NetworkInfo;

/**
 * Convenience class for implementing only the necessary methods of {@link ConnectivityListener}
 */
public class ConnectivityAdapter implements ConnectivityListener {

    @Override
    public void onNetworkAvailable(boolean backOnline, Network network) {}

    @Override
    public void onNetworkAvailable(boolean backOnline, NetworkInfo networkInfo) {}

    @Override
    public void onNetworkUnavailable() {}

}
