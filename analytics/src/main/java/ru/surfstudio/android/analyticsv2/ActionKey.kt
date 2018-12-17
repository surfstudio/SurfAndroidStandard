package ru.surfstudio.android.analyticsv2

/**
 * Имя события
 * В большинстве серсвисах аналитики есть имя события
 */
interface ActionKey {
    fun key() : String
}