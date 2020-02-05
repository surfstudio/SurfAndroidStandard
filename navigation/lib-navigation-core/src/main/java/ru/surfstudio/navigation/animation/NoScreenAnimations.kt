package ru.surfstudio.navigation.animation

import ru.surfstudio.navigation.animation.BaseScreenAnimations

/**
 * Animations that should not be displayed.
 *
 * You should use this class when you need to declare explicitly that animation shouldn't be processed.
 */
object NoScreenAnimations :
        BaseScreenAnimations(-1, -1, -1, -1)
