package com.mfrf.dawdletodo.data_center;

import com.mfrf.dawdletodo.exceptions.AddTaskError;
import com.mfrf.dawdletodo.model.Task;
import com.mfrf.dawdletodo.model.TaskContainer;
import com.mfrf.dawdletodo.model.TaskTreeManager;

import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Consumer;

public enum MemoryDataBase {
    INSTANCE();


//    public HashMap<String, AtomicBoolean> DIRTY_MARKS = new HashMap<>();
//    public HashMap<String, TaskTreeManager> TASK_GROUPS = new HashMap<>() {
//        @Nullable
//        @Override
//        public TaskTreeManager put(String key, TaskTreeManager value) {
//            TaskTreeManager put = super.put(key, value);
//            DIRTY_MARKS.compute(key, (k, v) -> v == null ? new AtomicBoolean(true) : v);
//            return put;
//        }
//
//        @Nullable
//        @Override
//        public TaskTreeManager compute(String key, @NonNull BiFunction<? super String, ? super TaskTreeManager, ? extends TaskTreeManager> remappingFunction) {
//            TaskTreeManager compute = super.compute(key, remappingFunction);
//            DIRTY_MARKS.compute(key, (k, v) -> v == null ? new AtomicBoolean(true) : v);
//            return compute;
//        }
//    };

    static {
        TaskTreeManager test = new TaskTreeManager("test");
        try {
            //functional programming time!!!!!
            test
                    .addTo("root",
                            new TaskContainer("test_prio",
                                    new TaskContainer("test_daily_task",
                                            new TaskContainer(new Task("test_single_task_in_daily_task", "balabala", 114, LocalDate.now(), LocalDate.now(), Integer.MAX_VALUE)),
                                            new TaskContainer("test_prio_task_in_daily_task",
                                                    new TaskContainer("test_prio_task_in_prio_task",
                                                            new TaskContainer(new Task("test_single_task_in_prio_prio", "eaaaaaaa", 514, LocalDate.now(), LocalDate.now(), Integer.MAX_VALUE)),
                                                            new TaskContainer(new Task("test_single_task_in_prio_prio_2", "henghenghengaaaaaa", 1919, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 1), 16))
                                                    ),
                                                    new TaskContainer(new Task("test_single_task_in_prio", "mulimomuli", 810, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 1), 24))
                                            )
                                    )
                            ))
                    .addTo("root",
                            new TaskContainer("test_daily_task_2",
                                    new TaskContainer(new Task("test_single_task_in_daily_2", "mulimomuli", 114514, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 1), Integer.MAX_VALUE)),
                                    new TaskContainer(new Task("test_single_task_in_daily_3", "mulimomuli", 1919, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 1), Integer.MAX_VALUE))
                            )
                    )
                    .addTo("test_daily_task_2",
                            new TaskContainer(new Task("test_single_task_add_later", "mulimomuli", 810, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 1), Integer.MAX_VALUE))
                    );
        } catch (AddTaskError e) {
            throw new RuntimeException(e);
        }
        DatabaseHandler.addTaskGroup(test);

    }

    public void remove_task_group(String id) {
        DatabaseHandler.operationTaskGroups(id, manager -> {
            manager.deleteFromRealm();
            manager = null;
            return Optional.empty();
        }, Optional.empty());
    }


    public boolean add_task_container(TaskContainer g, String parent_node, String group_id) {

        if (!DatabaseHandler.hasTaskGroup(group_id)) {
            DatabaseHandler.addTaskGroup(new TaskTreeManager(group_id));
        }
        DatabaseHandler.operationTaskGroups(group_id, manager -> {
            try {
                manager.addTo(parent_node, g);
            } catch (AddTaskError e) {
                throw new RuntimeException(e);
            }
            return Optional.empty();
        }, Optional.empty());
        return false;
    }

    public void query(String taskGroup, String taskContainer, Consumer<TaskContainer> input_nullable_operation) {
//        return DatabaseHandler.operationTaskGroups()Optional.of(TASK_GROUPS.get(taskGroup).find(taskContainer)) : Optional.empty();
        DatabaseHandler.operationTaskGroups(taskGroup, manager -> {
            input_nullable_operation.accept(manager.find(taskContainer));
            return Optional.empty();
        }, Optional.empty());
    }


}
