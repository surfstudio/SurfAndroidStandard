package ru.surfstudio.android.rx.downloader.base

import ru.surfstudio.android.rx.downloader.task.DownloadTask

/**
 * Интерфейс для хранилища тасков
 */
interface DownloadTaskStorage {

    /**
     * обновить или создать таск по его id
     * @param taskId
     */
    fun updateOrCreate(task: DownloadTask)

    /**
     * @return список всех тасков
     */
    fun getAllTasks(): List<DownloadTask>

    /**
     * @return список активных (незавершённых) тасков
     */
    fun getActiveTasks(): List<DownloadTask>

    /**
     * @return список завершённых тасков
     */
    fun getSuccessfulTasks(): List<DownloadTask>

    /**
     * @return таск по его taskId. Может вернуться null
     */
    fun getTask(taskId: String): DownloadTask?

    /**
     * очистить все таски
     */
    fun clearTasks()
}