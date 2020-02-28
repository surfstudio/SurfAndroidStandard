package ru.surfstudio.android.build.model.json.response.folder_info

import ru.surfstudio.android.build.model.FolderInfo
import ru.surfstudio.android.build.utils.EMPTY_STRING
import ru.surfstudio.android.build.utils.Transformable

/**
 * Represent information about folder info json object
 */
data class FolderInfoJson(
        val repo: String = EMPTY_STRING,
        val path: String = EMPTY_STRING,
        val created: String = EMPTY_STRING,
        val createdBy: String = EMPTY_STRING,
        val lastModified: String = EMPTY_STRING,
        val modifiedBy: String = EMPTY_STRING,
        val lastUpdated: String = EMPTY_STRING,
        val uri: String = EMPTY_STRING,
        val children: List<FolderInfoChildJson> = emptyList()
) : Transformable<FolderInfo> {

    override fun transform() = FolderInfo(
            repo,
            path,
            created,
            createdBy,
            lastModified,
            modifiedBy,
            lastUpdated,
            uri,
            children.map(FolderInfoChildJson::transform)
    )
}