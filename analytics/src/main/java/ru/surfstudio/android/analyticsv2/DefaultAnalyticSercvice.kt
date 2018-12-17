package ru.surfstudio.android.analyticsv2

import ru.surfstudio.android.analyticsv2.core.AnalyticAction
import ru.surfstudio.android.analyticsv2.core.AnalyticActionPerformerFactory
import ru.surfstudio.android.analyticsv2.core.AnalyticService

/**
 * Реализация сервиса аналитики по умолчанию.
 * @factory фабрика соотвествия действия и выполнителя действия
 */
open class DefaultAnalyticService(protected var factory: AnalyticActionPerformerFactory<AnalyticAction>) : AnalyticService {

    override fun performAction(action: AnalyticAction) {
        factory.getPerformerByAction(action)?.let {
            it.perform(action)
        }
    }
}