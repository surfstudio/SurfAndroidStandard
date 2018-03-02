package ru.surfstudio.android.core.mvp.delegate.factory;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.util.List;

import ru.surfstudio.android.core.mvp.activity.CoreActivityViewInterface;
import ru.surfstudio.android.core.mvp.delegate.ActivityViewDelegate;
import ru.surfstudio.android.core.mvp.delegate.FragmentViewDelegate;
import ru.surfstudio.android.core.mvp.fragment.CoreFragmentViewInterface;
import ru.surfstudio.android.core.ui.delegate.activity.ActivityCompletelyDestroyChecker;
import ru.surfstudio.android.core.ui.delegate.fragment.FragmentCompletelyDestroyChecker;
import ru.surfstudio.android.core.ui.event.ScreenEventResolverHelper;
import ru.surfstudio.android.core.ui.event.base.resolver.ScreenEventResolver;
import ru.surfstudio.android.core.ui.scope.PersistentScopeStorage;
import ru.surfstudio.android.core.ui.scope.PersistentScopeStorageContainer;


/**
 * Фабрика делегатов MVP экранов по умолчанию, предоставляет стандартные делегаты
 */
public class DefaultMvpScreenDelegateFactory implements MvpScreenDelegateFactory {

    @Override
    public <A extends FragmentActivity & CoreActivityViewInterface> ActivityViewDelegate createActivityViewDelegate(A activity) {
        return new ActivityViewDelegate(
                activity,
                getScopeStorage(activity),
                getEventResolvers(),
                new ActivityCompletelyDestroyChecker(activity)
        );
    }

    @Override
    public <A extends Fragment & CoreFragmentViewInterface> FragmentViewDelegate createFragmentViewDelegate(A fragment) {
        return new FragmentViewDelegate(
                fragment,
                getScopeStorage(fragment.getActivity()),
                getEventResolvers(),
                new FragmentCompletelyDestroyChecker(fragment)
        );
    }

    @NonNull
    protected List<ScreenEventResolver> getEventResolvers() {
        return ScreenEventResolverHelper.standardEventResolvers();
    }

    @NonNull
    protected PersistentScopeStorage getScopeStorage(FragmentActivity activity) {
        return PersistentScopeStorageContainer.getPersistentScopeStorage();
    }
}
