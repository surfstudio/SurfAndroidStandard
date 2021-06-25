package ru.surfstudio.android.navigation.observer.deprecated.storage

import java.io.Serializable

/**
 * Storage with information about screen results that were not handled.
 *
 * This storage is designed to survive configuration changes and process death,
 * so note that in your own implementations.
 */
interface ScreenResultStorage {

    fun <T : Serializable> get(targetId: String): ScreenResultInfo<T>?

    fun <T : Serializable> save(info: ScreenResultInfo<T>)

    fun remove(targetId: String)

    fun contains(targetId: String): Boolean

    fun clear()
}