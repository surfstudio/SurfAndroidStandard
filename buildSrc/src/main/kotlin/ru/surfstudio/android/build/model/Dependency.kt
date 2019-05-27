package ru.surfstudio.android.build.model

/**
 * Представляет информацию о зависимости
 */
data class Dependency(
        val name: String = "",
        val type: String = ""
)