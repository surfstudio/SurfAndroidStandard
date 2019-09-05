/*
  Copyright (c) 2018-present, SurfStudio LLC, Maxim Tuev.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.core.ui.scope;


import androidx.annotation.Nullable;

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
