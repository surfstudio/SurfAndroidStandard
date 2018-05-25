package ru.surfstudio.android.rx.downloader

import android.app.DownloadManager
import android.content.Context
import ru.surfstudio.android.rx.downloader.task.DownloadTask

class RxDownloader(val context: Context,
                   val downloadManager: DownloadManager,
                   val downloaderId: Int,
                   val downloadTaskStorage: DownloadTaskStorage) {

    var parallelDownloadsCount: Int = 1
        set(value) {
            //todo
        }

    fun download(task: DownloadTask) {
        val downloadId = downloadManager.enqueue(task)
        task.downloadId = downloadId
    }

    fun cancelDownload(taskId: Long) {
        //todo
    }

    /*fun observeTasksChanges(): Observable<DownloadTask> {
        //todo
    }

    fun observeTaskChanges(taskId: Long): Observable<DownloadTask> {
        //todo
    }*/

    fun setAllowedNetworkTypes(request: DownloadManager.Request) {

    }
}