package com.mfrf.dawdletodo.enums;

import androidx.fragment.app.Fragment;

import com.mfrf.dawdletodo.ui.addtask.AddAtomicTaskFragment;
import com.mfrf.dawdletodo.ui.addtask.AddNormalTaskContainersFragment;

public enum EnumTaskType {
    Priority("priority_based_task"), Atomic("atomic_task");

    private final String translationKey;

    EnumTaskType(String translationKey) {
        this.translationKey = translationKey;
    }

    @Override
    public String toString() {
        return this.translationKey;
    }

    public Fragment createFragment() {
        return switch (this) {
            case Priority -> new AddNormalTaskContainersFragment(this);
            case Atomic -> new AddAtomicTaskFragment();

        };
    }
}
