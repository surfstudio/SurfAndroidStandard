package ru.surfstudio.android.core.ui.base.scope;


import android.support.v4.app.FragmentActivity;

import ru.surfstudio.android.core.ui.base.event.delegate.ScreenEventResolverHelper;

public class PersistentScopeManagerInitializer {

    public PersistentScopeManager init(FragmentActivity fragmentActivity) {
        PersistentScopeManagerContainer container = PersistentScopeManagerContainer.getOrCreate(fragmentActivity);
        if(container.getPersistentScopeManager() == null){
            container.setPersistentScopeManager(createPersistentScopeManager());
        }
        return container.getPersistentScopeManager();
    }

    protected PersistentScopeManager createPersistentScopeManager() {
        return new PersistentScopeManager(ScreenEventResolverHelper.standardEventResolvers());
    }
}
