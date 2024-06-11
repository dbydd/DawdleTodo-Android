package com.mfrf.dawdletodo.data_center;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class Configuration extends RealmObject {
    @PrimaryKey
    private UUID id;
    public String name; //just for future more extensions
    private int default_time_limit_offset;

    public Configuration() {
        this("default");
    }

    public Configuration(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
        default_time_limit_offset = 30;
    }

    public int getDefault_time_limit_offset() {
        return default_time_limit_offset;
    }

    public void setDefaultTimeLimitOffset(int default_time_limit_offset) {
        this.default_time_limit_offset = default_time_limit_offset;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

