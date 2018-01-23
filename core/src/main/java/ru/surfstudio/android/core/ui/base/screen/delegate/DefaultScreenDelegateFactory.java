package ru.surfstudio.android.core.ui.base.screen.delegate;

//todo

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.util.List;

import ru.surfstudio.android.core.ui.base.event.delegate.ScreenEventResolverHelper;
import ru.surfstudio.android.core.ui.base.event.delegate.base.resolver.ScreenEventResolver;
import ru.surfstudio.android.core.ui.base.scope.PersistentScopeStorage;
import ru.surfstudio.android.core.ui.base.scope.PersistentScopeStorageContainer;
import ru.surfstudio.android.core.ui.base.screen.activity.BaseActivityInterface;
import ru.surfstudio.android.core.ui.base.screen.fragment.BaseFragmentInterface;
import ru.surfstudio.android.core.ui.base.screen.view.core.PresenterHolderActivityCoreView;
import ru.surfstudio.android.core.ui.base.screen.view.core.PresenterHolderFragmentCoreView;


public class DefaultScreenDelegateFactory implements ScreenDelegateFactory {

    @Override
    public <A extends FragmentActivity & BaseActivityInterface> BaseActivityDelegate createBaseActivtyDelegate(A activity) {
        return new BaseActivityDelegate(
                activity,
                getScopeStorage(activity),
                getEventResolvers()
        );
    }

    @Override
    public <A extends FragmentActivity & PresenterHolderActivityCoreView> MvpActivityViewDelegate createMvpActivityViewDelegate(A activity) {
        return new MvpActivityViewDelegate(
                activity,
                getScopeStorage(activity),
                getEventResolvers()
        );
    }

    @Override
    public <A extends Fragment & BaseFragmentInterface> BaseFragmentDelegate createBaseFragmentDelegate(A fragment) {
        return new BaseFragmentDelegate(
                fragment,
                getScopeStorage(fragment.getActivity()),
                getEventResolvers()
        );
    }

    @Override
    public <A extends Fragment & PresenterHolderFragmentCoreView> MvpFragmentViewDelegate createMvpFragmentViewDelegate(A fragment) {
        return new MvpFragmentViewDelegate(
                fragment,
                getScopeStorage(fragment.getActivity()),
                getEventResolvers()
        );
    }

    @NonNull
    protected List<ScreenEventResolver> getEventResolvers() {
        return ScreenEventResolverHelper.standardEventResolvers();
    }

    @NonNull
    private PersistentScopeStorage getScopeStorage(FragmentActivity activity) {
        PersistentScopeStorageContainer container = PersistentScopeStorageContainer.getOrCreate(activity);
        if (container.getPersistentScopeStorage() == null) {
            container.setPersistentScopeStorage(new PersistentScopeStorage());
        }
        return container.getPersistentScopeStorage();
    }
}
