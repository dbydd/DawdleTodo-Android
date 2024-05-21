package com.mfrf.dawdletodo.model.task_container;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class PriorityBasedTaskContainer extends AbstractTaskContainer {
    private final PriorityQueue<AbstractTaskContainer> prio_que = new PriorityQueue<>();

    public PriorityBasedTaskContainer(String groupId, AbstractTaskContainer... containers) {
        super(groupId);
        this.prio_que.addAll(Arrays.asList(containers));
    }

    @Override
    public int peek_priority() {
        return prio_que.peek().peek_priority();
    }

    @Nullable
    @Override
    public @NonNull List<AbstractTaskContainer> peekTaskGroups() {
        return this.prio_que.stream().toList();
    }

    @Override
    public AbstractTaskContainer markAsDone() {
        AbstractTaskContainer poll = prio_que.poll();
        prio_que.add(poll.markAsDone());
        return this;
    }

    @Override
    public AbstractTaskContainer add(AbstractTaskContainer container) {
        prio_que.add(container);
        return container;
    }

    @Override
    public String getTypeID() {
        return "Priority Based";
    }
}
