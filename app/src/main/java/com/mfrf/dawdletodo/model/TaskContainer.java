package com.mfrf.dawdletodo.model;

import androidx.annotation.Nullable;

import com.mfrf.dawdletodo.exceptions.AddTaskError;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class TaskContainer extends RealmObject {

    @PrimaryKey
    private UUID uuid = UUID.randomUUID();
    private String id = "placeholder";

    private RealmList<TaskContainer> childs = new RealmList<>();
    @Nullable
    private Task nullableTask = null;

    public TaskContainer(String id, TaskContainer... childs) {
        this.id = id;
        this.getChilds().addAll(Arrays.stream(childs).toList());
        this.uuid = UUID.randomUUID();
    }

    public TaskContainer(Task task) {
        this.id = task.getId();
        this.nullableTask = task;
        this.uuid = UUID.randomUUID();
    }

    public TaskContainer() {
    }

    public boolean isAtomic() {
        return getNullableTask() != null;
    }

    public TaskContainer find(String node_id) {
        if (this.id.equals(node_id)) {
            return this;
        }
        if (this.isAtomic()) {
            return null;
        }
        for (TaskContainer child : getChilds()) {
            TaskContainer taskContainer = child.find(node_id);
            if (taskContainer != null) {
                return taskContainer;
            }
        }
        return null;
    }

    public void add(TaskContainer container) throws AddTaskError.AddTaskToAtomickContainerError {
        if (this.isAtomic()) {
            throw new AddTaskError.AddTaskToAtomickContainerError();
        }
        this.getChilds().add(container);
    }

    public HashMap<String, Integer> countItems(HashMap<String, Integer> counter) {
        if (this.isAtomic()) {
            counter.compute("Atomic Task", (s, count) -> count == null ? 1 : count + 1);
        } else {
            counter.compute("Container", (s, count) -> count == null ? 1 : count + 1);
            getChilds().forEach(taskContainer -> taskContainer.countItems(counter));
        }
        return counter;
    }

    public Integer priority() {
        if (this.isAtomic()) {

            return PriorityModifiers.SimpleCompleteTimeModifier(
                    PriorityModifiers.SimpleDeadlinePriorityModifier(
                            getNullableTask().getInitial_priority(),
                            getNullableTask().getBegin_date(),
                            getNullableTask().getEnd_date(),
                            LocalDate.now()
                    ),
                    getNullableTask().getExpected_complete_times(),
                    getNullableTask().getCompleteTimes()
            );
        }
        return this.peek().priority();
    }

    public String getId() {
        return id;
    }

    public Optional<Task> peek_task() {
        if (this.isAtomic()) {
            assert getNullableTask() != null;
            return Optional.of(getNullableTask());
        } else if (this.getChilds().isEmpty()) {
            return Optional.empty();
        } else {
            return peek().peek_task();
        }
    }

    public TaskContainer peek() {
        if (this.isAtomic()) {
            return this;
        } else {
            PriorityQueue<TaskContainer> childs = new PriorityQueue<>(Comparator.comparingInt(TaskContainer::priority));
            childs.addAll(this.getChilds()); //actually store a ref, so it's ok!
            return childs.peek();
        }
    }

    public Optional<TaskContainer> markAsDone() {//not empty: item should be delete
        if (this.isAtomic()) {
            this.getNullableTask().incCompleteTimes();
            if (!this.getNullableTask().isInfini_long() && this.getNullableTask().getExpected_complete_times() <= getNullableTask().getCompleteTimes()) {
                return Optional.ofNullable(this);
            }
            return Optional.empty();
        } else {
            TaskContainer poll = this.peek();
            if (poll != null) {
                return poll.markAsDone();
            } else {
                return Optional.ofNullable(this); // while nothing left , drop self
            }
        }
    }

    public ArrayList<TaskContainer> peekTaskGroups() {
        return new ArrayList<>(this.getChilds().stream().toList());
    }

    @Nullable
    private Task getNullableTask() {
        return nullableTask;
    }

    public RealmList<TaskContainer> getChilds() {
        return childs;
    }
}
