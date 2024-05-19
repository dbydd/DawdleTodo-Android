package com.mfrf.dawdletodo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityTaskGroupEditor extends AppCompatActivity {
    private EditText editText1;
    private EditText editText2;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_group_editor);
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
//        MemoryDataBase.INSTANCE(id, (except_id, maybe_null_Abstract_taskContainer) -> {
//
//
//            // 初始化视图组件
//            editText1 = findViewById(R.id.editText1);
//            editText2 = findViewById(R.id.editText2);
//            addButton = findViewById(R.id.addButton);
//
//            addButton.setOnClickListener(v -> {
//                        // 创建 ItemData 并保存到 MemoryDataBase
//                        String text1 = editText1.getText().toString();
//                        String text2 = editText2.getText().toString();
//
//                        ItemDataEntry itemData = new ItemDataEntry(R.drawable.todos, text1, text2);
////                        MemoryDataBase.INSTANCE.add_task_group(new AbstractTaskContainer(itemData.getId(), LocalDate.now(), LocalDate.now()));
//
//                        // 清空输入框
//                        editText1.setText("");
//                        editText2.setText("");
//                        finish();
//                    }
//            );
//
//            return maybe_null_Abstract_taskContainer;
//        }
//        );
    }
}
