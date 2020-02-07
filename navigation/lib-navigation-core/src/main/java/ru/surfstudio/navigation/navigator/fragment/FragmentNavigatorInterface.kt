package ru.surfstudio.navigation.navigator.fragment

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import ru.surfstudio.navigation.animation.BaseScreenAnimations
import ru.surfstudio.navigation.animation.EmptyScreenAnimations
import ru.surfstudio.navigation.navigator.backstack.fragment.listener.BackStackChangedListener
import ru.surfstudio.navigation.route.fragment.FragmentRoute

/**
 * Base interface for a fragment navigator entity.
 *
 * It can
 */
interface FragmentNavigatorInterface {

    val containerId: Int

    val isBackStackEmpty: Boolean
        get() = backStackEntryCount == 0

    val backStackEntryCount: Int

    val fragmentManager: FragmentManager

    fun add(route: FragmentRoute, animations: BaseScreenAnimations = EmptyScreenAnimations)

    fun replace(route: FragmentRoute, animations: BaseScreenAnimations = EmptyScreenAnimations)

    fun replaceHard(route: FragmentRoute, animations: BaseScreenAnimations = EmptyScreenAnimations)

    fun remove(
            route: FragmentRoute,
            animations: BaseScreenAnimations = EmptyScreenAnimations
    ): Boolean

    fun removeLast(animations: BaseScreenAnimations): Boolean

    fun removeUntil(route: FragmentRoute, isInclusive: Boolean): Boolean

    fun removeAll(): Boolean

    fun hide(
            route: FragmentRoute,
            animations: BaseScreenAnimations = EmptyScreenAnimations
    ): Boolean

    fun show(
            route: FragmentRoute,
            animations: BaseScreenAnimations = EmptyScreenAnimations
    ): Boolean

    /**
     * Добавление лиснера на изменение бекстека
     */
    fun addBackStackChangeListener(listener: BackStackChangedListener)

    /**
     * Добавление лиснера на изменение бекстека
     */
    fun removeBackStackChangeListener(listener: BackStackChangedListener)

    fun onSaveState(outState: Bundle?)

    fun onRestoreState(savedInstanceState: Bundle?)
}
