package ru.surfstudio.standard.ui.common.contentcontainer;

import android.app.FragmentTransaction;

import javax.inject.Inject;

import ru.surfstudio.android.core.app.bus.RxBus;
import ru.surfstudio.android.core.app.dagger.scope.PerScreen;
import ru.surfstudio.android.core.ui.base.navigation.fragment.ChildFragmentNavigator;
import ru.surfstudio.android.core.ui.base.screen.presenter.BasePresenter;
import ru.surfstudio.android.core.ui.base.screen.presenter.BasePresenterDependency;

/**
 * Маршрут рутового экрана для дочерних фрагментов
 */
@PerScreen
class ContentContainerPresenter extends BasePresenter<ContentContainerFragmentView> {
    private final ChildFragmentNavigator childFragmentNavigator;
    private final ContentContainerFragmentRoute route;

    private final RxBus rxBus;

    @Inject
    ContentContainerPresenter(ChildFragmentNavigator childFragmentNavigator,
                              ContentContainerFragmentRoute route,
                              RxBus rxBus,
                              BasePresenterDependency basePresenterDependency) {
        super(basePresenterDependency);
        this.childFragmentNavigator = childFragmentNavigator;
        this.rxBus = rxBus;
        this.route = route;
    }

    @Override
    public void onLoad(boolean viewRecreated) {
        super.onLoad(viewRecreated);

        subscribe(rxBus.observeEvents(ClearBackStackEvent.class)
                        .filter(event -> event.getTargetTag().equals(route.getTag())),
                result -> childFragmentNavigator.clearBackStack());

        if (!viewRecreated) {
            childFragmentNavigator.replace(route.getInitialRoute(),
                    false, FragmentTransaction.TRANSIT_NONE);
        }
    }
}
