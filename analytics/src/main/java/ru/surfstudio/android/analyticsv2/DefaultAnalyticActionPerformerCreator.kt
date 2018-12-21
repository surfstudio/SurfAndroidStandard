package ru.surfstudio.android.analyticsv2

import ru.surfstudio.android.analytics.BuildConfig
import ru.surfstudio.android.analyticsv2.core.AnalyticAction
import ru.surfstudio.android.analyticsv2.core.AnalyticActionPerformer
import ru.surfstudio.android.analyticsv2.core.AnalyticActionPerformerCreator
import java.lang.Error

/**
 * Фабрика выполнителей действий аналитики по умолчанию
 * Выполняет действие на тех выполнителях, которые могут его обработать
 */
class DefaultAnalyticActionPerformerCreator : AnalyticActionPerformerCreator<AnalyticAction> {

    private val performers: MutableSet<AnalyticActionPerformer<AnalyticAction>> = mutableSetOf()

    override fun getPerformersByAction(event: AnalyticAction): List<AnalyticActionPerformer<AnalyticAction>> {
        val performers = performers.filter { it.canHandle(event) }
        if (performers.isEmpty() && BuildConfig.DEBUG) {
            throw Error("No action performer for action: ${event::class.java.canonicalName} in performers ${this.performers}")
        }
        return performers
    }

    /**
     * Добавить выполнитель действия
     */
    fun addActionPerformer(performer: AnalyticActionPerformer<AnalyticAction>): DefaultAnalyticActionPerformerCreator {
        performers.add(performer)
        return this
    }
}