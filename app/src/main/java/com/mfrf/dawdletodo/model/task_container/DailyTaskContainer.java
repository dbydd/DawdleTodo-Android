package com.mfrf.dawdletodo.model.task_container;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class DailyTaskContainer extends AbstractTaskContainer {
    private Queue<AbstractTaskContainer> round_robin_queue = new LinkedList<>();


    public DailyTaskContainer(String groupId) {
        super(groupId);
    }


    public DailyTaskContainer(String groupId, AbstractTaskContainer... containers) {
        super(groupId);
        round_robin_queue.addAll(Arrays.asList(containers));
    }

    @Nullable
    @Override
    public @NonNull List<AbstractTaskContainer> peekTaskGroups() {
        return round_robin_queue.stream().toList();
    }

    @Override
    public AbstractTaskContainer markAsDone() {
        round_robin_queue.add(round_robin_queue.poll().markAsDone());
        return this;
    }

    @Override
    public AbstractTaskContainer add(AbstractTaskContainer container) {
        round_robin_queue.add(container);
        return container;
    }

    @Override
    public String getTypeID() {
        return "Daily Task";
    }
}
