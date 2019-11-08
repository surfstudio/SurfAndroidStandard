package ru.surfstudio.android.notification.service

/**
 * Холдер-класс со ссылкой на конкретный
 * [BaseNotificationProcessStarter]
 */
object NotificationProcessStarterHolder {

    lateinit var serviceStarter: BaseNotificationProcessStarter
}