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


import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Хранилище всех PersistentScope в контексте одной активити
 */
public class PersistentScopeStorage {
    private final Map<String, PersistentScope> scopes = new HashMap<>();

    /**
     * сохраняет PersistentScope в хранилище
     *
     * @param scope
     */
    public void put(PersistentScope scope) {
        if (scopes.get(scope.getScopeId()) != null) {
            throw new IllegalStateException(String.format(
                    "ScreenScope with name %s already created", scope.getScopeId()));
        }

        scopes.put(scope.getScopeId(), scope);
        scope.setScopeAdded(true);
    }

    /**
     * удаляет PersistentScope из хранилища
     *
     * @param name
     */
    public void remove(String name) {
        scopes.get(name).setScopeAdded(false);
        scopes.remove(name);
    }

    public boolean isExist(String name) {
        return scopes.containsKey(name);
    }

    /**
     * @param name
     * @return PersistentScope с указанным именем
     */
    public @NonNull
    PersistentScope get(String name) {
        PersistentScope persistentScope = scopes.get(name);
        if (persistentScope == null) {
            throw new IllegalStateException(String.format("Something went wrong, PersistentScope with name %s doest not exist", name));
        }
        return persistentScope;
    }

    public @NonNull
    <P extends PersistentScope> P get(String name,
                                      Class<P> scopeClass) {
        PersistentScope persistentScope = get(name);
        if (!scopeClass.isInstance(persistentScope)) {
            throw new IllegalStateException(String.format("PersistentScope with name %s is not instance of %s", name, scopeClass.getCanonicalName()));
        }
        return scopeClass.cast(persistentScope);
    }
}
