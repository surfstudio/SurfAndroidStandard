package ru.surfstudio.android.rx.downloader

import ru.surfstudio.android.rx.downloader.base.DownloadTaskStorage
import ru.surfstudio.android.rx.downloader.task.DownloadTask
import ru.surfstudio.android.rx.downloader.task.enums.DownloadStatus
import java.util.*

/**
 * Хранилище всех загрузок
 * todo загрузку в БД (таблица c [DownloadTask]'ами)
 */
class DownloadTaskStorageImpl : DownloadTaskStorage {

    var tasks: List<DownloadTask> = ArrayList()

    override fun updateOrCreate(task: DownloadTask) {
        //todo
    }

    override fun getAllTasks(): List<DownloadTask> = tasks

    override fun getActiveTasks(): List<DownloadTask> =
            tasks.filter { it.downloadStatus != DownloadStatus.SUCCESSFUL }

    override fun getSuccessfulTasks(): List<DownloadTask> =
            tasks.filter { it.downloadStatus == DownloadStatus.SUCCESSFUL }

    override fun getTask(taskId: String): DownloadTask? =
            tasks.find { it.taskId == taskId }

    override fun clearTasks() {
        //todo
    }

    /**
     * @return есть ли таск в очереди?
     */
    fun isTaskAdded(taskId: String): Boolean = getTask(taskId) != null

}