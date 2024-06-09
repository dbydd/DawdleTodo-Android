package com.mfrf.dawdletodo;

import android.app.Application;

import com.mfrf.dawdletodo.data_center.Configuration;
import com.mfrf.dawdletodo.data_center.DatabaseHandler;

import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class DawdleTodo extends Application {

    private Timer databaseTask;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);

        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);

        this.databaseTask = new Timer("database", true);

//        ((Runnable) () -> {
            //since android cannot add event of app killed, so we can only save data periodically
            DatabaseHandler.deserializeConfigOrDefault("default", Configuration::new);
            DatabaseHandler.deserializeTaskGroups(); // lag!
            databaseTask.schedule(new DatabaseTask(databaseTask), 30 * 1000, 30 * 1000); //best initial period
//        }).run();
    }

    class DatabaseTask extends TimerTask {
        final Timer selfTimerRef; // may cause memory leak, but we have gc, git rid of it!

        DatabaseTask(Timer selfTimerRef) {
            this.selfTimerRef = selfTimerRef;
        }

        @Override
        public void run() {
            if (Configuration.dirty.get()) {
                DatabaseHandler.serializeConfigValue();
                int autoSaveInterval = Configuration.Instance.getAuto_save_interval();
                if (autoSaveInterval * 1000L != this.scheduledExecutionTime()) {
                    this.cancel();
                    selfTimerRef.schedule(new DatabaseTask(selfTimerRef), autoSaveInterval * 1000L);
                }
            }

            DatabaseHandler.serializeTaskGroups();
        }
    }
}
