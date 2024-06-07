package com.mfrf.dawdletodo.data_center;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ConfigurationCenter {
    public static final ReadWritePair<Integer> AUTO_SAVE_INTERVAL = new ReadWritePair<>(
            () -> fetchConfigurationValue("auto_save_interval", o -> Integer.parseInt(o.orElse("60"))),
            integer -> setConfigurationValue("auto_dave_interval", integer.toString())
    );

    private static <R> R fetchConfigurationValue(String key, Function<Optional<String>, R> mapper) {
        return mapper.apply(DatabaseHandler.findConfigValue(key));
    }

    private static void setConfigurationValue(String key, String value) {
         DatabaseHandler.saveConfigValue(key, value);
    }


    public record ReadWritePair<T>(Supplier<T> reader, Consumer<T> writer) {

    }
}

