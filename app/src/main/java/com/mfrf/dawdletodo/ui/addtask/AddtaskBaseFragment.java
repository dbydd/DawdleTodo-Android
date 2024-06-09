package com.mfrf.dawdletodo.ui.addtask;

import android.text.Editable;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.mfrf.dawdletodo.model.TaskContainer;

import java.util.Optional;

public abstract class AddtaskBaseFragment extends Fragment {


    public abstract Optional<TaskContainer> getTask();

    Optional<String> extractText(EditText in, boolean ShouldNonnull) {
        Editable text = in.getText();
        if (text.toString().isBlank()) {
            if (ShouldNonnull) {
                Toast.makeText(this.getContext(), in.getHint() + "cannot be empty", Toast.LENGTH_SHORT).show();
                return Optional.empty();
            } else {
                Optional.of("");
            }
        }
        return Optional.of(text.toString());
    }
}
