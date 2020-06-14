package ru.surfstudio.android.navigation.observer.storage

import java.io.Serializable

/**
 * Storage with information about screen results that were not handled.
 *
 * This storage is designed to survive configuration changes and process death,
 * so note that in your own implementations.
 */
interface ScreenResultStorage {

    fun <T : Serializable> get(sourceId: String, targetId: String): ScreenResultInfo<T>?

    fun <T : Serializable> save(info: ScreenResultInfo<T>)

    fun remove(sourceId: String, targetId: String)

    fun contains(sourceId: String, targetId: String): Boolean

    fun clear()
}