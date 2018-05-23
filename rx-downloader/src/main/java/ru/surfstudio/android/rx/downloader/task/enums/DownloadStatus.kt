package ru.surfstudio.android.rx.downloader.task.enums

/**
 * Статус загрузки у [DownloadTask]
 */
enum class DownloadStatus {
    NOT_SPECIFIED, //не указан
    IN_QUEUE, //в очереди
    IN_PROGRESS, //в процессе загрузки
    PAUSED, //приостановлено
    CANCELED, //отменено
    SUCCESSFUL, //загрузка прошла успешно
    ERROR //ошибка
}
