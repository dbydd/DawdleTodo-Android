package com.mfrf.dawdletodo.data_center;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class Configuration extends RealmObject {
    public static AtomicBoolean dirty = new AtomicBoolean(true);
    public static Configuration Instance = new Configuration();

    @PrimaryKey
    private UUID id;
    public String name; //just for future more extensions
    private int auto_save_interval;

    public Configuration() {
        this("default");
    }

    public Configuration(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
        auto_save_interval = 60;
    }

    public void markDirty() {
        dirty.set(true);
    }

    public int getAuto_save_interval() {
        return auto_save_interval;
    }

    public void setAuto_save_interval(int auto_save_interval) {
        this.auto_save_interval = auto_save_interval;
        markDirty();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        markDirty();
    }
}

