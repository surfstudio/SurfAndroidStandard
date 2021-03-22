package ru.surfstudio.android.message

/**
 * Причина скрытия сообщения
 * @property ACTION тап по экшену
 * @property MANUAL был вызван метод [MessageController.closeSnack]
 * @property SWIPE скрыт свайпом
 * @property TIMEOUT скрыт по таймауту
 * @property CONSECUTIVE скрыт по причине отображения следующего сообщения
 */
enum class DismissReason {
    ACTION,
    MANUAL,
    SWIPE,
    TIMEOUT,
    CONSECUTIVE
}
