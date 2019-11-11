package ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.composition

import ru.surfstudio.android.core.ui.navigation.Route

/**
 * Роут, не открывающий никакой экран, заглушка.
 */
object NoScreenRoute : Route {
    override fun toString(): String = "NoScreenRoute"
}