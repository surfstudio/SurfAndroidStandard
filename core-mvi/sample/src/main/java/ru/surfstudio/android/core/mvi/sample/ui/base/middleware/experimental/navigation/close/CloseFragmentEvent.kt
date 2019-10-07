package ru.surfstudio.android.core.mvi.sample.ui.base.middleware.experimental.navigation.close

import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.experimental.ExperimentalFeature
import ru.surfstudio.android.core.ui.navigation.fragment.route.FragmentRoute

/**
 * Событие закрытия фрагмента
 */
@ExperimentalFeature
interface CloseFragmentEvent : CloseScreenEvent {
    val route: FragmentRoute
}