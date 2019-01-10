package com.appsxhone.wordmeaning;

import android.app.Application;

/**
 * Created by Sameer on 23-Feb-16.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        UserPreferences.getInstance(getApplicationContext());
    }
}