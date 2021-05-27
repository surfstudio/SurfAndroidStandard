package ru.surfstudio.android.navigation.observer.storage

import ru.surfstudio.android.navigation.observer.route.ActivityWithResultRoute
import java.io.Serializable

/**
 * Storage for storing route which used for start system screen for result
 */
interface RouteStorage {

    fun <T : Serializable> get(): ActivityWithResultRoute<T>?

    fun <T : Serializable> save(info: ActivityWithResultRoute<T>)

    fun hasValue(): Boolean

    fun clear()
}