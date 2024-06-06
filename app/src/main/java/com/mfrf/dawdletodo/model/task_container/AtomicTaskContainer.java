package com.mfrf.dawdletodo.model.task_container;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mfrf.dawdletodo.exceptions.AddTaskError;
import com.mfrf.dawdletodo.model.Task;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AtomicTaskContainer extends AbstractTaskContainer {

    private final Task task;

    public AtomicTaskContainer(Task task) {
        super(task.getId());
        this.task = task;
    }

    @Override
    public @NonNull List<AbstractTaskContainer> peekTaskGroups() {
        List<AbstractTaskContainer> abstractTaskContainers = new java.util.ArrayList<>();
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

    @Override
    public Optional<Task> peek_task() {
        return Optional.of(this.task);
    }

    @Nullable
    @Override
    public AbstractTaskContainer find(String id) {
        return Objects.equals(id, this.getContainerID()) ?this:null;
    }

    @Override
    public String getTypeID() {
        return "Atomic Task";
    }

    @Override
    public boolean couldHasChild() {
        return false;
    }
}
