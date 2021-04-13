package ru.surfstudio.android.core.mvp.binding.test.navigation.base

import ru.surfstudio.android.core.ui.navigation.ActivityRouteInterface
import ru.surfstudio.android.core.ui.navigation.event.result.SupportOnActivityResultRoute
import ru.surfstudio.android.core.ui.navigation.fragment.route.FragmentRoute
import ru.surfstudio.android.core.ui.navigation.fragment.route.RootFragmentRoute
import ru.surfstudio.android.mvp.dialog.navigation.route.DialogRoute
import java.io.Serializable

/**
 * Тестовые события навигации
 */
sealed class TestNavigationEvent {

    sealed class TestActivityNavigationEvent : TestNavigationEvent() {

        sealed class RouteEvent<RT : ActivityRouteInterface> : TestActivityNavigationEvent(), RouteEventInterface<RT> {

            class Start<RT : ActivityRouteInterface>(override val route: RT) : RouteEvent<RT>()
            class StartForResult<RT : ActivityRouteInterface>(override val route: RT) : RouteEvent<RT>()
            class FinishWithResult<RT : SupportOnActivityResultRoute<T>, T : Serializable>(
                    override val route: RT,
                    val result: T? = null,
                    val success: Boolean = true
            ) : RouteEvent<RT>()
        }

        object FinishCurrent : TestActivityNavigationEvent()
        object FinishAffinity : TestActivityNavigationEvent()
    }

    sealed class TestFragmentNavigationEvent : TestNavigationEvent() {

        sealed class RouteEvent<RT : FragmentRoute> : TestFragmentNavigationEvent(), RouteEventInterface<RT> {

            class Add<RT : FragmentRoute>(override val route: RT) : RouteEvent<RT>()
            class Replace<RT : FragmentRoute>(override val route: RT) : RouteEvent<RT>()
            class Remove<RT : FragmentRoute>(override val route: RT) : RouteEvent<RT>()
            class Show<RT : FragmentRoute>(override val route: RT) : RouteEvent<RT>()
            class Hide<RT : FragmentRoute>(override val route: RT) : RouteEvent<RT>()
            class PopBackStack<RT : FragmentRoute>(override val route: RT? = null, val inclusive: Boolean = true) : RouteEvent<RT>()
        }

        object ClearBackStack : TestFragmentNavigationEvent()
    }

    sealed class TestDialogNavigationEvent<RT : DialogRoute> : TestNavigationEvent(), RouteEventInterface<RT> {

        class ShowDialog<RT : DialogRoute>(override val route: RT) : TestDialogNavigationEvent<RT>()
        class DismissDialog<RT : DialogRoute>(override val route: RT) : TestDialogNavigationEvent<RT>()
    }

    sealed class TestTabFragmentNavigationEvent : TestNavigationEvent() {

        sealed class RouteEvent<RT : FragmentRoute> : TestTabFragmentNavigationEvent(), RouteEventInterface<RT> {

            class Open<RT : FragmentRoute>(override val route: RT) : RouteEvent<RT>()
            class ShowAtTab<RT : FragmentRoute>(val tabRoute: FragmentRoute, override val route: RT, val clearStack: Boolean = false) : RouteEvent<RT>()
            class ClearStackTo<RT : FragmentRoute>(override val route: RT) : RouteEvent<RT>()
            class Replace<RT : FragmentRoute>(override val route: RT) : RouteEvent<RT>()
        }

        class ClearTabs(val routes: Array<out RootFragmentRoute>) : TestTabFragmentNavigationEvent()
        object ClearStack : TestTabFragmentNavigationEvent()
    }

    interface RouteEventInterface<RT> {
        val route: RT?
    }
}
