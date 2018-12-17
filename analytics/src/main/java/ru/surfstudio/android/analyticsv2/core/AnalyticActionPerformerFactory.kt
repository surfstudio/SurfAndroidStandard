package ru.surfstudio.android.analyticsv2.core

/**
 * Задает отображение действия в аналитике в сущность ответсвенную за ее выполнение
 */
interface AnalyticActionPerformerFactory<Action : AnalyticAction> {
    fun getPerformerByAction(event: Action): AnalyticActionPerformer<Action>?
}