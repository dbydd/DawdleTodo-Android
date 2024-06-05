package com.mfrf.dawdletodo.ui.addtask;

import androidx.fragment.app.Fragment;

import com.mfrf.dawdletodo.model.Task;
import com.mfrf.dawdletodo.model.task_container.AbstractTaskContainer;

import java.util.Optional;

public abstract class AddtaskBaseFragment extends Fragment {


    public abstract Optional<? extends AbstractTaskContainer> getTask();
}
