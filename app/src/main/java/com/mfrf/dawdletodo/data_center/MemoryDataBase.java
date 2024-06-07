package com.mfrf.dawdletodo.data_center;

import com.mfrf.dawdletodo.exceptions.AddTaskError;
import com.mfrf.dawdletodo.model.Task;
import com.mfrf.dawdletodo.model.TaskTreeManager;
import com.mfrf.dawdletodo.model.task_container.AbstractTaskContainer;
import com.mfrf.dawdletodo.model.task_container.DailyTaskContainer;
import com.mfrf.dawdletodo.model.task_container.PriorityBasedTaskContainer;
import com.mfrf.dawdletodo.model.task_container.AtomicTaskContainer;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.BiFunction;

public enum MemoryDataBase {
    INSTANCE();
    public final HashMap<String, TaskTreeManager> TASK_GROUPS = new HashMap<>();

    {
        TaskTreeManager test = new TaskTreeManager("test");
        try {
            //functional programming time!!!!!
            test
                    .addTo("root",
                            new PriorityBasedTaskContainer("test_prio",
                                    new DailyTaskContainer("test_daily_task",
                                            new AtomicTaskContainer(new Task("test_single_task_in_daily_task", "balabala", 114, LocalDate.now(), LocalDate.now(), Integer.MAX_VALUE)),
                                            new PriorityBasedTaskContainer("test_prio_task_in_daily_task",
                                                    new PriorityBasedTaskContainer("test_prio_task_in_prio_task",
                                                            new AtomicTaskContainer(new Task("test_single_task_in_prio_prio", "eaaaaaaa", 514, LocalDate.now(), LocalDate.now(), Integer.MAX_VALUE)),
                                                            new AtomicTaskContainer(new Task("test_single_task_in_prio_prio_2", "henghenghengaaaaaa", 1919, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 1), 16))
                                                    ),
                                                    new AtomicTaskContainer(new Task("test_single_task_in_prio", "mulimomuli", 810, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 1), 24))
                                            )
                                    )
                            ))
                    .addTo("root",
                            new DailyTaskContainer("test_daily_task_2",
                                    new AtomicTaskContainer(new Task("test_single_task_in_daily_2", "mulimomuli", 114514, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 1), Integer.MAX_VALUE)),
                                    new AtomicTaskContainer(new Task("test_single_task_in_daily_3", "mulimomuli", 1919, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 1), Integer.MAX_VALUE))
                            )
                    )
                    .addTo("test_daily_task_2",
                            new AtomicTaskContainer(new Task("test_single_task_add_later", "mulimomuli", 810, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 1), Integer.MAX_VALUE))
                    );
        } catch (AddTaskError e) {
            throw new RuntimeException(e);
        }
        this.TASK_GROUPS.put("test", test);

    }

    public void compute(String key, BiFunction<? super String, ? super TaskTreeManager, ? extends TaskTreeManager> create_closure) {
        TaskTreeManager abstractTaskContainer = this.TASK_GROUPS.compute(key, create_closure);
    }

    public TaskTreeManager remove_task_group(String id) {
        return this.TASK_GROUPS.remove(id);
    }

    public boolean add_task_container(AbstractTaskContainer g, String parent_node, String group_id) {
        this.TASK_GROUPS.compute(group_id, (s, taskTreeManager) -> {
            //todo solve errors
            try {
                return (taskTreeManager == null ? new TaskTreeManager(group_id) : taskTreeManager).addTo(parent_node, g);
            } catch (AddTaskError.CannotAddToSingleTaskContainerError e) {
                throw new RuntimeException(e);
            } catch (AddTaskError e) {
                throw new RuntimeException(e);
            }
        });
        return false;
    }

    public Optional<AbstractTaskContainer> query(String taskGroup,String taskContainer){
        return this.TASK_GROUPS.containsKey(taskGroup) ? Optional.of(TASK_GROUPS.get(taskGroup).find(taskContainer)) : Optional.empty();
    }

}
