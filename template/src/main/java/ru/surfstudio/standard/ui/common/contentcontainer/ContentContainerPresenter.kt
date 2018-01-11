package ru.surfstudio.standard.ui.common.contentcontainer

import android.app.FragmentTransaction

import javax.inject.Inject

import ru.surfstudio.android.core.app.bus.RxBus
import ru.surfstudio.android.core.app.dagger.scope.PerScreen
import ru.surfstudio.android.core.ui.base.navigation.fragment.ChildFragmentNavigator
import ru.surfstudio.android.core.ui.base.screen.presenter.BasePresenter
import ru.surfstudio.android.core.ui.base.screen.presenter.BasePresenterDependency

/**
 * Маршрут рутового экрана для дочерних фрагментов
 */
@PerScreen
internal class ContentContainerPresenter
@Inject constructor(private val childFragmentNavigator: ChildFragmentNavigator,
            private val route: ContentContainerFragmentRoute,
            private val rxBus: RxBus,
            basePresenterDependency: BasePresenterDependency) : BasePresenter<ContentContainerFragmentView>(basePresenterDependency) {

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)

        subscribe(rxBus.observeEvents<ClearBackStackEvent>(ClearBackStackEvent::class.java)
                .filter { event -> event.targetTag == route.tag }
        ) { result -> childFragmentNavigator.clearBackStack() }

        if (!viewRecreated) {
            childFragmentNavigator.replace(route.initialRoute,
                    false, FragmentTransaction.TRANSIT_NONE)
        }
    }
}
