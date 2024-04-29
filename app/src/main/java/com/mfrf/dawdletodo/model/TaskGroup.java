package com.mfrf.dawdletodo.model;

import java.time.LocalDate;
import java.util.Date;

public class TaskGroup {
    private final String group_id;
    private final LocalDate begin_date;
    private final LocalDate end_date;


    public TaskGroup(String groupId, LocalDate beginDate, LocalDate endDate) {
        group_id = groupId;
        begin_date = beginDate;
        end_date = endDate;
    }

    public String getGroup_id() {
        return group_id;
    }

    public LocalDate getBegin_date() {
        return begin_date;
    }

    public LocalDate getEnd_date() {
        return end_date;
    }
}
