package ru.surfstudio.android.firebase.sample.domain.notification

import java.io.Serializable

/**
 * Модель уведомления
 *
 * @param id уникальный идентификатор уведомления
 * @param type тип уведомления
 * @param isGrouped флаг сгруппированности уведомлений
 * @param notificationsCount количество сгруппированных уведомлений (1, если уведомление не сгруппировано)
 * @param date дата генерации уведомления
 * @param previewText сопроводительный текст к уведомлению
 */
class Notification(
        val id: Long,
        val type: NotificationType,
        val isGrouped: Boolean,
        val notificationsCount: Int,
        val date: String,
        val previewText: String
): Serializable