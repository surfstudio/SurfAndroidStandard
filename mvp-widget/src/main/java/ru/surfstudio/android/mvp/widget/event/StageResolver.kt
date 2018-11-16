package ru.surfstudio.android.mvp.widget.event

import ru.surfstudio.android.core.ui.state.LifecycleStage
import ru.surfstudio.android.core.ui.state.ScreenState
import ru.surfstudio.android.mvp.widget.state.WidgetScreenState

/**
 * Класс, отвечающий за расчет следующего этапа ЖЦ виджета.
 * Вычисляет на основе состояния родителя, текущего состояния виджета и возможных переходов
 */
class StageResolver(
        private val screenState: WidgetScreenState,
        private val parentState: ScreenState,
        private val stageApplier: (LifecycleStage) -> Unit
) {

    //разрешенные переходы по состояниям
    private val allowedStateTransition = mapOf(
            LifecycleStage.CREATED to listOf(LifecycleStage.VIEW_READY, LifecycleStage.DESTROYED),
            LifecycleStage.VIEW_READY to listOf(LifecycleStage.STARTED, LifecycleStage.VIEW_DESTROYED),
            LifecycleStage.STARTED to listOf(LifecycleStage.RESUMED, LifecycleStage.STOPPED),
            LifecycleStage.RESUMED to listOf(LifecycleStage.PAUSED),
            LifecycleStage.PAUSED to listOf(LifecycleStage.RESUMED, LifecycleStage.STOPPED),
            LifecycleStage.STOPPED to listOf(LifecycleStage.STARTED, LifecycleStage.VIEW_DESTROYED),
            LifecycleStage.VIEW_DESTROYED to listOf(LifecycleStage.VIEW_READY, LifecycleStage.DESTROYED),
            LifecycleStage.DESTROYED to listOf()
    )

    /**
     * Приводит виджет в состояние в зависимости от текущих родительского, виджета и желаемого
     * @param wishingState желаемое состояние
     */
    fun pushState(wishingState: LifecycleStage) {
        if (wishingState != LifecycleStage.DESTROYED && parentState.isCompletelyDestroyed) return

        val states = resolveStates(wishingState)
        for (state in states) {

            if (allowedStateTransition[screenState.lifecycleStage]?.contains(state) ?: false) {
                stageApplier(state)
            }
        }
    }

    private fun resolveStates(wishingState: LifecycleStage): Array<LifecycleStage> {
        return when {

            //Первые два кейса - поведение виджета в ресайклере. Необходимо послать STARTED/RESUMED и PAUSED/STOPPED
            //на attach/detach
            wishingState == LifecycleStage.VIEW_DESTROYED && parentState.lifecycleStage == LifecycleStage.RESUMED -> {
                arrayOf(LifecycleStage.PAUSED, LifecycleStage.STOPPED, LifecycleStage.VIEW_DESTROYED)
            }

            wishingState == LifecycleStage.VIEW_READY && parentState.lifecycleStage == LifecycleStage.RESUMED -> {
                arrayOf(LifecycleStage.VIEW_READY, LifecycleStage.STARTED, LifecycleStage.RESUMED)
            }

            // Когда уничтожаем виджет, необходимо в ручную послать предыдущие события
            wishingState == LifecycleStage.DESTROYED -> {
                when (screenState.lifecycleStage!!) {

                    LifecycleStage.CREATED, LifecycleStage.VIEW_READY, LifecycleStage.STOPPED -> {
                        arrayOf(LifecycleStage.VIEW_DESTROYED, LifecycleStage.DESTROYED)
                    }

                    LifecycleStage.STARTED, LifecycleStage.PAUSED -> {
                        arrayOf(LifecycleStage.STOPPED, LifecycleStage.VIEW_DESTROYED, LifecycleStage.DESTROYED)
                    }

                    LifecycleStage.RESUMED -> {
                        arrayOf(LifecycleStage.PAUSED, LifecycleStage.STOPPED, LifecycleStage.VIEW_DESTROYED, LifecycleStage.DESTROYED)
                    }

                    LifecycleStage.VIEW_DESTROYED -> {
                        arrayOf(LifecycleStage.DESTROYED)
                    }

                    LifecycleStage.DESTROYED -> {
                        arrayOf(wishingState)
                    }
                }
            }

            else -> arrayOf(wishingState)
        }
    }
}