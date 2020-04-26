package ru.surfstudio.android.navigation.observer

import ru.surfstudio.android.navigation.route.BaseRoute
import java.io.Serializable

/**
 * Observer, that listens for screen results.
 * To emit screen result you should use [ScreenResultEmitter]
 */
interface ScreenResultObserver {

    /**
     * Adds listener, which will be invoked on each screen result, emitted by [targetRoute],
     * and specified for [sourceRoute].
     *
     * @param sourceRoute [BaseRoute] of a screen, that is observing result
     * @param targetRoute [BaseRoute] of a screen, that is emitting result
     * @param listener listener tha
     */
    fun <T : Serializable> addListener(
            sourceRoute: BaseRoute<*>,
            targetRoute: BaseRoute<*>,
            listener: (T) -> Unit
    )

    /**
     * Removes screen result listener.
     *
     * @param sourceRoute [BaseRoute] of a screen, that is observing result
     * @param targetRoute [BaseRoute] of a screen, that is emitting result
     */
    fun removeListener(
            sourceRoute: BaseRoute<*>,
            targetRoute: BaseRoute<*>
    )
}