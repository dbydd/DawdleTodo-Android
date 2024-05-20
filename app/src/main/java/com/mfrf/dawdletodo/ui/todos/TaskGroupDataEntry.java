package com.mfrf.dawdletodo.ui.todos;

import com.mfrf.dawdletodo.model.Task;

public class TaskGroupDataEntry {
    private int imageResId;
    private String id;
    private String taskContainerID;
    private String describe;
    private Task task_cloned_for_view;

    public TaskGroupDataEntry(int imageResId, String id, String describe, Task task_cloned_for_view, String taskContainerID) {
        this.imageResId = imageResId;
        this.id = id;
        this.describe = describe;
        this.task_cloned_for_view = task_cloned_for_view;
        this.taskContainerID = taskContainerID;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getGroupID() {
        return id;
    }

    public String getTaskContainerID() {
        return this.taskContainerID;
    }

    public String getDescribe() {
        return describe;
    }

    public String getTaskDesc() {
        return task_cloned_for_view.getDescription();
    }
}
