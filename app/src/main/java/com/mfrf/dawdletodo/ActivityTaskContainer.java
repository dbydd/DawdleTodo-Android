package com.mfrf.dawdletodo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import com.mfrf.dawdletodo.data_center.MemoryDataBase;
import com.mfrf.dawdletodo.model.task_container.AbstractTaskContainer;
import com.mfrf.dawdletodo.ui.TaskContainerAdapter;
import com.mfrf.dawdletodo.utils.BasicActivityForConvince;

import java.util.Optional;

public class ActivityTaskContainer extends BasicActivityForConvince {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_container);
        Intent fromIntent = getIntent();
        String id = fromIntent.getStringExtra("id");
        String group_id = fromIntent.getStringExtra("group");
        ListView containers = (ListView) findViewById(R.id.task_container_groups);
        Optional<AbstractTaskContainer> query = MemoryDataBase.INSTANCE.query(group_id, id);
        query.ifPresent(c -> {
            containers.setAdapter(new TaskContainerAdapter(this.getBaseContext(), this, c, group_id));
        });

        Button addTask = (Button) findViewById(R.id.button_add_task);
        addTask.setOnClickListener(v -> {
            this.build_intent.accept(new Intent_ActivityPairProcessor(intent -> {
//                intent.putExtras(fromIntent.getExtras().deepCopy());
                intent.putExtra("id",id);
                intent.putExtra("group",group_id);
            }, AddTaskActivity.class));
        });

    }
}
