package ru.surfstudio.android.core.ui.base.scope;


import java.util.HashMap;
import java.util.Map;

import ru.surfstudio.android.core.ui.base.scope.activity.ActivityPersistentScope;
import ru.surfstudio.android.core.ui.base.scope.fragment.FragmentPersistentScope;

//todo package local constructor

/**
 * Хранилище всех PersistentScope в контексте одной активити
 */
public class PersistentScopeStorage {
    private final Map<String, FragmentPersistentScope> fragmentScopes = new HashMap<>();
    private ActivityPersistentScope activityScope;

    public PersistentScopeStorage() {
    }

    public FragmentPersistentScope getFragmentScope(String name) {
        return fragmentScopes.get(name);
    }

    public ActivityPersistentScope getActivityScope() {
        return activityScope;
    }

    public void putActivityScope(ActivityPersistentScope activityScope) {
        if (this.activityScope != null) {
            throw new IllegalStateException("ActivityPersistentScope already created");
        }
        this.activityScope = activityScope;
    }

    public void putFragmentScope(FragmentPersistentScope fragmentScope) {
        if (getFragmentScope(fragmentScope.getName()) != null) {
            throw new IllegalStateException(String.format(
                    "FragmentScreenScope with name %s already created",
                    fragmentScope.getName()));
        }

        fragmentScopes.put(fragmentScope.getName(), fragmentScope);
    }
}
