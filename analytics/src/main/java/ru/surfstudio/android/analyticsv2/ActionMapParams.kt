package ru.surfstudio.android.analyticsv2

/**
 * Параметры события типа "Map<String, String>"
 * В большинстве аналитик есть параметры события в виде набора пар ключ-значение
 */
interface ActionMapParams {
    fun params() : Map<String, String>
}