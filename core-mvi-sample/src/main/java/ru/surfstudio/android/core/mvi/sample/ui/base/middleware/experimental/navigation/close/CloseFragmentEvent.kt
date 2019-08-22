package ru.surfstudio.android.core.mvi.sample.ui.base.middleware.experimental.navigation.close

import ru.surfstudio.android.core.ui.navigation.fragment.route.FragmentRoute

interface CloseFragmentEvent : CloseScreenEvent {
    val route: FragmentRoute
}