package ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.close

import ru.surfstudio.android.core.ui.navigation.fragment.route.FragmentRoute

/**
 * Closes fragment with specific [route]
 */
interface CloseFragmentEvent : CloseScreenEvent {
    val route: FragmentRoute
}