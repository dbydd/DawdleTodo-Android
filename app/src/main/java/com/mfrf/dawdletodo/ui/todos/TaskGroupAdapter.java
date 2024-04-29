package com.mfrf.dawdletodo.ui.todos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mfrf.dawdletodo.R;

import java.util.List;

public class TaskGroupAdapter extends BaseAdapter {
    private Context context;
    private List<ItemData> itemList;

    public TaskGroupAdapter(Context context, List<ItemData> itemList) {
        this.context = context;
        this.itemList = itemList;
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
            // 使用 item 模板创建新的视图
            convertView = LayoutInflater.from(context).inflate(R.layout.task_group_item, parent, false);

            // 创建 ViewHolder 对象并保存视图组件的引用
            viewHolder = new ViewHolder();
            viewHolder.imageView = convertView.findViewById(R.id.imageView);
            viewHolder.textView1 = convertView.findViewById(R.id.textView1);
            viewHolder.textView2 = convertView.findViewById(R.id.textView2);

            // 将 ViewHolder 对象保存在 convertView 中
            convertView.setTag(viewHolder);
        } else {
            // 从 convertView 中获取之前保存的 ViewHolder 对象
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 获取当前位置的数据项
        ItemData item = itemList.get(position);

        // 设置 ImageView 的图片资源
        viewHolder.imageView.setImageResource(item.getImageResId());

        // 设置 TextView1 和 TextView2 的文本内容
        viewHolder.textView1.setText(item.getId());
        viewHolder.textView2.setText(item.getDescribe());

        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView textView1;
        TextView textView2;
    }
}

class ItemData {
    private int imageResId;
    private String id;
    private String describe;

    public ItemData(int imageResId, String id, String describe) {
        this.imageResId = imageResId;
        this.id = id;
        this.describe = describe;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getId() {
        return id;
    }

    public String getDescribe() {
        return describe;
    }
}
