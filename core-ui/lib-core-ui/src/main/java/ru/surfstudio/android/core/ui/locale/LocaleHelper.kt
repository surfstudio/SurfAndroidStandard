package ru.surfstudio.android.core.ui.locale

import android.content.Context

/**
 * Хелпер класс для локализации контекста. Используется при аттаче базового контекста к активити.
 * По умолчанию ничего не делает. Для корректной работы следует вызывать метод [init] при инициализации приложения
 */
object LocaleHelper : Localizer {

    private var localizer: Localizer? = null

    fun init(localizer: Localizer) {
        this.localizer = localizer
    }

    override fun localize(context: Context): Context {
        return localizer?.localize(context) ?: context
    }

    override fun isLocaleChanged(context: Context): Boolean {
        return localizer?.isLocaleChanged(context) ?: false
    }
}