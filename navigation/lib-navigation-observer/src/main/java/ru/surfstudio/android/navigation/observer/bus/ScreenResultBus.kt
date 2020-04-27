package ru.surfstudio.android.navigation.observer.bus

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
            sourceRoute: BaseRoute<*>,
            targetRoute: R,
            listener: (T) -> Unit
    ) where R : BaseRoute<*>, R : ResultRoute<T> {
        val sourceId = getRouteId(sourceRoute)
        val targetId = getRouteId(targetRoute)

        checkStorageForResult(sourceId, targetId, listener)
        addListenerInfo(sourceId, targetId, listener)
    }

    override fun <R> removeListener(
            sourceRoute: BaseRoute<*>,
            targetRoute: R
    ) where R : BaseRoute<*>, R : ResultRoute<*> {
        val sourceId = getRouteId(sourceRoute)
        val targetId = getRouteId(targetRoute)
        listeners.removeAll { it.sourceId == sourceId && it.targetId == targetId }
    }

    override fun <T : Serializable, R> emit(
            sourceRoute: BaseRoute<*>,
            targetRoute: R,
            result: T
    ) where R: BaseRoute<*>, R: ResultRoute<T> {
        val sourceId = getRouteId(sourceRoute)
        val targetId = getRouteId(targetRoute)

        val matchingObservers = listeners.filter { it.sourceId == sourceId && it.targetId == targetId }
        if (matchingObservers.isNotEmpty()) {
            matchingObservers.forEach { it.listener(result) }
        } else { //no observers, but result emitted
            saveResultToStorage(sourceId, targetId, result)
        }
    }

    private fun <T : Serializable> saveResultToStorage(sourceId: String, targetId: String, result: T) {
        val screenResultInfo = ScreenResultInfo(sourceId, targetId, result)
        screenResultStorage.save(screenResultInfo)
    }

    private fun <T : Serializable> checkStorageForResult(sourceId: String, targetId: String, listener: (T) -> Unit) {
        if (screenResultStorage.contains(sourceId, targetId)) {
            val resultInfo = screenResultStorage.get<T>(sourceId, targetId)
            screenResultStorage.remove(sourceId, targetId)
            if (resultInfo != null) listener(resultInfo.result)
        }
    }

    private fun <T : Serializable> addListenerInfo(
            sourceId: String,
            targetId: String,
            listener: (T) -> Unit
    ) {
        val alreadyHasListener = listeners.any { it.sourceId == sourceId && it.targetId == targetId }
        if (!alreadyHasListener) {
            val listenerInfo = ScreenResultListenerInfo(sourceId, targetId, listener as (Serializable) -> Unit)
            listeners.add(listenerInfo)
        }
    }

    protected open fun getRouteId(route: BaseRoute<*>): String = route.getTag()
}
