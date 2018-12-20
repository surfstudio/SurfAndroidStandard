package ru.surfstudio.android.analyticsv2.core

/**
 * Осуществляет выполнение действия в аналитике
 */
interface AnalyticActionPerformer<Action: AnalyticAction> {
    /**
     * Может ли выполнитель обработать действие?
     * @return true если может
     */
    fun canHandle(action: AnalyticAction) : Boolean
    /**
     * Выполнить действие
     */
    fun perform(action: Action)
}