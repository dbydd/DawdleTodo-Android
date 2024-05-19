package com.mfrf.dawdletodo.model.task_container;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mfrf.dawdletodo.exceptions.AddTaskError;
import com.mfrf.dawdletodo.model.PriorityModifiers;
import com.mfrf.dawdletodo.model.Task;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public abstract class AbstractTaskContainer implements Comparable<AbstractTaskContainer> {
    private final String group_id;


    public AbstractTaskContainer(String groupId) {
        group_id = groupId;
    }

    public String getGroup_id() {
        return group_id;
    }


    public int peek_priority() {
        Optional<Task> o_task = peek_task();
        return o_task.isPresent() ? o_task.map(task ->

                PriorityModifiers.SimpleCompleteTimeModifier(
                        task.isInfini_long() ?
                                task.getInitial_priority()
                                :
                                PriorityModifiers.SimpleDeadlinePriorityModifier(
                                        task.getInitial_priority(), task.getBegin_date(), task.getEnd_date(), LocalDate.now()
                                ),
                        task.getExpected_complete_times(), task.getCompleteTimes())
        ).get() : 0;
    }

    @NonNull
    protected abstract Collection<AbstractTaskContainer> peekTaskGroups();

    public Optional<Task> peek_task() {
        Optional<AbstractTaskContainer> max = this.peekTaskGroups().stream().filter(Objects::nonNull).max(Comparator.naturalOrder());
        return max.isPresent() ? max.get().peek_task() : Optional.empty();
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

        Optional<AbstractTaskContainer> any = peekTaskGroups().stream().map(container -> container.find(id)).filter(Objects::nonNull).findAny();
        return any.orElse(null);

    }

    public abstract AbstractTaskContainer markAsDone();

    public abstract AbstractTaskContainer add(AbstractTaskContainer container) throws AddTaskError.CannotAddToSingleTaskContainerError;

    public abstract String getTypeID();

    public Map<String, Integer> countItems(Map<String, Integer> in) {
        in.compute(getTypeID(), (id, before) ->
                before == null ? 0 : before + 1
        );
        this.peekTaskGroups().stream().forEach(c -> c.countItems(in));
        return in;
    }
}
