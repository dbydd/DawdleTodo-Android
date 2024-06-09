package com.mfrf.dawdletodo.ui.addtask;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mfrf.dawdletodo.R;
import com.mfrf.dawdletodo.enums.EnumTaskType;
import com.mfrf.dawdletodo.model.TaskContainer;

import java.util.Optional;

public class AddNormalTaskContainersFragment extends AddtaskBaseFragment {

    private final EnumTaskType type;

    public AddNormalTaskContainersFragment(EnumTaskType type) {
        this.type = type;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View self = inflater.inflate(R.layout.fragmen_addtask_normal_containers, container, false);
        return self;
    }

    @Override
    public Optional<TaskContainer> getTask() {

        View self = getView();
        return extractText((EditText) self.findViewById(R.id.task_id), true).map(TaskContainer::new);
    }
}
