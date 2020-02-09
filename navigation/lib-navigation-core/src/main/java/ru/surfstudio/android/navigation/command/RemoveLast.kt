package ru.surfstudio.android.navigation.command

import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.animation.BaseScreenAnimations
import ru.surfstudio.android.navigation.animation.EmptyScreenAnimations
import ru.surfstudio.android.navigation.animation.NoScreenAnimations
import ru.surfstudio.android.navigation.option.Options
import ru.surfstudio.android.navigation.route.Route
import ru.surfstudio.android.navigation.route.stub.StubActivityRoute

/**
 * Remove last screen from current stack and switch to previous.
 */
open class RemoveLast(
        override val route: Route,
        override val animations: Animations = EmptyScreenAnimations,
        override val options: Options? = null
) : NavigationCommand

/**
 * Remove last activity from current task
 */
data class RemoveLastActivity(
        override val animations: BaseScreenAnimations = NoScreenAnimations
) : RemoveLast(StubActivityRoute, animations) {

    override fun toString(): String = "RemoveLastActivity"
}

/**
 * Remove last fragment from current stack and switch to previous
 */
data class RemoveLastFragment(
        override val animations: BaseScreenAnimations = NoScreenAnimations
) : RemoveLast(TODO(), animations) {

    override fun toString(): String = "RemoveLastFragment"
}
