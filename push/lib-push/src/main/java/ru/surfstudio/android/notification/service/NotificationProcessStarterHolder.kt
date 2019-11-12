package ru.surfstudio.android.notification.service

/**
 * Холдер-класс со ссылкой на конкретный
 * [BaseNotificationProcessStarter];
 * сервис из push-модуля не знает о конкретной реализации AppComponent,
 * откуда он может взять ссылку
 */
object NotificationProcessStarterHolder {

    lateinit var serviceStarter: BaseNotificationProcessStarter
}