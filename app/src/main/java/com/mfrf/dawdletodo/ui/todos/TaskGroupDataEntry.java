package com.mfrf.dawdletodo.ui.todos;

public class TaskGroupDataEntry {
    private int imageResId;
    private String id;
    private String describe;

    public TaskGroupDataEntry(int imageResId, String id, String describe) {
        this.imageResId = imageResId;
        this.id = id;
        this.describe = describe;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getId() {
        return id;
    }

    public String getDescribe() {
        return describe;
    }
}
