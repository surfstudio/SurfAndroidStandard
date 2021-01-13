package ru.surfstudio.android.navigation.observer.bus

import ru.surfstudio.android.navigation.observer.ScreenResultEmitter
import ru.surfstudio.android.navigation.observer.ScreenResultObserver
import ru.surfstudio.android.navigation.observer.storage.ScreenResultInfo
import ru.surfstudio.android.navigation.observer.storage.ScreenResultStorage
import ru.surfstudio.android.navigation.route.BaseRoute
import ru.surfstudio.android.navigation.route.result.ResultRoute
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

    private val listeners = mutableListOf<ScreenResultListenerInfo<Serializable, BaseRoute<*>>>()

    override fun getObservedRoutes(): List<BaseRoute<*>> {
        return listeners.map {
            it.targetRoute
        }.toList()
    }

    override fun <T : Serializable, R> addListener(
            targetRoute: R,
            listener: (T) -> Unit
    ) where R : BaseRoute<*>, R : ResultRoute<T> {
        val targetId = getRouteId(targetRoute)

        checkStorageForResult(targetId, listener)
        addListenerInfo(targetRoute, listener)
    }

    override fun <R> removeListener(
            targetRoute: R
    ) where R : BaseRoute<*>, R : ResultRoute<*> {
        val targetId = getRouteId(targetRoute)
        listeners.removeAll { resultListenerInfo ->
            getRouteId(resultListenerInfo.targetRoute) == targetId
        }
    }

    override fun <T : Serializable, R> emit(
            targetRoute: R,
            result: T
    ) where R : BaseRoute<*>, R : ResultRoute<T> {
        val targetId = getRouteId(targetRoute)

        val matchingObservers = listeners.filter { resultListenerInfo ->
            getRouteId(resultListenerInfo.targetRoute) == targetId
        }

        if (matchingObservers.isNotEmpty()) {
            matchingObservers.forEach { it.listener(result) }
        } else { //no observers, but result emitted
            saveResultToStorage(targetId, result)
        }
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

    private fun <T : Serializable, R> addListenerInfo(
            targetRoute: R,
            listener: (T) -> Unit
    ) where R : BaseRoute<*>, R : ResultRoute<T> {
        val alreadyHasListener = listeners.any { resultListenerInfo ->
            getRouteId(resultListenerInfo.targetRoute) == getRouteId(targetRoute)
        }
        if (!alreadyHasListener) {
            val listenerInfo = ScreenResultListenerInfo<Serializable, BaseRoute<*>>(targetRoute, listener as (Serializable) -> Unit)
            listeners.add(listenerInfo)
        }
    }

    protected open fun getRouteId(route: BaseRoute<*>): String = route.getId()
}
