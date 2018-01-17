package ru.surfstudio.android.core.ui.base.scope;


import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.base.event.delegate.BaseScreenEventDelegateManager;

public abstract class PersistentScope<DM extends BaseScreenEventDelegateManager> {
    private final Map<ObjectKey, Object> objects = new HashMap<>();
    private final DM screenEventDelegateManager;
    private String name;
    private ScreenType screenType;
    private boolean viewRecreated;
    private boolean destroyed = false;

    public PersistentScope(String name,
                           ScreenType screenType,
                           DM screenEventDelegateManager) {
        this.name = name;
        this.screenType = screenType;
        this.screenEventDelegateManager = screenEventDelegateManager;
    }

    public DM getScreenEventDelegateManager() {
        return screenEventDelegateManager;
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
        if (destroyed) {
            throw new IllegalStateException("Unsupported operation, PersistentScope is destroyed");
        }
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public ScreenType getScreenType() {
        return screenType;
    }

    public String getName() {
        return name;
    }

    public boolean isViewRecreated() {
        return viewRecreated;
    }

    public void setViewRecreated(boolean viewRecreated) {
        this.viewRecreated = viewRecreated;
    }
}
