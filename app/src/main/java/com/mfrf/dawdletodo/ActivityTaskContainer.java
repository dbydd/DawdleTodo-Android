package com.mfrf.dawdletodo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import com.mfrf.dawdletodo.databinding.ActivityTaskContainerBinding;
import com.mfrf.dawdletodo.ui.TaskContainerAdapter;
import com.mfrf.dawdletodo.utils.BasicActivityForConvince;

public class ActivityTaskContainer extends BasicActivityForConvince {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        var binding = ActivityTaskContainerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setContentView(R.layout.activity_task_container);
        Intent fromIntent = getIntent();
        String id = fromIntent.getStringExtra("id");
        String group_id = fromIntent.getStringExtra("group");
        ListView containers = (ListView) findViewById(R.id.task_container_groups);
        containers.setAdapter(new TaskContainerAdapter(binding.getRoot().getContext(), this, group_id, id));

        Button addTask = (Button) findViewById(R.id.button_add_task);
        addTask.setOnClickListener(v -> {
            this.build_intent.accept(new Intent_ActivityPairProcessor(intent -> {
//                intent.putExtras(fromIntent.getExtras().deepCopy());
                intent.putExtra("id", id);
                intent.putExtra("group", group_id);
            }, AddTaskActivity.class));
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ((TaskContainerAdapter) ((ListView) this.findViewById(R.id.task_container_groups)).getAdapter()).notifyDataSetChanged();
    }
}
