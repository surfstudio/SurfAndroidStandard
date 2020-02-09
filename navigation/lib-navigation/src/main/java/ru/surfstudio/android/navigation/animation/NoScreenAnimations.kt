package ru.surfstudio.android.navigation.animation

/**
 * Animations that should not be displayed.
 *
 * You should use this class when you need to declare explicitly that animation shouldn't be processed.
 */
object NoScreenAnimations :
        BaseScreenAnimations(-1, -1, -1, -1)
