package com.mfrf.dawdletodo.ui.todos;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mfrf.dawdletodo.R;
import com.mfrf.dawdletodo.data_center.MemoryDataBase;
import com.mfrf.dawdletodo.databinding.FragmentTodoBinding;


public class TodoFragment extends Fragment {

    private FragmentTodoBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentTodoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ListView todoGroups = binding.todoGroups;
        todoGroups.setAdapter(
                new TaskGroupAdapter(
                        getContext(), MemoryDataBase.INSTANCE.request((g) -> new ItemData(R.drawable.todos, g.getGroup_id(), g.getGroup_id()))
                )
        );

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}