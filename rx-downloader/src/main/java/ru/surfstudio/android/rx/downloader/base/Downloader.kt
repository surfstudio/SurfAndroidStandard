package ru.surfstudio.android.rx.downloader.base

import android.database.Observable
import ru.surfstudio.android.rx.downloader.task.DownloadTask

/**
 * Интерфейс для загрузчика файлов
 */
interface Downloader<C : DownloaderConfig> {

    /**
     * кол-во возможных скачиваний одновременно
     */
    var parallelDownloadsCount: Int

    /**
     * конфигурация загрузчика со всеми настройками
     * должна реализовывать интерфейс DownloaderConfig
     */
    var config: C

    /**
     * @param запуск скачивания DownloadTask'а
     */
    fun download(task: DownloadTask)

    /**
     * отмена скачивания
     * @param taskId отменяемого DownloadTask'а
     */
    fun cancelDownload(taskId: Long)

    /**
     * наблюдение за всеми скачиваниями
     */
    //fun observeTasksChanges(): Observable<List<DownloadTask>>

    /**
     * наблюдение за конкретной загрузкой
     * @param taskId наблюдаемого DownloadTask'а
     */
    //fun observeTaskChanges(taskId: Long): Observable<DownloadTask>
}