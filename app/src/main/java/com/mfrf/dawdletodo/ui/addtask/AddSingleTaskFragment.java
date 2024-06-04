package com.mfrf.dawdletodo.ui.addtask;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mfrf.dawdletodo.R;
import com.mfrf.dawdletodo.model.task_container.AbstractTaskContainer;

public class AddSingleTaskFragment extends AddtaskBaseFragment
{

    private EditText task_id;
    private CheckBox completeTimesSelector;
    private EditText completeTime_edit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View self = inflater.inflate(R.layout.fragment_addtask_single, container);
        this.task_id = (EditText) self.findViewById(R.id.task_id);
        this.completeTimesSelector = ((CheckBox) self.findViewById(R.id.is_infinity_times));
        this.completeTime_edit = ((EditText) self.findViewById(R.id.edit_complete_time));

        this.completeTimesSelector.setOnClickListener(v -> {
            completeTime_edit.setActivated(!completeTimesSelector.isChecked());
        });


        return self;
    }

    @Override
    public AbstractTaskContainer getTask() {
        return null;
    }
}
