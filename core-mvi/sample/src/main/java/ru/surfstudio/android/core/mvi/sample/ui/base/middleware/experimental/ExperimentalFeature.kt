package ru.surfstudio.android.core.mvi.sample.ui.base.middleware.experimental

/**
 * Аннотация для пометки элементов как экспериментальных.
 * Если элемент помечен этой аннотацией - значит он может вызвать баги в production,
 * и не рекомендуется к использованию там, где необходима стабильная работа.
 */
@Target(AnnotationTarget.CLASS)
annotation class ExperimentalFeature