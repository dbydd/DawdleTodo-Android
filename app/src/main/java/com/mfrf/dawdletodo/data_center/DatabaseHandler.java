package com.mfrf.dawdletodo.data_center;

import com.mfrf.dawdletodo.model.TaskTreeManager;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import io.realm.Realm;

public class DatabaseHandler {


    public static void deserializeTaskGroups() {
        Realm defaultInstance = Realm.getDefaultInstance();
        if (!defaultInstance.isEmpty()) {
            MemoryDataBase.INSTANCE.TASK_GROUPS.clear();
            defaultInstance.where(TaskTreeManager.class).findAll().stream().forEach(taskTreeManager -> {
                MemoryDataBase.INSTANCE.TASK_GROUPS.put(taskTreeManager.getConfigID(), taskTreeManager);
                MemoryDataBase.INSTANCE.DIRTY_MARKS.put(taskTreeManager.getConfigID(), new AtomicBoolean(false)); //ugly, but it works!
            });
        }
        defaultInstance.close();
    }

    public static void serializeTaskGroups() {
        Realm defaultInstance = Realm.getDefaultInstance();
        defaultInstance.executeTransaction(realm -> {
            MemoryDataBase.INSTANCE.DIRTY_MARKS
                    .entrySet()
                    .stream()
                    .filter(id_mark_entry -> id_mark_entry.getValue().get())
                    .map(stringAtomicBooleanEntry ->
                            MemoryDataBase.INSTANCE.TASK_GROUPS
                                    .get(stringAtomicBooleanEntry.getKey()))
                    .filter(Objects::nonNull)
                    .forEach(manager -> {
                        realm.createOrUpdateObjectFromJson(TaskTreeManager.class, manager.toJSON());
                        manager.cleanDirty();
                    });
        });
        defaultInstance.close();
    }

    public static void deserializeConfigOrDefault(String key, Supplier<Configuration> default_value) {
        Realm defaultInstance = Realm.getDefaultInstance();
        if (!defaultInstance.isEmpty()) {
            Configuration.Instance = defaultInstance.where(Configuration.class).equalTo("name", "default").findFirst();
        }
        defaultInstance.close();
    }

    public static void serializeConfigValue() {
        if (Configuration.dirty.get()) {
            Realm defaultInstance = Realm.getDefaultInstance();
            if (!defaultInstance.isEmpty()) {
                defaultInstance.executeTransaction(realm -> {
                    realm.copyToRealmOrUpdate(Configuration.Instance);
                });
                defaultInstance.close();
            }
        }
    }


}
