package ru.surfstudio.android.rx.downloader

import android.net.Uri

open class DownloadTask(val taskId: Long,
                        val url: String) {

    val targetFileUri: Uri = Uri.EMPTY
    val priority: Int = 0
    val downloadId: Long = 0
    val downloadStatus = DownloadStatus.NEW
    val errorReason = DownloadErrorReason.EMPTY
    val pausedReason = DownloadPausedReason.EMPTY

}

enum class DownloadStatus {
    NEW, //первоначальный статус, не в очереди
    IN_QUEUE, //в очереди
    IN_PROGRESS, //в процессе загрузки
    PAUSED, //приостановлено
    CANCELED, //отменено
    SUCCESSFUL, //загрузка прошла успешно
    ERROR //ошибка
}

enum class DownloadErrorReason {
    EMPTY,
    SERVER_ERROR,
    NO_INTERNET
}

enum class DownloadPausedReason {
    EMPTY
    //todo
}