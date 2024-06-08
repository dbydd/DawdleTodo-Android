package com.mfrf.dawdletodo.model;

import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mfrf.dawdletodo.data_center.MemoryDataBase;
import com.mfrf.dawdletodo.data_center.RuntimeTypeAdapterFactory;
import com.mfrf.dawdletodo.exceptions.AddTaskError;
import com.mfrf.dawdletodo.model.task_container.AbstractTaskContainer;
import com.mfrf.dawdletodo.model.task_container.AtomicTaskContainer;
import com.mfrf.dawdletodo.model.task_container.DailyTaskContainer;
import com.mfrf.dawdletodo.model.task_container.PriorityBasedTaskContainer;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import io.realm.RealmObject;

public class TaskTreeManager extends RealmObject {
    public static final RuntimeTypeAdapterFactory<AbstractTaskContainer> typeFactory = RuntimeTypeAdapterFactory
            .of(AbstractTaskContainer.class, "type")
            .registerSubtype(AtomicTaskContainer.class, "AtomicTaskContainer")
            .registerSubtype(DailyTaskContainer.class, "DailyTaskContainer")
            .registerSubtype(PriorityBasedTaskContainer.class, "PriorityBasedTaskContainer");
    public static final Gson parser = new GsonBuilder().registerTypeAdapterFactory(typeFactory).setPrettyPrinting().create();

    private final String configID;
    private final AbstractTaskContainer root;

    public TaskTreeManager(AbstractTaskContainer root, String configID) {
        this.root = root;
        this.configID = configID;
    }

    public TaskTreeManager(String configID) {
        this.configID = configID;
        this.root = new DailyTaskContainer("root");
    }

    public TaskTreeManager addTo(String parent_id, AbstractTaskContainer container) throws AddTaskError {
        AbstractTaskContainer abstractTaskContainer = root.find(parent_id);
        if (abstractTaskContainer == null) {
            throw new AddTaskError.GroupNotFoundError(parent_id);
        }
        abstractTaskContainer.add(container);
        return this;
    }


    public String toJSON() {
        return parser.toJson(this);
    }

    public static TaskTreeManager fromJSON(String json) {
        return parser.fromJson(json, TaskTreeManager.class);
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
        return task.map(value -> Pair.create(this.root.getContainerID(), value));
    }

    public AbstractTaskContainer find(String task_id) {
        return root.find(task_id);
    }

    public boolean isDirty() {
        return MemoryDataBase.INSTANCE.DIRTY_MARKS.getOrDefault(this.configID, new AtomicBoolean(true)).get();
    }

    public void cleanDirty() {
        MemoryDataBase.INSTANCE.DIRTY_MARKS.put(this.configID, new AtomicBoolean(false));
    }
}
