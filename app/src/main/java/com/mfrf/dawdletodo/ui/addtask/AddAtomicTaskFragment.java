package com.mfrf.dawdletodo.ui.addtask;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mfrf.dawdletodo.R;
import com.mfrf.dawdletodo.model.Task;
import com.mfrf.dawdletodo.model.TaskContainer;

import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class AddAtomicTaskFragment extends AddtaskBaseFragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View self = inflater.inflate(R.layout.fragment_addtask_atomic, container,false);
        return self;
    }

    @Override
    public Optional<TaskContainer> getTask() {
//        return new SingleTaskContainer(created);
        View self = getView();

        String description = extractText((EditText) self.findViewById(R.id.description), false).get();
        LocalDate begin_date = extractText((EditText) self.findViewById(R.id.begin_date), true).map(LocalDate::parse).orElseGet(LocalDate::now);
        LocalDate end_date = extractText((EditText) self.findViewById(R.id.end_date), true).map(LocalDate::parse).orElseGet(() -> LocalDate.now().plusDays(30));
        int complete_times = extractText((EditText) self.findViewById(R.id.complete_times), true).map(t -> t.isEmpty() || t.isBlank() ? "0" : t).map(Integer::parseInt).orElseGet(() -> 0);

        AtomicReference<Optional<TaskContainer>> ret = new AtomicReference<>(Optional.empty());

        extractText((EditText) self.findViewById(R.id.task_id), true).ifPresent(task_id -> {
            extractText((EditText) self.findViewById(R.id.priority), true).ifPresent(priority -> {

                Task task_created = new Task(task_id, description, Integer.parseInt(priority.isBlank() || priority.isEmpty() ? "0" : priority), begin_date, end_date, complete_times);
                Optional<TaskContainer> singleTaskContainer = Optional.of(new TaskContainer(task_created));
                ret.set(singleTaskContainer);
            });
        });
        return ret.get();
    }

}
