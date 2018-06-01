package ru.surfstudio.android.rx.downloader

import android.app.DownloadManager
import android.content.Context
import android.database.Observable
import ru.surfstudio.android.rx.downloader.base.Downloader
import ru.surfstudio.android.rx.downloader.base.DownloaderConfig
import ru.surfstudio.android.rx.downloader.task.DownloadTask

/**
 * Класс-обёртка над [DownloadManager],
 * инкапсулирующая всю работу с загрузками
 */
class RxDownloader(val context: Context,
                   val downloaderId: Int,
                   val downloadManager: DownloadManager,
                   val downloadTaskStorage: DownloadTaskStorageImpl,
                   override var config: DownloaderConfigImpl) : Downloader<DownloaderConfigImpl> {


    override var parallelDownloadsCount: Int = 1
        set(value) {
            //todo
        }

    override fun download(task: DownloadTask) {
        //todo
    }

    override fun cancelDownload(taskId: Long) {
        //todo
    }
    /*
    override fun observeTasksChanges(): Observable<List<DownloadTask>> {
        //todo
    }

    override fun observeTaskChanges(taskId: Long): Observable<DownloadTask> {
        //todo
    }
    */
}