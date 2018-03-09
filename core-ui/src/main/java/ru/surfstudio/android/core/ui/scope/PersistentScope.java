package ru.surfstudio.android.core.ui.scope;


import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Хранилище основных обьектов экрана, необходимых для внутренней логики ядра
 * Также хранит соответствующие даггер компоненты
 * Для каждого активити, фрагмента и WidgetView создается инстанс PersistentScope
 * Не уничтожается при смене конфигурации
 * Для Доступа к этому обьекту следует использовать {@link PersistentScopeStorage}
 */
public class PersistentScope {
    protected final Map<ObjectKey, Object> objects = new HashMap<>();

    private boolean isAdded;

    private final String scopeId;

    public PersistentScope(String scopeId) {
        this.scopeId = scopeId;
    }

    public String getScopeId() {
        return scopeId;
    }

    public void setScopeAdded(boolean added) {
        this.isAdded = added;
    }

    /**
     * Put object to scope
     *
     * @param object
     * @param tag    - key, which used for store object in scope
     */
    public <T> void putObject(T object, String tag) {
        assertScopeAdded();
        ObjectKey key = new ObjectKey(tag);
        objects.put(key, object);
    }

    /**
     * Put object to scope
     *
     * @param object
     * @param clazz  key, which used for store object in scope
     */
    public <T> void putObject(T object, Class<T> clazz) {
        assertScopeAdded();
        ObjectKey key = new ObjectKey(clazz);
        objects.put(key, object);
    }


    /**
     * Put object to scope
     *
     * @param tag - key, which used for store object in scope
     */
    public void deleteObject(String tag) {
        assertScopeAdded();
        ObjectKey key = new ObjectKey(tag);
        objects.put(key, null);
    }

    /**
     * Put object to scope
     *
     * @param clazz key, which used for store object in scope
     */
    public <T> void deleteObject(Class<T> clazz) {
        assertScopeAdded();
        ObjectKey key = new ObjectKey(clazz);
        objects.put(key, null);
    }

    /**
     * @param tag key
     * @return object from scope
     */
    @Nullable
    public <T> T getObject(String tag) {
        assertScopeAdded();
        ObjectKey key = new ObjectKey(tag);
        return (T) objects.get(key);
    }

    /**
     * @param clazz key
     * @return object from scope
     */
    @Nullable
    public <T> T getObject(Class<T> clazz) {
        assertScopeAdded();
        ObjectKey key = new ObjectKey(clazz);
        return clazz.cast(objects.get(key));
    }

    private boolean isScopeAdded() {
        return isAdded;
    }

    private void assertScopeAdded() {
        if (!isScopeAdded()) {
            throw new IllegalStateException("Unsupported operation, PersistentScope is not added to scope storage");
        }
    }

}
