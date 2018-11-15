package ru.surfstudio.android.notification.ui.notification.groups

/**
 * Группа нотификаций.
 *
 * @param id уникальный идентификатор группы
 * @param groupAlias псевдоним группы
 */
data class NotificationsGroup(
        val id: Int,
        val groupAlias: String
)