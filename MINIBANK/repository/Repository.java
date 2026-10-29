package repository;

import model.annotation.Id;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class Repository<T> {
    private final ConcurrentHashMap<String, T> storage = new ConcurrentHashMap<>();

    public T save(T entity) {
        if (entity == null) {
            return null;
        }

        String key = null;
        Class<?> clazz = entity.getClass();

        while (clazz != null && clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    field.setAccessible(true);
                    try {
                        Object val = field.get(entity);
                        if (val != null) {
                            key = val.toString();
                            break;
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (key != null) break;
            clazz = clazz.getSuperclass();
        }

        if (key != null) {
            storage.put(key, entity);
        }
        return entity;
    }

    public T findById(String id) {
        if (id == null) return null;
        return storage.get(id);
    }

    public ArrayList<T> findAll() {
        return new ArrayList<>(storage.values());
    }

    public boolean delete(String id) {
        if (id == null) return false;
        return storage.remove(id) != null;
    }

    @SafeVarargs
    public final void saveAll(T... items) {
        for (T item : items) {
            save(item);
        }
    }

    public static <N extends Number> double average(N[] values) {
        if (values == null || values.length == 0) return 0.0;
        double sum = 0.0;
        for (N val : values) {
            sum += val.doubleValue();
        }
        return sum / values.length;
    }
}

