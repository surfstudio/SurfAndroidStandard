package ru.surfstudio.android.core.ui.base.screen.scope;


import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import ru.surfstudio.android.core.ui.base.screen.event.ScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.base.screen.state.ScreenState;

public abstract class PersistentScope<
        DM extends ScreenEventDelegateManager,
        S extends ScreenState> {
    private final Map<ObjectKey, Object> objects = new HashMap<>();
    private final DM screenEventDelegateManager;
    private final S screenState;
    private String name;

    public PersistentScope(String name,
                           DM screenEventDelegateManager,
                           S screenState) {
        this.name = name;
        this.screenEventDelegateManager = screenEventDelegateManager;
        this.screenState = screenState;
    }

    public DM getScreenEventDelegateManager() {
        return screenEventDelegateManager;
    }

    public S getScreenState() {
        return screenState;
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
