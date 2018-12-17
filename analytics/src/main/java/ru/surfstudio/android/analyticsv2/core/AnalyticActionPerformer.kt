package ru.surfstudio.android.analyticsv2.core

/**
 * Осуществляет выполнение действия в аналитике
 */
interface AnalyticActionPerformer<Action: AnalyticAction> {
    fun perform(action: Action)
}