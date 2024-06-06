package com.mfrf.dawdletodo.enums;

import androidx.fragment.app.Fragment;

import com.mfrf.dawdletodo.ui.addtask.AddAtomicTaskFragment;

public enum EnumTaskType {
    Daily("daily_task"), Priority("priority_based_task"), Atomic("atomic_task");

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
            case Daily -> null;
            case Priority -> null;
            case Atomic -> new AddAtomicTaskFragment();

        };
    }
}
