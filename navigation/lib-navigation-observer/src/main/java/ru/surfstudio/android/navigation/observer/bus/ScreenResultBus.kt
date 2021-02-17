package ru.surfstudio.android.navigation.observer.bus

import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.navigation.observer.ScreenResultEmitter
import ru.surfstudio.android.navigation.observer.ScreenResultObserver
import ru.surfstudio.android.navigation.observer.route.ResultRoute
import ru.surfstudio.android.navigation.observer.storage.ScreenResultInfo
import ru.surfstudio.android.navigation.observer.storage.ScreenResultStorage
import ru.surfstudio.android.navigation.route.BaseRoute
import java.io.Serializable

/**
 * Screen result bus, that acts both as an emitter and observer of screen results.
 *
 * It has simple emit/observe logic based on listeners, and also can save unhandled results into
 * [ScreenResultStorage], where they will be stored until handled.
 */
open class ScreenResultBus(
        private val screenResultStorage: ScreenResultStorage
) : ScreenResultEmitter, ScreenResultObserver {

    private val listeners = mutableListOf<ScreenResultListenerInfo<Serializable>>()

    override fun <T : Serializable, R> addListener(
            targetRoute: R,
            listener: (T) -> Unit
    ) where R : BaseRoute<*>, R : ResultRoute<T> {
        val targetId = getRouteId(targetRoute)

        checkStorageForResult(targetId, listener)
        addListenerInfo(targetId, listener)
    }

    override fun <R> removeListener(
            routeId: String
    ) where R : BaseRoute<*>, R : ResultRoute<*> {
        listeners.removeAll { it.targetId == routeId }
    }

    override fun <R> removeListener(
            targetRoute: R
    ) where R : BaseRoute<*>, R : ResultRoute<*> {
        removeListener<R>(getRouteId(targetRoute))
    }

    override fun <T : Serializable, R> emit(
            routeId: String,
            result: T
    ) where R : BaseRoute<*>, R : ResultRoute<T> {
        val matchingObservers = listeners.filter { it.targetId == routeId }
        if (matchingObservers.isNotEmpty()) {
            matchingObservers.forEach { it.listener(result) }
        } else { //no observers, but result emitted
            saveResultToStorage(routeId, result)
        }
    }

    override fun <T : Serializable, R> emit(
            targetRoute: R,
            result: T
    ) where R : BaseRoute<*>, R : ResultRoute<T> {
        emit<T, R>(getRouteId(targetRoute), result)
    }

    private fun <T : Serializable> saveResultToStorage(targetId: String, result: T) {
        val screenResultInfo = ScreenResultInfo(targetId, result)
        screenResultStorage.save(screenResultInfo)
    }

    private fun <T : Serializable> checkStorageForResult(targetId: String, listener: (T) -> Unit) {
        if (screenResultStorage.contains(targetId)) {
            val resultInfo = screenResultStorage.get<T>(targetId)
            screenResultStorage.remove(targetId)
            if (resultInfo != null) listener(resultInfo.result)
        }
    }

    private fun <T : Serializable> addListenerInfo(
            targetId: String,
            listener: (T) -> Unit
    ) {
        val alreadyHasListener = listeners.any { it.targetId == targetId }
        if (!alreadyHasListener) {
            val listenerInfo = ScreenResultListenerInfo(targetId, listener as (Serializable) -> Unit)
            listeners.add(listenerInfo)
        } else {
            Logger.e("The listener is already assigned, you cannot install a new one")
        }
    }

    protected open fun getRouteId(route: BaseRoute<*>): String = route.getId()
}