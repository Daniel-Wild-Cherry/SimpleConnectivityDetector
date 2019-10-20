package com.wildcherryapps.simpleconnectivitydetector;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * A utility class that handles internet connectivity detection for API levels both above
 * and below N (24), and uses a single {@link ConnectivityListener} callback to notify network availability.
 *
 * {@link ConnectivityReceiver} - Implementation for API levels below N.
 *                                Uses a BroadcastReceiver that's registered in the manifest nad
 *                                listens to "android.net.conn.CONNECTIVITY_CHANGE" action.
 *                                It is registered in the {@link #onStart()} method and unregistered
 *                                in the {@link #onStop()} method.
 *
 * {@link MainNetworkCallback}  - Implementation for API levels above N.
 *                                Uses a simple NetworkCallback.
 */
public final class ConnectivityDetector implements LifecycleObserver {

    private static final String TAG = "ConnectivityDetector";

    private Context mContext;
    private Lifecycle mLifecycle;
    private ConnectivityListener mCallback;

    private ConnectivityReceiver mConnectivityReceiverBelowApi24;

    public static <_ActivityImplementsConnectivityListener extends FragmentActivity & ConnectivityListener> void bind(_ActivityImplementsConnectivityListener obj) {
        bind(obj, obj);
    }

    public static <_FragmentImplementsConnectivityListener extends Fragment & ConnectivityListener> void bind(_FragmentImplementsConnectivityListener obj) {
        bind(obj, obj);
    }

    public static void bind(FragmentActivity activity, ConnectivityListener callback) {
        bind(activity, activity, callback);
    }

    public static void bind(Fragment fragment, ConnectivityListener callback) {
        bind(fragment.getContext(), fragment.getViewLifecycleOwner(), callback);
    }

    public static void bind(Context context, LifecycleOwner lifecycleOwner, ConnectivityListener callback) {
        Lifecycle lifecycle = lifecycleOwner.getLifecycle();
        ConnectivityDetector connectivityDetector = new ConnectivityDetector(context, lifecycle, callback);
        lifecycle.addObserver(connectivityDetector);
        connectivityDetector.onBound();
    }

    public static Builder create(Context context, LifecycleOwner lifecycleOwner) {
        return new Builder(context, lifecycleOwner);
    }

    public static Builder create(FragmentActivity activity) {
        return new Builder(activity, activity);
    }

    public static Builder create(Fragment fragment) {
        return new Builder(fragment.getContext(), fragment.getViewLifecycleOwner());
    }

    private ConnectivityDetector(Context context, Lifecycle lifecycle, ConnectivityListener callback) {
        mContext = context;
        mLifecycle = lifecycle;
        mCallback = callback;
    }

    // No need for an ON_CREATE event callback, this will get called upon binding
    private void onBound() {
        // Init NetworkCallback for API greater than 24
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Log.d(TAG, "API is GREATER than 24, we're registering a network callback");
            ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(CONNECTIVITY_SERVICE);
            if (cm != null)
                cm.registerDefaultNetworkCallback(new MainNetworkCallback(mCallback));
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void onStart() {
        // Init ConnectivityReceiver for API lower than 24
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Log.d(TAG, "API is LOWER than 24, we're registering a broadcast receiver");
            mConnectivityReceiverBelowApi24 = new ConnectivityReceiver(mCallback);
            mContext.registerReceiver(mConnectivityReceiverBelowApi24,
                    new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onStop() {
        // Destroy ConnectivityReceiver for API lower than 24
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N && mConnectivityReceiverBelowApi24 != null) {
            Log.d(TAG, "API is LOWER than 24, unregistering broadcast receiver");
            mContext.unregisterReceiver(mConnectivityReceiverBelowApi24);
            mConnectivityReceiverBelowApi24 = null;
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy() {
        // Destroy this bitch
        mLifecycle.removeObserver(this);
        Log.d(TAG, "ConnectivityDetector is destroyed");
    }

    @FunctionalInterface
    public interface OnNetworkAvailableFunction {
        void onNetworkAvailable();
    }

    @FunctionalInterface
    public interface OnNetworkAvailableBackOnlineFunction {
        void onNetworkAvailable(boolean backOnline);
    }

    @FunctionalInterface
    public interface OnNetworkAvailableAbove24Function {
        void onNetworkAvailable(boolean backOnline, Network network);
    }

    @FunctionalInterface
    public interface OnNetworkAvailableAbove24FunctionOnlyNetwork {
        void onNetworkAvaileble(Network network);
    }

    @FunctionalInterface
    public interface OnNetworkAvailableBelow24Function {
        void onNetworkAvailable(boolean backOnline, NetworkInfo networkInfo);
    }

    @FunctionalInterface
    public interface OnNetworkAvailableBelow24FunctionOnlyNetworkInfo {
        void onNetworkAvailable(NetworkInfo networkInfo);
    }

    @FunctionalInterface
    public interface OnNetworkUnavailableFunction {
        void onNetworkUnavailable();
    }

    public static final class Builder {

        private Context mContext;
        private LifecycleOwner mLifecycleOwner;
        private DefaultCallback mCallback;

        private Builder(Context context, LifecycleOwner lifecycleOwner) {
            mContext = context;
            mLifecycleOwner = lifecycleOwner;
            mCallback = new DefaultCallback();
        }

        public Builder onNetworkAvailable(OnNetworkAvailableFunction onNetworkAvailableFunction) {
            mCallback.onNetworkAvailableFunction = onNetworkAvailableFunction;
            return this;
        }

        public Builder onNetworkAvailable(OnNetworkAvailableBackOnlineFunction onNetworkAvailableBackOnlineFunction) {
            mCallback.onNetworkAvailableBackOnlineFunction = onNetworkAvailableBackOnlineFunction;
            return this;
        }

        public Builder onNetworkAvailableAboveApi24(OnNetworkAvailableAbove24Function onNetworkAvailableAbove24Function) {
            mCallback.onNetworkAvailableAbove24Function = onNetworkAvailableAbove24Function;
            return this;
        }

        public Builder onNetworkAvailableAboveApi24(OnNetworkAvailableAbove24FunctionOnlyNetwork onNetworkAvailableAbove24FunctionOnlyNetwork) {
            mCallback.onNetworkAvailableAbove24FunctionOnlyNetwork = onNetworkAvailableAbove24FunctionOnlyNetwork;
            return this;
        }

        public Builder onNetworkAvailableBelowApi24(OnNetworkAvailableBelow24Function onNetworkAvailableBelow24Function) {
            mCallback.onNetworkAvailableBelow24Function = onNetworkAvailableBelow24Function;
            return this;
        }

        public Builder onNetworkAvailableBelowApi24(OnNetworkAvailableBelow24FunctionOnlyNetworkInfo onNetworkAvailableBelow24FunctionOnlyNetworkInfo) {
            mCallback.onNetworkAvailableBelow24FunctionOnlyNetworkInfo = onNetworkAvailableBelow24FunctionOnlyNetworkInfo;
            return this;
        }

        public Builder onNetworkUnavailable(OnNetworkUnavailableFunction onNetworkUnavailableFunction) {
            mCallback.onNetworkUnavailableFunction = onNetworkUnavailableFunction;
            return this;
        }

        public void bind() {
            ConnectivityDetector.bind(mContext, mLifecycleOwner, mCallback);
        }

        private static final class DefaultCallback implements ConnectivityListener {

            private OnNetworkAvailableFunction onNetworkAvailableFunction;
            private OnNetworkAvailableBackOnlineFunction onNetworkAvailableBackOnlineFunction;
            private OnNetworkAvailableAbove24Function onNetworkAvailableAbove24Function;
            private OnNetworkAvailableAbove24FunctionOnlyNetwork onNetworkAvailableAbove24FunctionOnlyNetwork;
            private OnNetworkAvailableBelow24Function onNetworkAvailableBelow24Function;
            private OnNetworkAvailableBelow24FunctionOnlyNetworkInfo onNetworkAvailableBelow24FunctionOnlyNetworkInfo;
            private OnNetworkUnavailableFunction onNetworkUnavailableFunction;

            @Override
            public void onNetworkAvailable(boolean backOnline, Network network) {
                // Above 24
                if (onNetworkAvailableFunction != null)
                    onNetworkAvailableFunction.onNetworkAvailable();

                if (onNetworkAvailableBackOnlineFunction != null)
                    onNetworkAvailableBackOnlineFunction.onNetworkAvailable(backOnline);

                if (onNetworkAvailableAbove24Function != null)
                    onNetworkAvailableAbove24Function.onNetworkAvailable(backOnline, network);

                if (onNetworkAvailableAbove24FunctionOnlyNetwork != null)
                    onNetworkAvailableAbove24FunctionOnlyNetwork.onNetworkAvaileble(network);

            }

            @Override
            public void onNetworkAvailable(boolean backOnline, NetworkInfo networkInfo) {
                // Below 24
                if (onNetworkAvailableFunction != null)
                    onNetworkAvailableFunction.onNetworkAvailable();

                if (onNetworkAvailableBackOnlineFunction != null)
                    onNetworkAvailableBackOnlineFunction.onNetworkAvailable(backOnline);

                if (onNetworkAvailableBelow24Function != null)
                    onNetworkAvailableBelow24Function.onNetworkAvailable(backOnline, networkInfo);

                if (onNetworkAvailableBelow24FunctionOnlyNetworkInfo != null)
                    onNetworkAvailableBelow24FunctionOnlyNetworkInfo.onNetworkAvailable(networkInfo);

            }

            @Override
            public void onNetworkUnavailable() {
                if (onNetworkUnavailableFunction != null)
                    onNetworkUnavailableFunction.onNetworkUnavailable();
            }

        }
    }

}
