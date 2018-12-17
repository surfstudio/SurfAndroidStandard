package ru.surfstudio.android.analyticsv2

import ru.surfstudio.android.analytics.BuildConfig
import ru.surfstudio.android.analyticsv2.core.AnalyticAction
import ru.surfstudio.android.analyticsv2.core.AnalyticActionPerformer
import ru.surfstudio.android.analyticsv2.core.AnalyticActionPerformerFactory
import java.lang.Error

/**
 * Фабрика выполнителей действий аналитики по умолчанию
 * Сопоставляет выполнитель по классу события
 * Нужно заполнить мапу поддерживаемых действий
 */
class DefaultAnalyticActionPerformerFactory() : AnalyticActionPerformerFactory<AnalyticAction> {

    private val map: MutableMap<Class<out AnalyticAction>, AnalyticActionPerformer<out AnalyticAction>> = hashMapOf()

    override fun getPerformerByAction(event: AnalyticAction): AnalyticActionPerformer<AnalyticAction>? {
        val performer = map[event::class.java]
        if (performer == null) {
            if (BuildConfig.DEBUG) {
                throw Error("No action performer for action: ${event::class.java.canonicalName}")
            }
        }
        return performer as AnalyticActionPerformer<AnalyticAction>?
    }

    /**
     * Сопоставить действие и его выполнитель
     */
    fun <T: AnalyticAction>addActionPerformer(action: T, performer: AnalyticActionPerformer<T>): DefaultAnalyticActionPerformerFactory {
        map[action::class.java] = performer
        return this
    }
}