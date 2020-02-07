package ru.surfstudio.navigation.navigator.backstack.route

import ru.surfstudio.navigation.route.Route
import java.io.Serializable

/**
 * Роут, хранящийся в бекстеке.
 *
 * Для восстановления состояния при смене конфигурации является сериализуемым.
 */
data class BackStackRoute(val tag: String) : Route, Serializable
