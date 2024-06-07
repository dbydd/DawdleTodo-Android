package com.mfrf.dawdletodo;

import static com.mfrf.dawdletodo.R.layout.create_task_spinner_item;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.mfrf.dawdletodo.data_center.MemoryDataBase;
import com.mfrf.dawdletodo.enums.EnumTaskType;
import com.mfrf.dawdletodo.ui.AddTaskSpinAdapter;
import com.mfrf.dawdletodo.ui.addtask.AddtaskBaseFragment;
import com.mfrf.dawdletodo.utils.BasicActivityForConvince;

public class AddTaskActivity extends BasicActivityForConvince {


        @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Bundle parameters = getIntent().getExtras();
        Spinner selector = (Spinner) findViewById(R.id.select_task_type_to_add);
        selector.setAdapter(new AddTaskSpinAdapter(AddTaskActivity.this, create_task_spinner_item));
        FragmentManager manager = this.getSupportFragmentManager();
        FragmentTransaction init_transaction = manager.beginTransaction();

        String group_id = parameters.getString("group");
        String parent_id = parameters.getString("id");

        init_transaction.add(R.id.frag_task_container_creator, EnumTaskType.Atomic.createFragment());
        init_transaction.commit();
        selector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    FragmentTransaction fragmentTransaction = AddTaskActivity.this.getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frag_task_container_creator, ((EnumTaskType) selector.getItemAtPosition(position)).createFragment());
                    fragmentTransaction.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });


        Button create_button = (Button) findViewById(R.id.complete_edit_and_add_task);
        create_button.setOnClickListener(v -> {
            ((AddtaskBaseFragment) manager.findFragmentById(R.id.frag_task_container_creator)).getTask().ifPresent(container -> {
                MemoryDataBase.INSTANCE.add_task_container(container, parent_id, group_id);
                AddTaskActivity.this.finish();
            });
        });

        Button cancel = (Button) findViewById(R.id.cancel_button);
        cancel.setOnClickListener(v -> {
            AddTaskActivity.this.finish();
        });

    }



}