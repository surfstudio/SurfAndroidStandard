package ru.surfstudio.android.navigation.animation.shared

import android.view.View
import ru.surfstudio.android.navigation.animation.BaseScreenAnimations
import java.io.Serializable

/**
 * Animation Bundle with SharedElementTransition support.
 *
 * @param sharedElements elements to share between screens
 */
class SharedElementScreenAnimations(
        enterAnimation: Int,
        exitAnimation: Int,
        popEnterAnimation: Int,
        popExitAnimation: Int,
        val sharedElements: MutableList<SharedElement>
) : Serializable, BaseScreenAnimations(
        enterAnimation,
        exitAnimation,
        popEnterAnimation,
        popExitAnimation
) {

    constructor(elements: MutableList<SharedElement>) : this(
            0,
            0,
            0,
            0,
            elements
    )

    constructor(vararg elements: SharedElement) : this(elements = elements.toMutableList())

    constructor(view: View, transitionName: String) : this(
            mutableListOf(
                    SharedElement(
                            view,
                            transitionName
                    )
            )
    )
}
