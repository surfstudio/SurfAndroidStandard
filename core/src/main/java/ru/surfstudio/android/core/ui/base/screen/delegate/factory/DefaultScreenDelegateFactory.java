package ru.surfstudio.android.core.ui.base.screen.delegate.factory;

//todo

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.util.List;

import ru.surfstudio.android.core.ui.base.event.delegate.ScreenEventResolverHelper;
import ru.surfstudio.android.core.ui.base.event.delegate.base.resolver.ScreenEventResolver;
import ru.surfstudio.android.core.ui.base.screen.activity.CoreActivityInterface;
import ru.surfstudio.android.core.ui.base.screen.activity.CoreActivityViewInterface;
import ru.surfstudio.android.core.ui.base.screen.delegate.activity.ActivityCompletelyDestroyChecker;
import ru.surfstudio.android.core.ui.base.screen.delegate.activity.ActivityDelegate;
import ru.surfstudio.android.core.ui.base.screen.delegate.activity.ActivityViewDelegate;
import ru.surfstudio.android.core.ui.base.screen.delegate.fragment.FragmentCompletelyDestroyChecker;
import ru.surfstudio.android.core.ui.base.screen.delegate.fragment.FragmentDelegate;
import ru.surfstudio.android.core.ui.base.screen.delegate.fragment.FragmentViewDelegate;
import ru.surfstudio.android.core.ui.base.screen.fragment.CoreFragmentInterface;
import ru.surfstudio.android.core.ui.base.screen.fragment.CoreFragmentViewInterface;
import ru.surfstudio.android.core.ui.base.screen.scope.PersistentScopeStorage;
import ru.surfstudio.android.core.ui.base.screen.scope.PersistentScopeStorageContainer;


public class DefaultScreenDelegateFactory implements ScreenDelegateFactory {

    @Override
    public <A extends FragmentActivity & CoreActivityInterface> ActivityDelegate createActivityDelegate(A activity) {
        return new ActivityDelegate(
                activity,
                getScopeStorage(activity),
                getEventResolvers(),
                new ActivityCompletelyDestroyChecker(activity)
        );
    }

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
    public <A extends Fragment & CoreFragmentInterface> FragmentDelegate createFragmentDelegate(A fragment) {
        return new FragmentDelegate(
                fragment,
                getScopeStorage(fragment.getActivity()),
                getEventResolvers(),
                new FragmentCompletelyDestroyChecker(fragment)
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
    private PersistentScopeStorage getScopeStorage(FragmentActivity activity) {
        return PersistentScopeStorageContainer.getFrom(activity);
    }
}
