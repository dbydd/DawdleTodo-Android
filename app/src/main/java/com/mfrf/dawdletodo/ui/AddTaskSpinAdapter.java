package com.mfrf.dawdletodo.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mfrf.dawdletodo.enums.EnumTaskType;

public class AddTaskSpinAdapter extends ArrayAdapter<EnumTaskType> {

    private final int resid;

    public AddTaskSpinAdapter(@NonNull Context context, int resource) {
        super(context, resource, EnumTaskType.values());
        this.resid = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        EnumTaskType item = getItem(position);
//        View view = LayoutInflater.from(getContext()).inflate(resid, parent, false);
//        ((TextView) view.findViewById(R.id.holder)).setText(parent.getResources().getIdentifier(item.toString(), "string", view.getContext().getPackageName()));

        TextView v = (TextView) LayoutInflater.from(getContext()).inflate(resid, parent, false);
        v.setText(parent.getResources().getIdentifier(item.toString(), "string", v.getContext().getPackageName()));
        return v;
    }
}
