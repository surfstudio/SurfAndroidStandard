package ru.surfstudio.android.analyticsv2.core

/**
 * Задает отображение действия в аналитике и списка сущностей которые выполнят действие
 */
interface AnalyticActionPerformerCreator<Action: AnalyticAction> {
    /**
     * Возвращает выполнители, которые могут обработать действие аналитики
     */
    fun getPerformersByAction(event: Action): List<AnalyticActionPerformer<Action>>
}