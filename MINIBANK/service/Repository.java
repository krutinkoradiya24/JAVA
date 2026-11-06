package service;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import model.annotation.Id;
public class Repository<T> {
    private final Map<String, T> items = new HashMap<>();
    public T save(T entity) {
        items.put(readId(entity), entity);
        return entity;
    }
    @SafeVarargs
    public final void saveAll(T... entities) {
        for (T entity : entities) {
            save(entity);
        }
    }
    public T findById(String id) {
        return items.get(id);
    }
    public ArrayList<T> findAll() {
        return new ArrayList<>(items.values());
    }
    public boolean delete(String id) {
        return items.remove(id) != null;
    }
    private String readId(T entity) {
        Class<?> type = entity.getClass();
        while (type != null) {
            for (Field field : type.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    try {
                        field.setAccessible(true);
                        Object value = field.get(entity);
                        if (value == null) {
                            throw new IllegalArgumentException("@Id value cannot be null.");
                        }
                        return value.toString();
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Could not read @Id field.", exception);
                    }
                }
            }
            type = type.getSuperclass();
        }
        throw new IllegalArgumentException("No field has the @Id annotation.");
    }
}
