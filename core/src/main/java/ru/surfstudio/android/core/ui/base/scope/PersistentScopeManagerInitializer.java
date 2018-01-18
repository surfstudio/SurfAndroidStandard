package ru.surfstudio.android.core.ui.base.scope;


import android.support.v4.app.FragmentActivity;

import ru.surfstudio.android.core.ui.base.event.delegate.ScreenEventResolverHelper;

public class PersistentScopeManagerInitializer {

    public PersistentScopeStorage init(FragmentActivity fragmentActivity) {
        PersistentScopeStorageContainer container = PersistentScopeStorageContainer.getOrCreate(fragmentActivity);
        if (container.getPersistentScopeStorage() == null) {
            container.setPersistentScopeStorage(createPersistentScopeManager());
        }
        return container.getPersistentScopeStorage();
    }

    protected PersistentScopeStorage createPersistentScopeManager() {
        return new PersistentScopeStorage(ScreenEventResolverHelper.standardEventResolvers());
    }
}
