package ru.surfstudio.android.navigation.animation.shared

import android.view.View
import ru.surfstudio.android.navigation.animation.Animations
import java.io.Serializable

/**
 * Animation Bundle with SharedElementTransition support.
 *
 * @param sharedElements elements to share between screens
 */
class SharedElementAnimations(
        val sharedElements: MutableList<SharedElement>
) : Animations, Serializable {

    constructor(vararg elements: SharedElement) : this(elements.toMutableList())

    constructor(view: View, transitionName: String) : this(
            mutableListOf(
                    SharedElement(
                            view,
                            transitionName
                    )
            )
    )
}
