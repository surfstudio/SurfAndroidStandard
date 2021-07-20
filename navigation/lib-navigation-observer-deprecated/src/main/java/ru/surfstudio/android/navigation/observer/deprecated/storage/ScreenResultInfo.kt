package ru.surfstudio.android.navigation.observer.deprecated.storage

import java.io.Serializable

/**
 * Information about screen result with source screen id and target screen id to uniquely identify this result.
 * Used in [ScreenResultStorage].
 */
@Deprecated("Prefer using new implementation")
data class ScreenResultInfo<T : Serializable>(
        val targetId: String,
        val result: T
) : Serializable