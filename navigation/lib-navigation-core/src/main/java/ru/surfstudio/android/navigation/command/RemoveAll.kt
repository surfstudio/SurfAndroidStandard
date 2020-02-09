package ru.surfstudio.android.navigation.command

import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.animation.EmptyScreenAnimations
import ru.surfstudio.android.navigation.option.Options
import ru.surfstudio.android.navigation.route.Route
import ru.surfstudio.android.navigation.route.stub.StubActivityRoute


/**
 * Команда удаления всех экранов из текущего стека
 */
open class RemoveAll(
        override val route: Route,
        override val animations: Animations? = EmptyScreenAnimations,
        override val options: Options? = null
) : NavigationCommand

/**
 * Remove all activities from current task (taskAffinity).
 */
class RemoveAllActivities : RemoveAll(StubActivityRoute) {
    override fun toString(): String = "RemoveAllActivities"
}

/**
 * Remove all fragments from current task
 */
class RemoveAllFragments : RemoveAll(TODO())
