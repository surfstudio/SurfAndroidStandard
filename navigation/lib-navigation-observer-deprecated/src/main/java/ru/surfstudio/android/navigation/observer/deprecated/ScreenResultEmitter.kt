package ru.surfstudio.android.navigation.observer.deprecated

import ru.surfstudio.android.navigation.observer.deprecated.route.ResultRoute
import ru.surfstudio.android.navigation.route.BaseRoute
import java.io.Serializable

/**
 * Emitter, that emits result from target screen to a source screen.
 * All emitted results should be handled by [ScreenResultObserver].
 */
@Deprecated("Prefer using new implementation")
interface ScreenResultEmitter {

    /**
     * Emit screen result
     *
     * @param targetRoute [BaseRoute] of a screen, that is emitting result
     * @param result result from the target screen
     */
    fun <T : Serializable, R> emit(
            targetRoute: R,
            result: T
    ) where R: BaseRoute<*>, R: ResultRoute<T>
}