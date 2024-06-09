package com.mfrf.dawdletodo.data_center;

import com.mfrf.dawdletodo.model.TaskTreeManager;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;

import io.realm.Realm;

public class DatabaseHandler {


    public static <R> R operationTaskGroups(String id, Function<TaskTreeManager, R> mapper, R defaultValue) {
        Realm defaultInstance = Realm.getDefaultInstance();
        if (!defaultInstance.isEmpty()) {
            AtomicReference<R> mapped = new AtomicReference<>();
            defaultInstance.executeTransaction(trans -> {
                mapped.set(defaultInstance.where(TaskTreeManager.class).equalTo("configID", id).findAll().stream().findFirst().map(mapper).orElseGet(() -> defaultValue));
            });
            defaultInstance.close();
            return mapped.get();
        }
        defaultInstance.close();
        return null;
    }

    public static void addTaskGroup(TaskTreeManager manager) {
        Realm defaultInstance = Realm.getDefaultInstance();
        defaultInstance.beginTransaction();
        defaultInstance.copyToRealmOrUpdate(manager);
        defaultInstance.commitTransaction();
        defaultInstance.close();
    }

    public static boolean hasTaskGroup(String id) {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        Realm defaultInstance = Realm.getDefaultInstance();
        defaultInstance.executeTransaction(t ->
                atomicBoolean.set(t.where(TaskTreeManager.class).equalTo("configID", id).findFirst() != null)
        );
        defaultInstance.close();
        return atomicBoolean.get();
    }

//    public static void serializeTaskGroups(TaskTreeManager manager) {
//        Realm defaultInstance = Realm.getDefaultInstance();
//        defaultInstance.executeTransaction(realm -> {
//            realm.copyToRealmOrUpdate(manager);
//        });
//        defaultInstance.close();
//    }

    public static void operationConfig(String configName, Consumer<Configuration> operator) { //Configuration only had primitive value
        Realm defaultInstance = Realm.getDefaultInstance();
        if (!defaultInstance.isEmpty()) {
            if (defaultInstance.where(Configuration.class).equalTo("name", configName).findFirst() == null) {
                Configuration configuration = new Configuration();
                configuration.setName(configName);
                defaultInstance.copyToRealmOrUpdate(configuration);
            }
            operator.accept(defaultInstance.where(Configuration.class).equalTo("name", configName).findFirst());
        }
        defaultInstance.close();
    }

//    public static void serializeConfigValue() {
//        if (Configuration.dirty.get()) {
//            Realm defaultInstance = Realm.getDefaultInstance();
//            if (!defaultInstance.isEmpty()) {
//                defaultInstance.executeTransaction(realm -> {
//                    realm.copyToRealmOrUpdate(Configuration.Instance);
//                });
//                defaultInstance.close();
//            }
//        }
//    }


}
