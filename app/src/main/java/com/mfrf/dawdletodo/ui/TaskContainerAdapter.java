package com.mfrf.dawdletodo.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.mfrf.dawdletodo.ActivityTaskContainer;
import com.mfrf.dawdletodo.R;
import com.mfrf.dawdletodo.data_center.DatabaseHandler;
import com.mfrf.dawdletodo.data_center.MemoryDataBase;
import com.mfrf.dawdletodo.model.Task;
import com.mfrf.dawdletodo.model.TaskContainer;
import com.mfrf.dawdletodo.utils.BasicActivityForConvince;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class TaskContainerAdapter extends BaseAdapter {
    private final Context context;
    private final FragmentActivity activity;
    private final String groupID;
    private final String containerID;

    public TaskContainerAdapter(Context context, FragmentActivity activity, String groupID, String containerID) {
        this.context = context;
        this.activity = activity;
        this.groupID = groupID;
        this.containerID = containerID;
    }

    @Override
    public int getCount() {
        AtomicInteger num = new AtomicInteger();
        MemoryDataBase.INSTANCE.query(groupID, containerID, taskContainer -> {
            if (taskContainer != null) {
                num.set(taskContainer.peekTaskGroups().size());
            }
        });
        return num.get();
    }

    @Override
    public Object getItem(int position) {
//        return container.peekTaskGroups().get(position);
        AtomicReference<TaskContainer> item = new AtomicReference<>();
        MemoryDataBase.INSTANCE.query(TaskContainerAdapter.this.groupID, TaskContainerAdapter.this.containerID, taskContainer -> {
            if (taskContainer != null) {
                TaskContainer fetched = taskContainer.peekTaskGroups().get(position);
                if (fetched != null) {
                    item.set(taskContainer.getRealm().copyFromRealm(fetched));
                }
            }
        });
        return item.get();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TaskContainerAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.task_group_item, parent, false);

            viewHolder = new TaskContainerAdapter.ViewHolder();
            viewHolder.logo = convertView.findViewById(R.id.task_logo);
            viewHolder.group_describe = convertView.findViewById(R.id.task_group_describe);
            viewHolder.task_desc = convertView.findViewById(R.id.task_desc);
            viewHolder.complete_current_task = convertView.findViewById(R.id.complete_current_task);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (TaskContainerAdapter.ViewHolder) convertView.getTag();
        }

        TaskContainer item = (TaskContainer) getItem(position);
        Optional<Task> peeked = item.peek_task();
        viewHolder.logo.setImageResource(R.drawable.todos);
        viewHolder.group_describe.setText(item.getId());
        viewHolder.task_desc.setText(peeked.isPresent() ? peeked.get().getDescription() : "null");
        viewHolder.task_desc.setText(peeked.isPresent() ? peeked.get().getDescription() : "null");


        viewHolder.complete_current_task.setOnClickListener(view -> {
            item.markAsDone().ifPresent(t -> {
                DatabaseHandler.operationTaskGroups(groupID, manager -> {
                    manager.delete(t.getId());
                    return Optional.empty();
                }, Optional.empty());
            });
//            MemoryDataBase.INSTANCE.markDirty(groupID);

            TaskContainer refresh_item = (TaskContainer) getItem(position);
            Optional<Task> refreshed = refresh_item.peek_task();
            viewHolder.logo.setImageResource(R.drawable.todos);
            viewHolder.group_describe.setText(refresh_item.getId());
            viewHolder.task_desc.setText(refreshed.isPresent() ? refreshed.get().getDescription() : "null");
            viewHolder.task_desc.setText(refreshed.isPresent() ? refreshed.get().getDescription() : "null");
            TaskContainerAdapter.this.notifyDataSetChanged();
        });


        convertView.findViewById(R.id.actually_button_to_lower).setOnClickListener(view -> {
            if (!item.isAtomic()) {
                ((BasicActivityForConvince) activity).build_intent.accept(new BasicActivityForConvince.Intent_ActivityPairProcessor(intent -> {
                    intent.putExtra("id", item.getId());
                    intent.putExtra("group", groupID);
                },
                        ActivityTaskContainer.class));
            } else {
                Toast.makeText(TaskContainerAdapter.this.context, "cannot add a task that couldn't have child!", Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }


    static class ViewHolder {
        ImageView logo;
        TextView group_describe;

        TextView task_desc;
        Button complete_current_task;
    }
}
