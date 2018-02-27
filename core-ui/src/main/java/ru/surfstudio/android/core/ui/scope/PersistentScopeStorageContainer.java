package ru.surfstudio.android.core.ui.scope;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * контейнер для {@link PersistentScopeStorage}
 * По сути является Fragment с setRetainInstance(true)
 */
public class PersistentScopeStorageContainer extends Fragment {
    private static final String SCOPE_STORAGE_CONTAINER_FRAGMENT_TAG = "scope_storage_container";
    private PersistentScopeStorage persistentScopeStorage;

    public static PersistentScopeStorage getFrom(FragmentActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        PersistentScopeStorageContainer container = (PersistentScopeStorageContainer) fragmentManager
                .findFragmentByTag(SCOPE_STORAGE_CONTAINER_FRAGMENT_TAG);
        if (container == null) {
            container = new PersistentScopeStorageContainer();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.add(container, SCOPE_STORAGE_CONTAINER_FRAGMENT_TAG);
            ft.commit();
        }
        return container.getPersistentScopeStorage();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    private PersistentScopeStorage getPersistentScopeStorage() {
        if (persistentScopeStorage == null) {
            persistentScopeStorage = new PersistentScopeStorage();
        }
        return persistentScopeStorage;
    }
}
