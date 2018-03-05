package ru.surfstudio.android.core.ui.scope;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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

    public @NonNull
    ActivityPersistentScope getActivityScope() {
        String activityScopeName = getActivityScopeName();
        if (activityScopeName == null) {
            throw new IllegalStateException("ActivityPersistentScope doest not exist");
        }
        return get(activityScopeName, ActivityPersistentScope.class);
    }

    private @Nullable
    String getActivityScopeName() {
        for (Map.Entry<String, PersistentScope> entry : scopes.entrySet()) {
            if (entry.getValue() instanceof ActivityPersistentScope) {
                return entry.getKey();
            }
        }
        return null;
    }
}
