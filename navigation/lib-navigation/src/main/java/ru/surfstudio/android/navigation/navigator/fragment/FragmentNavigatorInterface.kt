package ru.surfstudio.android.navigation.navigator.fragment

import android.os.Bundle
import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.animation.resource.EmptyResourceAnimations
import ru.surfstudio.android.navigation.backstack.fragment.listener.FragmentBackStackChangedListener
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute

/**
 * Base interface for a fragment navigator.
 *
 * Has several methods for showing and hiding fragments.
 *
 * Uses [containerId] property to determine ViewGroup container which will be used to store fragments.
 *
 * To save it's state on configuration changes you must call
 * [onSaveState] and [onRestoreState] methods in corresponding lifecycle callbacks of parent screen.
 */
interface FragmentNavigatorInterface {

    val isBackStackEmpty: Boolean
        get() = backStackEntryCount == 0

    val backStackEntryCount: Int

    fun add(route: FragmentRoute, animations: Animations = EmptyResourceAnimations)

    fun replace(route: FragmentRoute, animations: Animations = EmptyResourceAnimations)

    fun replaceHard(route: FragmentRoute, animations: Animations = EmptyResourceAnimations)

    fun remove(route: FragmentRoute, animations: Animations = EmptyResourceAnimations)

    fun removeLast(animations: Animations)

    fun removeUntil(route: FragmentRoute, isInclusive: Boolean)

    fun removeAll(shouldRemoveLast: Boolean)

    fun hide(route: FragmentRoute, animations: Animations = EmptyResourceAnimations)

    fun show(route: FragmentRoute, animations: Animations = EmptyResourceAnimations)

    fun onSaveState(outState: Bundle?)

    fun onRestoreState(savedInstanceState: Bundle?)

    fun addBackStackChangeListener(listener: FragmentBackStackChangedListener)

    fun removeBackStackChangeListener(listener: FragmentBackStackChangedListener)
}
