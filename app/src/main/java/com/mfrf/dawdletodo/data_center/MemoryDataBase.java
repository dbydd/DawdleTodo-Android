package com.mfrf.dawdletodo.data_center;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mfrf.dawdletodo.exceptions.AddTaskError;
import com.mfrf.dawdletodo.model.Task;
import com.mfrf.dawdletodo.model.TaskContainer;
import com.mfrf.dawdletodo.model.TaskTreeManager;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;

public enum MemoryDataBase {
    INSTANCE();


    public HashMap<String, AtomicBoolean> DIRTY_MARKS = new HashMap<>();
    public HashMap<String, TaskTreeManager> TASK_GROUPS = new HashMap<>() {
        @Nullable
        @Override
        public TaskTreeManager put(String key, TaskTreeManager value) {
            TaskTreeManager put = super.put(key, value);
            DIRTY_MARKS.compute(key, (k, v) -> v == null ? new AtomicBoolean(true) : v);
            return put;
        }

        @Nullable
        @Override
        public TaskTreeManager compute(String key, @NonNull BiFunction<? super String, ? super TaskTreeManager, ? extends TaskTreeManager> remappingFunction) {
            TaskTreeManager compute = super.compute(key, remappingFunction);
            DIRTY_MARKS.compute(key, (k, v) -> v == null ? new AtomicBoolean(true) : v);
            return compute;
        }
    };

    {
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
        this.TASK_GROUPS.put("test", test);

    }

    public TaskTreeManager remove_task_group(String id) {
        TaskTreeManager remove = this.TASK_GROUPS.remove(id);
        DIRTY_MARKS.remove(id);
        return remove;
    }

    public boolean add_task_container(TaskContainer g, String parent_node, String group_id) {
        this.TASK_GROUPS.compute(group_id, (s, taskTreeManager) -> {
            //todo solve errors
            try {
                return (taskTreeManager == null ? new TaskTreeManager(group_id) : taskTreeManager).addTo(parent_node, g);
            } catch (AddTaskError.AddTaskToAtomickContainerError e) {
                throw new RuntimeException(e);
            } catch (AddTaskError e) {
                throw new RuntimeException(e);
            }
        });
        return false;
    }

    public void markDirty(String id) {
        DIRTY_MARKS.put(id, new AtomicBoolean(true));
    }

    public Optional<TaskContainer> query(String taskGroup, String taskContainer) {
        return this.TASK_GROUPS.containsKey(taskGroup) ? Optional.of(TASK_GROUPS.get(taskGroup).find(taskContainer)) : Optional.empty();
    }


}
