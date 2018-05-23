package ru.surfstudio.android.rx.downloader

import android.app.DownloadManager
import android.content.Context
import ru.surfstudio.android.rx.downloader.task.DownloadTask

class RxDownloader(val context: Context,
                   val downloadManager: DownloadManager,
                   val downloaderId: Int) {

    var parallelDownloadsCount: Int = 1
        set(value) {
            //todo
        }

    fun download(task: DownloadTask) {
        //todo
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

}