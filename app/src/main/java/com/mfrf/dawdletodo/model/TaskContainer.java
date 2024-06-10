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

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class TaskContainer extends RealmObject {

    @PrimaryKey
    private String id = "placeholder";

    private RealmList<TaskContainer> childs = new RealmList<>();
    @Nullable
    private Task nullableTask = null;

    public TaskContainer(String id, TaskContainer... childs) {
        this.id = id;
        this.childs.addAll(Arrays.stream(childs).toList());
    }

    public TaskContainer(Task task) {
        this.id = task.getId();
        this.nullableTask = task;
    }

    public TaskContainer() {
    }

    public boolean isAtomic() {
        return nullableTask != null;
    }

    public TaskContainer find(String node_id) {
        if (this.id.equals(node_id)) {
            return this;
        }
        if (this.isAtomic()) {
            return null;
        }
        for (TaskContainer child : childs) {
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
        this.childs.add(container);
    }

    public HashMap<String, Integer> countItems(HashMap<String, Integer> counter) {
        if (this.isAtomic()) {
            counter.compute("Atomic Task", (s, count) -> count == null ? 1 : count + 1);
        } else {
            counter.compute("Container", (s, count) -> count == null ? 1 : count + 1);
            childs.forEach(taskContainer -> taskContainer.countItems(counter));
        }
        return counter;
    }

    public Integer priority() {
        if (this.isAtomic()) {

            int ret = PriorityModifiers.SimpleDeadlinePriorityModifier(nullableTask.getInitial_priority(), nullableTask.getBegin_date(), nullableTask.getEnd_date(), LocalDate.now());
            if (!nullableTask.isInfini_long()) {
                ret = PriorityModifiers.SimpleCompleteTimeModifier(ret, nullableTask.getExpected_complete_times(), nullableTask.getCompleteTimes());
            }
            return ret;
        }
        return this.peek().priority();
    }

    public String getId() {
        return id;
    }

    public Optional<Task> peek_task() {
        if (this.isAtomic()) {
            assert nullableTask != null;
            return Optional.of(nullableTask);
        } else if (this.childs.isEmpty()) {
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
            childs.addAll(this.childs); //actually store a ref, so it's ok!
            return childs.peek();
        }
    }

    public Optional<TaskContainer> markAsDone() {//not empty: item should be delete
        if (this.isAtomic()) {
            this.nullableTask.incCompleteTimes();
            if (this.nullableTask.getExpected_complete_times() <= nullableTask.getCompleteTimes()) {
                return Optional.of(this);
            }
            return Optional.empty();
        } else {
            TaskContainer poll = this.peek();
            if (poll != null) {
                return poll.markAsDone();
            } else {
                return Optional.of(this); // while nothing left , drop self
            }
        }
    }

    public ArrayList<TaskContainer> peekTaskGroups() {
        return new ArrayList<>(this.childs);
    }


}
