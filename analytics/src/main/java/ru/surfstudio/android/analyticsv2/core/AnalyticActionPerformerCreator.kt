package ru.surfstudio.android.analyticsv2.core

/**
 * Задает отображение действия в аналитике и сущности который должны ее выполнить
 */
interface AnalyticActionPerformerCreator<Action: AnalyticAction> {
    fun getPerformersByAction(event: Action): List<AnalyticActionPerformer<Action>>
}