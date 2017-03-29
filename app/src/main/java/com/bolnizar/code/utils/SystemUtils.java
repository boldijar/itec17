package com.bolnizar.code.utils;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class SystemUtils {

    private final ConnectivityManager mConnectivityManager;

    public SystemUtils(ConnectivityManager connectivityManager) {
        mConnectivityManager = connectivityManager;
    }

    public boolean isNetworkUnavailable() {
        NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();
        return activeNetwork == null || !activeNetwork.isConnectedOrConnecting();
    }
}
