package com.mfrf.dawdletodo.model.task_container;

import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.Collection;
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
    protected Collection<AbstractTaskContainer> peekTaskGroups() {
        return this.prio_que;
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
}
