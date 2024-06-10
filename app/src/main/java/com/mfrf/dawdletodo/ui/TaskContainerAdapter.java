package com.mfrf.dawdletodo.ui;

import android.app.Activity;
import android.app.AlertDialog;
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
import com.mfrf.dawdletodo.data_center.MemoryDataBase;
import com.mfrf.dawdletodo.model.Task;
import com.mfrf.dawdletodo.model.TaskContainer;
import com.mfrf.dawdletodo.model.TaskTreeManager;
import com.mfrf.dawdletodo.utils.BasicActivityForConvince;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import io.realm.Realm;

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
        return (Consumer<Consumer<TaskContainer>>) taskTreeManagerConsumer -> { //return a callback function, this is best solution
            MemoryDataBase.INSTANCE.query(groupID, containerID, taskContainer -> {
                taskTreeManagerConsumer.accept(taskContainer.peekTaskGroups().get(position));
            });
        };
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
            viewHolder.task_to_be_done = convertView.findViewById(R.id.task_to_be_done);
            viewHolder.task_desc = convertView.findViewById(R.id.task_desc);
            viewHolder.complete_current_task = convertView.findViewById(R.id.complete_current_task);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (TaskContainerAdapter.ViewHolder) convertView.getTag();
        }


        View finalConvertView = convertView;
        ((Consumer<Consumer<TaskContainer>>) getItem(position)).accept(taskContainer -> {
            taskContainer.peek_task().or(() -> Optional.of(new Task())).ifPresent(peeked_task -> {
                viewHolder.logo.setImageResource(R.drawable.todos); //temporary solution
                viewHolder.task_to_be_done.setText(peeked_task.getId());
                viewHolder.task_desc.setText(peeked_task.getDescription());
            });


        });

        viewHolder.complete_current_task.setOnClickListener(view -> {
            ((Consumer<Consumer<TaskContainer>>) getItem(position)).accept(taskContainer -> {
                taskContainer.markAsDone().ifPresent(t -> {
                    t.deleteFromRealm();
                });

                taskContainer.peek_task().ifPresentOrElse(peek_again -> {
                    viewHolder.logo.setImageResource(R.drawable.todos); //temporary solution
                    viewHolder.task_to_be_done.setText(peek_again.getId());
                    viewHolder.task_desc.setText(peek_again.getDescription());
                }, () -> {
                    ((Activity) view.getContext()).finish();
                });
            });
        });

        finalConvertView.findViewById(R.id.actually_button_to_lower).setOnClickListener(view -> {
            ((Consumer<Consumer<TaskContainer>>) getItem(position)).accept(taskContainer -> {
                if (!taskContainer.isAtomic()) {
                    ((BasicActivityForConvince) activity).build_intent.accept(new BasicActivityForConvince.Intent_ActivityPairProcessor(intent -> {
                        intent.putExtra("id", taskContainer.getId());
                        intent.putExtra("group", groupID);
                    },
                            ActivityTaskContainer.class));
                } else {
                    Toast.makeText(TaskContainerAdapter.this.context, "cannot add a task that couldn't have child!", Toast.LENGTH_SHORT).show();
                }
            });
        });

        View entireItem = convertView.findViewById(R.id.entire_item);
        entireItem.setLongClickable(true);
        entireItem.setOnLongClickListener(v -> {
            ((Consumer<Consumer<TaskContainer>>) getItem(position)).accept(manager -> {
                new AlertDialog.Builder(context)
                        .setTitle(manager.getId())
                        .setMessage("你确认要删除这个任务组嘛？")
                        .setPositiveButton("删除", ((dialog, which) -> {
//                            ((Consumer<Consumer<TaskContainer>>) getItem(position)).accept(manager_not_above_one -> {
//                                manager_not_above_one.deleteFromRealm();
//                                TaskContainerAdapter.this.notifyDataSetChanged();
//                            });
                            try (Realm defaultInstance = Realm.getDefaultInstance()) {
                                defaultInstance.executeTransaction(t -> {
                                    t.where(TaskTreeManager.class).equalTo("configID", groupID).findFirst() // 100% exist
                                            .find(containerID).peekTaskGroups().get(position).deleteFromRealm();

                                });
                            }
                            TaskContainerAdapter.this.notifyDataSetChanged();
                        })).show();
            });
            return true;
        });

        return convertView;
    }


    static class ViewHolder {
        ImageView logo;
        TextView task_to_be_done;

        TextView task_desc;
        Button complete_current_task;
    }
}
