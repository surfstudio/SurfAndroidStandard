package ru.surfstudio.android.navigation.observer

import ru.surfstudio.android.navigation.observer.listener.ScreenResultListener
import ru.surfstudio.android.navigation.observer.route.ResultRoute
import java.io.Serializable

/**
 * Observer, that listens for screen results.
 * To emit screen result you should use [ScreenResultEmitter]
 */
interface ScreenResultObserver {

    /**
     * Adds listener, which will be invoked on each screen result, emitted by [targetRoute]
     *
     * @param targetRoute route of a screen, that is emitting result
     * @param listener listener tha
     */
    fun <T : Serializable, R> addListener(
        targetRoute: R,
        listener: ScreenResultListener<T>
    ) where R : ResultRoute<T>

    /**
     * Removes screen result listener.
     *
     * @param targetRoute route of a screen, that is emitting result
     */
    fun <R> removeListener(
        targetRoute: R
    ) where R : ResultRoute<*>
}