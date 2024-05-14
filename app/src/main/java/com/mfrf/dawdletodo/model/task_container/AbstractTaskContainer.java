package com.mfrf.dawdletodo.model.task_container;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mfrf.dawdletodo.exceptions.AddTaskError;
import com.mfrf.dawdletodo.model.PriorityModifiers;
import com.mfrf.dawdletodo.model.Task;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;

public abstract class AbstractTaskContainer implements Comparable<AbstractTaskContainer> {
    private final String group_id;


    public AbstractTaskContainer(String groupId) {
        group_id = groupId;
    }

    public String getGroup_id() {
        return group_id;
    }


    public int peek_priority() {
        Task task = peek_task();
        return
                PriorityModifiers.SimpleCompleteTimeModifier(
                        task.isInfini_long() ?
                                task.getInitial_priority()
                                :
                                PriorityModifiers.SimpleDeadlinePriorityModifier(
                                        task.getInitial_priority(), task.getBegin_date(), task.getEnd_date(), LocalDate.now()
                                ),
                        task.getExpected_complete_times(), task.getCompleteTimes()
                );
    }

    @NonNull
    protected abstract Collection<AbstractTaskContainer> peekTaskGroups();

    public Task peek_task() {
        return this.peekTaskGroups().stream().max(Comparator.naturalOrder()).get().peek_task();
    }

    @Override
    public int compareTo(AbstractTaskContainer o) {
        return this.peek_priority() - o.peek_priority();
    }

    @Nullable
    public AbstractTaskContainer find(String id) {
        if (Objects.equals(id, this.group_id)) {
            return this;
        }

        return peekTaskGroups().parallelStream().map(container -> container.find(id)).filter(Objects::nonNull).findAny().get();

    }

    public abstract AbstractTaskContainer markAsDone();

    public abstract AbstractTaskContainer add(AbstractTaskContainer container) throws AddTaskError.CannotAddToSingleTaskContainerError;
}
