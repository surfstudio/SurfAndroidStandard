package ru.surfstudio.android.navigation.animation

import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import java.io.Serializable
import ru.surfstudio.android.navigation.command.*

/**
 * Base resource-based screen animations.
 *
 * Contains specific animation resources to run when screens are added/removed.
 *
 * @param enterAnimation An animation or animator resource ID used for the enter animation on the
 * screen by [Add], [Replace] or same command.
 * @param exitAnimation An animation or animator resource ID used for the exit animation on the
 * screen by [Add], [Replace] or same command.
 * @param popEnterAnimation An animation or animator resource ID used for the enter animation on the
 * screen being re-added from backStack by [RemoveLast] command.
 * @param popExitAnimation An animation or animator resource ID used for the exit animation on the
 * screen being removed by [RemoveLast] command.

 */
open class BaseScreenAnimations(
        @AnimatorRes @AnimRes val enterAnimation: Int,
        @AnimatorRes @AnimRes val exitAnimation: Int,
        @AnimatorRes @AnimRes val popEnterAnimation: Int,
        @AnimatorRes @AnimRes val popExitAnimation: Int
) : Serializable, Animations
