package ru.surfstudio.android.rx.downloader.task.enums

/**
 * Причина паузы загрузки у [DownloadTask]
 */
enum class DownloadPausedReason {
    NOT_SPECIFIED, //не указан
    PAUSED_QUEUED_FOR_WIFI, //лимит на мобильный трафик закончен, ожидает Wi-Fi
    PAUSED_UNKNOWN, //неизвестно
    PAUSED_WAITING_FOR_NETWORK, //ожидает интернет соединение
    PAUSED_WAITING_TO_RETRY //ожидает повтора загрузки
}