package com.mfrf.dawdletodo;

import static com.mfrf.dawdletodo.R.layout.create_task_spinner_item;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.mfrf.dawdletodo.data_center.MemoryDataBase;
import com.mfrf.dawdletodo.enums.EnumTaskType;
import com.mfrf.dawdletodo.ui.AddTaskSpinAdapter;
import com.mfrf.dawdletodo.utils.BasicActivityForConvince;

import java.util.Locale;

public class AddTaskActivity extends BasicActivityForConvince {

    private String group_id;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        Bundle parameters = getIntent().getExtras();
        Spinner selector = (Spinner) findViewById(R.id.select_task_type_to_add);
        selector.setAdapter(new AddTaskSpinAdapter(AddTaskActivity.this, create_task_spinner_item));
        FragmentManager manager = this.getSupportFragmentManager();
        FragmentTransaction init_transaction = manager.beginTransaction();

        this.group_id = parameters.getString("group");
        this.group_id = parameters.getString("group");

        init_transaction.add(R.id.frag_task_creator_container, EnumTaskType.Single.createFragment());
        selector.setOnItemClickListener((parent, view, position, id) -> {
            FragmentTransaction fragmentTransaction = AddTaskActivity.this.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frag_task_creator_container, ((EnumTaskType) selector.getItemAtPosition(position)).createFragment());
            fragmentTransaction.commit();
        });

//        MemoryDataBase.INSTANCE.add_task_container() //TODO add a container
    }


}