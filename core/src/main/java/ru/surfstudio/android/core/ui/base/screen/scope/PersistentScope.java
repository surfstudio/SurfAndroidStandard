package ru.surfstudio.android.core.ui.base.screen.scope;


import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import ru.surfstudio.android.core.ui.base.screen.configurator.Configurator;
import ru.surfstudio.android.core.ui.base.screen.event.ScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.base.screen.state.ScreenState;

/**
 * Хранилище основных обьектов экрана, необходимых для внутренней логики ядра
 * Также хранит соответствующие даггер компоненты
 * Для каждого активити, фрагмента и WidgetView создается инстанс PersistentScope
 * Не уничтожается при смене конфигурации
 * Для Доступа к этому обьекту следует использовать {@link PersistentScopeStorage}
 *
 */
public abstract class PersistentScope {
    private final Map<ObjectKey, Object> objects = new HashMap<>();
    private final ScreenEventDelegateManager screenEventDelegateManager;
    private final ScreenState screenState;
    private final Configurator configurator;
    private final String name;

    public PersistentScope(ScreenEventDelegateManager screenEventDelegateManager,
                           ScreenState screenState,
                           Configurator configurator,
                           String name) {
        this.screenEventDelegateManager = screenEventDelegateManager;
        this.screenState = screenState;
        this.configurator = configurator;
        this.name = name;
    }

    public ScreenEventDelegateManager getScreenEventDelegateManager() {
        return screenEventDelegateManager;
    }

    public ScreenState getScreenState() {
        return screenState;
    }

    public Configurator getConfigurator() {
        return configurator;
    }

    /**
     * Put object to scope
     *
     * @param object
     * @param tag    - key, which used for store object in scope
     */
    public <T> void putObject(T object, String tag) {
        assertNotDestroyed();
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
        assertNotDestroyed();
        ObjectKey key = new ObjectKey(clazz);
        objects.put(key, object);
    }

    /**
     * Put object to scope
     *
     * @param tag    - key, which used for store object in scope
     */
    public void deleteObject(String tag) {
        assertNotDestroyed();
        ObjectKey key = new ObjectKey(tag);
        objects.put(key, null);
    }

    /**
     * Put object to scope
     *
     * @param clazz  key, which used for store object in scope
     */
    public <T> void deleteObject(Class<T> clazz) {
        assertNotDestroyed();
        ObjectKey key = new ObjectKey(clazz);
        objects.put(key, null);
    }

    /**
     * @param tag key
     * @return object from scope
     */
    @Nullable
    public <T> T getObject(String tag) {
        assertNotDestroyed();
        ObjectKey key = new ObjectKey(tag);
        return (T) objects.get(key);
    }

    /**
     * @param clazz key
     * @return object from scope
     */
    @Nullable
    public <T> T getObject(Class<T> clazz) {
        assertNotDestroyed();
        ObjectKey key = new ObjectKey(clazz);
        return clazz.cast(objects.get(key));
    }

    private void assertNotDestroyed() {
        if (isDestroyed()) {
            throw new IllegalStateException("Unsupported operation, PersistentScope is destroyed");
        }
    }

    private boolean isDestroyed() {
        return screenState.isCompletelyDestroyed();
    }

    public String getName() {
        return name;
    }
}
