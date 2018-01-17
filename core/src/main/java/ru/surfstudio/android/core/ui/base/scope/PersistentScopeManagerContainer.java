package ru.surfstudio.android.core.ui.base.scope;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class PersistentScopeManagerContainer extends Fragment {
    private static final String SCOPE_MANAGER_CONTAINER_FRAGMENT_TAG = "scope_manager_container";
    private PersistentScopeManager persistentScopeManager;

    public static PersistentScopeManagerContainer getOrCreate(FragmentActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        PersistentScopeManagerContainer container = (PersistentScopeManagerContainer) fragmentManager
                .findFragmentByTag(SCOPE_MANAGER_CONTAINER_FRAGMENT_TAG);
        if (container == null) {
            container = new PersistentScopeManagerContainer();
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
        if (persistentScopeManager != null) {
            persistentScopeManager.onFinallyDestroy(); //todo каждый сам определяет
        }
    }

    public PersistentScopeManager getPersistentScopeManager() {
        return persistentScopeManager;
    }

    public void setPersistentScopeManager(PersistentScopeManager persistentScopeManager) {
        this.persistentScopeManager = persistentScopeManager;
    }


}
