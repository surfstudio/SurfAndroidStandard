package ru.surfstudio.android.core.ui.base.screen.scope;


import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Хранилище всех PersistentScope в контексте одной активити
 */
public class PersistentScopeStorage {
    private final Map<String, PersistentScope> scopes = new HashMap<>();

    public PersistentScopeStorage() {
    }

    /**
     * сохраняет PersistentScope в хранилище
     * @param scope
     */
    public void putScope(PersistentScope scope) {
        if (scopes.get(scope.getName()) != null) {
            throw new IllegalStateException(String.format(
                    "ScreenScope with name %s already created", scope.getName()));
        }

        if (scope instanceof ActivityPersistentScope && getActivityScope() != null) {
            throw new IllegalStateException("ActivityPersistentScope already created");
        }

        scopes.put(scope.getName(), scope);
    }

    /**
     * удаляет PersistentScope из хранилища
     * @param name
     */
    public void removeScope(String name) {
        scopes.remove(name);
    }

    /**
     *
     * @param name
     * @return PersistentScope с указанным именем
     */
    public @Nullable PersistentScope getByName(String name) {
        return scopes.get(name);
    }

    public ActivityPersistentScope getActivityScope() {
        for (PersistentScope scope : scopes.values()) {
            if (scope instanceof ActivityPersistentScope) {
                return (ActivityPersistentScope) scope;
            }
        }
        return null;
    }

    public FragmentPersistentScope getFragmentScope(String name) {
        PersistentScope persistentScope = getByName(name);
        return (FragmentPersistentScope) persistentScope;
    }

    public WidgetPersistentScope getWidgetScope(String name) {
        PersistentScope persistentScope = getByName(name);
        return (WidgetPersistentScope) persistentScope;
    }
}
