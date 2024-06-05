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
import com.mfrf.dawdletodo.model.Task;
import com.mfrf.dawdletodo.model.task_container.AbstractTaskContainer;
import com.mfrf.dawdletodo.utils.BasicActivityForConvince;

import java.util.Optional;

public class TaskContainerAdapter extends BaseAdapter {
    private final Context context;
    private final FragmentActivity activity;
    private AbstractTaskContainer container;
    private final String groupID;

    public TaskContainerAdapter(Context context, FragmentActivity activity, AbstractTaskContainer container, String groupID) {
        this.context = context;
        this.activity = activity;
        this.container = container;
        this.groupID = groupID;
    }

    @Override
    public int getCount() {
        return container.peekTaskGroups().size();
    }

    @Override
    public Object getItem(int position) {
        return container.peekTaskGroups().get(position);
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

//        taskgroupdataentryentry item = itemlist.get(position);
        AbstractTaskContainer item = (AbstractTaskContainer) getItem(position);
        Optional<Task> peeked = item.peek_task();


        viewHolder.logo.setImageResource(R.drawable.todos);
        viewHolder.group_describe.setText(item.getContainerID());
        viewHolder.task_desc.setText(peeked.isPresent() ? peeked.get().getDescription() : "null");

        viewHolder.complete_current_task.setOnClickListener(view -> {
            item.markAsDone();
        });


        convertView.findViewById(R.id.actually_button_to_lower).setOnClickListener(view -> {
            if (item.couldHasChild()) {
                ((BasicActivityForConvince) activity).build_intent.accept(new BasicActivityForConvince.Intent_ActivityPairProcessor(intent -> {
                    intent.putExtra("id", item.getContainerID());
                    intent.putExtra("group", groupID);
                },
                        ActivityTaskContainer.class));
            } else {
                Toast.makeText(TaskContainerAdapter.this.context, "cannot add a task that couldn't have child!", Toast.LENGTH_LONG).show();
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
