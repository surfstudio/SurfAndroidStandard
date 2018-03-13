package ru.surfstudio.android.core.ui.delegate.factory;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.util.List;

import ru.surfstudio.android.core.ui.activity.CoreActivityInterface;
import ru.surfstudio.android.core.ui.delegate.activity.ActivityCompletelyDestroyChecker;
import ru.surfstudio.android.core.ui.delegate.activity.ActivityDelegate;
import ru.surfstudio.android.core.ui.delegate.fragment.FragmentCompletelyDestroyChecker;
import ru.surfstudio.android.core.ui.delegate.fragment.FragmentDelegate;
import ru.surfstudio.android.core.ui.delegate.fragment.ParentActivityPersistentScopeFinder;
import ru.surfstudio.android.core.ui.event.ScreenEventResolverHelper;
import ru.surfstudio.android.core.ui.event.base.resolver.ScreenEventResolver;
import ru.surfstudio.android.core.ui.fragment.CoreFragmentInterface;
import ru.surfstudio.android.core.ui.scope.PersistentScopeStorage;
import ru.surfstudio.android.core.ui.scope.PersistentScopeStorageContainer;

/**
 * Фабрика делегатов экранов по умолчанию, предоставляет стандартные делегаты
 */
public class DefaultScreenDelegateFactory implements ScreenDelegateFactory {

    @Override
    public <A extends FragmentActivity & CoreActivityInterface> ActivityDelegate createActivityDelegate(A activity) {
        return new ActivityDelegate(
                activity,
                getScopeStorage(),
                getEventResolvers(),
                new ActivityCompletelyDestroyChecker(activity)
        );
    }

    @Override
    public <A extends Fragment & CoreFragmentInterface> FragmentDelegate createFragmentDelegate(A fragment) {
        return new FragmentDelegate(
                fragment,
                getScopeStorage(),
                getEventResolvers(),
                new FragmentCompletelyDestroyChecker(fragment)
        );
    }

    @NonNull
    protected List<ScreenEventResolver> getEventResolvers() {
        return ScreenEventResolverHelper.standardEventResolvers();
    }

    @NonNull
    protected PersistentScopeStorage getScopeStorage() {
        return PersistentScopeStorageContainer.getPersistentScopeStorage();
    }
}
