package ru.surfstudio.android.navigation.observer.route

import ru.surfstudio.android.navigation.route.Route
import java.io.Serializable

interface ResultRoute<T : Serializable> : Route {

    fun getId(): String
}