package com.mfrf.dawdletodo.ui.addtask;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mfrf.dawdletodo.R;
import com.mfrf.dawdletodo.model.Task;
import com.mfrf.dawdletodo.model.task_container.AbstractTaskContainer;
import com.mfrf.dawdletodo.model.task_container.SingleTaskContainer;

import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class AddSingleTaskFragment extends AddtaskBaseFragment {

    private CheckBox completeTimesSelector;
    private EditText completeTime_edit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View self = inflater.inflate(R.layout.fragment_addtask_single, container);
        return self;
    }

    @Override
    public Optional<? extends AbstractTaskContainer> getTask() {
//        return new SingleTaskContainer(created);
        View self = getView();

        String description = extractText((EditText) self.findViewById(R.id.description), false).get();
        LocalDate begin_date = extractText((EditText) self.findViewById(R.id.begin_date), true).map(LocalDate::parse).orElseGet(LocalDate::now);
        LocalDate end_date = extractText((EditText) self.findViewById(R.id.end_date), true).map(LocalDate::parse).orElseGet(() -> LocalDate.now().plusDays(30));
        int complete_times = extractText((EditText) self.findViewById(R.id.complete_times), true).map(t -> t.isEmpty() || t.isBlank() ? "0" : t).map(Integer::parseInt).orElseGet(() -> 0);
        AtomicReference<Optional<? extends AbstractTaskContainer>> ret = new AtomicReference<>(Optional.empty());

        extractText((EditText) self.findViewById(R.id.task_id), true).ifPresent(task_id -> {
            extractText((EditText) self.findViewById(R.id.priority), true).ifPresent(priority -> {

                Task task_created = new Task(task_id, description, Integer.parseInt(priority.isBlank() || priority.isEmpty() ? "0" : priority), begin_date, end_date, complete_times);
                Optional<SingleTaskContainer> singleTaskContainer = Optional.of(new SingleTaskContainer(task_created));
                ret.set(singleTaskContainer);
            });
        });
        return ret.get();
    }

    private Optional<String> extractText(EditText in, boolean ShouldNonnull) {
        Editable text = in.getText();
        if (text.toString().isBlank()) {
            if (ShouldNonnull) {
                Toast.makeText(this.getContext(), in.getHint() + "cannot be empty", Toast.LENGTH_LONG).show();
                return Optional.empty();
            } else {
                Optional.of("");
            }
        }
        return Optional.of(text.toString());
    }
}
