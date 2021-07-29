package dev.lightdream.ultraprisoncore.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import dev.lightdream.ultraprisoncore.UltraPrisonCore;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;

@SuppressWarnings("unused")
public class Persist {

    private final ObjectMapper objectMapper;
    private final PersistType persistType;
    private final UltraPrisonCore plugin;

    public Persist(UltraPrisonCore plugin, PersistType persistType) {
        this.plugin = plugin;
        this.persistType = persistType;
        this.objectMapper = new ObjectMapper(persistType.getFactory());
    }

    private static String getName(Class<?> clazz) {
        return clazz.getSimpleName().toLowerCase();
    }

    public static String getName(Object instance) {
        return getName(instance.getClass());
    }

    public static String getName(Type type) {
        return getName(type.getClass());
    }

    public File getFile(String name) {
        return new File(plugin.getDataFolder(), name + persistType.getExtension());
    }

    public File getFile(Class<?> clazz) {
        return getFile(getName(clazz));
    }

    public File getFile(Object instance) {
        return getFile(getName(instance));
    }

    public void save(Object instance) {
        save(instance, getFile(instance));
    }

    private void save(Object instance, File file) {
        try {
            objectMapper.writeValue(file, instance);
        } catch (IOException e) {
            System.out.println("Failed to save " + file.toString() + ": " + e.getMessage());
        }
    }

    public String toString(Object instance) {
        try {
            return objectMapper.writeValueAsString(instance);
        } catch (IOException e) {
            System.out.println("Failed to save " + instance.toString() + ": " + e.getMessage());
        }
        return "";
    }

    public <T> T load(Class<T> clazz) {
        return load(clazz, getFile(clazz));
    }

    public <T> T load(Class<T> clazz, File file) {
        if (file.exists()) {
            try {
                return objectMapper.readValue(file, clazz);
            } catch (IOException e) {
                System.out.println("Failed to parse " + file + ": " + e.getMessage());
            }
        }
        try {
            save(clazz.newInstance());
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T load(Class<T> clazz, String content) {
        try {
            return objectMapper.readValue(content, clazz);
        } catch (IOException ignored) {
        }

        return null;
    }

    public enum PersistType {

        YAML(".yml", new YAMLFactory()),
        JSON(".json", new JsonFactory());

        private final String extension;
        private final JsonFactory factory;

        PersistType(String extension, JsonFactory factory) {
            this.extension = extension;
            this.factory = factory;
        }

        public String getExtension() {
            return extension;
        }

        public JsonFactory getFactory() {
            return factory;
        }
    }


}
