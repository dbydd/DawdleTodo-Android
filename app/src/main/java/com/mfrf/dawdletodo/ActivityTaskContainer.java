package com.mfrf.dawdletodo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.mfrf.dawdletodo.data_center.MemoryDataBase;
import com.mfrf.dawdletodo.model.task_container.AbstractTaskContainer;
import com.mfrf.dawdletodo.ui.TaskContainerAdapter;

import java.util.Optional;

public class ActivityTaskContainer extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_container);
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String group_id = intent.getStringExtra("group");
        ListView containers = (ListView) findViewById(R.id.task_container_groups);
        Optional<AbstractTaskContainer> query = MemoryDataBase.INSTANCE.query(group_id, id);
        query.ifPresent(c->{
            containers.setAdapter(new TaskContainerAdapter(this.getBaseContext(),this,c,group_id));
        });
    }
}
