package ru.surfstudio.navigation.animation

import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import java.io.Serializable

open class BaseScreenAnimations(
        @AnimatorRes @AnimRes val enterAnimation: Int,
        @AnimatorRes @AnimRes val exitAnimation: Int,
        @AnimatorRes @AnimRes val popEnterAnimation: Int,
        @AnimatorRes @AnimRes val popExitAnimation: Int
) : Serializable, Animations
