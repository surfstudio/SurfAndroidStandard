package ru.surfstudio.android.build.model

import ru.surfstudio.android.build.utils.EMPTY_STRING

/**
 * Represent folder information
 */
data class FolderInfo(
        val repo: String = EMPTY_STRING,
        val path: String = EMPTY_STRING,
        val created: String = EMPTY_STRING,
        val createdBy: String = EMPTY_STRING,
        val lastModified: String = EMPTY_STRING,
        val modifiedBy: String = EMPTY_STRING,
        val lastUpdated: String = EMPTY_STRING,
        val uri: String = EMPTY_STRING,
        val children: List<FolderInfoChild> = emptyList()
) {

    val isEmpty get() = repo.isEmpty()
}

/**
 * Represent folders file
 */
data class FolderInfoChild(
        val uri: String = EMPTY_STRING,
        val folder: Boolean = false
)