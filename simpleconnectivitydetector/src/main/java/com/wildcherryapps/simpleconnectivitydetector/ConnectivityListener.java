package com.wildcherryapps.simpleconnectivitydetector;

import android.net.Network;
import android.net.NetworkInfo;

/**
 * Used by the {@link ConnectivityDetector} framework to notify the listeners of network
 * availability changes.
 */
public interface ConnectivityListener {

    /**
     * Called when the network becomes available
     * Only called in devices with API level 24 AND ABOVE
     * @param backOnline    True if the network was previously unavailable
     * @param network       The available network
     */
    void onNetworkAvailable(boolean backOnline, Network network);

    /**
     * Called when the network becomes available
     * Only called in devices with API level BELOW 24
     * @param backOnline    True if the network was previously unavailable
     * @param networkInfo   The available network info
     */
    void onNetworkAvailable(boolean backOnline, NetworkInfo networkInfo);

    /**
     * Called when the network becomes unavailable
     */
    void onNetworkUnavailable();

}
