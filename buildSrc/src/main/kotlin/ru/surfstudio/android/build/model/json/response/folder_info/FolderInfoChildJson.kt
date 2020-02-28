package ru.surfstudio.android.build.model.json.response.folder_info

import ru.surfstudio.android.build.model.FolderInfoChild
import ru.surfstudio.android.build.utils.EMPTY_STRING
import ru.surfstudio.android.build.utils.Transformable

/**
 * Represent information about folder info's child json object
 */
data class FolderInfoChildJson(
        val uri: String = EMPTY_STRING,
        val folder: Boolean = false
) : Transformable<FolderInfoChild> {

    override fun transform() = FolderInfoChild(uri, folder)
}