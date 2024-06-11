package com.mfrf.dawdletodo.model;

import com.mfrf.dawdletodo.utils.TypeConverter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class Task extends RealmObject implements Serializable, Cloneable {
    @PrimaryKey
    private UUID uuid;
    private String id;
    private String description;
    private int initial_priority;

    private Date begin_date;
    private Date end_date;
    private int expected_complete_times;
    private int complete_times = 0;

    private boolean infini_long;

    public Task(String id, String description, int initialPriority, Date beginDate, Date endDate, int expectedCompleteTime) {
        this.id = id;
        this.description = description;
        initial_priority = initialPriority;
        begin_date = beginDate;
        end_date = endDate;
        expected_complete_times = expectedCompleteTime;
        infini_long = expectedCompleteTime <= 0;
        this.uuid = UUID.randomUUID();
    }

    public Task() {
        this("empty", "empty!", 0, LocalDate.now(), LocalDate.now(), 0);
    }

    public Task(String id, String desc, int initialPriority, LocalDate begin, LocalDate end, int maxValue) {
        this(id, desc, initialPriority, TypeConverter.localeDate2Date(begin), TypeConverter.localeDate2Date(end), maxValue);
    }

    public int getCompleteTimes() {
        return complete_times;
    }

    public int incCompleteTimes() {
        return ++complete_times;
    }

    public boolean isInfini_long() {
        return infini_long;
    }

    public int getExpected_complete_times() {
        return expected_complete_times;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public int getInitial_priority() {
        return initial_priority;
    }

    public Date getBegin_date() {
        return begin_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    @Override
    public Task clone() {
        try {
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return (Task) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
