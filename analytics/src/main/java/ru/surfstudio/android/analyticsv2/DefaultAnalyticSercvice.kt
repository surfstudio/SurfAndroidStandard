package ru.surfstudio.android.analyticsv2

import ru.surfstudio.android.analyticsv2.core.AnalyticAction
import ru.surfstudio.android.analyticsv2.core.AnalyticActionPerformerCreator
import ru.surfstudio.android.analyticsv2.core.AnalyticService

/**
 * Реализация сервиса аналитики по умолчанию.
 * @creator фабрика соотвествия действия и выполнителя действия
 */
open class DefaultAnalyticService(protected var creator: AnalyticActionPerformerCreator<AnalyticAction>) : AnalyticService<AnalyticAction> {

    override fun performAction(action: AnalyticAction) {
        creator.getPerformersByAction(action).forEach {
            it.perform(action)
        }
    }
}