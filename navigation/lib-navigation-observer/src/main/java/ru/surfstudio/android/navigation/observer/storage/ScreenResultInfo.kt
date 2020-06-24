package ru.surfstudio.android.navigation.observer.storage

import java.io.Serializable

/**
 * Information about screen result with source screen id and target screen id to uniquely identify this result.
 * Used in [ScreenResultStorage].
 */
data class ScreenResultInfo<T : Serializable>(
        val targetId: String,
        val result: T
) : Serializable