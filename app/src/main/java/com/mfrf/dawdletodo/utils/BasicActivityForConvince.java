package com.mfrf.dawdletodo.utils;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import java.util.function.Consumer;

public abstract class BasicActivityForConvince extends AppCompatActivity {
    public final Consumer<Intent_ActivityPairProcessor> build_intent = pair -> {
        Intent intent = new Intent(this, pair.aClass);

        pair.processor.accept(intent);

        startActivity(intent);
    };

    public record Intent_ActivityPairProcessor(Consumer<Intent> processor,
                                                  Class<? extends AppCompatActivity> aClass){

    }

}
