package com.example.farm_e_market;

import android.app.Application;

public class MyApp extends Application {
    public static MyApp mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance=this;
    }

    public static synchronized MyApp getInstance(){
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener){
        ConnectivityReceiver.connectivityReceiverListener=listener;
    }

}
