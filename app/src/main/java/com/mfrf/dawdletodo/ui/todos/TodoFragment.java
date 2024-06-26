package com.mfrf.dawdletodo.ui.todos;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mfrf.dawdletodo.data_center.DatabaseHandler;
import com.mfrf.dawdletodo.databinding.FragmentTodoBinding;
import com.mfrf.dawdletodo.model.TaskTreeManager;


public class TodoFragment extends Fragment {

    private FragmentTodoBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTodoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ListView todoGroups = binding.todoGroups;
        FragmentActivity activity = getActivity();

        TaskGroupAdapter taskGroupAdapter = new TaskGroupAdapter(
                getContext(),
                activity
        );
        todoGroups.setAdapter(taskGroupAdapter);

        FloatingActionButton addTaskGroup = binding.addTaskGroup;
        addTaskGroup.setOnClickListener(v -> {
            showInputDialog(taskGroupAdapter);
        });

        return root;
    }

    private void showInputDialog(TaskGroupAdapter taskGroupAdapter) { //copy and modified from https://www.cnblogs.com/gzdaijie/p/5222191.html
        /*@setView 装入一个EditView
         */
        final EditText editText = new EditText(this.getContext());
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(this.getContext());
        inputDialog.setTitle("请给出新建任务组的名字").setView(editText);
        inputDialog.setPositiveButton("确定", (dialog, which) -> {
            DatabaseHandler.addTaskGroup(new TaskTreeManager(editText.getText().toString()));
            taskGroupAdapter.notifyDataSetChanged();
        }).show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((TaskGroupAdapter) this.binding.todoGroups.getAdapter()).notifyDataSetChanged();
    }
}