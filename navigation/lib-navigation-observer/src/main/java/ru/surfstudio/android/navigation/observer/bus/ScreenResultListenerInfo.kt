package ru.surfstudio.android.navigation.observer.bus

import java.io.Serializable


/**
 * Information about screen result listener with source screen id and target screen id to uniquely identify this result.
 */
data class ScreenResultListenerInfo<T : Serializable>(
        val sourceId: String,
        val targetId: String,
        val listener: (T) -> Unit
)