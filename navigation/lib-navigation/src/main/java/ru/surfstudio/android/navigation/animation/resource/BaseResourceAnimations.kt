package ru.surfstudio.android.navigation.animation.resource

import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import ru.surfstudio.android.navigation.animation.Animations
import java.io.Serializable
import ru.surfstudio.android.navigation.command.fragment.*

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
 * screen being re-added from backStack by [RemoveLast] or same command.
 * @param popExitAnimation An animation or animator resource ID used for the exit animation on the
 * screen being removed by [RemoveLast] or same command.

 */
open class BaseResourceAnimations(
        @AnimatorRes @AnimRes val enterAnimation: Int,
        @AnimatorRes @AnimRes val exitAnimation: Int,
        @AnimatorRes @AnimRes val popEnterAnimation: Int,
        @AnimatorRes @AnimRes val popExitAnimation: Int
) : Serializable, Animations
