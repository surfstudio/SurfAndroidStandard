package ru.surfstudio.android.rx.downloader.task

import android.app.DownloadManager
import android.net.Uri
import ru.surfstudio.android.rx.downloader.task.enums.DownloadErrorReason
import ru.surfstudio.android.rx.downloader.task.enums.DownloadPausedReason
import ru.surfstudio.android.rx.downloader.task.enums.DownloadStatus

/**
 * Сущность для загрузки файлов через интернет
 * с помощью DownloadManager'а
 * Используется в [RxDownloader]
 */
open class DownloadTask(url: String) : DownloadManager.Request(Uri.parse(url)) {

    var taskId: String = url
    var downloadUri: Uri = Uri.parse(url)
    var targetFileUri: Uri = Uri.EMPTY
    var downloadId: Long = 0

    var priority: Int = 0

    var downloadStatus = DownloadStatus.NOT_SPECIFIED
    var errorReason = DownloadErrorReason.NOT_SPECIFIED
    var pausedReason = DownloadPausedReason.NOT_SPECIFIED
}