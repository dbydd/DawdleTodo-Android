package com.mfrf.dawdletodo.model.task_container;

import androidx.annotation.Nullable;

import com.mfrf.dawdletodo.exceptions.AddTaskError;
import com.mfrf.dawdletodo.model.Task;

import java.util.Collection;
import java.util.List;

public class SingleTaskContainer extends AbstractTaskContainer {

    private final Task task;

    public SingleTaskContainer(Task task) {
        super(task.getId());
        this.task = task;
    }

    @Nullable
    @Override
    protected Collection<AbstractTaskContainer> peekTaskGroups() {
        List<AbstractTaskContainer> abstractTaskContainers = new java.util.ArrayList<>();
        abstractTaskContainers.add(this);
        return abstractTaskContainers;
    }

    @Override
    public AbstractTaskContainer markAsDone() {
        task.incCompleteTimes();
        return this;
    }

    @Override
    public AbstractTaskContainer add(AbstractTaskContainer container) throws AddTaskError.CannotAddToSingleTaskContainerError {
        throw new AddTaskError.CannotAddToSingleTaskContainerError();
    }
}
