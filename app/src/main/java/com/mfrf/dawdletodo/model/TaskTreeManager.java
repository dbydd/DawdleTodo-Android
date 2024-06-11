package com.mfrf.dawdletodo.model;

import android.util.Pair;

import com.mfrf.dawdletodo.exceptions.AddTaskError;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class TaskTreeManager extends RealmObject {
    @PrimaryKey
    private UUID uuid;
    private String configID;
    private TaskContainer root;

    public TaskTreeManager(TaskContainer root, String configID) {
        this.root = root;
        this.configID = configID;
        this.uuid = UUID.randomUUID();
    }

    public TaskTreeManager(String configID) {
        this.configID = configID;
        this.root = new TaskContainer("root");
        this.uuid = UUID.randomUUID();
    }

    public TaskTreeManager() {
        this("default");
    }

    public TaskTreeManager addTo(String parent_id, TaskContainer container) throws AddTaskError {
        TaskContainer basicPriorityBasedTaskContainer = root.find(parent_id);
        if (basicPriorityBasedTaskContainer == null) {
            throw new AddTaskError.GroupNotFoundError(parent_id);
        }
        basicPriorityBasedTaskContainer.add(container);
        return this;
    }

    public String getConfigID() {
        return configID;
    }

    public String countItems() {
//        return this.root.countItems(new HashMap<>()).entrySet().stream().map(entry->entry.getKey() + ": " + entry.getValue().toString()).reduce((acc,current)->
//            acc + "\n" + current
//        ).get();
        return this.root.countItems(new HashMap<>()).get("Atomic Task").toString();
    }

    public Optional<Pair<String, Task>> advice() {
        Optional<Task> task = this.root.peek_task();
        return task.map(value -> Pair.create(this.root.getId(), value));
    }

    public TaskContainer find(String task_id) {
        return root.find(task_id);
    }

    public void delete(String id_to_be_delete) {
        root.find(id_to_be_delete).deleteFromRealm();
    }


}
