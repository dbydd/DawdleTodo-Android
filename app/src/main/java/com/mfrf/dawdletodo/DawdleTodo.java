package com.mfrf.dawdletodo;

import android.app.Application;

import com.mfrf.dawdletodo.data_center.Configuration;
import com.mfrf.dawdletodo.data_center.DatabaseHandler;

import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class DawdleTodo extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);

        RealmConfiguration config = new RealmConfiguration.Builder().allowWritesOnUiThread(true).allowQueriesOnUiThread(true).build();
        Realm.setDefaultConfiguration(config);

    }
}
