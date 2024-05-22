package com.mfrf.dawdletodo.ui.addtask;

import androidx.fragment.app.Fragment;

import com.mfrf.dawdletodo.model.task_container.AbstractTaskContainer;

public abstract class AddtaskBaseFragment extends Fragment {

    public abstract AbstractTaskContainer getTask();
}
