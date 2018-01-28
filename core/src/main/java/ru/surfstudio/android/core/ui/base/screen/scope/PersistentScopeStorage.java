package ru.surfstudio.android.core.ui.base.screen.scope;


import java.util.HashMap;
import java.util.Map;

//todo package local constructor

/**
 * Хранилище всех PersistentScope в контексте одной активити
 */
public class PersistentScopeStorage {
    private final Map<String, PersistentScope> scopes = new HashMap<>();

    public PersistentScopeStorage() {
    }

    public void putScope(PersistentScope scope) { //todo check activity
        if (scopes.get(scope.getName()) != null) {
            throw new IllegalStateException(String.format(
                    "ScreenScope with name %s already created", scope.getName()));
        }

        if (scope instanceof ActivityPersistentScope && getActivityScope() != null) {
            throw new IllegalStateException("ActivityPersistentScope already created");
        }

        scopes.put(scope.getName(), scope);
    }

    public void removeScope(String name) {
        scopes.remove(name);
    }

    public PersistentScope getByName(String name) {
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

}
