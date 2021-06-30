package ru.surfstudio.android.navigation.observer.deprecated

import ru.surfstudio.android.navigation.observer.deprecated.listener.ScreenResultListener
import ru.surfstudio.android.navigation.observer.deprecated.route.ResultRoute
import ru.surfstudio.android.navigation.route.BaseRoute
import java.io.Serializable

/**
 * Observer, that listens for screen results.
 * To emit screen result you should use [ScreenResultEmitter]
 */
@Deprecated("Prefer using new implementation")
interface ScreenResultObserver {

    /**
     * Adds listener, which will be invoked on each screen result, emitted by [targetRoute]
     *
     * @param targetRoute [BaseRoute] of a screen, that is emitting result
     * @param listener listener tha
     */
    fun <T : Serializable, R> addListener(
            targetRoute: R,
            listener: ScreenResultListener<T>
    ) where R : BaseRoute<*>, R : ResultRoute<T>

    /**
     * Removes screen result listener.
     *
     * @param targetRoute [BaseRoute] of a screen, that is emitting result
     */
    fun <R> removeListener(
            targetRoute: R
    ) where R : BaseRoute<*>, R : ResultRoute<*>
}