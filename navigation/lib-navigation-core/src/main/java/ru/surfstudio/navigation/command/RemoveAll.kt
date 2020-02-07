package ru.surfstudio.navigation.command

import ru.surfstudio.navigation.route.Route


/**
 * Команда удаления всех экранов из текущего стека
 */
open class RemoveAll(override val route: Route) : NavigationCommand

/**
 * Команда удаления всех Activity из текущего стека (taskAffinity)
 */
class RemoveAllActivities : RemoveAll(TODO()) {
    override fun toString(): String = "RemoveAllActivities"
}

/**
 * Команда удаления всех фрагментов из текущего стека
 */
data class RemoveAllFragments : RemoveAll(TODO())
