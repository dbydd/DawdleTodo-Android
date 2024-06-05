package com.mfrf.dawdletodo.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Task implements Serializable, Cloneable {
    private final String id;
    private final String description;
    private final int initial_priority;

    private final LocalDate begin_date;
    private final LocalDate end_date;
    private final int expected_complete_times;
    private int complete_times = 0;

    private final boolean infini_long;

    public Task(String id, String description, int initialPriority, LocalDate beginDate, LocalDate endDate, int expectedCompleteTime) {
        this.id = id;
        this.description = description;
        initial_priority = initialPriority;
        begin_date = beginDate;
        end_date = endDate;
        expected_complete_times = expectedCompleteTime;
        infini_long = expectedCompleteTime <= 0;
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

    public LocalDate getBegin_date() {
        return begin_date;
    }

    public LocalDate getEnd_date() {
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
