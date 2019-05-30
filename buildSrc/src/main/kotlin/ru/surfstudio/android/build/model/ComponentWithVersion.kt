package ru.surfstudio.android.build.model

/**
 * Проедставляет информацию о компоненте c версиями библиотек
 */
class ComponentWithVersion(
        val id: String,
        val version: String,
        val isStable: Boolean,
        val libs: List<LibWithVersion> = listOf()
) {
}