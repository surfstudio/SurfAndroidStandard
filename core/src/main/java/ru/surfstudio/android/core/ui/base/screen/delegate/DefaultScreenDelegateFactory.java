package ru.surfstudio.android.core.ui.base.screen.delegate;

//todo

import android.support.v4.app.FragmentActivity;

import ru.surfstudio.android.core.ui.base.event.delegate.ScreenEventResolverHelper;
import ru.surfstudio.android.core.ui.base.scope.PersistentScopeStorage;
import ru.surfstudio.android.core.ui.base.scope.PersistentScopeStorageContainer;
import ru.surfstudio.android.core.ui.base.screen.activity.BaseActivityInterface;

public class DefaultScreenDelegateFactory {

    public <A extends FragmentActivity & BaseActivityInterface> BaseActivityDelegate createBaseActivtyDelegate(A activity) {
        return new BaseActivityDelegate(
                activity,
                getScopeStorage(activity),
                ScreenEventResolverHelper.standardEventResolvers()
        );
    }

    private PersistentScopeStorage getScopeStorage(FragmentActivity activity) {
        PersistentScopeStorageContainer container = PersistentScopeStorageContainer.getOrCreate(activity);
        if (container.getPersistentScopeStorage() == null) {
            container.setPersistentScopeStorage(new PersistentScopeStorage());
        }
        return container.getPersistentScopeStorage();
    }
}
