package ru.surfstudio.android.navigation.navigator.fragment

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.animation.res.EmptyResourceAnimations
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

    val containerId: Int

    val isBackStackEmpty: Boolean
        get() = backStackEntryCount == 0

    val backStackEntryCount: Int

    val fragmentManager: FragmentManager

    fun add(route: FragmentRoute, animations: Animations = EmptyResourceAnimations)

    fun replace(route: FragmentRoute, animations: Animations = EmptyResourceAnimations)

    fun replaceHard(route: FragmentRoute, animations: Animations = EmptyResourceAnimations)

    fun remove(
            route: FragmentRoute,
            animations: Animations = EmptyResourceAnimations
    ): Boolean

    fun removeLast(animations: Animations): Boolean

    fun removeUntil(route: FragmentRoute, isInclusive: Boolean): Boolean

    fun removeAll(): Boolean

    fun hide(
            route: FragmentRoute,
            animations: Animations = EmptyResourceAnimations
    ): Boolean

    fun show(
            route: FragmentRoute,
            animations: Animations = EmptyResourceAnimations
    ): Boolean

    fun onSaveState(outState: Bundle?)

    fun onRestoreState(savedInstanceState: Bundle?)
}
