package com.mfrf.dawdletodo.data_center;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.mfrf.dawdletodo.model.TaskGroup;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiresApi(api = Build.VERSION_CODES.O)
public enum MemoryDataBase {
    INSTANCE();
    private final HashMap<String, TaskGroup> TASK_GROUPS = new HashMap<>();

    static {
        for (int i = 1; i <11; i++) {
            MemoryDataBase.INSTANCE.add_task_group(new TaskGroup("task_"+i,LocalDate.of(2024,i,1),LocalDate.of(2024,i+1,1)));
        }
    }

    public TaskGroup get_task_group(String id) {
        return this.TASK_GROUPS.get(id);
    }

    public TaskGroup remove_task_group(String id){
        return this.TASK_GROUPS.remove(id);
    }

    public boolean add_task_group(TaskGroup g){
        return this.TASK_GROUPS.put(g.getGroup_id(),g) == null;
    }

    public <T> List<T> request(Function<TaskGroup,T> f){
        return this.TASK_GROUPS.values().stream().map(f).collect(Collectors.toList());
    }
}
