package ru.surfstudio.android.analyticsv2.core

/**
 * Ответсвенен за выполнение действия аналитики.
 */
interface AnalyticService<Action: AnalyticAction> {
    fun performAction(action: Action)
}