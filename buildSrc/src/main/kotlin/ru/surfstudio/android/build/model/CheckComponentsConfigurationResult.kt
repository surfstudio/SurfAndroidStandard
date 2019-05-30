package ru.surfstudio.android.build.model

/**
 * Проедставляет информацию о результате проверки конфигурации проекта
 */
data class CheckComponentsConfigurationResult(val isOk: Boolean, val reasonFail: String="") {
}