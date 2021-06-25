package ru.surfstudio.android.navigation.observer.deprecated.bus

import java.io.Serializable


/**
 * Information about screen result listener with source screen id and target screen id to uniquely identify this result.
 */
data class ScreenResultListenerInfo<T : Serializable>(
        val targetId: String,
        val listener: (T) -> Unit
)