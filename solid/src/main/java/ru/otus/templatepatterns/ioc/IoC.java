package ru.otus.templatepatterns.ioc;

import java.util.HashMap;
import java.util.Map;

public class IoC {
    private static final Map<String, Object> CONTAINER = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> T resolve(String key) {
        return (T) CONTAINER.get(key);
    }

    public static <T> void register(String key, T value) {
        CONTAINER.put(key, value);
    }
}
