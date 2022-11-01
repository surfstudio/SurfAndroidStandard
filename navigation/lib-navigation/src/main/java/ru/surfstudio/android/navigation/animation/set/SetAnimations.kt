package ru.surfstudio.android.navigation.animation.set

import ru.surfstudio.android.navigation.animation.Animations
import java.io.Serializable

/**
 * The set with composition of different [Animations]
 */
class SetAnimations(val set: Set<Animations>) : Animations, Serializable {

    constructor(vararg animations: Animations): this(animations.toSet())
}