package ru.surfstudio.android.navigation.animation.styled

import androidx.annotation.StyleRes
import ru.surfstudio.android.navigation.animation.Animations
import java.io.Serializable

/**
 * Style resources based animations
 *
 * @param style style resource
 */
data class StyledAnimations(
        @StyleRes val style: Int
) : Animations, Serializable