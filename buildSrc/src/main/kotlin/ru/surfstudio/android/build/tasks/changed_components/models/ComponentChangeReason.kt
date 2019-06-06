package ru.surfstudio.android.build.tasks.changed_components.models

import ru.surfstudio.android.build.utils.EMPTY_STRING

/**
 * Represents reason of components change
 */
enum class ComponentChangeReason(val reason: String) {

    NO_REASON(EMPTY_STRING),
    FILE_CHANGED("files in revisions are different"),
    COMPONENT_REMOVED("No information about component in revision to compare:"),
    LIBRARIES_DIFFER("Libraries lists are different"),
    GENERAL_VALUES_DIFFER("One of the following values differ: libMinSdkVersion,targetSdkVersion," +
            "compileSdkVersion, moduleVersionCode")
}