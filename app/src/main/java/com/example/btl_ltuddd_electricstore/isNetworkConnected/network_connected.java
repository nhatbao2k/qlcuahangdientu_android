package com.example.btl_ltuddd_electricstore.isNetworkConnected;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class network_connected {
    public static boolean isConnectedInternet(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED){
            return true;
        }else {
            return false;
        }
    }
}
