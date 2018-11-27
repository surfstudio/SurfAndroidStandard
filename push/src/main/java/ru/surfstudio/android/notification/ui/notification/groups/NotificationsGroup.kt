@file:Suppress("unused")

package ru.surfstudio.android.notification.ui.notification.groups

/**
 * Группа нотификаций.
 *
 * @param groupAlias псевдоним группы, должен быть уникален для каждой группы
 */
data class NotificationsGroup(
        val groupAlias: String
) {

    //уникальный идентификатор группы
    val id: Int
        get() = groupAlias.hashCode()
}