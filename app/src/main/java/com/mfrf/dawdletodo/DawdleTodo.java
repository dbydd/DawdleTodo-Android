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
        databaseTask.schedule(new DatabaseTask(databaseTask), 0, 60); //best initial period
        //since android cannot add event of app killed, so we can only save data periodically
        DatabaseHandler.deserializeTaskGroups(); // lag!
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
                if (autoSaveInterval != this.scheduledExecutionTime()) {
                    this.cancel();
                    selfTimerRef.schedule(new DatabaseTask(selfTimerRef), autoSaveInterval);
                }
            }

            DatabaseHandler.serializeTaskGroups();
        }
    }
}
