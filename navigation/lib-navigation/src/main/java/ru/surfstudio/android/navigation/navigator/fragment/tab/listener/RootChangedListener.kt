package ru.surfstudio.android.navigation.navigator.fragment.tab.listener

import ru.surfstudio.android.navigation.navigator.fragment.tab.TabFragmentNavigatorInterface
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute

/**
 * Listener, that invokes after [TabFragmentNavigatorInterface]'s tab head is changed.
 *
 * @param tabHeadRoute the new tab head route.
 */
typealias TabHeadChangedListener = (tabHeadRoute: FragmentRoute) -> Unit