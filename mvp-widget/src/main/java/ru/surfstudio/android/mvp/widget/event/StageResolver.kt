package ru.surfstudio.android.mvp.widget.event

import ru.surfstudio.android.core.ui.state.LifecycleStage
import ru.surfstudio.android.core.ui.state.ScreenState
import ru.surfstudio.android.mvp.widget.state.WidgetRecoveryState
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
            LifecycleStage.CREATED to listOf(LifecycleStage.VIEW_CREATED, LifecycleStage.COMPLETELY_DESTROYED),
            LifecycleStage.VIEW_CREATED to listOf(LifecycleStage.STARTED, LifecycleStage.VIEW_DESTROYED),
            LifecycleStage.STARTED to listOf(LifecycleStage.RESUMED, LifecycleStage.STOPPED),
            LifecycleStage.RESUMED to listOf(LifecycleStage.PAUSED),
            LifecycleStage.PAUSED to listOf(LifecycleStage.RESUMED, LifecycleStage.STOPPED),
            LifecycleStage.STOPPED to listOf(LifecycleStage.STARTED, LifecycleStage.VIEW_DESTROYED),
            LifecycleStage.VIEW_DESTROYED to listOf(LifecycleStage.VIEW_CREATED, LifecycleStage.COMPLETELY_DESTROYED),
            LifecycleStage.COMPLETELY_DESTROYED to listOf()
    )

    /**
     * Приводит виджет в состояние в зависимости от текущих родительского, виджета и желаемого
     * @param wishingState желаемое состояние
     */
    fun pushState(wishingState: LifecycleStage) {
        val states = resolveStates(wishingState)
        for (state in states) {
            if (allowedStateTransition[screenState.lifecycleStage]?.contains(state) == true) {
                stageApplier(state)
            }
        }
    }

    private fun resolveStates(wishingState: LifecycleStage): Array<LifecycleStage> {
        val isParentDestroyed = wishingState != LifecycleStage.COMPLETELY_DESTROYED && parentState.isCompletelyDestroyed
        val isParentReady = parentState.lifecycleStage != null &&
                parentState.lifecycleStage.ordinal >= LifecycleStage.VIEW_CREATED.ordinal

        return when {

            //возвращаем пустой список, если родитель уже уничтожен
            isParentDestroyed -> arrayOf()

            isWidgetRecovering -> {
                //Восстановление виджета после смены конфигурации, пушим сразу 3 состояния
                screenState.widgetRecoveryState = WidgetRecoveryState.NONE
                arrayOf(LifecycleStage.VIEW_CREATED, LifecycleStage.STARTED, LifecycleStage.RESUMED)
            }

            isRecyclerViewCreate(wishingState) -> {
                //Создание элемента в recyclerView, пушим сразу 3 состояния
                arrayOf(LifecycleStage.VIEW_CREATED, LifecycleStage.STARTED, LifecycleStage.RESUMED)
            }

            isRecyclerViewDestroy(wishingState) -> {
                //Удаление элемента в recyclerView, пушим сразу 3 состояния
                arrayOf(LifecycleStage.PAUSED, LifecycleStage.STOPPED, LifecycleStage.VIEW_DESTROYED)
            }

            wishingState == LifecycleStage.VIEW_DESTROYED && parentState.lifecycleStage == LifecycleStage.PAUSED -> {
                arrayOf(LifecycleStage.STOPPED, LifecycleStage.VIEW_DESTROYED)
            }

            // Когда уничтожаем виджет, необходимо вручную послать предыдущие события
            wishingState == LifecycleStage.COMPLETELY_DESTROYED -> resolveStatesForCompletelyDestroy()

            !isParentReady -> arrayOf()

            isWidgetDestroyed -> arrayOf()

            isParentViewDestroyed -> {
                screenState.widgetRecoveryState = WidgetRecoveryState.WIDGET_DESTROYED
                arrayOf(wishingState)
            }

            else -> arrayOf(wishingState)
        }
    }

    private val isWidgetRecovering: Boolean
        get() = screenState.widgetRecoveryState == WidgetRecoveryState.WIDGET_RECOVERING

    private val isWidgetDestroyed: Boolean
        get() = screenState.widgetRecoveryState == WidgetRecoveryState.WIDGET_DESTROYED

    private val isParentViewDestroyed: Boolean
        get() = parentState.lifecycleStage == LifecycleStage.VIEW_DESTROYED

    private fun isRecyclerViewDestroy(wishingState: LifecycleStage) =
            wishingState == LifecycleStage.VIEW_DESTROYED && parentState.lifecycleStage == LifecycleStage.RESUMED

    private fun isRecyclerViewCreate(wishingState: LifecycleStage) =
            wishingState == LifecycleStage.VIEW_CREATED && parentState.lifecycleStage == LifecycleStage.RESUMED

    private fun resolveStatesForCompletelyDestroy(): Array<LifecycleStage> = when (screenState.lifecycleStage!!) {

        LifecycleStage.CREATED, LifecycleStage.VIEW_CREATED, LifecycleStage.STOPPED -> {
            arrayOf(LifecycleStage.VIEW_DESTROYED, LifecycleStage.COMPLETELY_DESTROYED)
        }

        LifecycleStage.STARTED, LifecycleStage.PAUSED -> {
            arrayOf(LifecycleStage.STOPPED, LifecycleStage.VIEW_DESTROYED, LifecycleStage.COMPLETELY_DESTROYED)
        }

        LifecycleStage.RESUMED -> {
            arrayOf(LifecycleStage.PAUSED, LifecycleStage.STOPPED, LifecycleStage.VIEW_DESTROYED, LifecycleStage.COMPLETELY_DESTROYED)
        }

        LifecycleStage.VIEW_DESTROYED -> {
            arrayOf(LifecycleStage.COMPLETELY_DESTROYED)
        }

        LifecycleStage.COMPLETELY_DESTROYED -> {
            arrayOf(LifecycleStage.COMPLETELY_DESTROYED)
        }
    }
}