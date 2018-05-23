package ru.surfstudio.android.rx.downloader

class DownloadTaskStorage {

    val tasks: List<DownloadTask> = emptyList()

    fun getActiveTasks(): List<DownloadTask> {
        //todo
        return emptyList()
    }

    fun getSuccessfulTasks(): List<DownloadTask> {
        //todo
        return emptyList()
    }

    fun getTask(taskId: Long): DownloadTask? {
        //todo
        return null
    }

    fun clearTasks() {

    }

    fun updateOrCreate(taskId: Long) {

    }

}