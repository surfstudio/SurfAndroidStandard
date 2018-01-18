package ru.surfstudio.android.core.ui.base.scope;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.destroy.OnDestroyEvent;

public class PersistentScopeStorageContainer extends Fragment {
    private static final String SCOPE_MANAGER_CONTAINER_FRAGMENT_TAG = "scope_manager_container";
    private PersistentScopeStorage persistentScopeStorage;

    public static PersistentScopeStorageContainer getOrCreate(FragmentActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        PersistentScopeStorageContainer container = (PersistentScopeStorageContainer) fragmentManager
                .findFragmentByTag(SCOPE_MANAGER_CONTAINER_FRAGMENT_TAG);
        if (container == null) {
            container = new PersistentScopeStorageContainer();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.add(container, SCOPE_MANAGER_CONTAINER_FRAGMENT_TAG);
            ft.commit();
        }
        return container;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (persistentScopeStorage != null) {
            persistentScopeStorage.getActivityScope().getScreenEventDelegateManager()
                    .sendEvent(new OnDestroyEvent());
        }
    }

    public PersistentScopeStorage getPersistentScopeStorage() {
        return persistentScopeStorage;
    }

    public void setPersistentScopeStorage(PersistentScopeStorage persistentScopeStorage) {
        this.persistentScopeStorage = persistentScopeStorage;
    }


}
