package ru.surfstudio.android.core.ui.locale

import android.content.Context
import androidx.core.os.ConfigurationCompat

/**
 * Интерфейс локализатора.
 */
interface Localizer {

    /**
     * Создает локазованный контекст на основе переданного
     */
    fun localize(context: Context): Context

    /**
     * Проверка изменения языка приложения
     * @return true, если локаль переданного контекста отличается от языка приложения, иначе false
     */
    fun isLocaleChanged(context: Context): Boolean

    val Context.language: String
        get() = ConfigurationCompat.getLocales(resources.configuration).get(0).language
}
