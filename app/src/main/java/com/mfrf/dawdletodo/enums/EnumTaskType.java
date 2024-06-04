package com.mfrf.dawdletodo.enums;

import androidx.fragment.app.Fragment;

import com.mfrf.dawdletodo.ui.addtask.AddSingleTaskFragment;

public enum EnumTaskType {
    Daily("daily"), Priority("priority_based"), Single("single_time");

    private final String translationKey;

    EnumTaskType(String translationKey) {
        this.translationKey = translationKey;
    }

    @Override
    public String toString() {
        return "@string/" + this.translationKey;
    }

    public Fragment createFragment() {
        return switch (this) {
            case Daily -> null;
            case Priority -> null;
            case Single -> new AddSingleTaskFragment();

        };
    }
}
