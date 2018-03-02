package ru.surfstudio.android.core.ui.scope;

import android.support.annotation.Nullable;

import ru.surfstudio.android.core.ui.configurator.Configurator;
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.state.ScreenState;

/**
 * PersistentScope для экрана
 */
public abstract class ScreenPersistentScope extends PersistentScope {
    private final ScreenEventDelegateManager screenEventDelegateManager;
    private final ScreenState screenState;
    private final Configurator configurator;

    protected ScreenPersistentScope(ScreenEventDelegateManager screenEventDelegateManager,
                                    ScreenState screenState,
                                    Configurator configurator) {
        super();
        this.screenEventDelegateManager = screenEventDelegateManager;
        this.screenState = screenState;
        this.configurator = configurator;
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
     * @param tag - key, which used for store object in scope
     */
    public void deleteObject(String tag) {
        assertNotDestroyed();
        ObjectKey key = new ObjectKey(tag);
        objects.put(key, null);
    }

    /**
     * Put object to scope
     *
     * @param clazz key, which used for store object in scope
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



}
