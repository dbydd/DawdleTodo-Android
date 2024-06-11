package com.mfrf.dawdletodo.ui.settings;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.mfrf.dawdletodo.R;
import com.mfrf.dawdletodo.data_center.Configuration;
import com.mfrf.dawdletodo.data_center.DatabaseHandler;
import com.mfrf.dawdletodo.databinding.FragmentSettingsBinding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //TODO fixit! settings does't created!
        binding = FragmentSettingsBinding.inflate(inflater, container, false);

        try {
            attachConfigListener(R.id.setting_save_interval, Integer::parseInt, Configuration.class.getDeclaredMethod("setAuto_save_interval", int.class));
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        return binding.getRoot();
    }

    private <V> void attachConfigListener(int id, Function<String, V> mapper, Method consumer) throws InvocationTargetException, IllegalAccessException {
        ((EditText) this.getView().findViewById(id)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // donothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //donothing
            }

            @Override
            public void afterTextChanged(Editable s) {
//                    consumer.invoke(Configuration.Instance, mapper.apply(s.toString()));
                DatabaseHandler.operationConfig("default", c -> {
                    try {
                        consumer.invoke(c, mapper.apply(s.toString()));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }); //fornow only default
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}