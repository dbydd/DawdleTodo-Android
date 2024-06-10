package com.mfrf.dawdletodo.ui.todos;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.mfrf.dawdletodo.ActivityTaskContainer;
import com.mfrf.dawdletodo.MainActivity;
import com.mfrf.dawdletodo.R;
import com.mfrf.dawdletodo.model.Task;
import com.mfrf.dawdletodo.model.TaskContainer;
import com.mfrf.dawdletodo.model.TaskTreeManager;
import com.mfrf.dawdletodo.utils.BasicActivityForConvince;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import io.realm.Realm;

public class TaskGroupAdapter extends BaseAdapter {
    private Context context;
    private final FragmentActivity activity;

    public TaskGroupAdapter(Context context, FragmentActivity activity) {
        this.context = context;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        AtomicInteger count = new AtomicInteger();
        try (Realm defaultInstance = Realm.getDefaultInstance()) {
            defaultInstance.executeTransaction(t ->
                    count.set(t.where(TaskTreeManager.class).findAll().size())
            );
        }

        return count.get();
    }

    @Override
    public Object getItem(int position) {
        return (Consumer<Consumer<TaskTreeManager>>) taskTreeManagerSupplier -> { //return a callback function, this is best solution
            try (Realm defaultInstance = Realm.getDefaultInstance()) {
                defaultInstance.executeTransaction(t ->
                        taskTreeManagerSupplier.accept(t.where(TaskTreeManager.class).findAll().get(position))
                );
            }
        };
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.task_group_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.logo = convertView.findViewById(R.id.task_logo);
            viewHolder.task_to_be_done = convertView.findViewById(R.id.task_to_be_done);
            viewHolder.desc_of_task = convertView.findViewById(R.id.task_desc);
            viewHolder.complete_current_task = convertView.findViewById(R.id.complete_current_task);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ((Consumer<Consumer<TaskTreeManager>>) getItem(position)).accept(manager -> {
            TaskGroupDataEntry item =
                    manager.advice().map(advice ->
                            new TaskGroupDataEntry(R.drawable.todos, manager.getConfigID(), "tasks: " + manager.countItems(), advice.second, advice.first)
                    ).orElse(new TaskGroupDataEntry(R.drawable.todos, manager.getConfigID(), "empty", new Task(), "empty"));

            viewHolder.logo.setImageResource(item.getImageResId());
            viewHolder.task_to_be_done.setText(item.getTaskTobeDone());
            viewHolder.desc_of_task.setText(manager.getConfigID());

        });

        viewHolder.complete_current_task.setOnClickListener(view -> {
            ((Consumer<Consumer<TaskTreeManager>>) getItem(position)).accept(manager -> {
                TaskContainer taskContainer = manager.find(viewHolder.task_to_be_done.getText().toString());
                taskContainer.markAsDone().ifPresent(to_be_del -> to_be_del.deleteFromRealm());
                manager.advice()
                        .map(advice -> new TaskGroupDataEntry(R.drawable.todos, manager.getConfigID(), "tasks: " + manager.countItems(), advice.second, advice.first))
                        .or(() -> Optional.of(new TaskGroupDataEntry(R.drawable.todos, manager.getConfigID(), "empty", new Task(), "empty")))
                        .ifPresent(advice_once_more -> {
                            viewHolder.logo.setImageResource(advice_once_more.getImageResId());
                            viewHolder.task_to_be_done.setText(advice_once_more.getTaskTobeDone());
                            viewHolder.desc_of_task.setText(advice_once_more.getTaskDesc());
                        });
            });
        });

        convertView.findViewById(R.id.actually_button_to_lower).setOnClickListener(view -> {

            ((Consumer<Consumer<TaskTreeManager>>) getItem(position)).accept(manager -> {
                ((MainActivity) activity).build_intent.accept(new BasicActivityForConvince.Intent_ActivityPairProcessor(intent -> {
                    intent.putExtra("group", manager.getConfigID());
                    intent.putExtra("id", "root"); //always root, hardcoded
                },
                        ActivityTaskContainer.class));
            });

        });

        View entireItem = convertView.findViewById(R.id.entire_item);
        entireItem.setLongClickable(true);
        entireItem.setOnLongClickListener(v -> {
            ((Consumer<Consumer<TaskTreeManager>>) getItem(position)).accept(manager -> {
                new AlertDialog.Builder(context)
                        .setTitle(manager.getConfigID())
                        .setMessage("你确认要删除这一整个任务组嘛？")
                        .setPositiveButton("删除", ((dialog, which) -> {

                            try (Realm defaultInstance = Realm.getDefaultInstance()) {
                                defaultInstance.executeTransaction(t ->
                                        {
                                            t.where(TaskTreeManager.class).findAll().get(position).deleteFromRealm(); //safety: 100% exist, not null
                                        }
                                );
                            }
                            TaskGroupAdapter.this.notifyDataSetChanged();
                        })).show();
            });
            return true;
        });

        return convertView;
    }


//    private boolean showTaskInfo(String group_id, String group_desc, Context c,) { //copy and modified from https://www.cnblogs.com/gzdaijie/p/5222191.html
//        TextView group_description = new TextView(c);
//        AlertDialog.Builder inputDialog = new AlertDialog.Builder(c);
//        inputDialog.setTitle("").setView(group_description);
//        inputDialog.setPositiveButton("确定", (dialog, which) -> {
//            DatabaseHandler.addTaskGroup(new TaskTreeManager(group_description.getText().toString()));
//        }).show();
//    }

    static class ViewHolder {
        ImageView logo;
        TextView task_to_be_done;

        TextView desc_of_task;
        Button complete_current_task;
    }
}

