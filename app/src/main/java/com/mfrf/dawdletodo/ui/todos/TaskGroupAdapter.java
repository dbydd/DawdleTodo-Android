package com.mfrf.dawdletodo.ui.todos;

import android.content.Context;
import android.util.Pair;
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
import com.mfrf.dawdletodo.data_center.MemoryDataBase;
import com.mfrf.dawdletodo.model.task_container.AbstractTaskContainer;

import java.util.List;
import java.util.stream.Collectors;

public class TaskGroupAdapter extends BaseAdapter {
    private final List<TaskGroupDataEntry> itemList;
    private Context context;
    private final FragmentActivity activity;

    public TaskGroupAdapter(Context context, FragmentActivity activity) {
        this.context = context;
        this.itemList = MemoryDataBase.INSTANCE.TASK_GROUPS.entrySet().stream().map(kv -> Pair.create(kv, kv.getValue().advice())).filter(p -> p.second.isPresent()).map(p -> new TaskGroupDataEntry(R.drawable.todos, p.first.getKey(), "tasks: " + p.first.getValue().countItems(), p.second.get().second.clone(), p.second.get().first)).collect(Collectors.toList());
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
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
            viewHolder.group_describe = convertView.findViewById(R.id.task_group_describe);
            viewHolder.task_desc = convertView.findViewById(R.id.task_desc);
            viewHolder.complete_current_task = convertView.findViewById(R.id.complete_current_task);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        TaskGroupDataEntry item = itemList.get(position);

        viewHolder.logo.setImageResource(item.getImageResId());
        viewHolder.group_describe.setText(item.getDescribe());
        viewHolder.task_desc.setText(item.getTaskDesc());

        viewHolder.complete_current_task.setOnClickListener(view -> {
            MemoryDataBase.INSTANCE.query(item.getGroupID(), item.getTaskContainerID()).ifPresent(AbstractTaskContainer::markAsDone);
        });


        convertView.findViewById(R.id.actually_button_to_lower).setOnClickListener(view -> {
            ((MainActivity) activity).build_intent.accept(Pair.create(intent -> {
                        intent.putExtra("id", item.getGroupID());
                    },
                    ActivityTaskContainer.class));
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

